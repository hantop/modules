package com.fenlibao.p2p.service.bid.impl;

import com.alibaba.fastjson.JSONArray;
import com.dimeng.p2p.S62.entities.T6232;

import com.fenlibao.p2p.dao.PublicAccessoryDao;
import com.fenlibao.p2p.dao.ShopTreasureDao;
import com.fenlibao.p2p.dao.bid.BidExtendDao;
import com.fenlibao.p2p.dao.bid.BidInfoDao;
import com.fenlibao.p2p.model.entity.PlanBidsStatus;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.bid.*;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.vo.InvestRecordsVO;
import com.fenlibao.p2p.model.vo.bidinfo.BidExtendGroupItemVO;
import com.fenlibao.p2p.model.vo.bidinfo.BidExtendGroupVO;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.*;

/** 
 * @ClassName: BidInfoServiceImpl 
 * @Description: TODO
 * @author: laubrence
 * @date: 2016-3-2 下午3:10:09  
 */
@Service
public class BidInfoServiceImpl implements BidInfoService {

    @Resource
    private ShopTreasureDao shopTreasureDao;
    
    @Resource
    private BidExtendDao bidExtendDao;
    
    @Resource
    private BidInfoDao bidInfoDao;

	@Resource
	private PublicAccessoryDao publicAccessoryDao;
    
	@Override
	public List<ShopTreasureInfo> getBidList(String[] bidStatus, String timestamp, int limit,String isNoviceBid,String isFull) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", bidStatus);
		map.put("limit", limit);
        if(StringUtils.isNotEmpty(timestamp)){
        	 Date time = DateUtil.timestampToDateBySec(Long.valueOf(timestamp));
        	map.put("time", time);
        }
        if(StringUtils.isNotEmpty(isNoviceBid)){
        	map.put("isNoviceBid", isNoviceBid);
        }
        if(StringUtils.isNotEmpty(isFull)){
        	map.put("isFull", isFull);
        }
        List<ShopTreasureInfo> list = this.shopTreasureDao.getCloseShopTreasure(map);
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getBidList(String[] bidStatus,String sortType,String sortBy,int limit,String isNoviceBid,int minDays, int maxDays,String bidType,int cgNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", bidStatus);
		map.put("limit", limit);
		if(StringUtils.isNotEmpty(isNoviceBid)){
        	map.put("isNoviceBid", isNoviceBid);
        }
        if(StringUtils.isNotEmpty(sortType)){
        	map.put("sortType", sortType);
        }
        if(StringUtils.isNotEmpty(sortBy)){
        	map.put("sortBy", sortBy);
        }if(StringUtils.isNotEmpty(sortBy)){
			map.put("sortBy", sortBy);
		}
		map.put("bidType", bidType);
		map.put("minDays", minDays);
		map.put("maxDays", maxDays);
		map.put("cgNum", cgNum);
        List<ShopTreasureInfo> list = this.shopTreasureDao.getBidInfoList(map);
		return list;
	}
	
	@Override
	public ShopTreasureInfo getBidInfo(int bidId) {
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", bidId);
        ShopTreasureInfo info = this.shopTreasureDao.getShopTreasureInfo(map);
		return info;
	}

    /** 
     * @Title: getBorrowerInfo 
     * @Description: 根据bidId查询借款人信息
     * @param bidId
     * @return
     * @return: BorrowerInfo
     */
    public BorrowerInfo getBorrowerInfo(int bidId){
    	return bidInfoDao.getBorrowerInfo(bidId);
    }
    
    /** 
     * @Title: getGroupInfoList 
     * @Description: TODO
     * @param bidId
     * @return
     * @throws Exception
     * @return: List<BidExtendGroupVO>
     */
    public List<BidExtendGroupVO> getGroupInfoList(int bidId) throws Exception{
    	
    	List<BidExtendGroupVO> groupInfoVOList = new ArrayList<BidExtendGroupVO>();
    	String groupCode = InterfaceConst.BID_EXTEND_GROUP_TYPE_CODE;
    	List<BidExtendGroupInfo> extGroupInfoList = bidExtendDao.getBidExtendGroupInfo(groupCode, bidId);
    	if(extGroupInfoList != null ){
    		for(BidExtendGroupInfo bidExtendGroupInfo: extGroupInfoList){
    			BidExtendGroupVO bidExtendGroupVO = new BidExtendGroupVO(); 
    			bidExtendGroupVO.setGroupName(bidExtendGroupInfo.getGroupName());
    			bidExtendGroupVO.setGroupCode(groupCode);
    			int groupId = bidExtendGroupInfo.groupId;
    			List<BidExtendGroupItemInfo> itemList = bidExtendDao.getBidExtendGroupItemInfo(bidId,groupId);
    			List<BidExtendGroupItemVO> itemVOList = new ArrayList<BidExtendGroupItemVO>();
    			if(itemList != null){
    				for(BidExtendGroupItemInfo item:itemList){
    					BidExtendGroupItemVO itemVO = new BidExtendGroupItemVO();
    					itemVO.setItemCode(item.getItemCode());
    					itemVO.setItemKey(item.getItemName());
    					itemVO.setItemType(item.getItemType());
    					itemVO.setItemValue(item.getItemValue());
    					if("pictures".equals(item.getItemType())){
    						String pics ="";
    						if(item.getItemValue()!=null && !"".equals(item.getItemValue())){
	    						String[] pic = item.getItemValue().split(",");
	    						for(String s:pic){
	    							s = FileUtils.getPicURL(s,Config.get("static.file.url"));
	    							pics+=s+",";
	    						}
	    						pics = pics.substring(0,pics.length()-1);
    						}
    						itemVO.setItemValue(pics);
    					}
    					itemVOList.add(itemVO);
    				}
    			}
    			bidExtendGroupVO.setItemInfoList(itemVOList);
    			groupInfoVOList.add(bidExtendGroupVO);
    		}
    	}
 		return groupInfoVOList;
    }

	@Override
	public List<InvestRecordsVO> getBidInvestRecords(String bidId, PageBounds pageBounds) {

		List<InvestRecordsVO> investsList = bidExtendDao.getBidInvestRecords(bidId,pageBounds);
		return investsList;
	}
    
	@Override
    public String getGroupItemValue(int bidId, String extCode) throws Exception{
		List<BidExtendGroupItemInfo> itemList = bidExtendDao.getBidExtendGroupItemInfo(bidId,extCode);
		if(itemList!=null && itemList.size() >0 ){
			return itemList.get(0).getItemValue();
		}
		return null;
    }
	
	@Override
	public BidBaseInfo getBidBaseInfoByUser(int userId, int bidId){
		return bidInfoDao.getBidBaseInfoByUser(userId, bidId);
	}

	/**
	 * 获取标的合同信息
	 * @param bidId
	 * @return
     */
	@Override
	public String[] getBidPublicAccessoryFiles(int bidId){
		Map<String, Object> accessoryMap = new HashMap<String, Object>();
		accessoryMap.put("bidId", bidId);
		accessoryMap.put("accessoryType", Config.get("shop.accessory.type"));
		List<T6232> publicAccessoryList = publicAccessoryDao.getPublicAccessory(accessoryMap);
		String[] contractUrls = new String[publicAccessoryList.size()];
		for(int i=0;i<publicAccessoryList.size();i++){
			T6232 accessory = publicAccessoryList.get(i);
			String fileUrl= FileUtils.getPicURL(accessory.F04,Config.get("contact.url"));
			contractUrls[i] = fileUrl;
		}
		return contractUrls;
	}

	/**
	 * 获取标的合同urls
	 * @param bidId
	 * @return
     */
	@Override
	public String getBidPublicAccessoryFileUrl(int bidId) throws Exception{
		String[] contractUrls = this.getBidPublicAccessoryFiles(bidId);
		String fileUrl = Config.get("contactInfo.url");
		if(contractUrls!=null && contractUrls.length>0) {
			fileUrl += URLEncoder.encode(JSONArray.toJSONString(contractUrls), "UTF-8");
		}
		return fileUrl;
	}

    /**
     * 获取标的基本信息
     * @param bidId
     * @return
     */
    @Override
    public BidBaseInfo getBidBaseInfo(int bidId){
        return bidInfoDao.getBidBaseInfo(bidId);
    }

    /**
     * 获取标的扩展信息
     * @param bidId
     * @return
     */
    @Override
    public BidExtendInfo getBidExtendInfo(int bidId){
        return bidInfoDao.getBidExtendInfo(bidId);
    }

    /**
     * 获取标的所有信息
     * @param bidId
     * @return
     */
    @Override
    public BidExtendInfo getBidAllInfo(int bidId){
        return bidInfoDao.getBidAllInfo(bidId);
    }

	@Override
	public List<ShopTreasureInfo> getBidInfoList(String bidType, String[] bidStatus, int minDays, int maxDays, PageBounds pageBounds) throws Exception {
		return bidInfoDao.getBidInfoList(bidType, bidStatus, minDays, maxDays, pageBounds);
	}

	@Override
	public List<ShopTreasureInfo> getBidInfoList(String bidType, String[] bidStatuses, int minDays, int maxDays, String sortType, String sortBy, PageBounds pageBounds) throws Exception {
		return bidInfoDao.getBidInfoOrderByList(bidType, bidStatuses, minDays, maxDays, sortType, sortBy, pageBounds);
	}

	@Override
	public List<ShopTreasureInfo> getBidInfoAndHisBidList(String bidType, String[] bidStatuses, int minDays, int maxDays, String sortType, String sortBy, PageBounds pageBounds) throws Exception {
		return bidInfoDao.getBidInfoAndHisBidList(bidType, bidStatuses, minDays, maxDays, sortType, sortBy, pageBounds);
	}

	@Override
	public boolean isNovice(int userId) {
		Integer count = bidInfoDao.getUserInvestCount(userId);
		if (count == null || count == 0) {
			return true;
		}
		return false;
	}

	@Override
	public BidBorrowerInfo getBidBorrowerInfo(int bidId) {
		return bidInfoDao.getBidBorrowerInfo(bidId);
	}

	/**
	 * 获取标的合同信息--2.0证明文件
	 * @param bidId
	 * @return
     */
	@Override
	public List<BidFiles> getBidPublicAccessoryFilesList(int bidId){
		Map<String, Object> accessoryMap = new HashMap<String, Object>();
		accessoryMap.put("bidId", bidId);
		accessoryMap.put("accessoryType", Config.get("shop.accessory.type"));
		List<BidFiles> publicAccessoryList = publicAccessoryDao.getPublicAccessoryFiles(accessoryMap);
		return publicAccessoryList;
	}

	@Override
	public DirectionalBid getDirectionalBid(int bidId) {
		return bidInfoDao.getDirectionalBid(bidId);
	}

	@Override
	public List<ShopTreasureInfo> getBidListOrderByStatus(String[] bidStatus, String timestamp, int limit, String isNoviceBid, String isFull,String bidType,int minDays, int maxDays) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", bidStatus);
		map.put("limit", limit);
		if(StringUtils.isNotEmpty(timestamp)){
			Date time = DateUtil.timestampToDateBySec(Long.valueOf(timestamp));
			map.put("time", time);
		}
		if(StringUtils.isNotEmpty(isNoviceBid)){
			map.put("isNoviceBid", isNoviceBid);
		}
		if(StringUtils.isNotEmpty(bidType)){
			map.put("bidType", bidType);
		}
		if(StringUtils.isNotEmpty(isFull)){
			map.put("isFull", isFull);
		}
		map.put("minDays", minDays);
		map.put("maxDays", maxDays);
		List<ShopTreasureInfo> list = this.shopTreasureDao.getCloseShopTreasureOderByStatus(map);
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getPreSaleBids(int limit,String bidType,int minDays, int maxDays) {
		Map<String, Object> map = new HashMap<>();
		map.put("limit",limit);
        if(!StringUtils.isEmpty(bidType)){
			map.put("bidType",bidType);
		}
		map.put("minDays", minDays);
		map.put("maxDays", maxDays);
		return this.shopTreasureDao.getPreSaleBids(map);
	}

	@Override
	public List<ShopTreasureInfo> getTimingBids(int limit,String bidType,int minDays, int maxDays) {
		Map<String, Object> map = new HashMap<>();
		map.put("limit",limit);
		if(!StringUtils.isEmpty(bidType)){
			map.put("bidType",bidType);
		}
		map.put("minDays", minDays);
		map.put("maxDays", maxDays);
		return this.shopTreasureDao.getTimingBids(map);
	}

	@Override
	public List<ShopTreasureInfo> getBidPlansList(String[] status, String timestamp, int minDaysInteger, int maxDaysInteger, String sortType, String sortBy, Integer pageNo, Integer pagesize, int limit, String isNoviceBid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		map.put("minDays", minDaysInteger);
		map.put("maxDays", maxDaysInteger);
		if (limit!=-1) {
			map.put("limit", limit);
		}
		if(StringUtils.isNotEmpty(timestamp)){
			Date time = DateUtil.timestampToDateBySec(Long.valueOf(timestamp));
			map.put("time", time);
		}
		if(StringUtils.isNotEmpty(sortType)){
			map.put("sortType", sortType);
		}
		if(StringUtils.isNotEmpty(sortBy)){
			map.put("sortBy", sortBy);
		}if(StringUtils.isNotEmpty(sortBy)){
			map.put("sortBy", sortBy);
		}
		if(StringUtils.isNotEmpty(isNoviceBid)){
			map.put("isNoviceBid", isNoviceBid);
		}
		/*PageHelper.startPage(pageNo,pagesize,false);*/
		List<ShopTreasureInfo> list = this.shopTreasureDao.getBidPlansList(map);
		return list;
	}

	@Override
	public List<PlanBidsStatus> getPlanBidsStatus(int planId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("planId", planId);
		return this.shopTreasureDao.getPlanBidsStatus(map);
	}

	@Override
	public ShopTreasureInfo getPlanInfo(int planId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", planId);
		ShopTreasureInfo info = this.shopTreasureDao.getPlanInfo(map);
		return info;
	}

	@Override
	public List<ShopTreasureInfo> getdirectionalBid(int limit,String bidType,int minDays, int maxDays) {
		Map<String, Object> map = new HashMap<>();
		map.put("limit",limit);
		if(StringUtils.isNotEmpty(bidType)){
			map.put("bidType", bidType);
		}
		map.put("minDays", minDays);
		map.put("maxDays", maxDays);
		return this.shopTreasureDao.getdirectionalBid(map);
	}

	@Override
	public int getpurchasedPlan(int userId, int planId) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId",userId);
		map.put("planId",planId);
		int i = shopTreasureDao.getpurchasedPlan(map);
		return i;
	}

	@Override
	public List<ShopTreasureInfo> getBidInfoAndPlanAndHisBidList(String bidType, String[] bidStatuses, int minDays, int maxDays, String sortType, String sortBy, PageBounds pageBounds,String productType,int cgNum,int isNeedHis,int isNovice) throws Exception {
		return bidInfoDao.getBidInfoAndPlanAndHisBidList(bidType, bidStatuses, minDays, maxDays, sortType, sortBy, pageBounds,productType,cgNum,isNeedHis,isNovice);
	}

	@Override
	public List<ShopTreasureInfo> getStickBidList(String[] bidStatus, int limit, String isNoviceBid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", bidStatus);
		if(limit!=-1) {
			map.put("limit", limit);
		}
		if (StringUtils.isNotEmpty(isNoviceBid)) {
			map.put("isNoviceBid", isNoviceBid);
		}
		List<ShopTreasureInfo> list = this.shopTreasureDao.getStickShopTreasure(map);
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getPlansList(String[] status, String timestamp, int minDaysInteger, int maxDaysInteger, String sortType, String sortBy, Integer pageNo, Integer pagesize, int limit, String isNoviceBid, int type, String timeSort, int timeBid,int cgNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> statusNum = new ArrayList<Integer>();
		if(status!=null&&status.length>0){
			for(int i=0;i<status.length;i++){
				if(status[i].equals("DTJ")){
					statusNum.add(1);
				}
				if(status[i].equals("DSH")){
					statusNum.add(2);
				}
				if(status[i].equals("DFB")){
					statusNum.add(3);
				}
				if(status[i].equals("TBZ")){
					statusNum.add(4);
				}
				if(status[i].equals("HKZ")){
					statusNum.add(5);
				}
				if(status[i].equals("YJQ")){
					statusNum.add(6);
				}
				if(status[i].equals("YZF")){
					statusNum.add(7);
				}
				if(status[i].equals("YFB")){
					statusNum.add(8);
				}

			}
		}
		int size = statusNum.size();
		Integer[] statusArray =(Integer[])statusNum.toArray(new Integer[size]);
		map.put("status", statusArray);
		map.put("minDays", minDaysInteger);
		map.put("maxDays", maxDaysInteger);
		map.put("cgNum", cgNum);
		if( !Arrays.asList(status).contains("TBZ")){
			map.put("fullTime", 1);
		}
		if(type!=-1) {
			map.put("type", type);
		}
		if(timeBid!=-1) {
			map.put("timeBid", timeBid);
		}
		if (limit!=-1) {
			map.put("limit", limit);
		}
		if(StringUtils.isNotEmpty(timestamp)){
			Date time = DateUtil.timestampToDateBySec(Long.valueOf(timestamp));
			map.put("time", time);
		}
		if(StringUtils.isNotEmpty(sortType)){
			map.put("sortType", sortType);
		}
		if(StringUtils.isNotEmpty(sortBy)){
			map.put("sortBy", sortBy);
		}
		if(StringUtils.isNotEmpty(isNoviceBid)){
			if(isNoviceBid.equals("S")) {
				map.put("isNoviceBid", 1);
			}else {
				map.put("isNoviceBid", 0);
			}
		}
		if (StringUtils.isNotBlank(timeSort)) {
			map.put("timeSort", timeSort);
		}
		List<ShopTreasureInfo> list = this.shopTreasureDao.getPlansList(map);
		return list;
	}

	@Override
	public ShopTreasureInfo getPlanDetail(int planId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", planId);
		ShopTreasureInfo info = this.shopTreasureDao.getPlanDetail(map);
		return info;
	}

	@Override
	public int getNewPurchasedPlan(int userId, int planId) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId",userId);
		map.put("planId",planId);
		int i = shopTreasureDao.getNewPurchasedPlan(map);
		return i;
	}

	@Override
	public int isCGBid(int bidId) {
		Map<String, Object> map = new HashMap<>();
		map.put("bidId",bidId);
		return this.shopTreasureDao.isCGBid(map);
	}

	@Override
	public int countIvestProduct(String type,String versionType) {
		return this.shopTreasureDao.countIvestProduct(type,versionType);
	}

	@Override
	public String getenterpriseSealCode(Integer bidId) {
		return this.bidInfoDao.getenterpriseSealCode(bidId);
	}
}
