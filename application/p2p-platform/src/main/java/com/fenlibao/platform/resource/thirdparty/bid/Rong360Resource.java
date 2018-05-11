package com.fenlibao.platform.resource.thirdparty.bid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fenlibao.platform.model.thirdparty.vo.bid.*;
import org.apache.commons.lang3.StringUtils;

import com.fenlibao.platform.common.util.DateUtil;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInfoEntity;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInvestrecordsEntity;
import com.fenlibao.platform.resource.ParentResource;
import com.fenlibao.platform.service.thirdparty.BidInfoService;
import com.fenlibao.platform.service.thirdparty.TPUserService;

/**
 * 融360
 * @author junda.feng
 * @date 2016年6月1日
 */
@Path("tp/rong360")
public class Rong360Resource extends ParentResource {
	
	@Inject
	private BidInfoService bidInfoService;
	@Inject
	private TPUserService userService;

    /**
     * 借款标信息满标list
     * @throws Throwable 
     */
    @GET
	@Path("bidInfo/successList")
	@Produces(MediaType.APPLICATION_JSON)
	public String getBidInfoListRong360(@QueryParam("page")Integer page, @QueryParam("pageSize")Integer pageSize,
			@QueryParam("date")String date, @QueryParam("token")String token, @Context HttpServletRequest request) throws Throwable {
    	
    	Map<String, Object> response = new LinkedHashMap<>();
    	//校验
    	if(!userService.validateToken(token, request.getRequestURI())){
    		response=failure();
    		return jackson(response);
    	}
    	if(pageSize==null)pageSize=20;
    	if(page==null)page=1;
		// modify by zeronx 2017-12-01 11:00  (2017-12-01 今天起不在提供数据) 临时
		if (true) {
			// 直接返回
			response.put("totalPage", 0);
			response.put("totalCount", 0);
			response.put("currentPage", page);
			response.put("totalAmount", 0);//当天借款标总额Double
			response.put("borrowList", new ArrayList<BidInfoVoRong360>());
			return jackson(response);
		}
    	Map<String,Object> map= new HashMap<>();
    	map.put("bidState", "360success");//360满标
    	if(date!=null){
    		map.put("successDate", DateUtil.StringToDate(date, "yyyy-MM-dd"));//满标时间
    	}
     	
    	List<BidInfoEntity> list=bidInfoService.getBidInfoList(page, pageSize,map);
    	List<BidInfoVoRong360> borrowList=null;
    	if(list!=null && list.size()>0){
    		borrowList=new ArrayList<BidInfoVoRong360>();
    		for (BidInfoEntity bid : list) {
    			BidInfoVoRong360 Rong360=new BidInfoVoRong360(bid);
    			Rong360.setInterestRate(Rong360.getInterestRate()+"%");
    			if(StringUtils.isNoneBlank(bid.getTypeName())){
    				Rong360.setType(bid.getTypeName().replace(",", ";"));
    			}else{
    				Rong360.setType("-");
    			}
    			
    			List<BidInvestrecordsEntity> subscribesList=bidInfoService.getBidInvestrecordsList(1, Integer.MAX_VALUE,bid.getProjectId());
    			List<BidInvestrecordsVoRong360> subscribes=new ArrayList<BidInvestrecordsVoRong360>();
    			if(subscribesList!=null && subscribesList.size()>0){
    				for (BidInvestrecordsEntity records : subscribesList) {
    					BidInvestrecordsVoRong360 recordsRong360=new BidInvestrecordsVoRong360(records);
    					subscribes.add(recordsRong360);
					}
    				Rong360.setSubscribes(subscribes);
        			borrowList.add(Rong360);
    			}
    			
			}
    	}
    	
    	int totalCount= bidInfoService.getTotalCount(map);
    	Double totalAmount=bidInfoService.getTotalAmount(map);
    	response.put("totalPage", String.valueOf(totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1));
    	response.put("totalCount", String.valueOf(totalCount));
    	response.put("currentPage", page);
    	response.put("totalAmount", totalAmount==null?0:totalAmount);//当天借款标总额Double
    	response.put("borrowList", borrowList);
    	return jackson(response);
    }
    

    /**
     * 借款标信息投标中列表
     * @throws Throwable 
     */
    @GET
	@Path("bidInfo/tbzList")
	@Produces(MediaType.APPLICATION_JSON)
	public String getBidInfoTBZListRong360(@QueryParam("page")Integer page, @QueryParam("pageSize")Integer pageSize,
			@QueryParam("date")String date, @QueryParam("token")String token, @Context HttpServletRequest request) throws Throwable {
    	
    	Map<String, Object> response = new LinkedHashMap<>();
    	//校验
    	if(!userService.validateToken(token, request.getRequestURI())){
    		response=failure();
    		return jackson(response);
    	}
    	
    	if(pageSize==null)pageSize=20;
    	if(page==null)page=1;
    	
    	Map<String,Object> map= new HashMap<>();
//    	map.put("bidState", "TBZ");//投标中
    	if(date!=null){
    		map.put("publishDate", DateUtil.StringToDate(date, "yyyy-MM-dd"));//发标时间
    	}
    	List<BidInfoEntity> list=bidInfoService.getBidInfoList(page, pageSize,map);
    	List<BidInfoVoRong360TBZ> borrowList=null;
    	if(list!=null && list.size()>0){
    		borrowList=new ArrayList<BidInfoVoRong360TBZ>();
    		for (BidInfoEntity bid : list) {
    			BidInfoVoRong360TBZ Rong360=new BidInfoVoRong360TBZ(bid);
    			Rong360.setInterestRate(Rong360.getInterestRate()+"%");
    			Rong360.setType("".equals(bid.getTypeName())?"-":bid.getTypeName().replace(",", ";"));
    			borrowList.add(Rong360);
			}
    	}
    	int totalCount= bidInfoService.getTotalCount(map);
    	response.put("totalPage", String.valueOf(totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1));
    	response.put("totalCount", String.valueOf(totalCount));
    	response.put("currentPage", page);
    	response.put("onSaleBorrowList", borrowList);
    	return jackson(response);
    }
    

    
    /**
     * 批量查询标状态接口
     * @throws Throwable 
     */
    @GET
	@Path("bidInfo/stateList")
	@Produces(MediaType.APPLICATION_JSON)
	public String getBidInfoStateListRong360(@QueryParam("idStr")String idStr, 
			@QueryParam("token")String token, @Context HttpServletRequest request) throws Throwable {
    	
    	Map<String, Object> response = new LinkedHashMap<>();
    	//校验
    	if(!userService.validateToken(token, request.getRequestURI())){
    		response=failure();
    		return jackson(response);
    	}
    	
    	if(StringUtils.isBlank(idStr)){
    		response=failure();
    		return jackson(response);
    	}
    	idStr=idStr.replace("|", ",");
    	String[] sb=idStr.split(",");  
    	
    	List<BidInfoEntity> list=bidInfoService.getBidInfoListByIds(sb);
    	List<BidInfoVoRong360State> borrowList=new ArrayList<BidInfoVoRong360State>();
    	if(list!=null){
    		for (BidInfoEntity bid : list) {
    			BidInfoVoRong360State Rong360=new BidInfoVoRong360State(bid);
    			borrowList.add(Rong360);
			}
    	}
    	int totalCount= bidInfoService.getTotalCountByIds(sb);
    	response.put("totalLoan", String.valueOf(totalCount));
    	response.put("borrowStatusList", borrowList);
    	return jackson(response);
    }
    
}
