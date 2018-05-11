package com.fenlibao.p2p.model.xinwang.consts;

import java.math.BigDecimal;

/**
 * trade模块常量
 */
public class TradeConsts {

    private TradeConsts(){}

    /**
     * 投标：借款人是否可投
     */
    public final static boolean BID_SFZJKT = false;

    /**
     * 封标金额
     */
    public final static BigDecimal BID_FBJE = new BigDecimal(100);

    /**
     * 存管标类型 2
     */
    public final static int T6230_F38_DM = 2;

    public final static int TRADE_PWD_WRONG_COUNT_MAX = 5;
}
