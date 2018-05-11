package com.fenlibao.p2p.service.xinwang.bid;


import com.fenlibao.p2p.model.xinwang.bo.XWTenderBO;
import com.fenlibao.p2p.model.xinwang.entity.bid.SysBidInfo;
import com.fenlibao.p2p.model.xinwang.entity.order.SysTenderOrder;

import java.math.BigDecimal;
import java.util.Map;

public interface SysBidManageService {

    SysTenderOrder getTenderOrder(int orderId);

    SysBidInfo process(int bidId, BigDecimal tenderAmount);

    void updateBid(Map<String, Object> bidParams);

    int createTenderOrder(XWTenderBO tender);
}
