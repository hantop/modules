package com.fenlibao.p2p.service.experiencegold.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fenlibao.p2p.dao.SendSmsRecordDao;
import com.fenlibao.p2p.dao.SendSmsRecordExtDao;
import com.fenlibao.p2p.dao.experiencegold.IExperienceGoldDao;
import com.fenlibao.p2p.model.entity.ExperienceGoldInfo;
import com.fenlibao.p2p.model.entity.FeeType;
import com.fenlibao.p2p.model.entity.SendSmsRecord;
import com.fenlibao.p2p.model.entity.SendSmsRecordExt;
import com.fenlibao.p2p.model.entity.experiencegold.ExperienceGoldEarningsEntity;
import com.fenlibao.p2p.model.form.experiencegold.ExperienceGoldEarningsForm;
import com.fenlibao.p2p.model.form.experiencegold.UserExperienceGoldForm;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.service.experiencegold.IExperienceGoldService;
import com.fenlibao.p2p.service.funds.IFundsService;
import com.fenlibao.p2p.service.privateMessage.PrivateMessageService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Sender;

@Service
public class ExperienceGoldServiceImpl implements IExperienceGoldService {

	private static final Logger logger = LogManager.getLogger(ExperienceGoldServiceImpl.class);
	
	@Resource
	private IExperienceGoldDao experienceGoldDao;
	@Resource
	private PrivateMessageService privateMessageService;
	@Resource
	private SendSmsRecordDao sendSmsRecordDao;
	@Resource
	private SendSmsRecordExtDao sendSmsRecordExtDao;
	@Resource
	private IFundsService fundsService;

	@Override
	public Map<String, Object> getDetail(int userId, int status) {
		// TODO 获取体验金金额和数量
		Map<String,Object> paramMap = new HashMap<>();
		BigDecimal totalEarnings = this.getTotalEarnings(userId);
		BigDecimal earningsYesterday = this.getEarningsYesterday(userId);
		paramMap.put("totalEarnings",totalEarnings);
		paramMap.put("earningsYesterday",earningsYesterday);

		//获取体验金金额和数量
		Map<String,Object> dataMap = new HashMap();
		dataMap.put("userId",userId);
		dataMap.put("status",InterfaceConst.EXPERIENCEGOLD_YES_TYPE);
	//	dataMap.put("yieldStatus",InterfaceConst.EXPERIENCEGOLD_JXZ_STATUS);
		List<ExperienceGoldInfo> experienceGoldInfos = experienceGoldDao.getExperienceGolds(dataMap);
		BigDecimal totalAmount = new BigDecimal(0);
		for (ExperienceGoldInfo experiencegold : experienceGoldInfos){
			totalAmount = totalAmount.add(experiencegold.getExperienceGold());
		}

		paramMap.put("earningsYesterday",earningsYesterday.toString());
		paramMap.put("totalEarnings",totalEarnings.toString());
		paramMap.put("experiencegold",totalAmount);
		paramMap.put("count",experienceGoldInfos.size());
		return paramMap;
	}

	@Override
	public BigDecimal getTotalEarnings(int userId) {
		return this.experienceGoldDao.getTotalEarnings(userId);
	}

	@Override
	public List<ExperienceGoldEarningsForm> getEarningsList(int userId, String timestamp) {
		return this.experienceGoldDao.getEarningsList(userId, timestamp);
	}

	@Override
	public BigDecimal getEarningsYesterday(int userId) {
		return this.experienceGoldDao.getEarningsYesterday(userId);
	}

	@Transactional
	@Override
	public void initExperienceGoldEarnings(int validDate, Date earningsDate,
			ExperienceGoldEarningsEntity goldEarnings, UserExperienceGoldForm g) throws Exception {
			validDate = g.getValidDate();
			for (int i = 0; i < validDate; i++) {
				if (i == 0) {
					earningsDate = g.getBeginTime();
				}
				goldEarnings = new ExperienceGoldEarningsEntity();
				goldEarnings.setAmount(this.countDayEarnings(g.getAmount(), g.getYield()));
				goldEarnings.setEarningsDate(earningsDate);
				goldEarnings.setExpId(g.getExpId());
				goldEarnings.setUserId(g.getUserId());
				this.experienceGoldDao.insertDayEarnings(goldEarnings);
				earningsDate = DateUtil.dateAdd(earningsDate, 1);
			}
			this.experienceGoldDao.updateUserExperienceYieldStatus(g.getExpId(), InterfaceConst.EXPERIENCEGOLD_JXZ_STATUS);
	}

	/**
	 * 计算每天的收益
	 * @param experienceGold
	 * @param yield
	 * @return
	 */
	private BigDecimal countDayEarnings(BigDecimal experienceGold, BigDecimal yield) {
		return experienceGold.multiply(yield)
				.divide(new BigDecimal(100))
				.divide(new BigDecimal(365), 2, RoundingMode.FLOOR);
	}

	
	@Override
	public List<ExperienceGoldInfo> getActivityByType(int expType) {
		Map<String, Object> expParamMap = new HashMap<>();
		// 当前时间
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		// 注册返现红包
		expParamMap.put("type", expType);
		expParamMap.put("currentTime", currentTimestamp);
		return experienceGoldDao.getActivityByType(expParamMap);
	}

	@Transactional(readOnly = false)
	@Override
	public void addUserExperienceGold(List<ExperienceGoldInfo> experienceGoldInfos, String userId, String phoneNum) {
		// 消息模板
		String smsPattern = Sender.get("sms.register.expgold.content");
		// 发放体验金到用户
		for(ExperienceGoldInfo experienceGold : experienceGoldInfos) {
			// 体验金年化率
			BigDecimal expAmount = experienceGold.getYearYield();
			// 体验金金额
			BigDecimal totalAmount = experienceGold.getExperienceGold();
			// 用户红包有效时间
			Calendar calendar = buildUseExpValidTime(experienceGold.getEffectDay());
			// 红包有效期
			Timestamp validTime = new Timestamp(calendar.getTimeInMillis());
			String dateStr = DateUtil.getDateTime(validTime);
			addUserExpGold(experienceGold.getExpId(), expAmount, userId, dateStr);
			//增加体验金流水记录
			addExpGoldFunding(userId,totalAmount,experienceGold.getExpId(),InterfaceConst.EXPERIENCEGOLD_INCOME_STATUS);

			// 发送站内信
			String validTimeStr =calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
			String content = smsPattern.replace("#{amount}", totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString())
										.replace("#{validTime}", validTimeStr);
            String znxSuffixContent = Sender.get("znx.suffix.content");
			privateMessageService.sendLetter(userId, InterfaceConst.PRIVATEMESSAGE_TITLE, content+znxSuffixContent);
			// 发送短信
			sendMsg(phoneNum, content);
		}
	}

	@Override
	public List getExperienceGolds(Map<String, Object> paramMap) {
		//获取体验金列表
		List<ExperienceGoldInfo> experienceGoldInfos = experienceGoldDao.getExperienceGolds(paramMap);

		List outExperienceGoldInfos = new ArrayList();
		for (int i = 0; i < experienceGoldInfos.size(); i++) {
			Map<String,Object> expMap = new HashMap<String,Object>();
			ExperienceGoldInfo expInfo = experienceGoldInfos.get(i);
			expMap.put("goldId",expInfo.getId());
			expMap.put("type",expInfo.getExperienceType());
			expMap.put("status",paramMap.get("status"));
			expMap.put("startTime",expInfo.getStartTime());
			expMap.put("EndTime",expInfo.getEndTime());
			expMap.put("experienceGold", expInfo.getExperienceGold().intValue());
			expMap.put("yearYield", expInfo.getYearYield().doubleValue());
			expMap.put("allDay",expInfo.getEffectDay());	//投资的总天数

			//获取体验金当前投资天数
			int tmpDay = DateUtil.getDayBetweenDates(new Date(),expInfo.getEndTime());
			if(tmpDay >= 0){
				int nowDay = expInfo.getEffectDay() - tmpDay;
				expMap.put("nowDay", nowDay);	//已经投资的天数
			}else{
				expMap.put("nowDay", expInfo.getEffectDay());	//投资天数到期
			}
			outExperienceGoldInfos.add(expMap);
		}
		return outExperienceGoldInfos;
	}

	private void addExpGoldFunding(String userId, BigDecimal experienceGold,Integer expId,String operatorType) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("expId", expId);
		paramMap.put("createTime", new Timestamp(System.currentTimeMillis()));
		paramMap.put("status", operatorType);	// 类型是收入还是支出
		if(!StringUtils.isBlank(operatorType)){
			if(InterfaceConst.EXPERIENCEGOLD_INCOME_STATUS.equals(operatorType)){
				paramMap.put("income", experienceGold);	//收入
				paramMap.put("overage", experienceGold);	//余额
				paramMap.put("remark", "注册体验金发放");    //备注
			}else if(InterfaceConst.EXPERIENCEGOLD_OVERAGE_STATUS.equals(operatorType)){
				paramMap.put("overage", BigDecimal.ZERO);	//支出
				paramMap.put("expenses", experienceGold);	//支出
				paramMap.put("remark", "注册体验金体验结束");    //备注
			}
		}
		experienceGoldDao.addExpGoldFunding(paramMap);
	}

	public void addUserExpGold(int expId, BigDecimal amount, String userId, String endTime) {

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("startTime", new Timestamp(System.currentTimeMillis()));
		paramMap.put("endTime", endTime);
		paramMap.put("status", InterfaceConst.EXPERIENCEGOLD_YES_TYPE);	// 体验金状态(2.已使用)
		paramMap.put("expId", expId);
		paramMap.put("userId", userId);
		paramMap.put("amount", amount);
		paramMap.put("yieldStatus", InterfaceConst.EXPERIENCEGOLD_WJX_STATUS);	//体验金计息状态
		paramMap.put("grantStatus", 1);
		experienceGoldDao.addUserExperienceGold(paramMap);
	}

	/**
	 * 用户红包有效时间
	 * @param effectDay
	 * @return
	 */
	private Calendar buildUseExpValidTime(Integer effectDay) {
		Calendar calendar = Calendar.getInstance();
		// 用户红包有效时间为当前时间加上红包有效天数
		calendar.add(Calendar.DATE, effectDay - 1);
	//	calendar.add(Calendar.DAY_OF_MONTH, -1);	//前一天
		// 时间到23:59:59
		calendar.set(calendar.HOUR_OF_DAY, 23);
		calendar.set(calendar.MINUTE, 59);
		calendar.set(calendar.SECOND, 59);
		return calendar;
	}

	private void sendMsg(String phoneNum, String content) {
		Timestamp outTime = new Timestamp(System.currentTimeMillis() / 1000 * 1000 + 30 * 60 * 1000);//验证码过期时间30分钟
		SendSmsRecord record = new SendSmsRecord();
		record.setContent(content);
		record.setCreateTime(new Date());
		record.setStatus("W");
		record.setType(0);
		record.setOutTime(outTime);
		sendSmsRecordDao.insertSendSmsRecord(record);

		SendSmsRecordExt recordExt = new SendSmsRecordExt();
		recordExt.setId(record.getId());
		recordExt.setPhoneNum(phoneNum);
		sendSmsRecordExtDao.insertSendSmsRecordExt(recordExt);
	}

	@Transactional
	@Override
	public void endExperience(FeeType feeType, String accountType, String platformAccount, UserExperienceGoldForm g,
			BigDecimal totalEarnings, String content) throws Exception {
		totalEarnings = this.countDayEarnings(g.getAmount(), g.getYield())
				.multiply(new BigDecimal(g.getValidDate()));
		this.fundsService.transfer(g.getUserId(), totalEarnings, feeType, accountType, platformAccount); //将收益转给用户
		this.experienceGoldDao.updateUserExperienceYieldStatus(g.getExpId(), InterfaceConst.EXPERIENCEGOLD_YWC_STATUS); //更新用户体验金计息状态为“已完成”
		
		this.experienceGoldDao.updateExperienceEarningsStatus(g.getExpId(), g.getUserId()); //将到期的体验金收益状态改成已到账状态
		this.addExpGoldFunding(g.getUserId().toString(), g.getAmount(), g.getExpId(), InterfaceConst.EXPERIENCEGOLD_OVERAGE_STATUS); //插入体验金资金流水
		content = String.format(content, totalEarnings.toString()); 
		this.privateMessageService.sendLetter(g.getUserId().toString(), InterfaceConst.PRIVATEMESSAGE_TITLE, content);// 发送站内信
	}

	@Override
	public List<UserExperienceGoldForm> getExpireUserExperienceGold() {
		return experienceGoldDao.getExpireUserExperienceGold();
	}

	@Override
	public List<UserExperienceGoldForm> getUserExperienceGoldList() {
		return experienceGoldDao.getUserExperienceGoldList();
	}

}
