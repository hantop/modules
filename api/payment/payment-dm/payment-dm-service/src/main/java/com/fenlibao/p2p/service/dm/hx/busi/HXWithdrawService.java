package com.fenlibao.p2p.service.dm.hx.busi;

import com.fenlibao.p2p.service.dm.hx.HXOrderProcess;

/**
 * Created by zcai on 2016/11/29.
 */
public interface HXWithdrawService extends HXOrderProcess {

    /**
     * 提现
     * @param orderId
     * @param userId
     * @param clientType
     * @param uri
     * @return 封装好的请求报文
     */
    String withdraw(int orderId, int userId, int clientType, String uri)  throws Exception;

}
