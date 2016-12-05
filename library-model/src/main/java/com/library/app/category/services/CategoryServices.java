package com.library.app.category.services;

import java.util.List;

import javax.ejb.Local;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.common.exception.FieldNotValidException;

/**
 * Interface to define methods that Category Service will have. This is a local interface that can only be invoked by
 * another class in the same package. Normally a Stateless class.
 *
 * @author wilferaciolli
 *
 */
@Local
public interface CategoryServices {

    /**
     * Method to add a category. Throws FieldNotValidException default custom exception handler implementation. Plus
     * Throws CategoryExistentException to handle add duplicated category.
     *
     * @param category
     *            The category to add.
     * @return Category added.
     */
    Category add(Category category) throws FieldNotValidException, CategoryExistentException;

}
