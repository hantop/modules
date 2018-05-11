package com.fenlibao.p2p.dao.financing;

import java.util.Map;

/**
 * 用户理财
 * Created by chenzhixuan on 2015/8/25.
 */
public interface UserFinancingDao {
    int addUserFinancing(Map<String, Object> map);

    int addUserBestFinancing(Map<String, Object> map);

}
