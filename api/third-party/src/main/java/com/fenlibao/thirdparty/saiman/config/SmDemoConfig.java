package com.fenlibao.thirdparty.saiman.config;

/**
 * 必奢规则调用配置Demo, Demo中的默认配置都无法调用成功,请根据注释进行对应配置修改.
 *
 * @author hush@udcredit.com
 * @version 1.0
 * @date 10/29/15
 */
public class SmDemoConfig {

    /**
     * 必奢服务调用地址
     */
    public static final String SERVICE_URL = "http://idcard.bishe.com/check.php";

    /**
     * 开发私钥Demo
     */
    public static final String SECRET_KEY = "a2eb9fded78d733fb8f0001d0c1877d3";

    /**
     * 商户号,请根据开户邮件通知进行修改.
     */
    public static final String PARTNER_CODE = "20160301001";


    private SmDemoConfig() {}
}
