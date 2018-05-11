package com.fenlibao.model.pms.da.channel.form;

import java.math.BigDecimal;

/**
 * 渠道详情
 * Created by chenzhixuan on 2015/11/18.
 */
public class ChannelDetailForm {
    private BigDecimal minimumMoney;// 最低投资金额
    private BigDecimal maximumMoney;// 最高投资金额
    private String channelId;// 渠道ID
    private String startDate;// 开始时间
    private String endDate;// 结束时间

    public BigDecimal getMinimumMoney() {
        return minimumMoney;
    }

    public void setMinimumMoney(BigDecimal minimumMoney) {
        this.minimumMoney = minimumMoney;
    }

    public BigDecimal getMaximumMoney() {
        return maximumMoney;
    }

    public void setMaximumMoney(BigDecimal maximumMoney) {
        this.maximumMoney = maximumMoney;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
