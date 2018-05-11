package com.fenlibao.p2p.schedule.task.sign;

import com.alibaba.fastjson.JSONObject;

import com.fenlibao.p2p.model.enums.user.ThirdPartyEnum2;
import com.fenlibao.p2p.model.xinwang.entity.sign.SealImage;
import com.fenlibao.p2p.model.xinwang.entity.sign.UploadImage;
import com.fenlibao.p2p.service.thirdparty.ShangshangqianRegisterService;
import com.fenlibao.p2p.service.xinwang.sign.ElectronicSignatureService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 上传图片到上上签
 * @author:bobo
 * @date: 201712
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BestSignUploadImageJob extends QuartzJobBean{
	
	private static final Logger logger = LogManager.getLogger(BestSignUploadImageJob.class);

	@Resource
	ElectronicSignatureService electronicSignatureService;

	@Resource
	ShangshangqianRegisterService shangshangqianRegisterService;
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("[上上签公章图片]开始上传");
		List<UploadImage> list = electronicSignatureService.getImageList();
		if(list!=null){
			for(UploadImage uploadImage : list) {
				//上传企业公章到上上签
				if (uploadImage.getEnterpriseStatus() == 0 && uploadImage.getEnterpriseOfficialSealCode() != null) {
					if (shangshangqianRegisterService.isRegisterThirdParty(uploadImage.getEnterpriseUserAccount(), ThirdPartyEnum2.shangshangqian.getCode())) {
						SealImage sealImage = new SealImage();
						sealImage.setPhone(uploadImage.getEnterprisePhoneNum());
						sealImage.setSealCode(uploadImage.getEnterpriseOfficialSealCode());
						sealImage.setUserAccount(uploadImage.getEnterpriseUserAccount());
						JSONObject jsonObject = null;
						try {
							jsonObject = electronicSignatureService.uploadImage(sealImage);
							Map<String, Map<String, Object>> resultMap = (Map<String, Map<String, Object>>) jsonObject.get("response");
							Map<String, Object> map = resultMap.get("info");
							Integer result = (Integer) map.get("code");
							if (result == 100000) {
								this.electronicSignatureService.updateSealStatus(uploadImage.getId(), "enterprise");
							}
						} catch (Exception e) {
							logger.info(String.format("企业公章上传到[上上签]签名失败 id:(%s)", uploadImage.getId()));
						}
					}

				}
				//上传连带担保公章图片到上上签
				if (uploadImage.getLiabilityStatus() == 0 && uploadImage.getLiabilityOfficialSealCode() != null) {
					//判断是否注册过上上签
					if (shangshangqianRegisterService.isRegisterThirdParty(uploadImage.getLiabilityUserAccount(), ThirdPartyEnum2.shangshangqian.getCode())) {
						SealImage sealImage = new SealImage();
						sealImage.setPhone(uploadImage.getLiabilityPhone());
						sealImage.setSealCode(uploadImage.getLiabilityOfficialSealCode());
						sealImage.setUserAccount(uploadImage.getLiabilityUserAccount());
						JSONObject jsonObject = null;
						try {
							jsonObject = electronicSignatureService.uploadImage(sealImage);
							Map<String, Map<String, Object>> resultMap = (Map<String, Map<String, Object>>) jsonObject.get("response");
							Map<String, Object> map = resultMap.get("info");
							Integer result = (Integer) map.get("code");
							if (result == 100000) {
								this.electronicSignatureService.updateSealStatus(uploadImage.getId(), "liability");
							}
						} catch (Exception e) {
							logger.info(String.format("连带担保公章上传到[上上签]签名失败 id:(%s)", uploadImage.getId()));
						}

					}

				}
			}
		}
		logger.info("图片上传到上上签结束");
	}

}