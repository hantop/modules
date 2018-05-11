package com.fenlibao.p2p.service.recharge;

/**
 * @author Mingway.Xu
 * @date 2016/12/15 15:00
 */
public interface HXAccountService {
    int queryFundAccountExist(int userId);

    int queryHXAccountExist(int userId);

    int queryActive(int userId);

    String queryEAccountNo(int userId);
}
