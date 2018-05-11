package com.fenlibao.p2p.schedule.task.sign;

import cn.bestsign.sdk.domain.vo.params.ReceiveUser;
import cn.bestsign.sdk.integration.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.model.entity.bid.ConsumeBid;
import com.fenlibao.p2p.model.entity.bid.InvestRecords;
import com.fenlibao.p2p.model.enums.bid.AgreementSignStatusEnum;
import com.fenlibao.p2p.service.thirdparty.ShangshangqianService;
import com.fenlibao.p2p.util.api.ssq.ShangshangqianUtil;
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
 * 上上签-备灾
 * @author: kris
 * @date: 20161107
 */

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BestSignPreparednessJob extends QuartzJobBean{
	
	private static final Logger logger = LogManager.getLogger(BestSignPreparednessJob.class);

	@Resource
	ShangshangqianService shangshangqianService;
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("[上上签]开始处理签名失败数据");
		List<ConsumeBid> consumeBids=shangshangqianService.getSignFailConsumeBids();
		if(consumeBids!=null && consumeBids.size()>0){
			for (ConsumeBid consumeBid: consumeBids) {
				List<InvestRecords> investRecords = shangshangqianService.investRecords(consumeBid.getBid());
				//更新记录
				consumeBid=shangshangqianService.getSignFailConsumeBid(consumeBid.getBid());
				if(consumeBid == null)continue;
				try {
					//继续签名投资用户
					Map userSignMap = goSignbyCA(investRecords,consumeBid,false);
					//签名分利宝
					autoSignbyCA_flb(userSignMap,consumeBid,true);
					//更新记录
					shangshangqianService.updateConsumeBid(consumeBid.getBid(), AgreementSignStatusEnum.YQM);
				} catch (Exception e) {
					logger.info(String.format("[上上签]备灾计划异常(%s)", e.toString()));
					//更新记录
					shangshangqianService.updateConsumeBid(consumeBid.getBid(), AgreementSignStatusEnum.QMSB);
					e.printStackTrace();
				}
			}
		}
		logger.info(String.format("[上上签]处理签名失败数据结束(%s)",consumeBids.size()));
	}

	private static final float X_INIT=0.15f;//x默认位置
	private static final float Y_INIT=0.15f;//y默认位置
	private static final int PAGE_SIGN_NUM=21;//一页默认可签数量
	private static final int LINE_SIGN_NUM=3;//一行默认可签数量
	private static final float SIGNX_=0.24f;//签名X偏移量
	private static final float SIGNY_=0.1f;//签名y偏移量
	//签名用户
	public Map goSignbyCA(List<InvestRecords> investRecords,
							 ConsumeBid consumeBid,boolean openflag)throws Exception{
		Map userSignMap= new HashMap();//存放已签名用户
		if(investRecords != null && investRecords.size()>0) {
			String signid=consumeBid.getSignid();
			//查看合同详情
			userSignMap = getContractInfo(signid);
			float signx = X_INIT;
			float signy = Y_INIT;
			int pagenum_init = consumeBid.getInvestorPageNum();
			int index=0;
			for (InvestRecords InvestRecord : investRecords) {
				String phone= InvestRecord.getPhone();
				signx =(index%PAGE_SIGN_NUM%LINE_SIGN_NUM)*SIGNX_+X_INIT;
				signy =(float)Math.round((Y_INIT+index%PAGE_SIGN_NUM/LINE_SIGN_NUM*SIGNY_)*100)/100;
				int a=(index+1)/PAGE_SIGN_NUM;
				int pagenum=a>0?(index+1)%PAGE_SIGN_NUM==0?pagenum_init+a-1:pagenum_init+a:pagenum_init;
				index++;

				Map signmap = new HashMap();
				signmap.put("signid",signid);
				signmap.put("phone", phone);
				signmap.put("pagenum", pagenum);
				signmap.put("signx",signx);
				signmap.put("signy", signy);
				signmap.put("openflag",openflag);
				try{
					ReceiveUser user = new ReceiveUser(null, InvestRecord.getUserName(),
							InvestRecord.getPhone(), Constants.USER_TYPE.PERSONAL, Constants.CONTRACT_NEEDVIDEO.NONE, true);
					ReceiveUser[] userlist = {user};
					Map sjdsendcontractMap = new HashMap();
					sjdsendcontractMap.put("userlist",userlist);
					sjdsendcontractMap.put("signid",signid);
					//添加签名记录
					signmap.put("uid",InvestRecord.getUserId());
					signmap.put("recordId",consumeBid.getId());
					if(userSignMap.containsKey(phone)){//已存在
						if((long)userSignMap.get(phone)==0){//未签名
							logger.info("[上上签备灾],开始签名：username="+InvestRecord.getUserName()+",signmap="+ JSON.toJSONString(signmap));
							shangshangqianService.AutoSignbyCA(signmap);
						}
					}else{
						logger.info("[上上签备灾],开始追加签名参数：sjdsendcontractMap="+ JSON.toJSONString(sjdsendcontractMap));
						shangshangqianService.sjdsendcontract(sjdsendcontractMap);
						logger.info("[上上签备灾],开始签名：username="+InvestRecord.getUserName()+",signmap="+ JSON.toJSONString(signmap));
						shangshangqianService.AutoSignbyCA(signmap);
					}
				}catch (Exception e){
					//记录
					logger.info("[上上签用户签名记录],异常："+e.toString());
					shangshangqianService.recordError(signmap);
					throw e;
				}
				}
			}
			return userSignMap;
		}

	//合同详情,返回签名用户集合
	public Map getContractInfo(String signid) throws Exception{
		Map userSignMap=new HashMap();
		JSONObject result = ShangshangqianUtil.sdk.contractInfo(signid);
		JSONObject response = result.getJSONObject("response");
		JSONObject content = response.getJSONObject("content");
		JSONArray  signUserlist = content.getJSONArray("userlist");
		for (int i=0;i<signUserlist.size();i++){
			JSONObject userinfo = signUserlist.getJSONObject(i);
			JSONObject user = userinfo.getJSONObject("userinfo");
			String signtime1 = JSONObject.toJSONString(user.get("signtime"));
			JSONObject signtime =null;
			if(signtime1.length() > 2) {
				signtime =user.getJSONObject("signtime");
			}
			long time = (null==signtime?0l:(long) signtime.get("time"));
			userSignMap.put(user.get("mobile"),time);
		}
		return userSignMap;
	}

	//签名分利宝
	public void autoSignbyCA_flb(Map userSignMap,ConsumeBid consumeBid,boolean openflag)throws Exception{
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
			String signid=consumeBid.getSignid();
			Map signmap = new HashMap();
			signmap.put("signid",signid);
			signmap.put("phone",email);//企业账号用的是邮箱
			signmap.put("pagenum", consumeBid.getFlbPageNum());
			signmap.put("signx", consumeBid.getFlbSignX());
			signmap.put("signy", consumeBid.getFlbSignY());
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
						logger.info("[上上签备灾],开始分利宝签名：signmap="+ JSON.toJSONString(signmap));
						shangshangqianService.AutoSignbyCA(signmap);
					}
				}else{
					logger.info("[上上签备灾],开始追加分利宝签名参数：sjdsendcontractMap="+ JSON.toJSONString(sjdsendcontractMap));
					shangshangqianService.sjdsendcontract(sjdsendcontractMap);
					logger.info("[上上签备灾],开始分利宝签名：signmap="+ JSON.toJSONString(signmap));
					shangshangqianService.AutoSignbyCA(signmap);
				}
			}catch (Exception e){
				//记录
				signmap.put("uid",0);
				signmap.put("recordId",consumeBid.getId());
				signmap.put("phone",0);//flb的phone改为0
				shangshangqianService.recordError(signmap);
				throw e;
			}
	}

}