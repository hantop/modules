package com.fenlibao.p2p.service.dm.hx.busi;

import com.fenlibao.p2p.model.dm.vo.HXBalanceVO;
import com.fenlibao.p2p.service.dm.hx.HXOrderProcess;

import java.math.BigDecimal;

/**
 * Created by zcai on 2016/11/30.
 */
public interface HXCommonService extends HXOrderProcess {

    /**
     *
     * @param userId
     * @throws Exception
     */
    HXBalanceVO getBalance(int userId) throws Exception;

    /**
     * 校验余额是否足够
     * <p>余额不足抛 TradeException(TradeResponseCode.PAYMENT_INSUFFICIENT_BALANCE) 请行处理</p>
     * @param userId
     * @param amount
     * @throws Exception
     */
    void validateBalance(int userId, BigDecimal amount) throws Exception;

    /**
     * 单笔奖励或分红
     * @param userId
     * @param amount
     * @param remark
     * @return success or failure
     * @throws Exception
     */
    boolean singleRewards(int userId, BigDecimal amount, String remark) throws Exception;
}
