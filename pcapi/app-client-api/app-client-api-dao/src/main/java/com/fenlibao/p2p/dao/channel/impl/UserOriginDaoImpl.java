package com.fenlibao.p2p.dao.channel.impl;

import com.fenlibao.p2p.dao.channel.UserOriginDao;
import com.fenlibao.p2p.model.entity.channel.UserOrigin;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/12/19.
 */
@Repository
public class UserOriginDaoImpl implements UserOriginDao {
    @Resource
    private SqlSession sqlSession;
    private static final String MAPPER = "UserOriginMapper.";

    @Override
    public UserOrigin getUserOrigin(int userId) {
        return sqlSession.selectOne(MAPPER + "getUserOrigin", userId);
    }
}
