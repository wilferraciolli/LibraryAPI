package com.library.app.common.model.filter;

/**
 * Class to manage pagination data.
 * Created by wilferaciolli on 21/02/2017.
 */
public class GenericFilter {

    private PaginationData paginationData;

    /**
     * Default constructor.
     */
    public GenericFilter() {
    }

    /**
     * Constructor to take parameters.
     *
     * @param paginationData The pagination data.
     */
    public GenericFilter(final PaginationData paginationData) {
        this.paginationData = paginationData;
    }

    /**
     * Sets pagination data.
     *
     * @param paginationData the pagination data
     */
    public void setPaginationData(final PaginationData paginationData) {
        this.paginationData = paginationData;
    }

    /**
     * Gets pagination data.
     *
     * @return the pagination data
     */
    public PaginationData getPaginationData() {
        return paginationData;
    }

    /**
     * Has pagination data boolean.
     *
     * @return the boolean
     */
    public boolean hasPaginationData() {
        return getPaginationData() != null;
    }

    /**
     * Has order field boolean.
     *
     * @return the boolean
     */
    public boolean hasOrderField() {
        return hasPaginationData() && getPaginationData().getOrderField() != null;
    }

    @Override
    public String toString() {
        return "GenericFilter [paginationData=" + paginationData + "]";
    }
}
