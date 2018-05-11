package com.fenlibao.p2p.controller.noversion.activity;

import com.fenlibao.p2p.controller.noversion.ActivityController;
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一周年活动
 */
@RestController
@RequestMapping("new/activity/anniversary")
public class AnniversaryActivityController {

	private static final Logger logger= LogManager.getLogger(AnniversaryActivityController.class);
	private static String ACTIVITY_CODE="ANNIVERSARY_ACTIVITY";//一周年活动code

	@Resource
	private ActivityService activityService;
	@Resource
	private UserTokenService userTokenService;

	/**
	 * 土豪pk榜接口
	 * @param paramForm
	 * @return
	 */
	@RequestMapping(value = "records",method = RequestMethod.POST)
	HttpResponse investRecords(BaseRequestForm paramForm) {
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate() ) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return  response;
		}
		List<AnniversaryInvestRecord> list=activityService.anniversaryInvestRecords(ACTIVITY_CODE);
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
	HttpResponse myAnniversaryInvestInfo(BaseRequestForm paramForm, String token, String userId) {
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
