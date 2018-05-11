package com.fenlibao.p2p.dao.user;

import com.fenlibao.p2p.model.enums.SpecialUserType;

import java.util.List;

/**
 * Created by zcai on 2016/8/25.
 */
public interface SpecialUserDao {

    List<String> getUserIds(SpecialUserType type);

}
