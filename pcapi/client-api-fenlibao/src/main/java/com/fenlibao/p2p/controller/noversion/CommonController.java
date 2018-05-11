package com.fenlibao.p2p.controller.noversion;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.global.ResponseEnum;
import com.fenlibao.p2p.model.trade.config.PayConfig;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.UserTokenService;
import com.fenlibao.p2p.util.HttpUtil;
import com.fenlibao.p2p.util.loader.Config;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础/公用服务接口
 * @author Administrator
 *
 */
@RestController
@RequestMapping("baseServices")
public class CommonController {

	private static final Logger logger=LogManager.getLogger(CommonController.class);

	@Resource
	UserInfoService userInfoService;

	@Resource
	UserTokenService userTokenService;

	/**
	 * 获取手机号所属运营商
	 * @param paramForm
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "phone/region", method = RequestMethod.GET)
	public HttpResponse getPhoneRgion(@ModelAttribute BaseRequestForm  paramForm,
			@RequestParam(required = false, value="phone") String phone){
		HttpResponse response = new HttpResponse();
		try {
			if (!paramForm.validate()||StringUtils.isEmpty(phone)) {
				response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(),ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
				return response;
			}
			String url = Config.get("taobao.api.region")+phone;
			String result = HttpUtil.httpget(url);
			
			result = result.replaceAll("^[__]\\w{14}+[_ = ]+","[");
			result= result + "]";
			JSONArray  array = JSONArray.parseArray(result);
			JSONObject jsonObj = array.getJSONObject(0);
			String region = (String) jsonObj.get("carrier");
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("region", region);
			
			response.setData(map);
		} catch (Exception ex) {
			response.setCodeMessage(ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getCode(),ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getMessage());
			logger.error("[BaseServicesController.getPhoneRgion]"+ex.getMessage(), ex);
		}
		return response;
	}


	/**
	 * 判断当前用户(手机号)是否第一次登陆系统
	 * @param paramForm
	 * @param phoneNum
     * @return
     */
	@RequestMapping(value = "user/isFirstLogin", method = RequestMethod.GET)
	public HttpResponse isUserFirstLogin(@ModelAttribute BaseRequestForm  paramForm,
									  @RequestParam(required = false, value="phoneNum") String phoneNum){
		HttpResponse response = new HttpResponse();

		if (!paramForm.validate()||StringUtils.isEmpty(phoneNum)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}
		UserInfo userInfo = userInfoService.getUserInfo(phoneNum);
		if(userInfo==null){
			response.setCodeMessage(ResponseCode.USER_NOT_EXIST.getCode(),ResponseCode.USER_NOT_EXIST.getMessage());
			return response;
		}
		int isFirstLogin = "S".equals(userInfo.getIsFirstLogin())?1:0;
		response.getData().put("isFirstLogin",isFirstLogin);
		return response;
	}

	@RequestMapping("isUserTokenValid")
	public HttpResponse isInvalidToken(@ModelAttribute BaseRequestForm paramForm,
									   @RequestParam(required = false, value="token") String token,
									   @RequestParam(required = false, value="userId") String userId) throws Exception{
		HttpResponse result = new HttpResponse();
		boolean invalidFlag = userTokenService.isInvalidToken(token, userId, paramForm.getClientType());
		result.getData().put("isUserTokenValid",invalidFlag==true?0:1);

		return result;
	}

	/**
	 * 获取配置
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "config", method = RequestMethod.GET)
	HttpResponse getConfig(BaseRequestForm form) {
		HttpResponse response = new HttpResponse();
		Map<String, Object> config = new HashMap<>();
		if (!form.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		//TODO 配置在数据库+redis
		PayConfig payConfig = ConfigFactory.create(PayConfig.class);
		config.put("TPPaymentChannelCode", payConfig.TPPAYMENT_CHANNEL_CODE());
		config.put("CGMode", "0");
		response.setData(config);
		return response;
	}

}
