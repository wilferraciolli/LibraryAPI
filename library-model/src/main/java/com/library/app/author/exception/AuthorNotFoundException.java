package com.library.app.author.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class AuthorNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
}
