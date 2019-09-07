
package faka.cs.model.common;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author L0672602
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "param_name"
})
public class QueryParam {

    @JsonProperty("param_name")
    private String paramName;

    public QueryParam() {
	}
    
    public QueryParam(String paramName, Map<String, Object> additionalProperties) {
		super();
		this.paramName = paramName;
	}

	@JsonProperty("param_name")
    public String getParamName() {
        return paramName;
    }

    @JsonProperty("param_name")
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

}
