package com.library.app.common.model.filter;

/**
 * Class responsible for receiving all of the pagination attributes such as page numbers, sorting...
 * Created by wilferaciolli on 21/02/2017.
 */
public class PaginationData {

    //Define pagination filters and values
    private final int firstResult;
    private final int maxResults;
    private final String orderField;
    private final OrderMode orderMode;

    /**
     * Enum for ordering.
     */
    public enum OrderMode {
        /**
         * Ascending order mode.
         */
        ASCENDING, /**
         * Descending order mode.
         */
        DESCENDING
    }

    /**
     * Constructor to take all the values.
     *
     * @param firstResult The first result. If the pagination brought back 10 results of the first call, then its value should be 11 for the next call
     * @param maxResults  The max result. The max number of rows to bring back
     * @param orderField  The order Field. The order colun name
     * @param orderMode   The order mode enum.
     */
    public PaginationData(final int firstResult, final int maxResults, final String orderField,
                          final OrderMode orderMode) {
        this.firstResult = firstResult;
        this.maxResults = maxResults;
        this.orderField = orderField;
        this.orderMode = orderMode;
    }

    /**
     * Gets first result.
     *
     * @return the first result
     */
    public int getFirstResult() {
        return firstResult;
    }

    /**
     * Gets max results.
     *
     * @return the max results
     */
    public int getMaxResults() {
        return maxResults;
    }

    /**
     * Gets order field.
     *
     * @return the order field
     */
    public String getOrderField() {
        return orderField;
    }

    /**
     * Gets order mode.
     *
     * @return the order mode
     */
    public OrderMode getOrderMode() {
        return orderMode;
    }

    /**
     * Is ascending boolean.
     *
     * @return the boolean
     */
    public boolean isAscending() {
        return OrderMode.ASCENDING.equals(orderMode);
    }

    @Override
    public String toString() {
        return "PaginationData [firstResult=" + firstResult + ", maxResults=" + maxResults + ", orderField="
                + orderField + ", orderMode=" + orderMode + "]";
    }
}
