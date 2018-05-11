package com.fenlibao.p2p.service.dm.hx.busi;

import com.fenlibao.p2p.service.dm.hx.HXOrderProcess;

/**
 * Created by zcai on 2016/12/8.
 */
public interface HXTenderService extends HXOrderProcess {

    /**
     * 投标
     * @param orderId
     * @param userId
     * @param clientType
     * @param uri
     * @return 封装好的报文
     * @throws Exception
     */
    String tender(int orderId, int userId, int clientType, String uri, Integer jxqId, String... userRedpacketIds) throws Exception;

}
