package com.fenlibao.model.pms.da.statistics.invest;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 统计管理查询数据
 * Created by Louis Wang on 2015/11/6.
 */

public class InvestInfo implements Serializable {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 手机号
     */
    private String phoneNum;

    /**
     * 注册时间
     */
    private String regtime;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 投资日期
     */
    private String createTime;

    /**
     * 投资金额
     */
    private BigDecimal investMoney;

    /**
     * 累计投资次数
     */
    private Integer investCount;

    /**
     * 累计金额
     */
    private BigDecimal investTotalMoney;

    /**
     * 投资人数
     */
    private Integer totalInvestor;

    /**
     * 总行数
     */
    private Integer totalLine;

    /**
     * 账户余额
     */
    private BigDecimal surplusMoney;

    /**
     * 产品类型
     */
    private String productType;

    /**
     * 标的类型
     */
    private String bidType;

    /**
     * 标的状态
     */
    private String status;
    /**
     * 投资期限
     */
    private String limitTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getRegtime() {
        return regtime;
    }

    public void setRegtime(String regtime) {
        this.regtime = regtime;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(BigDecimal investMoney) {
        this.investMoney = investMoney;
    }

    public Integer getInvestCount() {
        return investCount;
    }

    public void setInvestCount(Integer investCount) {
        this.investCount = investCount;
    }

    public BigDecimal getInvestTotalMoney() {
        return investTotalMoney;
    }

    public void setInvestTotalMoney(BigDecimal investTotalMoney) {
        this.investTotalMoney = investTotalMoney;
    }

    public Integer getTotalInvestor() {
        return totalInvestor;
    }

    public void setTotalInvestor(Integer totalInvestor) {
        this.totalInvestor = totalInvestor;
    }

    public Integer getTotalLine() {
        return totalLine;
    }

    public void setTotalLine(Integer totalLine) {
        this.totalLine = totalLine;
    }

    public BigDecimal getSurplusMoney() {
        return surplusMoney;
    }

    public void setSurplusMoney(BigDecimal surplusMoney) {
        this.surplusMoney = surplusMoney;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(String limitTime) {
        this.limitTime = limitTime;
    }
}