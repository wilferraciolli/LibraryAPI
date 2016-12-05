package com.library.app.category.services.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;

/**
 * implementation class for Category services interface. This is a stateless class that invokes method on its Local
 * interface.
 *
 * @author wilferaciolli
 */
@Stateless
public class CategoryServicesImpl implements CategoryServices {

    // String to define which column to order by when passing values to a database.
    private static String ORDERBY = "name";

    // define and instance of Validator to validate(annotations) the Category entity.
    @Inject
    Validator validator;

    // define the category repository to handle database calls
    @Inject
    CategoryRepository categoryRepository;

    // note that on the interface it has all the exception that it may throw
    @Override
    public Category add(final Category category) {
        // check for constraints violations
        validateCategory(category);

        return categoryRepository.add(category);
    }





    /**
     * Method to to validate constraints and to check if a category already exists.
     *
     * @param category The category to validate.
     */
    private void validateCategory(final Category category) {
        validateCategoryFields(category);

        // check if category already exists on the database (to avoid duplication)
        if (categoryRepository.alreadyExists(category)) {
            throw new CategoryExistentException();
        }
    }

    /**
     * Method to create an instance on Constraint Validator to validate Category Entity. It will loop through every
     * field and check for constraints and throw exceptions on validation such as @NotNull or @Size.
     *
     * @param category The category to validate.
     */
    private void validateCategoryFields(final Category category) {
        // instantiate the validator to validate constraint on violation annotations(@NotNull,@Size...)
        final Set<ConstraintViolation<Category>> errors = validator.validate(category);
        final Iterator<ConstraintViolation<Category>> itErrors = errors.iterator();

        // iterate through the errors if there are any
        if (itErrors.hasNext()) {
            final ConstraintViolation<Category> violation = itErrors.next();
            // throw FieldNotValidException by getting which fieldName has occurred the violation
            throw new FieldNotValidException(violation.getPropertyPath().toString(), violation.getMessage());
        }
    }
}
