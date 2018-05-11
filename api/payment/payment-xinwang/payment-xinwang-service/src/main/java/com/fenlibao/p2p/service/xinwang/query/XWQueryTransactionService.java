package com.fenlibao.p2p.service.xinwang.query;

/**
 * Created by Administrator on 2017/5/18.
 */
public interface XWQueryTransactionService {
    String queryTransaction(Integer userId, String userRole, String transactionType, String requestNo);
}
