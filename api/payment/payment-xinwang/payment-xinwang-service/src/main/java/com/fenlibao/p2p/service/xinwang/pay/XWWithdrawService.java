package com.fenlibao.p2p.service.xinwang.pay;

import com.fenlibao.p2p.model.xinwang.param.pay.WithdrawParam;
import com.fenlibao.p2p.service.xinwang.XWNotifyService;

import java.util.Map;

/**
 * @author Mingway.Xu
 * @date 2017/5/11 15:21
 */
public interface XWWithdrawService extends XWNotifyService {
    /**
     * 提现
     * @param params
     * @return
     * @throws Exception
     */
    Map<String,Object> withdrawApply(WithdrawParam params) throws Exception;

}
