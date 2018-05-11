package com.fenlibao.platform.common.config;

import org.aeonbits.owner.Config;

/**
 * 
 * @author yangzengcai
 * @date 2016年3月1日
 */
@Config.Sources("classpath:message.properties")
public interface MessageConfig extends Config {

    @Config.Key("sms.register.mimiso.fresh")
    String getRegisterFresh(String phoneNum, String pwd);

    @Config.Key("sms.register.mimiso.old")
    String getRegisterOld();

}
