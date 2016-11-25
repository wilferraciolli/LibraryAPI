package com.library.app.commontests.utils;

import org.junit.Ignore;

import javax.persistence.EntityManager;

/**
 * Class to implement DBCommand interface. This class is to instantiate an entity manager and execute an hibernate CRUD.
 * Created by wilferaciolli on 25/11/2016.
 */
@Ignore
public class DBCommandTransactionExecutor {

    private EntityManager em;

    /**
     * Default constructor.
     *
     * @param em The entity manager.
     */
    public DBCommandTransactionExecutor(EntityManager em) {
        this.em = em;
    }

    /**
     * Execute command.
     *
     * @param dbCommand The command to execute
     * @param <T> The object
     * @return The object.
     */
    public <T> T executeCommand(final DBCommand<T> dbCommand) {

        try {
            //execute entity manager command
            em.getTransaction().begin();
            final T toReturn = dbCommand.execute();

            em.getTransaction().commit();
            em.clear();
            return toReturn;
        } catch (final Exception e) {
            //thrown exception
            e.printStackTrace();
            em.getTransaction().rollback();
            throw new IllegalStateException(e);
        }
    }
}
