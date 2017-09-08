package com.library.app.author.resource;

import com.library.app.author.model.filter.AuthorFilter;
import com.library.app.common.resource.AbstractFilterExtractorFromUrl;

import javax.ws.rs.core.UriInfo;

/**
 * The type Author filter extractor from url. This class is usd to extract parameters from links urls.
 */
public class AuthorFilterExtractorFromUrl extends AbstractFilterExtractorFromUrl {

    /**
     * Instantiates a new Author filter extractor from url.
     *
     * @param uriInfo the uri info
     */
    public AuthorFilterExtractorFromUrl(final UriInfo uriInfo) {
        super(uriInfo);
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
        authorFilter.setName(getUriInfo().getQueryParameters().getFirst("name"));

        return authorFilter;
    }

    @Override
    protected String getDefaultSortField() {
        return "name";
    }

}
