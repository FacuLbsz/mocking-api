package faka.cs.service;

import javax.annotation.PostConstruct;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import faka.cs.model.http.HttpDocument;
import faka.cs.repository.HttpDocumentRepository;
import faka.cs.route.DynamicHttpDocumentRouteBuilder;
import faka.cs.route.HttpDocumentRouteException;
import faka.cs.utils.CamelUtils;

@Component
public class ServiceHttpCamelComponentRouteBuilder extends RouteBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceHttpCamelComponentRouteBuilder.class);

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private HttpDocumentRepository httpDocumentRepository;

	@Override
	public void configure() {
		restConfiguration().contextPath("/").bindingMode(RestBindingMode.off);
		
		
		restConfiguration()
         .apiContextPath("/api-docs")
         .apiProperty("api.title", "ms.j.lisa-mocks")
         .apiProperty("api.description", "Aplicacion de mocks")
         .apiProperty("api.version", "1.0.0")
         .apiProperty("cors", "true")
         .apiProperty("base.path", "/camel/")
         .apiProperty("api.path", "/")
         .apiProperty("host", "")
         .apiContextRouteId("doc-api")
         .contextPath("/camel/")
         .component("servlet");
		
	}

	@PostConstruct
	public void setup() {
		for (HttpDocument httpDocument : httpDocumentRepository.findAll()) {
			try {
				createEndpoint(httpDocument);
			} catch (HttpDocumentRouteException e) {
				LOGGER.error("Ocurrio un error al intentar crear la ruta : " + httpDocument.getId(), e);
			}
		}
	}

	private void createEndpoint(HttpDocument httpDocument) throws HttpDocumentRouteException {
		try {
			CamelUtils.buildRoute(this, httpDocument, context.getBean(HttpValidator.class));
		} catch (Exception e) {
			throw new HttpDocumentRouteException(e);
		}
	}
	
	public void createDynamicEndpoint(HttpDocument httpDocument) throws HttpDocumentRouteException {
		try {
			this.getContext().addRoutes(new DynamicHttpDocumentRouteBuilder(this.getContext(), context.getBean(HttpValidator.class), httpDocument));
		} catch (Exception e) {
			throw new HttpDocumentRouteException(e);
		}
	}

	public void deleteEndpoint(String routeId) throws HttpDocumentRouteException {
		try {
			this.getContext().stopRoute(routeId);
			this.getContext().removeRoute(routeId);
		} catch (Exception e) {
			throw new HttpDocumentRouteException(e);
		}
	}

	public void updateEndpoint(HttpDocument httpDocument) throws HttpDocumentRouteException {
		try {
			this.deleteEndpoint(httpDocument.getId());
		} catch (Exception e) {
			throw new HttpDocumentRouteException(e);
		}
		createDynamicEndpoint(httpDocument);
	}

}
