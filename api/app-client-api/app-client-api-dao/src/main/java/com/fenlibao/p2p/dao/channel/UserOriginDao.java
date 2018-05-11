package com.fenlibao.p2p.dao.channel;

import com.fenlibao.p2p.model.entity.channel.UserOrigin;

/**
 * 用户来源
 *
 * Created by Administrator on 2016/12/19.
 */
public interface UserOriginDao {
    UserOrigin getUserOrigin(int userId);
}
