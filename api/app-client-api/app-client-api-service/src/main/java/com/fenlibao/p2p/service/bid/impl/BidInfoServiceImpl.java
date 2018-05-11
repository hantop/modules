package com.fenlibao.p2p.service.bid.impl;

import com.alibaba.fastjson.JSONArray;
import com.dimeng.p2p.S62.entities.T6232;
import com.fenlibao.p2p.dao.PublicAccessoryDao;
import com.fenlibao.p2p.dao.ShopTreasureDao;
import com.fenlibao.p2p.dao.bid.BidExtendDao;
import com.fenlibao.p2p.dao.bid.BidInfoDao;
import com.fenlibao.p2p.model.entity.PfBidInfoVo;
import com.fenlibao.p2p.model.entity.PlanBidsStatus;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.bid.*;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.enums.bid.BidOriginEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.form.trade.DueInAmount;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.InvestRecordsVO;
import com.fenlibao.p2p.model.vo.PlanDetailVO;
import com.fenlibao.p2p.model.vo.bidinfo.BidDetailVO;
import com.fenlibao.p2p.model.vo.bidinfo.BidExtendGroupItemVO;
import com.fenlibao.p2p.model.vo.bidinfo.BidExtendGroupVO;
import com.fenlibao.p2p.model.vo.supervise.BorrowerInfoVO2;
import com.fenlibao.p2p.model.vo.supervise.PlanInfoVO;
import com.fenlibao.p2p.model.vo.supervise.ProjectInfoVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.StatusUtil;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @ClassName: BidInfoServiceImpl
 * @Description: TODO
 * @author: laubrence
 * @date: 2016-3-2 下午3:10:09
 */
@Service
public class BidInfoServiceImpl implements BidInfoService {
	protected static final Logger logger = LoggerFactory.getLogger(BidInfoServiceImpl.class);

	public static DecimalFormat df = new DecimalFormat("#,###");

	@Resource
    private ShopTreasureDao shopTreasureDao;

    @Resource
    private BidExtendDao bidExtendDao;

    @Resource
    private BidInfoDao bidInfoDao;

	@Resource
	private PublicAccessoryDao publicAccessoryDao;

	@Resource
	private UserInfoService userInfoService;

	@Resource
	private ITradeService tradeService;


	@Override
	public List<ShopTreasureInfo> getBidList(String[] bidStatus, String timestamp, int limit,String isNoviceBid,String isFull,String bidOrigin,int cgNum) {
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
		if(StringUtils.isNotEmpty(bidOrigin)){
			map.put("bidOrigin", bidOrigin);
		}
        if(StringUtils.isNotEmpty(isFull)){
        	map.put("isFull", isFull);
        }
		if( !Arrays.asList(bidStatus).contains("TBZ")){
			map.put("TBZ", 1);
		}
		if(cgNum != -1){
			map.put("cgNum", cgNum);
		}
        List<ShopTreasureInfo> list = this.shopTreasureDao.getCloseShopTreasure(map);
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getBidRecommedList(String[] bidStatus, int limit, String isNoviceBid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", bidStatus);
		map.put("limit", limit);
		if(StringUtils.isNotEmpty(isNoviceBid)){
			map.put("isNoviceBid", isNoviceBid);
		}
		List<ShopTreasureInfo> list = this.shopTreasureDao.getCloseShopTreasureRecommend(map);
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getBidRecommendList(String[] bidStatus, String timestamp, int limit, String isNoviceBid, String isFull) {
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
	public List<ShopTreasureInfo> getBidList(String[] bidStatus,String sortType,String sortBy,int limit,String isNoviceBid) {
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
        }
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
						if(item.getItemValue()!=null && !"".equals(item.getItemValue())){
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
					if(itemVOList.size()>0){
						bidExtendGroupVO = new BidExtendGroupVO();
						bidExtendGroupVO.setGroupName(bidExtendGroupInfo.getGroupName());
						bidExtendGroupVO.setGroupCode(groupCode);
						bidExtendGroupVO.setItemInfoList(itemVOList);
						groupInfoVOList.add(bidExtendGroupVO);
					}
				}
			}
		}
		return groupInfoVOList;
	}

	@Override
	public List<InvestRecordsVO> getBidInvestRecords(String bid, String timestamp, String isUp) {
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("bid",Integer.parseInt(bid.trim()));
        params.put("status",Status.F.name());
        params.put("isUp", isUp);
        if(!StringUtils.isBlank(timestamp)){
            Date time = DateUtil.timestampToDateBySec(Long.valueOf(timestamp));
            params.put("time",time);
        }

		List<InvestRecordsVO> investsList = bidExtendDao.getBidInvestRecords(params);
		return investsList;
	}

	@Override
	public List<InvestRecordsVO> getBidInvestAllRecords(String bid) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("bid",Integer.parseInt(bid.trim()));

		List<InvestRecordsVO> investsList = bidExtendDao.getBidInvestAllRecords(params);
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
			String fileUrl=FileUtils.getPicURL(accessory.F04,Config.get("contact.url"));
			contractUrls[i] = fileUrl;
		}
		return contractUrls;
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
	public DirectionalBid getDirectionalBid(int bidId) {
		return bidInfoDao.getDirectionalBid(bidId);
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
	public int updateBidInfo() {
		int i=0;
		synchronized (this) {
			List<PfBidInfoVo> updateRows = bidInfoDao.getNeedUpdateRows();
			if (updateRows != null && !updateRows.isEmpty()) {
				int ik =0, size = updateRows.size();
				int rows = 1000;
				do {
					i+=bidInfoDao.updateBidInfo(updateRows.subList(ik, (ik + rows) < size ? (ik + rows) : size));
					ik = ik + rows;
				} while (ik < size);
			}
		}
		/**
		 * 标的id：大于 2000000
		 标的金额：大于1000000
		 标的状态：('TBZ','DFK','HKZ','YJQ','YLB','YDF','DFZ')
		 标的不存在： pf.v_bid_info
		 */
		i+=bidInfoDao.addBidInfo();
		i+=bidInfoDao.addBidInvestrecords();
		return i;
	}

	@Override
	public BidBorrowerInfo getBidBorrowerInfo(int bidId) {
		return bidInfoDao.getBidBorrowerInfo(bidId);
	}

	@Override
	public List<ShopTreasureInfo> getTimingBids(int limit) {
		Map<String, Object> map = new HashMap<>();
		if(limit!=-1) {
			map.put("limit", limit);
		}
		return this.shopTreasureDao.getTimingBids(map);
	}

	@Override
	public List<ShopTreasureInfo> getPreSaleBids(int limit) {
		Map<String, Object> map = new HashMap<>();
		if(limit!=-1) {
			map.put("limit", limit);
		}
		return this.shopTreasureDao.getPreSaleBids(map);
	}

	@Override
	public List<ShopTreasureInfo> getInvestmentBids(String[] bidStatus,String sortType,String sortBy,int limit,int preSaleBidlimit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", bidStatus);
		map.put("limit", limit);
		map.put("preSaleBidlimit", preSaleBidlimit);
		if(StringUtils.isNotEmpty(sortType)){
			map.put("sortType", sortType);
		}
		if(StringUtils.isNotEmpty(sortBy)){
			map.put("sortBy", sortBy);
		}
		List<ShopTreasureInfo> list = this.shopTreasureDao.getInvestmentBids(map);
		//预售标
		map = new HashMap<String, Object>();
		map.put("limit", 1);
		List<ShopTreasureInfo> preSaleBids = this.shopTreasureDao.getPreSaleBids(map);
		if(preSaleBids!=null && preSaleBids.size()>0){
			list.addAll(preSaleBids);
		}
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getInvestmentBids(Map map) {
		List<ShopTreasureInfo> list = this.shopTreasureDao.getInvestmentBids(map);
		if(BidOriginEnum.qqm.getCode().equals(map.get("bidOrigin"))){
			return list;
		}
		//预售标
		map = new HashMap<String, Object>();
		map.put("limit", 1);
		List<ShopTreasureInfo> preSaleBids = this.shopTreasureDao.getPreSaleBids(map);
		if(preSaleBids!=null && preSaleBids.size()>0){
			list.addAll(preSaleBids);
		}
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getBidList(Map map) {
		List<ShopTreasureInfo> list = this.shopTreasureDao.getCloseShopTreasure(map);
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getXfxdBidList(Map map) {
		if (map==null) map=new HashMap();
		map.put("bidOrigin",BidOriginEnum.qqm.getCode());
		List<ShopTreasureInfo> list = this.shopTreasureDao.getXfxdBidList(map);
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getXfxdShortBidList(Map<String, Object> xfxdParam) {
		if (xfxdParam==null) xfxdParam=new HashMap();
		xfxdParam.put("bidOrigin",BidOriginEnum.qqm.getCode());
		List<ShopTreasureInfo> list = this.shopTreasureDao.getXfxdShortBidList(xfxdParam);
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getBidInfoByTimeSort(String[] bidStatus,int timeSort) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", bidStatus);
		map.put("timeSort", timeSort);
		List<ShopTreasureInfo> list = this.shopTreasureDao.getBidInfoByTimeSort(map);
		return list;
	}

	@Override
	public int isCGBid(int bidId) {
		Map<String, Object> map = new HashMap<>();
		map.put("bidId",bidId);
		return this.shopTreasureDao.isCGBid(map);
	}

	@Override
	public List<ShopTreasureInfo> getBidList(String[] bidStatus, String timestamp, int limit, String isNoviceBid, String isFull, int cgNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", bidStatus);
		map.put("limit", limit);
		map.put("cgNum", cgNum);
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
	public List<ShopTreasureInfo> getPreSaleBids(int limit,int cgNum) {
		Map<String, Object> map = new HashMap<>();
		if(limit !=-1) {
			map.put("limit", limit);
		}
		map.put("cgNum", cgNum);
		return this.shopTreasureDao.getPreSaleBids(map);
	}

	@Override
	public List<ShopTreasureInfo> getTimingBids(int limit,int cgNum) {
		Map<String, Object> map = new HashMap<>();
		if(limit !=-1) {
			map.put("limit", limit);
		}
		map.put("cgNum", cgNum);
		return this.shopTreasureDao.getTimingBids(map);
	}

	@Override
	public List<ShopTreasureInfo> getBidList(String[] bidStatus,String sortType,String sortBy,int limit,String isNoviceBid,int cgNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", bidStatus);
		map.put("limit", limit);
		map.put("cgNum", cgNum);
		if(StringUtils.isNotEmpty(isNoviceBid)){
			map.put("isNoviceBid", isNoviceBid);
		}
		if(StringUtils.isNotEmpty(sortType)){
			map.put("sortType", sortType);
		}
		if(StringUtils.isNotEmpty(sortBy)){
			map.put("sortBy", sortBy);
		}
		List<ShopTreasureInfo> list = this.shopTreasureDao.getBidInfoList(map);
		return list;
	}

	@Override
	public ShopTreasureInfo getBidInfo(int bidId,Integer cgNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", bidId);
		map.put("cgNum", cgNum);
		ShopTreasureInfo info = this.shopTreasureDao.getShopTreasureInfo(map);
		return info;
	}

	@Override
	public List<ShopTreasureInfo> getInvestmentBids(String[] bidStatus,String sortType,String sortBy,int limit,int preSaleBidlimit,int cgNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", bidStatus);
		map.put("limit", limit);
		map.put("preSaleBidlimit", preSaleBidlimit);
		map.put("cgNum", cgNum);
		if(StringUtils.isNotEmpty(sortType)){
			map.put("sortType", sortType);
		}
		if(StringUtils.isNotEmpty(sortBy)){
			map.put("sortBy", sortBy);
		}
		List<ShopTreasureInfo> list = this.shopTreasureDao.getInvestmentBids(map);
		//预售标
		map = new HashMap<String, Object>();
		map.put("limit", 1);
		List<ShopTreasureInfo> preSaleBids = this.shopTreasureDao.getPreSaleBids(map);
		if(preSaleBids!=null && preSaleBids.size()>0){
			list.addAll(preSaleBids);
		}
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getNoviceBidList(String[] bidStatus,int limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", bidStatus);
		map.put("limit", limit);


		List<ShopTreasureInfo> list = this.shopTreasureDao.getNoviceBidList(map);
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getBidPlansList(String[] status, String timestamp, int minDaysInteger, int maxDaysInteger, String sortType, String sortBy, Integer pageNo, Integer pagesize, int limit, String isNoviceBid) {
		return this.getBidPlansList(status, timestamp,  minDaysInteger,  maxDaysInteger,  sortType,  sortBy,  pageNo,  pagesize,  limit,  isNoviceBid,null);
	}

	@Override
	public List<ShopTreasureInfo> getBidPlansList(String[] status, String timestamp, int minDaysInteger, int maxDaysInteger, String sortType, String sortBy, Integer pageNo,Integer pagesize,int limit,String isNoviceBid,String timeSort) {
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
		if (StringUtils.isNotBlank(timeSort)) {
			map.put("timeSort", timeSort);
		}
		/*PageHelper.startPage(pageNo,pagesize,false);*/
		List<ShopTreasureInfo> list = this.shopTreasureDao.getBidPlansList(map);
		return list;
	}

	@Override
	public ShopTreasureInfo getPlanInfo(int planId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", planId);
		ShopTreasureInfo info = this.shopTreasureDao.getPlanInfo(map);
		return info;
	}

	@Override
	public List<PlanBidsStatus> getPlanBidsStatus(int planId,int limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("planId", planId);
		map.put("limit",limit);
		return this.shopTreasureDao.getPlanBidsStatus(map);
	}

	@Override
	public int isPlanBid(int bidId) {
		Map<String, Object> map = new HashMap<>();
		map.put("bidId",bidId);
		return this.shopTreasureDao.isPlanBid(map);
	}

	@Override
	public List<ShopTreasureInfo> getInvestmentBidsAndPlan(Map map) {
			List<ShopTreasureInfo> list = this.shopTreasureDao.getInvestmentBidsAndPlan(map);
			//预售标
			map = new HashMap<String, Object>();
			map.put("limit", 1);
			List<ShopTreasureInfo> preSaleBids = this.shopTreasureDao.getPreSaleBids(map);
			if(preSaleBids!=null && preSaleBids.size()>0){
				list.addAll(preSaleBids);
			}
			return list;

	}

	@Override
	public void checkCanInvestBid(int bidId, int userId,String versionType) {
		//如果是定向标 需要验证是否符合条件　ＡＮＤ　校验消费信贷计划内的标不能通过这里投
		DirectionalBid directionalBid = this.getDirectionalBid(bidId);
		if (directionalBid == null || directionalBid.getBidId() <= 0) {
			return;
		}

		if (directionalBid.getInnerPlan() == 1) {
			throw new BusinessException(ResponseCode.BID_ANYTIME_QUIT_USER_INVEST);
		}

		/*//用户资产总额
		BigDecimal totalAssets = BigDecimal.ZERO;
		totalAssets = userInfoService.getUserTotalAssets(userId);

		//计算累计收益
		DueInAmount dueInAmount = tradeService.getDueInAmount(userId); //待收本息
		BigDecimal YHGains = tradeService.getYHGains(String.valueOf(userId)); //已获收益
		int whiteBoardContain = userInfoService.checkWhiteBoard(userId);*/

		DueInAmount bidDueInAmount = null;
		DueInAmount planDueInAmount = null;
		Map<String, Object> userAssetsData = null;
		if(VersionTypeEnum.CG.getCode().equals(versionType)){
			bidDueInAmount = tradeService.getNewDueInAmount(userId,VersionTypeEnum.CG.getIndex());//标待收本息
			planDueInAmount = tradeService.getPlanDueInAmount(userId,VersionTypeEnum.CG.getIndex());//计划待收本息
			userAssetsData = userInfoService.getUserAssetsByXW(userId);
		}else{
			bidDueInAmount = tradeService.getNewDueInAmount(userId,VersionTypeEnum.PT.getIndex());//标待收本息
			planDueInAmount = tradeService.getPlanDueInAmount(userId,VersionTypeEnum.PT.getIndex());//计划待收本息
			userAssetsData = userInfoService.getUserAssets(userId);
		}

		//计算累计收益
		//待收本息
		DueInAmount dueInAmount = bidDueInAmount == null ? new DueInAmount() : bidDueInAmount;
		if(bidDueInAmount == null){
			bidDueInAmount = new DueInAmount();
		}
		if(planDueInAmount != null){
			dueInAmount.setPrincipal(planDueInAmount.getPrincipal().add(bidDueInAmount.getPrincipal()));
			dueInAmount.setGains(planDueInAmount.getGains().add(bidDueInAmount.getGains()));
		}
		//用户资产总额
		BigDecimal totalAssets = new BigDecimal((userAssetsData.get("totalAssets")).toString());
		//已获收益
		BigDecimal YHGains = new BigDecimal((userAssetsData.get("historyGains")).toString());
		int whiteBoardContain = userInfoService.checkWhiteBoard(userId);


		boolean assestFlag = false;
		if (directionalBid.getTotalUserAssets() != null) {
			assestFlag = directionalBid.getTotalUserAssets().compareTo(totalAssets) <= 0;
		}
		boolean interestFlag = false;
		if (directionalBid.getUserAccumulatedIncome() != null && dueInAmount != null) {
			interestFlag = directionalBid.getUserAccumulatedIncome().compareTo(dueInAmount.getGains().add(YHGains)) <= 0;
		}
		boolean investFlag = false;
		if (directionalBid.getUserInvestAmount() != null && dueInAmount != null) {
			investFlag = directionalBid.getUserInvestAmount().compareTo(dueInAmount.getPrincipal()) <= 0;
		}
		boolean whiteFlag = false;
		if (directionalBid.getTargetUser() == 1) {
			whiteFlag = whiteBoardContain >= 1;
		}
		//满足任一提交可投：1 在投金额 2 累计收益 3 白名单用户
		if (assestFlag || interestFlag || investFlag || whiteFlag) {
			return;
		}
		throw new BusinessException(ResponseCode.BID_ANYTIME_QUIT_USER_INVEST);
	}

	@Override
	public List<ShopTreasureInfo> getBidList(String[] bidStatus, String timestamp, int limit, String isNoviceBid, String isFull, int anytimeQuit, BigDecimal bidInterest) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", bidStatus);
		map.put("limit", limit);
		map.put("anytimeQuit", anytimeQuit);
		map.put("bidInterest", bidInterest);
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
	public List<ShopTreasureInfo> getBidRecommedList(String[] bidStatus, int limit, String isNoviceBid, int anytimeQuit, BigDecimal bidInterest) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", bidStatus);
		map.put("limit", limit);
		map.put("anytimeQuit", anytimeQuit);
		map.put("bidInterest", bidInterest);
		if(StringUtils.isNotEmpty(isNoviceBid)){
			map.put("isNoviceBid", isNoviceBid);
		}
		List<ShopTreasureInfo> list = this.shopTreasureDao.getCloseShopTreasureRecommend(map);
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getNoviceBidList(String[] bidStatus, int limit, int anytimeQuit, BigDecimal bidInterest) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", bidStatus);
		map.put("limit", limit);
		map.put("anytimeQuit", anytimeQuit);
		map.put("bidInterest", bidInterest);

		List<ShopTreasureInfo> list = this.shopTreasureDao.getNoviceBidList(map);
		return list;
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
	public List<ShopTreasureInfo> getPlansList(String[] status, String timestamp, int minDaysInteger, int maxDaysInteger, String sortType, String sortBy, Integer pageNo, Integer pagesize, int limit, String isNoviceBid, int type,String timeSort,int timeBid,int cgNum) {
		Map<String, Object> map = new HashMap<String, Object>();

		boolean flag = false;
		if (status != null){
			for (String s : status){
				if (Status.YJQ.name().equals(s)){
					flag = true;
					break;
				}
			}
		}
		if (flag){
			map.put("orderByStatus", 1); //已满标按照先满额后放款排序
		}
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
					map.put("YFB", "1");
				}

			}
		}
		int size = statusNum.size();
		Integer[] statusArray =(Integer[])statusNum.toArray(new Integer[size]);
		map.put("status", statusArray);
		map.put("minDays", minDaysInteger);
		map.put("maxDays", maxDaysInteger);
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
		if(cgNum != -1){
			map.put("cgNum", cgNum);
		}
		List<ShopTreasureInfo> list = this.shopTreasureDao.getPlansList(map);
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getBidListAndPlanList(Map map) {
		String status[] =(String[]) map.get("status");
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
					map.put("YFB","1");
				}

			}
		}
		int size = statusNum.size();
		Integer[] statusArray =(Integer[])statusNum.toArray(new Integer[size]);
		map.put("statusArray", statusArray);
		List<ShopTreasureInfo> list = this.shopTreasureDao.getBidListAndPlanList(map);
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getBidListAndPlanListOld(Map map) {
		String status[] =(String[]) map.get("status");
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
		map.put("statusArray", statusArray);
		List<ShopTreasureInfo> list = this.shopTreasureDao.getBidListAndPlanListOld(map);
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getOtherBidList(String[] bidStatus, String timestamp, int limit, String isNoviceBid, String isFull, String bidOrigin,int cgNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", bidStatus);
		if(limit!=-1) {
			map.put("limit", limit);
		}
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
        if(cgNum != -1){
			map.put("cgNum", cgNum);
		}
		List<ShopTreasureInfo> list = this.shopTreasureDao.getOtherCloseShopTreasure(map);
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
	public ShopTreasureInfo getPlanDetail(int planId, int versionType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", planId);
		map.put("versionType", versionType);
		ShopTreasureInfo info = this.shopTreasureDao.getPlanDetail(map);
		return info;
	}

	@Override
	public List<ShopTreasureInfo> getPlansListOrderBy(String[] status, int limit, String isNoviceBid,String orderType,int cgNum,int minDay,int maxDay,String statusOrder) {
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
					map.put("TBZ","1");
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
		map.put("planStatus", statusArray);
		map.put("status", status);
		map.put("orderType", orderType);
		map.put("statusOrder", statusOrder);
		if( !Arrays.asList(status).contains("TBZ")){
			map.put("fullTime", 1);
		}
		if(StringUtils.isNotEmpty(isNoviceBid)){
			if(isNoviceBid.equals("S")) {
				map.put("isNovicePlan", 1);
			}else {
				map.put("isNovicePlan", 0);
			}
		}
		map.put("isNoviceBid", isNoviceBid);
		map.put("limit",limit);
		map.put("cgNum",cgNum);
		if(minDay!=-1){
			map.put("minDays",minDay);
		}
		if(maxDay!=-1){
			map.put("maxDays",maxDay);
		}
		List<ShopTreasureInfo> list = this.shopTreasureDao.getPlansListOderBy(map);
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getStickBidAndPlanList(String[] bidStatus, int limit, String isNoviceBid,String productType,int cgNum,int recommend) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", bidStatus);
		if(limit!=-1) {
			map.put("limit", limit);
		}
		if (StringUtils.isNotEmpty(isNoviceBid)) {
			map.put("isNoviceBid", isNoviceBid);
			if(isNoviceBid.equals("S")){
				map.put("isNovicePlan", "1");
			}else {
				map.put("isNovicePlan", "0");
			}
		}
		if (recommend != -1) {
			map.put("recommend", String.valueOf(recommend));
		}
		if(!StringUtils.isEmpty(productType)){
			map.put("productType", productType);
		}else {
			map.put("productType", "all");
		}
		if(cgNum!=-1) {
			map.put("cgNum", cgNum);
		}
		List<ShopTreasureInfo> list = this.shopTreasureDao.getStickBidAndPlanTreasure(map);
		return list;
	}

	@Override
	public List<ShopTreasureInfo> getStickBidAndPlanList(String[] bidStatus, int limit, String isNoviceBid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", bidStatus);
		if(limit!=-1) {
			map.put("limit", limit);
		}
		if (StringUtils.isNotEmpty(isNoviceBid)) {
			map.put("isNoviceBid", isNoviceBid);
		}
		List<ShopTreasureInfo> list = this.shopTreasureDao.getStickBidAndPlanTreasureOld(map);
		return list;
	}

	@Override
	public int isCGProduct(int id, String productType) {
		Map<String, Object> map = new HashMap<>();
		if(StringUtils.isNotEmpty(productType)&&Integer.valueOf(productType)==2){
			map.put("planId",id);
            return this.shopTreasureDao.isPlanBid(map);
		}else{
			map.put("bidId",id);
			return this.shopTreasureDao.isCGBid(map);
		}

	}

	@Override
	public BidBorrowerInfoAfterLoan getBidBorrowerInfoAfterLoan(Integer bidId) {
		return bidInfoDao.getBidBorrowerInfoAfterLoan(bidId);
	}

	@Override
	public List<Map<String,Object>> pieceTogetherBorrowerInfo(BidBorrowerInfo bbInfo, BidBorrowerInfoAfterLoan bidBorrowerInfoAfterLoan, ShopTreasureInfo info) {
		BorrowerInfoVO2 borrowerInfoVO = new BorrowerInfoVO2();
		if(bbInfo != null) {
            UserInfo userInfo = userInfoService.getUserInfo(bbInfo.getUser_id());
			if(InterfaceConst.USER_TYPE_FZRR.equals(userInfo.getUserType())){ //借款人主体性质是法人
				Map<String, Object> enterpriseInfo = userInfoService.getEnterpriseInfo(bbInfo.getUser_id());
				if (enterpriseInfo != null){
					borrowerInfoVO.setEnterpriseName(StringHelper.getStringWithSomeStar((String) enterpriseInfo.get("enterpriseName"),2, 1, 3));
					String enterpriseNum = null;
					if (enterpriseInfo.get("unifiedCode") != null){
						enterpriseNum = (String)enterpriseInfo.get("unifiedCode");  //统一社会信用代码
					}else {
						enterpriseNum = (String)enterpriseInfo.get("businessLicense");  //营业执照登记注册号
					}
					if (enterpriseNum != null){
						borrowerInfoVO.setEnterpriseRegisterNo(StringHelper.getStringWithSomeStar(enterpriseNum, 2, 4 , 3));
					}
				}
			} else {  //借款人为个人
				if(bbInfo.getRealName() != null) {
					borrowerInfoVO.setBorrower(StringHelper.getNameWithStarByName(userInfo.getFullName()));
				}
				if(bbInfo.getRealCreditCardNum() != null) {
					try {
						borrowerInfoVO.setIdentificationNumber(StringHelper.getStringWithSomeStar(StringHelper.decode(userInfo.getIdCardEncrypt()), 3, 4, 3));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			borrowerInfoVO.setAddress(StringHelper.getStringPrefixWithSomeStar(bbInfo.getAddress(),5,5));
			if(bbInfo.getSubjectNature() != null) {
				borrowerInfoVO.setSubjectCharacter(SubjectCharacterEnum.getSubjectCharacter(bbInfo.getSubjectNature()));
			}
			borrowerInfoVO.setWhatTrade(bbInfo.getIndustry());
			borrowerInfoVO.setIncomeAndDebt(bbInfo.getBorrower_info());
			borrowerInfoVO.setCreditInvestigation(bbInfo.getCredit());
			borrowerInfoVO.setBankFlow(bbInfo.getBank_transaction());
			borrowerInfoVO.setOtherBorrowerInfo(bbInfo.getOtherBorrowerInfo());
		}

		boolean bidBorrowerInfoAfterLoanFlag = false;  //是否需要返回贷后信息
		if (info.getInterestDate() != null) {  //起息时间次月1号起才返回贷后信息
			Date nowRemainMonth = com.fenlibao.p2p.util.api.DateUtil.dateRemainPart(new Date(), com.fenlibao.p2p.util.api.DateUtil.yyyy_MM);
			Date interestDateRemainMonth = com.fenlibao.p2p.util.api.DateUtil.dateRemainPart(info.getInterestDate(), com.fenlibao.p2p.util.api.DateUtil.yyyy_MM);
			if(nowRemainMonth.getTime() > interestDateRemainMonth.getTime()){
				bidBorrowerInfoAfterLoanFlag = true;
			}
		}
		if (bidBorrowerInfoAfterLoanFlag) {
			if (bidBorrowerInfoAfterLoan != null) {
				borrowerInfoVO.setUseDetail(bidBorrowerInfoAfterLoan.getUseDetail() == null ? "未见异常" : bidBorrowerInfoAfterLoan.getUseDetail());
				borrowerInfoVO.setBorrowerManageAndFinanceCondition(bidBorrowerInfoAfterLoan.getFinanceDetail() == null ? "未见异常" : bidBorrowerInfoAfterLoan.getFinanceDetail());
				borrowerInfoVO.setBorrowerRepayAbilityVarietyCondition(bidBorrowerInfoAfterLoan.getRepayAbility() == null ? "未见异常" : bidBorrowerInfoAfterLoan.getRepayAbility());
				borrowerInfoVO.setBorrowerOverdueCondition(bidBorrowerInfoAfterLoan.getOverdueDetail() == null ? "未见异常" : bidBorrowerInfoAfterLoan.getOverdueDetail());
				borrowerInfoVO.setBorrowerSueCondition(bidBorrowerInfoAfterLoan.getLawsuitDetail() == null ? "未见异常" : bidBorrowerInfoAfterLoan.getLawsuitDetail());
				borrowerInfoVO.setBorrowerAdministrativePenaltyCondition(bidBorrowerInfoAfterLoan.getPunishDetail() == null ? "未见异常" : bidBorrowerInfoAfterLoan.getPunishDetail());
			}else {
				borrowerInfoVO.setUseDetail("未见异常");
				borrowerInfoVO.setBorrowerManageAndFinanceCondition("未见异常");
				borrowerInfoVO.setBorrowerRepayAbilityVarietyCondition("未见异常");
				borrowerInfoVO.setBorrowerOverdueCondition("未见异常");
				borrowerInfoVO.setBorrowerSueCondition("未见异常");
				borrowerInfoVO.setBorrowerAdministrativePenaltyCondition("未见异常");
			}
		}
		return CommonTool.voToListMap(borrowerInfoVO);
	}

	@Override
	public List<Map<String,Object>> pieceTogetherProjectInfo(ShopTreasureInfo info) {
		ProjectInfoVO projectInfoVO = new ProjectInfoVO();
		if(info == null){
			return new ArrayList<>();
		}
		projectInfoVO.setProjectName(info.getName());
		if (info.getLoanAmount() != null) {
			projectInfoVO.setBorrowAmount(StringHelper.formatNumber(info.getLoanAmount(), StringHelper.AMOUNT_FORMAT) + "元");
		}
		projectInfoVO.setBorrowTimeLimit(info.getLoanDays() != 0 ? info.getLoanDays() + "天" : info.getMonth() + "个月");
		String exceptInterestString = StringHelper.formatNumber(info.getRate() * 100, StringHelper.INTEREST_FORMAT) + "%";
		if (info.getInterest() >= 0.001){  //如果标有加息
			exceptInterestString += "+" + StringHelper.formatNumber(info.getInterest() * 100, StringHelper.INTEREST_FORMAT) + "%";
		}
		projectInfoVO.setExceptAnnualised(exceptInterestString);
		projectInfoVO.setBorrowPurpose(info.getFundPurpose());
		projectInfoVO.setAuthenticationType(info.getAssetsType());
		projectInfoVO.setRepaymentSource(info.getRepaymentSource());
		projectInfoVO.setRepaymentType(RepaymentTypeEnum.getRepaymentType(info.getRepaymentMode()));
		projectInfoVO.setInterestWay("按天计息");
		projectInfoVO.setInterestTime(info.getInterestDate() == null ? "放款起息" : DateUtil.getDate(info.getInterestDate()));
		Integer canDebtTransfer = Integer.parseInt(Config.get("fenlibao.canDebtTransfer"));
		if (canDebtTransfer == 1 && "0001".equals(info.getBidOrigin())){
			projectInfoVO.setEarlyQuit("起息3天后可申请提前退出");//info.getAnytimeQuit() == 1 ? "支持" : "不支持");
		} else {
			projectInfoVO.setEarlyQuit("不支持");
		}
		projectInfoVO.setGuaranteeMeasure(GuaranteeMeasureEnum.getGuaranteeMeasure(info.getGuaranteeMeasure()));
		return CommonTool.voToListMap(projectInfoVO);
	}

	@Override
	public List<Map<String,Object>> pieceTogetherPlanInfo(ShopTreasureInfo info) {
		PlanInfoVO planInfoVO = new PlanInfoVO();
		planInfoVO.setProjectName(info.getName());
		if(info.getLoanAmount() != null) {
			planInfoVO.setPlanAmount(StringHelper.formatNumber(info.getLoanAmount(), StringHelper.AMOUNT_FORMAT) + "元");
		}
		planInfoVO.setPlanTimeLimit(info.getLoanDays() != 0 ? info.getLoanDays() + "天" : info.getMonth() + "个月");
		String exceptInterestString = StringHelper.formatNumber(info.getRate() * 100, StringHelper.INTEREST_FORMAT) + "%";
		if (info.getInterest() >= 0.001){  //如果计划有加息
			exceptInterestString += "+" + StringHelper.formatNumber(info.getInterest() * 100, StringHelper.INTEREST_FORMAT) + "%";
		}
		planInfoVO.setExceptAnnualised(exceptInterestString);
		planInfoVO.setInterestWay("按天计息");
		planInfoVO.setInterestTime(info.getInterestDate() == null ? "放款起息" : DateUtil.getDate(info.getInterestDate()));
		planInfoVO.setEarlyQuit(info.getAnytimeQuit() == 1 ? "支持" : "不支持");
		if(info.getRepaymentMode() != null) {
			planInfoVO.setRepaymentType(RepaymentTypeEnum.getRepaymentType(info.getRepaymentMode()));
		}
		planInfoVO.setGuaranteeMeasure(PlanGuaranteeMeasureEnum.getGuaranteeMeasure(info.getGuaranteeMeasure()));
		return CommonTool.voToListMap(planInfoVO);
	}

	@Override
	public Map<String, Object> projectCreditRightList(Integer planId, Integer pageIndex, Integer pageSize) {
		Integer offset = (pageIndex - 1) * pageSize;  //计算偏移量

		Map<String, Object> map = new HashMap<>();
		Integer planBindBidCount = bidInfoDao.countPlanBindBid(planId);  //该计划绑定标数量
		Integer totalPages = planBindBidCount / pageSize;
		if (planBindBidCount % pageSize > 0){
			totalPages++;
		}

		List<Integer> bidList = bidInfoDao.getPlanBindBidList(planId, offset, pageSize);  //该计划绑定标列表
		List<Map<String, Object>> items = new ArrayList<>();
		for (Integer bidId : bidList){
			Map<String, Object> borrowInfo = new  HashMap<>();
			try {
				borrowInfo.put("bidId", bidId);
				BidBaseInfo bidBaseInfo = bidInfoDao.getBidBaseInfo(bidId);
				if(bidBaseInfo != null){
					borrowInfo.put("projectNum", bidBaseInfo.getBidTitle());
					borrowInfo.put("loanMoney", bidBaseInfo.getLoanAmount());
				}
				BidBorrowerInfo bbInfo = bidInfoDao.getBidBorrowerInfo(bidId);
				if(bbInfo != null){
					if(bbInfo.getRealName() != null){
						borrowInfo.put("borrower", StringHelper.getNameWithStarByName(bbInfo.getRealName()));
					}
				}
				items.add(borrowInfo);
			}catch (Exception e){
				logger.error("获取计划中相应标的借款人信息错误", e);
			}
		}

		map.put("pageIndex", pageIndex);
		map.put("pageSize", pageSize);
		map.put("totalCount", planBindBidCount);  //总共多少条数据
		map.put("totalPages", totalPages);  //总共多少页
		map.put("items", items);
    	return map;
	}

	@Override
	public BidDetailVO pieceBidDetailVO(ShopTreasureInfo info, BidBorrowerInfo bbInfo, String bidId, String userId, String id) throws Exception{
		BidDetailVO vo = new BidDetailVO();

		vo.setPlanCanQuit(1);
		if (info.getIsNoviceBid().equals("S")) {
			vo.setPlanCanQuit(0);
		}

		vo.setNoviceBIdlimit(Config.get("bid.novice.invest.limit"));//新手标额度
		vo.setBidId(info.getId());
		vo.setBidTitle(info.getName());
		vo.setPrecisionTitle(info.getName());//全称

		vo.setBidYield(String.valueOf(info.getRate()));
		vo.setRepaymentMode(info.getRepaymentMode());
		vo.setLoanDays(info.getLoanDays());
		vo.setLoanMonth(info.getMonth());
		vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name()) ? 1 : 0);
		vo.setBidClassify(Status.S.name().equals(info.getIsNoviceBid()) ? 1 : 0);
		//判断是否定向标
		if (info.getUserTotalAssets() != null) {
			vo.setBidClassify(2);
			vo.setUserTotalAssets(info.getUserTotalAssets().toPlainString());
		}
		vo.setBidType(info.getBidType());
		vo.setRemark(info.getBorrowingDescribe());
		vo.setBorrowerUrl(Config.get("bid.borrower.url") + bidId);
		vo.setPurchaseTotal(info.getLoanAmount().doubleValue());
		vo.setPurchasedTotal(info.getLoanAmount().subtract(info.getVoteAmount()).doubleValue());
		vo.setProgressRate(info.getLoanAmount(), info.getVoteAmount());

		//标的状态
		if (Status.TBZ.name().equals(info.getStatus())) {
			vo.setBidStatus(1);
		}
		if (Status.DFK.name().equals(info.getStatus())) {
			if (info.getFundraisDate() != null && (info.getFundraisDate().getTime() > DateUtil.nowDate().getTime())) {
				vo.setBidStatus(2);
			} else {
				vo.setBidStatus(3);
			}
		}
		if (Status.HKZ.name().equals(info.getStatus())) {
			vo.setBidStatus(4);
		}
		if (Status.YJQ.name().equals(info.getStatus())) {
			vo.setBidStatus(5);
		}
		vo.setLawFiles(getBidPublicAccessoryFiles(Integer.valueOf(id)));
		vo.setLawFileUrl(getBidPublicAccessoryFileUrl(Integer.valueOf(id)));
		vo.setFwxyUrl(userInfoService.getUserServiceAgreementUrl(Integer.valueOf(StringUtils.isBlank(userId) ? "-1" : userId)));
		//是否已申购
		if (StringUtils.isNoneEmpty(userId)) {
			BidBaseInfo bidInfo = getBidBaseInfoByUser(Integer.valueOf(userId), Integer.valueOf(id));
			if (bidInfo != null) {
				vo.setPurchasedStatus(1);
			} else {
				vo.setPurchasedStatus(0);
			}
		}
		//担保借款合同
		if (BidOriginEnum.qqm.getCode().equals(info.getBidOrigin())) {
			vo.setGuaranteeFileUrl(Config.get("bid.guaranteeFile.url"));
		}
		//设置抢购标参数 20161214 by:kris
		if (info.getPanicBuyingTime() != null) {
			long timeLeft = (info.getPublishDate().getTime() - System.currentTimeMillis()) / 1000 + InterfaceConst.EEROR_SECOND;
			Integer countdown = Integer.valueOf(Config.get("bid.countdown.mimute")) * 60;
			vo.setTimeLeft(timeLeft > 0 ? timeLeft : 0);
			vo.setCountdown(countdown);
			vo.setPanicBuyingTime(DateUtil.dateToTimestampToSec(info.getPublishDate()));
		}

		vo.setIsCG(info.getIsCG());//是否是存管类型的标 1:不是 2:是
		vo.setInvestLimit("100");
		vo.setAnytimeQuit(info.getAnytimeQuit());
		List<String> bidLabels = new ArrayList<>();
		if (info.getUserTotalAssets() != null || info.getAccumulatedIncome() != null || info.getUserInvestAmount() != null || info.getTargetUser() == 1) {
			bidLabels.add("大客户专享");
		}
		if (info.getAnytimeQuit() == 1) {
			bidLabels.add("随时退出");
		}
		if (info.getIsCG() == 2) {
			bidLabels.add("银行存管");
		}
		String[] selfLabel = StringUtils.isNotEmpty(info.getBidLabel()) ? info.getBidLabel().split(",") : new String[0];
		for (int i = 0; i < selfLabel.length; i++) {
			bidLabels.add(selfLabel[i]);
		}
		if (Status.S.name().equals(info.getIsNoviceBid())) {
			bidLabels.add("新手专享");
		}

		//标签最多显示两个
		if (bidLabels != null && bidLabels.size() > 0) {
			bidLabels = bidLabels.subList(0, bidLabels.size() >= 2 ? 2 : bidLabels.size());
			vo.setBidLabel(bidLabels);
		}

		List<String> markList = new ArrayList<>();
		if (info.getAccumulatedIncome() != null) {
			markList.add("累计利息达" + df.format(Double.valueOf(info.getAccumulatedIncome())) + "元客户专享");
		}
		if (info.getUserTotalAssets() != null) {
			markList.add("账户资产达" + df.format(info.getUserTotalAssets()) + "元客户专享");
		}
		if (info.getUserInvestAmount() != null) {
			markList.add("已出借额达" + df.format(info.getUserInvestAmount()) + "客户专享");
		}
		if (info.getTargetUser() == 1) {
			markList.add("vip客户专享");
		}
		vo.setMarkArray(markList);
		vo.setAccumulatedIncome(info.getAccumulatedIncome());
		vo.setTargetUser(info.getTargetUser());
		vo.setInvestingAmount(info.getInvestingAmount());

		if (info.getInterest() != 0) {
			vo.setInterest(info.getInterest() < 0.001 ? "0" : Double.toString(info.getInterest()));
		} else {
			vo.setInterest("0.00");
		}
		vo.setItemType(0);
		vo.setComment(info.getComment());

		vo.setHistoryInfoFlag(true);
		if (bbInfo != null) {
			vo.setBase(bbInfo.getBorrower_info());
			vo.setCredit(bbInfo.getCredit());
			vo.setBankTransaction(bbInfo.getBank_transaction());
			vo.setRisk(bbInfo.getRisk());
			vo.setHistoryInfoFlag(false);
		}
		if (info.getPublishDate() != null && info.getFundraisDate() != null){
			String publishDate = com.fenlibao.p2p.util.api.DateUtil.DateToString(info.getPublishDate(), com.fenlibao.p2p.util.api.DateUtil.yyyy_MM_dd);
			Date expiryDateSubOneSecond = com.fenlibao.p2p.util.api.DateUtil.timeAddOrSub(info.getFundraisDate() , Calendar.SECOND, -1);
			String expiryDate = com.fenlibao.p2p.util.api.DateUtil.DateToString(expiryDateSubOneSecond, com.fenlibao.p2p.util.api.DateUtil.yyyy_MM_dd);
			String recruitmentPeriod = publishDate + "至" + expiryDate + "。如到期仍未满标，出借资金将原路返还至用户账户。";
			vo.setRecruitmentPeriod(recruitmentPeriod);
		}
		vo.setRiskAnnounceUrl(Config.get("risk.announce.url"));
    	return vo;
	}

	@Override
	public PlanDetailVO piecePlanDetailVO(ShopTreasureInfo info, String planId, String userId, String id, VersionTypeEnum versionType) throws Exception{
		PlanDetailVO vo = new PlanDetailVO();
		info.setStatus(StatusUtil.status(info.getStatus()));
		if (!info.getIsNoviceBid().equals("S")) {
			vo.setPlanCanQuit(1);
		}

		vo.setPlanId(info.getId());
		vo.setPlanTitle(info.getName());
		vo.setBidYield(String.valueOf(info.getRate()));
		vo.setRepaymentMode(info.getRepaymentMode());
		vo.setLoanDays(info.getLoanDays());
		vo.setLoanMonths(info.getMonth());
		vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name()) ? 1 : 0);

		vo.setRemark(info.getBorrowingDescribe());
		vo.setPurchaseTotal(String.valueOf(info.getLoanAmount()));
		vo.setPurchasedTotal(String.valueOf(info.getLoanAmount().subtract(info.getVoteAmount())));
		double voteAmount = Double.valueOf(info.getVoteAmount().toString());
		vo.setVoteAmount(String.valueOf(voteAmount));
		vo.setProgressRate(info.getLoanAmount(), new BigDecimal(voteAmount));
		vo.setRaisedRate(info.getInterest() < 0.001 ? 0 : info.getInterest());
		int isPurchased = 0;
		if (!StringUtils.isEmpty(userId)) {
			if (tradeService.getpurchasedNewPlan(Integer.valueOf(userId), Integer.valueOf(planId)) > 0) {
				isPurchased = 1;
			}
		}

		vo.setPlanTitle(info.getName());
		vo.setPrecisionTitle(info.getName());//全称
		if (!Status.TBZ.name().equals(info.getStatus())) {
			vo.setVoteAmount("0.00");
		}

		//标的状态
		if (Status.TBZ.name().equals(info.getStatus())) {
			vo.setPlanStatus(1);
		}
		if (Status.DFK.name().equals(info.getStatus())) {
			if (info.getFundraisDate().getTime() > DateUtil.nowDate().getTime()) {
				vo.setPlanStatus(2);
			} else {
				vo.setPlanStatus(3);
			}
		}
		if (Status.HKZ.name().equals(info.getStatus())) {
			vo.setPlanStatus(4);
		}
		if (Status.YJQ.name().equals(info.getStatus())) {
			vo.setPlanStatus(5);
		}
		vo.setLawFiles(getBidPublicAccessoryFiles(Integer.valueOf(id)));
		vo.setLawFileUrl(getBidPublicAccessoryFileUrl(Integer.valueOf(id)));
		//服务协议
		String fwxy = getGroupItemValue(info.getId(), InterfaceConst.BID_EXTEND_ASSIGNMENT_CODE);
		if (versionType.getCode().equals("CG")) {
			vo.setFwxyUrl(userInfoService.getUserServicePlanAgreementUrlByCG(Integer.valueOf(StringUtils.isBlank(userId) ? "-1" : userId), info.getIsNoviceBid(), info.getPlanType()));
		} else {
			vo.setFwxyUrl(userInfoService.getUserServicePlanAgreementUrl(Integer.valueOf(StringUtils.isBlank(userId) ? "-1" : userId), info.getIsNoviceBid(), info.getPlanType()));

		} //组信息
		List<BidExtendGroupVO> groupInfoList = new ArrayList<BidExtendGroupVO>();
		vo.setGroupInfoList(groupInfoList);

		//担保借款合同
		if (BidOriginEnum.qqm.getCode().equals(info.getBidOrigin())) {
			vo.setGuaranteeFileUrl(Config.get("bid.guaranteeFile.url"));
		}

		// TODO: 2016/12/19
		//设置抢购标参数
		if (info.getPanicBuyingTime() != null) {
			long timeLeft = (info.getPublishDate().getTime() - System.currentTimeMillis()) / 1000 + InterfaceConst.EEROR_SECOND;
			Integer countdown = Integer.valueOf(Config.get("bid.countdown.mimute")) * 60;
			vo.setTimeLeft(timeLeft > 0 ? timeLeft : 0);
			vo.setCountdown(countdown);
			vo.setPanicBuyingTime(DateUtil.dateToTimestampToSec(info.getPublishDate()));
		}
		if (info.getItemType() != 1) {
			vo.setInterest("0.00");//加息利率
		} else {
			vo.setInterest(info.getInterest() < 0.001 ? "0" : Double.toString(info.getInterest()));
		}
		vo.setInvestLimit("100");
		if (info.getIsNoviceBid().equals("S")) {
			vo.setPlanLimit("10000");
		}
		List<String> bidLabels = new ArrayList<>();
		if (info.getUserTotalAssets() != null || info.getAccumulatedIncome() != null || info.getUserInvestAmount() != null || info.getTargetUser() == 1) {
			bidLabels.add("大客户专享");
		}
		if (info.getAnytimeQuit() == 1) {
			bidLabels.add("随时退出");
		}
		if (info.getIsCG() == 2) {
			bidLabels.add("银行存管");
		}
		String[] selfLabel = StringUtils.isNotEmpty(info.getBidLabel()) ? info.getBidLabel().split(",") : new String[0];
		for (int i = 0; i < selfLabel.length; i++) {
			bidLabels.add(selfLabel[i]);
		}
		if (Status.S.name().equals(info.getIsNoviceBid())) {
			bidLabels.add("新手专享");
		}

		if (bidLabels != null && bidLabels.size() > 0) {
			bidLabels = bidLabels.subList(0, bidLabels.size() >= 2 ? 2 : bidLabels.size());
			vo.setBidLabel(bidLabels);
		}

		List<String> markList = new ArrayList<>();
		if (info.getAccumulatedIncome() != null) {
			markList.add("累计利息达" + df.format(Double.valueOf(info.getAccumulatedIncome())) + "元客户专享");
		}
		if (info.getUserTotalAssets() != null) {
			markList.add("账户资产达" + df.format(info.getUserTotalAssets()) + "元客户专享");
		}
		if (info.getUserInvestAmount() != null) {
			markList.add("已出借额达" + df.format(info.getUserInvestAmount()) + "客户专享");
		}
		if (info.getTargetUser() == 1) {
			markList.add("vip客户专享");
		}
		vo.setPlanType(Integer.valueOf(info.getPlanType()));
		vo.setMarkArray(markList);
		vo.setItemType(1);
		vo.setLowRate(String.valueOf(info.getLowRate()));
		vo.setHighRate(String.valueOf(info.getHighRate()));
		vo.setBonusRate(String.valueOf(info.getBonusRate()));
		vo.setComment(info.getComment());
		vo.setPurchasedStatus(isPurchased);
		//前台要求 添加存管参数
		vo.setIsCG(1);
    	return vo;
	}

	@Resource
	private SqlSession sqlSession;

	private static final String MAPPER = "BidInfoMapper.";

	@Override
	public int addBidInfoFromHistory(List<Integer> ids) {
		if(ids == null || ids.isEmpty())
			return 0;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ids",ids);
		return  sqlSession.insert(MAPPER + "addBidInfoFromHistory",map);
	}

	@Override
	public int addBidInvestrecordsFromHistory(List<Integer> ids) {
		if(ids == null || ids.isEmpty())
			return 0;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ids",ids);
		return  sqlSession.insert(MAPPER + "addBidInvestrecordsFromHistory", map);
	}

	@Override
	public String getNoSensitiveAgreementPath(String bidId) {
		return bidInfoDao.getNoSensitiveAgreementPath(bidId);
	}

	@Override
	public Map getSignIdAndDocId(String bidId) {
		return bidInfoDao.getSignIdAndDocId(bidId);
	}
}
