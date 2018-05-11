package com.fenlibao.p2p.dao.xinwang.account;

import com.fenlibao.p2p.model.xinwang.entity.account.PlatformAccount;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/9.
 */
public interface XWAccountDao {
    void createXWAccount(XinwangAccount account) ;
    void createPTAssetAccount(Map<String,Object> params) ;
    Boolean getIdentityAuthState(Integer userId) ;
    void updatePTAccountIdentityState(Integer userId) ;
    void updatePTAccountInfo(Map<String,Object> params) ;
    PlatformAccount getPlatformAccountInfoByPlatformUserNo(String platformUserNo) ;
    PlatformAccount getPlatformAccountInfoByUserId(Integer userId) ;
    XWFundAccount getFundAccount(Integer userId, SysFundAccountType type);
    XinwangAccount getXinwangAccount(String platformUserNo);
    void updatePlatformMarketingAccount(Map<String, Object> m1);
    void updatePTAccountWLZH(Map<String, Object> m2);
    BigDecimal getPlatformMarketingAccount(Map<String, Object> n1);
    BigDecimal getPTAccountWLZH(Map<String, Object> n2);
    void updateFundAccount(Map<String,Object> params) ;
    void updateXWAccount(Map<String,Object> params);
    Integer countOverdue(Integer userId);

    /**
     * pms 解绑记录
     * @param userId
     * @param userRole
     * @return
     */
    Integer getPMSAudit(Integer userId, UserRole userRole);

    /**
     * 修改解绑成功状态
     * @param id
     */
    void updatePMSAuditStatus(Integer id);

    /**
     *  通过角色获取用户信息
     * @param userId
     * @param userRole
     * @return
     */
    XinwangAccount getXWRoleAccount(int userId, UserRole userRole);

    /**
     * 通过用户id跟账户类型获取账户id
     * @param o1
     * @return
     */
    Integer getAccountIdByUserId(Map<String, Object> o1);

    /**
     * 通过账户名称与账户类型获取账户id
     * @param paramMap
     * @return
     */
    public Integer getAccountIdByAccountNameAndaAccountType(Map<String, Object> paramMap);

    /**
     * 通过账户名称与账户类型获取userId
     * @param paramMap
     * @return
     */
    public Integer getUserIdByAccountNameAndaAccountType(Map<String, Object> paramMap);

    /**
     * 清除用户的银行卡信息
     * @param platformUserNo
     */
    void clearBankInfo(String platformUserNo);

    /**
     * 更新t6161企业审核状态
     * @param param
     */
    void updateAuditStatusToT6161(Map<String, Object> param);

    /**
     * 更新审核状态
     * @param reviewStatus
     * @param platformUserNo
     */
    void updateAuditStatus(String reviewStatus, String platformUserNo);

    /**
     * 新网账户信息表是否已存在
     * @return
     */
    Integer countXWAccount(String platformUserNo);

    /**
     * 更新新网账户信息的审核状态
     * @param account
     */
    void updateXWAccountAuditStatus(XinwangAccount account);

    /**
     * 资金账户增加
     * @param params
     */
    void updateFundAccountPlus(Map<String, Object> params);

    /**
     * 资金账户减少
     * @param params
     */
    void updateFundAccountMinus(Map<String, Object> params);
}
