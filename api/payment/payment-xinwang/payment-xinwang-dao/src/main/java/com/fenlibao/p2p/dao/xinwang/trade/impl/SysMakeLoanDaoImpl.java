package com.fenlibao.p2p.dao.xinwang.trade.impl;

import com.fenlibao.p2p.dao.xinwang.trade.SysMakeLoanDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWTenderRecord;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysProjectConfirmTenderInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 放款
 */
@Repository
public class SysMakeLoanDaoImpl implements SysMakeLoanDao{
    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "SysMakeLoanMapper.";

    @Override
    public int saveMakeLoanRequestNo(Map<String,Object> params) {
        return sqlSession.update(MAPPER + "saveMakeLoanRequestNo",params);
    }

    @Override
    public XWProjectInfo getProjectInfoByMakeLoanRequestNo(String makeLoanRequestNo) {
        return sqlSession.selectOne(MAPPER + "getProjectInfoByMakeLoanRequestNo",makeLoanRequestNo);
    }

    @Override
    public XWTenderRecord getTenderRecordByMakeLoanRequestNo(String makeLoanRequestNo) {
        return sqlSession.selectOne(MAPPER + "getTenderRecordByMakeLoanRequestNo",makeLoanRequestNo);
    }

    @Override
    public void createProjectConfirmTenderInfo(SysProjectConfirmTenderInfo projectConfirmTenderInfo) {
        sqlSession.insert(MAPPER + "createProjectConfirmTenderInfo",projectConfirmTenderInfo);
    }

    @Override
    public Integer getOngoingConfirmTenderOrder(Integer projectId) {
        return sqlSession.selectOne(MAPPER + "getOngoingConfirmTenderOrder",projectId);
    }

    @Override
    public SysProjectConfirmTenderInfo getProjectConfirmTenderInfoByOrderId(Integer orderId) {
        return sqlSession.selectOne(MAPPER + "getProjectConfirmTenderInfoByOrderId",orderId);
    }

    @Override
    public List<XWTenderRecord> getAcceptFailTenderRecordList(Integer projectId) {
        return sqlSession.selectList(MAPPER + "getAcceptFailTenderRecordList",projectId);
    }

    @Override
    public List<String> getPlatformConfirmTenderFailRequestList(Integer projectId) {
        return sqlSession.selectList(MAPPER + "getPlatformConfirmTenderFailRequestList",projectId);
    }

    @Override
    public List<XWRequest> getResultConfirmRequest(XWRequest request) {
        return sqlSession.selectList(MAPPER + "getResultConfirmRequest",request);
    }

    @Override
    public List<XWTenderRecord> getPlatformConfirmTenderFailTenderList() {
        return sqlSession.selectList(MAPPER + "getPlatformConfirmTenderFailTenderList");
    }

    @Override
    public List<Integer> getAutoConfirmTenderProjectList() {
        return sqlSession.selectList(MAPPER + "getAutoConfirmTenderProjectList");
    }

    @Override
    public List<SysProjectConfirmTenderInfo> getNotFinishConfirmTenderRecords() {
        return sqlSession.selectList(MAPPER + "getNotFinishConfirmTenderRecords");
    }

    @Override
    public void createMakeloanListen(Map<String, Object> listen) {
        sqlSession.insert(MAPPER + "createMakeloanListen", listen);
    }
}
