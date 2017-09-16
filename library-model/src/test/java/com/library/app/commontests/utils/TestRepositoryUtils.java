package com.library.app.commontests.utils;

import javax.persistence.EntityManager;

/**
 * The type Test repository utils. This is a test utils class to remove code duplication.
 */
public final class TestRepositoryUtils {

    private TestRepositoryUtils() {
    }

    /**
     * Find by property name and value t.
     *
     * @param <T>           the type parameter
     * @param em            the em
     * @param clazz         the clazz
     * @param propertyName  the property name
     * @param propertyValue the property value
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public static <T> T findByPropertyNameAndValue(final EntityManager em, final Class<T> clazz,
                                                   final String propertyName, final String propertyValue) {
        return (T) em
                .createQuery("Select o From " + clazz.getSimpleName() +
                        " o Where o." + propertyName + " = :propertyValue")
                .setParameter("propertyValue", propertyValue)
                .getSingleResult();
    }
}
