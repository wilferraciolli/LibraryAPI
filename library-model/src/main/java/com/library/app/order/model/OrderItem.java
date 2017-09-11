package com.library.app.order.model;

import com.library.app.book.model.Book;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * The type Order item.
 */
@Embeddable
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 3520003746949600860L;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @NotNull
    private Book book;

    @NotNull
    private Integer quantity;

    @NotNull
    private Double price;

    /**
     * Instantiates a new Order item.
     */
    public OrderItem() {
    }

    /**
     * Instantiates a new Order item.
     *
     * @param book     the book
     * @param quantity the quantity
     */
    public OrderItem(final Book book, final Integer quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    /**
     * Calculate price.
     */
    public void calculatePrice() {
        if (book != null && quantity != null) {
            price = book.getPrice() * quantity;
        }
    }

    /**
     * Gets book.
     *
     * @return the book
     */
    public Book getBook() {
        return book;
    }

    /**
     * Sets book.
     *
     * @param book the book
     */
    public void setBook(final Book book) {
        this.book = book;
    }

    /**
     * Gets quantity.
     *
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets quantity.
     *
     * @param quantity the quantity
     */
    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(final Double price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((book == null) ? 0 : book.hashCode());
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
        final OrderItem other = (OrderItem) obj;
        if (book == null) {
            if (other.book != null) {
                return false;
            }
        } else if (!book.equals(other.book)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OrderItem [book=" + book + ", quantity=" + quantity + ", price=" + price + "]";
    }

}