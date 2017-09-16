package com.library.app.order.model;

import com.library.app.book.model.Book;
import com.library.app.user.model.Customer;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Order.
 */
@Entity
@Table(name = "lib_order")
public class Order implements Serializable {
    private static final long serialVersionUID = -8589662328013809186L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @NotNull
    private Customer customer;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "lib_order_item", joinColumns = @JoinColumn(name = "order_id"))
    @NotNull
    @Size(min = 1)
    @Valid
    private Set<OrderItem> items;

    @NotNull
    private Double total;

    /**
     * The enum Order status.
     */
    public enum OrderStatus {
        /**
         * Reserved order status.
         */
        RESERVED,
        /**
         * Reservation expired order status.
         */
        RESERVATION_EXPIRED,
        /**
         * Delivered order status.
         */
        DELIVERED,
        /**
         * Cancelled order status.
         */
        CANCELLED
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "lib_order_history", joinColumns = @JoinColumn(name = "order_id"))
    @NotNull
    @Size(min = 1)
    @Valid
    private Set<OrderHistoryEntry> historyEntries;

    @Column(name = "current_status")
    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderStatus currentStatus;

    /**
     * Instantiates a new Order.
     */
    public Order() {
        this.createdAt = new Date();
    }

    /**
     * Add item boolean.
     *
     * @param book     the book
     * @param quantity the quantity
     * @return the boolean
     */
    public boolean addItem(final Book book, final Integer quantity) {
        final OrderItem item = new OrderItem(book, quantity);
        return getItems().add(item);
    }


    /**
     * Calculate total.
     */
    public void calculateTotal() {
        this.total = 0D;
        for (final OrderItem item : getItems()) {
            item.calculatePrice();
            this.total += item.getPrice();
        }
    }

    /**
     * Add history entry.
     *
     * @param status the status
     */
    public void addHistoryEntry(final OrderStatus status) {
        if (this.currentStatus != null) {
            if (this.currentStatus != OrderStatus.RESERVED) {
                throw new IllegalArgumentException("An order in the state " + currentStatus
                        + " cannot have its state changed");
            }
            if (this.currentStatus == status) {
                throw new IllegalArgumentException("The new state must be different from the current one");
            }
        }

        getHistoryEntries().add(new OrderHistoryEntry(status));
        this.currentStatus = status;
    }

    /**
     * Sets initial status.
     */
    public void setInitialStatus() {
        getHistoryEntries().clear();
        setCurrentStatus(null);
        addHistoryEntry(OrderStatus.RESERVED);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Gets createdAt at.
     *
     * @return the createdAt at
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets createdAt at.
     *
     * @param createdAt the createdAt at
     */
    public void setCreatedAt(final Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets customer.
     *
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets customer.
     *
     * @param customer the customer
     */
    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    /**
     * Gets items.
     *
     * @return the items
     */
    public Set<OrderItem> getItems() {
        if (items == null) {
            items = new HashSet<>();
        }
        return items;
    }

    /**
     * Sets items.
     *
     * @param items the items
     */
    public void setItems(final Set<OrderItem> items) {
        this.items = items;
    }

    /**
     * Gets total.
     *
     * @return the total
     */
    public Double getTotal() {
        return total;
    }

    /**
     * Sets total.
     *
     * @param total the total
     */
    public void setTotal(final Double total) {
        this.total = total;
    }

    /**
     * Gets history entries.
     *
     * @return the history entries
     */
    public Set<OrderHistoryEntry> getHistoryEntries() {
        if (historyEntries == null) {
            historyEntries = new HashSet<>();
        }
        return historyEntries;
    }

    /**
     * Sets history entries.
     *
     * @param historyEntries the history entries
     */
    public void setHistoryEntries(final Set<OrderHistoryEntry> historyEntries) {
        this.historyEntries = historyEntries;
    }

    /**
     * Gets current status.
     *
     * @return the current status
     */
    public OrderStatus getCurrentStatus() {
        return currentStatus;
    }

    /**
     * Sets current status.
     *
     * @param currentStatus the current status
     */
    public void setCurrentStatus(final OrderStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", createdAt=" + createdAt + ", customer=" + customer + ", total=" + total
                + ", currentStatus=" + currentStatus + "]";
    }

}
