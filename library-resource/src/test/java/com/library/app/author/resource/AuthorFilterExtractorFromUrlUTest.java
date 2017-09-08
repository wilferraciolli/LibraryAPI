package com.library.app.author.resource;


import com.library.app.author.model.filter.AuthorFilter;
import com.library.app.common.model.filter.PaginationData;
import com.library.app.common.model.filter.PaginationData.OrderMode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.UriInfo;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.library.app.commontests.utils.FilterExtractorTestUtils.assertActualPaginationDataWithExpected;
import static com.library.app.commontests.utils.FilterExtractorTestUtils.setUpUriInfoWithMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;



/**
 * The type Author filter extractor from url u test.
 */
public class AuthorFilterExtractorFromUrlUTest {

    @Mock
    private UriInfo uriInfo;

    /**
     * Init test case.
     */
    @Before
    public void initTestCase() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Only default values test. This is when no parameters are passed and it will use the default values.
     */
    @Test
    public void onlyDefaultValues() {
        setUpUriInfo(null, null, null, null);

        final AuthorFilterExtractorFromUrl extractor = new AuthorFilterExtractorFromUrl(uriInfo);
        final AuthorFilter authorFilter = extractor.getFilter();

        //assert that all the default values were picked up as no parameters were passed
        assertActualPaginationDataWithExpected(authorFilter.getPaginationData(), new PaginationData(0, 10, "name",
                OrderMode.ASCENDING));
        assertThat(authorFilter.getName(), is(nullValue()));
    }

    /**
     * With pagination and name and sort ascending.
     */
    @Test
    public void withPaginationAndNameAndSortAscending() {
        setUpUriInfo("2", "5", "Robert", "id");

        final AuthorFilterExtractorFromUrl extractor = new AuthorFilterExtractorFromUrl(uriInfo);
        final AuthorFilter authorFilter = extractor.getFilter();

        assertActualPaginationDataWithExpected(authorFilter.getPaginationData(), new PaginationData(10, 5, "id",
                OrderMode.ASCENDING));
        assertThat(authorFilter.getName(), is(equalTo("Robert")));
    }

    /**
     * With pagination and name and sort ascending with prefix.
     */
    @Test
    public void withPaginationAndNameAndSortAscendingWithPrefix() {
        setUpUriInfo("2", "5", "Robert", "+id");

        final AuthorFilterExtractorFromUrl extractor = new AuthorFilterExtractorFromUrl(uriInfo);
        final AuthorFilter authorFilter = extractor.getFilter();

        assertActualPaginationDataWithExpected(authorFilter.getPaginationData(), new PaginationData(10, 5, "id",
                OrderMode.ASCENDING));
        assertThat(authorFilter.getName(), is(equalTo("Robert")));
    }

    /**
     * With pagination and name and sort descending.
     */
    @Test
    public void withPaginationAndNameAndSortDescending() {
        setUpUriInfo("2", "5", "Robert", "-id");

        final AuthorFilterExtractorFromUrl extractor = new AuthorFilterExtractorFromUrl(uriInfo);
        final AuthorFilter authorFilter = extractor.getFilter();

        assertActualPaginationDataWithExpected(authorFilter.getPaginationData(), new PaginationData(10, 5, "id",
                OrderMode.DESCENDING));
        assertThat(authorFilter.getName(), is(equalTo("Robert")));
    }

    /**
     * Sets up uri info helper method. This is a dummy helper method to set up some parameters.
     *
     * @param page    the page
     * @param perPage the per page
     * @param name    the name
     * @param sort    the sort
     */
    @SuppressWarnings("unchecked")
    public void setUpUriInfo(final String page, final String perPage, final String name, final String sort) {
        final Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("page", page);
        parameters.put("per_page", perPage);
        parameters.put("name", name);
        parameters.put("sort", sort);

        setUpUriInfoWithMap(uriInfo, parameters);
    }

}
