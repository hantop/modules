package com.fenlibao.p2p.model.trade.enums.order;

/**
 * Created by zcai on 2016/10/27.
 */
public enum T6501_F03 {

    DTJ("待提交"),
    YTJ("已提交"),
    DQR("待确认"),
    CG("成功"),
    SB("失败"),
    MJL("没记录"),
    REFUND("退款"),
    ;

    private String chineseName;

    T6501_F03(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return chineseName;
    }
}
