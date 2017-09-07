package com.library.app.author.repository;

import com.library.app.author.model.Author;
import com.library.app.author.model.filter.AuthorFilter;
import com.library.app.common.model.PaginatedData;
import com.library.app.common.repository.GenericRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to persist data to the database for author entity. Repository.
 * Created by wilferaciolli on 21/02/2017.
 */
@Stateless
public class AuthorRepository extends GenericRepository<Author> {

    /**
     * The Em.
     */
    //declare the persistent context
    @PersistenceContext
    EntityManager em;

    @Override
    protected Class<Author> getPersistentClass() {
        return Author.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
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
        return findByParameters(clause.toString(), filter.getPaginationData(), queryParameters, "name ASC");
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
