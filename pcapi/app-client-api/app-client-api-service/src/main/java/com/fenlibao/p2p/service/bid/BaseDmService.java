package com.fenlibao.p2p.service.bid;

import com.dimeng.p2p.S61.entities.*;

import java.sql.SQLException;

/**
 *
 * 基础服务
 * @author  Created by LouisWang on 2015/8/5.
 * @version  [版本号, 2015年8月5日]
 */
public abstract interface BaseDmService {
    /**
     * 查询用户基本信息
     *
     * param connection
     * param F01
     * @return
     * @throws SQLException
     */
    T6110 selectT6110(int id, boolean isSession) throws Throwable;

    /**
     * 查询用户有多少张通过实名认证的银行卡
     * param bankCardNum
     * @return
     * @throws Throwable
     */
    int selectCountT6114(int userId) throws Throwable;

    /**
     * 查询用户的银行卡信息
     * @param F01
     * @param isSession
     * @return
     * @throws Throwable
     */
    T6114 selectT6114(int F01, boolean isSession) throws Throwable;

    /**
     * 查询用户第三方托管信息
     *
     * @param
     * @return
     * @throws Throwable
     */
    public T6119 selectT6119(int id, boolean isSession) throws Throwable;
}
