package com.fenlibao.p2p.controller.noversion;

import com.fenlibao.p2p.model.entity.feedback.FeedbackEntity;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.feedback.IFeedbackService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 */
@RestController("NoContextController")
@RequestMapping("/")
public class NoContextController {

	private static final Logger logger = LogManager.getLogger(NoContextController.class);
	
	@Resource
	private IFeedbackService feedbackService;
	
	@RequestMapping(value = "feedback", method = RequestMethod.POST, headers = APIVersion.V_2_0_0)
    HttpResponse feedback(@ModelAttribute BaseRequestForm paramForm,
			@ModelAttribute FeedbackEntity feedBack) {
		logger.debug("-------------Content----------------");
		logger.debug(feedBack.getContent());
		HttpResponse respone = new HttpResponse();
		if (!paramForm.validate() || StringUtils.isBlank(feedBack.getContent())) {
			respone.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return respone;
		}
		try {
			String contact = feedBack.getContact();
			if (StringUtils.isNotBlank(contact)) {
				if (contact.length() > 32) {
					respone.setCodeMessage(ResponseCode.COMMON_TEXT_LENGTH_TOO_LONG);
					return respone;
				}
			}
			respone = feedbackService.saveFeedback(feedBack);
		} catch (Exception e) {
			logger.error(e.toString(), e);
			respone.setCodeMessage(ResponseCode.FAILURE);
		}
		return respone;
	}
}
