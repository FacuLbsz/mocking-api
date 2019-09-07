
package faka.cs.model.common;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "param_name", "value" })
public class QueryParamsValidation {

	@NotEmpty
	@JsonProperty("param_name")
	private String paramName;
	@JsonProperty("value")
	private String value;
	@JsonProperty("any")
	private String any;
	
	public QueryParamsValidation() {
	}

	public QueryParamsValidation(String paramName, String value, String any) {
		super();
		this.paramName = paramName;
		this.value = value;
		this.any = any;
	}

	@JsonProperty("param_name")
	public String getParamName() {
		return paramName;
	}

	@JsonProperty("param_name")
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	@JsonProperty("value")
	public String getValue() {
		return value;
	}

	@JsonProperty("value")
	public void setValue(String value) {
		this.value = value;
	}

	@JsonProperty("any")
	public String getAny() {
		return any;
	}

	@JsonProperty("any")
	public void setAny(String any) {
		this.any = any;
	}

}
