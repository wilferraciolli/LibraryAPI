package com.library.app.logaudit.model;

import com.library.app.user.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * The type Log audit. This class is used to save audit data to the database.
 */
@Entity
@Table(name = "lib_log_audit")
public class LogAudit implements Serializable {
    private static final long serialVersionUID = -6737238567613975932L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    @NotNull
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    /**
     * The enum Action.
     */
    public enum Action {
        /**
         * Add action.
         */
        ADD, /**
         * Update action.
         */
        UPDATE
    }

    @Enumerated(EnumType.STRING)
    @NotNull
    private Action action;

    @NotNull
    private String element;

    /**
     * Instantiates a new Log audit.
     */
    public LogAudit() {
        this.createdAt = new Date();
    }

    /**
     * Instantiates a new Log audit.
     *
     * @param user    the user
     * @param action  the action
     * @param element the element
     */
    public LogAudit(final User user, final Action action, final String element) {
        this();
        this.user = user;
        this.action = action;
        this.element = element;
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
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * Gets action.
     *
     * @return the action
     */
    public Action getAction() {
        return action;
    }

    /**
     * Sets action.
     *
     * @param action the action
     */
    public void setAction(final Action action) {
        this.action = action;
    }

    /**
     * Gets element.
     *
     * @return the element
     */
    public String getElement() {
        return element;
    }

    /**
     * Sets element.
     *
     * @param element the element
     */
    public void setElement(final String element) {
        this.element = element;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        final LogAudit other = (LogAudit) obj;
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
        return "LogAudit [id=" + id + ", createdAt=" + createdAt + ", user=" + user + ", action=" + action
                + ", element=" + element + "]";
    }

}