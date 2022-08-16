package io.gaurs.sbm.itests

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.JsonObject
import io.gaurs.sbm.model.logical.Employee
import io.gaurs.sbm.model.logical.EmployeeAddress
import io.gaurs.sbm.model.physical.EmployeePhysical
import io.gaurs.sbm.repositories.EmployeeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Specification

/**
 * <code>@SpringBootTest</code>: denotes integration test and directs SB to load complete app context
 * <code>@Testcontainers</code>: inject test containers
 * <code>@AutoConfigureMockMvc</code>: auto configure a dummy mvc instance
 * <code>@TestPropertySource</code> disable the in-memory database
 *
 * @author gaurs
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
class SbMongoIntSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    def baseurl = "/employee"
    def objectMapper = new ObjectMapper()

    @Autowired
    private EmployeeRepository employeeRepository

    // do not restart between test cases
    // so make it shared among test cases
    final static mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"))

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl)
    }

    // run before the first feature method
    def setupSpec() {
        mongoDBContainer.start()
    }

    // run before every feature method
    def setup(){
        employeeRepository.deleteAll();
    }

    def "test saving an employee for a valid response"() {
        given: "context is loaded"
        and: "a valid employee record"
        def employee = createSampleRequestPayload()

        when: "end point is hit"
        def response = mockMvc.perform(MockMvcRequestBuilders.post(baseurl)
                .content(employee.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().response

        then: "created response should be returned"
        response.status == HttpStatus.CREATED.value()
        and: "values are correctly mapped"
        def jsonNode = objectMapper.readTree(response.getContentAsString())
        jsonNode.get("firstName").asText() == "fName"
        jsonNode.get("lastName").asText() == "lName"
        and: "a new valid id is generated and is assigned to saved record"
        jsonNode.get("id") != null
    }

    def "test fetching an employee for a valid response"() {
        given: "context is loaded"
        and: "a valid employee record"
        def employee = new Employee(null, "Sumit", "Gaur", new EmployeeAddress(221, "Baker Street", "London", "10001"))
        def savedEmployee = employeeRepository.save(new EmployeePhysical(employee))

        when: "end point is hit"
        def response = mockMvc.perform(MockMvcRequestBuilders.get(baseurl + "/" + savedEmployee.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().response

        then: "ok response should be returned"
        response.status == HttpStatus.OK.value()
        and: "values are correctly mapped"
        def jsonNode = objectMapper.readTree(response.getContentAsString())
        jsonNode.get("firstName").asText() == "Sumit"
        jsonNode.get("lastName").asText() == "Gaur"
        and: "a new valid id is generated and is assigned to saved record"
        jsonNode.get("id").asLong() == savedEmployee.getId().longValue()
    }

    private static String createSampleRequestPayload() {
        def address = new JsonObject()
        address.addProperty("houseNumber", 101)
        address.addProperty("street", "some-street")
        address.addProperty("city", "some-city")
        address.addProperty("pinCode", "code")

        def employee = new JsonObject()
        employee.addProperty("firstName", "fName")
        employee.addProperty("lastName", "lName")
        employee.add("address", address)

        return employee.toString()
    }
}