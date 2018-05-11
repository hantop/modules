package com.fenlibao.p2p.sms.service.impl;

import com.fenlibao.p2p.sms.domain.MTPack;
import com.fenlibao.p2p.sms.service.Constants;
import com.fenlibao.p2p.sms.service.AbsXuanwuServer;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2015/8/28.
 */
@Service(Constants.XUANWU_STANDARD)
public class StandardXuanwuServer extends AbsXuanwuServer {

    @Override
    public String getTunnel() {
        return Constants.XUANWU_STANDARD;
    }

    @Override
    public MTPack.ServerType getServerType() {
        return MTPack.ServerType.XUANWU_STANDARD;
    }

    @Override
    public String getSmsServerName() {
        return MTPack.ServerType.XUANWU_STANDARD.getName();
    }


}
