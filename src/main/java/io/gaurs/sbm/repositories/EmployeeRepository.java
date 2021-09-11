package io.gaurs.sbm.repositories;

import io.gaurs.sbm.model.physical.EmployeePhysical;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

/**
 * The entity repository implemented by the spring context
 * for basic CRUD operations.
 *
 * @author gaurs
 */
@Repository
public interface EmployeeRepository extends MongoRepository<EmployeePhysical, BigInteger>{
}
