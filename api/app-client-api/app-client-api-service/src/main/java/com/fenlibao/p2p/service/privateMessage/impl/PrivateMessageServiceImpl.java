package com.fenlibao.p2p.service.privateMessage.impl;

import com.dimeng.p2p.S61.enums.T6123_F05;
import com.fenlibao.p2p.dao.PrivateMessageDao;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.service.privateMessage.PrivateMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/23.
 */
@Service
public class PrivateMessageServiceImpl implements PrivateMessageService {

    @Resource
    private PrivateMessageDao privateMessageDao;

//    //为了防止站内信发送到错误的版本（普通|存管），现将默认的接口屏蔽，保证每个调用的地方都能重新检查并按需求进行修改
//    @Override
//    public void sendLetter(String userId, String title, String content) {
//        sendLetter(userId, title, content, VersionTypeEnum.PT);
//    }

    @Override
    public void sendLetter(String userId, String title, String content, VersionTypeEnum versionTypeEnum) {
        // 站内信
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("userId", userId);
        messageMap.put("title", title);
        messageMap.put("status", T6123_F05.WD);
        messageMap.put("versionType", versionTypeEnum == null ? VersionTypeEnum.PT.getIndex() : versionTypeEnum.getIndex());

        privateMessageDao.addMessage(messageMap);
        // 获取刚刚插入的站内信ID
        Object letterId = messageMap.get("letterId");
        // 站内信内容
        HashMap<String, Object> messageContent = new HashMap<>();
        messageContent.put("messageId", letterId);
        messageContent.put("content", content);
        privateMessageDao.addMessageContent(messageContent);
    }
}
