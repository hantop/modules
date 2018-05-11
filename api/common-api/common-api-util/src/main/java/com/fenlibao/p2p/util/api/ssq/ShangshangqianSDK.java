package com.fenlibao.p2p.util.api.ssq;

import cn.bestsign.sdk.BestSignSDK;
import com.fenlibao.p2p.util.api.load.ApiUtilConfig;

/**
 * 上上签sdk
 * create by zeronx 2017-12-20 23:48
 */
public enum ShangshangqianSDK {
    INSTANCE;
    private boolean develop_mode= Boolean.parseBoolean(ApiUtilConfig.get("ssq.develop.mode"));//true开发模式,false生产模式

    private BestSignSDK sdk;

    //测试环境参数
    private  final String mid_test = ApiUtilConfig.get("ssq.sign.mid_test");
    private  final String pem_test =ApiUtilConfig.get("ssq.sign.pem_test");
    private String host_test = ApiUtilConfig.get("ssq.sign.host_test");

    //正式环境参数
    private  final String mid = ApiUtilConfig.get("ssq.sign.mid");
    private  final String pem = ApiUtilConfig.get("ssq.sign.pem");
    private String host = ApiUtilConfig.get("ssq.sign.host");

    ShangshangqianSDK() {
        if(develop_mode){
            sdk = BestSignSDK.getInstance(mid_test, pem_test, host_test);
            sdk.setEnvCharset("UTF-8");
        }else{
            sdk = BestSignSDK.getInstance(mid, pem, host);
            sdk.setEnvCharset("UTF-8");
        }
    }

    public BestSignSDK getInstance() {
            return sdk;
    }
}
