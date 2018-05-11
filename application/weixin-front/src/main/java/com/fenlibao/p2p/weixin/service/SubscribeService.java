package com.fenlibao.p2p.weixin.service;

import com.fenlibao.p2p.weixin.domain.Subscribe;
import com.fenlibao.p2p.weixin.repository.SubscribeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by 赵波 on 2015/10/21.
 */
@Service
public class SubscribeService {

    @Autowired
    private SubscribeRepository subscribeRepository;

    public String findEventKeyByOpendId(String openId) {
        Subscribe subscribe = subscribeRepository.findByOpendId(openId);
        if (subscribe == null) {
            return WeixinMessageHandler.DEFAULT_EVENT_KEY;
        }
        return subscribe.getEventKey();
    }

    @Async
    public Subscribe save(Subscribe subscribe) {
        return this.subscribeRepository.save(subscribe);
    }
}
