package com.fenlibao.p2p.dao.xinwang.pay.impl;

import com.fenlibao.p2p.dao.xinwang.pay.XWWithdrawDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.pay.SysWithdrawApply;
import com.fenlibao.p2p.model.xinwang.entity.pay.XWWithdrawRequest;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/9.
 */
@Repository
public class XWWithdrawDaoImpl implements XWWithdrawDao{
    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "XWWithdrawMapper.";

    @Override
    public Integer getSuccessApplyId(Integer userId) {
        return sqlSession.selectOne(MAPPER + "getSuccessApplyId", userId);
    }

    @Override
    public void createWithdrawApply(SysWithdrawApply withdrawApply) {
        sqlSession.insert(MAPPER + "createWithdrawApply", withdrawApply);
    }

    @Override
    public void createWithdrawRequest(XWWithdrawRequest withdrawRequest) {
        sqlSession.insert(MAPPER + "createWithdrawRequest", withdrawRequest);
    }

    @Override
    public void updateWithdrawRequest(Map<String, Object> params) {
        sqlSession.update(MAPPER + "updateWithdrawRequest", params);
    }

    @Override
    public void updateWithdrawApply(Map<String, Object> params) {
        sqlSession.update(MAPPER + "updateWithdrawApply", params);
    }

    @Override
    public List<XWRequest> getPageExpiredRequest() {
        return sqlSession.selectList(MAPPER + "getPageExpiredRequest");
    }

    @Override
    public List<XWRequest> getResultConfirmRequest() {
        return sqlSession.selectList(MAPPER + "getResultConfirmRequest");
    }
}
