package com.fenlibao.p2p.sms.service;

import com.fenlibao.p2p.sms.domain.GsmsResponse;
import com.fenlibao.p2p.sms.domain.MTPack;

/**
 * Created by Administrator on 2015/8/28.
 */
public interface ISmsServer {

    /**
     * 发送短信
     * @param pack
     * @return
     * @throws Exception
     */
    GsmsResponse sendMsg(MTPack pack) throws Exception;

    /**
     * 发送短信的服务商
     * @return
     */
    String getSmsServerName();

    /**
     * 获取发送短信的通道
     * @return
     */
    MTPack.ServerType getServerType();
}
