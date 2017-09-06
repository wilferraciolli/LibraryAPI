package com.library.app.common.utils;

import com.library.app.common.exception.FieldNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.Set;

/**
 * The type Validation utils class.
 */
public class ValidationUtils {

    /**
     * Method to create an instance on Constraint Validator to validate an Entity. It will loop through every
     * field and check for constraints and throw exceptions on validation such as @NotNull or @Size.
     *
     * @param validator the validator
     * @param entity    the entity to validate
     */
    public static <T> void validateEntityFields(final Validator validator, final T entity) {
        // instantiate the validator to validate constraint on violation annotations(@NotNull,@Size...)
        final Set<ConstraintViolation<T>> errors = validator.validate(entity);
        final Iterator<ConstraintViolation<T>> itErrors = errors.iterator();

        // iterate through the errors if there are any
        if (itErrors.hasNext()) {
            final ConstraintViolation<T> violation = itErrors.next();
            // throw FieldNotValidException by getting which fieldName has occurred the violation
            throw new FieldNotValidException(violation.getPropertyPath().toString(), violation.getMessage());
        }
    }
}
