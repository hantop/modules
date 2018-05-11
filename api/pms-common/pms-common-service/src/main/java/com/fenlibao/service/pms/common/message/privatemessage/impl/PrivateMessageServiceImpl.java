package com.fenlibao.service.pms.common.message.privatemessage.impl;

import com.fenlibao.dao.pms.common.message.privatemessage.PrivateMessageMapper;
import com.fenlibao.model.pms.common.global.PrivatemessageStatusEnum;
import com.fenlibao.service.pms.common.message.privatemessage.PrivateMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 站内信
 * Created by chenzhixuan on 2015/10/23.
 */
@Service
public class PrivateMessageServiceImpl implements PrivateMessageService {

    @Resource
    private PrivateMessageMapper privateMessageMapper;

    @Override
    public void sendLetter(String userId, String title, String content) {
        // 站内信
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("userId", userId);
        messageMap.put("title", title);
        messageMap.put("status", PrivatemessageStatusEnum.WD);
        privateMessageMapper.addMessage(messageMap);
        // 获取刚刚插入的站内信ID
        Object letterId = messageMap.get("letterId");
        // 站内信内容
        HashMap<String, Object> messageContent = new HashMap<>();
        messageContent.put("messageId", letterId);
        messageContent.put("content", content);
        privateMessageMapper.addMessageContent(messageContent);
    }
}
