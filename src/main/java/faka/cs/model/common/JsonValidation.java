
package faka.cs.model.common;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "field",
    "value"
})
public class JsonValidation {

	@NotEmpty
    @JsonProperty("field")
    private String field;
	@NotEmpty
    @JsonProperty("value")
    private String value;
    
    public JsonValidation() {
	}
    
    public JsonValidation(String field, String value) {
		super();
		this.field = field;
		this.value = value;
	}

	@JsonProperty("field")
    public String getField() {
        return field;
    }

    @JsonProperty("field")
    public void setField(String field) {
        this.field = field;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }
}
