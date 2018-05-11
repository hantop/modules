package com.fenlibao.p2p.dao.user;

import com.fenlibao.p2p.model.entity.bid.AutoReleaseCGBidInfo;

import java.util.Map;

/**
 * 将录入的存管消费信贷标调用存管接口进行发布
 */
public interface AutoReleaseCGBidDao {

    AutoReleaseCGBidInfo getYCLBidInfo();

    void lockYCLBidInfo(int id);

    String getBidStatus(int bid);

    void addToProductLib(int bid);

    void updateConsumeBidInfoStatus(Map map);
}
