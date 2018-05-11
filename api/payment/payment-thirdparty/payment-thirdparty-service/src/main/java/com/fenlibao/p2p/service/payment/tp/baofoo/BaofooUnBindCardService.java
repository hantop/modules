package com.fenlibao.p2p.service.payment.tp.baofoo;

public interface BaofooUnBindCardService {
	/**
	 * 解绑银行卡
	 * @param userId 用户id
	 * @throws Exception
	 */
    void unBindCard(Integer userId) throws Exception;
    
}
