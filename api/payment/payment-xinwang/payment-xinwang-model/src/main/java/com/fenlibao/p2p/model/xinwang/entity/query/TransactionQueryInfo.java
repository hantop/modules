package com.fenlibao.p2p.model.xinwang.entity.query;

import com.fenlibao.p2p.model.xinwang.entity.common.BaseResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 单笔交易查询通用返回对象
 * @date 2017/7/4 17:26
 */
public class TransactionQueryInfo extends BaseResult implements Serializable{
    List<Map<String,Object>> records;

    public List<Map<String, Object>> getRecords() {
        return records;
    }

    public void setRecords(List<Map<String, Object>> records) {
        this.records = records;
    }
}
