package com.fenlibao.p2p.dao.pms.user;

import com.fenlibao.p2p.model.entity.pms.user.PmsUser;

/**
 * PMS用户
 *
 * Created by chenzhixuan on 2017/4/12.
 */
public interface PmsUserDao {
    PmsUser getUserByUsername(String username);
}
