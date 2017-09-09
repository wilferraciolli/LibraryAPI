package com.library.app.book.resource;

import com.library.app.book.model.filter.BookFilter;
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
import static org.junit.Assert.assertThat;

/**
 * The type Book filter extractor from url test.
 */
public class BookFilterExtractorFromUrlTest {

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
     * Only default values.
     */
    @Test
    public void onlyDefaultValues() {
        setUpUriInfo(null, null, null, null, null);

        final BookFilterExtractorFromUrl extractor = new BookFilterExtractorFromUrl(uriInfo);
        final BookFilter bookFilter = extractor.getFilter();

        assertActualPaginationDataWithExpected(bookFilter.getPaginationData(), new PaginationData(0, 10, "title",
                OrderMode.ASCENDING));
        assertFieldsOnFilter(bookFilter, null, null);
    }

    /**
     * With pagination and title and category id and sort ascending.
     */
    @Test
    public void withPaginationAndTitleAndCategoryIdAndSortAscending() {
        setUpUriInfo("2", "5", "Design", "1", "id");

        final BookFilterExtractorFromUrl extractor = new BookFilterExtractorFromUrl(uriInfo);
        final BookFilter bookFilter = extractor.getFilter();

        assertActualPaginationDataWithExpected(bookFilter.getPaginationData(), new PaginationData(10, 5, "id",
                OrderMode.ASCENDING));
        assertFieldsOnFilter(bookFilter, "Design", 1L);
    }

    /**
     * With pagination and title and sort ascending with prefix.
     */
    @Test
    public void withPaginationAndTitleAndSortAscendingWithPrefix() {
        setUpUriInfo("2", "5", "Design", null, "+id");

        final BookFilterExtractorFromUrl extractor = new BookFilterExtractorFromUrl(uriInfo);
        final BookFilter bookFilter = extractor.getFilter();

        assertActualPaginationDataWithExpected(bookFilter.getPaginationData(), new PaginationData(10, 5, "id",
                OrderMode.ASCENDING));
        assertFieldsOnFilter(bookFilter, "Design", null);
    }

    /**
     * With pagination and title and category id and sort descending.
     */
    @Test
    public void withPaginationAndTitleAndCategoryIdAndSortDescending() {
        setUpUriInfo("2", "5", "Design", "10", "-id");

        final BookFilterExtractorFromUrl extractor = new BookFilterExtractorFromUrl(uriInfo);
        final BookFilter bookFilter = extractor.getFilter();

        assertActualPaginationDataWithExpected(bookFilter.getPaginationData(), new PaginationData(10, 5, "id",
                OrderMode.DESCENDING));
        assertFieldsOnFilter(bookFilter, "Design", 10L);
    }

    /**
     * Sets up uri info.
     *
     * @param page       the page
     * @param perPage    the per page
     * @param title      the title
     * @param categoryId the category id
     * @param sort       the sort
     */
    private void setUpUriInfo(final String page, final String perPage, final String title, final String categoryId,
                              final String sort) {
        final Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("page", page);
        parameters.put("per_page", perPage);
        parameters.put("title", title);
        parameters.put("categoryId", categoryId);
        parameters.put("sort", sort);

        setUpUriInfoWithMap(uriInfo, parameters);
    }

    /**
     * Assert fields on filter.
     *
     * @param bookFilter the book filter
     * @param title      the title
     * @param categoryId the category id
     */
    private void assertFieldsOnFilter(final BookFilter bookFilter, final String title, final Long categoryId) {
        assertThat(bookFilter.getTitle(), is(equalTo(title)));
        assertThat(bookFilter.getCategoryId(), is(equalTo(categoryId)));
    }

}