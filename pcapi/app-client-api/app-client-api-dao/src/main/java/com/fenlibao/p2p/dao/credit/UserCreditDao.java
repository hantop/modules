package com.fenlibao.p2p.dao.credit;

import java.util.List;
import java.util.Map;

/**
 * 用户信用
 * Created by chenzhixuan on 2015/8/25.
 */
public interface UserCreditDao {
    /**
     * 新增用户信用账户(T6116)
     * @param map
     * @return
     */
    int addUserCredit(Map<String, Object> map);

    /**
     * 新增用户信用档案表(T6144)
     * @param map
     * @return
     */
    int addUserCreditArchive(Map<String, Object> map);

    /**
     * 获取信用认证项(T5123)
     * @return
     */
    List<Integer> getCreditAuthItem();

    /**
     * 新增用户认证信息(T6120)
     * @param map
     * @return
     */
    int addUserCreditAuthInfo(Map<String, Object> map);
}
