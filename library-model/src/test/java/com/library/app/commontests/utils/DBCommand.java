package com.library.app.commontests.utils;

import org.junit.Ignore;

/**
 * This is not a test class. This is command that need to be executed within a transaction.s
 *
 * @param <T> Generic type object.
 * @author wilferaciolli
 */
@Ignore
public interface DBCommand<T> {

    //Execute transaction
    T execute();
}
