package com.fenlibao.p2p.dao.xinwang.project.impl;

import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.model.xinwang.bo.InvestorBO;
import com.fenlibao.p2p.model.xinwang.bo.XWTenderBO;
import com.fenlibao.p2p.model.xinwang.entity.project.*;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysTenderRecord;
import com.fenlibao.p2p.model.xinwang.enums.entrust.AuthorizeStatus;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/19.
 */
@Repository
public class XWProjectDaoImpl implements XWProjectDao{

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "XWProjectMapper.";

    @Override
    public XWProjectInfo getProjectInfoById(Integer loanId) {
        return sqlSession.selectOne(MAPPER + "getProjectInfoById",loanId);
    }

    @Override
    public void establishProject(Map<String, Object> params) {
        sqlSession.update(MAPPER + "establishProject",params);
    }

    @Override
    public List<XWTenderRecord> getTenderRecord(Map<String, Object> params) {
        List<XWTenderRecord> list=sqlSession.selectList(MAPPER + "getTenderRecord", params);
        return list;
    }

    @Override
    public XWProjectRate getProjectRateById(Integer loanId) {
        return sqlSession.selectOne(MAPPER + "getProjectRateById",loanId);
    }

    @Override
    public XWTenderRecord getTenderRecordById(Integer id) {
        return sqlSession.selectOne(MAPPER + "getTenderRecordById",id);
    }

    @Override
    public void insertTenderEntity(XWTenderEntity tenderEntity) {
        sqlSession.insert(MAPPER + "insertTenderEntity", tenderEntity);
    }

    @Override
    public void updateTenderRecordById(Map<String, Object> params) {
        sqlSession.update(MAPPER + "updateTenderRecordById", params);
    }

    @Override
    public void updateProjectExtraInfo(Map<String, Object> params) {
        sqlSession.update(MAPPER + "updateProjectExtraInfo", params);
    }

    @Override
    public void batchInsertRepaymentPlan(List<XWRepaymentPlan> list) {
        sqlSession.insert(MAPPER + "batchInsertRepaymentPlan", list);
    }

    @Override
    public int updateProjectInfo(Map<String, Object> params) {
        return sqlSession.update(MAPPER + "updateProjectInfo", params);
    }

    @Override
    public XWProjectExtraInfo getProjectExtraInfo(Integer projectId) {
        return sqlSession.selectOne(MAPPER + "getProjectExtraInfo",projectId);
    }

    @Override
    public void setEntrustInfo(Integer loadId, String entrustedPlatformUserNo, String requestNo, AuthorizeStatus status) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("loadId", loadId);
        params.put("entrustedPlatformUserNo", entrustedPlatformUserNo);
        params.put("authorizeRequestNo", requestNo);
        params.put("authorizeStatus", status);
        sqlSession.update(MAPPER + "setEntrustInfo", params);
    }

    @Override
    public List<Integer> getPreEstablishProject() {
        return sqlSession.selectList(MAPPER + "getPreEstablishProject");
    }

    @Override
    public void projectJoinPlan(Integer projectId) {
        sqlSession.insert(MAPPER + "projectJoinPlan",projectId);
    }

    @Override
    public Integer lockRepayStatus(Integer projectId) {
        return sqlSession.selectOne(MAPPER + "lockRepayStatus", projectId);
    }

    @Override
    public List<XWTenderBO> getSendTender() {
        return sqlSession.selectList(MAPPER + "getSendTender");
    }

    @Override
    public void updateXWTenderSend(Integer id,Boolean send) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("id", id);
        params.put("send", send);
        sqlSession.update(MAPPER + "updateXWTenderSend", params);
    }

    @Override
    public List<SysTenderRecord> getSysTenderRecord(SysTenderRecord tenderRecord) {
        return sqlSession.selectList(MAPPER + "getSysTenderRecord", tenderRecord);
    }

    @Override
    public List<Integer> getBidByStatus(PTProjectState state) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("state", state);
        return sqlSession.selectList(MAPPER+"getBidByStatus",params);
    }

    @Override
    public int updateProjectStatus(Map<String, Object> projectInfoParams) {
        return sqlSession.update(MAPPER + "updateProjectStatus", projectInfoParams);
    }

    @Override
    public InvestorBO getInvestorBO(Integer id) {
        return sqlSession.selectOne(MAPPER+"getInvestorBO",id);
    }


    @Override
    public void saveXWProjectPrepaymentConfig(XWProjectPrepaymentConfig xwProjectPrepaymentConfig) {
        sqlSession.insert(MAPPER + "saveXWProjectPrepaymentConfig", xwProjectPrepaymentConfig);
    }


    @Override
    public BigDecimal getInterestPercent(Integer state) {
        Map<String, Object> param = new HashMap<>();
        param.put("state", state);
        return sqlSession.selectOne(MAPPER+"getInterestPercent",param);
    }

    @Override
    public int updateInterestPercent(Integer loanId, BigDecimal percent) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", loanId);
        params.put("interestPercent", percent);
        return sqlSession.update(MAPPER + "updateInterestPercent", params);
    }

    @Override
    public List<Integer> getCancelBidsByStatus(PTProjectState state) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("state", state);
        return sqlSession.selectList(MAPPER+"getCancelBidsByStatus",params);
    }

}
