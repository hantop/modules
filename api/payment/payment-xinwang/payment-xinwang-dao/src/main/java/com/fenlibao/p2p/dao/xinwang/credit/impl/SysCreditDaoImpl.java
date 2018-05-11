package com.fenlibao.p2p.dao.xinwang.credit.impl;

import com.fenlibao.p2p.dao.xinwang.credit.SysCreditDao;
import com.fenlibao.p2p.model.xinwang.entity.credit.BaseCreditInfo;
import com.fenlibao.p2p.model.xinwang.entity.credit.SysCredit;
import com.fenlibao.p2p.model.xinwang.entity.credit.SysCreditTransferApply;
import com.fenlibao.p2p.model.xinwang.entity.credit.SysTransferInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date 2017/6/1 14:54
 */
@Repository
public class SysCreditDaoImpl implements SysCreditDao {
    private final String MAPPER = "SysCreditMapper.";
    @Resource
    private SqlSession sqlSession;

    @Override
    public SysTransferInfo getTransferInfoByOrder(int orderId) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("orderId", orderId);
        return sqlSession.selectOne(MAPPER + "getTransferInfoByOrder", params);
    }

    @Override
    public void createCredit(SysCredit credit) {
        sqlSession.insert(MAPPER + "createCredit",credit);
    }

    @Override
    public List<SysCredit> getCreditInfoByProjectId(Integer projectId) {
        return sqlSession.selectList(MAPPER + "getCreditInfoByProjectId", projectId);
    }

    @Override
    public SysCredit getCreditInfoById(Integer id) {
        return sqlSession.selectOne(MAPPER + "getCreditInfoById", id);
    }

    @Override
    public void updateCreditInfoById(Map<String, Object> params) {
        sqlSession.update(MAPPER + "updateCreditInfoById", params);
    }

    @Override
    public SysCreditTransferApply getTransferingCreditByCreditId(Integer creditId) {
        return sqlSession.selectOne(MAPPER + "getTransferingCreditByCreditId", creditId);
    }

    @Override
    public void updateCreditTransferApplyById(Map<String, Object> params) {
        sqlSession.update(MAPPER + "updateCreditTransferApplyById", params);
    }

    @Override
    public SysTransferInfo getTransferInfo(int creditId) {
        return sqlSession.selectOne(MAPPER+"getTransferInfoByCreditId",creditId);
    }

    @Override
    public BaseCreditInfo getBaseCreditInfo(int creditId) {
        return sqlSession.selectOne(MAPPER+"getBaseCreditInfo",creditId);
    }
}
