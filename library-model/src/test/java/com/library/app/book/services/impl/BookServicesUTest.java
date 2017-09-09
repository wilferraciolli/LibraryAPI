package com.library.app.book.services.impl;

import com.library.app.author.exception.AuthorNotFoundException;
import com.library.app.author.services.AuthorServices;
import com.library.app.book.exception.BookNotFoundException;
import com.library.app.book.model.Book;
import com.library.app.book.model.filter.BookFilter;
import com.library.app.book.repository.BookRepository;
import com.library.app.book.services.BookServices;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.model.PaginatedData;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;

import static com.library.app.commontests.author.AuthorForTestsRepository.erichGamma;
import static com.library.app.commontests.author.AuthorForTestsRepository.robertMartin;
import static com.library.app.commontests.book.BookArgumentMatcher.bookEq;
import static com.library.app.commontests.book.BookForTestsRepository.bookWithId;
import static com.library.app.commontests.book.BookForTestsRepository.cleanCode;
import static com.library.app.commontests.book.BookForTestsRepository.designPatterns;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Book services unit test test.
 */
public class BookServicesUTest {

    private static Validator validator;
    private BookServices bookServices;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryServices categoryServices;

    @Mock
    private AuthorServices authorServices;

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

        bookServices = new BookServicesImpl();

        ((BookServicesImpl) bookServices).bookRepository = bookRepository;
        ((BookServicesImpl) bookServices).validator = validator;
        ((BookServicesImpl) bookServices).categoryServices = categoryServices;
        ((BookServicesImpl) bookServices).authorServices = authorServices;
    }

    /**
     * Add book with null title.
     */
    @Test
    public void addBookWithNullTitle() {
        final Book book = cleanCode();
        book.setTitle(null);
        addBookWithInvalidField(book, "title");
    }

    /**
     * Add book with null category.
     */
    @Test
    public void addBookWithNullCategory() {
        final Book book = cleanCode();
        book.setCategory(null);
        addBookWithInvalidField(book, "category");
    }

    /**
     * Add book with no authors.
     */
    @Test
    public void addBookWithNoAuthors() {
        final Book book = cleanCode();
        book.setAuthors(new ArrayList<>());
        addBookWithInvalidField(book, "authors");
    }

    /**
     * Add book with short description.
     */
    @Test
    public void addBookWithShortDescription() {
        final Book book = cleanCode();
        book.setDescription("short");
        addBookWithInvalidField(book, "description");
    }

    /**
     * Add book with null price.
     */
    @Test
    public void addBookWithNullPrice() {
        final Book book = cleanCode();
        book.setPrice(null);
        addBookWithInvalidField(book, "price");
    }

    /**
     * Add book with inexistent category.
     */
    @Test(expected = CategoryNotFoundException.class)
    public void addBookWithInexistentCategory() {
        when(categoryServices.findById(1L)).thenThrow(new CategoryNotFoundException());

        final Book book = cleanCode();
        book.getCategory().setId(1L);

        bookServices.add(book);
    }

    /**
     * Add book with inexistent author.
     *
     * @throws Exception the exception
     */
    @Test(expected = AuthorNotFoundException.class)
    public void addBookWithInexistentAuthor() throws Exception {
        when(categoryServices.findById(anyLong())).thenReturn(designPatterns().getCategory());
        when(authorServices.findById(1L)).thenReturn(erichGamma());
        when(authorServices.findById(2L)).thenThrow(new AuthorNotFoundException());

        final Book book = designPatterns();
        book.getAuthors().get(0).setId(1L);
        book.getAuthors().get(1).setId(2L);

        //TODO remove this categorySetId
        book.getCategory().setId(1L);

        bookServices.add(book);
    }

    /**
     * Add valid book.
     *
     * @throws Exception the exception
     */
    @Test
    public void addValidBook() throws Exception {
        when(categoryServices.findById(anyLong())).thenReturn(cleanCode().getCategory());
        when(authorServices.findById(anyLong())).thenReturn(robertMartin());
        when(bookRepository.add(bookEq(cleanCode()))).thenReturn(bookWithId(cleanCode(), 1L));

        //TODO remove this
        // set the category id oon the book category
        final Book cleanCodeBookCategoryWithId = cleanCode();
        cleanCodeBookCategoryWithId.getCategory().setId(1L);

        final Book bookAdded = bookServices.add(cleanCodeBookCategoryWithId);
        assertThat(bookAdded.getId(), equalTo(1L));
    }

    /**
     * Update author with short title.
     */
    @Test
    public void updateAuthorWithShortTitle() {
        final Book book = cleanCode();
        book.setTitle("short");
        try {
            bookServices.update(book);
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("title")));
        } catch (final Exception e) {
            fail("An Exception should not have been thrown");
        }
    }

    /**
     * Update book not found.
     *
     * @throws Exception the exception
     */
    @Test(expected = BookNotFoundException.class)
    public void updateBookNotFound() throws Exception {
        when(bookRepository.existsById(1L)).thenReturn(false);

        bookServices.update(bookWithId(cleanCode(), 1L));
    }

    /**
     * Update book with inexistent category.
     *
     * @throws Exception the exception
     */
    @Test(expected = CategoryNotFoundException.class)
    public void updateBookWithInexistentCategory() throws Exception {
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(categoryServices.findById(1L)).thenThrow(new CategoryNotFoundException());

        final Book book = bookWithId(cleanCode(), 1L);
        book.getCategory().setId(1L);

        bookServices.update(book);
    }

    /**
     * Update book with inexistent author.
     *
     * @throws Exception the exception
     */
    @Test(expected = AuthorNotFoundException.class)
    public void updateBookWithInexistentAuthor() throws Exception {
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(categoryServices.findById(anyLong())).thenReturn(cleanCode().getCategory());
        when(authorServices.findById(1L)).thenReturn(erichGamma());
        when(authorServices.findById(2L)).thenThrow(new AuthorNotFoundException());

        final Book book = bookWithId(designPatterns(), 1L);
        book.getAuthors().get(0).setId(1L);
        book.getAuthors().get(1).setId(2L);

        //TODO remove this categorySetId
        book.getCategory().setId(1L);

        bookServices.update(book);
    }

    /**
     * Update valid book.
     *
     * @throws Exception the exception
     */
    @Test
    public void updateValidBook() throws Exception {
        final Book bookToUpdate = bookWithId(cleanCode(), 1L);
        //TODO remove this categorySetId
        bookToUpdate.getCategory().setId(1L);


        when(categoryServices.findById(anyLong())).thenReturn(cleanCode().getCategory());
        when(authorServices.findById(anyLong())).thenReturn(robertMartin());
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookServices.update(bookToUpdate);
        verify(bookRepository).update(bookEq(bookToUpdate));
    }

    /**
     * Find book by id not found.
     *
     * @throws BookNotFoundException the book not found exception
     */
    @Test(expected = BookNotFoundException.class)
    public void findBookByIdNotFound() throws BookNotFoundException {
        when(bookRepository.findById(1L)).thenReturn(null);

        bookServices.findById(1L);
    }

    /**
     * Find book by filter.
     */
    @Test
    public void findBookByFilter() {
        final PaginatedData<Book> books = new PaginatedData<Book>(1, Arrays.asList(bookWithId(cleanCode(), 1L)));
        when(bookRepository.findByFilter((BookFilter) anyObject())).thenReturn(books);

        final PaginatedData<Book> booksReturned = bookServices.findByFilter(new BookFilter());
        assertThat(booksReturned.getNumberOfRows(), is(equalTo(1)));
        assertThat(booksReturned.getRow(0).getTitle(), is(equalTo(cleanCode().getTitle())));
    }

    /**
     * Find book by id.
     *
     * @throws BookNotFoundException the book not found exception
     */
    @Test
    public void findBookById() throws BookNotFoundException {
        when(bookRepository.findById(1L)).thenReturn(bookWithId(cleanCode(), 1L));

        final Book book = bookServices.findById(1L);
        assertThat(book, is(notNullValue()));
        assertThat(book.getTitle(), is(equalTo(cleanCode().getTitle())));
    }

    /**
     * Add book with invalid field.
     *
     * @param book                     the book
     * @param expectedInvalidFieldName the expected invalid field name
     */
    private void addBookWithInvalidField(final Book book, final String expectedInvalidFieldName) {
        try {
            bookServices.add(book);
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo(expectedInvalidFieldName)));
        }
    }
}