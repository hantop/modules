package com.fenlibao.p2p.model.entity.activity;

/**
 * 年会--计算iPhone用户中奖概率实体类
 */
public class AnnualMettingDrawRateRecord {
    private String name;// 用户名字
    private String phone;// 手机
    private Double drawRate;// 中奖概率

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

    public Double getDrawRate() {
        return drawRate;
    }

    public void setDrawRate(Double drawRate) {
        this.drawRate = drawRate;
    }
}
