package com.library.app.commontests.utils;


import com.library.app.common.model.filter.PaginationData;
import org.junit.Ignore;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The type Filter extractor test utils.
 */
@Ignore
public class FilterExtractorTestUtils {

    private FilterExtractorTestUtils() {
    }

    /**
     * Assert actual pagination data with expected. Helper method for assertions. This will compare the pagination data.
     *
     * @param actual   the actual
     * @param expected the expected
     */
    public static void assertActualPaginationDataWithExpected(final PaginationData actual,
                                                              final PaginationData expected) {
        assertThat(actual.getFirstResult(), is(equalTo(expected.getFirstResult())));
        assertThat(actual.getMaxResults(), is(equalTo(expected.getMaxResults())));
        assertThat(actual.getOrderField(), is(equalTo(expected.getOrderField())));
        assertThat(actual.getOrderMode(), is(equalTo(expected.getOrderMode())));
    }

    /**
     * Sets up uri info helper method. This is a dummy helper method to set up some parameters.
     *
     * @param uriInfo    the uri info
     * @param parameters the parameters
     */
    @SuppressWarnings("unchecked")
    public static void setUpUriInfoWithMap(final UriInfo uriInfo, final Map<String, String> parameters) {
        final MultivaluedMap<String, String> multiMap = mock(MultivaluedMap.class);

        for (final Map.Entry<String, String> keyValue : parameters.entrySet()) {
            when(multiMap.getFirst(keyValue.getKey())).thenReturn(keyValue.getValue());
        }

        when(uriInfo.getQueryParameters()).thenReturn(multiMap);
    }
}
