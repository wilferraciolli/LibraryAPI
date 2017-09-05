package com.library.app.author.model.filter;

import com.library.app.common.model.filter.GenericFilter;

/**
 * Class to manage the filter requested on the pagination data.
 */
public class AuthorFilter extends GenericFilter {

    private String name;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AuthorFilter [name=" + name + ", toString()=" + super.toString() + "]";
    }
}

