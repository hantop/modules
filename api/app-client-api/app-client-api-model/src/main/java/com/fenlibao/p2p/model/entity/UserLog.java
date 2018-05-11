package com.fenlibao.p2p.model.entity;

import java.math.BigDecimal;

/**
 * Created by chen on 2018/3/2.
 */
public class UserLog {
    private Integer id;

    private Integer userId;

    private String mobile;

    private String idcard;

    private String ip;

    private BigDecimal price;

    private int conduct;//行为0:登录 1:退出登录 2:充值 3:提现 4:投资 5:借款 6:债转申请 7购买债权 8:绑定银行卡 9解绑银行卡 10修改登录密码

    private String remarks;

    private int status;//操作状态0:失败 1:成功

    private String requestStr;//请求报文数据


    public int getConduct() {
        return conduct;
    }

    public void setConduct(int conduct) {
        this.conduct = conduct;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRequestStr() {
        return requestStr;
    }

    public void setRequestStr(String requestStr) {
        this.requestStr = requestStr;
    }
}
