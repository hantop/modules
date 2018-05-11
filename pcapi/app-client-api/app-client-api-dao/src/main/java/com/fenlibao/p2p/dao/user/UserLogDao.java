package com.fenlibao.p2p.dao.user;

import com.fenlibao.p2p.model.entity.UserLog;

/**
 * Created by chen on 2016/8/25.
 */
public interface UserLogDao {

    /**
     * 记录用户操作日志
     * @param userLog
     * @throws Exception
     */
    void addUserLog(UserLog userLog) throws Exception;

}
