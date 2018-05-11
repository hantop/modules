package com.fenlibao.p2p.service.bid;

import com.dimeng.p2p.S62.entities.*;
import com.dimeng.p2p.S65.entities.T6504;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Map;

/**
 * 标管理接口服务
 * Created by LouisWang on 2015/8/17.
 */
public interface IBidDmService extends BaseDmService{
    /**
     * <查询标信息>
     * @param bidId 标id
     * @return
     * @throws Throwable
     */
    T6230 selectT6230(int bidId)  throws Throwable;

    /**
     * <查询投标订单>
     * @param orderId 标id
     * @return
     */
    T6504 selectT6504(int orderId) throws Throwable;

    /**
     * 用户主动投标订单
     *
     * @param bidId
     *            标ID
     * @param amount
     *            投标金额
     * @return 订单ID
     * @throws Throwable
     */
    public int bid(final int accountId, final int bidId, final BigDecimal amount)
            throws Throwable;

    /**
     * 用户主动投标订单 新版本
     *
     * @param bidId
     *            标ID
     * @param amount
     *            投标金额
     * @return 订单ID
     * @throws Throwable
     */
    public Map<String, String> doBid(final int bidId, final BigDecimal amount, final int accountId, String experFlg, String bidType)  throws Throwable;

    /**
     * 用户主动投标订单(传入连接),用于计划发标
     *
     * @param bidId
     *            标ID
     * @param amount
     *            投标金额
     * @return 订单ID
     * @throws Throwable
     */
    public Map<String, String> doBid(Connection connection, final int bidId, final BigDecimal amount, final int accountId, String experFlg, String bidType, boolean planFlag)  throws Throwable;

}
