package com.fenlibao.p2p.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 红包活动 VO
 * Created by wangyunjing on 2015/10/16.
 */

public class RedPacketVO implements Serializable{

    /**
     * 自增id，无符号，主键
     */
    public Integer id;

    /**
     * 活动名称
     */
    public String activity_name;

    /**
     * 活动开始时间
     */
    public Timestamp time_start;

    /**
     * 活动结束时间
     */
    public Timestamp time_end;

    /**
     * 红包数量(1-999999)
     */
    public Integer red_number;

    /**
     * 红包金额
     */
    public BigDecimal red_money = BigDecimal.ZERO;

    /**
     * 红包活动类型
     */
    public Integer red_type ;

    /**
     * 红包有效期
     */
    public Integer effect_month;

    /**
     * 备注
     */
    public String remarks;

    /**
     * 红包有效天数
     */
    public Integer effect_day;

    /**
     * 是否首次充值
     */
    public String red_recharge_status;

    /**
     * 充值最低额度
     */
    public BigDecimal low_recharge_pay = BigDecimal.ZERO;

    /**
     * 活动编号：H(年月)(顺序号)
     */
    public String activity_code;

    /**
     * 红包活动状态
     */
    public Integer red_status;

    /**
     * 已领取红包数量(1-999999)
     */
    public Integer red_surplus_number;

    /**
     * 投资倍数每投资金额数就奖励F06对应的红包金额
     */
    public BigDecimal invest_money = BigDecimal.ZERO;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public Timestamp getTime_start() {
        return time_start;
    }

    public void setTime_start(Timestamp time_start) {
        this.time_start = time_start;
    }

    public Timestamp getTime_end() {
        return time_end;
    }

    public void setTime_end(Timestamp time_end) {
        this.time_end = time_end;
    }

    public Integer getRed_number() {
        return red_number;
    }

    public void setRed_number(Integer red_number) {
        this.red_number = red_number;
    }

    public BigDecimal getRed_money() {
        return red_money;
    }

    public void setRed_money(BigDecimal red_money) {
        this.red_money = red_money;
    }

    public Integer getRed_type() {
        return red_type;
    }

    public void setRed_type(Integer red_type) {
        this.red_type = red_type;
    }

    public Integer getEffect_month() {
        return effect_month;
    }

    public void setEffect_month(Integer effect_month) {
        this.effect_month = effect_month;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getEffect_day() {
        return effect_day;
    }

    public void setEffect_day(Integer effect_day) {
        this.effect_day = effect_day;
    }

    public String getRed_recharge_status() {
        return red_recharge_status;
    }

    public void setRed_recharge_status(String red_recharge_status) {
        this.red_recharge_status = red_recharge_status;
    }

    public BigDecimal getLow_recharge_pay() {
        return low_recharge_pay;
    }

    public void setLow_recharge_pay(BigDecimal low_recharge_pay) {
        this.low_recharge_pay = low_recharge_pay;
    }

    public String getActivity_code() {
        return activity_code;
    }

    public void setActivity_code(String activity_code) {
        this.activity_code = activity_code;
    }

    public Integer getRed_status() {
        return red_status;
    }

    public void setRed_status(Integer red_status) {
        this.red_status = red_status;
    }

    public Integer getRed_surplus_number() {
        return red_surplus_number;
    }

    public void setRed_surplus_number(Integer red_surplus_number) {
        this.red_surplus_number = red_surplus_number;
    }

    public BigDecimal getInvest_money() {
        return invest_money;
    }

    public void setInvest_money(BigDecimal invest_money) {
        this.invest_money = invest_money;
    }
}
