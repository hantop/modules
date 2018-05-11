package com.fenlibao.p2p.controller.noversion.activity;

import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.model.entity.activity.AnniversaryInvestRecord;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.global.ResponseEnum;
import com.fenlibao.p2p.service.UserTokenService;
import com.fenlibao.p2p.service.activity.ActivityService;
import com.fenlibao.p2p.util.loader.Message;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存管开户活动
 */
@RestController
@RequestMapping("register/activity/")
public class RegisterActivityController {

	private static final Logger logger= LogManager.getLogger(RegisterActivityController.class);
	private static String ACTIVITY_CODE="ANNIVERSARY_ACTIVITY";//一周年活动code

	@Resource
	private ActivityService activityService;
	@Resource
	private UserTokenService userTokenService;

	/**
	 * 手机号正则表达式
	 */
	private final String phonePattern = "^(13|14|15|17|18)[0-9]{9}$";

	/**
	 * 获取手机登记状态
	 * @param paramForm
	 * @return
	 */
	@RequestMapping(value = "getStatus",method = RequestMethod.POST)
	HttpResponse getStatus(BaseRequestForm paramForm,String token, String userId) {
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate() || StringUtils.isEmpty(userId)||StringUtils.isEmpty(token)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return  response;
		}

         //判断是否在活动期内
		if(!activityService.isActivityTime("cgkh")){
			response.setCodeMessage(ResponseCode.ACTIVITY_NOT_ACTIVITY_TIME);
			return response;
		}




			int count = activityService.getStatus(Integer.valueOf(userId));
			Map<String,Object> map = new HashMap<>();
			if(count>0){
				// 手机号码已注册
				response.setCodeMessage(ResponseCode.SUCCESS);
				map.put("status","1");
				response.setData(map);
			}else {
				response.setCodeMessage(ResponseCode.SUCCESS);
				map.put("status","0");
				response.setData(map);
			}

		return  response;
	}

	/**
	 * 登记手机号
	 * @param paramForm
	 * @return
	 */
	@RequestMapping(value = "addPhone",method = RequestMethod.POST)
	HttpResponse addPhone(BaseRequestForm paramForm,String token, String userId,String phoneNum) {
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate() || StringUtils.isEmpty(userId)||StringUtils.isEmpty(token)|| StringUtils.isBlank(phoneNum)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return  response;
		}
		try {
			AES aesInstance = AES.getInstace();
			phoneNum = aesInstance.decrypt2(phoneNum);// 手机号解密.
		} catch (BadPaddingException bpe) {
			logger.error("[RegisterActivityController.register]" + bpe.getMessage(), bpe);
			response.setCodeMessage(ResponseEnum.RESPONSE_INVALID_PARAM.getCode(), ResponseEnum.RESPONSE_INVALID_PARAM.getMessage());
			return response;
		}

		//判断是否在活动期内
		if(!activityService.isActivityTime("cgkh")){
			response.setCodeMessage(ResponseCode.ACTIVITY_NOT_ACTIVITY_TIME);
			return response;
		}

		// 验证手机号格式
		if (checkPhoneFormat(phoneNum)) {

			int count = activityService.getStatus(Integer.valueOf(userId));
			Map<String,Object> map = new HashMap<>();
			if(count>0){
				// 手机号码已注册
				response.setCodeMessage(ResponseCode.ACTIVITY_RECORD_EXIST);
				response.setData(map);
			}else {
				try {
					this.activityService.addActivityUserPhone(Integer.valueOf(userId),phoneNum,"cgkh");
				}catch (Exception e){
					response.setCodeMessage(ResponseCode.FAILURE);
					return response;
				}
				response.setCodeMessage(ResponseCode.SUCCESS);
				response.setData(map);
			}
		} else {
			// 手机号码格式不正确
			response.setCodeMessage(Message.STATUS_1019, Message.get(Message.STATUS_1019));
		}
		return  response;
	}


	/**
	 * 验证手机号格式
	 * @param phoneNum
	 * @return
	 */
	private boolean checkPhoneFormat(String phoneNum) {
		return phoneNum.matches(phonePattern);
	}
}
