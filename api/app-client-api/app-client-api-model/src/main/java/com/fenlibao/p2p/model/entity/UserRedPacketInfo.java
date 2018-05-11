package com.fenlibao.p2p.model.entity;

import com.fenlibao.p2p.model.vo.bidinfo.BidTypeVO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 用户相关的返现红包
 * Created by Louis Wang on 2015/10/17.
 */

public class UserRedPacketInfo implements Serializable {

    private Integer id; //自增id
    private Integer hbId;   //红包Id
    private Integer userId; //用户Id
    private String type;    //红包活动类型
    private BigDecimal hbBalance = BigDecimal.ZERO;   //红包金额
    private BigDecimal conditionBalance = BigDecimal.ZERO;  //投资金额数
    private Integer effectDay;// 红包有效天数
    private String status;  //红包状态
    private Timestamp timestamp;    //红包活动截止使用日期
    private String activityCode; //活动编码

    private Integer investDeadline; //投资期限（单位天）
    private List<BidTypeVO> BidTypes; //标类型

    private Integer times;//可分享次数

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHbId() {
        return hbId;
    }

    public void setHbId(Integer hbId) {
        this.hbId = hbId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getHbBalance() {
        return hbBalance;
    }

    public void setHbBalance(BigDecimal hbBalance) {
        this.hbBalance = hbBalance;
    }

    public BigDecimal getConditionBalance() {
        return conditionBalance;
    }

    public void setConditionBalance(BigDecimal conditionBalance) {
        this.conditionBalance = conditionBalance;
    }

    public Integer getEffectDay() {
        return effectDay;
    }

    public void setEffectDay(Integer effectDay) {
        this.effectDay = effectDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getInvestDeadline() {
        return investDeadline;
    }

    public void setInvestDeadline(Integer investDeadline) {
        this.investDeadline = investDeadline;
    }

    public List<BidTypeVO> getBidTypes() {
        return BidTypes;
    }

    public void setBidTypes(List<BidTypeVO> bidTypes) {
        BidTypes = bidTypes;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}
