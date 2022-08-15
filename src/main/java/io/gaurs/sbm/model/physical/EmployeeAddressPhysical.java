package io.gaurs.sbm.model.physical;

import io.gaurs.sbm.model.logical.EmployeeAddress;
import org.springframework.data.annotation.PersistenceCreator;

public record EmployeeAddressPhysical(int houseNumber, String street, String city, String pinCode){

    @PersistenceCreator
    public EmployeeAddressPhysical(int houseNumber, String street, String city, String pinCode){
        this.houseNumber = houseNumber;
        this.street = street;
        this.city = city;
        this.pinCode = pinCode;
    }

    public EmployeeAddressPhysical(EmployeeAddress employeeAddress){
        this(employeeAddress.houseNumber(), employeeAddress.street(), employeeAddress.city(), employeeAddress.pinCode());
    }

    public EmployeeAddress toModel(){
        return new EmployeeAddress(this.houseNumber, this.street, this.city, this.pinCode);
    }
}
