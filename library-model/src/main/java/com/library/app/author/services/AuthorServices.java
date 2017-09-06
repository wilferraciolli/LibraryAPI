package com.library.app.author.services;

import com.library.app.author.exception.AuthorNotFoundException;
import com.library.app.author.model.Author;
import com.library.app.author.model.filter.AuthorFilter;
import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.model.PaginatedData;

import javax.ejb.Local;

/**
 * The interface Author services.
 */
@Local
public interface AuthorServices {

    /**
     * Add author.
     *
     * @param author the author
     * @return the author
     * @throws FieldNotValidException the field not valid exception
     */
    Author add(Author author) throws FieldNotValidException;


    /**
     * Update author.
     *
     * @param author the author
     * @throws FieldNotValidException  the field not valid exception
     * @throws AuthorNotFoundException the author not found exception
     */
    void update(Author author) throws FieldNotValidException, AuthorNotFoundException;

    /**
     * Find by id author id.
     *
     * @param id the id
     * @return the author
     * @throws AuthorNotFoundException the author not found exception
     */
    Author findById(Long id) throws AuthorNotFoundException;

    /**
     * Find by filter paginated data.
     *
     * @param authorFilter the author filter
     * @return the paginated data
     */
    PaginatedData<Author> findByFilter(AuthorFilter authorFilter);
}
