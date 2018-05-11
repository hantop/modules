package com.fenlibao.service.pms.common.message.sms;

/**
 * 短信
 * Created by chenzhixuan on 2015/12/15.
 */
public interface SmsService {
    /**
     * 发送短信
     *
     * @param phoneNum
     * @param content
     */
    void sendMsg(String phoneNum, String content);
}
