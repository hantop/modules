package com.fenlibao.p2p.model.dm.config;

import org.aeonbits.owner.Config;

/**
 * 华兴相关配置信息
 * Created by zcai on 2016/10/8.
 */
@Config.Sources("classpath:huaxing.properties")
public interface HXConfig extends Config {
    /**
     * 渠道号
     */
    @Config.Key("channel.code")
    String channelCode();
    /**
     * 商户唯一标识
     */
    @Config.Key("merchant.id")
    String merchantId();
    /**
     * 商户名称
     */
    @Config.Key("merchant.name")
    String merchantName();
    /**
     * Des3Key
     */
    @Config.Key("des3.key")
    String des3Key();
    /**
     * RSA public key
     */
    @Config.Key("rsa.pub.key")
    String rsaPubKey();
    /**
     * RSA private key
     */
    @Config.Key("rsa.pri.key")
    String rsaPriKey();
    /**
     * 商户返回路径前缀（域名）
     */
    @Config.Key("server.domain")
    String serverDomain();
    /**
     * 华兴表单提交路径
     */
    @Config.Key("form.submit.url")
    String formSubmitUrl();

    /**
     * 平台华兴账户
     * @return
     */
    @Key("platform.hx.wlzh")
    String platformHxWlzh();
}
