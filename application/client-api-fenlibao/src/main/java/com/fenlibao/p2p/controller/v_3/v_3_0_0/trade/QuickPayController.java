package com.fenlibao.p2p.controller.v_3.v_3_0_0.trade;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestFormExtend;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.pay.ILlpQuickPayService;
import com.fenlibao.p2p.service.pay.IPaymentService;
import com.fenlibao.p2p.util.Validator;

/**
 * 快捷支付
 * @author zcai
 * @date 2016年4月18日
 */
@RequestMapping(value = "lianlianPay", headers = APIVersion.v_3_0_0)
@RestController("v_3_0_0/QuickPayController")
public class QuickPayController {

	private static final Logger logger = LogManager.getLogger(QuickPayController.class);
	
	@Resource
	private ILlpQuickPayService quickPayService;
	@Resource
	private IPaymentService paymentService;
	
	@RequestMapping(value = "quick", method = RequestMethod.POST)
	public HttpResponse quickPay(BaseRequestFormExtend params,
			String amount, String bankCardNo) {
		HttpResponse response = new HttpResponse();
		Map<String, Object> data = new HashMap<>();
		if (!params.validate() || !StringUtils.isNoneBlank(amount)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		if (!Validator.isAmount(amount)) {
			response.setCodeMessage(ResponseCode.PAYMENT_AMOUNT_FORMAT_ERROR);
			return response;
		}
		try {
			Map<String, String> result = quickPayService.getRequestData(amount, params.getUserId(), params.getClientType());
			data.put("req_data", result.get("req_data")); //AES.getInstace().encrypt(
			data.put("url", result.get("url"));
			response.setData(data);
		} catch (BusinessException busi) {
			response.setCodeMessage(busi);
			logger.warn("quick payment warn,amount[{}],bankCarNo[{}],params[{}]", amount,bankCardNo,params);
		} catch (Throwable e) {
			logger.error(String.format("quick payment error,amount[%s],bankCarNo[%s],params[%s]", amount,bankCardNo,params), e);
			response.setCodeMessage(ResponseCode.FAILURE);
		}
		
		return response;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(AES.getInstace().encrypt("2TKePcFUIOEO0cHzj5BmIMSsuPIvrenB0zllvw42TF4SX7tRfeSjxwUyc0BGuWwibw9Et7TH5UlahZNyFc"));
	}
	
}
