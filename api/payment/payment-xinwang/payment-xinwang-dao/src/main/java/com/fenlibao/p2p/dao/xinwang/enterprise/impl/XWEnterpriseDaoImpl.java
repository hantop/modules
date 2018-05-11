package com.fenlibao.p2p.dao.xinwang.enterprise.impl;

import com.fenlibao.p2p.dao.xinwang.enterprise.XWEnterpriseDao;
import com.fenlibao.p2p.model.xinwang.checkfile.*;
import com.fenlibao.p2p.model.xinwang.entity.account.OrganizationBaseInfo;
import com.fenlibao.p2p.model.xinwang.param.account.EnterpriseRegisterRequestParams;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/6/16.
 */
@Repository
public class XWEnterpriseDaoImpl implements XWEnterpriseDao {
    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "XWEnterpriseMapper.";

    @Override
    public Integer getCountEnterpriseUser(Integer userId) {
        Integer result = sqlSession.selectOne(MAPPER + "getCountEnterpriseUser", userId);
        return result;
    }

    @Override
    public void insertEnterpriseInfoToT6161(EnterpriseRegisterRequestParams params) {
        sqlSession.insert(MAPPER + "insertEnterpriseInfoToT6161", params);
    }

    @Override
    public void insertEnterpriseInfoToT6164(EnterpriseRegisterRequestParams params) {
        sqlSession.insert(MAPPER + "insertEnterpriseInfoToT6164", params);
    }

    @Override
    public void updateEnterpriseInfoT6161(EnterpriseRegisterRequestParams params) {
        sqlSession.update(MAPPER + "updateEnterpriseInfoT6161", params);
    }

    @Override
    public void updateEnterprideInfoT6164(EnterpriseRegisterRequestParams params) {
        sqlSession.update(MAPPER + "updateEnterprideInfoT6164", params);
    }

    @Override
    public Integer getCountEnterpriseUser6164(Integer userId) {
        Integer result = sqlSession.selectOne(MAPPER + "getCountEnterpriseUser6164", userId);
        return result;
    }

    @Override
    public OrganizationBaseInfo getOrganizationBaseInfo(Integer orgId) {
        return sqlSession.selectOne(MAPPER + "getOrganizationBaseInfo", orgId);
    }
}
