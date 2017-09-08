package com.library.app.user.resource;

import com.library.app.common.model.filter.PaginationData;
import com.library.app.common.model.filter.PaginationData.OrderMode;
import com.library.app.user.model.User.UserType;
import com.library.app.user.model.filter.UserFilter;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.UriInfo;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.library.app.commontests.utils.FilterExtractorTestUtils.assertActualPaginationDataWithExpected;
import static com.library.app.commontests.utils.FilterExtractorTestUtils.setUpUriInfoWithMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * The type User filter extractor from url unit test.
 */
public class UserFilterExtractorFromUrlUTest {
    private UriInfo uriInfo;

    /**
     * Init test case.
     */
    @Before
    public void initTestCase() {
        uriInfo = mock(UriInfo.class);
    }

    /**
     * Only default values.
     */
    @Test
    public void onlyDefaultValues() {
        setUpUriInfo(null, null, null, null, null);

        final UserFilterExtractorFromUrl extractor = new UserFilterExtractorFromUrl(uriInfo);
        final UserFilter userFilter = extractor.getFilter();

        assertActualPaginationDataWithExpected(userFilter.getPaginationData(), new PaginationData(0, 10, "name",
                OrderMode.ASCENDING));
        assertFieldsOnFilter(userFilter, null, null);
    }

    /**
     * With pagination and name and user type and sort ascending.
     */
    @Test
    public void withPaginationAndNameAndUserTypeAndSortAscending() {
        setUpUriInfo("2", "5", "John", UserType.CUSTOMER, "id");

        final UserFilterExtractorFromUrl extractor = new UserFilterExtractorFromUrl(uriInfo);
        final UserFilter userFilter = extractor.getFilter();

        assertActualPaginationDataWithExpected(userFilter.getPaginationData(), new PaginationData(10, 5, "id",
                OrderMode.ASCENDING));
        assertFieldsOnFilter(userFilter, "John", UserType.CUSTOMER);
    }

    /**
     * With pagination and name and sort ascending with prefix.
     */
    @Test
    public void withPaginationAndNameAndSortAscendingWithPrefix() {
        setUpUriInfo("2", "5", "John", null, "+id");

        final UserFilterExtractorFromUrl extractor = new UserFilterExtractorFromUrl(uriInfo);
        final UserFilter userFilter = extractor.getFilter();

        assertActualPaginationDataWithExpected(userFilter.getPaginationData(), new PaginationData(10, 5, "id",
                OrderMode.ASCENDING));
        assertFieldsOnFilter(userFilter, "John", null);
    }

    /**
     * With pagination and namee and user type and sort descending.
     */
    @Test
    public void withPaginationAndNameeAndUserTypeAndSortDescending() {
        setUpUriInfo("2", "5", "John", UserType.EMPLOYEE, "-id");

        final UserFilterExtractorFromUrl extractor = new UserFilterExtractorFromUrl(uriInfo);
        final UserFilter userFilter = extractor.getFilter();

        assertActualPaginationDataWithExpected(userFilter.getPaginationData(), new PaginationData(10, 5, "id",
                OrderMode.DESCENDING));
        assertFieldsOnFilter(userFilter, "John", UserType.EMPLOYEE);
    }

    /**
     * Sets up uri info.
     *
     * @param page    the page
     * @param perPage the per page
     * @param name    the name
     * @param type    the type
     * @param sort    the sort
     */
    private void setUpUriInfo(final String page, final String perPage, final String name, final UserType type,
                              final String sort) {
        final Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("page", page);
        parameters.put("per_page", perPage);
        parameters.put("name", name);
        parameters.put("type", type != null ? type.name() : null);
        parameters.put("sort", sort);

        setUpUriInfoWithMap(uriInfo, parameters);
    }

    /**
     * Assert fields on filter.
     *
     * @param userFilter the user filter
     * @param name       the name
     * @param userType   the user type
     */
    private void assertFieldsOnFilter(final UserFilter userFilter, final String name, final UserType userType) {
        assertThat(userFilter.getName(), is(equalTo(name)));
        assertThat(userFilter.getUserType(), is(equalTo(userType)));
    }

}