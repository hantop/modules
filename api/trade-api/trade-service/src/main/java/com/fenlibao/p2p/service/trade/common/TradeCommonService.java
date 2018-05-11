package com.fenlibao.p2p.service.trade.common;

import com.fenlibao.p2p.model.trade.entity.CapitalFlow;

import java.util.List;

public interface TradeCommonService {

    /**
     * 发站内信
     * @param userId
     * @param title
     * @param content
     * @throws Exception
     */
    void sendLetter(int userId, String title,String content) throws Exception;

    /**
     * 发短信
     * @param mobile
     * @param content
     * @throws Exception
     */
    void sendMsg(String mobile,String content,int type) throws Exception;

    /**
     * 插入交易流水
     * @param list
     * @throws Exception
     */
    void addTransactionFlows(List<CapitalFlow> list) throws Exception;

}
