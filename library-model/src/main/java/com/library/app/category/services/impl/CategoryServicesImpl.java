package com.library.app.category.services.impl;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.utils.ValidationUtils;
import com.library.app.logaudit.interceptor.Auditable;
import com.library.app.logaudit.interceptor.LogAuditInterceptor;
import com.library.app.logaudit.model.LogAudit;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.validation.Validator;
import java.util.List;

/**
 * Implementation class for Category services interface. This is a stateless class that invokes method on its Local
 * interface.
 *
 * @author wilferaciolli
 */
@Stateless
@Interceptors(LogAuditInterceptor.class)
public class CategoryServicesImpl implements CategoryServices {

    // String to define which column to order by when passing values to a database.
    private static final String ORDERBY = "name";

    // define and instance of Validator to validate(annotations) the Category entity.
    @Inject
    Validator validator;

    // define the category repository to handle database calls
    @Inject
    CategoryRepository categoryRepository;

    // note that on the interface it has all the exception that it may throw
    @Override
    @Auditable(action = LogAudit.Action.ADD)
    public Category add(final Category category) {
        // check for constraints violations
        validateCategory(category);

        return categoryRepository.add(category);
    }

    // method to update a category.
    @Override
    @Auditable(action = LogAudit.Action.UPDATE)
    public void update(final Category category) {
        // check for constraints violations
        validateCategory(category);

        // check if the category exists(validate the category ID passed to update)
        if (!categoryRepository.existsById(category.getId())) {
            throw new CategoryNotFoundException();
        }

        // update the category
        categoryRepository.update(category);
    }

    // method to find a category by id.
    @Override
    public Category findById(final long id) throws CategoryNotFoundException {
        final Category category = categoryRepository.findById(id);
        if (category == null) {
            throw new CategoryNotFoundException();
        }
        return category;
    }

    /**
     * get all categories.
     */
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll(ORDERBY);
    }

    /**
     * Method to to validate constraints and to check if a category already exists.
     *
     * @param category The category to validate.
     */
    private void validateCategory(final Category category) {
        ValidationUtils.validateEntityFields(validator, category);

        // check if category already exists on the database (to avoid duplication)
        if (categoryRepository.alreadyExists(category)) {
            throw new CategoryExistentException();
        }
    }

}
