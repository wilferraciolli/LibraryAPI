package com.library.app.user.model.filter;

import com.library.app.common.model.filter.GenericFilter;
import com.library.app.user.model.User.UserType;

/**
 * The type User filter.
 */
public class UserFilter extends GenericFilter {

    private String name;
    private UserType userType;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets user type.
     *
     * @return the user type
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * Sets user type.
     *
     * @param userType the user type
     */
    public void setUserType(final UserType userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "UserFilter [name=" + name + ", userType=" + userType + ", toString()=" + super.toString() + "]";
    }

}
