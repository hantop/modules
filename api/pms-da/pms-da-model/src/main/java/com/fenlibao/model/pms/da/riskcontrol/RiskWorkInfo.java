package com.fenlibao.model.pms.da.riskcontrol;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 风控信息-工作情况
 * Created by Administrator on 2017/12/27.
 */
public class RiskWorkInfo {

    /**
     *借款标ID,参考flb.t_consume_bidinfo.id
     */
    private String bidInfoId;
    /**
     *创建时间
     */
    private Date createTime;
    /**
     *单位名称
     */
    private String companyName;
    /**
     *单位电话
     */
    private String companyPhone;
    /**
     *单位地址
     */
    private String companyAddress;
    /**
     *职位
     */
    private String position;
    /**
     *月收入
     */
    private BigDecimal monthlyIncome;

    public String getBidInfoId() {
        return bidInfoId;
    }

    public void setBidInfoId(String bidInfoId) {
        this.bidInfoId = bidInfoId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }
}
