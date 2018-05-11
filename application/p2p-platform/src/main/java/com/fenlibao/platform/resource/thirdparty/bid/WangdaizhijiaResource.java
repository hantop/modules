package com.fenlibao.platform.resource.thirdparty.bid;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.alibaba.fastjson.JSON;
import com.fenlibao.platform.common.util.DateUtil;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInfoEntity;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInvestrecordsEntity;
import com.fenlibao.platform.model.thirdparty.vo.bid.BidInfoPreVoWDZJ;
import com.fenlibao.platform.model.thirdparty.vo.bid.BidInfoVoWDZJ;
import com.fenlibao.platform.model.thirdparty.vo.bid.BidInvestrecordsVoWDZJ;
import com.fenlibao.platform.resource.ParentResource;
import com.fenlibao.platform.service.RedisService;
import com.fenlibao.platform.service.thirdparty.BidInfoService;
import com.fenlibao.platform.service.thirdparty.TPUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * 网贷之家
 * @author junda.feng
 * @date 2016年6月1日
 */
@Path("tp/wdzj")
public class WangdaizhijiaResource extends ParentResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(WangdaizhijiaResource.class);

	@Inject
	private BidInfoService bidInfoService;
	@Inject
	private TPUserService userService;
//	@Inject
//	private RedisService redisService;

	/*private List<String> dates = Arrays.asList(
			"2017-09-05 00:00:00",
			"2017-09-07 00:00:00",
			"2017-09-10 00:00:00",
			"2017-09-15 00:00:00",
			"2017-09-22 00:00:00",
			"2017-09-23 00:00:00",
			"2017-09-24 00:00:00",
			"2017-10-09 00:00:00",
			"2017-10-14 00:00:00",
			"2017-10-22 00:00:00",
			"2017-10-27 00:00:00",
			"2017-10-31 00:00:00",
			"2017-11-04 00:00:00",
			"2017-11-08 00:00:00",
			"2017-11-14 00:00:00",
			"2017-11-20 00:00:00",
			"2017-11-25 00:00:00",
			"2017-11-27 00:00:00"
	);
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	private String lockIndexKey = "platform_lock_wdzj_index_key_num";
	private String lockDateKey = "platform_lock_wdzj_date_key";*/
    /**
     * 借款标信息list
     * @throws Throwable 
     */
    @GET
	@Path("bidInfo/list")
	@Produces(MediaType.APPLICATION_JSON)
	public String getBidInfoListWangdaizhijia(@QueryParam("page")Integer page, @QueryParam("pageSize")Integer pageSize
			,@QueryParam("date")String date, @QueryParam("token")String token, @Context HttpServletRequest request) throws Throwable {
    	
    	Map<String, Object> response = new LinkedHashMap<>();
    	//校验
    	if(!userService.validateToken(token, request.getRequestURI())){
    		response=failure();
    		return jackson(response);
    	}
    	if(pageSize==null)pageSize=20;
    	if(page==null)page=1;
		/*String tempDate = "2017-11-04 00:00:00";
		Random random = new Random();
		int index = 0;
		if (page == 1) {
			try(Jedis jedis = redisService.getInstance_10()) {
				String strIndex = jedis.get(lockIndexKey);
				if (StringUtils.isNotEmpty(strIndex)) {
					index = Integer.parseInt(strIndex) + 1;
					if (index >= dates.size()) {
						index = 0;
					}
				}
				jedis.set(lockIndexKey, index + "");
				tempDate = dates.get(index);
				jedis.set(lockDateKey, tempDate);
			}
		} else {
			try (Jedis jedis = redisService.getInstance_10()) {
				tempDate = jedis.get(lockDateKey);
			}
		}*/
    	Map<String,Object> map= new HashMap<>();
    	//map.put("bidState", "success");//只要满标
    	/*if(tempDate != null){
    		map.put("successDate", DateUtil.StringToDate(tempDate, "yyyy-MM-dd"));//满标时间
    	}*/
		if(date != null){
			map.put("successDate", DateUtil.StringToDate(date, "yyyy-MM-dd"));//满标时间
		}
    	List<BidInfoEntity> list=bidInfoService.getBidInfoList(page, pageSize,map);
    	List<BidInfoVoWDZJ> borrowList=new ArrayList<BidInfoVoWDZJ>();
		LOGGER.info("获取网贷之家的数据：{}", JSON.toJSONString(list));
    	if(list!=null){
    		for (BidInfoEntity bid : list) {
    			BidInfoVoWDZJ wdzj=new BidInfoVoWDZJ(bid);
    			if(bid.getVoteAmount().compareTo(new BigDecimal(100))>0){
    				wdzj.setAmount(bid.getAmount().subtract(bid.getVoteAmount()).doubleValue());
    			}
    			/*wdzj.setAmount(wdzj.getAmount() + random.nextInt(200));
    			wdzj.setProjectId((90000000 + Integer.parseInt(wdzj.getProjectId())) + "");
    			String formatTime = dealWithTime(date, wdzj.getSuccessTime());
    			wdzj.setSuccessTime(formatTime);
				formatTime = dealWithTime(date, wdzj.getPublishTime());
    			wdzj.setPublishTime(formatTime);*/
    			wdzj.setSchedule("100");
    			wdzj.setInterestRate(wdzj.getInterestRate()+"%");
    			wdzj.setType(bid.getTypeName()==null?"":bid.getTypeName().replace(",", ";"));
    			List<BidInvestrecordsEntity> subscribesList=bidInfoService.getBidInvestrecordsList(1, Integer.MAX_VALUE,bid.getProjectId());
    			List<BidInvestrecordsVoWDZJ> subscribes=new ArrayList<BidInvestrecordsVoWDZJ>();
    			if(subscribesList!=null && subscribesList.size()>0){
    				for (BidInvestrecordsEntity records : subscribesList) {
    					BidInvestrecordsVoWDZJ recordsWDZJ=new BidInvestrecordsVoWDZJ(records);
						/*formatTime = dealWithTime(date, recordsWDZJ.getAddDate());
    					recordsWDZJ.setAddDate(formatTime);*/
    					subscribes.add(recordsWDZJ);
					}
    				wdzj.setSubscribes(subscribes);
        			borrowList.add(wdzj);
    			}

			}
    	}

    	int totalCount= bidInfoService.getTotalCount(map);
    	response.put("totalPage", String.valueOf(totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1));
    	response.put("totalCount", String.valueOf(totalCount));
    	response.put("currentPage", page);
    	response.put("totalAmount", 0);//当天借款标总额Double
    	response.put("borrowList", borrowList);
    	return jackson(response);
    }

	/*private String dealWithTime(String date, String successTime) {
		Date timeDate = DateUtil.StringToDate(date, "yyyy-MM-dd");
		Date tempDate = DateUtil.StringToDate(successTime, "yyyy-MM-dd HH:mm:ss");
		String strDate = dateFormat.format(timeDate);
		String strTime = timeFormat.format(tempDate);
    	return strDate + " " + strTime;
	}*/


	@GET
	@Path("prebidInfo/list")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPreBidInfoListWangdaizhijia(@QueryParam("page")Integer page, @QueryParam("pageSize")Integer pageSize
			, @QueryParam("token")String token, @Context HttpServletRequest request) throws Throwable {

		Map<String, Object> response = new LinkedHashMap<>();
		//校验
		if(!userService.validateToken(token, request.getRequestURI())){
			response=failure();
			return jackson(response);
		}
		if(pageSize==null)pageSize=20;
		if(page==null)page=1;
		Map<String,Object> map= new HashMap<>();
		List<BidInfoPreVoWDZJ> borrowList=new ArrayList<BidInfoPreVoWDZJ>();
		int totalCount= bidInfoService.getWDZJPrepaymentTotalCount(map);
		if(totalCount > 0) {
			List<BidInfoEntity> list = bidInfoService.getWDZJPrepaymentBidInfoList(page, pageSize, map);
			LOGGER.info("获取网贷之家的数据：{}", JSON.toJSONString(list));
			if (list != null && !list.isEmpty()) {
				for (BidInfoEntity bid : list) {
					BidInfoPreVoWDZJ wdzj = new BidInfoPreVoWDZJ(bid);
					borrowList.add(wdzj);
				}
			}
			try {
				bidInfoService.setWDZJPrepaymentBids(list);
			}catch (Throwable t){
				LOGGER.info("获取网贷之家的更新数据异常：{}", t.getMessage());
			}

		}

		response.put("totalPage", String.valueOf(totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1));
		response.put("totalCount", String.valueOf(totalCount));
		response.put("currentPage", page);
		response.put("preapys", borrowList);
		return jackson(response);
	}

}
