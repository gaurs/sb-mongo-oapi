package io.gaurs.sbm.model.physical;

import io.gaurs.sbm.model.logical.Employee;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Document
public class EmployeePhysical{

    @Id
    private BigInteger id;
    private String firstName;
    private String lastName;
    private EmployeeAddressPhysical address;


    @PersistenceCreator
    public EmployeePhysical(BigInteger id, String firstName, String lastName, EmployeeAddressPhysical address){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public EmployeePhysical(Employee employee){
        this.id = employee.id();
        this.firstName = employee.firstName();
        this.lastName = employee.lastName();
        this.address = new EmployeeAddressPhysical(employee.address());
    }

    public Employee toModel(){
        return new Employee(this.id, this.firstName, this.lastName, this.address.toModel());
    }

    public BigInteger getId(){
        return id;
    }

    public void setId(BigInteger id){
        this.id = id;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public EmployeeAddressPhysical getAddress(){
        return address;
    }

    public void setAddress(EmployeeAddressPhysical address){
        this.address = address;
    }
}
