package com.fenlibao.p2p.service.privateMessage;

import com.fenlibao.p2p.model.enums.VersionTypeEnum;

/**
 * 站内信
 * Created by chenzhixuan on 2015/10/23.
 */
public interface PrivateMessageService {

//    /**
//     * 为了防止站内信发送到错误的版本（普通|存管），现将默认的接口屏蔽，保证每个调用的地方都能重新检查并按需求进行修改
//     * 发送站内信(普通版本)
//     *
//     * @param userId
//     * @param title
//     * @param content
//     */
//    void sendLetter(String userId, String title, String content);

    /**
     * 发送站内信
     *
     * @param userId
     * @param title
     * @param content
     */
    void sendLetter(String userId, String title, String content, VersionTypeEnum versionTypeEnum);
}
