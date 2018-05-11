package com.fenlibao.p2p.service.channel;

import java.util.Map;

/**
 * 平台的基础通道
 * @author Mingway.Xu
 * @date 2017/3/17 14:59
 */
public interface PaymentChannelService {
    /**
     * 获取支付通道的参数
     * @return
     */
    Map getBaseChannel();
}
