package com.library.app.book.services;

import com.library.app.author.exception.AuthorNotFoundException;
import com.library.app.book.exception.BookNotFoundException;
import com.library.app.book.model.Book;
import com.library.app.book.model.filter.BookFilter;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.model.PaginatedData;

import javax.ejb.Local;

/**
 * The interface Book services.
 */
@Local
public interface BookServices {

    /**
     * Add book.
     *
     * @param book the book
     * @return the book
     * @throws FieldNotValidException    the field not valid exception
     * @throws CategoryNotFoundException the category not found exception
     * @throws AuthorNotFoundException   the author not found exception
     */
    Book add(Book book) throws FieldNotValidException, CategoryNotFoundException, AuthorNotFoundException;

    /**
     * Update.
     *
     * @param book the book
     * @throws FieldNotValidException    the field not valid exception
     * @throws CategoryNotFoundException the category not found exception
     * @throws AuthorNotFoundException   the author not found exception
     * @throws BookNotFoundException     the book not found exception
     */
    void update(Book book) throws FieldNotValidException, CategoryNotFoundException, AuthorNotFoundException,
            BookNotFoundException;

    /**
     * Find by id book.
     *
     * @param id the id
     * @return the book
     * @throws BookNotFoundException the book not found exception
     */
    Book findById(Long id) throws BookNotFoundException;

    /**
     * Find by filter paginated data.
     *
     * @param bookFilter the book filter
     * @return the paginated data
     */
    PaginatedData<Book> findByFilter(BookFilter bookFilter);

}