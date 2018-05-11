package com.fenlibao.platform.common.config;

/**
 *
 * @author yangzengcai
 * @date 2016年3月1日
 */
@org.aeonbits.owner.Config.Sources("classpath:config.properties")
public interface Config extends org.aeonbits.owner.Config {

    @Key("tp.duozhuan.md5.key")
    String getDuozhuanMD5key();

    @Key("tp.bid.detail")
    String getBidDetail();
    
    @Key("tp.bid.platform_name")
    String getPlatformName();

    @Key("loan.resources.server.path")
    String getLoanResourcesPath();

    @Key("loan.extrainfo.server.path")
    String getLoanExtraInfoPath();

    @Key("loan.extrainfo.filesize")
    String getLoanExtraFileSize();

    @Key("loan.minAmount")
    String getLoanMinAmount();

    @Key("loan.maxAmount")
    String getLoanMaxAmount();

    @Key("loan.serialNum.maxLength")
    String getSerialNumMaxLength();

    @Key("loan.agreementId.maxLength")
    String getAgreementIdMaxLength();

    @Key("member.point.limit")
    String getMemberPointLimit();

    @Key("member.point.type.code")
    String getMiniTypeCode();

    @Key("xinwang.register.url")
    String getXinwangRegisterUrl();
}
