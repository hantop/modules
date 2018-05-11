package com.fenlibao.p2p.dao.user.impl;

import com.fenlibao.p2p.dao.user.AutoReleaseCGBidDao;
import com.fenlibao.p2p.model.entity.bid.AutoReleaseCGBidInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 将录入的存管消费信贷标调用存管接口进行发布
 */
@Repository
public class AutoReleaseCGBidDaoImpl implements AutoReleaseCGBidDao {
    private static final String MAPPER = "AutoReleaseCGBidMapper.";
    @Resource
    private SqlSession sqlSession;

    @Override
    public AutoReleaseCGBidInfo getYCLBidInfo() {
        return sqlSession.selectOne(MAPPER + "getYCLBidInfo");
    }

    @Override
    public void lockYCLBidInfo(int id) {
        sqlSession.selectOne(MAPPER + "lockYCLBidInfo", id);
    }

    @Override
    public String getBidStatus(int bid) {
        return sqlSession.selectOne(MAPPER + "getBidStatus", bid);
    }

    @Override
    public void addToProductLib(int bid) {
        sqlSession.insert(MAPPER + "addToProductLib", bid);
    }

    @Override
    public void updateConsumeBidInfoStatus(Map map) {
        sqlSession.insert(MAPPER + "updateConsumeBidInfoStatus", map);
    }
}
