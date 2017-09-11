package com.library.app.commontests.order;

import com.library.app.book.model.Book;
import com.library.app.common.utils.DateUtils;
import com.library.app.order.model.Order;
import com.library.app.order.model.Order.OrderStatus;
import com.library.app.order.model.OrderItem;
import com.library.app.user.model.Customer;
import org.junit.Ignore;

import javax.persistence.EntityManager;

import static com.library.app.commontests.book.BookForTestsRepository.bookWithId;
import static com.library.app.commontests.book.BookForTestsRepository.designPatterns;
import static com.library.app.commontests.book.BookForTestsRepository.refactoring;
import static com.library.app.commontests.user.UserForTestsRepository.johnDoe;
import static com.library.app.commontests.user.UserForTestsRepository.mary;

/**
 * The type Order for tests repository. Helper method to create some dummy orders.
 */
@Ignore
public final class OrderForTestsRepository {

    private OrderForTestsRepository() {
    }

    /**
     * Order delivered order.
     *
     * @return the order
     */
    public static Order orderDelivered() {
        final Order order = new Order();
        order.setCustomer((Customer) mary());

        order.addItem(bookWithId(designPatterns(), 1L), 2);
        order.addItem(bookWithId(refactoring(), 2L), 1);

        order.setInitialStatus();
        order.calculateTotal();

        order.addHistoryEntry(OrderStatus.DELIVERED);

        return order;
    }

    /**
     * Order reserved order.
     *
     * @return the order
     */
    public static Order orderReserved() {
        final Order order = new Order();
        order.setCustomer((Customer) johnDoe());

        order.addItem(bookWithId(designPatterns(), 1L), 2);
        order.addItem(bookWithId(refactoring(), 2L), 1);

        order.setInitialStatus();
        order.calculateTotal();

        return order;
    }

    /**
     * Order with id order.
     *
     * @param order the order
     * @param id    the id
     * @return the order
     */
    public static Order orderWithId(final Order order, final Long id) {
        order.setId(id);
        return order;
    }

    /**
     * Order created at order.
     *
     * @param order    the order
     * @param dateTime the date time
     * @return the order
     */
    public static Order orderCreatedAt(final Order order, final String dateTime) {
        order.setCreatedAt(DateUtils.getAsDateTime(dateTime));
        return order;
    }

    /**
     * Normalize dependencies order.
     *
     * @param order the order
     * @param em    the em
     * @return the order
     */
    public static Order normalizeDependencies(final Order order, final EntityManager em) {
        order.setCustomer(findByPropertyNameAndValue(em, Customer.class, "name", order.getCustomer()
                .getName()));

        for (final OrderItem item : order.getItems()) {
            item.setBook(findByPropertyNameAndValue(em, Book.class, "title", item.getBook().getTitle()));
        }
        return order;
    }

    @SuppressWarnings("unchecked")
    private static <T> T findByPropertyNameAndValue(final EntityManager em, final Class<T> clazz,
                                                    final String propertyName, final String propertyValue) {
        return (T) em
                .createQuery("Select o From " + clazz.getSimpleName() +
                        " o Where o." + propertyName + " = :propertyValue")
                .setParameter("propertyValue", propertyValue)
                .getSingleResult();
    }

}
