package com.fenlibao.p2p.sms.service;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.sms.domain.GsmsResponse;
import com.fenlibao.p2p.sms.domain.MTPack;
import com.fenlibao.p2p.sms.domain.MessageData;
import com.fenlibao.p2p.sms.persistence.GsmsResponseMapper;
import com.fenlibao.p2p.sms.persistence.MTPackMapper;
import com.fenlibao.p2p.sms.persistence.MessageDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2015/8/28.
 */
public abstract class AbsTemplateSmsService {
    private static final Logger log = LoggerFactory.getLogger(AbsTemplateSmsService.class);

    @Autowired
    private MTPackMapper mtPackMapper;
    @Autowired
    private MessageDataMapper messageDataMapper;
    @Autowired
    private GsmsResponseMapper gsmsResponseMapper;


    /**
     * 发送消息
     *
     * @param pack
     * @return
     */
    public final GsmsResponse post(MTPack pack) throws Exception {
        this.mtPackMapper.insertSelective(pack);
        GsmsResponse gsmsResponse = null;
        pack.setCustomNum(String.valueOf(pack.getId()));
        ISmsServer smsServer = this.getSmsServer(pack);
        String errorMsg = "";
        try {
            pack.setServerType(smsServer.getServerType());
            pack.setTunnel(smsServer.getSmsServerName());
            gsmsResponse = smsServer.sendMsg(pack);
            gsmsResponse.setServerType(smsServer.getSmsServerName());
            log.info("------->>>");
            log.info("短信发送：" + JSON.toJSONString(pack.getMsgs()) + ",返回结果：" + JSON.toJSONString(gsmsResponse));
        } catch (Exception e) {
            log.error(e.getMessage());
            errorMsg = e.getMessage();
        } finally {
            if (gsmsResponse != null) {
                this.gsmsResponseMapper.insertSelective(gsmsResponse);
            } else {
                gsmsResponse = new GsmsResponse();
                gsmsResponse.setResult(-1);
                gsmsResponse.setMessage(errorMsg);
                gsmsResponse.setAttributes(errorMsg);
                gsmsResponse.setCreateTime(System.currentTimeMillis());
                gsmsResponse.addErrors(gsmsResponse);
                this.gsmsResponseMapper.insertSelective(gsmsResponse);
            }
            pack.setGsmsResponseId(gsmsResponse.getId());
            this.mtPackMapper.updateByPrimaryKeySelective(pack);
            //保存短信详细信息
            if (pack.getMsgs().size() > 0) {
                for (MessageData messageData : pack.getMsgs()) {
                    messageData.setCreateTime(System.currentTimeMillis());
                    messageData.setMtPackId(pack.getId());
                }
                this.messageDataMapper.insertBatch(pack.getMsgs());
            }
        }
        return gsmsResponse;
    }


    protected abstract ISmsServer getSmsServer(MTPack pack);
}
