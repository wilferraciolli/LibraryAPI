package com.library.app.category.exception;

import javax.ejb.ApplicationException;

/**
 * Class to handle Category Not Found on hte database exception. It is mainly used to check if the category to be update
 * exists before updating it.
 * This class is annotated with @ApplicationException because it is required that the runtime exception is thrown as application exception instead of 500 internal error9exception).
 * It has to be an application exception because it is handled in CategoryResource.Java on the "add" method.
 *
 * @author wilferaciolli
 */
@ApplicationException
public class CategoryNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

}
