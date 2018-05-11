package com.fenlibao.p2p.dao.xinwang.enterprise;

import com.fenlibao.p2p.model.xinwang.checkfile.*;
import com.fenlibao.p2p.model.xinwang.entity.account.OrganizationBaseInfo;
import com.fenlibao.p2p.model.xinwang.param.account.EnterpriseRegisterRequestParams;

/**
 * Created by Administrator on 2017/6/16.
 */
public interface XWEnterpriseDao {
    Integer getCountEnterpriseUser(Integer userId);
    void insertEnterpriseInfoToT6161(EnterpriseRegisterRequestParams params);
    void insertEnterpriseInfoToT6164(EnterpriseRegisterRequestParams params);
    void updateEnterpriseInfoT6161(EnterpriseRegisterRequestParams params);
    void updateEnterprideInfoT6164(EnterpriseRegisterRequestParams params);

    Integer getCountEnterpriseUser6164(Integer userId);

    OrganizationBaseInfo getOrganizationBaseInfo(Integer orgId);
}
