package com.fenlibao.p2p.dao.user.impl;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.user.SpecialUserDao;
import com.fenlibao.p2p.model.enums.SpecialUserType;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zcai on 2016/8/25.
 */
@Repository
public class SpecialUserDaoImpl extends BaseDao implements SpecialUserDao {

    public SpecialUserDaoImpl() {
        super("SpecialUserMapper");
    }

    @Override
    public List<String> getUserIds(SpecialUserType type) {
        Map<String, Integer> param = new HashMap<>(1);
        param.put("uType", type.getCode());
        return sqlSession.selectList(MAPPER + "getUserIds", param);
    }
}
