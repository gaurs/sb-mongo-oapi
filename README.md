# Springboot, MongoDB and OpenAPI

A sample springboot application with MongoDB as backend created to demonstrate the usage of [OpenApi](https://swagger.io/specification/). In short a weekend well spent :)

## MongoDB setup

The following command will start a mongo database with some pre-configured values:

1. **username**: root
2. **password**: password
3. **database**: employee
4. **port**: 27017

```bash
# start mongodb container
docker run --name mongodb -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=password -e MONGO_INITDB_DATABASE=employee -d mongo:latest
```

Once this is done, the database should be available at the following location: `mongodb://localhost:27017/employee`

## Connection properties

```yaml
spring:
  application:
    name: SpringBootMongoDBDemo
  # database connectivity options
  data:
    mongodb:
      port: 27017
      host: localhost
      authentication-database: admin
      username: root
      password: password
      database: employee

# app port
server:
  port: 8080
```

## Packages

1. **endpoints**: the public endpoints marked with `@RestController` annotation.
2. **models**:
   1. **logical**: the published logical models.
   2. **physical**: actual physical models.
3. **repositories**: MongoRepositories to perform crud operations.
4. **services**: a collection of classes to abstract business logical from the other layers.

## To be noted

1. The `id` in `Employee` and `EmployeePhysical` is marked as `BigInteger` as mongo does not auto generate an id of type `Long`. Refer to [this](https://stackoverflow.com/questions/26574409/spring-data-mongodb-generating-ids-error) link for details.
2. If the `id` is populated in the request body, the same will be used. Otherwise, an autogenerated value will be assigned.
3. The fields in the `record` classes like `Employee` and `EmployeeAddress` that are published via public endpoints are marked with `@JsonProperty` to allow serialization and deserialization.
4. Both `EmployeePhysical` and `EmployeeAddressPhysical` have their parameterized constructors marked with `@PersistenceConstructor` to allow  
   **spring-data-mongodb** to do correct mappings. If not present, the following exception is thrown:

```text
org.springframework.data.mapping.model.MappingInstantiationException: Failed to instantiate io.gaurs.sbm.model.physical.EmployeePhysical using constructor NO_CONSTRUCTOR with arguments 
...
...
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [io.gaurs.sbm.model.physical.EmployeePhysical]: No default constructor found; nested exception is java.lang.NoSuchMethodException: io.gaurs.sbm.model.physical.EmployeePhysical.<init>()
```

## Open API Documentation

When [migrating](https://springdoc.org/#migrating-from-springfox) from **SpringFox**, the following mappings are really helpful when re-writing your annotations:

```textmate
    @Api → @Tag
    @ApiIgnore → @Parameter(hidden = true) or @Operation(hidden = true) or @Hidden
    @ApiImplicitParam → @Parameter
    @ApiImplicitParams → @Parameters
    @ApiModel → @Schema
    @ApiModelProperty(hidden = true) → @Schema(accessMode = READ_ONLY)
    @ApiModelProperty → @Schema
    @ApiOperation(value = "foo", notes = "bar") → @Operation(summary = "foo", description = "bar")
    @ApiParam → @Parameter
    @ApiResponse(code = 404, message = "foo") → @ApiResponse(responseCode = "404", description = "foo")
```

When our model includes **JSR-303** bean validation annotations, such as `@NotNull`, `@NotBlank`, `@Size`, `@Min`, and `@Max`, the **springdoc-openapi** library uses them to generate additional schema documentation for the corresponding constraints

![constraints](assets/constraints.png "Constraints")

Once the application is running, the open api documentation can be accessed at: `http://localhost:8080/v3/api-docs/`. The api playground is also 
available to be explored at: `localhost:8080/swagger-ui.html`

![documentation](assets/img.png "Documentation")

## Coverage

The code coverage is idol for the endpoints and service packages. The model lags behind due to the instances being mocked for test purpose

![codecov](assets/codecov.png "Code Coverage")

## To Do

The following tasks are pending and will be available in coming weeks:

1. Enable Streaming using webflux
2. Integrate Spring Security

## Feedback

Please drop an [email](mailto:sumit@gaurs.io) note in case you have any feedback.