package com.fenlibao.p2p.service.salary.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import com.dimeng.p2p.S62.entities.T6232;
import com.fenlibao.p2p.dao.PublicAccessoryDao;
import com.fenlibao.p2p.dao.salary.SalaryDao;
import com.fenlibao.p2p.dao.salary.TJoinRecordDao;
import com.fenlibao.p2p.model.consts.earnings.EarningsTypeConst;
import com.fenlibao.p2p.model.entity.TJoinRecord;
import com.fenlibao.p2p.model.entity.salary.BidInfo;
import com.fenlibao.p2p.model.entity.salary.SalaryInfo;
import com.fenlibao.p2p.model.entity.salary.UserXjbBidInfo;
import com.fenlibao.p2p.model.entity.salary.UserXjbJoinRecord;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.vo.SalaryDetailVO;
import com.fenlibao.p2p.model.vo.SalaryInfoVo;
import com.fenlibao.p2p.model.vo.SalaryJoinRecordVo;
import com.fenlibao.p2p.model.vo.SalaryPlanHistoryListVO;
import com.fenlibao.p2p.model.vo.SalaryUserListVO;
import com.fenlibao.p2p.model.vo.SalaryVo;
import com.fenlibao.p2p.service.bid.BidService;
import com.fenlibao.p2p.service.salary.SalaryService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.pay.CalculateEarningsUtil;

/**
 * 薪金宝相关实现
 */
@Service
public class SalaryServiceImpl implements SalaryService {

	protected static final Logger logger=LogManager.getLogger(SalaryServiceImpl.class);
	
	@Resource
	private SalaryDao salaryDao;
	
	@Resource
	private TJoinRecordDao joinRecordDao;
	
	@Resource
	private BidService bidService;

	@Resource
	private PublicAccessoryDao publicAccessoryDao;
	@Resource
	private ITradeService tradeService;
	
	@Override
	public BigDecimal getXjbEarning(String userId, Date earnDate) {
//		Map<String, Object> map = new HashMap<>();
//		map.put("userId", userId);
//		map.put("feeTypes", new int[]{InterfaceConst.TZ_LX, InterfaceConst.TZ_WYJ, InterfaceConst.TZ_FX});
//		map.put("status", Status.YH.name());
//		map.put("userType", InterfaceConst.USER_TYPE_ZRR);
//		map.put("earnDate", earnDate);
//		return salaryDao.getUserEarnByDate(map);
		
		return tradeService.getYesterdayEarnings(Integer.valueOf(userId), EarningsTypeConst.XJB);
	}

	@Override
	public SalaryVo getCurrentSalary(String userId) {
		SalaryInfo info = new SalaryInfo();
		int xjbId=0;
		
		//获取当前投资的薪金宝计划 如果有则显示当前日期之后的最近的一期薪金宝;如果没有则显示之前最近的一期薪金宝 跟用户无关
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("proType", InterfaceConst.PRO_TYPE_XJB);
		map.put("month", InterfaceConst.XJB_TIME_LIMIT);
		map.put("currentDate", new Date());
		logger.info(map);
		info=salaryDao.getSalaryInfo(map);
		
		/*
		if(StringUtils.isEmpty(userId)){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("status", Status.TBZ.name());
			map.put("proType", InterfaceConst.PRO_TYPE_XJB);
			map.put("month", InterfaceConst.XJB_TIME_LIMIT);
			map.put("currentDate", new Date());
			info=salaryDao.getSalaryInfo(map);
			
		}else{
			Map<String,Object> joinmap = new HashMap<String,Object>();
			joinmap.put("fType", InterfaceConst.PRO_TYPE_XJB);
			joinmap.put("fUserId", userId);
			
			//用户的加入记录
			List<TJoinRecord> recordList = this.joinRecordDao.getJoinRecord(joinmap);
			TJoinRecord record = null;
			if(recordList!=null&&recordList.size()>0){
				record = recordList.get(0);
			}
			
			if(record!=null){//有加入记录
				int days = record.getfDay();//期号
				Map<String,Object> dayMap=new HashMap<String,Object>();
				dayMap.put("proType", InterfaceConst.PRO_TYPE_XJB);
				dayMap.put("days", days);
				info=salaryDao.getSalaryInfo(dayMap);
			}else{
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("status", Status.TBZ.name());
				map.put("proType", InterfaceConst.PRO_TYPE_XJB);
				map.put("month", InterfaceConst.XJB_TIME_LIMIT);
				map.put("currentDate", new Date());
				info=salaryDao.getSalaryInfo(map);
			}
		}
		
		*/
		if(null==info){
			return null;
		}
		
		xjbId=info.getId();
		
		SalaryVo vo = new SalaryVo();
		vo.setXjbId(xjbId);
		vo.setXjbTitle(info.getName());
		vo.setXjbYield(String.valueOf(info.getRate()));
		vo.setXjbTime(InterfaceConst.XJB_TIME_LIMIT);
		vo.setXjbqtSum(InterfaceConst.XJB_MIX_AMOUNT);
		vo.setInvestDay(DateUtil.getDayOfMonth(info.getFundraisDate()));
		vo.setServiceAgreement(Config.get("xjb.serviceAgreement.url"));
		if(info.getFundraisDate().getTime()< Calendar.getInstance().getTimeInMillis()){
			vo.setCanbuy(0);
		}else{
			vo.setCanbuy(1);
		}
		return vo;
	}

	@Override
	public SalaryInfoVo getSalaryInfo(int salaryId) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("proType", InterfaceConst.PRO_TYPE_XJB);
		map.put("id", salaryId);
		
		//薪金宝信息
		SalaryInfo info = salaryDao.getSalaryDetail(map);
		
		//判断bid是否存在  add by zhaohongfeng  2015-8-22 09:57:04
		if(info==null){
			return null;
		}
		
		SalaryInfoVo vo = new SalaryInfoVo();
		vo.setXjbId(info.getId());
		vo.setXjbqtSum(InterfaceConst.XJB_MIX_AMOUNT);
		vo.setXjbTitle(info.getName());
		vo.setXjbYield(String.valueOf(info.getRate()));
		vo.setXjbTime(InterfaceConst.XJB_TIME_LIMIT);
		vo.setBuyTimestamp(DateUtil.nowDate().getTime());
		if(null!=info.getFundraisDate()){
			vo.setInvestDay(DateUtil.getDayOfMonth(info.getFundraisDate()));
			vo.setInterestTimestamp(DateUtil.dateAdd(info.getFundraisDate(),1).getTime());
			vo.setEndTimestamp(DateUtil.dateAdd(DateUtil.monthAdd(info.getFundraisDate(),12),1).getTime());
			
			if(info.getFundraisDate().getTime()< Calendar.getInstance().getTimeInMillis()){
				vo.setCanbuy(0);
			}else{
				vo.setCanbuy(1);
			}
		}
		vo.setTotalSum(info.getTotalSum());
		vo.setInvestSum(info.getTotalSum().subtract(info.getLeftSum()));
		vo.setCpmxUrl(Config.get("xjb.detail.url")+info.getId());
		vo.setServiceAgreement(Config.get("xjb.serviceAgreement.url"));
		return vo;
	}

	@Override
	public List<SalaryJoinRecordVo> getSalaryJoinRecord(int salaryId, String timestamp) {
		Map<String,Object> bidmap=new HashMap<String,Object>();
		bidmap.put("proType", InterfaceConst.PRO_TYPE_XJB);
		bidmap.put("id", salaryId);
		
		//标的信息
		SalaryInfo info = salaryDao.getSalaryDetail(bidmap);
		
		//判断bid是否存在  add by zhaohongfeng  2015-8-25 18:14:26
		if(info==null){
			return null;
		}
		
		//期号
		int days=DateUtil.getDayOfMonth(info.getFundraisDate());
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("fType", InterfaceConst.PRO_TYPE_XJB);
		map.put("fDay", days);
		if (StringUtils.isNotEmpty(timestamp)) {
            map.put("fJoinTime", DateUtil.timestampToDate(Long.valueOf(timestamp)));
        }
		
		List<TJoinRecord> list = this.joinRecordDao.getJoinRecord(map);
		
		List<SalaryJoinRecordVo> voList = new ArrayList<SalaryJoinRecordVo>();
		
		for(TJoinRecord record:list){
			SalaryJoinRecordVo vo =new SalaryJoinRecordVo();
			vo.setInvestName(record.getfUserName().substring(0,1) + "**");
			vo.setInvestSum(record.getfAmount());
			vo.setTimestamp(record.getfJoinTime().getTime());
			voList.add(vo);
		}
		return voList;
	}

	@Override
	public List<SalaryPlanHistoryListVO> getXjbList(String timestamp)
	{
		List<SalaryPlanHistoryListVO> xjbHistoryList = new ArrayList<SalaryPlanHistoryListVO>();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("bidType", InterfaceConst.PRO_TYPE_XJB);
		map.put("currentTime", new Date());
		
        String filtertime = "";   
        if(timestamp!=null && !"".equals(timestamp) && !"null".equals(timestamp)){
        	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        	filtertime = sdf.format(Long.valueOf(timestamp)/1000); //前端传入的时间戳带毫秒数，除以1000
        	map.put("timestamp",filtertime);
        }
		
		List<BidInfo> xjbBidList = salaryDao.getSalaryPlanHistoryList(map);
		
		if(xjbBidList==null || xjbBidList.size() ==0){
			return xjbHistoryList;
		}
		
		for(BidInfo bid : xjbBidList){
			SalaryPlanHistoryListVO vo =new SalaryPlanHistoryListVO();
			vo.setXjbId(bid.getBidId());
			vo.setTimestamp(bid.getStopBidDay().getTime());
			if(bid.getBidStatus()!=null){
				if("TBZ".equals(bid.getBidStatus())){
					vo.setXjbPlanStatus(1);
				}else{
					vo.setXjbPlanStatus(0);
				}
			}
			vo.setXjbTitle(bid.getBidTitle());
			vo.setXjbYield(bid.getYearRate());
			vo.setXjbTime(bid.getLoandays());
			vo.setInvestDay(bid.getInvestDay());
			xjbHistoryList.add(vo);
		}
		return xjbHistoryList;
	}
	
	@Override
	public List<SalaryUserListVO> getUserXjbList(int userId, String timestamp, String investDay){
		List<SalaryUserListVO> userXjbList = new ArrayList<SalaryUserListVO>();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		
		String filtertime = "";   
        if(timestamp!=null && !"".equals(timestamp) && !"null".equals(timestamp)){
        	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        	filtertime = sdf.format(Long.valueOf(timestamp)/1000); //前端传入的时间戳带毫秒数，除以1000
        	map.put("timestamp",filtertime);
        }
		
		map.put("investDay", investDay);
		
		List<UserXjbBidInfo> userXjbBidInfoList = salaryDao.getUserXjbBidList(map);
		if(userXjbBidInfoList==null || userXjbBidInfoList.size() == 0){
			return userXjbList;
		}
		
		for(UserXjbBidInfo userXjb:userXjbBidInfoList){
			SalaryUserListVO vo = new SalaryUserListVO();
			//本月购买薪金宝的薪金宝Id
			Map<String, Object> buyIdMap=this.getXjbBid(userXjb.getId());
			int bidId=0;
			vo.setCanbuy(0);//初始化设置为0
			if(buyIdMap!=null && buyIdMap.get("status")!=null && Integer.valueOf(String.valueOf(buyIdMap.get("status"))) == 100){
				bidId = buyIdMap.get("bidId")!=null?Integer.valueOf((String.valueOf(buyIdMap.get("bidId")))):0;
				if(bidId>0){
					BidInfo bidInfo = isUserCanBid(bidId,userId);
					if(bidInfo==null){
						vo.setCanbuy(1);
					}
				}
			}
			vo.setUserXjbId(userXjb.getId());
			vo.setXjbTitle(userXjb.getBidTitle());
			vo.setXjbYield(userXjb.getYearRate());
			vo.setXjbTime(userXjb.getLoandays());
			vo.setTimestamp(userXjb.getPublishTime()==null?0:userXjb.getPublishTime().getTime());
			vo.setBuyTimestamp(userXjb.getUserJoinTime().getTime());
			vo.setEndTimestamp(userXjb.getPlanStopTime().getTime());
			
			//每月投资金额
			vo.setInvestSum(userXjb.getMonthAmount());
			Map<String,Object> mapbuybid = new HashMap<String,Object>();
			mapbuybid.put("userXjbId", userXjb.getId());
			mapbuybid.put("bidType", InterfaceConst.PRO_TYPE_XJB);
			List<UserXjbBidInfo> userBuyBidList = salaryDao.getBuyBidList(mapbuybid);
			
			BigDecimal totalSum = new BigDecimal(0);
			BigDecimal totalEaring = new BigDecimal(0);
			if(userBuyBidList!=null && userBuyBidList.size()!=0){
				for(UserXjbBidInfo userBid:userBuyBidList){
					totalSum=totalSum.add(userBid.getMonthAmount());
					totalEaring=totalEaring.add(CalculateEarningsUtil.calEarnings(InterfaceConst.YCFQ, userBid.getMonthAmount(), userBid.getYearRate(), userBid.getLoandays()));
				}
			}
			//累计投资总额
			vo.setTotalSum(totalSum);
			//购买了几个月
			vo.setInvestMonths(userBuyBidList.size());
			//已经获取的收益
			vo.setXjbEaring(totalEaring);
			//该薪金宝本月续买是否过期(0:未过期 1:过期  2:薪金宝计划完结)
			if(Calendar.getInstance().getTime().getTime() > userXjb.getPlanStopTime().getTime()){//薪金宝计划完结
				vo.setValidStatus(2);
			}else{
				//计算下次投资日
				vo.setInvestTimestamp(getUserBidNextInvestDay(bidId,userId,userXjb.getInvestDay()));
				vo.setValidStatus(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)>userXjb.getInvestDay()?1:0);
			}
			vo.setServiceAgreement(Config.get("xjb.serviceAgreement.url"));
			userXjbList.add(vo);
		}
		return userXjbList;
	}
	
	public static void main(String[] args){
		System.out.println(Calendar.getInstance().getTime().getTime());
	}
	
	@Override
	public List<SalaryUserListVO> getUserXjbInvestDayList(int userId, String investDay){
		List<SalaryUserListVO> userXjbList = new ArrayList<SalaryUserListVO>();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("investDay", investDay);
		
		//获取当前用户当前投资日的薪金宝计划投资列表
		List<UserXjbBidInfo> userXjbBidInfoList = salaryDao.getUserXjbInvestDayList(map);
		if(userXjbBidInfoList==null || userXjbBidInfoList.size() == 0){
			return userXjbList;
		}
		for(UserXjbBidInfo userXjb:userXjbBidInfoList){
			SalaryUserListVO vo = new SalaryUserListVO();
			vo.setUserXjbId(userXjb.getId());//用户薪金宝计划id
			vo.setInvestSum(userXjb.getMonthAmount());//每月投资金额
			userXjbList.add(vo);
		}
		return userXjbList;
	}
	
	
	
	/**
	 * @param  userId 当前购买的用户id
	 * @return returnMap(map.status 100表示用户未加入，200表示用户加入过，并已经有续买的标id，300表示用户加入过，但没有续买的标id)
	 * 
	 */
	@Override
	public Map<String,Object> getXjbBid(int userId, int investDay) {
		return getXjbBid(0,userId,investDay);
	}
	
	/**
	 * @param  userSalaryId 当前用户薪金宝计划id
	 * @return returnMap(map.status 100表示用户未加入，200表示用户加入过，并已经有续买的标id，300表示用户加入过，但没有续买的标id)
	 * 
	 */
	@Override
	public Map<String,Object> getXjbBid(int userSalaryId) {
		return getXjbBid(userSalaryId,0,0);
	}
	
	/**
	 * @param  userSalaryId 用户薪金宝计划id
	 * @param  userId 当前购买的用户id
	 * @param  investDay 用户薪金宝投资日
	 * @return returnMap(map.status 200表示用户未加入，100表示用户加入过，并已经有续买的标id，300表示开始下一轮回的加入；400表示用户加入过，但没有续买的标id)
	 */
	public Map<String,Object> getXjbBid(int userSalaryId, int userId, int investDay) {
		//返回值存储在returnMap
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		Map<String,Object> mapbuybid = new HashMap<String,Object>();
		if(userSalaryId>0){
			mapbuybid.put("userSalaryId", userSalaryId);
		}
		if(userId>0){
			mapbuybid.put("userId", userId);
		}
		//此处先注释掉 (如果以后用户允许同一个月购买多个薪金宝,打开开关即可)
//		if(investDay>0){
//			mapbuybid.put("investDay", investDay);
//		}
		UserXjbJoinRecord record = salaryDao.getUserXjbJoinRecord(mapbuybid);
		if(record==null){
			returnMap.put("status", 200);
			return returnMap;
		}
		
		int currentLoadDays = getCurrentLoadDays(record.getFirstJoinPlanDate(), record.getInvestDay());
		if(currentLoadDays <=0){//下一轮回的加入,续买结束
			returnMap.put("status", 300);
			return returnMap;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("currentLoadDays", currentLoadDays);
		map.put("bidType", InterfaceConst.PRO_TYPE_XJB);
		map.put("investDay", record.getInvestDay());
		BidInfo buybidInfo= salaryDao.getContinueBuyBid(map);
		
		if(buybidInfo == null || buybidInfo.getBidId() <=0){
			returnMap.put("status", 400);
			String nextDate = getNowDateNextInvestDay(record.getInvestDay());
			returnMap.put("nextDate", nextDate);
			return returnMap;
		}
		returnMap.put("status", 100);
		returnMap.put("bidId", buybidInfo.getBidId());
		if(currentLoadDays==1){
			returnMap.put("isLastOne", 1);
		}
		return returnMap;
	}
	
	/**
	 * @param joinPlanDate 第一次加入薪金宝时间
	 * @param period 加入的计划天数
	 * @return currentLoadDays 当前续买薪金宝的还款期限应为（月）
	 */
	public int getCurrentLoadDays(Date joinPlanDate, int period){
		int _day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int _month = Calendar.getInstance().get(Calendar.MONTH)+1;
		int _year = Calendar.getInstance().get(Calendar.YEAR);
        Calendar _joinPlantDate = Calendar.getInstance();
        _joinPlantDate.setTime(joinPlanDate);
		int month= _joinPlantDate.get(Calendar.MONTH)+1;
		int year = _joinPlantDate.get(Calendar.YEAR);
		
		int currentLoadDays=0;
		//计算下次投资日
		logger.info("薪金宝续买监控开始：-------------------------");
		logger.info("用户加入薪金宝计划时间："+year+"-"+month);
		logger.info("当前续买薪金宝系统时间："+_year+"-"+_month+"-"+_day);
		if(_day >= period){
			//下面的就是把当前日期加一个月
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MONTH, 1);
			
			_year = cal.get(Calendar.YEAR);
			_month = cal.get(Calendar.MONTH)+1;
			if(year >= _year){ //同一年
				currentLoadDays= 12-(_month-month);
			}else{
				currentLoadDays= 12-(_month+12-month); 
			}
		}else{
			if(year >= _year){ //同一年
				currentLoadDays= 12-(_month-month);
			}else{ 
				currentLoadDays= 12-(_month+12-month); 
			}
		}
		logger.info("当前续买薪金宝的还款期限应为（月）："+currentLoadDays);
		logger.info("薪金宝续买监控结束：-------------------------");
		return currentLoadDays;
	}
	
	@Override
	public boolean isUserHaveBid(int userId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("bidType", InterfaceConst.PRO_TYPE_XJB);
		map.put("currentDate", new Date());
		BidInfo bidIfo = salaryDao.isUserHaveBid(map);
		if(bidIfo!=null){
			return true;
		}
		return false;
	}

	@Override
	public SalaryDetailVO getSalaryDetail(int salaryId) {
		SalaryDetailVO salaryDetail = salaryDao.getSalaryDetailInfo(salaryId);
		salaryDetail.setXjbYield(salaryDetail.getXjbYield());
		if(salaryDetail!=null){
			salaryDetail.setServiceAgreement(Config.get("xjb.serviceAgreement.url"));
			
			Map<String, Object> accessoryMap = new HashMap<String, Object>();
			accessoryMap.put("bidId", salaryId);
			accessoryMap.put("accessoryType", Config.get("shop.accessory.type"));
			List<T6232> list = publicAccessoryDao.getPublicAccessory(accessoryMap);
			String[] contractUrls = new String[list.size()];
			for(int i=0;i<list.size();i++){
				T6232 accessory = list.get(i);
				String fileUrl=FileUtils.getPicURL(accessory.F04,Config.get("contact.url"));
				contractUrls[i] = fileUrl;
			}
			salaryDetail.setContractUrl(contractUrls);
		}
		return salaryDetail;
	}
	
	@Override
	public BidInfo isUserCanBid(int bidId, int userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bidId", bidId);
		map.put("userId", userId);
		BidInfo bid = salaryDao.isUserBided(map);
		if(bid!=null){
			//下面的就是把当前日期加一个月
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MONTH, 1);
			bid.setBidTitle((cal.get(Calendar.MONTH)+1)+"月"+bid.getInvestDay()+"日");
		}
		return bid;
	}
	
	/**
	 * @return 
	 * 
	 */
	private long getUserBidNextInvestDay(int bidId, int userId, int investDay){
		int _day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		Calendar nextDate = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		if(_day > investDay){//下面的就是把当前日期加一个月
			now.add(Calendar.MONTH, 1);
		}else{
			if(bidId>0){
				BidInfo bidInfo = isUserCanBid(bidId,userId);
				if(bidInfo!=null){
					//下面的就是把当前日期加一个月
					now.add(Calendar.MONTH, 1);
				}
			}
		}
		nextDate.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), investDay);
		return nextDate.getTimeInMillis();
	}
	
	
	/**
	 * @Title: getNowDateNextInvestDay 
	 * @Description: 获取薪金宝当前日期的下次投资日
	 * @param investDay
	 * @return
	 * @return: String
	 */
	public String getNowDateNextInvestDay(int investDay){
		int _day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		Calendar nextDate = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		if(_day > investDay){//下面的就是把当前日期加一个月
			now.add(Calendar.MONTH, 1);
		}			
		nextDate.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), investDay);
		int _nextMonth=nextDate.get(Calendar.MONTH)+1;
		String nextMsg = _nextMonth+"月"+investDay+"日";
		return  nextMsg;
	}
	
	@Override
	public String getBidNextInvestDay(int bidId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("bidId", bidId);
		map.put("bidType", InterfaceConst.PRO_TYPE_XJB);
		BidInfo bid = salaryDao.getBidNextInvestDay(map);
		if(bid!=null && bid.getInvestDay()!=0){
			//下面的就是把当前日期加一个月
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MONTH, 1);
			return (cal.get(Calendar.MONTH)+1)+"月"+bid.getInvestDay()+"日";
		}
		return "";
	}
	
	@Override
	public String getUserNextInvestDay(int userXjbId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userXjbId", userXjbId);
		BidInfo bid = salaryDao.getUserNextInvestDay(map);
		if(bid!=null && bid.getInvestDay()!=0){
			//下面的就是把当前日期加一个月
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MONTH, 1);
			return (cal.get(Calendar.MONTH)+1)+"月"+bid.getInvestDay()+"日";
		}
		return "";
	}
}
















