package com.fenlibao.p2p.dao.xinwang.project.impl;

import com.fenlibao.p2p.dao.xinwang.project.SysBidManageDao;
import com.fenlibao.p2p.model.xinwang.entity.bid.SysBidInfo;
import com.fenlibao.p2p.model.xinwang.entity.bid.TBidExtUser;
import com.fenlibao.p2p.model.xinwang.entity.order.SysTenderOrder;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysTenderRecord;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2017/6/1 9:47
 */
@Service
public class SysBidManageDaoImpl implements SysBidManageDao {
    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "SysBidManageMapper.";

    @Override
    public SysTenderOrder getTenderOrder(int orderId) {
        return sqlSession.selectOne(MAPPER + "getTenderOrder", orderId);
    }

    @Override
    public SysBidInfo getBidById(int bidId) {
        return sqlSession.selectOne(MAPPER + "getBidById", bidId);
    }

    @Override
    public void updateBid(Map<String, Object> bidParams) {
        sqlSession.update(MAPPER+"updateBid", bidParams);
    }

    @Override
    public int addTenderRecord(SysTenderRecord tenderRecord) {
        return sqlSession.insert(MAPPER + "addTenderRecord", tenderRecord);
    }

    @Override
    public void updateTenderOrder(int orderId, int recordId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("orderId", orderId);
        params.put("recordId", recordId);
        sqlSession.update(MAPPER + "updateTenderOrder", params);
    }

    @Override
    public int countTenderOrderOfDQR(int bidId, int tenderId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("bidId", bidId);
        params.put("tenderId", tenderId);
        return sqlSession.selectOne(MAPPER + "countTenderOrderOfDQR", params);
    }

    @Override
    public void updateBidExInfo(Map<String, Object> params) {
        sqlSession.update(MAPPER+"updateBidExInfo", params);
    }

    @Override
    public TBidExtUser getTBidExtUser(Integer bid) {
        return sqlSession.selectOne(MAPPER + "getTBidExtUser", bid);
    }

    @Override
    public Map<String, BigDecimal> getBidOtherRateByBid(Integer bid) {
        return sqlSession.selectOne(MAPPER + "getBidOtherRateByBid", bid);
    }
}
