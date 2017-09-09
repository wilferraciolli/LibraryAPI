package com.library.app.book.resource;

import com.library.app.book.model.filter.BookFilter;
import com.library.app.common.resource.AbstractFilterExtractorFromUrl;

import javax.ws.rs.core.UriInfo;

/**
 * The type Book filter extractor from url. Extract query params from the path.
 */
public class BookFilterExtractorFromUrl extends AbstractFilterExtractorFromUrl {

    /**
     * Instantiates a new Book filter extractor from url.
     *
     * @param uriInfo the uri info
     */
    public BookFilterExtractorFromUrl(final UriInfo uriInfo) {
        super(uriInfo);
    }

    /**
     * Gets filter.
     *
     * @return the filter
     */
    public BookFilter getFilter() {
        final BookFilter bookFilter = new BookFilter();

        bookFilter.setPaginationData(extractPaginationData());
        bookFilter.setTitle(getUriInfo().getQueryParameters().getFirst("title"));

        final String categoryIdStr = getUriInfo().getQueryParameters().getFirst("categoryId");
        if (categoryIdStr != null) {
            bookFilter.setCategoryId(Long.valueOf(categoryIdStr));
        }

        return bookFilter;
    }

    @Override
    protected String getDefaultSortField() {
        return "title";
    }

}