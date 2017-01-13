package com.library.app.category.exception;

        import javax.ejb.ApplicationException;

/**
 * This is and exception handler class that is very specific to handle duplication of a category by its name.
 * This class is annotated with @ApplicationException because it is required that the runtime exception is thrown as application exception instead of 500 internal error9exception).
 * It has to be an application exception because it is handled in CategoryResource.Java on the "add" method.
 *
 * @author wilferaciolli
 */
@ApplicationException
public class CategoryExistentException extends RuntimeException {
    private static final long serialVersionUID = 1L;

}
