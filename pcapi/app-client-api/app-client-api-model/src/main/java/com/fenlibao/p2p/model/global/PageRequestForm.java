package com.fenlibao.p2p.model.global;

import org.apache.commons.lang3.StringUtils;

public class PageRequestForm extends BaseRequestForm{

    private String page;

    private String limit;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String toString() {
        return "PageRequestForm [clientType="+super.getClientType()+",page=" + page + ",limit="+limit+"]";
    }

    public boolean validate() {
        if (StringUtils.isEmpty(page)) {
            page = String.valueOf(InterfaceConst.PAGINATOR_PAGE_DEFAULT_NUM);
        }
        if (StringUtils.isEmpty(limit)) {
            limit = String.valueOf(InterfaceConst.PAGINATOR_PAGE_DEFAULT_LIMIT);
        }
        return super.validate();
    }
}
