package com.fenlibao.p2p.controller.v_3.v_3_0_0.trade;

import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.common.util.encrypt.PasswordCryptUtil;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.form.trade.DueInAmount;
import com.fenlibao.p2p.model.form.trade.TradeRecordForm;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.trade.ITradeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易控制器
 * @author yangzengcai
 * @date 2015年8月15日
 */
@RequestMapping(value = "trade", headers = APIVersion.v_3_0_0)
@RestController("v_3_0_0/TradeController")
public class TradeController {

	private static final Logger logger = LogManager.getLogger(TradeController.class);
	
	@Resource
	private ITradeService tradeService;
	@Resource
	private UserInfoService userInfoService;
	
	private final String TRADE_PASSWORD_REG = "^\\d{6}$"; //交易密码正则
	
	/**
	 * 交易记录
	 * @param params
	 * @param isUp
	 * @param timestamp
	 * @return
	 */
	@RequestMapping(value = "record/list", method = RequestMethod.GET)
	public HttpResponse recordList(@ModelAttribute BaseRequestFormExtend params,Integer pageNo,Integer pagesize) {
		HttpResponse response = new HttpResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		List<TradeRecordForm> list = tradeService.getRecordList(params.getUserId(), pageNo, pagesize, VersionTypeEnum.PT);
		data.put("list", list);
		response.setData(data);
		return response;
	}
	
	/**
	 * 收益记录
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "earnings/list", method = RequestMethod.GET)
	public HttpResponse earningsList(@ModelAttribute BaseRequestFormExtend params) {
		HttpResponse response = new HttpResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		if (!params.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM
					);
			return response;
		}
		//List<EarningsRecordForm> list = tradeService.getEarningsList(params.getUserId());
		Map<String, Object> list = tradeService.getEarningsRecordList(params.getUserId());
		data.put("list", list);
		response.setData(data);
		return response; 
	}
	
	/**
	 * 重置交易密码
	 * @param params
	 * @param phoneNum
	 * @param verifyCode
	 * @param newPassword
	 * @param identityNo
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "password", method = RequestMethod.PUT)
	public HttpResponse resetPassword(@ModelAttribute BaseRequestFormExtend params,
			String phoneNum, String verifyCode, String newPassword,
			String identityNo, String identityName) {
		HttpResponse response = new HttpResponse();
		if (!params.validate() || !StringUtils.isNoneBlank(phoneNum, verifyCode, newPassword)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM
					);
			return response;
		}
		int userId = params.getUserId();
		try { 
			ResponseCode responseCode = this.verifyIdentity(identityNo, identityName, userId);
			if (responseCode != null) {
				response.setCodeMessage(responseCode.getCode(), responseCode.getMessage());
				return response;
			}
			phoneNum = AES.getInstace().decrypt(phoneNum);
			newPassword = AES.getInstace().decrypt(newPassword);
			if (!newPassword.matches(TRADE_PASSWORD_REG)) { //校验交易密码类型
				response.setCodeMessage(ResponseCode.USER_TRADE_PASSWORD_ERROR);
			} else {
				newPassword = PasswordCryptUtil.crypt(newPassword);
				//校验验证码
				ResponseCode status = userInfoService.verifySmsCode(phoneNum, String.valueOf(InterfaceConst.RESET_TRADE_PASSWORD), verifyCode);
				if (status == null) {
					int result = tradeService.resetPassword(userId, newPassword); //重置交易密码
					if (result < 1) {
						response.setCodeMessage(ResponseCode.FAILURE);
					}
				} else {
					response.setCodeMessage(status.getCode(),
							status.getMessage());
				}
			}
		} catch (Throwable e) {
			logger.error(e.toString(), e);
			response.setCodeMessage(ResponseCode.FAILURE
					);
		}
		return response;
	}
	
	private ResponseCode verifyIdentity(String identityNo, String identityName, int userId) throws Throwable {
		boolean hasPwd = this.tradeService.hasSetPwd(userId);
		if (hasPwd) {
			if (!StringUtils.isNoneBlank(identityNo, identityName)) {
				return ResponseCode.EMPTY_PARAM;
			}
			identityNo = AES.getInstace().decrypt(identityNo);
			identityName = AES.getInstace().decrypt(identityName);
			//校验身份
			boolean isTrue = this.userInfoService.verifyIdentity(identityNo, identityName, userId);
			if (!isTrue) {
				return ResponseCode.USER_IDENTITY_VERIFY_FAIL;
			}
		}
		return null;	
	}
	
	/**
	 * 开通/关闭免交易密码接口
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "password/status", method = RequestMethod.PUT)
	public HttpResponse switchNoPassword(@ModelAttribute BaseRequestFormExtend params,
			Integer isOpen, String tradePassword) {
		HttpResponse response = new HttpResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		if (!params.validate() || isOpen == null || StringUtils.isBlank(tradePassword)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM
					);
			return response;
		}
		try {
			tradePassword = PasswordCryptUtil.cryptAESPassword(tradePassword);
			if (!tradeService.getTradePassword(params.getUserId()).equals(tradePassword)) {
				response.setCodeMessage(ResponseCode.USER_PASSWORD_ERROR);
				return response;
			}
			int result = tradeService.switchNoPassword(params.getUserId(), isOpen);
			if (result < 1) {
				response.setCodeMessage(ResponseCode.FAILURE
						);
				return response;
			}
			data.put("isOpen", isOpen); // app说要
			response.setData(data);
		} catch (Exception e) {
			logger.error(e.toString(), e);
			response.setCodeMessage(ResponseCode.FAILURE
					);
		}
		return response;
	}
	
	/**
	 * 待收本息
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "duein/amount", method = RequestMethod.GET)
	public HttpResponse dueInPrincipalAndInterest(@ModelAttribute BaseRequestFormExtend params) {
		HttpResponse response = new HttpResponse();
		if (!params.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM
					);
			return response;
		}
		DueInAmount dueInAmount = tradeService.getDueInAmount(params.getUserId());
		BigDecimal principal = dueInAmount.getPrincipal();
		BigDecimal gains = dueInAmount.getGains();
		Map<String, Object> data = new HashMap<String, Object>();
		BigDecimal totalDueInAmount = principal.add(gains);
		data.put("totalDueInAmount", totalDueInAmount.toString());
		data.put("principal", principal.toString());
		data.put("interest", gains.toString());
		response.setData(data);
		return response;
	}
}
