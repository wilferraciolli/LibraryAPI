package com.library.app.user.model;


import org.hibernate.validator.constraints.Email;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The type User. Abstract class to define the super class for a user.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Table(name = "lib_user")
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1050881026659874901L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @NotNull
    @Size(min = 3, max = 40)
    private String name;

    @Email
    @NotNull
    @Column(unique = true)
    @Size(max = 70)
    private String email;

    @NotNull
    private String password;

    /**
     * The enum Roles. This are the possible roles that a user can have.
     */
    public enum Roles {
        /**
         * Customer roles.
         */
        CUSTOMER, /**
         * Employee roles.
         */
        EMPLOYEE, /**
         * Administrator roles.
         */
        ADMINISTRATOR
    }

    //List of roles assigned to a user. This is used as CollectionTable because it is part of user, otherwise it could have been OneToMany relationship
    @CollectionTable(name = "lib_user_role", joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role"}))
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private List<Roles> roles;

    /**
     * The enum User type.
     */
    public enum UserType {
        /**
         * Customer user type.
         */
        CUSTOMER, /**
         * Employee user type.
         */
        EMPLOYEE
    }

    @Column(name = "type", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    /**
     * Gets default roles.
     *
     * @return the default roles
     */
    protected abstract List<Roles> getDefaultRoles();

    /**
     * Instantiates a new User.
     */
    public User() {
        this.createdAt = new Date();
        this.roles = getDefaultRoles();
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Gets created at.
     *
     * @return the created at
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets created at.
     *
     * @param createdAt the created at
     */
    public void setCreatedAt(final Date createdAt) {
        this.createdAt = createdAt;
    }

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
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Gets roles.
     *
     * @return the roles
     */
    public List<Roles> getRoles() {
        return roles;
    }

    /**
     * Sets roles.
     *
     * @param roles the roles
     */
    public void setRoles(final List<Roles> roles) {
        this.roles = roles;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", createdAt=" + createdAt + ", name=" + name + ", email=" + email + ", userType="
                + userType + "]";
    }

}