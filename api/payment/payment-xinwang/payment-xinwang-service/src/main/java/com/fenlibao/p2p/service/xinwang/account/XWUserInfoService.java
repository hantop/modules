package com.fenlibao.p2p.service.xinwang.account;

import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/5/15.
 */
public interface XWUserInfoService {

    /**
     * 查询用户信息
     * @param platformUserNo
     * @return
     */
    XinwangUserInfo queryUserInfo(String platformUserNo);

    /**
     * 获取用户账户信息，系统保存的，不是实际的
     * @param userId
     * @param fundAccountType
     * @return
     */
    XWFundAccount getFundAccount(int userId, SysFundAccountType fundAccountType);

    /**
     * 校验用户余额
     * @param userInfo
     * @param amount
     */
    void validateAmount(XWFundAccount userInfo, BigDecimal amount);
}
