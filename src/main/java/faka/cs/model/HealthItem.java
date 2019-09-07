package faka.cs.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class HealthItem {
	@JsonProperty("success")
	private Boolean success = null;

	public HealthItem success(Boolean success) {
		this.success = success;
		return this;
	}

	/**
	 * Get success
	 * 
	 * @return success
	 **/
	@ApiModelProperty(example = "true", value = "")

	public Boolean isSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		HealthItem healthItem = (HealthItem) o;
		return Objects.equals(this.success, healthItem.success);
	}

	@Override
	public int hashCode() {
		return Objects.hash(success);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class HealthItem {\n");

		sb.append("    success: ").append(toIndentedString(success)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}