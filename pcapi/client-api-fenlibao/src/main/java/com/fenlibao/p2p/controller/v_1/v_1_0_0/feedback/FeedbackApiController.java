package com.fenlibao.p2p.controller.v_1.v_1_0_0.feedback;

import com.fenlibao.p2p.model.entity.feedback.FeedbackEntity;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseEnum;
import com.fenlibao.p2p.service.feedback.IFeedbackService;
import com.fenlibao.p2p.util.loader.Message;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 意见反馈控制器
 * @author yangzengcai
 * 2015年7月23日
 */
@RestController("v_1_0_0/FeedbackApiController")
@RequestMapping("/")
public class FeedbackApiController {

	private static final Logger logger = LogManager.getLogger(FeedbackApiController.class);
	
	@Resource
	private IFeedbackService feedbackService;
	
	@RequestMapping(value = "feedback", method = RequestMethod.POST, headers = APIVersion.V_1_0_0)
    HttpResponse feedback(@ModelAttribute BaseRequestForm paramForm,
			@ModelAttribute FeedbackEntity feedBack) {
		logger.debug("-------------Content----------------");
		logger.debug(feedBack.getContent());
		HttpResponse respone = new HttpResponse();
		if (!paramForm.validate() || StringUtils.isBlank(feedBack.getContent())) {
			respone.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
			return respone;
		}
		try {
			String contact = feedBack.getContact();
			if (StringUtils.isNotBlank(contact)) {
				if (contact.length() > 32) {
					respone.setCodeMessage(ResponseEnum.CONTACT_TOO_LONG.getCode(), ResponseEnum.CONTACT_TOO_LONG.getMessage());
					return respone;
				}
			}
			respone = feedbackService.saveFeedback(feedBack);
		} catch (Exception e) {
			logger.error(e.toString(), e);
			respone.setCodeMessage(Message.STATUS_12138, Message.get(Message.STATUS_12138));
		}
		return respone;
	}
}
