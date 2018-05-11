package com.fenlibao.p2p.service.user;

import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.form.BindIdCardForm;
import com.fenlibao.p2p.model.global.HttpResponse;

import java.util.Map;

/**
 * 实名认证服务类
 * @author Mingway.Xu
 * @date 2016/11/7 14:07
 */
public interface IdCardAuthService {
    /**
     * 实名验证
     * @param userInfo
     * @param bindIdCardForm
     * @param response
     * @return
     */
    HttpResponse realNameAuth(UserInfo userInfo, BindIdCardForm bindIdCardForm, HttpResponse response) throws Throwable;

    /**
     * 国信-调用第三方api
     * @param userInfo
     * @param form
     * @return
     */
    Map<String,Object> guoxinAuthUserRealNameAndIdcard(UserInfo userInfo, BindIdCardForm form) throws Throwable;

    /**
     * 国信实名认证
     * @param userInfo
     * @param bindIdCardForm
     * @param response
     * @return
     */
    HttpResponse guoxinRealNameAuth(UserInfo userInfo, BindIdCardForm bindIdCardForm, HttpResponse response) throws Throwable;

    /**
     * 有盾-调用第三方api
     * @param userInfo
     * @param form
     * @return
     */
    Map<String,Object> edRealNameAuthentication(UserInfo userInfo,BindIdCardForm form) throws Exception;

    /**
     * 有盾实名认证
     * @param userInfo
     * @param bindIdCardForm
     * @param response
     * @return
     * @throws Throwable
     */
    HttpResponse udRealNameAuth(UserInfo userInfo, BindIdCardForm bindIdCardForm, HttpResponse response) throws Throwable;

    /**
     * 保存实名认证信息
     * @param bindIdCardForm
     * @return
     * @throws Throwable
     */
    String saveRealNameAuthentication(BindIdCardForm bindIdCardForm) throws Throwable;

}
