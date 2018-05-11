package com.fenlibao.p2p.service.xinwang.pay;

import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/6.
 */
public interface XWWithdrawTransactionService {
    void pretreatment(XinwangAccount xinwangAccount, String requestNo, BigDecimal totalAmount, BigDecimal withdrawAmount, BigDecimal commossion, Date requestTime, Map<String,Object> reqData) throws Exception;
    void withdrawAccept(XWRequest request) throws Exception;
    void withdrawSuccess(XWRequest request) throws Exception;
    void withdrawAcceptFail(XWRequest request) throws Exception;
    void withdrawFail(XWRequest request) throws Exception;
    void backrollRecharge(Map<String, Object> respMap)throws Exception;
}
