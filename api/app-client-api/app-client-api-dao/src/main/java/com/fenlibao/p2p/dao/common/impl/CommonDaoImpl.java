package com.fenlibao.p2p.dao.common.impl;

import com.fenlibao.p2p.dao.common.CommonDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * kris [fengjunda@fenlibao.com]
 * 2017年05月26日  17:00
 */
@Repository
public class CommonDaoImpl implements CommonDao {
    @Resource
    private SqlSession sqlSession;
    private static final String MAPPER = "CommonMapper.";


    @Override
    public Map platformStatictis() {
        return sqlSession.selectOne(MAPPER + "platformStatictis");
    }
}
