package com.library.app.book.model.filter;

import com.library.app.common.model.filter.GenericFilter;

/**
 * The type Book filter.
 */
public class BookFilter extends GenericFilter {

    private String title;
    private Long categoryId;

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
     * Gets category id.
     *
     * @return the category id
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * Sets category id.
     *
     * @param categoryId the category id
     */
    public void setCategoryId(final Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "BookFilter [title=" + title + ", categoryId=" + categoryId + ", toString()=" + super.toString() + "]";
    }
}