package com.library.app.common.model;

import java.util.List;

/**
 * class to manage the rows and number of rows returned from the database.
 * Created by wilferaciolli on 21/02/2017.
 *
 * @param <T> the type parameter
 */
public class PaginatedData<T> {

    private final int numberOfRows;
    private final List<T> rows;

    /**
     * Constructor with parameters.
     *
     * @param numberOfRows The number of rows.
     * @param rows         The rows objects.
     */
    public PaginatedData(final int numberOfRows, final List<T> rows) {
        this.numberOfRows = numberOfRows;
        this.rows = rows;
    }

    /**
     * Gets number of rows.
     *
     * @return the number of rows
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * Gets rows.
     *
     * @return the rows
     */
    public List<T> getRows() {
        return rows;
    }

    /**
     * Gets row.
     *
     * @param index the index
     * @return the row
     */
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
