package com.library.app.common.repository;

import com.library.app.common.model.PaginatedData;
import com.library.app.common.model.filter.PaginationData;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

    /**
     * Find by parameters paginated data. Helper method to return paginated data.
     *
     * @param clause                        the clause, the JPQL query
     * @param paginationData                the pagination data, the data
     * @param queryParameters               the query parameters, the parameters
     * @param defaultSortFieldWithDirection the default sort field with direction, sort order
     * @return the paginated data
     */
    @SuppressWarnings("unchecked")
    protected PaginatedData<T> findByParameters(final String clause, final PaginationData paginationData,
                                                final Map<String, Object> queryParameters, final String defaultSortFieldWithDirection) {

        //get the clause of the search
        final String clauseSort = "Order by e." + getSortField(paginationData, defaultSortFieldWithDirection);

        //build the query passing the query, the parameters and the sort filters
        final Query queryEntities = getEntityManager().createQuery(
                "Select e From " + getPersistentClass().getSimpleName()
                        + " e " + clause + " " + clauseSort);

        //add filters to the query
        applyQueryParametersOnQuery(queryParameters, queryEntities);

        //paginate the query
        applyPaginationOnQuery(paginationData, queryEntities);

        final List<T> entities = queryEntities.getResultList();

        return new PaginatedData<T>(countWithFilter(clause, queryParameters), entities);
    }

    /**
     * Count with filter int.
     *
     * @param clause          the clause
     * @param queryParameters the query parameters
     * @return the int
     */
    private int countWithFilter(final String clause, final Map<String, Object> queryParameters) {
        final Query queryCount = getEntityManager().createQuery(
                "Select count(e) From " + getPersistentClass().getSimpleName() + " e " + clause);

        //add parameters
        applyQueryParametersOnQuery(queryParameters, queryCount);
        return ((Long) queryCount.getSingleResult()).intValue();
    }

    /**
     * Apply pagination on query.
     *
     * @param paginationData the pagination data
     * @param query          the query
     */
    private void applyPaginationOnQuery(final PaginationData paginationData, final Query query) {
        if (paginationData != null) {
            query.setFirstResult(paginationData.getFirstResult());
            query.setMaxResults(paginationData.getMaxResults());
        }
    }

    /**
     * Gets sort field. Return default if null.
     *
     * @param paginationData   the pagination data
     * @param defaultSortField the default sort field
     * @return the sort field
     */
    private String getSortField(final PaginationData paginationData, final String defaultSortField) {
        if (paginationData == null || paginationData.getOrderField() == null) {
            return defaultSortField;
        }
        return paginationData.getOrderField() + " " + getSortDirection(paginationData);
    }

    /**
     * Gets sort direction. ASC or DESC.
     *
     * @param paginationData the pagination data
     * @return the sort direction
     */
    private String getSortDirection(final PaginationData paginationData) {
        return paginationData.isAscending() ? "ASC" : "DESC";
    }

    /**
     * Apply query parameters on query.
     *
     * @param queryParameters the query parameters
     * @param query           the query
     */
    private void applyQueryParametersOnQuery(final Map<String, Object> queryParameters, final Query query) {
        for (final Entry<String, Object> entryMap : queryParameters.entrySet()) {
            query.setParameter(entryMap.getKey(), entryMap.getValue());
        }
    }

}
