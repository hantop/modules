package com.fenlibao.p2p.service.xinwang.account;

import com.fenlibao.p2p.model.xinwang.param.account.ModifyMobileRequestParams;
import com.fenlibao.p2p.service.xinwang.XWNotifyService;

import java.util.Map;

/**
 * Created by Administrator on 2017/5/15.
 */
public interface XWModifyMobileService extends XWNotifyService {

    /**
     * 预留手机号更新
     * @param params
     * @return
     */
    Map<String,Object> getModifyMobileRequestData(ModifyMobileRequestParams params) throws Exception;
}
