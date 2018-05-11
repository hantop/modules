package com.fenlibao.p2p.model.vo.sms;

/**
 * 发送短信任务 com.dimeng.framework.message.sms.entity.SmsTask
 * @author Mingway.Xu
 * @date 2016/12/6 14:11
 */
public class SmsTaskVO {
    int id;
    int type;
    String content;
    String receivers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }
}
