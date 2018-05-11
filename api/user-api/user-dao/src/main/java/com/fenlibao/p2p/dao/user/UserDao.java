package com.fenlibao.p2p.dao.user;

import java.util.List;

import com.fenlibao.p2p.model.user.entity.T5020;
import com.fenlibao.p2p.model.user.entity.AssetAccount;
import com.fenlibao.p2p.model.user.entity.T6118;
import com.fenlibao.p2p.model.user.entity.UserInfoEntity;
import com.fenlibao.p2p.model.user.enums.T6101_F03;
import com.fenlibao.p2p.model.user.vo.UserBankCardVO;

/**
 * Created by zcai on 2016/10/10.
 */
public interface UserDao {

    /**
     * 获取用户信息
     * @param userId
     * @param phoneNum
     * @return
     */
    UserInfoEntity get(Integer userId, String phoneNum);

    /**
     * 添加用户银行卡
     * @param bankCardVO
     * @throws Exception
     */
    void addBankCard(UserBankCardVO bankCardVO) throws Exception;
    
    /**
     * 修改用户银行卡信息
     * @param bankCardVO
     * @throws Exception
     */
    void updateBankCard(UserBankCardVO bankCardVO) throws Exception;

    /**
     * 获取用户银行卡信息
     * @param userId
     * @return
     */
    UserBankCardVO getBankCard(int userId);
    
    /**
     * 更新账户
     */
    void updateAccount(AssetAccount t6101);

    /**
     * 初始化资金账号类型
     * @param accounts
     */
    void initFundAccount(List<AssetAccount> accounts);

    /**
     * 获取指定类型资金账户
     * @param userId
     * @param type
     * @return
     */
    AssetAccount getFundAccount(int userId, T6101_F03 type);

    /**
     * 获取指定资金账户
     * @param account (F04)
     * @return
     */
    AssetAccount getFundAccountByF04(String account);

    /**
     * 获取用户逾期
     * @param userId
     * @return
     */
    int countOverdue(int userId);
    
    /**
     * 获取安全认证信息
     * @param userId
     * @return
     */
    T6118 getAuthInfo(int userId);
    
    /**
     * 获取银行信息
     * @param bankId
     * @param bankName
     * @return
     */
    T5020 getBank(T5020 t5020);
    
    AssetAccount getPlatformFundAccount(T6101_F03 type);
    
    int getTradePwdWrongCount(int userId);
    
    void updateTradePwdWrongCount(int userId, boolean isReset);
}
