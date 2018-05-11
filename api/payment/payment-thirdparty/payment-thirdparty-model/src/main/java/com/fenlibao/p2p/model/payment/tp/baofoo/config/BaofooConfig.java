package com.fenlibao.p2p.model.payment.tp.baofoo.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:baofoo.properties")
public interface BaofooConfig extends Config{

	/**
	 * 是否为宝付测试
	 */
    @Config.Key("is_test")
    boolean isTest();
	
	/**
	 * 认证支付版本号
	 */
    @Config.Key("version_rzzf")
    String versionRZZF();
    
    /**
     * 代付版本号
     */
    @Config.Key("version_df")
    String versionDF();
	
	/**
	 * 终端号
	 */
    @Config.Key("terminal_id_rzzf")
    String terminalIdRZZF();
    
    /**
     * 终端号
     */
    @Config.Key("terminal_id_df")
    String terminalIdDF();
	
	/**
	 * 商户号
	 */
    @Config.Key("member_id_rzzf")
    String memberIdRZZF();
    
    /**
     * 商户号
     */
    @Config.Key("member_id_df")
    String memberIdDF();
	
	/**
	 * pfx私钥密码
	 */
    @Config.Key("pfx_pwd_rzzf")
    String pfxPwdRZZF();
    
    /**
     * pfx私钥密码
     */
    @Config.Key("pfx_pwd_df")
    String pfxPwdDF();
	
	/**
	 * 认证支付请求url
	 */
    @Config.Key("request_url_rzzf")
    String requestUrlRzzf();
	
	/**
	 * 代付请求url
	 */
    @Config.Key("request_url_df")
    String requestUrlDf();

    /**
     * 公钥
     * @return
     */
	@Key("rsa_pub_key_path_rzzf")
	String rsaPubKeyPathRZZF();
	/**
	 * 私钥
	 * @return
     */
	@Key("rsa_pri_key_path_rzzf")
	String rsaPriPeyPathRZZF();
	/**
	 * 公钥
	 * @return
     */
	@Key("rsa_pri_key_path_df")
	String rsaPriPeyPathDF();
	/**
	 * 私钥
	 * @return
     */
	@Key("rsa_pub_key_path_df")
	String rsaPubPeyPathDF();

}
