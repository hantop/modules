package com.fenlibao.p2p.dao.financing.impl;

import com.fenlibao.p2p.dao.financing.UserFinancingDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户理财
 * Created by chenzhixuan on 2015/8/25.
 */
@Repository
public class UserFinancingDaoImpl implements UserFinancingDao {
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "UserFinancingMapper.";

    @Override
    public int addUserFinancing(Map<String, Object> map) {
        return sqlSession.insert(MAPPER + "addUserFinancing", map);
    }

    @Override
    public int addUserBestFinancing(Map<String, Object> map) {
        return sqlSession.insert(MAPPER + "addUserBestFinancing", map);
    }
}
