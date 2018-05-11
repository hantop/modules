package com.fenlibao.p2p.service.xinwang.bid;

import com.fenlibao.p2p.model.xinwang.bo.XWTenderBO;

import java.util.List;

/**
 * @date 2017/5/11 15:59
 */
public interface XWBidService {
    /**
     * 新网存管投标
     * @param orderId
     * @param jxqId
     * @param redpacketIdsArr
     * @throws Exception
     */
    void doBid(Integer orderId, Integer jxqId, String[] redpacketIdsArr) throws Exception;

    /**
     * 投资单个的计划标
     * 流程：解冻一部分资金进行投资，解冻资金并不需要写入用户资金账户，解冻后马上投标
     * @param orderId
     * @param tender
     * @throws Exception
     */
    void doBidForPlan(Integer orderId, XWTenderBO tender) throws Exception;

    /**
     * 获取需要投资的列表
     * @return
     */
    List<XWTenderBO> getSendTender();
}
