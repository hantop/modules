package com.fenlibao.p2p.controller.noversion;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.ApkUpdateVo;
import com.fenlibao.p2p.service.DeviceService;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.channel.PaymentChannelService;
import com.fenlibao.p2p.service.common.CommonServices;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.HttpUtil;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
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
	private DeviceService deviceService;
	@Resource
	private PaymentChannelService paymentChannelService;
	@Resource
	private CommonServices commonServices;
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
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
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
			response.setCodeMessage(ResponseCode.FAILURE);
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

	/**
	 * 客户端升级接口
	 *
	 * @param paramForm
	 * @return
	 */
	@RequestMapping(value = "app/update", method = RequestMethod.GET)
	HttpResponse appUpdate(
			@ModelAttribute BaseRequestForm paramForm,
			@RequestParam(required = false, value = "versionCode") String versionCode,
			@RequestParam(required = false, value = "channelCode") String channelCode) {
		logger.info(paramForm.toString());
		HttpResponse response = new HttpResponse();
		try {
			if (StringUtils.isEmpty(versionCode)) {
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}
			if(StringUtils.isEmpty(channelCode)){//设置默认渠道
				channelCode = Config.get("app.default.channelCode");
			}
			ApkUpdateVo vo = this.deviceService.getApk(versionCode,Integer.parseInt(paramForm.getClientType()),channelCode);
			response.setData(CommonTool.toMap(vo));
		} catch (Exception ex) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("[DeviceController.appUpdate]" + ex.getMessage(), ex);
		}
		return response;
	}

	/**
	 * 获取短链
	 * @param paramForm
	 * @param longUrl
     * @return
     */
	@RequestMapping(value = "shorturl", method = RequestMethod.GET)
	HttpResponse getShortUrl(
			@ModelAttribute BaseRequestForm paramForm, String longUrl) {
		logger.info(paramForm.toString());
		HttpResponse response = new HttpResponse();
		try {
			if (StringUtils.isEmpty(longUrl)) {
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}
			Map<String, Object> data = new HashMap<>(1);
			String shortUrl = CommonTool.genShortUrl(longUrl);
			data.put("shortUrl", shortUrl);
			response.setData(data);
		} catch (Exception ex) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("[DeviceController.appUpdate]" + ex.getMessage(), ex);
		}
		return response;
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

		Integer canDebtTransfer = Integer.parseInt(Config.get("fenlibao.canDebtTransfer"));

		Map<String,String> resultMap = paymentChannelService.getBaseChannel();
		config.put("TPPaymentChannelCode",resultMap.get("TPPaymentChannelCode") );
		config.put("CGMode", resultMap.get("CGMode"));
		config.put("canDebtTransfer", canDebtTransfer);
		response.setData(config);
		return response;
	}

	/**
	 * 成交金额和安全回款天数
	 */
	@RequestMapping(value = "platformStatictis", method = RequestMethod.GET)
	HttpResponse platformStatictis(BaseRequestForm form) throws ParseException {
		HttpResponse response = new HttpResponse();
		Map<String, Object> config = new HashMap<>();
		if (!form.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		Date begin = DateUtil.StringToDate("2015-09-15",DateUtil.DATE_FORMAT);
		Map<String,Object> resultMap = commonServices.platformStatictis();
		config.put("days",DateUtil.daysOfTwo(begin,new Date()));
		config.put("sumMoney", String.valueOf(resultMap.get("sumMoney")));
		response.setData(config);
		return response;
	}
}
