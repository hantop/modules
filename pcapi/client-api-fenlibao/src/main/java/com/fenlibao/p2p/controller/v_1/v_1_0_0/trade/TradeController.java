package com.fenlibao.p2p.controller.v_1.v_1_0_0.trade;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.common.util.encrypt.PasswordCryptUtil;
import com.fenlibao.p2p.model.form.trade.DueInAmount;
import com.fenlibao.p2p.model.form.trade.TradeRecordForm;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestFormExtend;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.global.ResponseEnum;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.loader.Message;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import com.fenlibao.p2p.util.paginator.domain.Pager;

/**
 * 交易控制器
 * @author yangzengcai
 * @date 2015年8月15日
 */
@RequestMapping("/trade")
@RestController("v_1_0_0/TradeController")
public class TradeController {

	private static final Logger logger = LogManager.getLogger(TradeController.class);
	
	@Resource
	private ITradeService tradeService;
	@Resource
	private UserInfoService userInfoService;
	
	private final String TRADE_PASSWORD_REG = "^\\d{6}$"; //交易密码正则
	
	/**
	 * 交易记录   2016-5-10 junda.feng
	 * @param params
	 * @param type 
	 * @return
	 * @throws IntrospectionException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping(value = "record/list", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
	public HttpResponse recordList(@ModelAttribute BaseRequestFormExtend params,
			Integer dayType, String startTimestamp, String endTimestamp, Integer type,Integer page,Integer limit,String versionType) throws IllegalAccessException, InvocationTargetException, IntrospectionException {
		HttpResponse response = new HttpResponse();
		if (!params.validate()) {
			response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(),
					ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
			return response;
		}

		VersionTypeEnum vte = VersionTypeEnum.parse(versionType);
		vte = vte != null ? vte : VersionTypeEnum.PT;

		//分页  junda.feng  2016-5-10
        PageBounds pageBounds=new PageBounds(page==null?1:page, limit==null? InterfaceConst.PAGING_NUMBER:limit);

		List<TradeRecordForm> list = tradeService.getRecordList(params.getUserId(), dayType, startTimestamp, endTimestamp, type, vte, pageBounds);
		Pager pager = new Pager(list);
		response.setData(CommonTool.toMap(pager));
		return response;
	}
	
	/**
	 * 收益记录
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "earnings/list", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
	public HttpResponse earningsList(@ModelAttribute BaseRequestFormExtend params) {
		HttpResponse response = new HttpResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		if (!params.validate()) {
			response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(), 
					ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
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
	 * @return
	 */
	@RequestMapping(value = "password", method = RequestMethod.PUT, headers = APIVersion.V_1_0_0)
	public HttpResponse resetPassword(@ModelAttribute BaseRequestFormExtend params,
			String phoneNum, String verifyCode, String newPassword,
			String identityNo, String identityName) {
		HttpResponse response = new HttpResponse();
		if(1==1){
			response.setCodeMessage(ResponseCode.NOT_SUPPURT_TOPUP);
			return response;
		}
		if (!params.validate() || !StringUtils.isNoneBlank(phoneNum, verifyCode, newPassword)) {
			response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(), 
					ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
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
				response.setCodeMessage(ResponseEnum.RESPONSE_TRADE_PASSWORD_TYPE_WRONG.getCode(), 
						ResponseEnum.RESPONSE_TRADE_PASSWORD_TYPE_WRONG.getMessage());
			} else {
				newPassword = PasswordCryptUtil.crypt(newPassword);
				//校验验证码
				ResponseEnum status = userInfoService.verifySmsCode(phoneNum, String.valueOf(InterfaceConst.RESET_TRADE_PASSWORD), verifyCode);
				if (status == null) {
					int result = tradeService.resetPassword(userId, newPassword); //重置交易密码
					if (result < 1) {
						response.setCodeMessage(Message.STATUS_1031, Message.get(Message.STATUS_1031));
					}
				} else {
					response.setCodeMessage(status.getCode(),
							status.getMessage());
				}
			}
		} catch (Throwable e) {
			logger.error(e.toString(), e);
			response.setCodeMessage(ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getCode(), 
					ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getMessage());
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
	@RequestMapping(value = "password/status", method = RequestMethod.PUT, headers = APIVersion.V_1_0_0)
	public HttpResponse switchNoPassword(@ModelAttribute BaseRequestFormExtend params,
			Integer isOpen, String tradePassword) {
		HttpResponse response = new HttpResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		if (!params.validate() || isOpen == null || StringUtils.isBlank(tradePassword)) {
			response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(), 
					ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
			return response;
		}
		try {
			tradePassword = PasswordCryptUtil.cryptAESPassword(tradePassword);
			if (!tradeService.getTradePassword(params.getUserId()).equals(tradePassword)) {
				response.setCodeMessage(Message.STATUS_1015, Message.get(Message.STATUS_1015));
				return response;
			}
			int result = tradeService.switchNoPassword(params.getUserId(), isOpen);
			if (result < 1) {
				response.setCodeMessage(Message.STATUS_1031, 
						Message.get(Message.STATUS_1031));
				return response;
			}
			data.put("isOpen", isOpen); // app说要
			response.setData(data);
		} catch (Exception e) {
			logger.error(e.toString(), e);
			response.setCodeMessage(ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getCode(), 
					ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getMessage());
		}
		return response;
	}
	
	/**
	 * 待收本息
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "duein/amount", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
	public HttpResponse dueInPrincipalAndInterest(@ModelAttribute BaseRequestFormExtend params, String versionType) {
		HttpResponse response = new HttpResponse();
		if (!params.validate()) {
			response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(), 
					ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
			return response;
		}

		VersionTypeEnum versionTypeEnum = VersionTypeEnum.parse(versionType);
		versionTypeEnum = versionTypeEnum != null ? versionTypeEnum : VersionTypeEnum.PT;

		BigDecimal dueInPrincipal = BigDecimal.ZERO;
		BigDecimal dueInterest = BigDecimal.ZERO;
		BigDecimal dueOthers = BigDecimal.ZERO;

		//3.1.0版本以前待收本息
		//DueInAmount dueInAmount = tradeService.getDueInAmount(params.getUserId()); //待收本息及违约金

		//3.2.0版本待收本息
		DueInAmount dueInAmount = tradeService.getNewDueInAmount(params.getUserId(), versionTypeEnum);
		DueInAmount planDueInAmount = tradeService.getPlanDueInAmount(params.getUserId(), versionTypeEnum);
		if (dueInAmount != null&&planDueInAmount!=null) {
			dueInPrincipal = dueInAmount.getPrincipal().add(planDueInAmount.getPrincipal());
			dueInterest = dueInAmount.getInterest().add(planDueInAmount.getInterest());
			dueOthers = dueInAmount.getOthers().add(planDueInAmount.getOthers());
		}else if(null ==dueInAmount &&planDueInAmount!=null){
			dueInPrincipal = planDueInAmount.getPrincipal();
			dueInterest = planDueInAmount.getInterest();
			dueOthers = planDueInAmount.getOthers();
		}else if(dueInAmount != null&&null == planDueInAmount){
			dueInPrincipal = dueInAmount.getPrincipal();
			dueInterest = dueInAmount.getInterest();
			dueOthers = dueInAmount.getOthers();
		}
		Map<String, Object> data = new HashMap<String, Object>();
		BigDecimal totalDueInAmount = dueInPrincipal.add(dueInterest).add(dueOthers);
		data.put("totalDueInAmount", totalDueInAmount.toString());
		data.put("principal", dueInPrincipal.toString());
		data.put("interest", dueInterest.toString());
		data.put("others", dueOthers.toString());
		response.setData(data);
		return response;
	}
}
