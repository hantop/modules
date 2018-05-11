package com.fenlibao.model.pms.da.cs.form;

import com.fenlibao.model.pms.da.cs.account.Transaction;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Bogle on 2015/12/22.
 */
public class TransactionForm extends Transaction {

    private static final long serialVersionUID = -1079207183674933955L;

    private Integer userId;
    /**
     * 用户手机号
     */
    /*@JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = "(13|14|15|17|18)[0-9]{9}", message = "请正确输入手机号")*/
    private String phoneNum; // s61.t6101.F05

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private Date endTime;

    private String awardType;   //奖励类型

    private String cashBackStatus; //返现金状态

    private BigDecimal minInvestMoney ; //起始金额

    private BigDecimal maxInvestMoney ; //结束金额
    
    private String idCard; //身份证号  
    
    private String name;//姓名 
    
    private Integer sold;//买入卖出行为

    private String title;//标的名称

    public Integer getUserId() {
        return userId;
    }

    public Integer getSold() {
		return sold;
	}

	public void setSold(Integer sold) {
		this.sold = sold;
	}

	public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAwardType() {
        return awardType;
    }

    public void setAwardType(String awardType) {
        this.awardType = awardType;
    }

    public String getCashBackStatus() {
        return cashBackStatus;
    }

    public void setCashBackStatus(String cashBackStatus) {
        this.cashBackStatus = cashBackStatus;
    }

    public BigDecimal getMinInvestMoney() {
        return minInvestMoney;
    }

    public void setMinInvestMoney(BigDecimal minInvestMoney) {
        this.minInvestMoney = minInvestMoney;
    }

    public BigDecimal getMaxInvestMoney() {
        return maxInvestMoney;
    }

    public void setMaxInvestMoney(BigDecimal maxInvestMoney) {
        this.maxInvestMoney = maxInvestMoney;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
