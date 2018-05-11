package com.fenlibao.p2p.service.bid;

import com.fenlibao.p2p.model.vo.invite.InviteUrlInfoVO_131;

import java.util.Map;

/**
 * Created by zcai on 2016/6/25.
 */
public interface TenderShareService {

    /**
     * 添加投资分享红包记录
     * @param userId
     * @param shareId
     * @param itemType
     * @throws Exception
     */
    InviteUrlInfoVO_131 addTenderShare(int userId, int shareId, int itemType) throws Exception;
    /**
     * 添加领取红包记录
     * @param phoneNum
     * @param redEnvelopeCode
     * @return 红包的金额
     * @throws Exception
     */
    Map<String, Object> addReceiveRecord(String phoneNum, String redEnvelopeCode) throws Exception;
    /**
     * 发放领取的投资分享的红包
     * @param userId
     * @param phoneNum
     */
    void grantRedEnvelopeForRegister(String userId, String phoneNum);


}
