package com.library.app.category.repository;

import com.library.app.category.model.Category;
import com.library.app.common.repository.GenericRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Class to manage the database. This is the repository which will persist and read data from the database. Stateless
 * Java bean that can be ejected to run once for a transaction.
 *
 * @author wilferaciolli
 */
@Stateless
public class CategoryRepository extends GenericRepository<Category> {

    //set the entity manager and inject it
    @PersistenceContext
    public EntityManager em;

    @Override
    protected Class<Category> getPersistentClass() {
        return Category.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


    /**
     * Method to check if a category already exists on the database before adding it. It checks by category name.
     *
     * @param category
     *            The category to check if already exists.
     * @return
     */
    public boolean alreadyExists(final Category category) {
        return alreadyExists("name", category.getName(), category.getId());
    }

}
