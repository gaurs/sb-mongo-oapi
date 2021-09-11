package io.gaurs.sbm.model.logical;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

/**
 * The published employee record. This is different from
 * {@link io.gaurs.sbm.model.physical.EmployeePhysical} to abstract consumers
 * from internal physical models.
 *
 * @author gaurs
 */
@Schema
public record Employee(@JsonProperty("id") @Schema(description = "employee id - unique identifier", implementation = Integer.class, name = "id", example = "101") BigInteger id,
                       @JsonProperty("firstName") @NotNull @NotBlank @Schema(description = "employee first name", implementation = String.class, name = "firstName", required = true, example = "Sumit") String firstName,
                       @JsonProperty("lastName") @NotNull @NotBlank @Schema(description = "employee last name", implementation = String.class, name = "lastName", required = true, example = "Gaur")String lastName,
                       @JsonProperty("address") @NotNull @Schema(description = "employee last name", implementation = EmployeeAddress.class, name = "address", required = true) EmployeeAddress address){

}
