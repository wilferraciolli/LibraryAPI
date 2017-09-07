package com.library.app.user.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Arrays;
import java.util.List;

/**
 * The type Employee. This is the implementation of user that sets the default type and role for an employee.
 * Ps note that Administrators cannot be created via code, it has to b via the API.
 */
@Entity
@DiscriminatorValue("EMPLOYEE")
public class Employee extends User {
    private static final long serialVersionUID = 8976498066151628068L;

    /**
     * Instantiates a new Employee.
     */
    public Employee() {
        setUserType(UserType.EMPLOYEE);
    }

    @Override
    protected List<Roles> getDefaultRoles() {
        return Arrays.asList(Roles.EMPLOYEE);
    }

}