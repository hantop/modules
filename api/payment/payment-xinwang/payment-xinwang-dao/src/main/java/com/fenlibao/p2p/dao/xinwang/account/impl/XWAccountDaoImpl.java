package com.fenlibao.p2p.dao.xinwang.account.impl;

import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.model.xinwang.entity.account.PlatformAccount;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/15.
 */
@Repository
public class XWAccountDaoImpl implements XWAccountDao{

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "XWAccountMapper.";

    @Override
    public void createXWAccount(XinwangAccount account)  {
        sqlSession.insert(MAPPER + "createXWAccount",account);
    }

    @Override
    public void createPTAssetAccount(Map<String, Object> params)  {
        sqlSession.insert(MAPPER + "createPTAssetAccount",params);
    }

    @Override
    public Boolean getIdentityAuthState(Integer userId) {
        Boolean result=sqlSession.selectOne(MAPPER + "getIdentityAuthState",userId);
        return result;
    }

    @Override
    public void updatePTAccountIdentityState(Integer userId)  {
        sqlSession.update(MAPPER + "updatePTAccountIdentityState",userId);
    }

    @Override
    public void updatePTAccountInfo(Map<String, Object> params)  {
        sqlSession.update(MAPPER + "updatePTAccountInfo",params);
    }

    @Override
    public PlatformAccount getPlatformAccountInfoByPlatformUserNo(String platformUserNo)  {
        return sqlSession.selectOne(MAPPER + "getPlatformAccountInfoByPlatformUserNo",platformUserNo);
    }

    @Override
    public PlatformAccount getPlatformAccountInfoByUserId(Integer userId) {
        return sqlSession.selectOne(MAPPER + "getPlatformAccountInfoByUserId",userId);
    }

    @Override
    public XWFundAccount getFundAccount(Integer userId, SysFundAccountType type) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("userId", userId);
        params.put("type", type);
        return sqlSession.selectOne(MAPPER + "getFundAccount", params);
    }

    @Override
    public XinwangAccount getXinwangAccount(String platformUserNo) {
        return sqlSession.selectOne(MAPPER + "getXinwangAccount",platformUserNo);
    }

    @Override
    public void updatePlatformMarketingAccount(Map<String, Object> m1) {
        sqlSession.update(MAPPER + "updatePlatformMarketingAccount", m1);
    }

    @Override
    public void updatePTAccountWLZH(Map<String, Object> m2) {
        sqlSession.update(MAPPER + "updatePTAccountWLZH", m2);
    }

    @Override
    public BigDecimal getPlatformMarketingAccount(Map<String, Object> n1) {
        BigDecimal result = sqlSession.selectOne(MAPPER + "getPlatformMarketingAccount", n1);
        return result;
    }

    @Override
    public BigDecimal getPTAccountWLZH(Map<String, Object> n2) {
        BigDecimal result = sqlSession.selectOne(MAPPER + "getPTAccountWLZH", n2);
        return result;
    }

 	@Override
    public void updateFundAccount(Map<String, Object> params)  {
        sqlSession.update(MAPPER + "updateFundAccount",params);
    }

    @Override
    public void updateXWAccount(Map<String, Object> params) {
        sqlSession.update(MAPPER + "updateXWAccount", params);
    }

    @Override
    public Integer countOverdue(Integer userId) {
        return sqlSession.selectOne(MAPPER + "countOverdue", userId);
    }

    @Override
    public Integer getPMSAudit(Integer userId, UserRole userRole) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("userId", userId);
        params.put("userRole", userRole.getCode());
        return sqlSession.selectOne(MAPPER + "getPMSAudit", params);
    }

    @Override
    public void updatePMSAuditStatus(Integer id) {
        sqlSession.update(MAPPER + "updatePMSAuditStatus", id);
    }

    @Override
    public XinwangAccount getXWRoleAccount(int userId, UserRole userRole) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("userId", userId);
        params.put("userRole", userRole);
        return sqlSession.selectOne(MAPPER + "getXWRoleAccount", params);
    }

    @Override
    public Integer getAccountIdByUserId(Map<String, Object> o1) {
        return sqlSession.selectOne(MAPPER + "getAccountIdByUserId", o1);
    }

    @Override
    public Integer getAccountIdByAccountNameAndaAccountType(Map<String, Object> paramMap) {
        return sqlSession.selectOne(MAPPER + "getAccountIdByAccountNameAndaAccountType", paramMap);
    }

    @Override
    public Integer getUserIdByAccountNameAndaAccountType(Map<String, Object> paramMap) {
        return sqlSession.selectOne(MAPPER + "getUserIdByAccountNameAndaAccountType", paramMap);
    }

    @Override
    public void clearBankInfo(String platformUserNo) {
        sqlSession.update(MAPPER + "clearBankInfo", platformUserNo);
    }

    @Override
    public void updateAuditStatusToT6161(Map<String, Object> param) {
        sqlSession.update(MAPPER + "updateAuditStatusToT6161", param);
    }

    @Override
    public void updateAuditStatus(String reviewStatus, String platformUserNo) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("auditStatus", reviewStatus);
        params.put("platformUserNo", platformUserNo);
        sqlSession.update(MAPPER + "updateAuditStatus", params);
    }

    @Override
    public Integer countXWAccount(String platformUserNo) {
        return sqlSession.selectOne(MAPPER + "countXWAccount", platformUserNo);
    }

    @Override
    public void updateXWAccountAuditStatus(XinwangAccount account) {
        sqlSession.update(MAPPER + "updateXWAccountAuditStatus", account);
    }

    @Override
    public void updateFundAccountPlus(Map<String, Object> params) {
        sqlSession.update(MAPPER + "updateFundAccountPlus", params);
    }

    @Override
    public void updateFundAccountMinus(Map<String, Object> params) {
        sqlSession.update(MAPPER + "updateFundAccountMinus", params);
    }
}
