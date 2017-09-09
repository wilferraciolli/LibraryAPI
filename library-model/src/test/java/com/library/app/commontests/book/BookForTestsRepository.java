package com.library.app.commontests.book;

import com.library.app.author.model.Author;
import com.library.app.book.model.Book;
import com.library.app.category.model.Category;
import com.library.app.commontests.category.CategoryForTestsRepository;
import org.junit.Ignore;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static com.library.app.commontests.author.AuthorForTestsRepository.donRoberts;
import static com.library.app.commontests.author.AuthorForTestsRepository.erichGamma;
import static com.library.app.commontests.author.AuthorForTestsRepository.johnBrant;
import static com.library.app.commontests.author.AuthorForTestsRepository.johnVlissides;
import static com.library.app.commontests.author.AuthorForTestsRepository.joshuaBloch;
import static com.library.app.commontests.author.AuthorForTestsRepository.kentBeck;
import static com.library.app.commontests.author.AuthorForTestsRepository.martinFowler;
import static com.library.app.commontests.author.AuthorForTestsRepository.ralphJohnson;
import static com.library.app.commontests.author.AuthorForTestsRepository.richardHelm;
import static com.library.app.commontests.author.AuthorForTestsRepository.robertMartin;
import static com.library.app.commontests.author.AuthorForTestsRepository.williamOpdyke;
import static com.library.app.commontests.category.CategoryForTestsRepository.architecture;
import static com.library.app.commontests.category.CategoryForTestsRepository.java;

/**
 * The type Book for tests repository.
 */
@Ignore
public final class BookForTestsRepository {

    private BookForTestsRepository() {
    }

    /**
     * Clean code book.
     *
     * @return the book
     */
    public static Book cleanCode() {
        final Book book = new Book();
        book.setTitle("Clean Code: A Handbook of Agile Software Craftsmanship");
        book.setDescription("Even bad code can function. But if code isn't clean, ...");
        book.setCategory(CategoryForTestsRepository.cleanCode());
        book.addAuthor(robertMartin());
        book.setPrice(35.06);

        return book;
    }

    /**
     * Design patterns book.
     *
     * @return the book
     */
    public static Book designPatterns() {
        final Book book = new Book();
        book.setTitle("Design Patterns: Elements of Reusable Object-Oriented Software");
        book.setDescription("Design Patterns is a modern classic in the literature of object-oriented development");
        book.setCategory(architecture());
        book.addAuthor(erichGamma());
        book.addAuthor(johnVlissides());
        book.addAuthor(ralphJohnson());
        book.addAuthor(richardHelm());
        book.setPrice(48.94D);

        return book;
    }

    /**
     * Peaa book.
     *
     * @return the book
     */
    public static Book peaa() {
        final Book book = new Book();
        book.setTitle("Patterns of Enterprise Application Architecture");
        book.setDescription("Developers of enterprise applications (e.g reservation systems, supply...");
        book.setCategory(architecture());
        book.addAuthor(martinFowler());
        book.setPrice(52D);

        return book;
    }

    /**
     * Refactoring book.
     *
     * @return the book
     */
    public static Book refactoring() {
        final Book book = new Book();
        book.setTitle("Refactoring: Improving the Design of Existing Code");
        book.setDescription("Your class library works, but could it be better?...");
        book.setCategory(CategoryForTestsRepository.cleanCode());
        book.addAuthor(martinFowler());
        book.addAuthor(kentBeck());
        book.addAuthor(johnBrant());
        book.addAuthor(williamOpdyke());
        book.addAuthor(donRoberts());
        book.setPrice(31.16D);

        return book;
    }

    /**
     * Effective java book.
     *
     * @return the book
     */
    public static Book effectiveJava() {
        final Book book = new Book();
        book.setTitle("Effective Java (2nd Edition)");
        book.setDescription("Are you looking for a deeper understanding of the Java programming language so ..");
        book.setCategory(java());
        book.addAuthor(joshuaBloch());
        book.setPrice(38.80D);

        return book;
    }

    /**
     * Book with id book.
     *
     * @param book the book
     * @param id   the id
     * @return the book
     */
    public static Book bookWithId(final Book book, final Long id) {
        book.setId(id);
        return book;
    }

    /**
     * All books list.
     *
     * @return the list
     */
    public static List<Book> allBooks() {
        return Arrays.asList(cleanCode(), designPatterns(), peaa(), refactoring(), effectiveJava());
    }

    /**
     * Normalize dependencies book. Get Categories ad Authors from the database so it can be assigned to the book.
     *
     * @param book the book
     * @param em   the em
     * @return the book
     */
    public static Book normalizeDependencies(final Book book, final EntityManager em) {
        final Category managedCategory = findByPropertyNameAndValue(em, Category.class, "name", book.getCategory()
                .getName());
        book.setCategory(managedCategory);

        for (final Author author : book.getAuthors()) {
            final Author managedAuthor = findByPropertyNameAndValue(em, Author.class, "name", author.getName());
            author.setId(managedAuthor.getId());
        }

        return book;
    }

    @SuppressWarnings("unchecked")
    private static <T> T findByPropertyNameAndValue(final EntityManager em, final Class<T> clazz,
                                                    final String propertyName, final String propertyValue) {
        return (T) em
                .createQuery("Select o From " + clazz.getSimpleName() +
                        " o Where o." + propertyName + " = :propertyValue")
                .setParameter("propertyValue", propertyValue)
                .getSingleResult();
    }

}