package com.fenlibao.p2p.util.paginator.domain;

import java.util.List;

/**
 * 分页对象
 * @author laubrence
 * @param <T> 传递的泛型对象
 */
public class Pager<T> {

    private int page;//当前页数

    private int limit;//每页记录数

    private int totalCount;//总记录数

    private int totalPages;//总页数

    private List<T> items;//每页数据

    public Pager(){}

    public Pager(List<T> itemList) {
        this.setPage(((PageList)itemList).getPaginator().getPage());
        this.setLimit(((PageList)itemList).getPaginator().getLimit());
        this.setTotalCount(((PageList)itemList).getPaginator().getTotalCount());
        this.setTotalPages(((PageList)itemList).getPaginator().getTotalPages());
        this.setItems(itemList);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
