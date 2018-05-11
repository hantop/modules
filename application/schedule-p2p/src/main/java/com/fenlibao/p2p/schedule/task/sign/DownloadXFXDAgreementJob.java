package com.fenlibao.p2p.schedule.task.sign;

import com.fenlibao.p2p.model.xinwang.entity.sign.SignXFXDBid;
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
 * @author zeronx on 2017/12/20 14:41.
 * @version 1.0
 */
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class DownloadXFXDAgreementJob extends QuartzJobBean {

    private static final Logger LOGGER = LogManager.getLogger(DownloadXFXDAgreementJob.class);

    // 类太多，将下载消费信贷的这个功能代码也写在了常规标service类了，
    // 如果可以的话重构另起一个类比较直观（类单一功能）
    @Autowired
    private SignNormalBidService signNormalBidService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            LOGGER.info("开始下载消费信贷标协议....");
            // 步骤1：处理新增消费信贷标(t_consume_bidinfo)没有下载文档已签名/没有签名 , 保存到 s62.sign_agreement_download
            // 如果已经签名 将 s62.sign_agreement_download.is_sign 设置1 否则为0
            List<SignXFXDBid> signXFXDBids = signNormalBidService.getXFXDBids();
            LOGGER.info("下载消费信贷标协议数量{}", signXFXDBids == null ? 0 : signXFXDBids.size());
            signNormalBidService.downloadXFXDAgreement(signXFXDBids, true);
            // 处理步骤1保存时签名状态为未签名(s62.sign_agreement_download.is_sign = 0)
            // 而现在为YQM（t_consume_bidinfo.sign_agreement_status = "YQM"） 的记录
            List<SignXFXDBid> signYqmXFXDBids = signNormalBidService.getYqmXFXDBids();
            LOGGER.info("处理下载消费信贷时【签名状态为未签名现在为YQM】消费信贷标协议数量{}", signYqmXFXDBids == null ? 0 : signYqmXFXDBids.size());
            signNormalBidService.downloadXFXDAgreement(signYqmXFXDBids, false);
        } catch (Exception e) {
            LOGGER.error("下载消费信贷标协议异常....", e);
        }
    }

}
