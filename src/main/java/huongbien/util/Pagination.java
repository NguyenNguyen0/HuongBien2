package com.huongbien.utils;

import java.util.List;
import java.util.function.BiFunction;

public class Pagination<T> {
    private final boolean isRollBack;
    private final int itemsPerPage;

    private int totalPages;
    private int currentPageIndex;
    private int startIndex;
    private BiFunction<Integer, Integer, List<T>> getData;

    public Pagination(BiFunction<Integer, Integer, List<T>> getData, int itemsPerPage, int totalItems, boolean isRollBack) {
        this.isRollBack = isRollBack;
        this.itemsPerPage = itemsPerPage;
        this.totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
        this.currentPageIndex = 1;
        this.startIndex = 0;
        this.getData = getData;
    }

    public void setCurrentPage(BiFunction<Integer, Integer, List<T>> getData, int totalItems) {
        this.getData = getData;
        setTotalPage(totalItems);
        setCurrentPageIndex(1);
        startIndex = (currentPageIndex - 1) * itemsPerPage;
    }

    public List<T> getCurrentPage() {
        return getData.apply(startIndex, itemsPerPage);
    }

    public void goToFirstPage() {
        setCurrentPageIndex(1);
        startIndex = (currentPageIndex - 1) * itemsPerPage;
    }

    public void goToPreviousPage() {
        setCurrentPageIndex(currentPageIndex - 1);
        startIndex = (currentPageIndex - 1) * itemsPerPage;
    }

    public void goToNextPage() {
        setCurrentPageIndex(currentPageIndex + 1);
        startIndex = (currentPageIndex - 1) * itemsPerPage;
    }

    public void goToLastPage() {
        setCurrentPageIndex(getTotalPages());
        startIndex = (currentPageIndex - 1) * itemsPerPage;
    }

    public void setTotalPage(int dataSize) {
        this.totalPages = (int) Math.ceil((double) dataSize / this.itemsPerPage);
    }

    public void setCurrentPageIndex(int pageIndex) {
        if (pageIndex > totalPages && isRollBack) {
            pageIndex = 1;
        } else if (pageIndex <= 0 && isRollBack) {
            pageIndex = getTotalPages();
        } else if (pageIndex <= 0 || pageIndex > totalPages) {
            return;
        }
        this.currentPageIndex = pageIndex;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }
}
