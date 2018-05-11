package com.fenlibao.p2p.model.xinwang.config;
import org.aeonbits.owner.Config;
/**
 * Created by Administrator on 2017/5/4.
 */
@Config.Sources("classpath:xinwang.properties")
public interface XinWangConfig extends Config{
    /**
     * 新网接口地址
     */
    @Config.Key("url")
    String url();

    /**
     * 商户编号
     */
    @Config.Key("platformNo")
    String platformNo();

    /**
     * 证书序号
     */
    @Config.Key("keySerial")
    String keySerial();

    /**
     * 商户私钥
     */
    @Config.Key("privateKey")
    String privateKey();

    /**
     * 存管公钥
     */
    @Config.Key("lmPublicKey")
    String lmPublicKey();

    /**
     * 页面回跳ip
     * @return
     */
    @Config.Key("redirectIP")
    String redirectIP();

    /**
     * 对账文件保存路径
     * @return
     */
    @Config.Key("checkFileSavePath")
    String checkFileSavePath();

    /**
     * 平台总账户
     * @return
     */
    @Config.Key("generalAccount")
    String generalAccount();
    /**
     * 代偿金账户
     * @return
     */
    @Config.Key("compensatoryAccount")
    String compensatoryAccount();
    /**
     * 营销款账户
     * @return
     */
    @Config.Key("marketingAccount")
    String marketingAccount();
    /**
     * 分润账户
     * @return
     */
    @Config.Key("shareAccount")
    String shareAccount();
    /**
     * 收入账户
     * @return
     */
    @Config.Key("incomeAccount")
    String incomeAccount();
    /**
     * 派息账户
     * @return
     */
    @Config.Key("dividendAccount")
    String dividendAccount();
    /**
     * 代充值账户
     * @return
     */
    @Config.Key("replacementRechargeAccount")
    String replacementRechargeAccount();

    /**
     * 平台垫资账户
     * @return
     */
    @Config.Key("underwrittenAmount")
    String underwrittenAmount();

    /**
     * 新网的账户名称
     * @return
     */
    @Config.Key("xinwangPlatformAccount")
    String xinwangPlatformAccount();

    /**
     * 支付公司区分正式环境和测试环境
     * @return
     */
    @Config.Key("payCompanyChannel")
    String payCompanyChannel();
}
