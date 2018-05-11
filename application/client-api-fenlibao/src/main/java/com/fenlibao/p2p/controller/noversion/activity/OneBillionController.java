package com.fenlibao.p2p.controller.noversion.activity;

import com.fenlibao.p2p.model.entity.activity.AnniversaryInvestRecord;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.UserTokenService;
import com.fenlibao.p2p.service.activity.ActivityService;
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
@RequestMapping("new/activity/onebillion")
public class OneBillionController {

	private static final Logger logger= LogManager.getLogger(OneBillionController.class);
	private static String ACTIVITY_CODE="ONE_BILLION";//破10亿活动


	@Resource
	private ActivityService activityService;
	@Resource
	private UserTokenService userTokenService;

	/**
	 * 破10亿活动
	 * @param paramForm
	 * @return
	 */
	@RequestMapping(value = "records",method = RequestMethod.POST)
	HttpResponse investRecords(BaseRequestForm  paramForm) {
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return  response;
		}
		List<AnniversaryInvestRecord> list = activityService.anniversaryInvestRecords(ACTIVITY_CODE);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("items", list);
		response.setData(data);
		return response;
	}

	/**
	 * 我的冲榜信息接口
	 * @param paramForm
	 * @return
	 */
	@RequestMapping(value = "myInvestInfo",method = RequestMethod.POST)
	HttpResponse myAnniversaryInvestInfo(BaseRequestForm paramForm,String token,String userId) {
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate() || StringUtils.isEmpty(userId)||StringUtils.isEmpty(token)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return  response;
		}
		if (StringUtils.isNotEmpty(userId)&&StringUtils.isNotEmpty(token)){
			if(userTokenService.isInvalidToken(token, userId, paramForm.getClientType())){
				response.setCodeMessage(ResponseCode.COMMON_NOT_VALID_TOKEN);
				return response;
			}
		}
		Map<String,Object> data = activityService.myAnniversaryInvestInfo(ACTIVITY_CODE,userId);
		response.setData(data);
		return response;
	}
}
