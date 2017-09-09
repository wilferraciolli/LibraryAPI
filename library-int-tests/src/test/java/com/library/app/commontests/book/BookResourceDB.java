package com.library.app.commontests.book;

import com.library.app.book.services.BookServices;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.library.app.commontests.book.BookForTestsRepository.allBooks;
import static com.library.app.commontests.book.BookForTestsRepository.normalizeDependencies;

/**
 * The type Book resource db.
 */
@Path("/DB/books")
@Produces(MediaType.APPLICATION_JSON)
public class BookResourceDB {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private BookServices bookServices;

    /**
     * Add all books to the database for tests. It first need to get the category and authors to be able to successfully add a book.
     */
    @POST
    public void addAll() {
        allBooks().forEach((book) -> bookServices.add(normalizeDependencies(book, em)));
    }

}