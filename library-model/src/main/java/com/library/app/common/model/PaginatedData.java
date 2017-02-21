package com.library.app.common.model;

import java.util.List;

/**
 * class to manage the rows and number of rows returned from the database.
 * Created by wilferaciolli on 21/02/2017.
 */
public class PaginatedData<T> {

    private final int numberOfRows;
    private final List<T> rows;

    /**
     * Constructor with parameters.
     * @param numberOfRows The number of rows.
     * @param rows The rows objects.
     */
    public PaginatedData(final int numberOfRows, final List<T> rows) {
        this.numberOfRows = numberOfRows;
        this.rows = rows;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public List<T> getRows() {
        return rows;
    }

    public T getRow(final int index) {
        if (index >= rows.size()) {
            return null;
        }
        return rows.get(index);
    }

    @Override
    public String toString() {
        return "PaginatedData [numberOfRows=" + numberOfRows + ", rows=" + rows + "]";
    }


}
