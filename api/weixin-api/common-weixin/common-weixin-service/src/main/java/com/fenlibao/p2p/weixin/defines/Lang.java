package com.fenlibao.p2p.weixin.defines;

/**
 * 语言
 * Created by Administrator on 2015/9/8.
 */
public enum Lang {
    zh_CN("简体"), zh_TW("繁体"), en("英语");

    private final String name;
    Lang(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
