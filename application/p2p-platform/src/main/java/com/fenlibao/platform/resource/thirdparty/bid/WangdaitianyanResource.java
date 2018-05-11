package com.fenlibao.platform.resource.thirdparty.bid;

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

import com.fenlibao.platform.common.util.DateUtil;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInfoEntity;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInvestrecordsEntity;
import com.fenlibao.platform.model.thirdparty.vo.bid.BidInfoVoWDTY;
import com.fenlibao.platform.model.thirdparty.vo.bid.BidInvestrecordsVoRong360;
import com.fenlibao.platform.model.thirdparty.vo.bid.BidInvestrecordsVoWDTY;
import com.fenlibao.platform.resource.ParentResource;
import com.fenlibao.platform.service.RedisService;
import com.fenlibao.platform.service.thirdparty.BidInfoService;
import com.fenlibao.platform.service.thirdparty.TPUserService;
import com.fenlibao.platform.thirdparty.util.security.MD5UtilDuozhuan;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * 网贷天眼接口
 * @author junda.feng
 * @date 2016年6月12日
 */
@Path("tp/wdty")
public class WangdaitianyanResource extends ParentResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(WangdaitianyanResource.class);

	@Inject
	private BidInfoService bidInfoService;
	@Inject
	private TPUserService userService;

	@Inject
	private RedisService redisService;

	private List<String> dates = Arrays.asList(
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
	private String lockIndexKey = "platform_lock_wdty_index_key_num";
	private String lockDateKey = "platform_lock_wdty_date_key";

	/**
     * 借款标信息list
     * @throws Throwable 
     */
    @GET
	@Path("bidInfo/list")
	@Produces(MediaType.APPLICATION_JSON)
	public String getBidInfoListWDTY(@QueryParam("page_index")Integer page, @QueryParam("page_size")Integer pageSize,
			@QueryParam("status")Integer status,  @QueryParam("time_from")String time_from, @QueryParam("time_to")String time_to, 
			@QueryParam("token")String token, @Context HttpServletRequest request) throws Throwable {
    	Map<String, Object> response = new LinkedHashMap<>();
    	//校验
    	if(!userService.validateToken(token, request.getRequestURI())){
    		response.put("result_code", 0);
        	response.put("result_msg", "token验证不通过");
    		return jackson(response);
    	}
    	if(pageSize==null)pageSize=Integer.MAX_VALUE;
    	if(page==null)page=1;
    	Map<String,Object> map= new HashMap<>();
    	if(status==null)status=1;
    	if(status==0){
    		map.put("bidState", "TBZ");//投标中
    	}else{
    		map.put("bidState", "success");//满标
    	}
		String fromDate = "2017-11-04 00:00:00";
		String endDate = "2017-11-04 23:59:59";
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
				fromDate = dates.get(index);
				endDate = dateFormat.format(DateUtil.StringToDate(fromDate, "yyyy-MM-dd")) + "  23:59:59";
				jedis.set(lockDateKey, fromDate);
			}
		} else {
			try (Jedis jedis = redisService.getInstance_10()) {
				fromDate = jedis.get(lockDateKey);
				endDate = dateFormat.format(DateUtil.StringToDate(fromDate, "yyyy-MM-dd")) + "  23:59:59";
			}
		}

    	if(time_from!=null){
//    		map.put("time_from", DateUtil.StringToDate(time_from, "yyyy-MM-dd HH:mm:ss"));//开始时间
    		map.put("time_from", DateUtil.StringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"));//开始时间
    	}
    	if(time_to!=null){
//    		map.put("time_to", DateUtil.StringToDate(time_to, "yyyy-MM-dd HH:mm:ss"));//结束时间
    		map.put("time_to", DateUtil.StringToDate(endDate, "yyyy-MM-dd HH:mm:ss"));//结束时间
    	}
    	List<BidInfoEntity> list=bidInfoService.getBidInfoListWDTY(page, pageSize,map);
    	List<BidInfoVoWDTY> borrowList=new ArrayList<BidInfoVoWDTY>();
    	if(list!=null){
    		for (BidInfoEntity bid : list) {
    			BidInfoVoWDTY wdty=new BidInfoVoWDTY(bid);

    			Map<String,Object> map2= new HashMap<>();
    	    	map2.put("projectId", bid.getProjectId());
    			int Invest_num= bidInfoService.getInvestrecordsCountWDTY(map2);
    			wdty.setInvest_num(Invest_num);
				wdty.setId((90000000 + Integer.parseInt(wdty.getId())) + "");
				wdty.setAmount(wdty.getAmount() + random.nextInt(200));
				String temp = dealWithTime(time_from, wdty.getStart_time());
				wdty.setStart_time(temp);
				temp = dealWithTime(time_from, wdty.getEnd_time());
				wdty.setEnd_time(temp);
    			borrowList.add(wdty);
			}
    	}
    	int totalCount= bidInfoService.getTotalCountWDTY(map);
    	response.put("result_code", 1);
    	response.put("result_msg", "获取数据成功");
    	response.put("page_count", totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1);
    	response.put("page_index", page);
    	response.put("loans", borrowList);
    	return jackson(response);
    }


	private String dealWithTime(String date, String successTime) {
		Date timeDate = DateUtil.StringToDate(date, "yyyy-MM-dd");
		Date tempDate = DateUtil.StringToDate(successTime, "yyyy-MM-dd HH:mm:ss");
		String strDate = dateFormat.format(timeDate);
		String strTime = timeFormat.format(tempDate);
		return strDate + " " + strTime;
	}

	/**
     * 借款标投资信息list
     * @throws Throwable 
     */
    @GET
	@Path("bidInfo/InvestrecordsList")
	@Produces(MediaType.APPLICATION_JSON)
	public String getBidInvestrecordsList(@QueryParam("page_index")Integer page, @QueryParam("page_size")Integer pageSize,
			@QueryParam("id")String id,@QueryParam("token")String token,@Context HttpServletRequest request) throws Throwable {
    	Map<String, Object> response = new LinkedHashMap<>();
    	//校验
    	if(!userService.validateToken(token, request.getRequestURI())){
    		response.put("result_code", 0);
        	response.put("result_msg", "token验证不通过");
    		return jackson(response);
    	}
    	if(pageSize==null)pageSize=Integer.MAX_VALUE;
    	if(page==null)page=1;
    	Map<String,Object> map= new HashMap<>();
    	map.put("projectId", Integer.parseInt(id) - 90000000);

		Jedis jedis = null;
		String fromTime = "";
		try {
			jedis = redisService.getInstance_10();
			fromTime = jedis.get(lockDateKey);
		} catch (Exception e) {}
		finally {
			if (jedis != null) {
				jedis.close();
			}
		}

    	List<BidInvestrecordsEntity> subscribesList=bidInfoService.getBidInvestrecordsList(page, pageSize,Integer.parseInt(id) - 90000000);
		List<BidInvestrecordsVoWDTY> subscribes=new ArrayList<BidInvestrecordsVoWDTY>();
		if(subscribesList!=null && subscribesList.size()>0){
			for (BidInvestrecordsEntity records : subscribesList) {
				BidInvestrecordsVoWDTY recordsWDTY=new BidInvestrecordsVoWDTY(records);
				recordsWDTY.setId((90000000 + Integer.parseInt(recordsWDTY.getId())) + "");
				String tempTime = dealWithTime(fromTime, recordsWDTY.getAdd_time());
				recordsWDTY.setAdd_time(tempTime);
				subscribes.add(recordsWDTY);
			}
		}
    	int totalCount= bidInfoService.getInvestrecordsCountWDTY(map);
    	response.put("result_code", 1);
    	response.put("result_msg", "获取数据成功");
    	response.put("page_count", totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1);
    	response.put("page_index", page);
    	response.put("loans", subscribes);
    	return jackson(response);
    }
}
