package com.library.app.book.repository;

import com.library.app.book.model.Book;
import com.library.app.book.model.filter.BookFilter;
import com.library.app.common.model.PaginatedData;
import com.library.app.common.repository.GenericRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * Find by filter paginated data.
     *
     * @param bookFilter the book filter
     * @return the paginated data
     */
    public PaginatedData<Book> findByFilter(final BookFilter bookFilter) {
        final StringBuilder clause = new StringBuilder("Where e.id is not null");
        final Map<String, Object> queryParameters = new HashMap<>();

        if (bookFilter.getTitle() != null) {
            clause.append(" AND UPPER(e.title) Like UPPER(:title)");
            queryParameters.put("title", "%" + bookFilter.getTitle() + "%");
        }
        if (bookFilter.getCategoryId() != null) {
            clause.append(" AND e.category.id = :categoryId");
            queryParameters.put("categoryId", bookFilter.getCategoryId());
        }

        return findByParameters(clause.toString(), bookFilter.getPaginationData(), queryParameters, "title ASC");
    }

}