package com.fenlibao.p2p.service.bank;

import com.fenlibao.p2p.model.entity.BankCard;
import com.fenlibao.p2p.model.form.BankCardQuery;
import com.fenlibao.p2p.model.vo.BankCardVO;

/**
 * 银行卡
 * @author Created by LouisWang on 2015/8/5.
 *
 */
public interface BankCardDmService {
    /**
     * 查询添加银行卡信息列表
     * param acount
     * @return
     * @throws Throwable
     */
    public abstract BankCard[] getBankCars(String userId, String status) throws Throwable;

    /**
     * 查询添加银行卡信息
     * @param cardnumber 银行卡号
     * @return
     * @throws Throwable
     */
    public abstract BankCardVO getBankCar(String cardnumber) throws Throwable;

    /**
     * 添加银行卡
     * @param query
     * @throws Throwable
     */
    public abstract int AddBankCar(BankCardQuery query) throws Throwable;
    
    /**
     * 绑定银行卡（通过充值绑定）
     * @param userId
     * @param cardNo
     * @return
     * @throws Exception
     */
    int bindBankcard(int userId, String cardNo) throws Throwable;

    /**
     * 更新银行卡的开户行信息
     * @param bankCode
     * @param userId
     * @return
     */
    public abstract int updateT6114Bank(int bankCode, int userId) throws Throwable;

    /**
     * 修改银行卡信息
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract void update(int id, BankCardQuery query) throws Throwable;

    /**
     * 修改已经停用的银行卡信息
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract void updateTY(int id, BankCardQuery query, int userId) throws Throwable;

}
