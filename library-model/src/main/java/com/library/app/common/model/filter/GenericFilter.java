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

    public void setPaginationData(final PaginationData paginationData) {
        this.paginationData = paginationData;
    }

    public PaginationData getPaginationData() {
        return paginationData;
    }

    public boolean hasPaginationData() {
        return getPaginationData() != null;
    }

    public boolean hasOrderField() {
        return hasPaginationData() && getPaginationData().getOrderField() != null;
    }

    @Override
    public String toString() {
        return "GenericFilter [paginationData=" + paginationData + "]";
    }
}
