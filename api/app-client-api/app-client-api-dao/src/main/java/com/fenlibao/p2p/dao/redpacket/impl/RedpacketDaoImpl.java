package com.fenlibao.p2p.dao.redpacket.impl;

import com.fenlibao.p2p.dao.redpacket.RedpacketDao;
import com.fenlibao.p2p.model.entity.FeeType;
import com.fenlibao.p2p.model.entity.UserAccount;
import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.entity.bid.InverstBidTradeInfo;
import com.fenlibao.p2p.model.vo.redpacket._RedPacketVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/16.
 */
@Repository
public class RedpacketDaoImpl implements RedpacketDao {
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "RedpacketMapper.";

    @Override
    public Integer getAccountId(Map<String, Object> paramMap) {
        return sqlSession.selectOne(MAPPER + "getAccountId", paramMap);
    }

    @Override
    public void addRedpackExceptionLog(Map<String, Object> paramMap) {
        sqlSession.insert(MAPPER + "addRedpackExceptionLog", paramMap);
    }

    @Override
    public FeeType getFeeType(int code) {
        return sqlSession.selectOne(MAPPER + "getFeeType", code);
    }

    @Override
    public int subtractUserAccountAmount(Map<String, Object> paramMap) {
        return sqlSession.update(MAPPER + "subtractUserAccountAmount", paramMap);
    }

    @Override
    public int increaseUserAccountAmount(Map<String, Object> paramMap) {
        return sqlSession.update(MAPPER + "increaseUserAccountAmount", paramMap);
    }

    @Override
    public UserAccount lockUserAccount(Map<String, Object> paramMap) {
        return sqlSession.selectOne(MAPPER + "lockUserAccount", paramMap);
    }

    @Override
    public UserAccount lockSalaryaccount(Integer accountId) {
        return sqlSession.selectOne(MAPPER + "lockSalaryaccount", accountId);
    }

    @Override
    public int addTruninFundsRecord(Map<String, Object> paramMap) {
        return sqlSession.insert(MAPPER + "addTruninFundsRecord", paramMap);
    }

    @Override
    public int addTrunoutFundsRecord(Map<String, Object> paramMap) {
        return sqlSession.insert(MAPPER + "addTrunoutFundsRecord", paramMap);
    }

    @Override
    public List<UserRedPacketInfo> getActivityRedBagByType(Map<String, Object> paramMap) {
        return sqlSession.selectList(MAPPER + "getActivityRedBagByType", paramMap);
    }

    @Override
    public int addUserRedpacket(Map<String, Object> paramMap) {
        return sqlSession.insert(MAPPER + "addUserRedpacket", paramMap);
    }

    @Override
    public List<UserRedPacketInfo> getRedpackets(Map<String, Object> paramMap) {
        return sqlSession.selectList(MAPPER + "getRedPackets",paramMap);
    }

    @Override
    public int getRedpacketCount(Map<String, Object> paramMap) {
        return sqlSession.selectOne(MAPPER + "getRedpacketCount", paramMap);
    }

    @Override
    public List<UserRedPacketInfo> getBidRedpacket(Map<String, Object> paramMap) {
        return sqlSession.selectList(MAPPER + "getBidRedpacket",paramMap);
    }

    @Override
    public void updateRedpacketsRelation(Map<String, Object> paramMap) {
        sqlSession.update(MAPPER + "updateRedpacketsRelation", paramMap);
    }

    @Override
    public void updateRedpacketsRelationForPlan(Map<String, Object> paramMap) {
        sqlSession.update(MAPPER + "updateRedpacketsRelationForPlan", paramMap);
    }

    @Override
    public InverstBidTradeInfo getBidOrderDetail(Integer orderId) {
        return sqlSession.selectOne(MAPPER + "getBidOrderDetail", orderId);
    }

    @Override
    public _RedPacketVO getById(int id) {
        return sqlSession.selectOne(MAPPER + "getById", id);
    }

    @Override
    public List<UserRedPacketInfo> getActivityRedBagBySetting(Map<String, Object> paramMap) {
        return sqlSession.selectList(MAPPER + "getActivityRedBagBySetting",paramMap);
    }

    @Override
    public List<UserRedPacketInfo> getActivityRedBagList(Map<String, Object> paramMap) {
        return sqlSession.selectList(MAPPER + "getActivityRedBagList",paramMap);
    }

    @Override
    public List<UserRedPacketInfo> getUserRedBagByActivity(Map<String, Object> paramMap) {
        return sqlSession.selectList(MAPPER + "getUserRedBagByActivity",paramMap);
    }
}
