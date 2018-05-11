package com.fenlibao.platform.resource.thirdparty.bid;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fenlibao.platform.model.thirdparty.entity.bid.BidInfoEntity;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInvestrecordsEntity;
import com.fenlibao.platform.model.thirdparty.vo.bid.BidInfoVoDZ;
import com.fenlibao.platform.model.thirdparty.vo.bid.BidInvestrecordsVoDZ;
import com.fenlibao.platform.resource.ParentResource;
import com.fenlibao.platform.service.thirdparty.BidInfoService;
import com.fenlibao.platform.thirdparty.util.security.MD5UtilDuozhuan;

/**
 * 多赚接口
 * @author junda.feng
 * @date 2016年5月28日
 */
@Path("tp/duozhuan")
public class DuozhuanResource extends ParentResource {
	@Inject
	private BidInfoService bidInfoService;

	/**
     * 借款标信息list
     * @throws Throwable 
     */
    @GET
	@Path("bidInfo/list")
	@Produces(MediaType.APPLICATION_JSON)
	public String getBidInfoListDuozhuan(@QueryParam("page")Integer page, @QueryParam("pageSize")Integer pageSize
			, @QueryParam("token")String token) throws Throwable {
    	Map<String, Object> response = new LinkedHashMap<>();
    	//校验签名
    	if(!MD5UtilDuozhuan.validate(page, pageSize, token)){
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
			response.put("borrowList", new ArrayList<BidInfoVoDZ>());
			return jackson(response);
		}
    	List<BidInfoEntity> list=bidInfoService.getBidInfoList(page, pageSize,null);
    	List<BidInfoVoDZ> borrowList=new ArrayList<BidInfoVoDZ>();
    	if(list!=null){
    		for (BidInfoEntity bid : list) {
    			BidInfoVoDZ dz=new BidInfoVoDZ(bid);
    			List<BidInvestrecordsEntity> subscribesList=bidInfoService.getBidInvestrecordsList(1, Integer.MAX_VALUE,bid.getProjectId());
    			List<BidInvestrecordsVoDZ> subscribes=new ArrayList<BidInvestrecordsVoDZ>();
    			if(subscribesList!=null && subscribesList.size()>0){
    				for (BidInvestrecordsEntity records : subscribesList) {
    					BidInvestrecordsVoDZ recordsDz=new BidInvestrecordsVoDZ(records);
    					subscribes.add(recordsDz);
					}
    			}
    			dz.setSubscribes(subscribes);
    			borrowList.add(dz);
			}
    	}
    	int totalCount= bidInfoService.getTotalCount(null);
    	response.put("totalPage", totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1);
    	response.put("totalCount", totalCount);
    	response.put("borrowList", borrowList);
    	return jackson(response);
    }

}
