package io.gaurs.sbm.model.logical;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "the employee address record", implementation = EmployeeAddress.class, name = "Employee Address")
public record EmployeeAddress(@JsonProperty("houseNumber") @NotNull @Min(101) @Schema(description = "house number", implementation = Integer.class, name = "houseNumber", required = true, example = "1010") Integer houseNumber,
                              @JsonProperty("street") @NotNull @NotBlank @Schema(description = "street name", implementation = String.class, name = "street", required = true, example = "Gurdwara Street") String street,
                              @JsonProperty("city") @NotNull @NotBlank @Schema(description = "address city", implementation = String.class, name = "city", required = true, example = "Patiala") String city,
                              @NotNull @JsonProperty("pinCode") @NotBlank @Schema(description = "pin number", implementation = String.class, name = "pinCode", required = true, example = "147001") String pinCode){

}
