package com.fenlibao.p2p.service.xinwang.entrust;

import com.fenlibao.p2p.model.xinwang.enums.entrust.AuthorizeStatus;

/**
 * @date 2017/8/10 17:53
 */
public interface EntrustTransactionService {
    void doSuccess(String requestNo, Integer loadId, AuthorizeStatus status);

    void doFail(String requestNo, Integer loadId, String errorMessage, AuthorizeStatus status)throws Exception;
}
