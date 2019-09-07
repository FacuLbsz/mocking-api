
package faka.cs.model.common;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "value"
})
public class XmlValidation {

	@NotEmpty
    @JsonProperty("value")
    private String value;
    
    public XmlValidation() {
	}

    public XmlValidation(String value) {
		super();
		this.value = value;
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
