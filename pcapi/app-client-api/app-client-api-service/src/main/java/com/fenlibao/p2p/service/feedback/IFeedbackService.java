package com.fenlibao.p2p.service.feedback;

import com.fenlibao.p2p.model.entity.feedback.FeedbackEntity;
import com.fenlibao.p2p.model.global.HttpResponse;

public interface IFeedbackService {

	/**
	 * 保存反馈信息
	 * @param feedBack
	 * @return
	 * @throws Exception
	 */
	HttpResponse saveFeedback(FeedbackEntity feedBack) throws Exception;
}
