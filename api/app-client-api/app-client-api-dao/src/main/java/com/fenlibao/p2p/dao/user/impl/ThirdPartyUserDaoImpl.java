package com.fenlibao.p2p.dao.user.impl;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.user.ThirdPartyUserDao;
import com.fenlibao.p2p.model.entity.bid.BidExtendRelatedGuaranteeInfo;
import com.fenlibao.p2p.model.entity.bid.ConsumeBid;
import com.fenlibao.p2p.model.entity.bid.InvestRecords;
import com.fenlibao.p2p.model.entity.bid.Tripleagreement;
import com.fenlibao.p2p.model.entity.user.UnRegUser;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * by kris
 */
@Repository
public class ThirdPartyUserDaoImpl extends BaseDao implements ThirdPartyUserDao {

    public ThirdPartyUserDaoImpl() {
        super("ThirdPartyUserMapper");
    }

    @Override
    public List<ConsumeBid> getUnSignBidList() {
        return sqlSession.selectList(MAPPER + "getUnSignBidList", null);
    }

    @Override
    public List<InvestRecords> getInvestRecords(int bidId) {
        Map<String, Integer> params = new HashMap<>(1);
        params.put("bidId", bidId);
        return sqlSession.selectList(MAPPER + "getInvestRecords", params);
    }

    @Override
    public List<UnRegUser> getUnRegUsers(int bidId) {
        Map<String, Integer> params = new HashMap<>(1);
        params.put("bidId", bidId);
        return sqlSession.selectList(MAPPER + "getUnRegUsers", params);
    }

    @Override
    public int addRegUser(Map map) {
        return sqlSession.insert(MAPPER + "addRegUser",map);
    }

    @Override
    public int updateRegUser(Map map) {
        return sqlSession.update(MAPPER + "updateRegUser",map);
    }


    @Override
    public ConsumeBid lockConsumeBid(Map map) {
        return sqlSession.selectOne(MAPPER + "lockConsumeBid", map);
    }

    @Override
    public int updateUnSignBidInfo(Map map) {
        return sqlSession.update(MAPPER + "updateUnSignBidInfo", map);
    }

    @Override
    public int recordError(Map map) {
        return sqlSession.insert(MAPPER + "recordError", map);
    }

    @Override
    public List<ConsumeBid> getSignFailConsumeBids() {
        return sqlSession.selectList(MAPPER + "getSignFailConsumeBids", null);
    }

    @Override
    public ConsumeBid getSignFailConsumeBid(Map map) {
        return sqlSession.selectOne(MAPPER + "getSignFailConsumeBid", map);
    }

    @Override
    public List<Tripleagreement> getUnSignTripleagreements() {
        return sqlSession.selectList(MAPPER + "getUnSignTripleagreements", null);
    }

    @Override
    public Tripleagreement getUnSignTripleagreement(Map map) {
        return sqlSession.selectOne(MAPPER + "getUnSignTripleagreement", map);
    }

    @Override
    public int updateTripleagreement(Map map) {
        return sqlSession.update(MAPPER + "updateTripleagreement",map);
    }

    @Override
    public List<Tripleagreement> getSignFailTripleagreements() {
        return sqlSession.selectList(MAPPER + "getSignFailTripleagreements", null);
    }

    @Override
    public Tripleagreement getSignFailTripleagreement(Map map) {
        return sqlSession.selectOne(MAPPER + "getSignFailTripleagreement", map);
    }

    @Override
    public Integer countRegisterThirdParty(String email, int platform) {
        Map map = new HashMap();
        map.put("email", email);
        map.put("platform", platform);
        return sqlSession.selectOne(MAPPER + "countRegisterThirdParty", map);
    }

    @Override
    public List<Integer> getInvestorUserIdList(int bidId) {
        return sqlSession.selectList(MAPPER + "getInvestorUserIdList", bidId);
    }

    @Override
    public void addSSQRegUser(Map map) {
        sqlSession.insert(MAPPER + "addSSQRegUser", map);
    }
}
