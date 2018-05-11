package com.fenlibao.p2p.model.xinwang.entity.sign;

import java.util.Date;

/**
 * @author zeronx on 2017/12/29 14:42.
 * @version 1.0
 */
public class SignRegUserRecord {

    private Integer id; // 主键
    private Integer userId; // 用户id 6110.f01
    private String phone; // 手机号
    private String email; // 邮箱
    private Integer platform; // 注册平台1：上上签
    private String platformUserId; // 注册平台 用户id
    private String pwd; // 注册平台用户密码
    private Integer caResult; // ca结果0:成功，1:失败
    private String mark; // 备注
    private Date createTime; // 创建时间
    private Date updateTime; // 更新时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getPlatformUserId() {
        return platformUserId;
    }

    public void setPlatformUserId(String platformUserId) {
        this.platformUserId = platformUserId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getCaResult() {
        return caResult;
    }

    public void setCaResult(Integer caResult) {
        this.caResult = caResult;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
