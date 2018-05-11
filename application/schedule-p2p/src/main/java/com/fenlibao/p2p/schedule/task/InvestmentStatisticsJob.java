package com.fenlibao.p2p.schedule.task;

import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.bid.ThridPartyWangdaiService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 投资信息统计
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class InvestmentStatisticsJob extends QuartzJobBean {

	private static final Logger logger = LogManager.getLogger(InvestmentStatisticsJob.class);

	@Resource
	private BidInfoService bidInfoService;

	@Resource
	ThridPartyWangdaiService thridPartyWangdaiService;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("投资信息统计开始  实例ID[{}] ", context.getFireInstanceId());
//		try {
////			bidInfoService.updateBidInfo();
////			logger.info("投资信息统计 完成。。。。");
////
////			//需求：每天发标金额至少一千万，如果少于1000万，用一些历史数据替代
////			//替代算法：（1）总额金额：java random nextint（300）+ 1000 范围-发标金额
////			//（2）差额-减去标金额：随机过去某一天的标金额（随机日期、借款人小于100万），统计构造字段然后insert标（批量插入150条），投资人同理构造
////			String maxAmout = Config.get("bid_wang_dai_max_amout");
////			String minAmout = Config.get("bid_wang_dai_min_amout");
////			String perAmout = Config.get("bid_wang_dai_per_amout");
////			logger.info("网贷需求浮动金额最大值maxAmout：{},最小值minAmout:{},标准值：{}",maxAmout,minAmout,perAmout);
////			if(!StringUtils.isEmpty(maxAmout) ||!StringUtils.isEmpty(minAmout) || !StringUtils.isEmpty(perAmout)){
////				Map<String,Object> map= new HashMap<>();
////				map.put("successDate", DateUtil.dateAdd(new Date(),-1));//昨天数据，满标时间
////				BigDecimal aoumt = thridPartyWangdaiService.getBidInfoSumAmout(map);
////				logger.info("昨天标的总额：{}",aoumt);
////				BigDecimal perDecimal = new BigDecimal(perAmout).multiply(new BigDecimal(10000));
////				if(aoumt == null || perDecimal.compareTo(aoumt) > 0){//平均值高于当天标的总额，需要补充数据
////					BigDecimal balance = null;
////					try {
////						int max = Integer.parseInt(maxAmout);
////						int min = Integer.parseInt(minAmout);
////						Random rand = new Random( );
////						int per = rand.nextInt(max - min);//随机差额
////						balance = new BigDecimal(per).multiply(new BigDecimal(10000));//乘以10000
////						balance = perDecimal.subtract(aoumt==null?BigDecimal.ZERO:aoumt).add(balance);
////						logger.info("随机差额balance：{}",balance);
////					}catch (Exception e){
////						logger.info("随机差额balance异常");
////					}
////					while(balance.compareTo(BigDecimal.ZERO) > 0){//循环补充数据
////						Date date = thridPartyWangdaiService.getThridPartyFristDate(map);
////						date = randomDate(DateUtil.getDateTime(date),DateUtil.getDateTime( DateUtil.dateAdd(new Date(),-1)));
////						logger.info("查询时间date需要减去一天：{}",date);
////						map= new HashMap<>();
////						map.put("successDate", DateUtil.dateAdd(date,-1));//昨天数据，满标时间
////						List<HashMap> listbid = thridPartyWangdaiService.getThridPartyRandomDayBids(map);
////						if(listbid!=null && !listbid.isEmpty()) {
////							BigDecimal amout = null;
////							int count = 200, step = 0; // 每次处理count条数据
////							List<Integer> listIds = new ArrayList<>();
////							Iterator it = listbid.iterator();
////							while (it.hasNext()){
////								HashMap<String, Object> mapBids = (HashMap)it.next();
////								/*for (Map.Entry<String, Object> entry : mapBids.entrySet()) {
////									String key = entry.getKey();
////									Object o = entry.getValue();
////								}*/
////								amout = new BigDecimal(String.valueOf(mapBids.get("amount")));
////								if(balance.compareTo(amout) > 0 || balance.compareTo(BigDecimal.ZERO) >=0 ){//差额每次减去标总额
////									balance = balance.subtract(amout);
////								}else{
////									logger.info("补充数据入库标的ids：{}",listIds.toString());
////									listIds.add(Integer.parseInt(String.valueOf(mapBids.get("id"))));
////									bidInfoService.addBidInfoFromHistory(listIds);
////									bidInfoService.addBidInvestrecordsFromHistory(listIds);
////									break;
////								}
////								step = step+ 1;
////								listIds.add(Integer.parseInt(String.valueOf(mapBids.get("id"))));
////								if(step == count){//批量插入数据count条
////									step = 0;
////									logger.info("补充数据入库标的ids：{}",listIds.toString());
////									bidInfoService.addBidInfoFromHistory(listIds);
////									bidInfoService.addBidInvestrecordsFromHistory(listIds);
////									listIds = new ArrayList<>(count);
////								}
////							}
////						}
////					}
////				}
////			}
//		} catch (Exception e) {
//			logger.info("投资信息统计 失败。。。。");
//			logger.error(e.toString(), e);
//		}
	}

	/**
	 * 某个时间段的随机时间
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	private  Date randomDate(String beginDate, String endDate) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date start = format.parse(beginDate);// 开始日期
			Date end = format.parse(endDate);// 结束日期
			if (start.getTime() >= end.getTime()) {
				return null;
			}
			long date = random(start.getTime(), end.getTime());

			return new Date(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private  long random(long begin, long end) {
		long rtnn = begin + (long) (Math.random() * (end - begin));
		if (rtnn == begin || rtnn == end) {
			return random(begin, end);
		}
		return rtnn;
	}


}
