package com.library.app.author.services.impl;


import com.library.app.author.exception.AuthorNotFoundException;
import com.library.app.author.model.Author;
import com.library.app.author.model.filter.AuthorFilter;
import com.library.app.author.repository.AuthorRepository;
import com.library.app.author.services.AuthorServices;
import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.model.PaginatedData;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;

import static com.library.app.commontests.author.AuthorForTestsRepository.authorWithId;
import static com.library.app.commontests.author.AuthorForTestsRepository.robertMartin;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Author services unit tests.
 */
public class AuthorServicesUTest {

    private static Validator validator;
    private AuthorServices authorServices;

    @Mock
    private AuthorRepository authorRepository;

    /**
     * Init test class.
     */
    @BeforeClass
    public static void initTestClass() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * Init test case.
     */
    @Before
    public void initTestCase() {
        MockitoAnnotations.initMocks(this);

        authorServices = new AuthorServicesImpl();

        ((AuthorServicesImpl) authorServices).authorRepository = authorRepository;
        ((AuthorServicesImpl) authorServices).validator = validator;
    }

    /**
     * Add author with null name.
     */
    @Test
    public void addAuthorWithNullName() {
        addAuthorWithInvalidName(null);
    }

    /**
     * Add author with short name.
     */
    @Test
    public void addAuthorWithShortName() {
        addAuthorWithInvalidName("A");
    }

    /**
     * Add author with long name.
     */
    @Test
    public void addAuthorWithLongName() {
        addAuthorWithInvalidName("This is a very long name that will cause an exception to be thrown");
    }

    /**
     * Add valid author.
     */
    @Test
    public void addValidAuthor() {
        when(authorRepository.add(robertMartin())).thenReturn(authorWithId(robertMartin(), 1L));

        try {
            final Author authorAdded = authorServices.add(robertMartin());
            assertThat(authorAdded.getId(), equalTo(1L));
        } catch (final FieldNotValidException e) {
            fail("No error should have been thrown");
        }
    }

    /**
     * Update author with null name.
     */
    @Test
    public void updateAuthorWithNullName() {
        updateAuthorWithInvalidName(null);
    }

    /**
     * Update author with short name.
     */
    @Test
    public void updateAuthorWithShortName() {
        updateAuthorWithInvalidName("A");
    }

    /**
     * Update author with long name.
     */
    @Test
    public void updateAuthorWithLongName() {
        updateAuthorWithInvalidName("This is a very long name that will cause an exception to be thrown");
    }

    /**
     * Update author not found.
     *
     * @throws Exception the exception
     */
    @Test(expected = AuthorNotFoundException.class)
    public void updateAuthorNotFound() throws Exception {
        when(authorRepository.existsById(1L)).thenReturn(false);

        authorServices.update(authorWithId(robertMartin(), 1L));
    }

    /**
     * Update valid author.
     *
     * @throws Exception the exception
     */
    @Test
    public void updateValidAuthor() throws Exception {
        final Author authorToUpdate = authorWithId(robertMartin(), 1L);
        when(authorRepository.existsById(1L)).thenReturn(true);

        authorServices.update(authorToUpdate);
        verify(authorRepository).update(authorToUpdate);
    }

    /**
     * Find author by id not found.
     *
     * @throws AuthorNotFoundException the author not found exception
     */
    @Test(expected = AuthorNotFoundException.class)
    public void findAuthorByIdNotFound() throws AuthorNotFoundException {
        when(authorRepository.findById(1L)).thenReturn(null);

        authorServices.findById(1L);
    }

    /**
     * Find author by id.
     *
     * @throws AuthorNotFoundException the author not found exception
     */
    @Test
    public void findAuthorById() throws AuthorNotFoundException {
        when(authorRepository.findById(1L)).thenReturn(authorWithId(robertMartin(), 1L));

        final Author author = authorServices.findById(1L);
        assertThat(author, is(notNullValue()));
        assertThat(author.getName(), is(equalTo(robertMartin().getName())));
    }

    /**
     * Find author by filter.
     */
    @Test
    public void findAuthorByFilter() {
        final PaginatedData<Author> authors = new PaginatedData<Author>(1, Arrays.asList(authorWithId(robertMartin(),
                1L)));
        when(authorRepository.findByFilter((AuthorFilter) anyObject())).thenReturn(authors);

        final PaginatedData<Author> authorsReturned = authorServices.findByFilter(new AuthorFilter());
        assertThat(authorsReturned.getNumberOfRows(), is(equalTo(1)));
        assertThat(authorsReturned.getRow(0).getName(), is(equalTo(robertMartin().getName())));
    }

    private void updateAuthorWithInvalidName(final String name) {
        try {
            authorServices.update(new Author(name));
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("name")));
        }
    }

    private void addAuthorWithInvalidName(final String name) {
        try {
            authorServices.add(new Author(name));
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("name")));
        }
    }
}
