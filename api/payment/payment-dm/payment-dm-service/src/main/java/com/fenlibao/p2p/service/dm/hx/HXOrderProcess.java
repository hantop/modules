package com.fenlibao.p2p.service.dm.hx;

import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;

import java.util.Map;

/**
 * 华兴订单统一处理
 * Created by zcai on 2016/10/12.
 */
public interface HXOrderProcess {

    /**
     * 统一处理华兴异步回调
     * @param busiParams
     * @param thirdPartyFlowNum
     * @throws Exception
     */
    void process(RespBusinessParams busiParams, String thirdPartyFlowNum) throws Exception;

    /**
     * 提交订单
     * @param orderId
     * @throws Exception
     */
    void submit(int orderId, String flowNum, Map<String, Object> params) throws Exception;

    /**
     * 查询订单
     * @param order
     * @return
     * @throws Exception
     */
    RespBusinessParams queryOrder(HXOrder order) throws Exception;

}
