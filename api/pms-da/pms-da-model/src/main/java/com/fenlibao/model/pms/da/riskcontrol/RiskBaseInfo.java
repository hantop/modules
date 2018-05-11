package com.fenlibao.model.pms.da.riskcontrol;

import java.util.Date;

/**
 * 风控数据-基本信息
 * Created by Administrator on 2017/12/27.
 */
public class RiskBaseInfo {
    /**
     *借款标ID,参考flb.t_consume_bidinfo.id
     */
    private int bidInfoId;
    /**
     *创建时间
     */
    private Date createTime;
    /**
     *姓名
     */
    private String idName;
    /**
     *身份证号
     */
    private String idNo;
    /**
     *手机号
     */
    private String phoneNum;
    /**
     *性别
     */
    private int sex;
    /**
     * 年龄
     */
    private int age;
    /**
     *婚育情况
     */
    private String marryStatus;
    /**
     *身份证归属地
     */
    private String idLocation;
    /**
     *居住地
     */
    private String address;
    /**
     *QQ号
     */
    private String qq;

    public int getBidInfoId() {
        return bidInfoId;
    }

    public void setBidInfoId(int bidInfoId) {
        this.bidInfoId = bidInfoId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getMarryStatus() {
        return marryStatus;
    }

    public void setMarryStatus(String marryStatus) {
        this.marryStatus = marryStatus;
    }

    public String getIdLocation() {
        return idLocation;
    }

    public void setIdLocation(String idLocation) {
        this.idLocation = idLocation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
