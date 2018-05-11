package com.fenlibao.p2p.service.privateMessage;

import com.fenlibao.p2p.model.entity.PrivateMessage;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

import java.util.List;

/**
 * 站内信
 * Created by chenzhixuan on 2015/10/23.
 */
public interface PrivateMessageService {

    /**
     * 修改用户消息状态
     *  @param messageIds 多个ID用英文逗号拼接
     * @param userId     用户ID
     * @param status     状态：WD:未读;YD:已读;SC:删除
     * @param whereStatus
     */
    void updateUserMessageStatus(List<Integer> messageIds, int userId, String status, String whereStatus);

    /**
     * 获取用户的消息
     *
     * @param userId
     * @param pageBounds
     * @return
     */
    List<PrivateMessage> getUserMessages(String userId, VersionTypeEnum versionTypeEnum, PageBounds pageBounds);

    /**
     * 获取用户消息数量
     *
     * @param userId
     * @return
     */
    int getUserMessageCount(String userId, String status);

    /**
     * 发送站内信
     *
     * @param userId
     * @param title
     * @param content
     */
    void sendLetter(String userId, String title, String content);

    /**
     * 发送站内信
     *
     * @param userId
     * @param title
     * @param content
     */
    void sendLetter(String userId, String title, String content, VersionTypeEnum versionTypeEnum);
}
