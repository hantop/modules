package com.fenlibao.p2p.model.entity.activity;

import java.math.BigDecimal;

/**
 * 2017年会用户中奖名单实体
 * Created by xiao on 2016/12/23.
 */
public class AnnualMettingRecord {
    private String name;// 用户名字
    private String phone;// 手机
    private String type;//   奖品类型1.红米；2.返现红包；3.iphone7
    private String prizeCode;// 奖品编码
    private String realFlag;//  0假数据1真实数据

    private BigDecimal amout;//额外增加给前端展示的金额数
    private String prizeName;//额外增加给前端展示的奖品名称

    public AnnualMettingRecord() {
    }

    public AnnualMettingRecord(String name, String phone, String type, String prizeCode, String realFlag) {
        this.name = name;
        this.phone = phone;
        this.type = type;
        this.prizeCode = prizeCode;
        this.realFlag = realFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrizeCode() {
        return prizeCode;
    }

    public void setPrizeCode(String prizeCode) {
        this.prizeCode = prizeCode;
    }

    public String getRealFlag() {
        return realFlag;
    }

    public void setRealFlag(String realFlag) {
        this.realFlag = realFlag;
    }

    public BigDecimal getAmout() {
        return amout;
    }

    public void setAmout(BigDecimal amout) {
        this.amout = amout;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }
}
