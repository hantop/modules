package com.fenlibao.service.pms.da.finance.replacementRecharge;

import com.fenlibao.model.pms.da.finance.ReplacementRecharge;
import com.fenlibao.model.pms.da.finance.form.ReplacementRechargeForm;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReplacementRechargeService {

    /**
     * 查询平台代充值账户余额
     * @return
     */
    BigDecimal getReplacementRechargeAccountBalance();

	 /**
     * 查询代充值的记录信息
     *
     * @param replacementRechargeForm
     * @param bounds
     * @return
     */
    List<ReplacementRecharge> getReplacementRechargeList(ReplacementRechargeForm replacementRechargeForm, RowBounds bounds);

    /**
     * 校验用户新网开户相关信息
     * @param userType
     * @param account
     * @param userRole
     * @return
     */
    Map<String, Object> getUserRechargeAuthVO(String userType, String account, String userRole);

    /**
     * 保存录入充值请求信息
     * @param replacementRecharge
     * @return
     */
    int saveReplacementRecharge(ReplacementRecharge replacementRecharge);

    /**
     *  校验审核与充值的是否是同一个人
     * @param id 记录Id
     * @param operator 审核经办人
     * @return
     */
    boolean checkAuditAndRechargeUser(int id, String operator);

    String audit(int flag, ReplacementRecharge replacementRecharge) throws Exception;
}
