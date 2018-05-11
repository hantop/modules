package com.fenlibao.service.pms.common.message.privatemessage;

/**
 * 站内信
 * Created by chenzhixuan on 2015/10/23.
 */
public interface PrivateMessageService {

    /**
     * 发送站内信
     * @param userId
     * @param title
     * @param content
     */
    void sendLetter(String userId, String title, String content);

}
