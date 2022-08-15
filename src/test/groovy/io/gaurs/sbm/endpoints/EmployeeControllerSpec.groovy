package io.gaurs.sbm.endpoints

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.JsonObject
import io.gaurs.sbm.MockBeanConfig
import io.gaurs.sbm.model.logical.Employee
import io.gaurs.sbm.model.logical.EmployeeAddress
import io.gaurs.sbm.services.EmployeePersistenceService
import org.hamcrest.CoreMatchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@ActiveProfiles(profiles = ["TEST"])
@WebMvcTest(controllers = [EmployeeController])
@ContextConfiguration(classes = MockBeanConfig.class)
class EmployeeControllerSpec extends Specification {

    @Autowired
    MockMvc mvc

    @Autowired
    EmployeePersistenceService employeePersistenceService

    def baseurl = "/employee"
    def objectMapper = new ObjectMapper()

    def "test saving an employee for a valid response"() {
        given: "context is loaded"
        // return the same instance as received
        employeePersistenceService.save(_ as Employee) >> { Employee employee -> employee }
        and: "a valid employee record"
        def employee = createSampleRequestPayload()

        when: "end point is hit"
        def response = mvc.perform(MockMvcRequestBuilders.post(baseurl)
                .content(employee.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().response

        then: "created response should be returned"
        // the argument should match with the parameter
        1* employeePersistenceService.save(_) >> {
            def argument = it[0]
            assert argument instanceof Employee
            assert argument.firstName() == "fName"
            assert argument.lastName() == "lName"
        }
        and: "created status should be returned"
        response.status == HttpStatus.CREATED.value()
    }

    def "test error response while saving an employee"(){
        given: "context is loaded"
        employeePersistenceService.save(_ as Employee) >> {{ throw new RuntimeException("oops, exception thrown for testing")}}
        and: "a valid employee record"
        def employee = createSampleRequestPayload()

        when: "end point is hit"
        def response = mvc.perform(MockMvcRequestBuilders.post(baseurl)
                .content(employee.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().response

        then: "error code should be returned"
        response.status == HttpStatus.INTERNAL_SERVER_ERROR.value()
    }

    def "test fetching an employee using json path"(){
        given: "context is loaded"
        def employee = createSampleEmployee()
        employeePersistenceService.getEmployeeById(_ as BigInteger) >> employee

        when: "an employee is fetched"
        def resultAction = mvc.perform(MockMvcRequestBuilders.get(baseurl + "/101").accept(MediaType.APPLICATION_JSON))

        then: "correct response should be returned"
        resultAction.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$.firstName", CoreMatchers.is(employee.firstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.lastName", CoreMatchers.is(employee.lastName())))
    }

    def "test fetching an employee using object mapper"(){
        given: "context is loaded"
        def employee = createSampleEmployee()
        employeePersistenceService.getEmployeeById(_ as BigInteger) >> employee

        when: "an employee is fetched"
        def resp = mvc.perform(MockMvcRequestBuilders.get(baseurl + "/101").accept(MediaType.APPLICATION_JSON)).andReturn().response

        then: "correct response code should be returned"
        resp.status == HttpStatus.OK.value()
        and: "values are correctly mapped"
        def jsonNode = objectMapper.readTree(resp.getContentAsString())
        jsonNode.get("firstName").asText() == employee.firstName()
        jsonNode.get("lastName").asText() == employee.lastName()
        jsonNode.get("address").get("houseNumber").asLong() == employee.address().houseNumber()
    }

    def "test error response while fetching an employee"(){
        given: "context is loaded"
        employeePersistenceService.getEmployeeById(_ as BigInteger) >> {{ new RuntimeException("oops")}}

        when: "an employee is fetched"
        def response = mvc.perform(MockMvcRequestBuilders.get(baseurl + "/101").accept(MediaType.APPLICATION_JSON)).andReturn().response

        then: "error code should be returned"
        response.status == HttpStatus.INTERNAL_SERVER_ERROR.value()
    }

    def "test error response while fetching an employee due to bad request"(){
        given: "context is loaded"
        employeePersistenceService.getEmployeeById(_ as BigInteger) >> null

        when: "an employee is fetched"
        def response = mvc.perform(MockMvcRequestBuilders.get(baseurl + "/101").accept(MediaType.APPLICATION_JSON)).andReturn().response

        then: "error code should be returned"
        response.status == HttpStatus.NO_CONTENT.value()
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

    private static Employee createSampleEmployee(){
        return new Employee(BigInteger.valueOf(101),
                "fname",
                "lnmame",
                new EmployeeAddress(101,
                        "some-street",
                        "some-city",
                        "some-code")
        )
    }
}
