package com.fenlibao.p2p.service.common;

/**
 * @date 2017/8/22 15:44
 */
public interface LockService {
    /**
     * 锁流水和状态
     * @param requestNo
     * @param noticeStatus
     */
    void createLock(String requestNo, String status);
}
