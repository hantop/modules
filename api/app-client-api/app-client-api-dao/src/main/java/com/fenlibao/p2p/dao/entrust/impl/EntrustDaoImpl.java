package com.fenlibao.p2p.dao.entrust.impl;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.entrust.EntrustDao;
import com.fenlibao.p2p.model.entity.user.UnRegUser;
import com.fenlibao.p2p.model.vo.entrust.EntrustAgreementVo;
import com.fenlibao.p2p.model.vo.entrust.EntrustUserInfoVo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zeronx on 2017/8/1.
 */
@Repository
public class EntrustDaoImpl extends BaseDao implements EntrustDao {

    public EntrustDaoImpl() {
        super("EntrustMapper");
    }

    @Override
    public List<EntrustAgreementVo> getUnSignAgreementList() {
        return sqlSession.selectList(MAPPER + "getUnSignAgreementList", null);
    }

    @Override
    public UnRegUser getUnRegUser(int userId) {
        Map<String, Integer> params = new HashMap<>(1);
        params.put("userId", userId);
        return sqlSession.selectOne(MAPPER + "getUnRegUser", params);
    }

    @Override
    public EntrustAgreementVo lockAgreement(Map map) {
        return sqlSession.selectOne(MAPPER + "lockAgreement", map);
    }

    @Override
    public EntrustUserInfoVo getEntruetUserInfoByUserId(int userId) {
        Map<String, Integer> params = new HashMap<>(1);
        params.put("userId", userId);
        return sqlSession.selectOne(MAPPER + "getEntruetUserInfoByUserId", params);
    }

    @Override
    public int updateUnSignAgreementById(Map map) {
        return sqlSession.update(MAPPER + "updateUnSignAgreementById", map);
    }

    @Override
    public List<EntrustAgreementVo> getSignFailAgreementList() {
        return sqlSession.selectList(MAPPER + "getSignFailAgreementList", null);
    }

    @Override
    public EntrustAgreementVo lockSignFailAgreement(Map map) {
        return sqlSession.selectOne(MAPPER + "lockSignFailAgreement", map);
    }
}
