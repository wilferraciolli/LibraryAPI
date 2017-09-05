package com.library.app.common.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * The type Generic repository. This abstract repository hold the default CRUD repository that can be extended by repositories.
 *
 * @param <T> the type parameter
 */
public abstract class GenericRepository<T> {

    /**
     * Gets persistent class.
     *
     * @return the persistent class
     */
    protected abstract Class<T> getPersistentClass();

    /**
     * Gets entity manager.
     *
     * @return the entity manager
     */
    protected abstract EntityManager getEntityManager();

    /**
     * Add t.
     *
     * @param entity the entity
     * @return the t
     */
    public T add(final T entity) {
        getEntityManager().persist(entity);
        return entity;
    }

    /**
     * Find by id t.
     *
     * @param id the id
     * @return the t
     */
    public T findById(final Long id) {
        //check if the id given is null
        if (id == null) {
            return null;
        }
        return getEntityManager().find(getPersistentClass(), id);
    }

    /**
     * Update.
     *
     * @param entity the entity
     */
    public void update(final T entity) {
        getEntityManager().merge(entity);
    }


    /**
     * Find all list.
     *
     * @param orderField the order field
     * @return the list
     */
    public List<T> findAll(final String orderField) {

        return getEntityManager().createQuery("SELECT e FROM " + getPersistentClass().getSimpleName() + " e ORDER BY  e." + orderField).getResultList();
    }

    /**
     * Already exists boolean.
     *
     * @param propertyName  the property name
     * @param propertyValue the property value
     * @param id            the id
     * @return the boolean
     */
    public boolean alreadyExists(final String propertyName, final String propertyValue, final Long id) {
        final StringBuilder jpql = new StringBuilder();
        jpql.append("Select 1 From " + getPersistentClass().getSimpleName() + " e where e." + propertyName
                + " = :propertyValue");
        if (id != null) {
            jpql.append(" and e.id != :id");
        }

        final Query query = getEntityManager().createQuery(jpql.toString());
        query.setParameter("propertyValue", propertyValue);
        if (id != null) {
            query.setParameter("id", id);
        }

        return query.setMaxResults(1).getResultList().size() > 0;
    }

    /**
     * Exists by id boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public boolean existsById(final Long id) {
        return getEntityManager()
                .createQuery("SELECT 1 FROM " + getPersistentClass().getSimpleName() + " e WHERE e.id = :id")
                .setParameter("id", id)
                .setMaxResults(1)
                .getResultList().size() > 0;
    }

}
