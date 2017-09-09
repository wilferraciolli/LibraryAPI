package com.library.app.book.repository;

import com.library.app.author.model.Author;
import com.library.app.book.model.Book;
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
import static com.library.app.commontests.book.BookForTestsRepository.designPatterns;
import static com.library.app.commontests.book.BookForTestsRepository.normalizeDependencies;
import static com.library.app.commontests.category.CategoryForTestsRepository.allCategories;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
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
