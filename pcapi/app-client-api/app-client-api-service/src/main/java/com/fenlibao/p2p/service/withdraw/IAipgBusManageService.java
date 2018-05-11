package com.fenlibao.p2p.service.withdraw;

import java.sql.Connection;
import java.util.List;

import com.dimeng.p2p.S61.entities.T6130;
import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.p2p.S61.enums.T6130_F16;
import com.fenlibao.p2p.model.form.trade.BankCardInfo;

//copy from com.dimeng.p2p.pay.service.IAipgBusManageService
/**
 * 
 * 账号支付订单管理接口
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年8月24日]
 */
public interface IAipgBusManageService {

    /**
     * 获取用户中心用户银行卡信息
     * @param bankCardId
     * @return
     * @throws Exception 
     */
    BankCardInfo getUserBankCardInfo(Connection connection, int bankCardId)
        throws Exception;
    
    /**
     * 根据订单id获取提现银行卡id
     * @param bankCardId
     * @return
     * @throws Exception 
     */
    int getWithdrawBankCardByOrderId(Connection connection, int orderId)
        throws Exception;
    
    /**
     * <获取放款中状态的提现订单>
     * <功能详细描述>
     * @return
     */
    List<Integer> getWithdrawingList()throws Exception;
    
    /**
     * <更新提现申请状态>
     * @param orderId
     * @param status
     * @return
     */
    int updateT6130Status(Connection con, int orderId, T6130_F09 status)
        throws Exception;
    
    /**
     * <根据提现订单id获取提现申请id>
     * <p>获取并进行锁定相对应的记录
     * @return
     */
    T6130 lockWithdrawByOrderId(Connection connection, int whithdrawOrderId, T6130_F09 status)
        throws Exception;
    
    /**
     * <提现失败>
     */
    void withdrawFail(int orderId, String reason)
        throws Throwable;
    
    /**
     * <更新提现申请状态>
     * @param orderId
     * @param status
     * @return
     */
    int updateT6130Status(Connection connection, int orderId, T6130_F09 status, String reason)
        throws Exception;
    
    /**
     * <更新提现申请状态>
     * @param connection
     * @param orderId 订单id
     * @param status 状态
     * @param reason 原因
     * @param isDZ 是否到账
     * @return
     * @throws Exception
     */
    int updateT6130Status(Connection connection, int orderId, T6130_F09 status, String reason,T6130_F16 isDZ)
        throws Exception;
}
