package com.fenlibao.p2p.dao.mq.topup.impl;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.mq.topup.IJfGateWayDataDao;
import com.fenlibao.p2p.model.mp.vo.topup.MobileTopUpCallbackVO;

@Repository
public class JfGateWayDataDaoImpl implements IJfGateWayDataDao {

	@Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "JfGatewayDataMapper.";
    
	@Override
	public void addGatewayData(MobileTopUpCallbackVO vo) {
		this.sqlSession.insert(MAPPER+"addGatewayData", vo);
	}

}
