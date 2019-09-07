package faka.cs.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Headers;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import faka.cs.model.common.Case;
import faka.cs.model.common.JsonValidation;
import faka.cs.model.common.QueryParamsValidation;
import faka.cs.model.common.UrlParamsValidation;
import faka.cs.model.common.XmlValidation;
import faka.cs.model.http.HttpDocument;
import faka.cs.model.http.HttpDocument.TypeEnum;

@Scope("prototype")
@Component()
public class HttpValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpValidator.class);

	private HttpDocument document;

	public void setHttpValidator(HttpDocument document) {
		this.document = document;
	}

	/**
	 * Strings conformados por primero urlParams y luego queryParams
	 * .get("/process?arg1={arg1}&arg2={arg2}")
	 * callTestService(${header.arg1},${header.arg2},${header.hdrId})
	 * .get("/{id}/{edad}").description("Find user by id").outType(User.class)
	 * callTestService(null,null)
	 * .to("bean:userService?method=getUser(${header.id})")
	 * 
	 * @param strings
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getProcess(@Headers Map headers) {

		List<Case> collect = document.getCases().stream()
				.filter(c -> c.getIsDefaultResponse() == null || !c.getIsDefaultResponse())
				.collect(Collectors.toList());
		for (Case caze : collect) {

			try {
				for (UrlParamsValidation urlValidation : caze.getUrlParamsValidations()) {

					if (Boolean.valueOf(urlValidation.getAny())) {
						continue;
					}
					if (!urlValidation.getValue()
							.equalsIgnoreCase(String.valueOf(headers.get(urlValidation.getParamName())))) {
						throw new Exception();
					}
				}

				for (QueryParamsValidation queryParamsValidations : caze.getQueryParamsValidations()) {

					if (Boolean.valueOf(queryParamsValidations.getAny())) {
						continue;
					}
					if (!queryParamsValidations.getValue()
							.equalsIgnoreCase(String.valueOf(headers.get(queryParamsValidations.getParamName())))) {
						throw new Exception();
					}
				}

			} catch (Exception e) {
				continue;
			}
			return caze.getResponse();

		}

		Optional<Case> defaultResponse = document.getCases().stream()
				.filter(c -> c.getIsDefaultResponse() != null && c.getIsDefaultResponse()).findFirst();
		if(defaultResponse.isPresent()) {
			return defaultResponse.get().getResponse();
		}
		
		String response = null; 
		if(document.getConsume().equals(TypeEnum.XML)) {
		response = String.format("<response>"
				+ "<error>NO HAY CASO PARA : { 'url' : '%s'}</error>"
				+ "<headers>%s</headers>"
				+ "</response>", document.getUrl(),headers.toString());}
		else {
			response = String.format("{ \"error\": \"NO HAY CASO PARA URL: %s\","
					+ "\"request\":\"%s\"}", document.getUrl(),headers.toString());			
		}
		LOGGER.info(response);

		headers.put(Exchange.HTTP_RESPONSE_CODE, "404");
		return response;

	}

	/**
	 * Body y queryParams
	 * 
	 * @param body
	 * @param strings
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String postProcess(@Body String body, @Headers Map headers) throws Exception {

		for (Case caze : document.getCases()) {

			try {
				for (UrlParamsValidation urlValidation : caze.getUrlParamsValidations()) {

					if (Boolean.valueOf(urlValidation.getAny())) {
						continue;
					}
					if (!urlValidation.getValue()
							.equalsIgnoreCase(String.valueOf(headers.get(urlValidation.getParamName())))) {
						throw new Exception();
					}
				}

				if (document.getConsume().equals(TypeEnum.JSON)) {

					for (JsonValidation jsonValidation : caze.getJsonValidations()) {
						if (!jsonValidation.getValue()
								.equalsIgnoreCase(getJsonObject(jsonValidation.getField(), body))) {
							throw new Exception();
						}
					}
				} else {
					for (XmlValidation xmlValidation : caze.getXmlValidations()) {
						if (!body.contains(xmlValidation.getValue())) {
							throw new Exception();
						}
					}
				}

			} catch (Exception e) {
				continue;
			}

			if (caze.getResponseSatusCode() != null) {
				headers.put(Exchange.HTTP_RESPONSE_CODE, caze.getResponseSatusCode());
			}

			return caze.getResponse();

		}
		String response = null; 
		if(document.getConsume().equals(TypeEnum.XML)) {
		response = String.format("<response>"
				+ "<error>NO HAY CASO PARA : { 'url' : '%s'}</error>"
				+ "<request>%s</request>"
				+ "</response>", document.getUrl(), body);}
		else {
			response = String.format("{ \"error\": \"NO HAY CASO PARA URL: %s\","
					+ "\"request\":\"%s\"}", document.getUrl(), body);			
		}
		LOGGER.info(response);

		headers.put(Exchange.HTTP_RESPONSE_CODE, "404");
		return response;
	}

	public String getJsonObject(String path, String jsonObjectString) throws Exception {
		String[] split = path.split("\\.");
		JSONObject jsonObject = new JSONObject(jsonObjectString);

		for (String key : split) {

			try {
				jsonObject = jsonObject.getJSONObject(key);
			} catch (Exception e) {
			}
			try {
				return jsonObject.getString(key);
			} catch (Exception e) {
			}

		}

		throw new Exception();
	}
}
