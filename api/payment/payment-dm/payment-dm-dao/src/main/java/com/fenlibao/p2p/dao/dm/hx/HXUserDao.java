package com.fenlibao.p2p.dao.dm.hx;

import com.fenlibao.p2p.model.dm.entity.FundAccount;
import com.fenlibao.p2p.model.dm.enums.FundAccountType;
import com.fenlibao.p2p.model.dm.vo.HXAccountInfo;

import java.util.List;

/**
 * Created by zcai on 2016/10/9.
 */
public interface HXUserDao {


    /**
     * 保存E账号
     * @param userId
     * @param eAccount
     */
    void saveEAccount(int userId, String eAccount);

    /**
     * 获取用户E账户
     * @param userId
     * @return
     */
    HXAccountInfo getAccountInfo(int userId);

    /**
     * 创建用户资金账号
     * @param accounts
     */
    void createFundAcount(List<FundAccount> accounts);

    void updateAccountInfo(HXAccountInfo info);

    /**
     * 获取用户资金账户
     * @param userId
     * @param accountType
     * @return
     */
    FundAccount getUserFundAccount(int userId, FundAccountType accountType);
}
