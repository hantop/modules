package com.fenlibao.p2p.service.mp.topup.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.fenlibao.p2p.dao.mq.topup.IJfGateWayDataDao;
import com.fenlibao.p2p.model.mp.vo.topup.MobileTopUpCallbackVO;
import com.fenlibao.p2p.service.mp.topup.IGatewayDataService;

@Service
public class GatewayDataServiceImpl implements IGatewayDataService {
	
	@Resource
	private IJfGateWayDataDao jfGateWayDataDao;
	
	@Override
	public void addGatewayData(MobileTopUpCallbackVO vo){
		this.jfGateWayDataDao.addGatewayData(vo);
		
	}
	

}
