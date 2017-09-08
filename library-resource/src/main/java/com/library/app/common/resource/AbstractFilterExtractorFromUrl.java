package com.library.app.common.resource;

import com.library.app.common.model.filter.PaginationData;
import com.library.app.common.model.filter.PaginationData.OrderMode;

import javax.ws.rs.core.UriInfo;

/**
 * The type Abstract filter extractor from url. This is a helper class to extract filter parameters from an URL.
 */
public abstract class AbstractFilterExtractorFromUrl {

    private final UriInfo uriInfo;
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PER_PAGE = 10;

    /**
     * Gets default sort field. This method needs to be implemented on every child.
     *
     * @return the default sort field
     */
    protected abstract String getDefaultSortField();

    /**
     * Instantiates a new Abstract filter extractor from url.
     *
     * @param uriInfo the uri info
     */
    public AbstractFilterExtractorFromUrl(final UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    /**
     * Gets uri info.
     *
     * @return the uri info
     */
    protected UriInfo getUriInfo() {
        return uriInfo;
    }

    /**
     * Extract pagination data pagination data.
     *
     * @return the pagination data
     */
    protected PaginationData extractPaginationData() {
        final int perPage = getPerPage();
        final int firstResult = getPage() * perPage;

        final String orderField;
        final OrderMode orderMode;
        final String sortField = getSortField();

        if (sortField.startsWith("+")) {
            orderField = sortField.substring(1);
            orderMode = OrderMode.ASCENDING;
        } else if (sortField.startsWith("-")) {
            orderField = sortField.substring(1);
            orderMode = OrderMode.DESCENDING;
        } else {
            orderField = sortField;
            orderMode = OrderMode.ASCENDING;
        }

        return new PaginationData(firstResult, perPage, orderField, orderMode);
    }

    /**
     * Gets sort field.
     *
     * @return the sort field
     */
    protected String getSortField() {
        final String sortField = uriInfo.getQueryParameters().getFirst("sort");
        if (sortField == null) {
            return getDefaultSortField();
        }
        return sortField;
    }

    /**
     * Gets page.
     *
     * @return the page
     */
    private Integer getPage() {
        final String page = uriInfo.getQueryParameters().getFirst("page");
        if (page == null) {
            return DEFAULT_PAGE;
        }
        return Integer.parseInt(page);
    }

    /**
     * Gets per page.
     *
     * @return the per page
     */
    private Integer getPerPage() {
        final String perPage = uriInfo.getQueryParameters().getFirst("per_page");
        if (perPage == null) {
            return DEFAULT_PER_PAGE;
        }
        return Integer.parseInt(perPage);
    }

}