package io.gaurs.sbm.endpoints;

import io.gaurs.sbm.model.logical.Employee;
import io.gaurs.sbm.services.EmployeePersistenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

/**
 * The public endpoint used to perform crud operations on {@link Employee} resource.
 *
 * @author gaurs
 */
@RestController
@Tag(name = "employee", description = "the employee API")
public class EmployeeController{

    private final EmployeePersistenceService employeePersistenceService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public EmployeeController(EmployeePersistenceService employeePersistenceService){
        this.employeePersistenceService = employeePersistenceService;
    }

    @PostMapping("/employee")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(operationId = "save", summary = "persist a new employee record", tags = { "employee" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "record created successfully", content = @Content(schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "500", description = "something bad happened. please contact service provider", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<Employee> save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "employee record to be added", required = true, content = @Content(schema=@Schema(implementation = Employee.class)))
            @RequestBody Employee employee){
        logger.info("request received to save employee: [{}]", employee);

        try{
            Employee persisted = employeePersistenceService.save(employee);
            return ResponseEntity.status(HttpStatus.CREATED).body(persisted);
        } catch(Exception exception){
            logger.error("exception occurred while persisting employee record", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/employee/{id}")
    @Operation(operationId = "get", summary = "fetch an employee record", tags = { "employee" }, parameters = { @Parameter(in = ParameterIn.PATH, name = "id", description = "employee id to uniquely identify an employee", required = true) })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "record found successfully", content = @Content(schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "204", description = "no record found", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "something bad happened. please contact service provider", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<Employee> get(@PathVariable BigInteger id){
        try{
            Employee employee = employeePersistenceService.getEmployeeById(id);
            if(null != employee){
                return ResponseEntity.status(HttpStatus.OK).body(employee);
            } else{
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

        } catch(Exception exception){
            logger.error("exception occurred while fetching employee record", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
