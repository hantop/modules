package com.fenlibao.p2p.service.user;

import com.fenlibao.p2p.model.enums.SpecialUserType;

import java.util.List;

/**
 * Created by zcai on 2016/8/25.
 */
public interface SpecialUserService {

    /**
     *
     * @return
     */
    List<String> getUserIds(SpecialUserType type);

    /**
     * 是否是特殊用户
     * @param userId
     * @param type
     * @return
     */
    boolean isSpecial(String userId, SpecialUserType type);

    /**
     *
     * @return
     */
    void lremSpecialUser(String userId,SpecialUserType type);

}
