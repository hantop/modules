package com.fenlibao.p2p.service.trade.pay;

public interface BindCardService {

    /**
     * 绑卡
     * @param userId 用户id
     * @param cardNo 银行卡号（解密）
     * @param acName 用户名
     * @param bankId 银行id
     * @param reservedPhone 银行预留手机号
     * @return 是否更换了原有的银行卡
     * @throws Exception
     */
    boolean bindCard(int userId, String cardNo, String acName, Integer bankId,String reservedPhone) throws Exception;

    /**
     * 解绑银行卡
     * @param userId
     * @throws Exception
     */
    void unBindCard(int userId) throws Exception;
}
