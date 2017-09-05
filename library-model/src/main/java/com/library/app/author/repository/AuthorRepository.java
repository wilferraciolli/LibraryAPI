package com.library.app.author.repository;

import com.library.app.author.model.Author;
import com.library.app.author.model.filter.AuthorFilter;
import com.library.app.common.model.PaginatedData;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to persist data to the database for author entity. Repository.
 * Created by wilferaciolli on 21/02/2017.
 */
@Stateless
public class AuthorRepository {

    /**
     * The Em.
     */
//declare the persistent context
    @PersistenceContext
    EntityManager em;

    /**
     * Add a new author to the database.
     *
     * @param author The authotr object.
     * @return The newly added author.
     */
    public Author add(final Author author) {
        em.persist(author);
        return author;
    }

    /**
     * Find Author by id.
     *
     * @param id The id.
     * @return The Author object if exists.
     */
    public Author findById(final Long id) {
        if (id == null) {
            return null;
        }
        return em.find(Author.class, id);
    }

    /**
     * Update author.
     *
     * @param author The author object.
     */
    public void update(final Author author) {
        em.merge(author);
    }

    /**
     * Check whether author exists by given id.
     *
     * @param id the id
     * @return boolean boolean
     */
    public boolean existsById(final long id) {
        return em.createQuery("Select 1 From Author e where e.id = :id")
                .setParameter("id", id)
                .setMaxResults(1)
                .getResultList().size() > 0;
    }

    /**
     * Find by filter paginated data. This method return a list of Authors filtered.
     *
     * @param filter the filter
     * @return the paginated data
     */
    public PaginatedData<Author> findByFilter(final AuthorFilter filter) {
        final StringBuilder clause = new StringBuilder("WHERE e.id is not null");
        final Map<String, Object> queryParameters = new HashMap<>();

        //add the filter to the query
        if (filter.getName() != null) {
            clause.append(" AND UPPER(e.name) Like UPPER(:name)");
            queryParameters.put("name", "%" + filter.getName() + "%");
        }

        //Add the sort by field
        final StringBuilder clauseSort = new StringBuilder();
        if (filter.hasOrderField()) {
            clauseSort.append("Order by e." + filter.getPaginationData().getOrderField());
            clauseSort.append(filter.getPaginationData().isAscending() ? " ASC" : " DESC");
        } else {
            clauseSort.append("Order by e.name ASC");
        }

        //Prepare the query to get the authors by filter
        final Query queryAuthors = em.createQuery("Select e from Author e " + clause.toString() + " " + clauseSort.toString());
        applyQueryParametersOnQuery(queryParameters, queryAuthors);
        if (filter.hasPaginationData()) {
            queryAuthors.setFirstResult(filter.getPaginationData().getFirstResult());
            queryAuthors.setMaxResults(filter.getPaginationData().getMaxResults());
        }

        //get the list of authors
        final List<Author> authors = queryAuthors.getResultList();

        //get the count to provide the pagination the number of rows
        final Query queryCount = em.createQuery("Select count(e) From Author e " + clause.toString());

        applyQueryParametersOnQuery(queryParameters, queryCount);
        final Integer count = ((Long) queryCount.getSingleResult()).intValue();

        //return the result
        return new PaginatedData<Author>(count, authors);
    }

    /**
     * Apply query parameters on query. Helper method to set query parameters.
     *
     * @param queryParameters the query parameters
     * @param query           the query
     */
    private void applyQueryParametersOnQuery(final Map<String, Object> queryParameters, final Query query) {
        for (final Map.Entry<String, Object> entryMap : queryParameters.entrySet()) {
            query.setParameter(entryMap.getKey(), entryMap.getValue());
        }
    }
}
