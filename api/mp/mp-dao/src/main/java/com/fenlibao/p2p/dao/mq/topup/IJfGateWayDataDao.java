package com.fenlibao.p2p.dao.mq.topup;

import com.fenlibao.p2p.model.mp.vo.topup.MobileTopUpCallbackVO;

public interface IJfGateWayDataDao {

	
	void addGatewayData(MobileTopUpCallbackVO vo);
	
}
