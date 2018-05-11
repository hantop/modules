package com.fenlibao.p2p.service.xinwang.entrust;

import com.fenlibao.p2p.model.xinwang.entity.trust.AuthorizationEntrustRecord;
import com.fenlibao.p2p.model.xinwang.enums.account.XWUserType;
import com.fenlibao.p2p.model.xinwang.enums.entrust.AuthorizeStatus;

/**
 * @date 2017/7/13 14:43
 */
public interface XWEntrustService {
    /**
     *  委托支付授权
     * @param loadId 标的号
     * @return
     */
    AuthorizeStatus authEntrustPay(Integer loadId) throws Exception;

    /**
     * 委托支付授权记录查询
     * @param requestNo
     * @return
     */
    AuthorizationEntrustRecord getAuthEntrustRecord(String requestNo);
}
