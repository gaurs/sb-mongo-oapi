package io.gaurs.sbm.services;

import io.gaurs.sbm.model.logical.Employee;
import io.gaurs.sbm.model.physical.EmployeePhysical;
import io.gaurs.sbm.repositories.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * A service class to perform basic CRUD operations on an
 * {@link Employee} instance. The class also handles the transformation
 * between logical and physical instances of the entity.
 *
 * @author gaurs
 * @see EmployeePhysical
 */
@Service
public class EmployeePersistenceService{

    private final EmployeeRepository employeeRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public EmployeePersistenceService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    /**
     * The method is used to fetch an employee by its id
     *
     * @param id the unique identifier
     * @return employee if found, null otherwise
     * @throws NullPointerException if id is null
     */
    public Employee getEmployeeById(@NonNull BigInteger id){
        logger.info("request received to fetch employee with id: [{}]", id);
        var optionalEmployee = employeeRepository.findById(id);

        if(optionalEmployee.isPresent()){
            return optionalEmployee.get().toModel();
        } else{
            logger.info("no record found for id: [{}]", id);
            return null;
        }
    }

    /**
     * The method is used to save an employee record. The id for persisted record
     * is self generated.
     *
     * @param employee record to be persisted
     * @return persisted record with id populated
     */
    public Employee save(Employee employee){
        logger.info("request received to save employee: [{}]", employee);

        var employeePhysical = new EmployeePhysical(employee);
        return employeeRepository.save(employeePhysical).toModel();
    }
}
