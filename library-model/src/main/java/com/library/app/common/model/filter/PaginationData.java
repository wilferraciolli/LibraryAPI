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
        ASCENDING, DESCENDING
    }

    /**
     * Constructor to take all the values.
     * @param firstResult The first result.
     * @param maxResults The max result.
     * @param orderField The order Field.
     * @param orderMode The order mode enum.
     */
    public PaginationData(final int firstResult, final int maxResults, final String orderField,
                          final OrderMode orderMode) {
        this.firstResult = firstResult;
        this.maxResults = maxResults;
        this.orderField = orderField;
        this.orderMode = orderMode;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public String getOrderField() {
        return orderField;
    }

    public OrderMode getOrderMode() {
        return orderMode;
    }

    public boolean isAscending() {
        return OrderMode.ASCENDING.equals(orderMode);
    }

    @Override
    public String toString() {
        return "PaginationData [firstResult=" + firstResult + ", maxResults=" + maxResults + ", orderField="
                + orderField + ", orderMode=" + orderMode + "]";
    }
}
