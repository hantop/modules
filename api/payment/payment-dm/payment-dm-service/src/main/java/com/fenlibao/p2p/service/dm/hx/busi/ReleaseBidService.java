package com.fenlibao.p2p.service.dm.hx.busi;

import java.sql.Timestamp;
import java.util.Map;

import com.fenlibao.p2p.model.dm.entity.HXOrder;

public interface ReleaseBidService {
    /**
     * 发标通知
     * 没有报错算成功
     * @param loanId 借款id
     * @param pmsUserId 后台用户id
     * @param zrFlag 是否为债权转让标    0 否，1 是
     * @param refLoanNo 债权转让原标ID
     * @param OLDREQSEQ 债权转让原投标第三方交易流水号 
     * @throws Exception
     * @throws TradeException
     * @return Map code CG成功/SB失败, msg 文字信息
     */
    Map<String,String> releaseBid(int loanId,Integer pmsUserId,int zrFlag,String refLoanNo,String OLDREQSEQ) throws Exception;
    /**
     * 预发标
     * @param loanId 借款id
     * @param pmsUserId 后台用户id
     * @param zrFlag 是否为债权转让标    0 否，1 是
     * @param refLoanNo 债权转让原标ID （没有的传空字符串）
     * @param OLDREQSEQ 债权转让原投标第三方交易流水号 （没有的传空字符串）
     * @param displayTime 显示时间
     * @param releaseTime 发布时间
     * @throws Exception
     * @throws TradeException
     */
    void preReleaseBid(int loanId,Integer pmsUserId,int zrFlag,String refLoanNo,String OLDREQSEQ,Timestamp displayTime,Timestamp releaseTime) throws Exception;
    /**
     * 定时器调用，发布预发布状态而且到发布时间的标
     * @param order
     * @throws Exception
     * @throws TradeException
     */
    void releaseBid(HXOrder order) throws Exception;
	/**
	 * 定时器调用，用于在华兴同步操作成功但我方业务代码报错回滚的时候再次运行我方代码
	 * @param order
	 * @throws Exception
	 * @throws TradeException
	 */
	void reProcess(HXOrder order) throws Exception;
}
