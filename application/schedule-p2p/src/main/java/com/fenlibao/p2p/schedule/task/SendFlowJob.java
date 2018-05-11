package com.fenlibao.p2p.schedule.task;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.model.entity.TFlowSendRecord;
import com.fenlibao.p2p.schedule.common.ThreadPoolHelper;
import com.fenlibao.p2p.service.impl.FlowSendRecordServiceImpl;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.loader.Config;

/**
 * 定时发送流量
 * 条件：指定渠道注册用户，并且绑定了银行卡
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SendFlowJob extends QuartzJobBean{

	private static final Logger logger = LogManager.getLogger(SendFlowJob.class);
	
	public SendFlowJob(){
		logger.info("---------------定时发送流量启动--------------");
	}
	
	@Resource
	private FlowSendRecordServiceImpl flowSendRecordServiceImpl;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("--------开始定时发送流量---------");
		try {
			ThreadPoolExecutor executor = ThreadPoolHelper.INSTANCE.getInstance();
			List<TFlowSendRecord>  list = flowSendRecordServiceImpl.getWaitFlowSendRecord(Config.get("telecom800.channel.code"));
			
			if(list!=null&&list.size()>0){
				logger.info("一共："+list.size()+"条");
				for(int i=0,len=list.size();i<len;i++){
					TFlowSendRecord record = list.get(i);
					try{
						logger.info("发送第"+i+"条,手机号："+record.getPhone());
						if(CommonTool.isTelecom(record.getPhone())){
							logger.info(record.getPhone()+"开始执行线程----------------");
							Runner runner = new Runner(record);
							executor.submit(runner);
						}
					}catch(Exception ex){
						logger.info(record.getPhone()+"发送流量异常:"+ex.getMessage(),ex);
					}
					
					if(i%30==0){
						Thread.sleep(3000);
					}
				}
			}
			
		} catch (Exception e) {
			logger.error("批量发送流量异常:"+e.getMessage(),e);
		}
		logger.info("--------定时发送流量结束-------------");
		
	}
	
	private void sendFlow(int userId,String phone){
		String requestNo=this.flowSendRecordServiceImpl.genRequestNo(phone);//订单流水号
		logger.info("订单号："+requestNo);
		String resultMessage=this.flowSendRecordServiceImpl.sendTeleFlow(phone,requestNo);//调用接口
		logger.info("返回结果："+resultMessage);
		JSONObject obj = JSONObject.parseObject(resultMessage);
		String code = obj.getString("result_code");//发送结果状态
		int sendStatus=1;//发送状态-第三方受理成功
		
		if(code.equals("00000")){
			logger.info(phone+"调用发送流量接口成功--------");
		}else{
			logger.info(phone+"调用发送流量接口失败,结果编码："+code);
			sendStatus=2;//第三方受理失败
		}
		
		flowSendRecordServiceImpl.addFlowSendRecord(userId, Config.get("telecom800.name"), 0, sendStatus,requestNo, code);
	}
	
	public class Runner implements Runnable {
		protected TFlowSendRecord record;
		
		public Runner(TFlowSendRecord record){
			this.record = record;	
		}
		
		@Override
		public void run() {
			if(record == null){
				return;
			}
			sendFlow(record.getUserId(),record.getPhone());
		}

	}

}
