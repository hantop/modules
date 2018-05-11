package com.fenlibao.p2p.schedule.task.sign;

import com.fenlibao.p2p.model.xinwang.entity.sign.SignNormalBidInfo;
import com.fenlibao.p2p.service.xinwang.sign.SignNormalBidService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

/**
 * @author zeronx on 2017/12/13 16:31.
 * @version 1.0
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SignNormalBidJob extends QuartzJobBean {

    private static final Logger LOGGER = LogManager.getLogger(SignNormalBidJob.class);

    @Autowired
    private SignNormalBidService signNormalBidService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("开始签常规标.......");
        try {
            // status = 0
            List<SignNormalBidInfo> status0Bids = signNormalBidService.getSignBidsByStatus(0);
            LOGGER.info("开始签常规标, 需处理状态为【0：待上传到上上签】的标的数量{}.......", status0Bids == null ? 0 : status0Bids.size());
            for (SignNormalBidInfo signBidInfo : status0Bids) {
                try {
                    signNormalBidService.dealWithStatus0(signBidInfo);
                } catch (Exception e) {
                    LOGGER.error("签常规标异常，签名时处理bid:{}状态为：{}异常：{}", signBidInfo.getBid(), 0, e);
                }
            }
            // status = 1
            List<SignNormalBidInfo> status1Bids = signNormalBidService.getSignBidsByStatus(1);
            LOGGER.info("开始签常规标, 需处理状态为【1：待签名】的标的数量{}.......", status1Bids == null ? 0 : status1Bids.size());
            for (SignNormalBidInfo signBidInfo : status1Bids) {
                try {
                    // 先判断要签名的这个常规标的所有要签名的企业/用户是否都已注册/上传公章（没有则不进行签名）
                    boolean isPassed = signNormalBidService.isAllSignUserRegAndCA(signBidInfo.getBid());
                    if (isPassed) {
                        signNormalBidService.dealWithStatus1(signBidInfo);
                    } else {
                        LOGGER.warn("判断要签名的这个常规标{}的所有要签名的企业/用户是否都已注册/上传公章异常。不进行签名操作", signBidInfo.getBid());
                    }
                } catch (Exception e) {
                    LOGGER.error("签常规标异常，签名时处理bid:{}状态为：{}异常：{}", signBidInfo.getBid(), 1, e);
                }
            }
            // status = 2
            List<SignNormalBidInfo> status2Bids = signNormalBidService.getSignBidsByStatus(2);
            LOGGER.info("开始签常规标, 需处理状态为【2:待关闭合同和下载】的标的数量{}.......", status2Bids == null ? 0 : status2Bids.size());
            for (SignNormalBidInfo signBidInfo : status2Bids) {
                try {
                    signNormalBidService.dealWithStatus2(signBidInfo);
                } catch (Exception e) {
                    LOGGER.error("签常规标异常，签名时处理bid:{}状态为：{}异常：{}", signBidInfo.getBid(), 2, e);
                }
            }
        } catch (Exception e) {
            LOGGER.info("签常规标异常.......", e);
        }
    }
}
