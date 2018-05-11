package com.fenlibao.p2p.util.payment.tp.baofoo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.model.payment.tp.baofoo.config.BaofooConfig;
import com.fenlibao.p2p.model.payment.tp.baofoo.enums.TxnSubType;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.util.api.encrypt.Base64Decoder;
import com.fenlibao.p2p.util.api.encrypt.Base64Encoder;
import com.fenlibao.p2p.util.api.http.HttpClientUtil;
import com.fenlibao.p2p.util.api.http.defines.HttpResult;
import com.fenlibao.p2p.util.payment.tp.baofoo.loader.CerLoader;
import com.fenlibao.p2p.util.payment.tp.baofoo.loader.PfxLoader;
import com.fenlibao.p2p.util.payment.tp.baofoo.rsa.RsaCodingUtil;
import com.fenlibao.p2p.util.payment.tp.baofoo.rsa.RsaReadUtil;
import com.fenlibao.p2p.util.trade.order.OrderUtil;
import org.aeonbits.owner.ConfigFactory;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zcai on 2017/2/11.
 */
public class BaoFooUtil {

    private static final Logger log = LogManager.getLogger(BaoFooUtil.class);

    public static BaofooConfig CONFIG;

    static {
        CONFIG = ConfigFactory.create(BaofooConfig.class);
    }

    public static Map<String, Object> getBaseParams(TxnSubType tradeSubType) {
        Map<String, Object> params = new HashMap<>(10);
        params.put("txn_sub_type", tradeSubType.getCode());//交易子类
        params.put("biz_type", "0000");//接入类型
        params.put("terminal_id", BaoFooUtil.CONFIG.terminalIdRZZF());//终端号
        params.put("member_id", BaoFooUtil.CONFIG.memberIdRZZF());//商户号
        params.put("trans_serial_no", OrderUtil.getFlowNo());//商户流水号
        return params;
    }

    /**
     * 获取请求参数json
     * @param tradeSubType 交易子类型
     * @param dataContent  加密数据域
     * @return
     */
    public static Map<String, Object> getReqParams(TxnSubType tradeSubType, Map<String, Object> dataContent) {
        Map<String, Object> params = new HashMap<>(7);
        String dataContentStr = JSONObject.toJSONString(dataContent);
        String encryptData = RsaCodingUtil.encryptByPriPfxStream(Base64Encoder.encode(dataContentStr), PfxLoader.getPrivateKeyRZZF(), CONFIG.pfxPwdRZZF());
        params.put("version", CONFIG.versionRZZF());//版本号
        params.put("terminal_id", CONFIG.terminalIdRZZF());//终端号
        params.put("txn_type", "0431");//交易类型
        params.put("txn_sub_type", tradeSubType.getCode());//交易子类型
        params.put("member_id", CONFIG.memberIdRZZF());//商户号
        params.put("data_type", "json");//数据类型
        params.put("data_content", encryptData);//加密数据
        return params;
    }
   
    /**
     * 认证支付发送请求
     * @param requestParam
     * @return
     * @throws Exception
     */
	public static Map<String,String> sendRequest(Map<String,Object> requestParam) throws Exception{
        JSONObject requestParamJsonObject=new JSONObject(requestParam);
        String requestUrl= BaoFooUtil.CONFIG.requestUrlRzzf();
        log.info("宝付充值发送参数requestUrl=" + requestUrl + "requestParamJsonObject =" + requestParamJsonObject);
        HttpResult httpResult=HttpClientUtil.httpsMapPost(requestUrl, requestParamJsonObject);
        log.info("宝付充值返回参数httpResult=" + httpResult);
        if(httpResult==null||httpResult.getStatusCode()!= HttpStatus.SC_OK){
        	throw new TradeException(TradeResponseCode.PAYMENT_REQUEST_FAIL.getCode(),"请求发送失败");
        }
        PublicKey publicKey= RsaReadUtil.getPublicKeyByText(CerLoader.getPublicKeyRZZF());
        String rsaDecryptedHttpResult=RsaCodingUtil.decryptByPublicKey(new String(httpResult.getBytes()), publicKey);
		if(rsaDecryptedHttpResult.isEmpty()){//判断解密是否正确。如果为空则宝付公钥不正确
			throw new TradeException(TradeResponseCode.PAYMENT_PUBLICKEY_WRONG);
		}
		String httpResultJson = Base64Decoder.decode(rsaDecryptedHttpResult);
		Map<String,String> httpResultMap=JSON.parseObject(httpResultJson, HashMap.class);
		return httpResultMap;
	}

}
