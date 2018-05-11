package com.fenlibao.p2p.service.xinwang.trade;

import com.fenlibao.p2p.model.xinwang.entity.common.BaseResult;
import com.fenlibao.p2p.model.xinwang.entity.trade.PreTransactionResult;
import com.fenlibao.p2p.model.xinwang.entity.trade.SyncTransactionResult;
import com.fenlibao.p2p.model.xinwang.param.transaction.PreTransactionParam;
import com.fenlibao.p2p.model.xinwang.param.transaction.SyncTransactionParam;

import java.math.BigDecimal;

/**
 * 平台交易
 * @date 2017/5/11 15:59
 */
public interface XWTransactionService {
    /**
     * 预处理
     * @return
     */
    PreTransactionResult preTransaction(PreTransactionParam param) throws Exception;

    /**
     * 单笔交易
     * @return
     * @throws Exception
     */
    SyncTransactionResult doSyncTransaction(SyncTransactionParam param) throws Exception;

    /**
     * 取消交易
     * @param preTransactionNo
     * @param amount
     * @return
     */
    BaseResult cancelTransaction(String preTransactionNo, BigDecimal amount);

}
