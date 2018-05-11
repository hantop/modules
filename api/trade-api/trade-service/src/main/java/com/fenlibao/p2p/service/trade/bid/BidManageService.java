package com.fenlibao.p2p.service.trade.bid;

import com.fenlibao.p2p.model.trade.entity.T6230;
import com.fenlibao.p2p.model.trade.entity.T6252;
import com.fenlibao.p2p.model.trade.entity.bid.T6504;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface BidManageService {
	/**
	 * 发标
	 * @param loanId 标id  t6230.F01
	 * @throws Exception
	 */
    void releaseBid(int loanId) throws Exception;
    /**
     * 预发布
     * @param loanId 标id t6230.F01
     * @param displayTime 显示时间  t6230.F35
     * @param releaseTime 发布时间  t6230.F22
     * @throws Exception
     */
    void preReleaseBid(int loanId, Timestamp displayTime,Timestamp releaseTime) throws Exception;
    /**
     * 取消预发布
     * @param loanId 标id t6230.F01
     * @throws Exception
     */
    void rollBackPreReleaseBid(int loanId) throws Exception;
    /**
     * 流标
     * @param loanId 标id t6230.F01
     * @param flowNum 流标流水号  t6501.F10
     * @throws Exception
     */
    void flowBid(int loanId,String flowNum) throws Exception;
    /**
     * 放款
     * @param loanId 标id t6230.F01
     * @param flowNum 放款流水号  t6501.F10
     * @param ptwlzhCode 平台往来账户编号 t6101.F04
     * @throws Exception
     */
    void makeALoan(int loanId,String flowNum,String ptwlzhCode) throws Exception;
    /**
     * 还款
     * @param loanId 标id t6230.F01
     * @param flowNum 放款流水号  t6501.F10
     * @param ptwlzhCode 平台往来账户编号 t6101.F04
     * @throws Exception
     */
    void repay(int loanId,String flowNum,String ptwlzhCode) throws Exception;
    /**
     * 提前还款
     * @param loanId 标id t6230.F01
     * @param flowNum 放款流水号  t6501.F10
     * @param ptwlzhCode 平台往来账户编号 t6101.F04
     * @throws Exception
     */
    void prepay(int loanId,String flowNum,String ptwlzhCode) throws Exception;
    /**
     * 投标
     * @param bidId
     * @param amount
     * @param userId
     * @throws Exception
     * @return 订单ID
     */
    int tender(int bidId, BigDecimal amount, int userId) throws Exception;

    /**
     * 获取投标订单
     * @param orderId
     * @return
     */
    T6504 getTenderOrder(int orderId);

    /**
     * 校验标的状态、可投等
     * @param bidId
     * @param amount
     * @return
     */
    T6230 process(int bidId, BigDecimal amount) throws Exception;
    /**
     * 获取还款计划
     * @param loanId 标id t6230.F01
     * @param term 期号
     * @return 
     * @throws Exception
     */
    List<T6252> getRepayList(int loanId, int term) throws Exception;
    /**
     * 获取提前还款计划
     * @param loanId 标id t6230.F01
     * @param term 期号
     * @return 
     * @throws Exception
     */
    List<T6252> getPrepayList(int loanId, int term) throws Exception;

}
