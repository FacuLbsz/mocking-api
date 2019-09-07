package faka.cs.model.http;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

import faka.cs.model.common.Case;
import faka.cs.model.common.PathVariable;
import faka.cs.model.common.QueryParam;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "method", "consume", "url", "query_params", "path_variables", "cases" })
@Document
public class HttpDocument {
	
	/**
	 * Gets or Sets type
	 */
	public enum TypeEnum {

		XML("XML"),

		JSON("JSON");

		@NotEmpty
		private String value;

		TypeEnum(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static TypeEnum fromValue(String text) {
			for (TypeEnum b : TypeEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			throw new IllegalArgumentException("Valor no esperado '" + text + "', solo se permite XML y JSON");
		}
	}
	
	/**
	 * Gets or Sets type
	 */
	public enum MethodTypeEnum {

		POST("POST"),

		GET("GET");

		@NotEmpty
		private String value;

		MethodTypeEnum(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static MethodTypeEnum fromValue(String text) {
			for (MethodTypeEnum b : MethodTypeEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			throw new IllegalArgumentException("Valor no esperado '" + text + "', solo se permite POST y GET");
		}
	}

	@Id
	private String id;
	@Valid
	@NotNull
	@JsonProperty("method")
	private MethodTypeEnum method;
	@Valid
	@NotNull
	@JsonProperty("consume")
	private TypeEnum consume;
	@NotEmpty
	@JsonProperty("url")
	private String url;
	@JsonProperty("query_params")
	private List<QueryParam> queryParams = new ArrayList<>();
	@JsonProperty("path_variables")
	private List<PathVariable> pathVariables = new ArrayList<>();
	@JsonProperty("cases")
	@NotEmpty
	@Valid
	private List<Case> cases = null;
	@NotEmpty
	@JsonProperty("description")
    private String description;

	@JsonProperty("method")
	public MethodTypeEnum getMethod() {
		return method;
	}

	@JsonProperty("method")
	public void setMethod(MethodTypeEnum method) {
		this.method = method;
	}

	@JsonProperty("url")
	public String getUrl() {
		return url;
	}

	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}
	
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("consume")
	public TypeEnum getConsume() {
		return consume;
	}

	@JsonProperty("consume")
	public void setConsume(TypeEnum consume) {
		this.consume = consume;
	}

	@JsonProperty("query_params")
	public List<QueryParam> getQueryParams() {
		return queryParams;
	}

	@JsonProperty("query_params")
	public void setQueryParams(List<QueryParam> queryParams) {
		this.queryParams = queryParams;
	}
	
	@JsonProperty("path_variables")
	public List<PathVariable> getPathVariables() {
		return pathVariables;
	}

	@JsonProperty("path_variables")
	public void setPathVariables(List<PathVariable> pathVariables) {
		this.pathVariables = pathVariables;
	}

	@JsonProperty("cases")
	public List<Case> getCases() {
		return cases;
	}

	@JsonProperty("cases")
	public void setCases(List<Case> cases) {
		this.cases = cases;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
