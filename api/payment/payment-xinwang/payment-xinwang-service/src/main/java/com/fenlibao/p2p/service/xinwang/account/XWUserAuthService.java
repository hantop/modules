package com.fenlibao.p2p.service.xinwang.account;

import com.fenlibao.p2p.service.xinwang.XWNotifyService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author zeronx on 2017/12/21 17:04.
 * @version 1.0
 */
public interface XWUserAuthService extends XWNotifyService {

    /**
     * 取消用户授权
     * @param userId
     * @param role
     * @param authList  【用户授权列表】此处可传一个或多个值，传多个值用“,”英文半角逗号分隔【可为空】
     *                   为空时：投资人默认取消自动投资权限，借款人默认取消自动还款权限
     * @return
     */
    boolean cancelUserAuth(Integer userId, String role, String authList) throws Exception;

    /**
     * 用户授权
     * @param userId
     * @param role
     * @param authList 【用户授权列表】此处可传一个或多个值，传多个值用“,”英文半角逗号分隔【可为空】
     *                  为空时：投资人默认授权自动投资，借款人默认授权自动还款
     * @param failTime 授权期限 【可为空】
     * @param amount 授权金额 【可为空】
     * @param redirectUrl 页面回跳URL 长度 50内
     * @return
     */
    Map<String, Object> doUserAuth(Integer userId, String role, String authList, Date failTime, BigDecimal amount, String redirectUrl) throws Exception;
}
