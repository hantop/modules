package com.fenlibao.dao.pms.da.finance.replacementRecharge;

import com.fenlibao.model.pms.da.finance.ReplacementRecharge;
import com.fenlibao.model.pms.da.finance.form.ReplacementRechargeForm;
import com.fenlibao.model.pms.da.finance.vo.UserRechargeAuthVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReplacementRechargeMapper {

    /**
     * 获取平台代充值账户余额
     * @return
     */
    BigDecimal getReplacementRechargeAccountBalance();

    List<ReplacementRecharge> getReplacementRechargeList(ReplacementRechargeForm replacementRechargeForm, RowBounds bounds);

    /**
     * 获取用户基本信息
     * @param userIds
     * @return
     */
    List<Map<String, Object>> getUserBaseInfo(@Param("userIds")List<Integer> userIds);

    /**
     * 个人信息
     * @param userId
     * @param platformUserNo
     * @return
     */
    UserRechargeAuthVO getUserRechargeAuthVO(@Param("userId") Integer userId, @Param("platformUserNo") String platformUserNo);

    /**
     * 企业信息
     * @param userId
     * @param platformUserNo
     * @return
     */
    UserRechargeAuthVO getCompanyRechargeAuthVO(@Param("userId") Integer userId, @Param("platformUserNo") String platformUserNo);

    /**
     * 录入充值信息
     * @param replacementRecharge
     * @return
     */
    int saveReplacementRecharge(ReplacementRecharge replacementRecharge);

    String getRechargeUserName(int id);

    /**
     * 审核
     * @param replacementRecharge
     * @return
     */
    int updateReplacementRecharge(ReplacementRecharge replacementRecharge);

    /**
     * 获取当笔订单状态
     * @param rechargeId
     * @param xwRequestId
     * @return
     */
    String getReplacementRechargeState(@Param("rechargeId") int rechargeId, @Param("xwRequestId") int xwRequestId);

    /**
     * 获取用户id
     * @param account
     * @return
     */
    Integer getUserIdByAccount(String account);

    Integer getReplacementRechargeId(ReplacementRecharge replacementRecharge);
}
