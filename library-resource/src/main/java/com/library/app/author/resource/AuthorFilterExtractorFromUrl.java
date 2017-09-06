package com.library.app.author.resource;

import com.library.app.author.model.filter.AuthorFilter;
import com.library.app.common.model.filter.PaginationData;
import com.library.app.common.model.filter.PaginationData.OrderMode;

import javax.ws.rs.core.UriInfo;

/**
 * The type Author filter extractor from url. This class is usd to extract parameters from links urls.
 */
public class AuthorFilterExtractorFromUrl {

    private final UriInfo uriInfo;

    /**
     * Instantiates a new Author filter extractor from url.
     *
     * @param uriInfo the uri info
     */
    public AuthorFilterExtractorFromUrl(final UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    /**
     * Gets filter.
     *
     * @return the filter
     */
    public AuthorFilter getFilter() {
        final AuthorFilter authorFilter = new AuthorFilter();

        //get the parameters passed on the URI
        authorFilter.setPaginationData(extractPaginationData());
        authorFilter.setName(uriInfo.getQueryParameters().getFirst("name"));

        return authorFilter;
    }

    /**
     * Extract pagination data fro URI.
     *
     * @return the pagination data
     */
    private PaginationData extractPaginationData() {
        final int perPage = getPerPage();
        final int firstResult = getPage() * perPage;

        final String orderField;
        final PaginationData.OrderMode orderMode;
        final String sortField = getSortField();

        if (sortField.startsWith("+")) {
            //parameters started with + then it should be asc
            orderField = sortField.substring(1);
            orderMode = OrderMode.ASCENDING;
        } else if (sortField.startsWith("-")) {
            //parameters started with - then it should be desc
            orderField = sortField.substring(1);
            orderMode = OrderMode.DESCENDING;
        } else {
            //return the default value
            orderField = sortField;
            orderMode = OrderMode.ASCENDING;
        }

        return new PaginationData(firstResult, perPage, orderField, orderMode);
    }

    /**
     * Gets sort field parameter.
     *
     * @return the sort field
     */
    protected String getSortField() {
        final String sortField = uriInfo.getQueryParameters().getFirst("sort");
        if (sortField == null) {
            //return the default value
            return "name";
        }
        return sortField;
    }

    /**
     * Gets page parameter.
     *
     * @return the page
     */
    private Integer getPage() {
        final String page = uriInfo.getQueryParameters().getFirst("page");
        if (page == null) {
            //return the default value
            return 0;
        }
        return Integer.parseInt(page);
    }

    /**
     * Gets per_page parameter.
     *
     * @return the per page
     */
    private Integer getPerPage() {
        final String perPage = uriInfo.getQueryParameters().getFirst("per_page");
        if (perPage == null) {
            // return the default value
            return 10;
        }
        return Integer.parseInt(perPage);
    }
}
