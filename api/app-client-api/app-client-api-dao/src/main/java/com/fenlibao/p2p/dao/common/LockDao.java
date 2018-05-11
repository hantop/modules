package com.fenlibao.p2p.dao.common;

/**
 * @date 2017/8/22 15:46
 */
public interface LockDao {
    void createLock(String requestNo, String status);
}
