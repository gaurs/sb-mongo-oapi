package io.gaurs.sbm.repositories

import io.gaurs.sbm.model.logical.Employee
import io.gaurs.sbm.model.logical.EmployeeAddress
import io.gaurs.sbm.model.physical.EmployeePhysical
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

/**
 * <code>@DataMongoTest</code> only loads the dao layer.
 * The following test case uses an embeded MongoDB instance
 * to test the DAO layer.
 *
 * Refer to following links for embedded mongo details:
 *
 * https://docs.spring.io/spring-boot/docs/current/reference/html/data.html#data.nosql.mongodb.embedded
 * https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo/blob/de.flapdoodle.embed.mongo-3.4.7/src/main/java/de/flapdoodle/embed/mongo/distribution/Version.java
 *
 * @author gaurs
 */
@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=5.0.6")
class EmployeeRepositorySpec extends Specification {

    @Autowired
    EmployeeRepository employeeRepository

    def "test saving an employee"() {
        given: "an employee instance to be saved"
        def employee = new Employee(null, "sumit", "gaur", new EmployeeAddress(221, "Baker Street", "London", "1001"))

        when: "the employee is persisted"
        def savedEmployee = employeeRepository.save(new EmployeePhysical(employee))

        then: "employee is correctly saved"
        savedEmployee != null
    }

    def "test fetching an employee"() {
        given: "an employee instance is saved"
        def employee = new Employee(null, "sumit", "gaur", new EmployeeAddress(221, "Baker Street", "London", "1001"))
        def savedEmployee = employeeRepository.save(new EmployeePhysical(employee))

        when: "the employee is fetched"
        def found = employeeRepository.findById(savedEmployee.getId())

        then: "employee is correctly fetched"
        found.isPresent()
        and: "found employee is identical to the saved employee"
        found.get().getId() == savedEmployee.getId()
    }
}
