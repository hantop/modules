package com.fenlibao.p2p.model.business.sms.xuanwu;

/**
 * 短信信息
 */
public class MessageData {

	private String phone;//手机号码
    private String content;//短信内容
    private String customMsgID;//用户自定义消息ID
    private String customNum;//用户扩展码
    private boolean vipFlag = false;//是否VIP号码(默认：false)

    public MessageData() {
    }

    public MessageData(String phone, String content) {
        this.setPhone(phone);
        this.setContent(content);
    }

    public MessageData(String phone, String content, String customMsgID) {
        this.setPhone(phone);
        this.setContent(content);
        this.customMsgID = customMsgID;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        if(phone == null) {
            this.phone = "";
        } else {
            this.phone = phone;
        }

    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        if(content == null) {
            this.content = "";
        } else {
            this.content = content;
        }

    }

    public String getCustomMsgID() {
        return this.customMsgID;
    }

    public void setCustomMsgID(String customMsgID) {
        this.customMsgID = customMsgID;
    }

    public String getCustomNum() {
        return this.customNum;
    }

    public void setCustomNum(String customNum) {
        this.customNum = customNum;
    }

    public boolean isVipFlag() {
        return this.vipFlag;
    }

    public void setVipFlag(boolean vipFlag) {
        this.vipFlag = vipFlag;
    }

}
