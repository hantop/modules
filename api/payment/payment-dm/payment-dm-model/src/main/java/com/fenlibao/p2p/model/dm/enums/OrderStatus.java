package com.fenlibao.p2p.model.dm.enums;

/**
 * Created by zcai on 2016/10/12.
 */
public enum OrderStatus {

    /**
     * 该状态不保存到数据库, 只用来判断订单的状态是否需要改变
     */
    N(0),
    /**
     * 待提交
     */
    DTJ(1),
    /**
     * 待确认
     */
    DQR(2),
    /**
     * 成功
     */
    CG(3),
    /**
     * 失败
     */
    SB(4),
    /**
     * 在华兴没记录
     */
    MJL(5),
    /**
     * 超时
     */
    CS(6),

    ;

    private int code; //订单状态码

    OrderStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
