package com.fenlibao.p2p.service.bid;

import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.p2p.S61.enums.T6114_F10;
import com.fenlibao.p2p.model.form.BankCardForm;
import com.fenlibao.p2p.model.form.BankCardQuery;

import java.sql.SQLException;

/**
 * Created by LouisWang on 2015/8/17.
 */
public interface IUserDmService extends BaseDmService {
    /**
     * 查询用户身份证信息
     *
     * @param
     * @return
     * @throws Throwable
     */
    public T6141 selectT6141(int id, boolean isSession) throws Throwable;

    /**
     * 根据第三方账号更新银行卡可用
     * @param status 状态
     * @param auth 认证
     * @param threeAccNo  第三方账号
     * @return
     */
    int updateT6114Status(T6114_F08 status, T6114_F10 auth, String threeAccNo) throws SQLException;

    /**
     * 更新银行卡可用
     * @param status
     * @param auth
     * @return
     */
    int updateT6114Status(T6114_F08 status, T6114_F10 auth, int userId) throws SQLException;

    /**
     * 插入银行卡
     * param t6114
     * @param f08
     * @param status
     */
    int insertT6114(int userId, BankCardForm bankCardForm, T6114_F08 f08, T6114_F10 status) throws Throwable;

    /**
     * 根据银行卡号查询银行卡
     * @param bankCardNum
     * @return
     * @throws Throwable
     */
    T6114 selectT6114(String bankCardNum) throws Throwable;

    /**
     *
     * 描述:查询用户的托管账号信息
     * 作者:wangshaohua
     * 创建时间：2015年3月13日
     * @param userId
     * @return
     * @throws Throwable
     */
    public T6119 selectT6119(int userId)throws Throwable;


    /**
     * 更新银行卡的开户行信息2
     * @param query
     * @param userId
     * @return
     */
    int updateT6114BankAdr(BankCardQuery query, int userId) throws SQLException;

    /**
     * 查询用户基本信息
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract T6110 getUserInfo(int userId)throws Throwable;

    /**
     * 是否实名认证
     * @return
     * @throws Throwable
     */
    public abstract boolean isSmrz(int userId) throws Throwable;
}
