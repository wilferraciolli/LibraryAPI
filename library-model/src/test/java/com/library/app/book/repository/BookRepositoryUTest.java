package com.library.app.book.repository;

import com.library.app.author.model.Author;
import com.library.app.book.model.Book;
import com.library.app.book.model.filter.BookFilter;
import com.library.app.common.model.PaginatedData;
import com.library.app.common.model.filter.PaginationData;
import com.library.app.commontests.book.BookForTestsRepository;
import com.library.app.commontests.utils.TestBaseRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.library.app.commontests.author.AuthorForTestsRepository.allAuthors;
import static com.library.app.commontests.author.AuthorForTestsRepository.erichGamma;
import static com.library.app.commontests.author.AuthorForTestsRepository.johnVlissides;
import static com.library.app.commontests.author.AuthorForTestsRepository.ralphJohnson;
import static com.library.app.commontests.author.AuthorForTestsRepository.richardHelm;
import static com.library.app.commontests.book.BookForTestsRepository.allBooks;
import static com.library.app.commontests.book.BookForTestsRepository.designPatterns;
import static com.library.app.commontests.book.BookForTestsRepository.effectiveJava;
import static com.library.app.commontests.book.BookForTestsRepository.normalizeDependencies;
import static com.library.app.commontests.book.BookForTestsRepository.peaa;
import static com.library.app.commontests.book.BookForTestsRepository.refactoring;
import static com.library.app.commontests.category.CategoryForTestsRepository.allCategories;
import static com.library.app.commontests.category.CategoryForTestsRepository.architecture;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
/**
 * The type Book repository unit test.
 */
public class BookRepositoryUTest extends TestBaseRepository {
    private BookRepository bookRepository;

    /**
     * Init test case.
     */
    @Before
    public void initTestCase() {
        initializeTestDB();

        bookRepository = new BookRepository();
        bookRepository.em = em;

        loadCategoriesAndAuthors();
    }

    /**
     * Sets down test case.
     */
    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    /**
     * Add book and find it.
     */
    @Test
    public void addBookAndFindIt() {
        final Book designPatterns = normalizeDependencies(designPatterns(), em);
        final Long bookAddedId = dbCommandExecutor.executeCommand(() -> {
            return bookRepository.add(designPatterns).getId();
        });
        assertThat(bookAddedId, is(notNullValue()));

        final Book book = bookRepository.findById(bookAddedId);
        assertThat(book.getTitle(), is(equalTo(designPatterns().getTitle())));
        assertThat(book.getDescription(), is(equalTo(designPatterns().getDescription())));
        assertThat(book.getCategory().getName(), is(equalTo(designPatterns().getCategory().getName())));
        assertAuthors(book, erichGamma(), johnVlissides(), ralphJohnson(), richardHelm());
        assertThat(book.getPrice(), is(equalTo(48.94D)));
    }

    /**
     * Find book by id not found.
     */
    @Test
    public void findBookByIdNotFound() {
        final Book book = bookRepository.findById(999L);
        assertThat(book, is(nullValue()));
    }

    /**
     * Update book.
     */
    @Test
    public void updateBook() {
        final Book designPatterns = normalizeDependencies(designPatterns(), em);
        final Long bookAddedId = dbCommandExecutor.executeCommand(() -> {
            return bookRepository.add(designPatterns).getId();
        });

        assertThat(bookAddedId, is(notNullValue()));
        final Book book = bookRepository.findById(bookAddedId);
        assertThat(book.getTitle(), is(equalTo(designPatterns().getTitle())));

        book.setTitle("Design Patterns");
        dbCommandExecutor.executeCommand(() -> {
            bookRepository.update(book);
            return null;
        });

        final Book bookAfterUpdate = bookRepository.findById(bookAddedId);
        assertThat(bookAfterUpdate.getTitle(), is(equalTo("Design Patterns")));
    }

    /**
     * Exists by id.
     */
    @Test
    public void existsById() {
        final Book designPatterns = normalizeDependencies(designPatterns(), em);
        final Long bookAddedId = dbCommandExecutor.executeCommand(() -> {
            return bookRepository.add(designPatterns).getId();
        });

        assertThat(bookRepository.existsById(bookAddedId), is(equalTo(true)));
        assertThat(bookRepository.existsById(999l), is(equalTo(false)));
    }

    /**
     * Find by filter no filter.
     */
    @Test
    public void findByFilterNoFilter() {
        loadBooksForFindByFilter();

        final PaginatedData<Book> result = bookRepository.findByFilter(new BookFilter());
        assertThat(result.getNumberOfRows(), is(equalTo(5)));
        assertThat(result.getRows().size(), is(equalTo(5)));
        assertThat(result.getRow(0).getTitle(), is(equalTo(BookForTestsRepository.cleanCode().getTitle())));
        assertThat(result.getRow(1).getTitle(), is(equalTo(designPatterns().getTitle())));
        assertThat(result.getRow(2).getTitle(), is(equalTo(effectiveJava().getTitle())));
        assertThat(result.getRow(3).getTitle(), is(equalTo(peaa().getTitle())));
        assertThat(result.getRow(4).getTitle(), is(equalTo(refactoring().getTitle())));
    }

    /**
     * Find by filter with paging.
     */
    @Test
    public void findByFilterWithPaging() {
        loadBooksForFindByFilter();

        final BookFilter bookFilter = new BookFilter();
        bookFilter.setPaginationData(new PaginationData(0, 3, "title", PaginationData.OrderMode.DESCENDING));
        PaginatedData<Book> result = bookRepository.findByFilter(bookFilter);

        assertThat(result.getNumberOfRows(), is(equalTo(5)));
        assertThat(result.getRows().size(), is(equalTo(3)));
        assertThat(result.getRow(0).getTitle(), is(equalTo(refactoring().getTitle())));
        assertThat(result.getRow(1).getTitle(), is(equalTo(peaa().getTitle())));
        assertThat(result.getRow(2).getTitle(), is(equalTo(effectiveJava().getTitle())));

        bookFilter.setPaginationData(new PaginationData(3, 3, "title", PaginationData.OrderMode.DESCENDING));
        result = bookRepository.findByFilter(bookFilter);

        assertThat(result.getNumberOfRows(), is(equalTo(5)));
        assertThat(result.getRows().size(), is(equalTo(2)));
        assertThat(result.getRow(0).getTitle(), is(equalTo(designPatterns().getTitle())));
        assertThat(result.getRow(1).getTitle(), is(equalTo(BookForTestsRepository.cleanCode().getTitle())));
    }

    /**
     * Find by filter filtering by category and title.
     */
    @Test
    public void findByFilterFilteringByCategoryAndTitle() {
        loadBooksForFindByFilter();

        final Book book = new Book();
        book.setCategory(architecture());

        final BookFilter bookFilter = new BookFilter();
        bookFilter.setCategoryId(normalizeDependencies(book, em).getCategory().getId());
        bookFilter.setTitle("Software");
        bookFilter.setPaginationData(new PaginationData(0, 3, "title", PaginationData.OrderMode.ASCENDING));
        final PaginatedData<Book> result = bookRepository.findByFilter(bookFilter);

        assertThat(result.getNumberOfRows(), is(equalTo(1)));
        assertThat(result.getRows().size(), is(equalTo(1)));
        assertThat(result.getRow(0).getTitle(), is(equalTo(designPatterns().getTitle())));
    }

    /**
     * Load books for find by filter.
     */
    private void loadBooksForFindByFilter() {
        dbCommandExecutor.executeCommand(() -> {
            allBooks().forEach((book) -> bookRepository.add(normalizeDependencies(book, em)));
            return null;
        });
    }

    /**
     * Assert authors.
     *
     * @param book            the book
     * @param expectedAuthors the expected authors
     */
    private void assertAuthors(final Book book, final Author... expectedAuthors) {
        final List<Author> authors = book.getAuthors();
        assertThat(authors.size(), is(equalTo(expectedAuthors.length)));

        for (int i = 0; i < expectedAuthors.length; i++) {
            final Author actualAuthor = authors.get(i);
            final Author expectedAuthor = expectedAuthors[i];
            assertThat(actualAuthor.getName(), is(equalTo(expectedAuthor.getName())));
        }
    }

    /**
     * Load categories and authors. Run this method to load the database with the categories and authors
     * before it can be created a book.
     */
    private void loadCategoriesAndAuthors() {
        dbCommandExecutor.executeCommand(() -> {
            allCategories().forEach(em::persist);
            allAuthors().forEach(em::persist);
            return null;
        });
    }

}
