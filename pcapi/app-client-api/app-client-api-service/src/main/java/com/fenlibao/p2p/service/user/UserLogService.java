package com.fenlibao.p2p.service.user;

import com.fenlibao.p2p.model.entity.UserLog;

/**
 * Created by zcai on 2016/8/25.
 */
public interface UserLogService {
    /**
     * 记录用户操作日志
     * @param userLog
     * @throws Exception
     */
    void addUserLog(UserLog userLog) throws Exception;

}
