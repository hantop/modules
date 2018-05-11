package com.fenlibao.p2p.service.user;

import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.form.BindIdCardForm;

import java.util.Map;

/**
 * 国信实名认证服务类
 * @author Mingway.Xu
 * @date 2016/11/7 14:07
 */
public interface GuoXinIdCardAuthService {


    /**
     * 国信实名认证
     * @param userInfo
     * @param form
     * @return
     */
    Map<String,Object> authUserRealNameAndIdcard(UserInfo userInfo, BindIdCardForm form) throws Throwable;

}
