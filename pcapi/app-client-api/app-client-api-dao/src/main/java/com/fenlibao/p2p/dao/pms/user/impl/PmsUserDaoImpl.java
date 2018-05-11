package com.fenlibao.p2p.dao.pms.user.impl;

import com.fenlibao.p2p.dao.pms.user.PmsUserDao;
import com.fenlibao.p2p.model.entity.pms.user.PmsUser;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/4/12.
 */
@Repository
public class PmsUserDaoImpl implements PmsUserDao {
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "PmsUserMapper.";

    @Override
    public PmsUser getUserByUsername(String username) {
        return sqlSession.selectOne(MAPPER + "getUserByUsername", username);
    }
}
