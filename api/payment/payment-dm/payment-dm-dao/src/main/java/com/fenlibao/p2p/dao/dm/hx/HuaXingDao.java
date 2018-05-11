package com.fenlibao.p2p.dao.dm.hx;

import java.util.Map;

import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.entity.HXRepayOrder;

import java.math.BigDecimal;

/**
 * Created by zcai on 2016/10/10.
 */
public interface HuaXingDao {

    /**
     * 保存华兴回调报文
     * @param tradeCode
     * @param flowNum
     * @param message
     */
    void saveMessage(String tradeCode, String oldFlowNum, String flowNum, String message);

    /**
     * 创建订单
     * @param order
     */
    void createOrder(HXOrder order);

    /**
     * 添加客户端与订单的关系
     * @param clientType
     * @param orderId
     */
    void addOrderAndClientRelation(int clientType, int orderId);

    /**
     * 获取订单
     * @param flowNum
     * @return
     */
    HXOrder getOrderByFlowNum(String flowNum);

    /**
     * 获取订单
     * @param orderId
     * @return
     */
    HXOrder getOrderById(int orderId);
    
    /**
     * 获取订单
     * @param param
     * @return
     */
    HXOrder getOrder(Map<String,Object> param);

    /**
     * 提交订单
     * @param order
     */
    void submitOrder(HXOrder order);

    /**
     * 完订单
     * @param order
     */
    void completeOrder(HXOrder order);
    /**
     * 查询客户端类型
     * @param orderId
     * @return
     */
    Integer getClientType(int orderId);

    /**
     * 添加奖励发放记录
     * @param userId
     * @param amount
     * @param remark
     */
    int addRewardsRecord(int userId, BigDecimal amount, String remark);
    /**
     * 创建还款订单
     * @param order
     */
    void createRepayOrder(HXRepayOrder order);
    
    /**
     * 获取还款订单
     * @param orderId
     * @return
     */
    HXRepayOrder getRepayOrderById(int orderId);

    /**
     * 更新奖励发放记录状态
     * @param id
     * @param state
     * @param result 如果是失败状态保存失败的原因
     */
    void updateRewardsRecord(int id, int state, String result);
}
