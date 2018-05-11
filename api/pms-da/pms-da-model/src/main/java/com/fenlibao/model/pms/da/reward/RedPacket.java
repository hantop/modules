package com.fenlibao.model.pms.da.reward;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RedPacket {
    private int id;

    private String activityName;

    private Date timeStart;

    private Date timeEnd;

    private Integer redNumber;

    private BigDecimal useRedLimitmoney;

    @NotNull(message = "返现券金额不能为空")
    @Min(value = 0, message = "返现券金额不能为负数")
    @Max(value = 1000, message = "返现券金额不能超过1000元")
    private BigDecimal redMoney;

    private int redType;

    private Integer effectMonth;

    private String remarks;//返现券来源,实际用途是备注

    @NotNull(message = "返现券有效期不能为空")
    @Min(value = 1, message = "返现券有效期不能小于1天")
    @Max(value = 30, message = "返现券有效期不超过30天")
    private Integer effectDay;

    private String redRechargeStatus;

    private BigDecimal lowRechargePay;

    @NotEmpty(message = "返现券代码不能为空")
    @NotNull(message = "返现券代码不能为空")
    @Length(min = 2, max = 50, message = "返现券代码字符长度在 {min} 到 {max} 之间")
    private String activityCode;

    private Byte redStatus;

    private Integer redSurplusNumber;

    @NotNull(message = "返现券门槛不能为空")
    @Min(value = 0, message = "返现券门槛不能为负数")
    private BigDecimal investMoney;

    private Integer tradeType;

    private Integer grantStatus;//投资状态
    
    //==================增加标的类型限制,投资期限,以及来源(实际是备注)add Lee==============
    @NotNull(message = "标的类型不能为空")
    private List<Integer> bidTypeIds = new ArrayList<Integer>(); //对象映射,来自对应的标的限制,一对多(不存入这个实体类中对应的数据库表)
    
    private Integer investDeadLine; //投资期限
    
    private boolean investDeadLineType;//默认为false,此时表示投资期限不限.反之,按天来计算
    
    private List<String> bidTypeNames = new ArrayList<>();//关联关系,用于页面做显示
    
    private String bidTypeAlias;//返现券的页面限制,不要显示数组中的[]
    
    private boolean granted;//判断一种返现券是否已经发放

    public boolean isGranted() {
		return granted;
	}

	public void setGranted(boolean granted) {
		this.granted = granted;
	}

	public String getBidTypeAlias() {
		return bidTypeAlias;
	}

	public void setBidTypeAlias(String bidTypeAlias) {
		this.bidTypeAlias = bidTypeAlias;
	}
    
	public List<String> getBidTypeNames() {
		return bidTypeNames;
	}

	public void setBidTypeNames(List<String> bidTypeNames) {
		this.bidTypeNames = bidTypeNames;
	}

	public boolean isInvestDeadLineType() {
		return investDeadLineType;
	}

	public void setInvestDeadLineType(boolean investDeadLineType) {
		this.investDeadLineType = investDeadLineType;
	}
    
	public List<Integer> getBidTypeIds() {
		return bidTypeIds;
	}

	public void setBidTypeIds(List<Integer> bidTypeIds) {
		this.bidTypeIds = bidTypeIds;
	}

	public Integer getInvestDeadLine() {
		return  investDeadLine;
	}

	public void setInvestDeadLine(Integer investDeadLine) {
		this.investDeadLine = investDeadLine;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName == null ? null : activityName.trim();
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Integer getRedNumber() {
        return redNumber;
    }

    public void setRedNumber(Integer redNumber) {
        this.redNumber = redNumber;
    }

    public BigDecimal getUseRedLimitmoney() {
        return useRedLimitmoney;
    }

    public void setUseRedLimitmoney(BigDecimal useRedLimitmoney) {
        this.useRedLimitmoney = useRedLimitmoney;
    }

    public BigDecimal getRedMoney() {
        return redMoney;
    }

    public void setRedMoney(BigDecimal redMoney) {
        this.redMoney = redMoney;
    }

    public int getRedType() {
        return redType;
    }

    public void setRedType(int redType) {
        this.redType = redType;
    }

    public Integer getEffectMonth() {
        return effectMonth;
    }

    public void setEffectMonth(Integer effectMonth) {
        this.effectMonth = effectMonth;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public Integer getEffectDay() {
        return effectDay;
    }

    public void setEffectDay(Integer effectDay) {
        this.effectDay = effectDay;
    }

    public String getRedRechargeStatus() {
        return redRechargeStatus;
    }

    public void setRedRechargeStatus(String redRechargeStatus) {
        this.redRechargeStatus = redRechargeStatus == null ? null : redRechargeStatus.trim();
    }

    public BigDecimal getLowRechargePay() {
        return lowRechargePay;
    }

    public void setLowRechargePay(BigDecimal lowRechargePay) {
        this.lowRechargePay = lowRechargePay;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode == null ? null : activityCode.trim();
    }

    public Byte getRedStatus() {
        return redStatus;
    }

    public void setRedStatus(Byte redStatus) {
        this.redStatus = redStatus;
    }

    public Integer getRedSurplusNumber() {
        return redSurplusNumber;
    }

    public void setRedSurplusNumber(Integer redSurplusNumber) {
        this.redSurplusNumber = redSurplusNumber;
    }

    public BigDecimal getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(BigDecimal investMoney) {
        this.investMoney = investMoney;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public Integer getGrantStatus() {
        return grantStatus;
    }

    public void setGrantStatus(Integer grantStatus) {
        this.grantStatus = grantStatus;
    }
}