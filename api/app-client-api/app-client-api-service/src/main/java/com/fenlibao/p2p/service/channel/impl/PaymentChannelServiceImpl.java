package com.fenlibao.p2p.service.channel.impl;

import com.fenlibao.p2p.dao.channel.PaymentChannelDao;
import com.fenlibao.p2p.service.channel.PaymentChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Mingway.Xu
 * @date 2017/3/17 15:02
 */
@Service
public class PaymentChannelServiceImpl implements PaymentChannelService {
    @Autowired
    private PaymentChannelDao paymentChannelDao;

    @Override
    public Map getBaseChannel() {
        return paymentChannelDao.getBaseChannel(1);//默认使用id为1的配置
    }
}
