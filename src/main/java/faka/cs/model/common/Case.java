
package faka.cs.model.common;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"id",
	"title",
	"description",
    "json_validations",
    "xml_validations",
    "query_params_validations",
    "url_params_validations",
    "response",
    "response_status_code",
    "default_response"
})
@Document
public class Case {

	@Id
	@JsonProperty("id")
    private String _id;
	@JsonProperty("title")
    private String title;
	@JsonProperty("description")
    private String description;
	@Valid
    @JsonProperty("json_validations")
    private List<JsonValidation> jsonValidations = null;
    @Valid
    @JsonProperty("xml_validations")
    private List<XmlValidation> xmlValidations = null;
    @Valid
    @JsonProperty("query_params_validations")
    private List<QueryParamsValidation> queryParamsValidations = null;
    @Valid
    @JsonProperty("url_params_validations")
    private List<UrlParamsValidation> urlParamsValidations = null;
    @JsonProperty("response")
    @NotEmpty
    private String response;
    @JsonProperty("response_status_code")
    private String responseSatusCode;
    
    @JsonProperty("default_response")
    private Boolean isDefaultResponse;
    
    public Case() {
    	_id = new ObjectId().toHexString();
    	this.jsonValidations = new ArrayList<>();
    	this.xmlValidations = new ArrayList<>();
    	this.queryParamsValidations = new ArrayList<>();
    	this.urlParamsValidations = new ArrayList<>();
	}
    
    @JsonProperty("id")
    public String get_id() {
		return _id;
	}

    @JsonProperty("id")
	public void set_id(String _id) {
		this._id = _id;
	}

	@JsonProperty("query_params_validations")
    public List<QueryParamsValidation> getQueryParamsValidations() {
        return queryParamsValidations;
    }

    @JsonProperty("query_params_validations")
    public void setQueryParamsValidations(List<QueryParamsValidation> queryParamsValidations) {
        this.queryParamsValidations = queryParamsValidations;
    }

    @JsonProperty("json_validations")
    public List<JsonValidation> getJsonValidations() {
        return jsonValidations;
    }

    @JsonProperty("json_validations")
    public void setJsonValidations(List<JsonValidation> jsonValidations) {
        this.jsonValidations = jsonValidations;
    }

    @JsonProperty("xml_validations")
    public List<XmlValidation> getXmlValidations() {
        return xmlValidations;
    }

    @JsonProperty("xml_validations")
    public void setXmlValidations(List<XmlValidation> xmlValidations) {
        this.xmlValidations = xmlValidations;
    }

    @JsonProperty("url_params_validations")
    public List<UrlParamsValidation> getUrlParamsValidations() {
        return urlParamsValidations;
    }

    @JsonProperty("url_params_validations")
    public void setUrlParamsValidations(List<UrlParamsValidation> urlParamsValidations) {
        this.urlParamsValidations = urlParamsValidations;
    }

    @JsonProperty("response")
    public String getResponse() {
        return response;
    }

    @JsonProperty("response")
    public void setResponse(String response) {
        this.response = response;
    }

    @JsonProperty("response_status_code")
	public String getResponseSatusCode() {
		return responseSatusCode;
	}

    @JsonProperty("response_status_code")
	public void setResponseSatusCode(String responseSatusCode) {
		this.responseSatusCode = responseSatusCode;
	}

    @JsonProperty("title")
	public String getTitle() {
		return title;
	}

    @JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

    @JsonProperty("description")
	public String getDescription() {
		return description;
	}

    @JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsDefaultResponse() {
		return isDefaultResponse;
	}

	public void setIsDefaultResponse(Boolean isDefaultResponse) {
		this.isDefaultResponse = isDefaultResponse;
	}

}
