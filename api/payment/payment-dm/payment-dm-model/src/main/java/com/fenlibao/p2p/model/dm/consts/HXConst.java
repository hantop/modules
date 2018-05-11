package com.fenlibao.p2p.model.dm.consts;

/**
 * 华兴常量
 */
public interface HXConst {

    /**
     * 订单处理状态：订单完成
     */
    String ORDERSTATUS_COMPLETED = "ORDER_COMPLETED";
    /**
     * 订单处理状态：订单预授权中（非实时到账，下一工作日到账）
     */
    String ORDERSTATUS_PRE_AUTHING = "ORDER_PRE_AUTHING";

    /**
     * 交易状态：成功
     */
    String RETURN_STATUS_S = "S";
    /**
     * 交易状态：失败
     */
    String RETURN_STATUS_F = "F";
    /**
     * 交易状态：处理中（客户仍停留在页面操作，25分钟后仍是此状态可置交易为失败）
     */
    String RETURN_STATUS_R = "R";
    /**
     * 交易状态：未知（已提交后台，需再次发查询接口。）
     */
    String RETURN_STATUS_N = "N";

    /**
     * 同步响应状态：成功
     */
    String ERRORCODE_SUCCESS = "0";
    /**
     * 同步响应状态：无此交易流水
     */
    String ERRORCODE_FLOWNO_NOT_EXIST = "OGWERR999997";
    /**
     * 同步响应状态：接口请求过于频繁
     */
    String ERRORCODE_REQUEST_FREQUENTLY = "OGW100200009";

    /**
     * 用于记录页面超时的订单
     */
    String ORDER_STATE_TIMEOUT = "TIMEOUT";
}
