package com.fenlibao.p2p.service.sms;

import com.dimeng.framework.message.sms.entity.SmsTask;

/**
 * 玄武短信接口
 * @author Mingway.Xu
 * @date 2016/12/6 13:58
 */
public interface XWSmsService {
    String sendXWSms(SmsTask smsTask, String sendUrl, String xwUsername, String xwPassword);
}
