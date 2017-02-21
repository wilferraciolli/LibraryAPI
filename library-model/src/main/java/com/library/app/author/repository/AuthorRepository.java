package com.library.app.author.repository;

import com.library.app.author.model.Author;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Class to persist data to the database for author entity. Repository.
 * Created by wilferaciolli on 21/02/2017.
 */
@Stateless
public class AuthorRepository {

    //declare the persistent context
    @PersistenceContext
    EntityManager em;

    /**
     * Add a new author to the database.
     * @param author The authotr object.
     * @return The newly added author.
     */
    public Author add(final Author author) {
        em.persist(author);
        return author;
    }

    /**
     * Find Author by id.
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
     * @param author The author object.
     */
    public void update(final Author author) {
        em.merge(author);
    }

    /**
     * Check whether author exists by given id.
     * @param id
     * @return
     */
    public boolean existsById(final long id) {
        return em.createQuery("Select 1 From Author e where e.id = :id")
                .setParameter("id", id)
                .setMaxResults(1)
                .getResultList().size() > 0;
    }

}
