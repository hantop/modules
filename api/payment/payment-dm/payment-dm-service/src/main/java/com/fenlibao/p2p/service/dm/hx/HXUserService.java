package com.fenlibao.p2p.service.dm.hx;

import com.fenlibao.p2p.model.dm.entity.FundAccount;
import com.fenlibao.p2p.model.dm.enums.FundAccountType;
import com.fenlibao.p2p.model.dm.vo.HXAccountInfo;

import java.util.List;

/**
 * 华兴相关用户操作
 * Created by zcai on 2016/10/9.
 */
public interface HXUserService {


    /**
     * 保存E账号
     * @param userId
     * @param eAccount
     */
    void saveEAccount(int userId, String eAccount);

    /**
     * 获取用户华兴账号信息
     * @param userId
     * @return
     */
    HXAccountInfo getAccountInfo(int userId);

    /**
     * 创建用户资金账号
     * @param accounts
     */
    void createFundAcount(List<FundAccount> accounts);

    /**
     * 获取用户资金账户
     * @param userId
     * @param accountType
     * @return
     */
    @Deprecated
    FundAccount getUserFundAccount(int userId, FundAccountType accountType);

}
