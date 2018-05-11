package com.fenlibao.p2p.common.util.http.defines;

/**
 * Created by lenovo on 2015/9/4.
 */
public enum HttpFormat {
    JSON("json格式数据"),
    MAP("key-value格式数据");

    private String name;

    HttpFormat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
