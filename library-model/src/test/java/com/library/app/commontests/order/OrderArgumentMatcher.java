package com.library.app.commontests.order;

import com.library.app.order.model.Order;
import org.mockito.ArgumentMatcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;

/**
 * The type Order argument matcher.
 */
public class OrderArgumentMatcher extends ArgumentMatcher<Order> {

    private final Order expected;

    /**
     * Order eq order. This is an ovveride method for Mockito equals.
     *
     * @param expected the expected
     * @return the order
     */
    public static Order orderEq(final Order expected) {
        return argThat(new OrderArgumentMatcher(expected));
    }

    /**
     * Instantiates a new Order argument matcher.
     *
     * @param expected the expected
     */
    public OrderArgumentMatcher(final Order expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(final Object actualObj) {
        final Order actual = (Order) actualObj;

        assertThat(actual.getId(), is(equalTo(expected.getId())));
        assertThat(actual.getCustomer(), is(equalTo(expected.getCustomer())));

        assertThat(actual.getItems(), is(equalTo(expected.getItems())));

        assertThat(actual.getTotal(), is(equalTo(expected.getTotal())));
        assertThat(actual.getHistoryEntries(), is(equalTo(expected.getHistoryEntries())));
        assertThat(actual.getCurrentStatus(), is(equalTo(expected.getCurrentStatus())));

        return true;
    }

}