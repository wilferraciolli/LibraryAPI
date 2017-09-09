package com.library.app.book.repository;

import com.library.app.book.model.Book;
import com.library.app.common.repository.GenericRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * The type Book repository.
 */
@Stateless
public class BookRepository extends GenericRepository<Book> {

    /**
     * The Entity manager.
     */
    @PersistenceContext
    EntityManager em;

    @Override
    protected Class<Book> getPersistentClass() {
        return Book.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}