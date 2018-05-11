package com.fenlibao.p2p.controller.noversion.activity;

import com.fenlibao.p2p.model.entity.activity.DtbForCashBack;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.activity.ActivityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 地铁报活动
 */
@RestController
@RequestMapping("new/activity/dtb")
public class DTBActivityController {

	private static final Logger logger= LogManager.getLogger(DTBActivityController.class);

	@Resource
	private ActivityService activityService;

	/**
	 * 判断是否活动时间
	 * @param paramForm
	 * @return
	 */
	@RequestMapping(value = "isActivityTime",method = RequestMethod.POST)
	HttpResponse isActivityTime(BaseRequestForm paramForm) {
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return  response;
		}
		activityService.isActivityTime(DtbForCashBack.ACTIVITY_CODE);
		return response;
	}

}
