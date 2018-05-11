package com.fenlibao.p2p.dao.feedback.impl;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.feedback.IFeedbackDao;
import com.fenlibao.p2p.model.entity.feedback.FeedbackEntity;
import org.springframework.stereotype.Repository;

@Repository
public class FeedbackDaoImpl extends BaseDao implements IFeedbackDao {

    public FeedbackDaoImpl() {
        super("FeedbackMapper");
    }

    @Override
    public int saveFeedback(FeedbackEntity feedBack) throws Exception {
        return sqlSession.insert(MAPPER + "addFeedback", feedBack);
    }

}
