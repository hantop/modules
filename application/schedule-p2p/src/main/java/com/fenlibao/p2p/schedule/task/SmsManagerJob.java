package com.fenlibao.p2p.schedule.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

import com.dimeng.framework.message.sms.entity.SmsTask;
import com.fenlibao.p2p.model.business.sms.xuanwu.GsmsResponse;
import com.fenlibao.p2p.model.business.sms.xuanwu.MTPack;
import com.fenlibao.p2p.model.business.sms.xuanwu.MessageData;
import com.fenlibao.p2p.service.sms.impl.SmsExtracterServiceImpl;

/**
 * 短信定时发送任务
 * @author Administrator
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SmsManagerJob extends QuartzJobBean {

	private static final Logger logger = LogManager.getLogger(SmsManagerJob.class);
	
	@Value("#{configProperties['sms.max.count']}")
	private int maxCount;
	
	@Value("#{configProperties['sms.expires.minutes']}")
	private int expirsMinutes;
	
	@Value("#{configProperties['sms.xw.url']}")
	private String sendUrl;
	
	@Value("#{configProperties['sms.xw.username']}")
	private String xwUsername;
	
	@Value("#{configProperties['sms.xw.password']}")
	private String xwPassword;
	
	@Resource
	private SmsExtracterServiceImpl smsExtracterServiceImpl;
	
	protected final ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 50, 30, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());
	
	public SmsManagerJob(){
		logger.info("启动--------------------------启动");
	}
	
	protected transient boolean alive = true;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("send sms thread start");
		int a = 1;
		while (alive) {
			SmsTask[] smsTasks = null;
			try {
				a++;
				logger.info("******************第" + a + "批");

				smsTasks = smsExtracterServiceImpl.extract(maxCount, expirsMinutes);

				if (smsTasks != null && smsTasks.length > 0) {
					logger.info("******************短信条数:"+smsTasks.length);
					Runner runner = new Runner(smsTasks);
					executor.submit(runner);
				}
			} catch (Throwable e) {
				logger.error("开始调用发送短信服务异常："+e.getMessage(), e);
			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				alive = false;
				logger.error("alive=" + alive);
				logger.error(e.getMessage(), e);
				break;
			}
		}
	}

	public class Runner implements Runnable {
		protected SmsTask[] smsTasks;
		protected final StringBuilder sb = new StringBuilder();
		protected final String encode = "UTF-8";

		public Runner(SmsTask[] smsTasks) {
			this.smsTasks = smsTasks;
		}

		@Override
		public void run() {
			if (smsTasks == null || smsTasks.length == 0) {
				return;
			}
			try {

				for (int i = 0; i < smsTasks.length; i++) {
					try {
						logger.info("-------------第" + i + "条发送开始");
						String code = sendXWSms(smsTasks[i]);
						logger.info("-------------第" + i + "条发送结束");
						if (StringUtils.isNotEmpty(code)) {
							smsExtracterServiceImpl.mark(smsTasks[i].id, true, code);
						}
					} catch (Throwable e) {
						logger.error("发送短信中异常:"+e.getMessage(), e);
					}
				}
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
			}

		}

		public String sendXWSms(SmsTask smsTask) {

			int len = smsTask.receivers.length;
			if (len <= 0) {
				return null;
			}
			try {
				HttpHeaders headers = new HttpHeaders();
				headers.add("Accept", "application/json");
				headers.add("Content-Type", "application/json;charset=utf-8");

				ArrayList<MessageData> msgs = new ArrayList<MessageData>();

				MessageData vo = new MessageData(smsTask.receivers[0], smsTask.content);
				msgs.add(vo);

				MTPack pack = new MTPack();
				pack.setBatchID(UUID.randomUUID());
				pack.setMsgType(MTPack.MsgType.SMS);
				pack.setSendType(MTPack.SendType.GROUP);
				pack.setUsername(xwUsername);
				pack.setPassword(xwPassword);
				pack.setMsgs(msgs);
				if(smsTask.type != 0){
					pack.setBizCode(String.valueOf(smsTask.type));
				}

				HttpEntity<MTPack> entity = new HttpEntity<MTPack>(pack, headers);
				RestTemplate restTemplate = new RestTemplate();

				List messageConverters = new ArrayList();
				messageConverters.add(new SourceHttpMessageConverter());
				messageConverters.add(new FormHttpMessageConverter());
				messageConverters.add(new MappingJackson2HttpMessageConverter());
				restTemplate.setMessageConverters(messageConverters);
				logger.info(smsTask.receivers[0] + "start time:" + new Date());
				GsmsResponse result = restTemplate.postForObject(sendUrl, entity, GsmsResponse.class);
				logger.info(smsTask.receivers[0] + "end time:" + new Date());
				logger.info("*******sms ID:{},result:{},send success", smsTask.id, result.getResult());
				return String.valueOf(result.getResult());
			} catch (Exception e) {
				logger.error("******sms ID:{},send error", smsTask.id);
				logger.error(e.getMessage(), e);
				return null;
			}
		}

	}
}
