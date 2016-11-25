package com.library.app.category.repository;

import com.library.app.category.model.Category;

import javax.persistence.EntityManager;

/**
 * Repository for categories.
 * Created by wilferaciolli on 25/11/2016.
 */
public class CategoryRepository {

    //set the entity manager
    public EntityManager em;

    /**
     * Add(Persist) category tot he database.
     * @param category The category to add.
     * @return The category added.
     */
    public Category add(Category category) {
        em.persist(category);
        return category;
    }

    /**
     * find category based on given id.
     * @param id The category id.
     * @return Category
     */
    public Category findById(Long id) {
        return em.find(Category.class,id );
    }
}
