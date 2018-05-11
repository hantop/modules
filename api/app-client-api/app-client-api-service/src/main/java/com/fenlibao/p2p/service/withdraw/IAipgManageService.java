package com.fenlibao.p2p.service.withdraw;

import java.sql.Connection;
import java.util.Map;

import com.fenlibao.p2p.model.entity.pay.UserWithdrawals;

/**
 * 连连账户支付系统管理接口
 * <p> copy from com.dimeng.p2p.pay.service.IAipgManageService
 * @author yangzengcai
 * @date 2015年8月29日
 */
public interface IAipgManageService {

	/**
     * 放款、提现
     * @param aipgOrder 放款、提现订单
     * @return 
     * @return AipgRsp 实时返回结果
     * @throws Throwable 
     */
    public String loan(Map<String, String> requsetParams)throws Exception, Throwable;
    
    /**
     *  交易结果查询请求
     * @param orderId
     * @return 
     * @return
     * @throws Throwable 
     */
    public abstract  String queryTrade(String orderId) throws Throwable;
    
   
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param passed
     * @param check_reason
     * @param id
     * @return
     * @throws Throwable
     */
    public int fksb(String check_reason, int id) throws Throwable;
    
    /**
     * 获取用户提现信息
     * @param connection
     * @param id
     * @return
     * @throws Throwable
     */
    public UserWithdrawals get(Connection connection, int id)
            throws Throwable;
	
    /**
     * 提现失败回滚
     * @param withdrawApplyId 提现申请订单ID（t6130Id）
     * @throws Exception
     */
    public void withdrawRollback(int withdrawApplyId, String failureReason) throws Throwable;
}
