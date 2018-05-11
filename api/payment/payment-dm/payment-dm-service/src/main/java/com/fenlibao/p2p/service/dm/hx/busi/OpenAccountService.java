package com.fenlibao.p2p.service.dm.hx.busi;

import com.fenlibao.p2p.service.dm.hx.HXOrderProcess;

/**
 * Created by zcai on 2016/10/12.
 */
public interface OpenAccountService extends HXOrderProcess {

    /**
     * 开户
     * @param userId
     * @param clientType
     * @param uri
     * @return
     * @throws Exception
     */
    String openAccount(int userId, int clientType, String uri) throws Exception;

}
