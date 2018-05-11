package com.fenlibao.service.pms.da.finance.withdrawLimit;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.fenlibao.model.pms.da.finance.WithdrawLimit;
import com.fenlibao.model.pms.da.finance.form.WithdrawLimitForm;

public interface WithdrawLimitService {

	 /**
     * 查询用户设置提现金额的记录信息
     *
     * @param replacePhoneForm
     * @param bounds
     * @return
     */
    List<WithdrawLimit> getWithdrawLimitList(WithdrawLimitForm withdrawLimitForm, RowBounds bounds);

   /**
    * 用户设置提现金额
    * @param userId
    * @param limitMoney
    * @param remark
    * @return
    */
	int insertIntoUserWithdrawLimit(int userId, BigDecimal limitMoney, String remark);
}
