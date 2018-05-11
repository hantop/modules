package com.fenlibao.p2p.dao.bank;

import com.fenlibao.p2p.model.entity.BankCard;
import com.fenlibao.p2p.model.vo.BankCardVO;
import com.fenlibao.p2p.model.vo.BankVO;
import com.fenlibao.p2p.model.vo.pay.PaymentLimitVO;

import java.util.List;
import java.util.Map;

/**
 * Created by LouisWang on 2015/8/15.
 */
public interface BankDao {

    /**
     * 根据用户ID获取用户银行卡
     * @param userId
     * @return
     */
    List<BankCard> getBankCardsByUserId(int userId);


    List<BankCardVO> getBankCars(int acount, String status);

    /**
     * S10.1010 SYSTEM.WITHDRAW 提现手续费 先默认都是2 元
     * T6118 交易密码
     * T6101 账户余额
     * @param userId
     * @return
     */
    Map getUserDealStatus(Integer userId) throws Exception;

    /**
     * 查询银行卡是否信息完整
     * @param userId
     * @return
     */
    BankCardVO checkBankCardInfo(Integer userId) throws Exception;

    /**
     * 查询银行卡是否信息完整
     * @param id 自增id
     * @return
     */
    Map<String,Object> getBankCardById(int id) throws Exception;
    
    /**
     * 更新银行卡信息
     * @param vo
     * @return
     * @throws Exception
     */
    int updateBankCardInfo(BankCardVO vo) throws Exception;
    
    /**
     * 通过银行编码获取银行ID
     * @param code
     * @return
     */
    int getIdByCode(String code);

    /**
     * 通过bankId 获取银行信息
     * @return
     * @throws Exception
     */
    BankVO getBank(int bankId);

    /**
     * 通过 bankNum 通过银行卡获取银行所有详细信息
     * @return
     * @throws Exception
     */
    Map getBankCardMsg(Map query) throws Throwable;
    
    /**
     * 判断用户是否已经绑定银行卡（只适用于用户只有一张卡）
     * @param userId
     * @return WRZ为未绑定，YRZ、KTX为已绑定
     */
    String getBankCardBindStatus(int userId);

    /**
     * 获取用户对应银行卡的限额信息
     * @param userId
     * @return
     */
    PaymentLimitVO getPaymentLimitByUserId(String userId);

    /**
     * 获取支持的银行编码
     * @return
     */
    List<String> getQYBankCode();
}
