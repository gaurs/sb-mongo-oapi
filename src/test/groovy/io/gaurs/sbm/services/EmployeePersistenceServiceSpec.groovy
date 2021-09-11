package io.gaurs.sbm.services

import io.gaurs.sbm.MockBeanConfig
import io.gaurs.sbm.model.logical.Employee
import io.gaurs.sbm.model.logical.EmployeeAddress
import io.gaurs.sbm.model.physical.EmployeePhysical
import io.gaurs.sbm.repositories.EmployeeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = MockBeanConfig.class)
class EmployeePersistenceServiceSpec extends Specification {

    @Autowired
    EmployeeRepository employeeRepository

    def employeePersistenceService

    def setup() {
        employeePersistenceService = new EmployeePersistenceService(employeeRepository)
    }

    def "saving an employee"() {
        given: "context is loaded"
        employeeRepository.save(_ as EmployeePhysical) >> { EmployeePhysical employee -> employee }

        when: "employee record is persisted"
        def employee = createSampleEmployee()
        def saved = employeePersistenceService.save(employee)

        then: "record should be persisted correctly"
        employee == saved
    }

    def "fetching an employee"() {
        given: "context is loaded"
        def employee = createSampleEmployee()
        employeeRepository.findById(_ as BigInteger) >> Optional.of(new EmployeePhysical(employee))

        when: "employee record is persisted"
        def saved = employeePersistenceService.getEmployeeById(BigInteger.valueOf(101))

        then: "correct record should be returned"
        employee == saved
    }

    def "fetching an employee when id does not match with any record"() {
        given: "context is loaded"
        employeeRepository.findById(_ as BigInteger) >> Optional.empty()

        when: "employee record is persisted"
        def saved = employeePersistenceService.getEmployeeById(BigInteger.valueOf(101))

        then: "correct record should be returned"

        saved == null
    }

    private static Employee createSampleEmployee() {
        return new Employee(BigInteger.valueOf(101), "fname", "lnmame", new EmployeeAddress(101, "some-street", "some-city", "some-code"))
    }
}
