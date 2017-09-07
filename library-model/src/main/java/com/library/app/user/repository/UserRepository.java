package com.library.app.user.repository;

import com.library.app.common.repository.GenericRepository;
import com.library.app.user.model.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

}
