package com.fenlibao.p2p.service.xinwang.enterprise;

import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.service.xinwang.XWNotifyService;

import java.util.Map;

/**
 * @date 2017/6/29 11:43
 */
public interface XWEnpBindcardService extends XWNotifyService{
    /**
     * 获取新网企业用户绑卡参数
     * @param enpId
     * @param userRole
     * @param bankcode
     * @param bankcardNo
     * @param uri
     * @return
     */
    Map<String, Object> getBindcardInfo(int enpId, UserRole userRole, String bankcode, String bankcardNo, String uri);
}
