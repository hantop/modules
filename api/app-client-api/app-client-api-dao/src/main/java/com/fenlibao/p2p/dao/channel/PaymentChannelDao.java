package com.fenlibao.p2p.dao.channel;

import com.fenlibao.p2p.util.api.annotations.RedisCache;

import java.util.Map;

/**
 * 支付通道
 * @author Mingway.Xu
 * @date 2017/3/17 15:05
 */
public interface PaymentChannelDao {
    /**
     * 获取支付通道的参数
     * @return
     */
    @RedisCache(type = Map.class, cacheFlag = "1")
    Map getBaseChannel(int id);
}
