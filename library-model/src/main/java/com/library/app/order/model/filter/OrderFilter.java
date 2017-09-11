package com.library.app.order.model.filter;

import com.library.app.common.model.filter.GenericFilter;
import com.library.app.order.model.Order.OrderStatus;

import java.util.Date;

/**
 * The type Order filter.
 */
public class OrderFilter extends GenericFilter {
    private Date startDate;
    private Date endDate;
    private Long customerId;
    private OrderStatus status;

    /**
     * Gets start date.
     *
     * @return the start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets start date.
     *
     * @param startDate the start date
     */
    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets end date.
     *
     * @return the end date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets end date.
     *
     * @param endDate the end date
     */
    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets customer id.
     *
     * @return the customer id
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * Sets customer id.
     *
     * @param customerId the customer id
     */
    public void setCustomerId(final Long customerId) {
        this.customerId = customerId;
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

    @Override
    public String toString() {
        return "OrderFilter [startDate=" + startDate + ", endDate=" + endDate + ", customerId=" + customerId
                + ", status=" + status + ", toString()=" + super.toString() + "]";
    }

}