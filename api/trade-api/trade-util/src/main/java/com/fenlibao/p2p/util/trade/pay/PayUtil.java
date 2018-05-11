package com.fenlibao.p2p.util.trade.pay;

import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fenlibao.p2p.model.trade.config.PayConfig;

/**
 */
public class PayUtil {

    private static final Logger log = LogManager.getLogger(PayUtil.class);

    public static PayConfig PAY_CONFIG;

    static {
    	PAY_CONFIG = ConfigFactory.create(PayConfig.class);
    }

}
