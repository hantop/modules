package com.fenlibao.p2p.model.trade.config;

import org.aeonbits.owner.Config;

import java.math.BigDecimal;

@Config.Sources("classpath:payment.properties")
public interface PayConfig extends Config{

	/**
	 * 提现固定手续费1元
	 */
    @Config.Key("WITHDRAW_POUNDAGE_1_RMB")
    String WITHDRAW_POUNDAGE_1_RMB();

	/**
	 * 提现手续费: 扣除方式(true:内扣，从提现金额里面扣，false：外扣，提现金额+提现手续费)
	 */
    @Config.Key("TXSXF_KCFS")
    String TXSXF_KCFS();
    
    /**
     * 提现: 最低提取金额（单位：元）
     */
    @Config.Key("WITHDRAW_MIN_FUNDS")
    String WITHDRAW_MIN_FUNDS();
    
    /**
     * 提现: 最高提取金额（单位：元）
     */
    @Config.Key("WITHDRAW_MAX_FUNDS")
    String WITHDRAW_MAX_FUNDS();
    
    /**
     * 线下充值时间间隔（单位：小时）
     */
    @Config.Key("WITHDRAW_OFFLINE_INTERVAL")
    String WITHDRAW_OFFLINE_INTERVAL();

    /**
     * 充值最低金额(元)
     * @return
     */
    @Key("CHARGE_MIN_AMOUNT")
    BigDecimal CHARGE_MIN_AMOUNT();

    /**
     * 支付通道
     * @return
     */
    @Key("TPPAYMENT_CHANNEL_CODE")
    String TPPAYMENT_CHANNEL_CODE();

    @Config.Key("SEND_BORROWER_PREPAY_SMS")
    @DefaultValue("true")
    Boolean SEND_BORROWER_PREPAY_SMS();

    @Config.Key("SEND_BORROWER_REPAY_SMS")
    @DefaultValue("true")
    Boolean SEND_BORROWER_REPAY_SMS();
}
