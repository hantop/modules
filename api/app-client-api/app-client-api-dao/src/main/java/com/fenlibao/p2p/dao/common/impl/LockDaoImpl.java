package com.fenlibao.p2p.dao.common.impl;

import com.fenlibao.p2p.dao.common.LockDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2017/8/22 15:46
 */
@Repository
public class LockDaoImpl implements LockDao {
    @Resource
    private SqlSession sqlSession;
    private static final String MAPPER = "LockMapper.";

    @Override
    public void createLock(String requestNo, String status) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("requestNo", requestNo);
        params.put("status", status);
        sqlSession.insert(MAPPER + "createLock", params);
    }
}
