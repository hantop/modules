package com.fenlibao.model.pms.da.cs;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 日志信息
 */
public class LogInfo {
    private int userId; // 用户id
    private int conduct; // 行为0:登录 1:退出登录 2:充值 3:提现 4:投资 5:借款 6:债转申请 7购买债权 8:绑定银行卡 9解绑银行卡 10修改登录密码
    private int status; // 操作状态0:失败 1:成功
    private String phoneNum; // 手机号
    private String idcard; // 身份证号
    private String name; // 姓名
    private String ip; // ip
    private String remarks; // 备注
    private Date createTime; // 创建时间
    private BigDecimal price; // 金额
    private  String requestStr;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getConduct() {
        return conduct;
    }

    public void setConduct(int conduct) {
        this.conduct = conduct;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequestStr() {
        return requestStr;
    }

    public void setRequestStr(String requestStr) {
        this.requestStr = requestStr;
    }
}
