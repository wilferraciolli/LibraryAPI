package com.library.app.logaudit.model.filter;

import com.library.app.common.model.filter.GenericFilter;

import java.util.Date;

/**
 * The type Log audit filter. This class allow to filter by userId, startDate and endDate.
 * Eg if the admin wants to know what userId=123 have done then add it as a filter.
 */
public class LogAuditFilter extends GenericFilter {

    private Date startDate;
    private Date endDate;
    private Long userId;

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
     * Gets user id.
     *
     * @return the user id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "LogAuditFilter [startDate=" + startDate + ", endDate=" + endDate + ", userId=" + userId
                + ", toString()=" + super.toString() + "]";
    }

}