package com.fenlibao.p2p.weixin.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by Administrator on 2015/8/18.
 */
public class MessageEvent extends ApplicationEvent {

    public MessageEvent(Object source) {
        super(source);
    }
}
