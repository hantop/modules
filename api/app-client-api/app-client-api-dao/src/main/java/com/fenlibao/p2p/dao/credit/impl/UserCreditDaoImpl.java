package com.fenlibao.p2p.dao.credit.impl;

import com.fenlibao.p2p.dao.credit.UserCreditDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 用户信用
 * Created by chenzhixuan on 2015/8/25.
 */
@Repository
public class UserCreditDaoImpl implements UserCreditDao {
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "UserCreditMapper.";

    @Override
    public int addUserCredit(Map<String, Object> map) {
        return sqlSession.insert(MAPPER + "addUserCredit", map);
    }

    @Override
    public int addUserCreditArchive(Map<String, Object> map) {
        return sqlSession.insert(MAPPER + "addUserCreditArchive", map);
    }

    @Override
    public List<Integer> getCreditAuthItem() {
        return sqlSession.selectList(MAPPER + "getCreditAuthItem");
    }

    @Override
    public int addUserCreditAuthInfo(Map<String, Object> map) {
        return sqlSession.insert(MAPPER + "addUserCreditAuthInfo", map);
    }
}
