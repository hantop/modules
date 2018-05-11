package com.fenlibao.model.pms.da.statistics.invest.form;

import com.fenlibao.model.pms.da.statistics.invest.Register;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Bogle on 2015/12/30.
 */
public class RegisterForm extends Register {

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private Date endTime;

    private boolean def;

    private String isAuth;// 身份证号
    private String isBankCar;//是否绑卡

    private String isInvestMoney ;  //是否有投资

    private BigDecimal minInvestMoney ; //起始金额
    private BigDecimal maxInvestMoney ; //结束金额

    BigDecimal investMoneyTotal = new BigDecimal(0);
    BigDecimal investSumTotal = new BigDecimal(0);
    
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

    public Boolean getDef() {
        return def;
    }

    public void setDef(Boolean def) {
        this.def = def;
    }

    public String getIsInvestMoney() {
        return isInvestMoney;
    }

    public void setIsInvestMoney(String isInvestMoney) {
        this.isInvestMoney = isInvestMoney;
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

    public String getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(String isAuth) {
        this.isAuth = isAuth;
    }

    public String getIsBankCar() {
        return isBankCar;
    }

    public void setIsBankCar(String isBankCar) {
        this.isBankCar = isBankCar;
    }

    public BigDecimal getInvestMoneyTotal() {
        return investMoneyTotal;
    }

    public void setInvestMoneyTotal(BigDecimal investMoneyTotal) {
        this.investMoneyTotal = investMoneyTotal;
    }

    public BigDecimal getInvestSumTotal() {
        return investSumTotal;
    }

    public void setInvestSumTotal(BigDecimal investSumTotal) {
        this.investSumTotal = investSumTotal;
    }
}
