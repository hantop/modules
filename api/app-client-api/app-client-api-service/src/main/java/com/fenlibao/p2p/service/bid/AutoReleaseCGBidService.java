package com.fenlibao.p2p.service.bid;

/**
 * 将录入的存管消费信贷标调用存管接口进行发布
 */
public interface AutoReleaseCGBidService {

    /**
     * 自动发布
     */
    void startAutoReleaseCGBid() throws Throwable;
}

