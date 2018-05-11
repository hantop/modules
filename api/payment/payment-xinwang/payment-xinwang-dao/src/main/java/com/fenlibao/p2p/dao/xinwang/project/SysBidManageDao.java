package com.fenlibao.p2p.dao.xinwang.project;


import com.fenlibao.p2p.model.xinwang.entity.bid.SysBidInfo;
import com.fenlibao.p2p.model.xinwang.entity.bid.TBidExtUser;
import com.fenlibao.p2p.model.xinwang.entity.order.SysTenderOrder;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysTenderRecord;

import java.math.BigDecimal;
import java.util.Map;

public interface SysBidManageDao {

    SysTenderOrder getTenderOrder(int orderId);

    SysBidInfo getBidById(int bidId);

    void updateBid(Map<String, Object> bidParams);

    int addTenderRecord(SysTenderRecord tenderRecord);

    void updateTenderOrder(int orderId, int recordId);

    int countTenderOrderOfDQR(int bidId, int tenderId);

    void updateBidExInfo(Map<String, Object> params);

    TBidExtUser getTBidExtUser(Integer bid);

    Map<String,BigDecimal> getBidOtherRateByBid(Integer bid);
}
