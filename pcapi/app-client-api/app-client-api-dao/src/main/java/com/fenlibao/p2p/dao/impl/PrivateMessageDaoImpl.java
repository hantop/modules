package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.PrivateMessageDao;
import com.fenlibao.p2p.model.entity.PrivateMessage;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class PrivateMessageDaoImpl implements PrivateMessageDao {

    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "PrivateMessageMapper.";

    @Override
    public List<PrivateMessage> getMessagesByUserId(Map<String, Object> map, PageBounds pageBounds) {
        return sqlSession.selectList(MAPPER + "getPrivateMessage", map, pageBounds);
    }

    @Override
    public void addMessage(Map<String, Object> map) {
        sqlSession.insert(MAPPER + "addMessage", map);
    }

    @Override
    public int addMessageContent(Map<String, Object> map) {
        return sqlSession.insert(MAPPER + "addMessageContent", map);
    }

    @Override
    public int getUserMessageCount(Map<String, Object> map) {
        return sqlSession.selectOne(MAPPER + "getUnreadCount", map);
    }

    @Override
    public void updateMessageStatus(Map<String, Object> map) {
        sqlSession.update(MAPPER + "updateMessageStatus", map);
    }

}
