package com.fenlibao.p2p.model.trade.enums.order;

/**
 * Created by zcai on 2016/10/27.
 */
public enum T6501_F07 {

    XT("系统"),
    YH("用户"),
    HT("后台"),
    HX("华兴存管");

    private String chineseName;

    T6501_F07(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return chineseName;
    }
}
