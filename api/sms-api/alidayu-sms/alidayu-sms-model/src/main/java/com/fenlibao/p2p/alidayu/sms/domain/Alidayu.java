package com.fenlibao.p2p.alidayu.sms.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bogle on 2016/2/25.
 */
public class Alidayu implements Serializable {
    private static final long serialVersionUID = 4225667596502470493L;

    private Integer id;

    private String bizName;

    private String bizCode;

    private String templateCode;

    private String signName;

    private List<AlidayuItem> alidayuItems;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Alidayu(String templateCode, String signName, List<AlidayuItem> alidayuItems) {
        this.templateCode = templateCode;
        this.signName = signName;
        this.alidayuItems = alidayuItems;
    }

    public Alidayu() {
    }

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public List<AlidayuItem> getAlidayuItems() {
        return alidayuItems;
    }

    public void setAlidayuItems(List<AlidayuItem> alidayuItems) {
        this.alidayuItems = alidayuItems;
    }
}
