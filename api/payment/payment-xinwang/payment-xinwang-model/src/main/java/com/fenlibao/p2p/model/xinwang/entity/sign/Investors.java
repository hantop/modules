package com.fenlibao.p2p.model.xinwang.entity.sign;

/**
 * @author zeronx on 2017/12/18 11:05.
 * @version 1.0
 */
public class Investors {

    private Integer userId; // 用户id
    private String name; // 用户姓名
    private String idCard; // 身份证已解密
    private String phone; // 手机号
    private String signId; // 上上签文档id

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }
}
