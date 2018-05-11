package com.fenlibao.p2p.schedule.task.sign;

import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.model.xinwang.entity.sign.ElectronicSignature;
import com.fenlibao.p2p.model.xinwang.enums.sign.AgreementStage;
import com.fenlibao.p2p.model.xinwang.param.errorLog.ErrorLogParam;
import com.fenlibao.p2p.service.xinwang.sign.AgreementService;
import com.fenlibao.p2p.service.xinwang.sign.ElectronicSignatureService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成合同文件
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CreateAgreementJob extends QuartzJobBean{
	
	private static final Logger logger = LogManager.getLogger(CreateAgreementJob.class);
	@Resource
	ElectronicSignatureService signatureService;
	@Resource
	AgreementService agreementService;
	@Resource
    PTCommonDao commonDao;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		/** 获取未生成电子签章的信息列表 */
		List<ElectronicSignature> list = signatureService.getListByStatus(AgreementStage.KSC);
		for (ElectronicSignature electronicSignature : list) {
			try {
				agreementService.createAgreement(electronicSignature);
			} catch (Exception ex) {
				logger.error(String.format("生成电子签章合同出错,bid = %s,exception : %s", electronicSignature.getBid(), ex.toString()));
				ErrorLogParam logParam = new ErrorLogParam("CreateAgreementJob", ex.toString());
				commonDao.insertErrorLog(logParam);
			}
		}
	}
}