package com.fenlibao.dao.pms.da.finance.withdrawLimit;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.fenlibao.model.pms.da.finance.WithdrawLimit;

public interface WithdrawLimitMapper {

    List<WithdrawLimit> getWithdrawLimitList(Map<String, Object> withdrawLimitForm, RowBounds bounds);

    /**
     * 用户设置提现金额
     * @param paramMap
     * @return
     */
	int insertIntoUserWithdrawLimit(Map<String, Object> paramMap);

}
