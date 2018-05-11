package com.fenlibao.p2p.service.mp.topup;

import com.fenlibao.p2p.model.mp.vo.topup.MobileTopUpCallbackVO;

public interface IGatewayDataService {

	/**
	 * 添加网关数据
	 * @param vo
	 * @throws Exception
	 */
	void addGatewayData(MobileTopUpCallbackVO vo);
	
}
