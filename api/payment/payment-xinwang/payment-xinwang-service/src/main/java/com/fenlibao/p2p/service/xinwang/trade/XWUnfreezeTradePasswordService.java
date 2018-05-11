package com.fenlibao.p2p.service.xinwang.trade;

/**
 * Created by Administrator on 2017/5/18.
 */
public interface XWUnfreezeTradePasswordService {
    void unfreezeTradePassword(Integer userId, String userRole) throws Exception;
}
