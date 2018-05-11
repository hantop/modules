package com.fenlibao.p2p.model.xinwang.enums.order;

/**
 * Created by zcai on 2016/10/27.
 */
public enum Source {

    XT("系统"),
    YH("用户"),
    HT("后台"),
    HX("华兴存管");

    private String chineseName;

    Source(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return chineseName;
    }
}
