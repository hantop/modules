package com.fenlibao.p2p.service.privateMessage.impl;

import com.dimeng.p2p.S61.enums.T6123_F05;
import com.fenlibao.p2p.dao.PrivateMessageDao;
import com.fenlibao.p2p.model.entity.PrivateMessage;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.global.Status;
import com.fenlibao.p2p.service.privateMessage.PrivateMessageService;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/23.
 */
@Service
public class PrivateMessageServiceImpl implements PrivateMessageService {
    @Resource
    private PrivateMessageDao privateMessageDao;

    @Transactional
    @Override
    public void updateUserMessageStatus(List<Integer> messageIds, int userId, String status, String whereStatus) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("status", status);
        map.put("whereStatus", whereStatus);
        map.put("messageIds", messageIds);
        this.privateMessageDao.updateMessageStatus(map);
    }

    @Override
    public List<PrivateMessage> getUserMessages(String userId, VersionTypeEnum versionTypeEnum, PageBounds pageBounds) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("status", new String[]{Status.YD.name(), Status.WD.name()});
        map.put("versionType", versionTypeEnum == null ? VersionTypeEnum.PT.getIndex() : versionTypeEnum.getIndex());
        return privateMessageDao.getMessagesByUserId(map, pageBounds);
    }

    @Override
    public int getUserMessageCount(String userId, String status) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("status", status);
        return privateMessageDao.getUserMessageCount(params);
    }

    @Transactional
    @Override
    public void sendLetter(String userId, String title, String content) {
        sendLetter(userId, title, content, VersionTypeEnum.PT);
    }

    @Transactional
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
