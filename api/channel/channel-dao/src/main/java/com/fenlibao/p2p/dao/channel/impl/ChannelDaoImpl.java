package com.fenlibao.p2p.dao.channel.impl;

import com.fenlibao.p2p.dao.channel.ChannelDao;
import com.fenlibao.p2p.model.channel.vo.ChannelVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 渠道
 * Created by chenzhixuan on 2015/9/24.
 */
@Repository
public class ChannelDaoImpl implements ChannelDao {
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "ChannelDaoMapper.";

    @Override
    public ChannelVO getChannel(Map<String, Object> paramMap) {
        return sqlSession.selectOne(MAPPER + "getChannel", paramMap);
    }

    @Override
    public int addUserOrigin(Map<String, Object> paramMap) {
        return sqlSession.insert(MAPPER + "addUserOrigin", paramMap);
    }
}
