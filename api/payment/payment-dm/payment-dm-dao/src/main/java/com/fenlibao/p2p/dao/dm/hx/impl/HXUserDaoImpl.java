package com.fenlibao.p2p.dao.dm.hx.impl;

import com.fenlibao.p2p.dao.dm.hx.HXUserDao;
import com.fenlibao.p2p.model.dm.entity.FundAccount;
import com.fenlibao.p2p.model.dm.enums.FundAccountType;
import com.fenlibao.p2p.model.dm.vo.HXAccountInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zcai on 2016/10/9.
 */
@Repository
public class HXUserDaoImpl implements HXUserDao {

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "HXUserMapper.";


    @Override
    public void saveEAccount(int userId, String eAccount) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("userId", userId);
        params.put("eAccount", eAccount);
        sqlSession.insert(MAPPER + "saveEAccount", params);
    }

    @Override
    public HXAccountInfo getAccountInfo(int userId) {
        return sqlSession.selectOne(MAPPER + "getAccountInfo", userId);
    }

    @Override
    public void createFundAcount(List<FundAccount> accounts) {
        sqlSession.insert(MAPPER + "createFundAcount", accounts);
    }

    @Override
    public void updateAccountInfo(HXAccountInfo info) {
        sqlSession.update(MAPPER + "updateAccountInfo", info);
    }

    @Override
    public FundAccount getUserFundAccount(int userId, FundAccountType accountType) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("userId", userId);
        params.put("accountType", accountType.getCode());
        return sqlSession.selectOne(MAPPER + "getUserFundAccount", params);
    }
}
