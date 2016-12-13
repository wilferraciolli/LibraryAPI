package com.library.app.category.repository;

import com.library.app.category.model.Category;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Class to manage the database. This is the repository which will persist and read data from the database. Stateless
 * Java bean that can be ejected to run once for a transaction.
 *
 * @author wilferaciolli
 */
@Stateless
public class CategoryRepository {

    //set the entity manager and inject it
    @PersistenceContext
    public EntityManager em;

    /**
     * Add(Persist) category tot he database.
     *
     * @param category The category to add.
     * @return The category added.
     */
    public Category add(Category category) {
        em.persist(category);
        return category;
    }

    /**
     * find category based on given id.
     *
     * @param id The category id.
     * @return Category
     */
    public Category findById(Long id) {
        //check if the id given is null
        if (id == null) {
            return null;
        }
        return em.find(Category.class, id);
    }

    /**
     * Method to update an item on the database.
     *
     * @param category
     */
    public void update(final Category category) {
        em.merge(category);
    }

    /**
     * Method to find all categories from the database
     *
     * @param orderField Name of the column to order by.
     * @return
     */
    public List<Category> findAll(final String orderField) {

        return em.createQuery("SELECT e FROM Category e ORDER BY  e." + orderField).getResultList();
    }

    /**
     * Method to check if a category already exists on the database before adding it. It checks by category name.
     *
     * @param category
     *            The category to check if already exists.
     * @return
     */
    public boolean alreadyExists(final Category category) {
        // create the query
        final StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT 1 FROM Category e WHERE e.name = :name");
        // check if the category's name already has an id, if it has then it is an update therefore should not be
        // flagged as duplicated but should be a merge
        if (category.getId() != null) {
            jpql.append(" AND e.id != :id");
        }

        final Query query = em.createQuery(jpql.toString());
        query.setParameter("name", category.getName());
        // check if the category id is not null, if not null then it is an update therefore should not be flagged as
        // duplicated.
        if (category.getId() != null) {
            query.setParameter("id", category.getId());
        }

        // return true if the category name already exists on the database.
        return query.setMaxResults(1).getResultList().size() > 0;
    }

    /**
     * Method to find if category is already defined on the database by its id.
     *
     * @param id
     *            The category id.
     * @return true if already exists.
     */
    public boolean existsById(final Long id) {
        return em.createQuery("SELECT 1 FROM Category e WHERE e.id = :id")
                .setParameter("id", id)
                .setMaxResults(1)
                .getResultList().size() > 0;
    }
}
