package com.library.app.order.repository;

import com.library.app.book.model.Book;
import com.library.app.commontests.utils.TestBaseRepository;
import com.library.app.order.model.Order;
import com.library.app.order.model.Order.OrderStatus;
import com.library.app.order.model.OrderItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.library.app.commontests.author.AuthorForTestsRepository.allAuthors;
import static com.library.app.commontests.book.BookForTestsRepository.allBooks;
import static com.library.app.commontests.book.BookForTestsRepository.normalizeDependencies;
import static com.library.app.commontests.category.CategoryForTestsRepository.allCategories;
import static com.library.app.commontests.order.OrderForTestsRepository.normalizeDependencies;
import static com.library.app.commontests.order.OrderForTestsRepository.orderDelivered;
import static com.library.app.commontests.order.OrderForTestsRepository.orderReserved;
import static com.library.app.commontests.user.UserForTestsRepository.allUsers;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * The type Order repository utest.
 */
public class OrderRepositoryUtest extends TestBaseRepository {

    private OrderRepository orderRepository;

    /**
     * Init test case.
     */
    @Before
    public void initTestCase() {
        initializeTestDB();

        orderRepository = new OrderRepository();
        orderRepository.em = em;

        //load books to DB so orders can be created
        loadBooks();
    }

    /**
     * Sets down test case.
     */
    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    /**
     * Add an order and find it.
     */
    @Test
    public void addAnOrderAndFindIt() {
        final Long orderAddedId = dbCommandExecutor.executeCommand(() -> {
            return orderRepository.add(normalizeDependencies(orderDelivered(), em)).getId();
        });
        assertThat(orderAddedId, is(notNullValue()));

        final Order orderAdded = orderRepository.findById(orderAddedId);
        assertActualOrderWithExpectedOrder(orderAdded, normalizeDependencies(orderDelivered(), em));
    }

    /**
     * Find order by id not found.
     */
    @Test
    public void findOrderByIdNotFound() {
        final Order order = orderRepository.findById(999L);
        assertThat(order, is(nullValue()));
    }

    /**
     * Update order.
     */
    @Test
    public void updateOrder() {
        final Long orderAddedId = dbCommandExecutor.executeCommand(() -> {
            return orderRepository.add(normalizeDependencies(orderReserved(), em)).getId();
        });
        assertThat(orderAddedId, is(notNullValue()));

        final Order orderAdded = orderRepository.findById(orderAddedId);
        assertThat(orderAdded.getHistoryEntries().size(), is(equalTo(1)));
        assertThat(orderAdded.getCurrentStatus(), is(equalTo(OrderStatus.RESERVED)));

        orderAdded.addHistoryEntry(OrderStatus.DELIVERED);
        dbCommandExecutor.executeCommand(() -> {
            orderRepository.update(orderAdded);
            return null;
        });

        final Order orderAfterUpdate = orderRepository.findById(orderAddedId);
        assertThat(orderAfterUpdate.getHistoryEntries().size(), is(equalTo(2)));
        assertThat(orderAfterUpdate.getCurrentStatus(), is(equalTo(OrderStatus.DELIVERED)));
    }

    /**
     * Exists by id.
     */
    @Test
    public void existsById() {
        final Long orderAddedId = dbCommandExecutor.executeCommand(() -> {
            return orderRepository.add(normalizeDependencies(orderReserved(), em)).getId();
        });
        assertThat(orderAddedId, is(notNullValue()));

        assertThat(orderRepository.existsById(orderAddedId), is(equalTo(true)));
        assertThat(orderRepository.existsById(999l), is(equalTo(false)));
    }

    private void assertActualOrderWithExpectedOrder(final Order actualOrder, final Order expectedOrder) {
        assertThat(expectedOrder.getCreatedAt(), is(notNullValue()));
        assertThat(actualOrder.getCustomer(), is(equalTo(expectedOrder.getCustomer())));
        assertThat(actualOrder.getItems().size(), is(equalTo(expectedOrder.getItems().size())));
        for (final OrderItem actualItem : actualOrder.getItems()) {
            final OrderItem expectedItem = findItemByBook(expectedOrder, actualItem.getBook());
            assertThat(actualItem.getBook().getTitle(), is(equalTo(expectedItem.getBook().getTitle())));
            assertThat(actualItem.getPrice(), is(equalTo(expectedItem.getPrice())));
            assertThat(actualItem.getQuantity(), is(equalTo(expectedItem.getQuantity())));
        }
        assertThat(actualOrder.getTotal(), is(equalTo(expectedOrder.getTotal())));
        assertThat(actualOrder.getCurrentStatus(), is(equalTo(expectedOrder.getCurrentStatus())));
        assertThat(actualOrder.getHistoryEntries().size(), is(equalTo(expectedOrder.getHistoryEntries().size())));
        for (int i = 0; i < actualOrder.getHistoryEntries().size(); i++) {
            assertThat(actualOrder.getHistoryEntries(), is(equalTo(expectedOrder.getHistoryEntries())));
        }
    }


    /**
     * Find item by book order item. Get the book item from an order.
     *
     * @param order the order
     * @param book  the book
     * @return the order item
     */
    private OrderItem findItemByBook(final Order order, final Book book) {
        for (final OrderItem item : order.getItems()) {
            if (item.getBook().getTitle().equals(book.getTitle())) {
                return item;
            }
        }
        return null;
    }

    /**
     * Load books.
     */
    private void loadBooks() {
        dbCommandExecutor.executeCommand(() -> {
            //create dummy data so we can create books
            allUsers().forEach(em::persist);
            allCategories().forEach(em::persist);
            allAuthors().forEach(em::persist);
            allBooks().forEach(book -> em.persist(normalizeDependencies(book, em)));
            return null;
        });
    }

}