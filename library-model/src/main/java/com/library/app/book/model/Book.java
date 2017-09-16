package com.library.app.book.model;

import com.library.app.author.model.Author;
import com.library.app.category.model.Category;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Book entity.
 */
@Entity
@Table(name = "lib_book")
public class Book implements Serializable {
    private static final long serialVersionUID = -98804459764760011L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 10, max = 150)
    private String title;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    //one book can have many authors, but it has to be unique
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "lib_book_author",
            joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"book_id", "author_id"}))
    @JoinColumn(name = "author_id")
    @OrderBy("name")
    @NotNull
    @Size(min = 1)
    private List<Author> authors;

    @Lob
    @NotNull
    @Size(min = 10)
    private String description;

    @NotNull
    private Double price;

    /**
     * Instantiates a new Book.
     */
    public Book() {
    }

    /**
     * Instantiates a new Book.
     *
     * @param bookId the book id
     */
    public Book(final Long bookId) {
        this.id = bookId;
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
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Gets category.
     *
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets category.
     *
     * @param category the category
     */
    public void setCategory(final Category category) {
        this.category = category;
    }

    /**
     * Gets authors.
     *
     * @return the authors
     */
    public List<Author> getAuthors() {
        if (authors == null) {
            authors = new ArrayList<>();
        }
        return authors;
    }

    /**
     * Sets authors.
     *
     * @param authors the authors
     */
    public void setAuthors(final List<Author> authors) {
        this.authors = authors;
    }

    /**
     * Add author boolean. Adds an author if it doesnt exists yet.
     *
     * @param author the author
     * @return the boolean
     */
    public boolean addAuthor(final Author author) {
        if (!getAuthors().contains(author)) {
            getAuthors().add(author);
            return true;
        }
        return false;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(final String description) {
        this.description = description;
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
        final Book other = (Book) obj;
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
        return "Book [id=" + id + ", title=" + title + ", price=" + price + "]";
    }

}