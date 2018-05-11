package com.fenlibao.p2p.service.bid;

import com.fenlibao.p2p.model.entity.ShopTreasureInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by LouisWang on 2015/8/14.
 */
public interface BidService {

    public boolean isValidPassword(int accountId, String tradePassword) throws Throwable;

    ShopTreasureInfo findShopTreasureInfo(int bid)throws Throwable;

    void doBid(int bidId, BigDecimal amount, int accountId, String experFlg,
               String fxhbIds) throws Throwable;

    void doBid(int bidId, BigDecimal amount, int accountId, String experFlg,
               String fxhbIds, String jxqId) throws Throwable;
}

