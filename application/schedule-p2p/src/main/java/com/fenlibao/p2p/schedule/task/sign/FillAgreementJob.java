package com.fenlibao.p2p.schedule.task.sign;

import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.model.xinwang.consts.XinwangConsts;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.sign.ElectronicSignature;
import com.fenlibao.p2p.model.xinwang.enums.common.PathEnum;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;
import com.fenlibao.p2p.model.xinwang.enums.sign.AgreementStage;
import com.fenlibao.p2p.model.xinwang.param.errorLog.ErrorLogParam;
import com.fenlibao.p2p.service.xinwang.sign.AgreementService;
import com.fenlibao.p2p.service.xinwang.sign.ElectronicSignatureService;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * 放款后填写合同文件
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class FillAgreementJob extends QuartzJobBean{
	
	private static final Logger logger = LogManager.getLogger(FillAgreementJob.class);
	@Resource
	ElectronicSignatureService signatureService;
	@Resource
	AgreementService agreementService;
	@Resource
    PTCommonDao commonDao;
	@Resource
	XWProjectDao projectDao;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("放款后填写合同文件.......");
		/** 获取可签名电子签章的信息列表 */
		List<ElectronicSignature> list = signatureService.getListByStatus(AgreementStage.KQM);
		for (ElectronicSignature electronicSignature : list) {
			try {
				// 检查标的状态为还款中状态
				XWProjectInfo projectInfo = projectDao.getProjectInfoById(electronicSignature.getBid());
				if (!PTProjectState.HKZ.equals(projectInfo.getState())) {
					logger.info("填写合同失败,bidId:{},不是还款中状态.......", projectInfo.getProjectNo());
					continue;
				}
				File[] files = agreementService.fillAgreement(electronicSignature);
				logger.info("填写合同成功,bidId:{}.......", projectInfo.getProjectNo());
				//应该合并成功并且保存信息后才删除
				for (File file : files) {
					FileUtils.deleteQuietly(file);
				}
			} catch (Exception ex) {
				logger.error(String.format("填写合同出错,bid = %s,exception : %s", electronicSignature.getBid(), ex.toString()));
				ErrorLogParam logParam = new ErrorLogParam("FillAgreementJob", ex.toString());
				commonDao.insertErrorLog(logParam);
			}
		}
	}
}