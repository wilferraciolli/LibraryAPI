package com.library.app.order.repository;

import com.library.app.common.repository.GenericRepository;
import com.library.app.order.model.Order;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * The type Order repository.
 */
@Stateless
public class OrderRepository extends GenericRepository<Order> {

    /**
     * The Entity manager.
     */
    @PersistenceContext
    EntityManager em;

    @Override
    protected Class<Order> getPersistentClass() {
        return Order.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}