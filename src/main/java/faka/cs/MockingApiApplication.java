package faka.cs;

import javax.validation.Valid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.base.Predicates;

import faka.cs.model.HealthItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class MockingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockingApiApplication.class, args);
	}
	
	@Controller
	@Api(value = "Utils")
	class Stringify{
		
		@ApiOperation(value = "Take a text and transform it into a single line", consumes = "text/plain", produces = "text/plain")
		@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class)})
		@PostMapping("/stringify")
		public ResponseEntity<String> stringify(
				@ApiParam(value = "Response", required = true) @Valid @RequestBody String response) {
			return ResponseEntity.ok(response.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\"","\\\\\""));
		}
	}

	@Controller
	@Api(value = "Health")
	class Welcome {
		@RequestMapping("/")
		public String redirectToSwaggerUi() {
			return "redirect:/swagger-ui.html";
		}

		@GetMapping("/swagger/mocks")
		public String redirectToSwaggerMocksUi() {
			return "redirect:/webjars/swagger-ui/index.html?url=/camel/api-docs&validatorUrl=";
		}

		@GetMapping("/health")
		public ResponseEntity<HealthItem> health() {
			HealthItem body = new HealthItem();
			body.setSuccess(true);
			return ResponseEntity.ok(body);
		}

	}

	@Bean
	public Docket swaggerSpringMvcPlugin() {
		return new Docket(DocumentationType.SWAGGER_2).select().paths(Predicates.not(PathSelectors.regex("/error")))
				.paths(Predicates.not(PathSelectors.regex("/"))).paths(Predicates.not(PathSelectors.regex("/swagger/mocks"))).build();
	}
}
