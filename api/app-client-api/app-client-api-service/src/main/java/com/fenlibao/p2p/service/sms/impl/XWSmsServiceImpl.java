package com.fenlibao.p2p.service.sms.impl;

import com.dimeng.framework.message.sms.entity.SmsTask;
import com.fenlibao.p2p.model.business.sms.xuanwu.GsmsResponse;
import com.fenlibao.p2p.model.business.sms.xuanwu.MTPack;
import com.fenlibao.p2p.model.business.sms.xuanwu.MessageData;
import com.fenlibao.p2p.service.sms.XWSmsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Mingway.Xu
 * @date 2016/12/6 14:00
 */
@Service
public class XWSmsServiceImpl implements XWSmsService {
    private static final Logger logger = LogManager.getLogger(XWSmsServiceImpl.class);

    @Override
    public String sendXWSms(SmsTask smsTask, String sendUrl, String xwUsername, String xwPassword) {

        int len = smsTask.receivers.length;
        if (len <= 0) {
            return null;
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", "application/json");
            headers.add("Content-Type", "application/json;charset=utf-8");

            ArrayList<MessageData> msgs = new ArrayList<MessageData>();

            MessageData vo = new MessageData(smsTask.receivers[0], smsTask.content);
            msgs.add(vo);

            MTPack pack = new MTPack();
            pack.setBatchID(UUID.randomUUID());
            pack.setMsgType(MTPack.MsgType.SMS);
            pack.setSendType(MTPack.SendType.GROUP);
            pack.setUsername(xwUsername);
            pack.setPassword(xwPassword);
            pack.setMsgs(msgs);
            if(smsTask.type != 0){
                pack.setBizCode(String.valueOf(smsTask.type));
            }

            HttpEntity<MTPack> entity = new HttpEntity<MTPack>(pack, headers);
            RestTemplate restTemplate = new RestTemplate();

            List messageConverters = new ArrayList();
            messageConverters.add(new SourceHttpMessageConverter());
            messageConverters.add(new FormHttpMessageConverter());
            messageConverters.add(new MappingJackson2HttpMessageConverter());
            restTemplate.setMessageConverters(messageConverters);
            logger.info(smsTask.receivers[0] + "start time:" + new Date());
            GsmsResponse result = restTemplate.postForObject(sendUrl, entity, GsmsResponse.class);
            logger.info(smsTask.receivers[0] + "end time:" + new Date());
            logger.info("*******sms ID:{},result:{},send success", smsTask.id, result.getResult());
            return String.valueOf(result.getResult());
        } catch (Exception e) {
            logger.error("******sms ID:{},send error", smsTask.id);
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
