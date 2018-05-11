package com.fenlibao.p2p.dao.xinwang.trade.impl;

import com.fenlibao.p2p.dao.xinwang.trade.SysCancelTenderDao;
import com.fenlibao.p2p.model.xinwang.entity.project.XWTenderRecord;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysProjectCancelTenderInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 流标
 */
@Repository
public class SysCancelTenderDaoImpl implements SysCancelTenderDao{

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "SysCancelTenderMapper.";

    @Override
    public void createProjectCancelTenderInfo(SysProjectCancelTenderInfo projectCancelTenderInfo) {
        sqlSession.insert(MAPPER + "createProjectCancelTenderInfo",projectCancelTenderInfo);
    }

    @Override
    public SysProjectCancelTenderInfo getProjectCancelTenderInfo(Integer orderId) {
        return sqlSession.selectOne(MAPPER + "getProjectCancelTenderInfo",orderId);
    }

    @Override
    public void saveCancelTenderRequestNo(Map<String, Object> params) {
        sqlSession.update(MAPPER + "saveCancelTenderRequestNo",params);
    }

    @Override
    public List<XWTenderRecord> getAcceptFailTenderRecordList(Integer projectId) {
        return sqlSession.selectList(MAPPER + "getAcceptFailTenderRecordList",projectId);
    }

    @Override
    public List<XWTenderRecord> getPlatformCancelTenderFailList(Integer projectId) {
        return sqlSession.selectList(MAPPER + "getPlatformCancelTenderFailList",projectId);
    }

    @Override
    public Integer getOngoingCancelTenderOrder(Integer projectId) {
        return sqlSession.selectOne(MAPPER + "getOngoingCancelTenderOrder",projectId);
    }
}
