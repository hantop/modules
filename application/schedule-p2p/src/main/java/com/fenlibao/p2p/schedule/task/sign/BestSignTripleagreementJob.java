package com.fenlibao.p2p.schedule.task.sign;

import cn.bestsign.sdk.domain.vo.params.ReceiveUser;
import cn.bestsign.sdk.integration.Constants;
import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.util.api.ssq.ShangshangqianUtil;
import com.fenlibao.p2p.model.entity.bid.Tripleagreement;
import com.fenlibao.p2p.model.enums.bid.AgreementSignStatusEnum;
import com.fenlibao.p2p.service.thirdparty.ShangshangqianService;
import com.fenlibao.p2p.util.loader.Config;
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
 * 上上签--三方合同
 * @author: kris
 * @date: 20161108
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BestSignTripleagreementJob extends QuartzJobBean{
	
	private static final Logger logger = LogManager.getLogger(BestSignTripleagreementJob.class);

	@Resource
	ShangshangqianService shangshangqianService;
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//处理未签名的合同
		autoSignTripleagreements();
		//处理签名失败的合同
		dealSignFailTripleagreements();

	}

	public void autoSignTripleagreements(){
		logger.info("[上上签三方合同]开始签名");
		List<Tripleagreement> Tripleagreements=shangshangqianService.getUnSignTripleagreements();
		if(Tripleagreements!=null && Tripleagreements.size()>0){
			for (Tripleagreement tripleagreement:Tripleagreements) {
				tripleagreement=shangshangqianService.getUnSignTripleagreement(tripleagreement.getId());
				if(tripleagreement==null)continue;
				try {//签名分利宝
					autoSignbyCA_flb(tripleagreement,true);
					//更新记录
					shangshangqianService.updateTripleagreement(tripleagreement.getId(), AgreementSignStatusEnum.YQM);
				} catch (Exception e) {
					logger.info("[上上签三方合同]签名异常(合同id={})",tripleagreement.getId());
//					logger.info(String.format("[上上签三方合同]签名异常(%s)", e.toString()));
					//更新记录
					shangshangqianService.updateTripleagreement(tripleagreement.getId(), AgreementSignStatusEnum.QMSB);
					e.printStackTrace();
				}
			}
		}
		logger.info(String.format("[上上签三方合同]签名结束(%s)",Tripleagreements.size()));
	}
	public void dealSignFailTripleagreements(){
		logger.info("[上上签-三方合同-备灾]备灾开始签名");
		List<Tripleagreement> Tripleagreements=shangshangqianService.getSignFailTripleagreements();
		if(Tripleagreements!=null && Tripleagreements.size()>0){
			for (Tripleagreement tripleagreement:Tripleagreements) {
				tripleagreement=shangshangqianService.getSignFailTripleagreement(tripleagreement.getId());
				if(tripleagreement==null)continue;
				try {
					//查看合同详情
					Map userSignMap = ShangshangqianUtil.getContractInfo(tripleagreement.getSignid());
					//签名分利宝
					autoSignbyCA_flb(userSignMap,tripleagreement,true);
					//更新记录
					shangshangqianService.updateTripleagreement(tripleagreement.getId(), AgreementSignStatusEnum.YQM);
				} catch (Exception e) {
					logger.info("[上上签三方合同-备灾]签名异常(合同id={})",tripleagreement.getId());
//					logger.info(String.format("[上上签三方合同-备灾]签名异常(%s)", e.toString()));
					//更新记录
					shangshangqianService.updateTripleagreement(tripleagreement.getId(), AgreementSignStatusEnum.QMSB);
					e.printStackTrace();
				}
			}
		}
		logger.info(String.format("[上上签三方合同-备灾]签名结束(%s)",Tripleagreements.size()));
	}

	//签名分利宝
	public void autoSignbyCA_flb(Tripleagreement tripleagreement,boolean openflag)throws Exception{
			String email =null;
			String username=null;
			String phone=null;
			if(Boolean.parseBoolean(Config.get("ssq.develop.mode"))){
				email=Config.get("ssq.sign.email_test");
				username=Config.get("ssq.sign.name");
				phone=Config.get("ssq.sign.phone_test");
			}else{
				email=Config.get("ssq.sign.email");
				username=Config.get("ssq.sign.name");
				phone=Config.get("ssq.sign.phone");
			}
			String signid=tripleagreement.getSignid();
				Map signmap = new HashMap();
				signmap.put("signid",signid);
				signmap.put("phone",email);//企业账号用的是邮箱
				signmap.put("pagenum", tripleagreement.getFlbPageNum());
				signmap.put("signx", tripleagreement.getFlbSignX());
				signmap.put("signy", tripleagreement.getFlbSignY());
				signmap.put("openflag",openflag);
				try{
					ReceiveUser user = new ReceiveUser(email, username,
							phone, Constants.USER_TYPE.ENTERPRISE, Constants.CONTRACT_NEEDVIDEO.NONE, true);
					ReceiveUser[] userlist = {user};
					Map sjdsendcontractMap = new HashMap();
					sjdsendcontractMap.put("userlist",userlist);
					sjdsendcontractMap.put("signid",signid);
					shangshangqianService.sjdsendcontract(sjdsendcontractMap);
					shangshangqianService.AutoSignbyCA(signmap);
				}catch (Exception e){
					logger.info(String.format("[上上签三方合同]签名异常,recordid(%s)",tripleagreement.getId()));
					throw e;
				}
	}


	//签名分利宝-备灾
	public void autoSignbyCA_flb(Map userSignMap,Tripleagreement tripleagreement,boolean openflag)throws Exception{
		String email =null;
		String username=null;
		String phone=null;
		if(Boolean.parseBoolean(Config.get("ssq.develop.mode"))){
			email=Config.get("ssq.sign.email_test");
			username=Config.get("ssq.sign.name");
			phone=Config.get("ssq.sign.phone_test");

		}else{
			email=Config.get("ssq.sign.email");
			username=Config.get("ssq.sign.name");
			phone=Config.get("ssq.sign.phone");
		}
		String signid=tripleagreement.getSignid();
		Map signmap = new HashMap();
		signmap.put("signid",signid);
		signmap.put("phone",email);//企业账号用的是邮箱
		signmap.put("pagenum", tripleagreement.getFlbPageNum());
		signmap.put("signx", tripleagreement.getFlbSignX());
		signmap.put("signy", tripleagreement.getFlbSignY());
		signmap.put("openflag",openflag);
		try{
			ReceiveUser user = new ReceiveUser(email, username,
					phone, Constants.USER_TYPE.ENTERPRISE, Constants.CONTRACT_NEEDVIDEO.NONE, true);
			ReceiveUser[] userlist = {user};
			Map sjdsendcontractMap = new HashMap();
			sjdsendcontractMap.put("userlist",userlist);
			sjdsendcontractMap.put("signid",signid);
			if(userSignMap.containsKey(phone)){//已存在
				if((long)userSignMap.get(phone)==0){//未签名
					logger.info("[上上签三方合同-备灾],开始分利宝签名：signmap="+ JSON.toJSONString(signmap));
					shangshangqianService.AutoSignbyCA(signmap);
				}
			}else{
				logger.info("[上上签三方合同-备灾],开始追加分利宝签名参数：sjdsendcontractMap="+ JSON.toJSONString(sjdsendcontractMap));
				shangshangqianService.sjdsendcontract(sjdsendcontractMap);
				logger.info("[上上签三方合同-备灾],开始分利宝签名：signmap="+ JSON.toJSONString(signmap));
				shangshangqianService.AutoSignbyCA(signmap);
			}
		}catch (Exception e){
			logger.info(String.format("[上上签三方合同-备灾]签名异常,recordid(%s)",tripleagreement.getId()));
			throw e;
		}
	}

}