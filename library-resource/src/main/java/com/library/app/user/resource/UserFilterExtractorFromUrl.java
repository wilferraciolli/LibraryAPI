package com.library.app.user.resource;

import com.library.app.common.resource.AbstractFilterExtractorFromUrl;
import com.library.app.user.model.User.UserType;
import com.library.app.user.model.filter.UserFilter;

import javax.ws.rs.core.UriInfo;

/**
 * The type User filter extractor from url.
 */
public class UserFilterExtractorFromUrl extends AbstractFilterExtractorFromUrl {

    /**
     * Instantiates a new User filter extractor from url.
     *
     * @param uriInfo the uri info
     */
    public UserFilterExtractorFromUrl(final UriInfo uriInfo) {
        super(uriInfo);
    }

    /**
     * Gets filter.
     *
     * @return the filter
     */
    public UserFilter getFilter() {
        final UserFilter userFilter = new UserFilter();
        userFilter.setPaginationData(extractPaginationData());
        userFilter.setName(getUriInfo().getQueryParameters().getFirst("name"));

        final String userType = getUriInfo().getQueryParameters().getFirst("type");
        if (userType != null) {
            userFilter.setUserType(UserType.valueOf(userType));
        }

        return userFilter;
    }

    /**
     * Gets default sort field.
     *
     * @return the default sort field
     */
    @Override
    protected String getDefaultSortField() {
        return "name";
    }

}