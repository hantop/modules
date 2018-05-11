package com.fenlibao.service.pms.da.biz.investplan;

import com.fenlibao.model.pms.da.global.SpecialUserType;

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
    void lremSpecialUser(String userId, SpecialUserType type);

}
