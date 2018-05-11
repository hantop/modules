package com.fenlibao.p2p.service.xinwang.enterprise;

import com.fenlibao.p2p.model.xinwang.entity.account.OrganizationBaseInfo;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.service.xinwang.XWNotifyService;

import java.util.Map;

/**
 * 新网企业用户相关
 * @date 2017/6/19 11:07
 */
public interface XWEnterpriseService extends XWNotifyService {
    /**
     * 修改企业用户信息
     * @param enpId
     * @param uri
     * @return
     */
    Map<String, Object> getModifyInfo(Integer enpId, UserRole userRole, String uri) throws Exception;

    /**
     * T6161企业用户基础信息
     * @param orgId
     * @return
     */
    OrganizationBaseInfo getOrganizationBaseInfo(Integer orgId);
}
