package com.fenlibao.p2p.service.xinwang.account;

import com.fenlibao.p2p.model.xinwang.param.account.PersonalRegisterRequestParams;
import com.fenlibao.p2p.service.xinwang.XWNotifyService;

import java.util.Map;

/**
 * Created by Administrator on 2017/5/9.
 */
public interface XWPersonalRegisterService extends XWNotifyService {
    /**
     *
     * @param
     * @return
     * @throws Exception
     */
    Map<String,Object> getPersonalRegisterRequestData(PersonalRegisterRequestParams params) throws Exception;

}
