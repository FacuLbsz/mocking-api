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
    "name"
})
public class PathVariable {

    @JsonProperty("name")
    private String name;

    public PathVariable() {
	}
    
    public PathVariable(String name, Map<String, Object> additionalProperties) {
		super();
		this.name = name;
	}

	@JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

}
