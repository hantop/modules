package com.fenlibao.p2p.dao.trade;

/**
 * @author Mingway.Xu
 * @date 2016/12/15 15:03
 */
public interface HXFundAccountDao {
    int queryFundAccountExist(int userId);

    int queryHXAccountExist(int userId);

    int queryActive(int userId);

    String queryEAccountNo(int userId);
}
