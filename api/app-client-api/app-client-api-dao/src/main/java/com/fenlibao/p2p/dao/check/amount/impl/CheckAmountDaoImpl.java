package com.fenlibao.p2p.dao.check.amount.impl;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.check.amount.CheckAmountDao;
import com.fenlibao.p2p.model.check.amount.XwAccount;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zeronx on 2017/8/31.
 */
@Repository
public class CheckAmountDaoImpl extends BaseDao implements CheckAmountDao {

    public CheckAmountDaoImpl() {
        super("CheckAmountMapper");
    }

    @Override
    public List<XwAccount> getAllXwUsers(Map<String, Object> params) {
        return sqlSession.selectList(MAPPER + "getAllXwUsers", params);
    }

    @Override
    public int saveUserAmountError(Map<String, Object> params) {
        return sqlSession.insert(MAPPER + "saveUserAmountError", params);
    }
}
