package com.fenlibao.p2p.util.pay;

import com.fenlibao.p2p.model.xinwang.config.SysPayConfig;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 */
public class SysPayUtil {

    private static final Logger log = LogManager.getLogger(SysPayUtil.class);

    public static SysPayConfig PAY_CONFIG;

    static {
    	PAY_CONFIG = ConfigFactory.create(SysPayConfig.class);
    }

}
