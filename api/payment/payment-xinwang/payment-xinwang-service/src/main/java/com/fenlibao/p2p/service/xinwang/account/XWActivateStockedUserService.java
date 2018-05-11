package com.fenlibao.p2p.service.xinwang.account;

import com.fenlibao.p2p.model.xinwang.param.account.ActivateStockedUserRequestParams;
import com.fenlibao.p2p.service.xinwang.XWNotifyService;

import java.util.Map;

/**
 * 迁移用户激活
 */
public interface XWActivateStockedUserService extends XWNotifyService {
    Map<String,Object> getActivateStockedUserRequestData(ActivateStockedUserRequestParams params) throws Exception;
}
