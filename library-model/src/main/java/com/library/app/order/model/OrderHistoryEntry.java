package com.library.app.order.model;

import com.library.app.order.model.Order.OrderStatus;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * The type Order history entry.
 */
@Embeddable
public class OrderHistoryEntry implements Serializable {
    private static final long serialVersionUID = -5544853563085399050L;

    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @NotNull
    private Date createdAt;

    /**
     * Instantiates a new Order history entry.
     */
    public OrderHistoryEntry() {
    }

    /**
     * Instantiates a new Order history entry.
     *
     * @param status the status
     */
    public OrderHistoryEntry(final OrderStatus status) {
        this.status = status;
        this.createdAt = new Date();
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(final OrderStatus status) {
        this.status = status;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((status == null) ? 0 : status.hashCode());
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
        final OrderHistoryEntry other = (OrderHistoryEntry) obj;
        if (status != other.status) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OrderHistoryEntry [status=" + status + ", createdAt=" + createdAt + "]";
    }

}