package com.fenlibao.p2p.service.xinwang.pay;

import com.fenlibao.p2p.model.xinwang.entity.pay.XWFundsTransfer;

import java.util.List;

/**
 * @date 2017/7/1 11:11
 */
public interface XWFundsTransferService {
    void batchInsertFlow(List<XWFundsTransfer> transferList);
}
