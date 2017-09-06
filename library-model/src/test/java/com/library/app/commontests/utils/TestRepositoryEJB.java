package com.library.app.commontests.utils;

import com.library.app.author.model.Author;
import com.library.app.category.model.Category;
import org.junit.Ignore;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class to clean all the database when running integration tests.
 * It takes a list of entities and delete them all from the database.
 * Created by wilferaciolli on 13/01/2017.
 */
@Ignore
@Stateless
public class TestRepositoryEJB {

    @PersistenceContext
    private EntityManager em;

    //the list of entities to be removed from the database
    private static final List<Class<?>> ENTITIES_TO_REMOVE = Arrays.asList(Category.class, Author.class);

    /**
     * Delete all entities from the database.
     */
    public void deleteAll() {
        for (final Class<?> entityClass : ENTITIES_TO_REMOVE) {
            deleteAllForEntity(entityClass);
        }
    }

    /**
     * Get every entity and delete one by one from the database.
     * @param entityClass The entity class to be removed.
     */
    @SuppressWarnings("unchecked")
    private void deleteAllForEntity(final Class<?> entityClass) {
        //get all the rows that matches the entity given to delete
        final List<Object> rows = em.createQuery("Select e From " + entityClass.getSimpleName() + " e").getResultList();
        for (final Object row : rows) {
            //delete from the database
            em.remove(row);
        }
    }

}