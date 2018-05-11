package com.fenlibao.p2p.dao.bid;

import com.fenlibao.p2p.model.entity.redenvelope.ReceiveTenderShareEntity;
import com.fenlibao.p2p.model.entity.redenvelope.TenderShareEntity;
import com.fenlibao.p2p.model.vo.share.ShareRecordVO;

import java.util.List;

/**
 * Created by zcai on 2016/6/25.
 */
public interface TenderShareDao {

    /**
     * 添加投资分享记录
     * @param entity
     * @throws Exception
     */
    void addTenderShare(TenderShareEntity entity) throws Exception;
    /**
     * 添加领取红包记录
     * @param entity
     * @throws Exception
     */
    void addReceiveRecord(ReceiveTenderShareEntity entity) throws Exception;
    /**
     * 通过code获取投资分享记录(同时锁住记录)
     * @param code
     * @return
     */
    TenderShareEntity getRecordByCode(String code);
    /**
     * 获取领取记录数
     * @param phoneNum
     * @param investmentShareId
     * @return
     */
    int getReceiveCount(String phoneNum, int investmentShareId);
    /**
     * 更新红包分享剩余数量
     * @param id
     * @param remainingQty
     */
    void updateShareRemainingQty(int id, int remainingQty);
    /**
     * 获取投资分享次数
     * @param userId
     * @param tenderId
     * @return
     */
    int getShareCount(String userId, int tenderId, int itemType);
    /**
     * 获取用户红包数量
     * @param phoneNum
     * @return
     */
    List<ReceiveTenderShareEntity> getRedEnvelopeQty(String phoneNum);

    /**
     * 获取分享剩余的次数
     * @param redEnvelopeCode
     * @return
     */
    List<ShareRecordVO> getRestShareNum(String redEnvelopeCode);
}
