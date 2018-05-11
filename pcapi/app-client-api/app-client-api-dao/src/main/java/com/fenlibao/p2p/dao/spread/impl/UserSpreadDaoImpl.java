package com.fenlibao.p2p.dao.spread.impl;

import com.fenlibao.p2p.dao.spread.UserSpreadDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 推广奖励统计
 * Created by chenzhixuan on 2015/8/25.
 */
@Repository
public class UserSpreadDaoImpl implements UserSpreadDao {
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "UserSpreadMapper.";

    @Override
    public int addSpreadAwardStatistics(Map<String, Object> map) {
        return sqlSession.insert(MAPPER + "addSpreadAwardStatistics", map);
    }

    @Override
    public int addFirstChargeAward(Map<String, Object> map) {
        return sqlSession.insert(MAPPER + "addFirstChargeAward", map);
    }

    @Override
    public int updateSpreadAwardStatistics(Map<String, Object> map) {
        return sqlSession.update(MAPPER + "updateSpreadAwardStatistics", map);
    }

    @Override
    public int addSpreadInfo(Map<String, Object> map) {
        return sqlSession.insert(MAPPER + "addSpreadInfo", map);
    }
}
