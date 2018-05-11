package com.fenlibao.p2p.model.entity.finacing;

import java.util.Date;

/**
 * 3.2之后的计划投资记录
 *
 * @author Mingway.Xu
 * @date 2017/4/1 9:04
 */
public class PlanFinacingInfo extends InvestInfo {

    /** 计划类型 */
    int type;

    String planStatus;//新计划的发布状态

    int holdStatus;//新计划的持有状态

    double lowRate;//最低年化利率

    double highRate;//最高年化利率

    double bonusRate;//月增幅利率

    Date exitTime;//计划退出成功日期

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getLowRate() {
        return lowRate;
    }

    public void setLowRate(double lowRate) {
        this.lowRate = lowRate;
    }

    public double getHighRate() {
        return highRate;
    }

    public void setHighRate(double highRate) {
        this.highRate = highRate;
    }

    public double getBonusRate() {
        return bonusRate;
    }

    public void setBonusRate(double bonusRate) {
        this.bonusRate = bonusRate;
    }

    public Date getExitTime() {
        return exitTime;
    }

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public int getHoldStatus() {
        return holdStatus;
    }

    public void setHoldStatus(int holdStatus) {
        this.holdStatus = holdStatus;
    }
}
