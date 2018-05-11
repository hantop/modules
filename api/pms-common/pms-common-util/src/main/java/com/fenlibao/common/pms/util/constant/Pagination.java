package com.fenlibao.common.pms.util.constant;

import java.io.Serializable;

/**
 * Created by Lullaby on 2015/7/20.
 */
public class Pagination implements Serializable {

    public static final String DEFAULT_PAGE = "1";

    public static final String DEFAULT_LIMIT = "15";

    private int page;

    private int limit;

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

}
