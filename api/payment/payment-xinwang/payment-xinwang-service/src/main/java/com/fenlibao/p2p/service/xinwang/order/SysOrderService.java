package com.fenlibao.p2p.service.xinwang.order;

/**
 * t6501
 * @date 2017/6/1 11:02
 */
public interface SysOrderService {

    void submit(int orderId);
    void success(Integer orderId);
    void fail(Integer orderId);
    void insertOrderExceptionLog(Integer orderId, String log);
}
