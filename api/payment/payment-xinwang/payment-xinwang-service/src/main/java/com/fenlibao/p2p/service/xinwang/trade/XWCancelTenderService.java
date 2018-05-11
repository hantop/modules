package com.fenlibao.p2p.service.xinwang.trade;

/**
 * 流标
 */
public interface XWCancelTenderService {
    void cancelTenders(Integer projectId)throws Exception;
    void handleError(Integer orderId)throws Exception;
}
