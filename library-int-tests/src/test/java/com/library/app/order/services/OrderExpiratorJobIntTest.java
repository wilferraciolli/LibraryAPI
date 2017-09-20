package com.library.app.order.services;

import com.library.app.common.model.PaginatedData;
import com.library.app.common.utils.DateUtils;
import com.library.app.commontests.utils.ArquillianTestUtils;
import com.library.app.commontests.utils.TestRepositoryEJB;
import com.library.app.order.model.Order;
import com.library.app.order.model.Order.OrderStatus;
import com.library.app.order.model.filter.OrderFilter;
import com.library.app.order.services.impl.OrderExpiratorJob;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.library.app.commontests.author.AuthorForTestsRepository.allAuthors;
import static com.library.app.commontests.book.BookForTestsRepository.allBooks;
import static com.library.app.commontests.book.BookForTestsRepository.normalizeDependencies;
import static com.library.app.commontests.category.CategoryForTestsRepository.allCategories;
import static com.library.app.commontests.order.OrderForTestsRepository.normalizeDependencies;
import static com.library.app.commontests.order.OrderForTestsRepository.orderReserved;
import static com.library.app.commontests.user.UserForTestsRepository.allUsers;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * The type Order expirator job int test. This integration test will trigger the batchlet to braking its schedule of every hour.
 */
@RunWith(Arquillian.class)
public class OrderExpiratorJobIntTest {

    @Inject
    private OrderExpiratorJob orderExpiratorJob;

    @Inject
    private OrderServices orderServices;

    @Inject
    private TestRepositoryEJB testRepositoryEJB;

    @PersistenceContext
    private EntityManager em;

    /**
     * Create deployment web archive.
     *
     * @return the web archive
     */
    @Deployment
    public static WebArchive createDeployment() {
        return ArquillianTestUtils.createDeploymentArchive();
    }

    /**
     * Prepare order for test.
     */
    @Test
    @InSequence(1)
    public void prepareOrderForTest() {
        testRepositoryEJB.deleteAll();

        //populate data to be able to create orders
        allCategories().forEach(testRepositoryEJB::add);
        allAuthors().forEach(testRepositoryEJB::add);
        allBooks().forEach((book) -> testRepositoryEJB.add(normalizeDependencies(book, em)));
        allUsers().forEach(testRepositoryEJB::add);

        //create orders
        final Order orderReservedToBeExpired = orderReserved();
        orderReservedToBeExpired.setCreatedAt(DateUtils.currentDatePlusDays(-8));

        final Order orderReserved = orderReserved();

        testRepositoryEJB.add(normalizeDependencies(orderReservedToBeExpired, em));
        testRepositoryEJB.add(normalizeDependencies(orderReserved, em));
    }

    /**
     * Run job.
     */
    @Test
    @InSequence(2)
    public void runJob() {
        //check that there are two orders in reserved status
        assertNumberOfOrdersWithStatus(2, OrderStatus.RESERVED);
        assertNumberOfOrdersWithStatus(0, OrderStatus.RESERVATION_EXPIRED);

        //call real method
        orderExpiratorJob.run();

        //cgech that one order was set to expired
        assertNumberOfOrdersWithStatus(1, OrderStatus.RESERVED);
        assertNumberOfOrdersWithStatus(1, OrderStatus.RESERVATION_EXPIRED);
    }

    private void assertNumberOfOrdersWithStatus(final int expectedTotalRecords, final OrderStatus status) {
        final OrderFilter orderFilter = new OrderFilter();
        orderFilter.setStatus(status);

        final PaginatedData<Order> orders = orderServices.findByFilter(orderFilter);

        assertThat(orders.getNumberOfRows(), is(equalTo(expectedTotalRecords)));
    }

}