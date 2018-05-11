package com.fenlibao.p2p.service.bank;

import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.BankCard;
import com.fenlibao.p2p.model.vo.BankCardVO;
import com.fenlibao.p2p.model.vo.BankVO;

/**
 * Created by LouisWang on 2015/8/15.
 */
public interface BankService {

    /**
     * 创建银行卡列表返回
     * @param bankCards
     * @param containBaofoo
     * @return
     */
    List<Map<String, Object>> buildBankCardsResult(List<BankCard> bankCards, boolean containBaofoo);

    /**
     * 根据用户ID获取银行卡
     * @param userId
     * @return
     */
    List<Map<String, Object>> getBankCardsByUserId(int userId);

    /**
     * 查询添加银行卡信息列表
     * param acount
     * @return
     * @throws Exception
     */
     List<BankCardVO> getBankCars(String userId,String status) throws Exception;

    /**
     * S10.1010 SYSTEM.WITHDRAW 提现手续费 先默认都是2 元
     * T6118 交易密码
     * T6101 账户余额
     * @param userId
     * @return
     */
    Map getUserDealStatus(int userId) throws Exception;

    /**
     * 查询银行卡是否信息完整
     * @param userId
     * @return
     */
    BankCardVO checkBankCardInfo(int userId) throws Exception;

    /**
     * 查询银行卡是否信息完整
     * @param id 自增id
     * @return
     */
    Map<String,Object> getBankCardById(int id) throws Exception;
    
    /**
     * 获取状态为“启用”的银行卡号
     * @param userId
     * @return
     * @throws Exception
     */
    String getCardNo(int userId) throws Throwable;
    
    /**
     * 更新银行卡信息(更新首次绑定银行卡后剩余没有完善的信息)
     * @param bankInfo
     * @return
     * @throws Exception
     */
    int updateBankCardInfo(BankCardVO bankInfo) throws Exception;
    
    /**
     * 通过银行编码获取银行ID
     * @param code
     * @return
     */
    int getIdByCode(String code);
    
    /**
     * 是否绑定银行卡
     * @param userId
     * @param bankCardNo
     * @return
     * @throws Throwable
     */
    boolean isBindBankCard(int userId, String bankCardNo) throws Throwable;

    /**
     * 通过bankId 获取银行卡信息
     * @return
     * @throws Exception
     */
    BankVO getBank(int bankId);

    /**
     * 通过 bankNum 通过银行卡获取银行所有详细信息
     * @return
     * @throws Exception
     */
    Map getBankCardMsg(int userId,String bankNum) throws Throwable;
    
    /**
     * 判断用户是否已经绑定银行卡（只适用于用户只有一张卡）
     * @param userId
     * @return WRZ为是未绑定，YRZ、KTX为已绑定
     */
    String getBankCardBindStatus(int userId);
    
    /**
     * 更新支行信息
     * @param cityId
     * @param branchName
     * @param userId
     * @throws Exception
     */
    int updateBranchInfo(String cityCode, String branchName, int userId) throws Exception;
    
    /**
     * 通过银行卡号判断是否支持该银行
     * @param bankCode
     * @return
     */
    boolean isSupportBank(String bankCardNo) throws Throwable;
    
    /**
     * 查询银行卡信息
     * @param bankCardNo
     * @return
     * @throws Throwable
     */
    Map<String, String> queryBankCardInfo(String bankCardNo) throws Throwable;

    /**
     * 根据银行编码查询新网专用银行编码
     * @param code
     * @return
     */
    String getXWBankcode(String code);
}
