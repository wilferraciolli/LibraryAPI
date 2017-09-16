package com.library.app.order.resource;

import com.library.app.common.resource.AbstractFilterExtractorFromUrl;
import com.library.app.common.utils.DateUtils;
import com.library.app.order.model.Order.OrderStatus;
import com.library.app.order.model.filter.OrderFilter;

import javax.ws.rs.core.UriInfo;

/**
 * The type Order filter extractor from url.
 */
public class OrderFilterExtractorFromUrl extends AbstractFilterExtractorFromUrl {

    /**
     * Instantiates a new Order filter extractor from url.
     *
     * @param uriInfo the uri info
     */
    public OrderFilterExtractorFromUrl(final UriInfo uriInfo) {
        super(uriInfo);
    }

    /**
     * Gets filter.
     *
     * @return the filter
     */
    public OrderFilter getFilter() {
        final OrderFilter orderFilter = new OrderFilter();

        orderFilter.setPaginationData(extractPaginationData());

        final String startDateStr = getUriInfo().getQueryParameters().getFirst("startDate");
        if (startDateStr != null) {
            orderFilter.setStartDate(DateUtils.getAsDateTime(startDateStr));
        }

        final String endDateStr = getUriInfo().getQueryParameters().getFirst("endDate");
        if (endDateStr != null) {
            orderFilter.setEndDate(DateUtils.getAsDateTime(endDateStr));
        }

        final String customerIdStr = getUriInfo().getQueryParameters().getFirst("customerId");
        if (customerIdStr != null) {
            orderFilter.setCustomerId(Long.valueOf(customerIdStr));
        }

        final String status = getUriInfo().getQueryParameters().getFirst("status");
        if (status != null) {
            orderFilter.setStatus(OrderStatus.valueOf(status));
        }

        return orderFilter;
    }

    @Override
    protected String getDefaultSortField() {
        return "-createdAt";
    }

}