package com.fenlibao.lianpay.share.util;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.fenlibao.lianpay.share.config.PartnerConfig;
import com.fenlibao.lianpay.share.security.Md5Algorithm;
import com.fenlibao.lianpay.share.security.RSAUtil;
import com.fenlibao.lianpay.yintong.PaymentInfo;

/**
 * 连连支付充值form表单工具类
 * @author yangzengcai
 * @date 2015年8月21日
 */
public class PayFormUtil {

	/**
	 * 对充值参数进行签名和封装转成json格式
	 * @param params
	 * @return "req_data"
	 * @throws UnsupportedEncodingException
	 */
	public static String getPayFormJson(Map<String, String> params) throws UnsupportedEncodingException {
		String req_data = null;
		StringBuffer strBuf = new StringBuffer();
		if (!FuncUtils.isNull(params.get("acct_name")))
		{
			strBuf.append("acct_name=");
			strBuf.append(params.get("acct_name"));
			strBuf.append("&app_request=3");
		} else
		{
			strBuf.append("app_request=3");
		}
		if (!FuncUtils.isNull(params.get("bg_color")))
		{
			strBuf.append("&bg_color=");
			strBuf.append(params.get("bg_color"));
		}
		strBuf.append("&busi_partner=");
		strBuf.append(params.get("busi_partner"));
		if (!FuncUtils.isNull(params.get("card_no")))
		{
			strBuf.append("&card_no=");
			strBuf.append(params.get("card_no"));
		}
		strBuf.append("&dt_order=");
		strBuf.append(params.get("dt_order"));
		if (!FuncUtils.isNull(params.get("id_no")))
		{
			strBuf.append("&id_no=");
			strBuf.append(params.get("id_no"));
		}
		if (!FuncUtils.isNull(params.get("info_order")))
		{
			strBuf.append("&info_order=");
			strBuf.append(params.get("info_order"));
		}
		strBuf.append("&money_order=");
		strBuf.append(params.get("money_order"));
		if (!FuncUtils.isNull(params.get("name_goods")))
		{
			strBuf.append("&name_goods=");
			strBuf.append(params.get("name_goods"));
		}
		if (!FuncUtils.isNull(params.get("no_agree")))
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
		if (!FuncUtils.isNull(params.get("risk_item")))
		{
			strBuf.append("&risk_item=");
			strBuf.append(params.get("risk_item"));
		}
		strBuf.append("&sign_type=");
		strBuf.append(params.get("sign_type"));
		if (!FuncUtils.isNull(params.get("url_return")))
		{
			strBuf.append("&url_return=");
			strBuf.append(params.get("url_return"));
		}
		if (!FuncUtils.isNull(params.get("user_id")))
		{
			strBuf.append("&user_id=");
			strBuf.append(params.get("user_id"));
		}
		if (!FuncUtils.isNull(params.get("valid_order")))
		{
			strBuf.append("&valid_order=");
			strBuf.append(params.get("valid_order"));
		}
		String sign_src = strBuf.toString();
		if (sign_src.startsWith("&"))
		{
			sign_src = sign_src.substring(1);
		}
		String sign = "";
		if ("RSA".equals(params.get("sign_type")))
		{
			sign = RSAUtil.sign(PartnerConfig.TRADER_PRI_KEY, sign_src);
		} else
		{
			sign_src += "&key=" + PartnerConfig.MD5_KEY;
			sign = Md5Algorithm.getInstance().md5Digest(
					sign_src.getBytes("utf-8"));
		}
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
		strBuf = null;
		return req_data;
	}
}
