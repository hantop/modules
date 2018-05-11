package com.fenlibao.dao.pms.da.global;

import com.fenlibao.model.pms.da.Bank;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
public interface GlobalMapper {
    /**
     * 获取可用的银行列表
     * @return
     */
     List<Bank> getBankList();
}
