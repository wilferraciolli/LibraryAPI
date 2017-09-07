package com.library.app.user.model;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Arrays;
import java.util.List;

/**
 * The type Customer. This is an implementation class for User. This class initiates a User class
 * and set the type and default roles that are applicable for a customer
 */
@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {
    private static final long serialVersionUID = -6100894877953675646L;

    /**
     * Instantiates a new Customer.
     */
    public Customer() {
        setUserType(UserType.CUSTOMER);
    }

    @Override
    protected List<Roles> getDefaultRoles() {
        return Arrays.asList(Roles.CUSTOMER);
    }

}