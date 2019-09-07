package faka.cs.route.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import faka.cs.model.common.Case;
import faka.cs.model.http.HttpDocument;
import faka.cs.repository.HttpDocumentRepository;
import faka.cs.route.HttpDocumentRouteException;
import faka.cs.service.ServiceHttpCamelComponentRouteBuilder;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/mock")
public class HttpDocumentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpDocumentController.class);

	@Autowired
	private HttpDocumentRepository httpDocumentRepository;

	@Autowired
	private ServiceHttpCamelComponentRouteBuilder httpCamelComponentRouteBuilder;

	final private SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");

	final private ObjectMapper mapper = new ObjectMapper();

	@ApiOperation(value = "Get all mocks", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Mocks", response = HttpDocument.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Internal ERROR") })
	@GetMapping
	public ResponseEntity<List<HttpDocument>> get() {
		return ResponseEntity.ok(httpDocumentRepository.findAll());
	}

	@ApiOperation(value = "Get a mock response by ID", consumes = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Mock", response = HttpDocument.class),
			@ApiResponse(code = 400, message = "invalid input, object invalid") })
	@GetMapping("/{id}")
	public ResponseEntity<HttpDocument> getById(
			@ApiParam(name = "id", value = "id", required = true) @PathVariable String id) {
		Optional<HttpDocument> findById = httpDocumentRepository.findById(id);

		return findById.isPresent() ? ResponseEntity.ok(findById.get()) : ResponseEntity.notFound().build();
	}

	@ApiOperation(value = "Delete a mock response by ID", consumes = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Mock removed"),
			@ApiResponse(code = 400, message = "invalid input, object invalid") })
	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteById(
			@ApiParam(name = "id", value = "id", required = true) @PathVariable String id) {

		Optional<HttpDocument> findById = httpDocumentRepository.findById(id);

		if (!findById.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		httpDocumentRepository.deleteById(id);

		try {
			httpCamelComponentRouteBuilder.deleteEndpoint(id);
		} catch (HttpDocumentRouteException e) {
			LOGGER.error("Ocurrio un error al intentar eliminar la ruta : " + id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "Add a new mock Response", consumes = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Mock created", response = HttpDocument.class),
			@ApiResponse(code = 400, message = "Already exists a mock with the same URL and Method") })
	@PostMapping
	public ResponseEntity<HttpDocument> add(
			@ApiParam(value = "Mock to add", required = true) @Valid @RequestBody HttpDocument httpDocument) {

		formatUrl(httpDocument);

		if (httpDocumentRepository.findByUrlAndMethod(httpDocument.getUrl(),
				httpDocument.getMethod().getValue()) != null) {
			return ResponseEntity.badRequest().build();
		}

		httpDocumentRepository.save(httpDocument);

		try {
			httpCamelComponentRouteBuilder.createDynamicEndpoint(httpDocument);
		} catch (HttpDocumentRouteException e) {
			LOGGER.error("Ocurrio un error al intentar crear la ruta : " + httpDocument.getId(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		saveBackup();
		return ResponseEntity.ok(httpDocument);
	}

	private void formatUrl(HttpDocument httpDocument) {
		if (httpDocument.getUrl().startsWith("/")) {
			httpDocument.setUrl(httpDocument.getUrl().replaceFirst("/", ""));
		}
		if (httpDocument.getUrl().endsWith("/")) {
			httpDocument.setUrl(httpDocument.getUrl().substring(0, httpDocument.getUrl().length() - 1));
		}
	}

	@ApiOperation(value = "Get a specific case by ID of a particular Mock", consumes = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Case by id", response = Case.class),
			@ApiResponse(code = 400, message = "Mock or Case doesnt exist") })
	@GetMapping("/{id}/case/{idCase}")
	public ResponseEntity<Case> getCase(
			@ApiParam(value = "Mock ID", name = "id", required = true) @NotEmpty @PathVariable String id,
			@ApiParam(value = "Case ID", required = true, name = "idCase") @NotEmpty @PathVariable String idCase) {

		Optional<HttpDocument> findById = httpDocumentRepository.findById(id);

		if (!findById.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Optional<Case> findFirst = findById.get().getCases().stream().filter(c -> c.get_id().equalsIgnoreCase(idCase))
				.findFirst();

		if (!findFirst.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(findFirst.get());
	}

	@ApiOperation(value = "Update an existing CASE", consumes = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Case updated", response = Case.class),
			@ApiResponse(code = 400, message = "Mock or Case not found") })
	@PostMapping("/{id}/case/{idCase}")
	public ResponseEntity<Case> updateCase(
			@ApiParam(value = "Case updated", required = true) @Valid @RequestBody Case caze,
			@ApiParam(name = "id", value = "Mock ID", required = true) @NotEmpty @PathVariable String id,
			@ApiParam(name = "idCase", value = "Case ID to update", required = true) @NotEmpty @PathVariable String idCase) {

		Optional<HttpDocument> findById = httpDocumentRepository.findById(id);

		if (!findById.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Optional<Case> findFirst = findById.get().getCases().stream().filter(c -> c.get_id().equalsIgnoreCase(idCase))
				.findFirst();

		if (!findFirst.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		findById.get().getCases().removeIf(c -> c.get_id().equalsIgnoreCase(idCase));
		caze.set_id(idCase);
		findById.get().getCases().add(caze);

		httpDocumentRepository.save(findById.get());

		try {
			httpCamelComponentRouteBuilder.updateEndpoint(findById.get());
		} catch (HttpDocumentRouteException e) {
			LOGGER.error("Ocurrio un error al intentar actualizar la ruta : " + findById.get().getId(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		saveBackup();
		return ResponseEntity.ok(caze);
	}

	@ApiOperation(value = "Add new case to an existing mock", consumes = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "new case", response = Case.class),
			@ApiResponse(code = 400, message = "Mock not found") })
	@PostMapping("/{id}/case")
	public ResponseEntity<Case> addCase(
			@ApiParam(value = "Mock ID", name = "id", required = true) @NotEmpty @PathVariable String id,
			@ApiParam(value = "New Case", required = true) @Valid @RequestBody Case caze) {

		Optional<HttpDocument> findById = httpDocumentRepository.findById(id);

		if (!findById.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		HttpDocument httpDocument = findById.get();
		httpDocument.getCases().add(caze);

		httpDocumentRepository.save(httpDocument);

		try {
			httpCamelComponentRouteBuilder.updateEndpoint(httpDocument);
		} catch (HttpDocumentRouteException e) {
			LOGGER.error("Ocurrio un error al intentar actualizar la ruta : " + httpDocument.getId(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		saveBackup();
		return ResponseEntity.ok(caze);
	}

	@ApiOperation(value = "Delete a case by id from particular mock.", consumes = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Case deleted"),
			@ApiResponse(code = 400, message = "Mock or Case not found") })
	@DeleteMapping("/{id}/case/{idCase}")
	public ResponseEntity<HttpStatus> deleteCase(
			@ApiParam(name = "id", value = "Mock ID", required = true) @NotEmpty @PathVariable String id,
			@ApiParam(name = "idCase", value = "Case ID to delete", required = true) @NotEmpty @PathVariable String idCase) {

		Optional<HttpDocument> findById = httpDocumentRepository.findById(id);

		if (!findById.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		HttpDocument httpDocument = findById.get();
		httpDocument.getCases().removeIf(c -> c.get_id().toString().equalsIgnoreCase(idCase));

		httpDocumentRepository.save(httpDocument);

		try {
			httpCamelComponentRouteBuilder.updateEndpoint(httpDocument);
		} catch (HttpDocumentRouteException e) {
			LOGGER.error("Ocurrio un error al intentar actualizar la ruta : " + httpDocument.getId(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.ok().build();
	}

	private void saveBackup() {

		List<HttpDocument> findAll = httpDocumentRepository.findAll();
		try {
			mapper.writeValue(new File(getfileNameByDate()), findAll);
		} catch (JsonGenerationException e) {
			LOGGER.error("Error generando json", e);
		} catch (JsonMappingException e) {
			LOGGER.error("Error mapeando json", e);
		} catch (IOException e) {
			LOGGER.error("Error escribiendo archivo", e);
		}

	}

	private String getfileNameByDate() {
		return "backup_" + format.format(new Date()) + ".json";
	}

}
