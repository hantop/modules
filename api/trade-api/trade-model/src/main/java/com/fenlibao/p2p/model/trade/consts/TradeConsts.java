package com.fenlibao.p2p.model.trade.consts;

import java.math.BigDecimal;

/**
 * trade模块常量
 */
public interface TradeConsts {

    /**
     * 投标：借款人是否可投
     */
    boolean BID_SFZJKT = false;

    /**
     * 封标金额
     */
    BigDecimal BID_FBJE = new BigDecimal(100);

    /**
     * 存管标类型 2
     */
    int T6230_F38_DM = 2;
    
    int TRADE_PWD_WRONG_COUNT_MAX = 5;
}
