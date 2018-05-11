package com.fenlibao.p2p.dao.bid.impl;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.bid.TenderShareDao;
import com.fenlibao.p2p.model.entity.redenvelope.ReceiveTenderShareEntity;
import com.fenlibao.p2p.model.entity.redenvelope.TenderShareEntity;
import com.fenlibao.p2p.model.vo.share.ShareRecordVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zcai on 2016/6/25.
 */
@Repository
public class TenderShareDaoImpl extends BaseDao implements TenderShareDao {

    public TenderShareDaoImpl() {
        super("TenderShareMapper");
    }


    @Override
    public void addTenderShare(TenderShareEntity entity) throws Exception {
        sqlSession.insert(MAPPER + "addTenderShare", entity);
    }

    @Override
    public void addReceiveRecord(ReceiveTenderShareEntity entity) throws Exception {
        sqlSession.insert(MAPPER + "addReceiveRecord", entity);
    }

    @Override
    public TenderShareEntity getRecordByCode(String code) {
        return sqlSession.selectOne(MAPPER + "getRecordByCode", code);
    }

    @Override
    public int getReceiveCount(String phoneNum, int investmentShareId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("phoneNum", phoneNum);
        params.put("investmentShareId", investmentShareId);
        return sqlSession.selectOne(MAPPER + "getReceiveCount", params);
    }

    @Override
    public void updateShareRemainingQty(int id, int remainingQty) {
        Map<String, Integer> params = new HashMap<>(2);
        params.put("id", id);
        params.put("remainingQty", remainingQty);
        sqlSession.update(MAPPER + "updateShareRemainingQty", params);
    }

    @Override
    public int getShareCount(String userId, int tenderId, int itemType) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("userId", userId);
        params.put("tenderId", tenderId);
        params.put("itemType", itemType);
        return sqlSession.selectOne(MAPPER + "getShareCount", params);
    }

    @Override
    public List<ReceiveTenderShareEntity> getRedEnvelopeQty(String phoneNum) {
        return sqlSession.selectList(MAPPER + "getRedEnvelopeQty", phoneNum);
    }

    @Override
    public List<ShareRecordVO> getRestShareNum(String redEnvelopeCode) {
        return sqlSession.selectList(MAPPER + "getRestShareNum", redEnvelopeCode);
    }
}
