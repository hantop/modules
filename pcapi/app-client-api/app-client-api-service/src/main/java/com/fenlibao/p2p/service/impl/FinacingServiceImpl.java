
package com.fenlibao.p2p.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.dimeng.p2p.S62.entities.T6232;
import com.fenlibao.p2p.dao.BidRefundDao;
import com.fenlibao.p2p.dao.PublicAccessoryDao;
import com.fenlibao.p2p.dao.ShopTreasureDao;
import com.fenlibao.p2p.dao.UserInfoDao;
import com.fenlibao.p2p.dao.bid.PlanDao;
import com.fenlibao.p2p.dao.financing.FinacingDao;
import com.fenlibao.p2p.model.consts.earnings.EarningsTypeConst;
import com.fenlibao.p2p.model.entity.Finacing;
import com.fenlibao.p2p.model.entity.ShopInformation;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.finacing.*;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.global.FeeCode;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.Status;
import com.fenlibao.p2p.model.vo.*;
import com.fenlibao.p2p.service.FinacingService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.StringHelper;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import com.fenlibao.p2p.util.pay.CalculateEarningsUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

@Service
public class FinacingServiceImpl implements FinacingService {
	
	@Resource
	private FinacingDao finacingDao;
	
	@Resource
    private ShopTreasureDao shopTreasureDao;
	
	@Resource
    private BidRefundDao bidRefundDao;
	
	@Resource
	private PublicAccessoryDao publicAccessoryDao;
	
	@Resource
	private UserInfoDao userInfoDao;
	
	@Resource
	private ITradeService tradeService;
	
	@Resource
	private BidInfoService bidInfoService;

	@Resource
	private PlanDao planDao;

	@Override
	public BigDecimal getKdbEarning(String userId, Date earnDate) {
		return tradeService.getYesterdayEarnings(Integer.valueOf(userId), EarningsTypeConst.KDB);
	}
	
	/** 
	 * @Title: getFinacingList 
	 * @Description: 获取用户债权列表
	 * @param userId
	 * @return
	 * @return: FinacingResultVo
	 
	@Override
    public FinacingResultVo getFinacingList(String userId, int isUp, String timestamp) {
        FinacingResultVo result = new FinacingResultVo();

        //债权列表
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bidStatus", new String[]{Status.TBZ.name(), Status.DFK.name(), Status.HKZ.name(), Status.YJQ.name()});
        map.put("userId", userId);
        map.put("proType", InterfaceConst.PRO_TYPE_KDB);
        if (StringUtils.isNotEmpty(timestamp)) {
            map.put("time", DateUtil.timestampToDate(Long.valueOf(timestamp)));
        }
        List<Finacing> settleFinacingList = this.finacingDao.getFinacingList(map);

        List<FinacingVo> voList = new ArrayList<FinacingVo>();
        for (Finacing finacing : settleFinacingList) {
            FinacingVo vo = new FinacingVo();
            vo.setBuyTimestamp(finacing.getCreateTime().getTime());
            vo.setZqId(finacing.getId());
            vo.setZqTitle(finacing.getName());
            vo.setZqYield(String.valueOf(finacing.getRate()));
            vo.setZqTime(finacing.getPeriod());
            vo.setZqSum(finacing.getMoney());

            //债权的状态
            if (finacing.getIsTransfer().equals(Status.S.name())) {
                vo.setZqStatus(3);//转让中
            } else {
                if (finacing.getBidStatus().equals(Status.TBZ.name())) {
                    vo.setZqStatus(0);//已购买
                } else if (finacing.getBidStatus().equals(Status.HKZ.name())) {
                    vo.setZqStatus(1);//收益中
                } else if (finacing.getBidStatus().equals(Status.YJQ.name())) {
                    vo.setZqStatus(2);//已回款
                } else if (finacing.getBidStatus().equals(Status.DFK.name())) {
                    vo.setZqStatus(0);//已购买
                }
            }

            //债权已获取收益
            Map<String, Object> earMap = new HashMap<String, Object>();
            earMap.put("userId", userId);
            earMap.put("tradeType", InterfaceConst.TZ_LX);
            earMap.put("zqId", finacing.getId());
            earMap.put("status", Status.YH.name());
            double earnings = this.bidRefundDao.getPredictEarnings(earMap);
            vo.setZqEarning(earnings);

            voList.add(vo);
        }

        result.setZqList(voList);
        return result;
    }
	*/
	
	@Override
	public FinacingDetailVo getFinacingDetail(String userId, String zqId) {
		
		Map<String,Object> detailMap=new HashMap<String,Object>();
		detailMap.put("userId", userId);
		detailMap.put("zqId", zqId);
		Finacing finacingDetail = this.finacingDao.getFinacingDetail(detailMap);
		
		if(finacingDetail==null)
			return null;
		FinacingDetailVo finacingDetailVo = new FinacingDetailVo();
		if("S".equals(finacingDetail.getIsTransfer())){//转让中
			finacingDetailVo.setZqStatus(3);
		}else if("F".equals(finacingDetail.getIsTransfer())){
			switch(finacingDetail.getBidStatus()){
				case "TBZ":
					finacingDetailVo.setZqStatus(0);
					break;
				case "DFK":
					finacingDetailVo.setZqStatus(0);
					break;
				case "HKZ":
					finacingDetailVo.setZqStatus(1);
					break;
				case "YJQ":
					finacingDetailVo.setZqStatus(2);
					break;
				case "YDF":
					finacingDetailVo.setZqStatus(2);
					break;
			}
		}
		finacingDetailVo.setKdbId(finacingDetail.getBidId());
		finacingDetailVo.setZqTitle(finacingDetail.getName());
		finacingDetailVo.setZqSum(finacingDetail.getMoney());
		finacingDetailVo.setZqYield(String.valueOf(finacingDetail.getRate()));
		finacingDetailVo.setZqTime(finacingDetail.getPeriod());
		finacingDetailVo.setZqEarning(finacingDetail.getZqEarning());
		finacingDetailVo.setBuyTimestamp(finacingDetail.getBuyTimestamp().getTime());
		finacingDetailVo.setInterestTimestamp(finacingDetail.getInterestTimestamp().getTime());
		finacingDetailVo.setEndTimestamp(finacingDetail.getEndTimestamp().getTime());
		finacingDetailVo.setShopDetailUrl(Config.get("shop.detail.url")+finacingDetail.getBidId());//产品明细
		finacingDetailVo.setShopInfoUrl(Config.get("shop.info.url")+finacingDetail.getBidId());//店铺信息
		finacingDetailVo.setOriginalMoney(finacingDetail.getOriginalMoney());//原始债权金额
		
		//已过天数的收益（未到账）
		int passedDays = DateUtil.getDayBetweenDates(finacingDetail.getCreateTime(),DateUtil.nowDate());//已算收益天数
		int totalDays = DateUtil.getDayBetweenDates(finacingDetail.getCreateTime(),finacingDetail.getEndTimestamp());//当前债权一共计息的天数
		if(totalDays>0){
			BigDecimal passedEarning = new BigDecimal(finacingDetail.getZqEarning()).divide(BigDecimal.valueOf(totalDays),2,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(passedDays));
			finacingDetailVo.setPassedEarning(passedEarning.doubleValue());
			finacingDetailVo.setSurplusDays(totalDays-passedDays);
		}
		//用户信息
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("userId", userId);
		UserInfo userInfo = this.userInfoDao.getUserInfoByPhoneNumOrUsername(userMap);
				
		//服务协议
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("userName", userInfo.getFullName().substring(0,1)+"**");
		paramMap.put("phone", StringHelper.replace(3, 7, userInfo.getUsername(), "****"));
		paramMap.put("IDNumber", userInfo.getIdCard());
		if(finacingDetail.getPeriod()==12){
			finacingDetailVo.setServiceAgreement(CommonTool.convertParamToUrl(Config.get("shop.serviceAgreementC.url"),paramMap));
        }else if(finacingDetail.getPeriod()==6){
        	finacingDetailVo.setServiceAgreement(CommonTool.convertParamToUrl(Config.get("shop.serviceAgreementB.url"),paramMap));
        }else if(finacingDetail.getPeriod()==3){
        	finacingDetailVo.setServiceAgreement(CommonTool.convertParamToUrl(Config.get("shop.serviceAgreementA.url"),paramMap));
        }
		
		//判断是否是新手标  (laubrence 2015-12-15 19:49:09)
		finacingDetailVo.setIsNoviceBid(0);
        if("S".equals(finacingDetail.getIsNoviceBid())){
        	finacingDetailVo.setServiceAgreement(CommonTool.convertParamToUrl(Config.get("shop.novicebid.serviceAgreement.url"),paramMap));
        	finacingDetailVo.setIsNoviceBid(1);
        }
    	finacingDetailVo.setLoanDays(finacingDetail.getLoanDays());

		//设置合同
		Map<String, Object> accessoryMap = new HashMap<String, Object>();
		accessoryMap.put("bidId", finacingDetail.getBidId());
		accessoryMap.put("accessoryType", Config.get("shop.accessory.type"));
		List<T6232> publicAccessoryList = publicAccessoryDao.getPublicAccessory(accessoryMap);
		String[] contractUrls = new String[publicAccessoryList.size()];
		for(int i=0;i<publicAccessoryList.size();i++){
			T6232 accessory = publicAccessoryList.get(i);
			String fileUrl=FileUtils.getPicURL(accessory.F04,Config.get("contact.url"));
			contractUrls[i] = fileUrl;
		}
		finacingDetailVo.setContractUrl(contractUrls);
		finacingDetailVo.setContractInfoUrl(Config.get("contactInfo.url")+URLEncoder.encode(JSONArray.toJSONString(contractUrls)));
		return finacingDetailVo;
	}



    @Override
	public FinacingDetailVO_131 getFinacingDetail_131(String userId, String zqId) throws Exception{
		
		Map<String,Object> detailMap=new HashMap<String,Object>();
		detailMap.put("userId", userId);
		detailMap.put("zqId", zqId);
		Finacing finacingDetail = this.finacingDao.getFinacingDetail(detailMap);
		
		if(finacingDetail==null)
			return null;
		FinacingDetailVO_131 finacingDetailVo = new FinacingDetailVO_131();
		finacingDetailVo.setZqId(Integer.valueOf(zqId));
		if("S".equals(finacingDetail.getIsTransfer())){//转让中
			finacingDetailVo.setZqStatus(3);
		}else if("F".equals(finacingDetail.getIsTransfer())){
			switch(finacingDetail.getBidStatus()){
				case "TBZ":
					finacingDetailVo.setZqStatus(0);
					break;
				case "DFK":
					finacingDetailVo.setZqStatus(0);
					break;
				case "HKZ":
					finacingDetailVo.setZqStatus(1);
					break;
				case "YJQ":
					finacingDetailVo.setZqStatus(2);
					break;
				case "YDF":
					finacingDetailVo.setZqStatus(2);
					break;
			}
		}
		finacingDetailVo.setBidId(finacingDetail.getBidId());
		finacingDetailVo.setZqTitle(finacingDetail.getName());
		finacingDetailVo.setZqSum(finacingDetail.getMoney());
		finacingDetailVo.setZqYield(String.valueOf(finacingDetail.getRate()));
		finacingDetailVo.setZqTime(finacingDetail.getPeriod());
		finacingDetailVo.setZqEarning(finacingDetail.getZqEarning());
		finacingDetailVo.setBuyTimestamp(finacingDetail.getBuyTimestamp().getTime()/1000);
		finacingDetailVo.setInterestTimestamp(finacingDetail.getInterestTimestamp().getTime()/1000);
		finacingDetailVo.setEndTimestamp(finacingDetail.getEndTimestamp().getTime()/1000);
		finacingDetailVo.setOriginalMoney(finacingDetail.getOriginalMoney());//原始债权金额
		
		//已过天数的收益（未到账）
		int passedDays = DateUtil.getDayBetweenDates(finacingDetail.getCreateTime(),DateUtil.nowDate());//已算收益天数
		int totalDays = DateUtil.getDayBetweenDates(finacingDetail.getCreateTime(),finacingDetail.getEndTimestamp());//当前债权一共计息的天数
		if(totalDays>0){
			BigDecimal passedEarning = new BigDecimal(finacingDetail.getZqEarning()).divide(BigDecimal.valueOf(totalDays),2,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(passedDays));
			finacingDetailVo.setPassedEarning(passedEarning.doubleValue());
			finacingDetailVo.setSurplusDays(totalDays-passedDays);
		}
		
		//用户信息
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("userId", userId);
		UserInfo userInfo = this.userInfoDao.getUserInfoByPhoneNumOrUsername(userMap);
				
		//服务协议
		Map<String,String> paramMap = new HashMap<String,String>();
		
		paramMap.put("userName","");
		if(userInfo.getFullName() !=null && !"".equals(userInfo.getFullName())){
			paramMap.put("userName", userInfo.getFullName().substring(0,1)+"**");
		}
		paramMap.put("phone", StringHelper.replace(3, 7, userInfo.getUsername(), "****"));
		paramMap.put("IDNumber", userInfo.getIdCard());
		
		String serviceArgeementUrl = bidInfoService.getGroupItemValue(finacingDetail.getBidId(), InterfaceConst.BID_EXTEND_ASSIGNMENT_CODE);
    	if(serviceArgeementUrl!=null && !"".equals(serviceArgeementUrl)){
    		finacingDetailVo.setServiceArgeementUrl(CommonTool.convertParamToUrl(serviceArgeementUrl,paramMap));
    	}
    	
		//判断是否是新手标  (laubrence 2015-12-15 19:49:09)
		finacingDetailVo.setIsNoviceBid(0);
        if("S".equals(finacingDetail.getIsNoviceBid())){
        	finacingDetailVo.setIsNoviceBid(1);
        }
    	finacingDetailVo.setLoanDays(finacingDetail.getLoanDays());
		
		//设置合同
		Map<String, Object> accessoryMap = new HashMap<String, Object>();
		accessoryMap.put("bidId", finacingDetail.getBidId());
		accessoryMap.put("accessoryType", Config.get("shop.accessory.type"));
		List<T6232> publicAccessoryList = publicAccessoryDao.getPublicAccessory(accessoryMap);
		String[] contractUrls = new String[publicAccessoryList.size()];
		for(int i=0;i<publicAccessoryList.size();i++){
			T6232 accessory = publicAccessoryList.get(i);
			String fileUrl=FileUtils.getPicURL(accessory.F04,Config.get("contact.url"));
			contractUrls[i] = fileUrl;
		}
		finacingDetailVo.setLawFiles(contractUrls);
		finacingDetailVo.setLawFileUrl(Config.get("contactInfo.url")+URLEncoder.encode(JSONArray.toJSONString(contractUrls)));
		finacingDetailVo.setRemark(finacingDetail.getRemark());
		finacingDetailVo.setGroupInfoList(bidInfoService.getGroupInfoList(finacingDetail.getBidId()));
		finacingDetailVo.setBorrowerUrl(Config.get("bid.borrower.url")+finacingDetail.getBidId());
		return finacingDetailVo;
	}


	
	@Override
    public ShopFinacingVo getShopFinacing(int zqId) {
		ShopFinacingVo vo = new ShopFinacingVo();
        
        ShopTreasureInfo info = this.shopTreasureDao.getShopTreasureByZpid(zqId);
        if (null == info) {
            return null;
        }
        vo.setKdbPlantId(info.getId());
        vo.setKdbPlanTitle(info.getName());
        vo.setInvestInfo(info.getDescription());
        vo.setProUrl(Config.get("shop.pro.url"));
        return vo;
    }

	@Override
	public ShopProductVo getShopProduct(int kdbPlantId) {
		ShopProductVo vo = new ShopProductVo();
		
		//获取开店宝计划详情
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", kdbPlantId);
        //map.put("userType", InterfaceConst.USER_TYPE_FZRR);
        map.put("proType", InterfaceConst.PRO_TYPE_KDB);//产品类型
        ShopTreasureInfo info = this.shopTreasureDao.getShopTreasureInfo(map);
        if (null == info) {
            return null;
        }
		vo.setKdbPlantId(kdbPlantId);
		vo.setKdbPlanTitle(info.getName());
		vo.setKdbSum(info.getLoanAmount().doubleValue());
		vo.setKdbYield(String.valueOf(info.getRate()));
		vo.setProUrl(Config.get("shop.pro.url"));
		
		//服务协议
		if(info.getMonth()==12){
        	vo.setKdbType(InterfaceConst.KDB_MONTH12_TYPE);
        	vo.setServiceAgreement(Config.get("shop.serviceAgreementC.url"));
        }else if(info.getMonth()==6){
        	vo.setKdbType(InterfaceConst.KDB_MONTH6_TYPE);
        	vo.setServiceAgreement(Config.get("shop.serviceAgreementB.url"));
        }else if(info.getMonth()==3){
        	vo.setKdbType(InterfaceConst.KDB_MONTH3_TYPE);
        	vo.setServiceAgreement(Config.get("shop.serviceAgreementA.url"));
        }
		
		//判断是否是新手标  (laubrence 2015-12-15 17:40:12)
        vo.setIsNoviceBid(0);
        if("S".equals(info.getIsNoviceBid())){
            vo.setIsNoviceBid(1);
            vo.setServiceAgreement(Config.get("shop.novicebid.serviceAgreement.url"));
        }
        vo.setLoanDays(info.getLoanDays());
		
		//获取合同
		Map<String, Object> accessoryMap = new HashMap<String, Object>();
		accessoryMap.put("bidId", kdbPlantId);
		accessoryMap.put("accessoryType", Config.get("shop.accessory.type"));
		List<T6232> list = publicAccessoryDao.getPublicAccessory(accessoryMap);
		String[] contractUrls = new String[list.size()];
		for(int i=0;i<list.size();i++){
			T6232 accessory = list.get(i);
			String fileUrl=FileUtils.getPicURL(accessory.F04,Config.get("contact.url"));
			contractUrls[i] = fileUrl;
		}
		vo.setContractUrl(contractUrls);
		return vo;
	}

	@Override
	public FinacingResultVo getFinacingList(String userId, int isUp, String timestamp) {
		FinacingResultVo result = new FinacingResultVo();

        //投资列表
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bidStatus", new String[]{Status.TBZ.name(), Status.DFK.name(), Status.HKZ.name(), Status.YJQ.name()});
        map.put("userId", userId);
        if (StringUtils.isNotEmpty(timestamp)) {
            map.put("time", DateUtil.timestampToDate(Long.valueOf(timestamp)));
        }
        List<Finacing> settleFinacingList = this.finacingDao.getInvestRecord(map);

        List<FinacingVo> voList = new ArrayList<FinacingVo>();
        for (Finacing finacing : settleFinacingList) {
            FinacingVo vo = new FinacingVo();
            vo.setBuyTimestamp(finacing.getCreateTime().getTime());
            vo.setZqId(finacing.getId());
            vo.setZqTitle(finacing.getName());
            vo.setZqYield(String.valueOf(finacing.getRate()));
            vo.setZqTime(finacing.getMonth());
            vo.setZqSum(finacing.getMoney());

            //债权的状态
            if (Status.S.name().equals(finacing.getIsTransfer())) {
                vo.setZqStatus(3);//转让中
            } else {
                if (finacing.getBidStatus().equals(Status.TBZ.name())) {
                    vo.setZqStatus(0);//已购买
                } else if (finacing.getBidStatus().equals(Status.HKZ.name())) {
                    vo.setZqStatus(1);//收益中
                } else if (finacing.getBidStatus().equals(Status.YJQ.name())) {
                    vo.setZqStatus(2);//已回款
                } else if (finacing.getBidStatus().equals(Status.DFK.name())) {
                    vo.setZqStatus(0);//已购买
                }
            }
            //判断是否是新手标  (laubrence 2015-12-15 19:39:14)
        	vo.setIsNoviceBid(0);
        	if("S".equals(finacing.getIsNoviceBid())){
        		vo.setIsNoviceBid(1);
        	}
        	vo.setLoanDays(finacing.getLoanDays());

            if(vo.getZqStatus()==2){//债权已获收益
            	Map<String, Object> earMap = new HashMap<String, Object>();
                earMap.put("userId", userId);
                earMap.put("tradeType", InterfaceConst.TZ_LX);
                earMap.put("zqId", finacing.getId());
                earMap.put("status", Status.YH.name());
                double earnings = this.bidRefundDao.getPredictEarnings(earMap);
                vo.setZqEarning(earnings);
            }else{//债权预计收益
            	BigDecimal earnings = null;
            	if("S".equals(finacing.getIsNoviceBid())){
            		earnings = CalculateEarningsUtil.calEarningsByDays(finacing.getPaymentType(), BigDecimal.valueOf(finacing.getMoney()), finacing.getRate(), finacing.getLoanDays());
                }else{
            		earnings = CalculateEarningsUtil.calEarnings(finacing.getPaymentType(), BigDecimal.valueOf(finacing.getMoney()), finacing.getRate(), finacing.getMonth());
                }
                vo.setZqEarning(earnings.doubleValue());
            }
            voList.add(vo);
        }

        result.setZqList(voList);
        return result;
	}

	@Override
	public ShopInformationVO getShopInformation(int bidId) {
		ShopInformation info = this.shopTreasureDao.getShopInfomationByBid(bidId);
		
		ShopInformationVO vo = new ShopInformationVO();
		if(null != info){
			if(StringUtils.isNotEmpty(info.getShopPicture())){
				String[] shopPictures = info.getShopPicture().split(",");
				String[] shopPicturesVo = new String[shopPictures.length];
				for(int i=0;i<shopPictures.length;i++){
					String shopPic = shopPictures[i];
					String fileUrl=FileUtils.getPicURL(shopPic,Config.get("contact.url"));
					shopPicturesVo[i] = fileUrl;
				}
				vo.setShopPictures(shopPicturesVo);
				
			}
			vo.setShopAddress(info.getShopAddress());
			vo.setShopName(info.getShopName());
			return vo;
		}
		return null;
	}
	
	@Override
	public List<Finacing> getFinacing(String userId, String bidId, String creditId) {
		Map<String,Object> map = new HashMap<String,Object>();
        if(StringUtils.isNotEmpty(creditId)){
        	map.put("creditId", creditId);
		}
		if(StringUtils.isNotEmpty(userId)){
			map.put("userId", userId);
		}
        if(StringUtils.isNotEmpty(bidId)){
        	map.put("bidId", bidId);
		}
        List<Finacing> list = this.finacingDao.getFinacingByBid(map);
		return list;
	}

	@Override
	public BigDecimal getZqzrEarning(String userId) {
		return tradeService.getYesterdayEarnings(Integer.valueOf(userId), EarningsTypeConst.ZQZR);
	}

	@Override
	public BigDecimal getZqzrAssets(String userId) {
		return this.finacingDao.getZqzrAssets(userId);
	}

	@Override
	public int isUserInvest(int userId) {
		int investId = shopTreasureDao.isUserInvest(Integer.valueOf(userId));
		return investId;
	}

	@Override
	public FinacingResultVo getInvestRecordList(String userId, int isUp,
			String timestamp, String proType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/** 
	 * @Title: getUserInfo 
	 * @Description: 获取用户信息
	 * @param userId
	 * @return
	 * @return: UserInfo
	 */
	@Override
	public UserInfo getUserInfo(int userId){
		//用户信息
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("userId", userId);
		return this.userInfoDao.getUserInfoByPhoneNumOrUsername(userMap);
	}

	/**
	 * 用户投资列表
	 * @param userId
	 * @param bidType
	 * @param bidStatusStr
	 * @param pageBounds
	 * @return
     * @throws Exception
     */
	@Override
	public List<InvestInfo> getUserInvestList(int userId, String bidType, String[] bidStatusStr, PageBounds pageBounds,int cgNum) throws Exception {
		return finacingDao.getUserInvestList(userId, bidType, bidStatusStr, pageBounds,cgNum);
	}

    /**
     * 用户投资债权详情（1.4.0 接口以上适用）
     * @param userId
     * @param creditId
     * @return
     * @throws Exception
     */
    @Override
    public InvestInfoDetail getUserFinacingDetail(String userId, String creditId) throws Exception {
        return finacingDao.getUserInvestDetail(userId, creditId);
    }

    /**
     * 获取用户待收本息金额
     * @param userId
     * @param creditId
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getUserCollectInterest(int userId, int creditId) throws Exception {
        String[] repaymentStatus = new String[]{Status.WH.name()};
		BigDecimal b = new BigDecimal(finacingDao.getUserCollectInterest(userId, creditId, repaymentStatus));
		return b.divide(new BigDecimal(1),2,BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 获取用户债权信息
     * @param creditId
     * @return
     * @throws Exception
     */
    @Override
    public CreditInfo getUserCreditInfo(int creditId) throws Exception {
        return finacingDao.getUserCreditInfo(creditId);
    }


	/**
	 * 获取用户投资债权的还款计划
	 * @param userId
	 * @param creditId
	 * @return
	 * @throws Exception
     */
	@Override
	public List<RepaymentInfo> getUserRepaymentItem(int userId, int creditId) throws Exception {
		int[] tradeTypes = new int[]{ FeeCode.TZ_BJ, FeeCode.TZ_LX, FeeCode.TZ_FX, FeeCode.TZ_WYJ, FeeCode.TZ_JXJL, FeeCode.TZ_BJX};
		return finacingDao.getUserRepaymentItem(userId,creditId,tradeTypes);
	}

	@Override
	public List<RepaymentInfo> getUserRepaymentItem(int userId, int creditId, int[] tradeTypes){
		return finacingDao.getUserRepaymentItem(userId,creditId,tradeTypes);
	}

	@Override
	public List<RepaymentInfo> getNextRepaymentItem(int userId, int creditId, int[] tradeTypes) {
		return finacingDao.getNextRepaymentItem(userId,creditId,tradeTypes);
	}

	@Override
	public Map getNextRepaymentItemProfit(int userId, int creditId, int[] tradeTypes) {
		return finacingDao.getNextRepaymentItemProfit(userId,creditId,tradeTypes);
	}

	@Override
	public RepaymentInfo getLastRepaymentItem(int userId, int creditId, int[] tradeTypes) {
		return finacingDao.getLastRepaymentItem(userId,creditId,tradeTypes);
	}
	@Override
	public Map<String, Map<String, String>> getInvestmentQty(Integer userId, VersionTypeEnum versionTypeEnum) {
		return finacingDao.getInvestmentQty(userId, versionTypeEnum);
	}

	@Override
	public List<InvestInfo> getNearInvestList(int userId, VersionTypeEnum vte, PageBounds pageBounds) throws Exception {
		//新计划
		List<InvestInfo> newPlans = planDao.getNewPlanList(userId, 5, vte, pageBounds);
		//老计划
		if(VersionTypeEnum.PT.equals(vte)){
			List<InvestInfo> oldPlans = planDao.getOldPlanList(userId, 5, pageBounds);
			newPlans.addAll(oldPlans);
		}
		//标
		List<InvestInfo> bidList = shopTreasureDao.getNearBid(userId, 5, vte, pageBounds);
		newPlans.addAll(bidList);
		//债权
		List<InvestInfo> creditList = finacingDao.getNearCredit(userId, 5, vte, pageBounds);
		newPlans.addAll(creditList);
		Collections.sort(newPlans);

		return newPlans;
	}


	@Override
	public PlanFinacing getUserPlanDetail(int userId, Integer planRecordId) {
		return finacingDao.getUserPlanDetail(userId,planRecordId);
	}

	@Override
	public List<Double> getPlanCollectInterest(Integer planRecordId) throws Exception {
		return finacingDao.getPlanCollectInterest(planRecordId);
	}

	@Override
	public PlanFinacing getUserNewPlanDetail(int userId, Integer planRecordId) {
		return finacingDao.getUserNewPlanDetail(userId,planRecordId);
	}

    @Override
    public Integer getPlanMonthNum(int ownsStatus, long interestTime, long exitTime) {
        int month = 0;
        for (int i = 1; i <= 12; i++) {

            long naturaDate = 0;
            try {
                naturaDate = DateUtil.rollNaturalMonth(interestTime, i);//起息时间经自然月推算到当前月经过了i个月得到准确日期
            } catch (Exception e) {

            }
            if (ownsStatus == 1 || ownsStatus == 2) {//持有中或者锁定期
                if (naturaDate >= new Date().getTime()) {//
                    month = i;
                    break;
                }
            } else {//已退出
                if (naturaDate >= exitTime) {//起息时间经自然月推算到退出日期经过了几个月
                    month = i;
                    break;
                }
            }

        }
        return month;
    }
}
