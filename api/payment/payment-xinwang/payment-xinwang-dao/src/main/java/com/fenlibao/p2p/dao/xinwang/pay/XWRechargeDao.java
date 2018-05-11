package com.fenlibao.p2p.dao.xinwang.pay;

import java.util.Map;

/**
 * 充值请求
 *
 * @date 2017/5/23 13:50
 */
public interface XWRechargeDao {
    /**
     * 保存新网充值请求信息
     * @param param
     * @return
     */
    int insertRechargeRequest(Map<String,Object> param);
}
