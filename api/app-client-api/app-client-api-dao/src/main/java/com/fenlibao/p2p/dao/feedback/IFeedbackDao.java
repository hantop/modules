package com.fenlibao.p2p.dao.feedback;

import com.fenlibao.p2p.model.entity.feedback.FeedbackEntity;

public interface IFeedbackDao {

	
	/**
	 * 保存反馈信息
	 * @param feedBack
	 * @return
	 * @throws Exception
	 */
	int saveFeedback(FeedbackEntity feedBack) throws Exception;
}
