package com.library.app.author.resource;

import com.library.app.author.exception.AuthorNotFoundException;
import com.library.app.author.model.Author;
import com.library.app.author.model.filter.AuthorFilter;
import com.library.app.author.services.AuthorServices;
import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.model.HttpCode;
import com.library.app.common.model.PaginatedData;
import com.library.app.commontests.utils.ResourceDefinitions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;
import java.util.List;

import static com.library.app.commontests.author.AuthorForTestsRepository.authorWithId;
import static com.library.app.commontests.author.AuthorForTestsRepository.erichGamma;
import static com.library.app.commontests.author.AuthorForTestsRepository.jamesGosling;
import static com.library.app.commontests.author.AuthorForTestsRepository.martinFowler;
import static com.library.app.commontests.author.AuthorForTestsRepository.robertMartin;
import static com.library.app.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static com.library.app.commontests.utils.FileTestNameUtils.getPathFileResponse;
import static com.library.app.commontests.utils.JsonTestUtils.assertJsonMatchesExpectedJson;
import static com.library.app.commontests.utils.JsonTestUtils.assertJsonMatchesFileContent;
import static com.library.app.commontests.utils.JsonTestUtils.readJsonFile;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * The type Author resource u test.
 */
public class AuthorResourceUTest {

    //add the endpoint resource
    private AuthorResource authorResource;

    private static final String PATH_RESOURCE = ResourceDefinitions.AUTHOR.getResourceName();

    @Mock
    private AuthorServices authorServices;

    @Mock
    private UriInfo uriInfo;

    /**
     * Init test case.
     */
    @Before
    public void initTestCase() {
        MockitoAnnotations.initMocks(this);
        authorResource = new AuthorResource();

        //initialize mocks
        authorResource.authorServices = authorServices;
        authorResource.authorJsonConverter = new AuthorJsonConverter();
        authorResource.uriInfo = uriInfo;
    }

    /**
     * Add valid author.
     */
    @Test
    public void addValidAuthor() {
        when(authorServices.add(robertMartin())).thenReturn(authorWithId(robertMartin(), 1L));

        final Response response = authorResource
                .add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "robertMartin.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));
        assertJsonMatchesExpectedJson(response.getEntity().toString(), "{\"id\": 1}");
    }

    /**
     * Add author with null name.
     *
     * @throws Exception the exception
     */
    @Test
    public void addAuthorWithNullName() throws Exception {
        when(authorServices.add((Author) anyObject())).thenThrow(new FieldNotValidException("name", "may not be null"));

        final Response response = authorResource
                .add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "authorWithNullName.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "authorErrorNullName.json");
    }

    /**
     * Update valid author.
     *
     * @throws Exception the exception
     */
    @Test
    public void updateValidAuthor() throws Exception {
        final Response response = authorResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "robertMartin.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertThat(response.getEntity().toString(), is(equalTo("")));

        verify(authorServices).update(authorWithId(robertMartin(), 1L));
    }

    /**
     * Update author with null name.
     *
     * @throws Exception the exception
     */
    @Test
    public void updateAuthorWithNullName() throws Exception {
        doThrow(new FieldNotValidException("name", "may not be null")).when(authorServices).update(
                (Author) anyObject());

        final Response response = authorResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "authorWithNullName.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "authorErrorNullName.json");
    }

    /**
     * Update author not found.
     *
     * @throws Exception the exception
     */
    @Test
    public void updateAuthorNotFound() throws Exception {
        doThrow(new AuthorNotFoundException()).when(authorServices).update(authorWithId(robertMartin(), 2L));

        final Response response = authorResource.update(2L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "robertMartin.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
    }

    /**
     * Find author.
     *
     * @throws AuthorNotFoundException the author not found exception
     */
    @Test
    public void findAuthor() throws AuthorNotFoundException {
        when(authorServices.findById(1L)).thenReturn(authorWithId(robertMartin(), 1L));

        final Response response = authorResource.findById(1L);
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertJsonResponseWithFile(response, "robertMartinFound.json");
    }

    /**
     * Find author not found.
     *
     * @throws AuthorNotFoundException the author not found exception
     */
    @Test
    public void findAuthorNotFound() throws AuthorNotFoundException {
        when(authorServices.findById(1L)).thenThrow(new AuthorNotFoundException());

        final Response response = authorResource.findById(1L);
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findByFilterNoFilter() {
        //populate few authors
        final List<Author> authors = Arrays.asList(authorWithId(erichGamma(), 2L), authorWithId(jamesGosling(), 3L),
                authorWithId(martinFowler(), 4L), authorWithId(robertMartin(), 1L));

        final MultivaluedMap<String, String> multiMap = mock(MultivaluedMap.class);
        when(uriInfo.getQueryParameters()).thenReturn(multiMap);

        when(authorServices.findByFilter((AuthorFilter) anyObject())).thenReturn(
                new PaginatedData<Author>(authors.size(), authors));

        final Response response = authorResource.findByFilter();
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertJsonResponseWithFile(response, "authorsAllInOnePage.json");
    }


    private void assertJsonResponseWithFile(final Response response, final String fileName) {
        assertJsonMatchesFileContent(response.getEntity().toString(), getPathFileResponse(PATH_RESOURCE, fileName));
    }

}
