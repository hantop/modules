package com.fenlibao.p2p.controller.noversion.pay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.util.parser.BooleanParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenlibao.lianpay.share.security.Md5Algorithm;
import com.fenlibao.lianpay.yintong.PaymentInfo;
import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.model.entity.pay.RechargeOrder;
import com.fenlibao.p2p.model.entity.pay.ThirdPartyAgreement;
import com.fenlibao.p2p.service.bank.BankService;
import com.fenlibao.p2p.service.recharge.IRechargeMangeService;
import com.fenlibao.p2p.service.recharge.IRechargeOrderService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.StringHelper;
import com.fenlibao.p2p.util.loader.Payment;
import com.fenlibao.p2p.util.pay.OrderUtil;
import com.fenlibao.p2p.util.pay.RSAUtil;

public abstract class AbstractLianLianPayController {
	
	protected static final Logger logger = LogManager.getLogger(AbstractLianLianPayController.class);

	@Resource
	private IRechargeMangeService rechargeMangeService;
	@Resource
	private IRechargeOrderService rechargeOrderService;
	@Resource
	protected BankService bankService;
	@Resource
	protected ITradeService tradeService;
	
	protected void setPayFormPropertiesByUserId(Map<String, String> params, int userId) throws Throwable {
		String cardNo = bankService.getCardNo(userId);
		if (StringUtils.isNotBlank(cardNo)) {
			params.put("card_no", cardNo); //银行卡号
		}
		ThirdPartyAgreement agreement = tradeService.getNoAgree(userId);
		if (StringUtils.isNotBlank(agreement.getLianlianAgreement())) {
			params.put("no_agree", agreement.getLianlianAgreement()); //协议号
		}
	}


	/**
	 * 充值成功后，保存数据
	 * 
	 * @param TransID
	 *            充值订单 由getTransID 生成的
	 * @param serviceSession
	 * @throws Throwable
	 */
	protected int sucessSave(String TransID, Map<String, String> params)
			throws Throwable {
		String amount = params.get("money_order"); //返回的充值金额
		int BillNo = OrderUtil.getOrderIdByNoOrder(TransID);//TransID.split("_")[1];
		RechargeOrder order = rechargeMangeService.getChargeOrder(BillNo);
		if (order != null && order.status != T6501_F03.CG) {
			Map<String, String> param = new HashMap<>();
			param.put("paymentOrderId", TransID);
			param.put(
					"PAY_RATE", Payment.get(Payment.CHARGE_RATE_LIANLIAN)); //getResourceProvider().getResource(ConfigureProvider.class).getProperty(LianLianPayVariable.CHARGE_RATE_LIANLIAN)
			param.put("amount", amount);
			rechargeOrderService.confirmForPay(order.id, param);
			
			return order.userId; //返回userId用于保存其他信息
		}
		return -1;
	}
	
	/**
	 * 返回的是一个json字符串 转换成一个map<String,String>
	 * 
	 * @param request
	 * @return map<String,String>
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, String> readReqStr(InputStream is) throws Throwable {
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			logger.error(e, e);
		} finally {
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (IOException e) {

			}
		}
		ObjectMapper objm = new ObjectMapper();
		Map<String, String> params = objm.readValue(sb.toString(), Map.class);
		return params;
	}

	
	/**
	 * 对充值参数进行签名和封装转成json格式(用于wap)
	 * @param params
	 * @return "req_data"
	 * @throws UnsupportedEncodingException
	 */
	public String getPayFormJsonForWAP(Map<String, String> params) throws UnsupportedEncodingException {
		String req_data = null;
		StringBuffer strBuf = new StringBuffer();
		if (!StringHelper.isNull(params.get("acct_name")))
	    {
	        strBuf.append("acct_name=");
	        strBuf.append(params.get("acct_name"));
        	strBuf.append("&app_request=3"); //wap,sdk不需要
	    } else
	    {
        	strBuf.append("app_request=3"); //wap,sdk不需要
	    }
	    if (!StringHelper.isNull(params.get("bg_color")))
	    {
	        strBuf.append("&bg_color=");
	        strBuf.append(params.get("bg_color"));
	    }
	    strBuf.append("&busi_partner=");
	    strBuf.append(params.get("busi_partner"));
    	if (!StringHelper.isNull(params.get("card_no")))
    	{
    		strBuf.append("&card_no=");
    		strBuf.append(params.get("card_no"));
    	}
	    strBuf.append("&dt_order=");
	    strBuf.append(params.get("dt_order"));
	    if (!StringHelper.isNull(params.get("id_no")))
	    {
	        strBuf.append("&id_no=");
	        strBuf.append(params.get("id_no"));
	    }
	    if (!StringHelper.isNull(params.get("info_order")))
	    {
	        strBuf.append("&info_order=");
	        strBuf.append(params.get("info_order"));
	    }
	    strBuf.append("&money_order=");
	    strBuf.append(params.get("money_order"));
	    if (!StringHelper.isNull(params.get("name_goods")))
	    {
	        strBuf.append("&name_goods=");
	        strBuf.append(params.get("name_goods"));
	    }
	    if (!StringHelper.isNull(params.get("no_agree")))
	    {
	        strBuf.append("&no_agree=");
	        strBuf.append(params.get("no_agree"));
	    }
	    strBuf.append("&no_order=");
	    strBuf.append(params.get("no_order"));
	    strBuf.append("&notify_url=");
	    strBuf.append(params.get("notify_url"));
	    strBuf.append("&oid_partner=");
	    strBuf.append(params.get("oid_partner"));
	    if (!StringHelper.isNull(params.get("risk_item")))
	    {
	        strBuf.append("&risk_item=");
	        strBuf.append(params.get("risk_item"));
	    }
	    strBuf.append("&sign_type=");
	    strBuf.append(params.get("sign_type"));
	    if (!StringHelper.isNull(params.get("url_return")))
	    {
	        strBuf.append("&url_return=");
	        strBuf.append(params.get("url_return"));
	    }
	    if (!StringHelper.isNull(params.get("user_id")))
	    {
	        strBuf.append("&user_id=");
	        strBuf.append(params.get("user_id"));
	    }
	    if (!StringHelper.isNull(params.get("valid_order")))
	    {
	        strBuf.append("&valid_order=");
	        strBuf.append(params.get("valid_order"));
	    }
	    
	    String sign = getSign(params, strBuf);
	    req_data = setPaymentInfo(params, sign);
	    strBuf = null;
	    return req_data;
	}
	
	/**对充值参数进行签名和封装转成json格式（用于sdk）
	 * （分开调用，避免过多的判断）
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getPayFormJsonForSDK(Map<String, String> params) throws UnsupportedEncodingException {
		String req_data = null;
		StringBuffer strBuf = new StringBuffer();
	    strBuf.append("&busi_partner=");
	    strBuf.append(params.get("busi_partner"));
	    strBuf.append("&dt_order=");
	    strBuf.append(params.get("dt_order"));
	    if (!StringHelper.isNull(params.get("info_order")))
	    {
	        strBuf.append("&info_order=");
	        strBuf.append(params.get("info_order"));
	    }
	    strBuf.append("&money_order=");
	    strBuf.append(params.get("money_order"));
	    if (!StringHelper.isNull(params.get("name_goods")))
	    {
	        strBuf.append("&name_goods=");
	        strBuf.append(params.get("name_goods"));
	    }
	    strBuf.append("&no_order=");
	    strBuf.append(params.get("no_order"));
	    strBuf.append("&notify_url=");
	    strBuf.append(params.get("notify_url"));
	    strBuf.append("&oid_partner=");
	    strBuf.append(params.get("oid_partner"));
	    if (!StringHelper.isNull(params.get("risk_item")))
	    {
	        strBuf.append("&risk_item=");
	        strBuf.append(params.get("risk_item"));
	    }
	    strBuf.append("&sign_type=");
	    strBuf.append(params.get("sign_type"));
	    if (!StringHelper.isNull(params.get("valid_order")))
	    {
	        strBuf.append("&valid_order=");
	        strBuf.append(params.get("valid_order"));
	    }
	    
	    String sign = getSign(params, strBuf);
	    req_data = setPaymentInfo(params, sign);
	    strBuf = null;
	    return req_data;
	}


	/**
	 * 获取签名
	 * @param params
	 * @param strBuf
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String getSign(Map<String, String> params, StringBuffer strBuf)
			throws UnsupportedEncodingException {
		String sign_src = strBuf.toString();
	    if (sign_src.startsWith("&"))
	    {
	        sign_src = sign_src.substring(1);
	    }
	    String sign = "";
	    boolean isTestAccount = BooleanParser.parse(Payment.get(Payment.IS_ACCOUNT_TEST)); 
	    if ("RSA".equals(params.get("sign_type")))
	    {
	        sign = RSAUtil.sign(Payment.get(Payment.RSA_P_KEY), sign_src);
	    } else
	    {
	    	String md5_key = AES.getInstace().decrypt(Payment.get(Payment.MD5_KEY));
	    	if (isTestAccount) {
	    		md5_key = AES.getInstace().decrypt(Payment.get(Payment.MD5_KEY_TEST));
	    	}
	        sign_src += "&key=" + md5_key;
	        sign = Md5Algorithm.getInstance().md5Digest(
	                sign_src.getBytes("utf-8"));
	    }
		return sign;
	}

	/**
	 * 封装支付信息
	 * @param params
	 * @param sign
	 * @return
	 */
	private String setPaymentInfo(Map<String, String> params, String sign) {
		String req_data;
		PaymentInfo payInfo = new PaymentInfo();
	    payInfo.setApp_request(params.get("app_request"));
	    payInfo.setBg_color(params.get("bg_color"));
	    payInfo.setBusi_partner(params.get("busi_partner"));
	    payInfo.setCard_no(params.get("card_no"));
	    payInfo.setDt_order(params.get("dt_order"));
	    payInfo.setId_no(params.get("id_no"));
	    payInfo.setInfo_order(params.get("info_order"));
	    payInfo.setMoney_order(params.get("money_order"));
	    payInfo.setName_goods(params.get("name_goods"));
	    payInfo.setNo_agree(params.get("no_agree"));
	    payInfo.setNo_order(params.get("no_order"));
	    payInfo.setNotify_url(params.get("notify_url"));
	    payInfo.setOid_partner(params.get("oid_partner"));
	    payInfo.setAcct_name(params.get("acct_name"));
	    payInfo.setRisk_item(params.get("risk_item"));
	    payInfo.setSign_type(params.get("sign_type"));
	    payInfo.setUrl_return(params.get("url_return"));
	    payInfo.setUser_id(params.get("user_id"));
	    payInfo.setValid_order(params.get("valid_order"));
	    payInfo.setSign(sign);
	    
	    req_data = JSON.toJSONString(payInfo);
		return req_data;
	}
}
