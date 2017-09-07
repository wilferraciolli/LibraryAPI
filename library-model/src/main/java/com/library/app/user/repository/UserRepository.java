package com.library.app.user.repository;

import com.library.app.common.model.PaginatedData;
import com.library.app.common.repository.GenericRepository;
import com.library.app.user.model.User;
import com.library.app.user.model.filter.UserFilter;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

/**
 * The type User repository.
 */
@Stateless
public class UserRepository extends GenericRepository<User> {

    /**
     * The Em.
     */
    @PersistenceContext
    EntityManager em;

    @Override
    protected Class<User> getPersistentClass() {
        return User.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Already exists boolean.
     *
     * @param user the user
     * @return the boolean
     */
    public boolean alreadyExists(final User user) {
        return alreadyExists("email", user.getEmail(), user.getId());
    }

    /**
     * Find user by email user.
     *
     * @param email the email
     * @return the user
     */
    public User findByEmail(final String email) {
        try {
            return (User) em.createQuery("Select e from User e where e.email = :email")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (final NoResultException e) {
            return null;
        }
    }

    /**
     * Find users by filter paginated data.
     *
     * @param userFilter the user filter
     * @return the paginated data
     */
    public PaginatedData<User> findByFilter(final UserFilter userFilter) {

        //build the clause based on parameters passed by the client
        final StringBuilder clause = new StringBuilder("WHERE e.id is not null");
        final Map<String, Object> queryParameters = new HashMap<>();

        //get user name filter
        if (userFilter.getName() != null) {
            clause.append(" And Upper(e.name) Like Upper(:name)");
            queryParameters.put("name", "%" + userFilter.getName() + "%");
        }

        //get user type parameter
        if (userFilter.getUserType() != null) {
            clause.append(" And e.userType = :userType");
            queryParameters.put("userType", userFilter.getUserType());
        }

        return findByParameters(clause.toString(), userFilter.getPaginationData(), queryParameters, "name ASC");
    }
}
