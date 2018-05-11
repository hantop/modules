package com.fenlibao.p2p.service.dm.hx.busi;

import com.fenlibao.p2p.service.dm.hx.HXOrderProcess;

import java.math.BigDecimal;

/**
 * Created by zcai on 2016/11/3.
 */
public interface HXRechargeService extends HXOrderProcess {

    /**
     * 充值
     * @param orderId
     * @param userId
     * @param clientType
     * @param uri
     * @return 封装好的请求报文
     */
    String recharge(int orderId, int userId, int clientType, String uri)  throws Exception;


}
