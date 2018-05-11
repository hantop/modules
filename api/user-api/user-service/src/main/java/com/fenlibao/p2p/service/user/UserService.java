package com.fenlibao.p2p.service.user;

import com.fenlibao.p2p.model.user.entity.T5020;
import com.fenlibao.p2p.model.user.entity.AssetAccount;
import com.fenlibao.p2p.model.user.entity.T6118;
import com.fenlibao.p2p.model.user.entity.UserInfoEntity;
import com.fenlibao.p2p.model.user.enums.T6101_F03;
import com.fenlibao.p2p.model.user.vo.UserBankCardVO;

import java.util.List;

/**
 * Created by zcai on 2016/10/10.
 */
public interface UserService {

    /**
     * 获取用户信息(userId or phoneNum)
     * @param userId
     * @param phoneNum
     * @return
     */
    UserInfoEntity get(Integer userId, String phoneNum);

    /**
     * 添加用户银行卡
     * @param bankCard
     * @throws Exception
     */
    void addBankCard(UserBankCardVO bankCard) throws Exception;

    /**
     * 获取用户银行卡信息
     * @param userId
     * @return
     */
    UserBankCardVO getBankCard(int userId);

    void updateBankCard(UserBankCardVO userBankCard) throws Exception;

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
     * 获取用户认证信息
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
}
