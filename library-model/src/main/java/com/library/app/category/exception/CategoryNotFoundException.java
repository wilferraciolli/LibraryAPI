package com.library.app.category.exception;

/**
 * Class to handle Category Not Found on hte database exception. It is mainly used to check if the category to be update
 * exists before updating it.
 *
 * @author wilferaciolli
 *
 */
public class CategoryNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

}
