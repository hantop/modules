package com.fenlibao.p2p.service.feedback.impl;

import com.fenlibao.p2p.dao.feedback.IFeedbackDao;
import com.fenlibao.p2p.model.entity.feedback.FeedbackEntity;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.service.feedback.IFeedbackService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FeedbackServiceImpl implements IFeedbackService {

	@Resource
	private IFeedbackDao feedbackDao;
	
	@Override
	public HttpResponse saveFeedback(FeedbackEntity feedBack) throws Exception {
		HttpResponse response = new HttpResponse();
		int result =  feedbackDao.saveFeedback(feedBack);
		if (result < 1) {
			response.setMessage("添加意见反馈失败");
		}
		return response;
	}

	

}
