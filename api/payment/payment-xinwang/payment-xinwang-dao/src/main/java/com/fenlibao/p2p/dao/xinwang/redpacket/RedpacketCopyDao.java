package com.fenlibao.p2p.dao.xinwang.redpacket;


import java.util.Map;

/**
 * Created by Administrator on 2015/10/16.
 */
public interface RedpacketCopyDao {

    /**
     * 新增资金流水(转入)
     * @param paramMap
     * @return
     */
    int addTruninFundsRecord(Map<String, Object> paramMap);

    /**
     * 新增资金流水(转出)
     * @param paramMap
     * @return
     */
    int addTrunoutFundsRecord(Map<String, Object> paramMap);

}
