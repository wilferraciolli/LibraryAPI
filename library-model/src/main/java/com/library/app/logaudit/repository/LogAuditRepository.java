package com.library.app.logaudit.repository;

import com.library.app.common.model.PaginatedData;
import com.library.app.common.repository.GenericRepository;
import com.library.app.logaudit.model.LogAudit;
import com.library.app.logaudit.model.filter.LogAuditFilter;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Log audit repository.
 */
@Stateless
public class LogAuditRepository extends GenericRepository<LogAudit> {

    /**
     * The Em.
     */
    @PersistenceContext
    EntityManager em;

    @Override
    protected Class<LogAudit> getPersistentClass() {
        return LogAudit.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Find by filter paginated data.
     *
     * @param logAuditFilter the log audit filter
     * @return the paginated data
     */
    public PaginatedData<LogAudit> findByFilter(final LogAuditFilter logAuditFilter) {
        final StringBuilder clause = new StringBuilder("WHERE e.id is not null");
        final Map<String, Object> queryParameters = new HashMap<>();
        if (logAuditFilter.getStartDate() != null) {
            clause.append(" AND e.createdAt >= :startDate");
            queryParameters.put("startDate", logAuditFilter.getStartDate());
        }
        if (logAuditFilter.getEndDate() != null) {
            clause.append(" AND e.createdAt <= :endDate");
            queryParameters.put("endDate", logAuditFilter.getEndDate());
        }
        if (logAuditFilter.getUserId() != null) {
            clause.append(" AND e.user.id = :userId");
            queryParameters.put("userId", logAuditFilter.getUserId());
        }

        return findByParameters(clause.toString(), logAuditFilter.getPaginationData(), queryParameters,
                "createdAt Desc");
    }

}
