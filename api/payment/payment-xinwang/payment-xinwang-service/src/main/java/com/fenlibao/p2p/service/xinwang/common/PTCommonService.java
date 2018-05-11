package com.fenlibao.p2p.service.xinwang.common;

/**
 * Created by Administrator on 2017/5/22.
 */
public interface PTCommonService {
    /**
     * 发站内信
     * @param userId
     * @param title
     * @param content
     * @throws Exception
     */
    void sendLetter(int userId, String title,String content) throws Exception;

    /**
     * 发短信
     * @param mobile
     * @param content
     * @throws Exception
     */
    void sendMsg(String mobile,String content,int type) throws Exception;
}
