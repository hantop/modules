package com.fenlibao.p2p.controller.noversion;

import com.dimeng.p2p.S61.entities.T6141;
import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.bank.BankService;
import com.fenlibao.p2p.service.pay.ILianLianPayService;
import com.fenlibao.p2p.service.pay.IUserPayInfoService;
import com.fenlibao.p2p.service.recharge.IRechargeService;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("sdk")
public class BindBankCardController {
	
	protected static final Logger logger = LogManager.getLogger(BindBankCardController.class);
	
	@Resource
	private BankService bankService;
	@Resource
	private IUserPayInfoService userPayInfoService;
	@Resource
	private IRechargeService rechargeService;
	@Resource
	private ILianLianPayService lianLianPayService;

	/**
	 * sdk绑定银行卡
	 * @param userId
	 * @param bankCardNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "bind/card", method = RequestMethod.POST)
	public HttpResponse rechargeSDK(String userId, String bankCardNo, String token) {
		logger.debug(String.format("+++++ bind bankCard by SDK +++++++"));
		HttpResponse response = new HttpResponse();
		if (!StringUtils.isNoneBlank(userId, bankCardNo, token)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		try {
			int user_id = Integer.parseInt(userId);
			String returnMsg = lianLianPayService.queryBankCardByNo(AES.getInstace().decrypt(bankCardNo));
			T6141 t6141 = userPayInfoService.selectT6141(user_id);
			// Json转成Map
	        Map<String, String>  retMap = new Gson().fromJson(returnMsg, Map.class);
	        Map<String, Object> data = new HashMap<String, Object>();
	        List<Map<String, Object>> bankCrads = null;
	        if (retMap.size() > 0 && "0000".equals(retMap.get("ret_code"))) {
	        	Map<String, String> params = new HashMap<String, String>();
	        	params.put("acct_name", t6141.F02); // 开户名
	        	params.put("bank_code", retMap.get("bank_code")); // 银行编码
	        	this.rechargeService.perfectPayInfo(params, user_id);
	        	bankCrads = bankService.getBankCardsByUserId(user_id,1);
	        	
	        }
	        data.put("bankCardList", bankCrads);
	        response.setData(data);
		}catch (Throwable e) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error(e.toString(), e);
		}
		
		return response;
	}
	
}
