package com.fenlibao.p2p.model.entity.activity;

import java.sql.Timestamp;


public class AutoRegist {

    private  int id;

    private Integer userId;

    private int registStatus;

    private int cardStatus;

    private String mobile;

    private String name;

    private String idCard;

    private String registLog;

    private String cardLog;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardLog() {
        return cardLog;
    }

    public void setCardLog(String cardLog) {
        this.cardLog = cardLog;
    }

    public int getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(int cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRegistLog() {
        return registLog;
    }

    public void setRegistLog(String registLog) {
        this.registLog = registLog;
    }

    public int getRegistStatus() {
        return registStatus;
    }

    public void setRegistStatus(int registStatus) {
        this.registStatus = registStatus;
    }

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
}
