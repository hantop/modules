package com.fenlibao.p2p.model.xinwang.entity.sign;

/**
 * @author zeronx on 2017/12/18 13:34.
 * @version 1.0
 */
public class SignUserInfo extends ElectronicSignature {
    private Integer userId; // 借款用户id
    private Integer borrowerISPerson; // 借款人为个人：0：否、1：是
    private String borrowerIdCard; // 借款人身份证 没有解密 / 借款企业统一注册码
    private String borrowerLinkManIdCard; // 借款企业用户联系人身份证
    private String borrowerLinkMan; // 借款企业用户联系人

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBorrowerISPerson() {
        return borrowerISPerson;
    }

    public void setBorrowerISPerson(Integer borrowerISPerson) {
        this.borrowerISPerson = borrowerISPerson;
    }

    public String getBorrowerIdCard() {
        return borrowerIdCard;
    }

    public void setBorrowerIdCard(String borrowerIdCard) {
        this.borrowerIdCard = borrowerIdCard;
    }

    public String getBorrowerLinkManIdCard() {
        return borrowerLinkManIdCard;
    }

    public void setBorrowerLinkManIdCard(String borrowerLinkManIdCard) {
        this.borrowerLinkManIdCard = borrowerLinkManIdCard;
    }

    public String getBorrowerLinkMan() {
        return borrowerLinkMan;
    }

    public void setBorrowerLinkMan(String borrowerLinkMan) {
        this.borrowerLinkMan = borrowerLinkMan;
    }
}
