package com.fenlibao.p2p.model.xinwang.consts;

import java.math.BigDecimal;

/**
 * 新网静态常量
 */
public class XinwangConsts {
    private XinwangConsts(){}
    public static final Integer MAX_REQUESTS_PER_BATCH=3000;
    public static final int DEFAULT_USER_AUTH_YEAR = 30;
    public static final BigDecimal DEFAULT_USER_AUTH_MONEY = new BigDecimal(10000000);
}
