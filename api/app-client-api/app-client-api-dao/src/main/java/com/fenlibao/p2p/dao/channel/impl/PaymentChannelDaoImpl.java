package com.fenlibao.p2p.dao.channel.impl;

import com.fenlibao.p2p.dao.channel.PaymentChannelDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 支付通道
 * @author Mingway.Xu
 * @date 2017/3/17 15:07
 */
@Repository
public class PaymentChannelDaoImpl implements PaymentChannelDao {
    @Resource
    private SqlSession sqlSession;
    private static final String MAPPER = "PaymentChannelMapper.";

    @Override
    public Map getBaseChannel(int id) {
        return sqlSession.selectOne(MAPPER + "getBaseChannel",id);
    }
}
