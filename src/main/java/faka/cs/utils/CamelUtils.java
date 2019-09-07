package faka.cs.utils;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestParamType;

import faka.cs.model.common.PathVariable;
import faka.cs.model.common.QueryParam;
import faka.cs.model.http.HttpDocument;
import faka.cs.model.http.HttpDocument.TypeEnum;
import faka.cs.service.HttpValidator;

public class CamelUtils {

	private static final String APPLICATION_JSON = "application/json";
	private static final String APPLICATION_XML = "application/xml";
	private static final String TEXT_PLAIN = "text/plain";

	public static String buildQueryParamsURL(HttpDocument document) {
		StringBuilder urlBuilder = new StringBuilder("/" + document.getUrl() + "/");
		for (int i = 0; i < document.getQueryParams().size(); i++) {

			QueryParam queryParam = document.getQueryParams().get(i);

			String paramName = queryParam.getParamName();
			if (i == 0) {
				urlBuilder.append(String.format("?%1$s={%1$s}", paramName));
			} else {
				urlBuilder.append(String.format("&%1$s={%1$s}", paramName));
			}

		}
		return urlBuilder.toString();
	}

	public static void buildRoute(RouteBuilder routeBuilder, HttpDocument httpDocument, HttpValidator httpValidator) {

		String urlBuilder = CamelUtils.buildQueryParamsURL(httpDocument);
		httpValidator.setHttpValidator(httpDocument);

		String consume = TEXT_PLAIN;
		if (httpDocument.getConsume().equals(TypeEnum.JSON)) {
			consume = APPLICATION_JSON;
		} else if (httpDocument.getConsume().equals(TypeEnum.XML)) {
			consume = APPLICATION_XML;
		}

		switch (httpDocument.getMethod()) {
		case GET:

			RestDefinition rest = routeBuilder.rest().get(urlBuilder)
					.description((httpDocument.getDescription() != null ? httpDocument.getDescription() : "")
							+ " ID MOCK: " + httpDocument.getId());

			for (QueryParam iterable_element : httpDocument.getQueryParams()) {
				rest.param().name(iterable_element.getParamName()).type(RestParamType.query).required(false)
						.dataType("string").endParam();
			}

			for (PathVariable iterable_element : httpDocument.getPathVariables()) {
				rest.param().name(iterable_element.getName()).type(RestParamType.path).dataType("string").endParam();
			}

			rest.responseMessage().message("No se encontro un caso para el request ingresado").code(404)
					.endResponseMessage().responseMessage().message("Respuesta mockeada").code(200).endResponseMessage()
					.produces(consume).route().routeId(httpDocument.getId()).bean(httpValidator, "getProcess")
					.endRest();

			break;
		case POST:
			routeBuilder.rest().post(urlBuilder)
					.description((httpDocument.getDescription() != null ? httpDocument.getDescription() : "")
							+ " ID MOCK: " + httpDocument.getId())
					.type(String.class).responseMessage().message("Respuesta mockeada").code(200).endResponseMessage()
					.responseMessage().message("No se encontro un caso para el request ingresado").code(404)
					.endResponseMessage().consumes(consume).produces(consume).route().routeId(httpDocument.getId())
					.bean(httpValidator, "postProcess").endRest();

			break;
		}
	}
}
