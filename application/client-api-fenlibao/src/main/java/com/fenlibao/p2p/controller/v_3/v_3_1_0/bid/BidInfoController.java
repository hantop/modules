package com.fenlibao.p2p.controller.v_3.v_3_1_0.bid;

import com.alibaba.fastjson.JSONArray;
import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.p2p.model.entity.PlanBidsStatus;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.bid.BidBaseInfo;
import com.fenlibao.p2p.model.entity.bid.BidBorrowerInfo;
import com.fenlibao.p2p.model.entity.bid.BidFiles;
import com.fenlibao.p2p.model.entity.bid.BorrowerInfo;
import com.fenlibao.p2p.model.enums.bid.BidOriginEnum;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.InvestRecordsVO;
import com.fenlibao.p2p.model.vo.NoviceBidVO;
import com.fenlibao.p2p.model.vo.PlanDetailVO;
import com.fenlibao.p2p.model.vo.bidinfo.*;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by laubrence on 2016/2/20.
 */
@RestController("v_3_1_0/bidInfoController")
@RequestMapping(value = "bidInfo", headers = APIVersion.v_3_1_0)
public class BidInfoController {

	public static DecimalFormat df = new DecimalFormat("#,###");

	@Resource
	private BidInfoService bidInfoService;

	@Resource
	private UserInfoService userInfoService;

	@Resource
	private ITradeService tradeService;


	/**
	 * 首页
	 ***************************************************************
	 推荐产品规则
	 1、用户未登录&用户未投资，显示新手标/新手计划（优先显示新手标，再到新手计划）
	 2、用户已投资，显示月升计划/推荐标（优先显示月升计划，再到推荐标）
	 推荐标规则：利率（最高）>期限（最长）>发布时间（最近）
	 3、推荐产品规则：默认利率高的（例如12个月11%）
	 • 期限相同，取利率高的
	 • 利率相同，取期限长的
	 • 期限、利率相同，取发布时间较早的
	 • 若没有12个月，选9个月，没有9个月，选6个月，没有6个月，选3个月的，以此类推
	 4、无可投的新手标、新手计划和推荐标时，显示可投金额最大的消费信贷标；若无可投的消费信贷标，则显示最近满额/收益中的新手标/新手计划

	 热门产品规则
	 1、3个月以下为短期标，3个月~6个月为中期标，6个月以上为长期标
	 2、热门产品规则：短期标2个（消费信贷1个+常规标1个）+中期标1个+长期标1个
	 3、消费信贷、常规标取期限短的
	 •期限相同，取利率高的
	 •期限、利率相同，取进度快的
	 •期限、利率、进度相同，取发布时间早的
	 4、中期标、长期标取利率高的
	 •利率相同，取期限短的
	 •利率、期限相同，取进度快的
	 •利率、期限、进度相同，取发布时间早的
	 显示顺序：定时标1个（等待抢购的）>定时标1个（最近正在抢购的）>短期标>中期标>长期标

	 如有定时标，显示定时标

	 *************************************************************/
	@RequestMapping(value = "recommend", method = RequestMethod.GET)
	public HttpResponse getRecommendBid(@ModelAttribute BaseRequestForm  paramForm,
										@RequestParam(required = false, value = "userId") String userId) throws Exception{
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}

		List<ShopTreasureInfo> hotList = new LinkedList<>();
		List<ShopTreasureInfo> recommendList = new ArrayList<>();

		String[] status = new String[]{Status.DFK.name(), Status.HKZ.name()};
		//置顶标
		List<ShopTreasureInfo> stickList = bidInfoService.getStickBidList(new String[]{Status.TBZ.name()},1,null);
		if(null!=stickList&&stickList.size()>0){
			recommendList.addAll(stickList);
		}else {
			boolean isNovice;
			try {
				if (StringUtils.isNotBlank(userId) && Integer.valueOf(userId) > 0) {
					isNovice = bidInfoService.isNovice(Integer.valueOf(userId));
				} else {
					isNovice = true;
				}
			} catch (Throwable e) {
				isNovice = true;
			}


			//新手标
			List<ShopTreasureInfo> noviceBidList = null;

			if (isNovice) {//添加新手标
				noviceBidList = this.bidInfoService.getBidList(new String[]{Status.TBZ.toString()}, null, 1, Status.S.name(), null,null,1);
				if (noviceBidList == null || noviceBidList.size() == 0) {//添加新手专享计划
					List<ShopTreasureInfo> novicePlanist = this.bidInfoService.getBidPlansList(new String[]{Status.TBZ.toString()}, null, -1, -1, null, null, 1, -1, -1, Status.S.name());
					if (null != novicePlanist && novicePlanist.size() > 0) {
						for (int i = 0; i < novicePlanist.size(); i++) {
							if (planStaus(novicePlanist.get(i).getId()).equals("TBZ")) {
								novicePlanist.get(i).setStatus(planStaus(novicePlanist.get(i).getId()));
								noviceBidList.add(novicePlanist.get(i));
								break;
							}
						}
						//String bidStatus ="DFK";//
						//获取标状态
					/*List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(noviceBidList.get(0).getId());
                    for(PlanBidsStatus planBidsStatus : planBidsList){
                         if(planBidsStatus.getStatus().equals("TBZ")&&Double.valueOf(planBidsStatus.getSurplusAmount())>=100){
							 bidStatus ="TBZ";
					}
				}*/
						//noviceBidList.get(0).setStatus(planStaus(noviceBidList.get(0).getId()));
					}

				}
			}
			if (!isNovice || noviceBidList.size() == 0 || noviceBidList == null) {
				String[] recommendStatus = new String[]{Status.TBZ.name()};
				noviceBidList = this.bidInfoService.getBidRecommedList(recommendStatus, 1, Status.F.name());//消费信贷也包含其中
				if (null == noviceBidList || 0 == noviceBidList.size()) {
					noviceBidList = this.bidInfoService.getNoviceBidList(new String[]{Status.DFK.name(), Status.HKZ.name()}, 1);
				}
			}

			if (null != noviceBidList && noviceBidList.size() > 0) {
				ShopTreasureInfo noviceBid = noviceBidList.get(0);
				recommendList.add(noviceBid);
			}
		}
		//抢购标(计时中)
		List<ShopTreasureInfo> preSaleBids = this.bidInfoService.getPreSaleBids(1);
		if(null != preSaleBids && preSaleBids.size()>0){
			for (ShopTreasureInfo rateBid : preSaleBids) {
				if (!hotList.contains(rateBid) && !recommendList.contains(rateBid)) {
					hotList.add(rateBid);
					break;
				}
			}
		}

		//抢购标(投标中)
		List<ShopTreasureInfo> timingBids = this.bidInfoService.getTimingBids(1);
		if(null != timingBids && timingBids.size()>0){
			for (ShopTreasureInfo rateBid : timingBids) {
				if (!hotList.contains(rateBid) && !recommendList.contains(rateBid)) {
					hotList.add(rateBid);
					break;
				}
			}
		}

		//短期标2个（省心计划1个+常规标1个）
		//消费信贷标
		/*List<ShopTreasureInfo> xfxdBidList = this.bidInfoService.getXfxdShortBidList(null);
		if (null != xfxdBidList && xfxdBidList.size() > 0) {
			for (ShopTreasureInfo rateBid : xfxdBidList) {
				if (!hotList.contains(rateBid) && !recommendList.contains(rateBid)) {
					hotList.add(rateBid);
					break;
				}
			}
		}*/
		List<ShopTreasureInfo> shengxinPlansList = new ArrayList<>();
		//30天省心计划
		List<ShopTreasureInfo> thirtyPlansList = this.bidInfoService.getBidPlansList(new String[]{Status.TBZ.toString()}, null, 30, 30, null, null, 1, 1, 1, Status.F.name());
		//20省心计划
		List<ShopTreasureInfo> twentyPlansList = this.bidInfoService.getBidPlansList(new String[]{Status.TBZ.toString()}, null, 20, 20, null, null, 1, 1, 1, Status.F.name());
		//10省心计划
		List<ShopTreasureInfo> tenPlansList = this.bidInfoService.getBidPlansList(new String[]{Status.TBZ.toString()}, null, 10,10, null, null, 1, 1, 1, Status.F.name());
		if(thirtyPlansList!=null&&thirtyPlansList.size()>0){
			if(planStaus(thirtyPlansList.get(0).getId()).equals("TBZ")) {
				shengxinPlansList.add(thirtyPlansList.get(0));
			}
		}else if (twentyPlansList!=null&&twentyPlansList.size()>0){
			if(planStaus(twentyPlansList.get(0).getId()).equals("TBZ")) {
				shengxinPlansList.add(twentyPlansList.get(0));
			}
		}else if (tenPlansList!=null&&tenPlansList.size()>0){
			if(planStaus(tenPlansList.get(0).getId()).equals("TBZ")) {
				shengxinPlansList.add(tenPlansList.get(0));
			}
		}
		//省心计划
		if (null != shengxinPlansList && shengxinPlansList.size() > 0) {
			for (ShopTreasureInfo rateBid : shengxinPlansList) {
				rateBid.setStatus(planStaus(rateBid.getId()));
				if (!hotList.contains(rateBid) && !recommendList.contains(rateBid)) {
					hotList.add(rateBid);
					break;
				}
			}
		}

		//常规标
		List<ShopTreasureInfo> normalList = this.bidInfoService.getBidInfoByTimeSort(new String[]{Status.TBZ.toString()}, 0);
		if (null != normalList && normalList.size() > 0) {
			for (ShopTreasureInfo rateBid : normalList) {
				if (!hotList.contains(rateBid) && !recommendList.contains(rateBid)) {
					hotList.add(rateBid);
					break;
				}
			}
		}

		//中期标
		List<ShopTreasureInfo> middleList = this.bidInfoService.getBidInfoByTimeSort(new String[]{Status.TBZ.toString()}, 1);
		if (null != middleList && middleList.size() > 0) {
			for (ShopTreasureInfo rateBid : middleList) {
				if (!hotList.contains(rateBid) && !recommendList.contains(rateBid)) {
					hotList.add(rateBid);
					break;
				}
			}
		}

		//长期标
		List<ShopTreasureInfo> longList = this.bidInfoService.getBidInfoByTimeSort(new String[]{Status.TBZ.toString()}, 2);
		if (null != longList && longList.size() > 0) {
			for (ShopTreasureInfo rateBid : longList) {
				if (!hotList.contains(rateBid) && !recommendList.contains(rateBid)) {
					hotList.add(rateBid);
					break;
				}
			}
		}

		//不可投资的标
		List<ShopTreasureInfo> closeBidList = this.bidInfoService.getBidList(status,SortEnum.getOrderType("5"),InterfaceConst.ORDER_TYPE_ASC, 5,Status.F.name());
		if(null != closeBidList && closeBidList.size()>0){
			hotList.addAll(closeBidList);
		}

		int showNum=6;//显示条数
		hotList = hotList.subList(0, hotList.size()>=showNum?showNum:hotList.size());

		response.getData().put("hotList", extendBidInfo(hotList));
		//判断新手标还是推荐标
		List<BidInfoVO> recommendOrNovice = extendBidInfo(recommendList);
		if (0!=recommendList.size()&&recommendOrNovice.size()!=0&&1 != recommendOrNovice.get(0).getIsNoviceBid()) {
			recommendOrNovice.get(0).setBidClassify(3);
		}
		response.getData().put("recommendList", recommendOrNovice);
		return response;
	}

	private List<BidInfoVO> extendBidInfo(List<ShopTreasureInfo> hotList) {
		List<BidInfoVO> voList = new ArrayList<BidInfoVO>();
		for(ShopTreasureInfo info:hotList){
			BidInfoVO vo = new BidInfoVO();
			vo.setBidId(info.getId());
			//过滤数字信息
			String name = info.getName();
			Pattern pattern = Pattern.compile("\\d+$");
			Matcher matcher = pattern.matcher(name);
			if(matcher.find()){//标题尾部是数字
				pattern = Pattern.compile("[0-9]+(?=[^0-9]*$)");
				matcher = pattern.matcher(name);
				name=matcher.replaceAll("").trim();
			}
			vo.setBidTitle(name.replaceAll("(?<=.{10}).+","…"));
			vo.setBidType(info.getBidType());
			vo.setBidYield(String.valueOf(info.getRate()));
			vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name())?1:0);
			vo.setLoanDays(info.getLoanDays());
			vo.setLoanMonth(info.getMonth());
			vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType())?info.getAssetsType().split(","):new String[0]);
			vo.setTimestamp(DateUtil.dateToTimestampToSec(info.getPublishDate()));
			vo.setLoanAmount(info.getLoanAmount().toString());
			vo.setInterest(info.getInterest()< 0.001?"0":Double.toString(info.getInterest()));
			if (Status.HKZ.name().equals(info.getStatus()) || Status.YJQ.name().equals(info.getStatus()) || Status.DFK.name().equals(info.getStatus())) {
				vo.setSurplusAmount(BigDecimal.ZERO.toString());
			} else {
				if(info.getItemType()==1){
					List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(Integer.valueOf(info.getId()), Integer.MAX_VALUE);
					String status = "";
					double voteAmount = 0.00;
					for (PlanBidsStatus planBidsStatus : planBidsList) {
						if (planBidsStatus.getStatus().equals("TBZ") && Double.valueOf(planBidsStatus.getSurplusAmount()) >= 100) {
							voteAmount = voteAmount + Double.valueOf(planBidsStatus.getSurplusAmount());
						}
					}
					vo.setSurplusAmount(String.valueOf(voteAmount));
				}else {
					vo.setSurplusAmount(info.getVoteAmount().toString());
				}

			}
			vo.setProgressRate(info.getLoanAmount(), info.getVoteAmount());
			vo.setBidClassify(Status.S.name().equals(info.getIsNoviceBid())?1:0);
			vo.setIsCG(info.getIsCG());
			vo.setInvestLimit("100");
			//判断是否定向标
			if(info.getUserTotalAssets()!=null){
				vo.setBidClassify(2);
			}
			//设置抢购标参数
			if (info.getPanicBuyingTime()!=null){
				long timeLeft=(info.getPublishDate().getTime()-System.currentTimeMillis())/1000+InterfaceConst.EEROR_SECOND;;
				Integer countdown=Integer.valueOf(Config.get("bid.countdown.mimute"))*60;
				vo.setTimeLeft(timeLeft>0?timeLeft:0);
				vo.setCountdown(countdown);
				vo.setPanicBuyingTime(DateUtil.dateToTimestampToSec(info.getPublishDate()));
			}
			vo.setRepaymentMode(info.getRepaymentMode());
			//标的状态
			if(Status.TBZ.name().equals(info.getStatus())){
				vo.setBidStatus(1);
			}
			if(Status.DFK.name().equals(info.getStatus())){
				if(info.getItemType()==1){
					vo.setBidStatus(2);
				}
				else {
					if (info.getFundraisDate().getTime() > DateUtil.nowDate().getTime()) {
						vo.setBidStatus(2);
					} else {
						vo.setBidStatus(3);
					}
				}
			}
			if(Status.HKZ.name().equals(info.getStatus())){
				vo.setBidStatus(4);
			}
			if(Status.YJQ.name().equals(info.getStatus())){
				vo.setBidStatus(5);
			}
			if(Status.YFB.name().equals(info.getStatus())){
				vo.setBidStatus(0);
			}
			//todo 标的说明
			vo.setAnytimeQuit(info.getAnytimeQuit());
			List<String> bidLabels = new ArrayList<>();
			if(info.getUserTotalAssets()!=null){
				bidLabels.add("大客户专享");
			}
			if (info.getAnytimeQuit() == 1) {
				bidLabels.add("随时退出");
			}
			if (info.getIsCG() == 2) {
				bidLabels.add("银行存管");
			}
			String[] selfLabel = StringUtils.isNotEmpty(info.getBidLabel())?info.getBidLabel().split(","):new String[0];
			for (int i = 0; i < selfLabel.length; i++) {
				bidLabels.add(selfLabel[i]);
			}
			if(Status.S.name().equals(info.getIsNoviceBid())){
				bidLabels.add("新手专享");
			}
			if (bidLabels != null && bidLabels.size() > 0) {
				bidLabels = bidLabels.subList(0, bidLabels.size()>=2?2:bidLabels.size());
				vo.setBidLabel(bidLabels);
			}
			List<String> markList = new ArrayList<>();
			if(info.getAccumulatedIncome()!=null){
				markList.add("累计收益达"+df.format(Double.valueOf(info.getAccumulatedIncome()))+"元客户专享");
			}
			if(info.getUserTotalAssets()!=null){
				markList.add("账户资产达"+df.format(info.getUserTotalAssets())+"元客户专享");
			}
			if(info.getUserInvestAmount()!=null){
				markList.add("在投金额达"+df.format(info.getUserInvestAmount())+"客户专享");
			}
			if(info.getTargetUser()==1){
				markList.add("vip客户专享");
			}
			vo.setMarkArray(markList);
			vo.setItemType(info.getItemType());
			voList.add(vo);
		}

		List<BidInfoVO> items = new LinkedList<>();//首页
		/*for (BidInfoVO vo:voList ){
			if(vo.getBidClassify()!=1){
				if (vo.getPanicBuyingTime()!=null && vo.getTimeLeft()>0){
					items.add(0, vo); //抢购标未到点时
				}else{
					items.add(vo);
				}
			}
		}
		for (BidInfoVO vo:voList ){
			if (vo.getBidClassify()==1){
				items.add(0, vo); //新手标在最前
			}
		}*/
		items.addAll(voList);
		return items;
	}

	/**
	 * 投资-可投资的标的
	 * @param paramForm
	 * @param sortType   排序类型(1:时间 2：利率 3：进度)
	 * @param sortBy     排序方式(1: 降序2：升序)，默认降序
	 * @param bidOrigin  标的来源：（分利宝：0001\缺钱么：0002）
	 ****************************************************************
	rule：1、显示顺序：新手标1个>定时标1个（等待抢购的）>定时标（最近正在抢购的）>其他类型标>不可抢购标，其他类型以发布时间升序排列
	2、点击筛选条件时，不影响定时标(等待抢购的)和新手标的排序
	3、多个定时标（最近正在抢购的）以发布时间降序排列
	 ****************************************************************/
	@RequestMapping(value = "caninvest/list", method = RequestMethod.GET)
	public HttpResponse getCaninvestBid(@ModelAttribute BaseRequestForm  paramForm,
										String sortType,String sortBy,String bidOrigin,@RequestParam(required = false, value = "userId") String userId) throws Exception{
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		List<BidInfoVO> voList = new LinkedList<>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", new String[]{Status.TBZ.name()});
		if(StringUtils.isNotEmpty(sortType)){
			sortType = SortEnum.getOrderType(sortType);
			map.put("sortType", sortType);
		}
		if(StringUtils.isNotEmpty(sortBy)){
			sortBy = sortBy.equals("1")?InterfaceConst.ORDER_TYPE_DESC:InterfaceConst.ORDER_TYPE_ASC;
			map.put("sortBy", sortBy);
		}
		map.put("bidOrigin", bidOrigin);
		boolean showPrecisionTitle = bidOrigin!=null&&"0002".equals(bidOrigin);
		if (showPrecisionTitle) {
			//消费信贷列表只显示50条
			map.put("limit", 50);
		}
		if(bidOrigin!=null&&"0001".equals(bidOrigin)) {
			List<ShopTreasureInfo> bidList = new ArrayList<>();
			//置顶标
			List<ShopTreasureInfo> stickList = bidInfoService.getStickBidList(new String[]{Status.TBZ.name()},-1,null);
			if(null!=stickList&&stickList.size()>0){
				bidList.addAll(stickList);
			}
			boolean isNovice;
			try {
				if (StringUtils.isNotBlank(userId) && Integer.valueOf(userId) > 0) {
					isNovice = bidInfoService.isNovice(Integer.valueOf(userId));
				} else {
					isNovice = true;
				}
			} catch (Throwable e) {
				isNovice = true;
			}



			//新手标
			List<ShopTreasureInfo> noviceBidList = null;

			if (isNovice) {//添加新手标
				noviceBidList = this.bidInfoService.getBidList(new String[]{Status.TBZ.toString()}, null, Integer.MAX_VALUE, Status.S.name(), null,null,1);
				if(noviceBidList!=null&&noviceBidList.size()>0) {
					bidList.addAll(noviceBidList);

				}

				//添加新手计划
				List<ShopTreasureInfo> plansList = this.bidInfoService.getBidPlansList(new String[]{Status.TBZ.toString()}, null, -1, -1, null, null, 1, -1, -1, Status.S.name());
				String bidStatus = "HKZ";//
				if(plansList!=null&&plansList.size()>0){
					for(ShopTreasureInfo newPlans : plansList){
						if(planStaus(newPlans.getId()).equals("TBZ")){
							bidList.add(newPlans);
						}
					}
				}

			}
			List<BidInfoVO> bidVOList= extendBidInfo(bidList);
			voList.addAll(bidVOList);
		/*   List<ShopTreasureInfo> shengxinPlansList = new ArrayList<>();
		   //添加一个10天的省心计划
		   List<ShopTreasureInfo> tenPlansList = this.bidInfoService.getBidPlansList(new String[]{Status.TBZ.toString()}, null, 10, 10, null, null, 1,  Integer.MAX_VALUE, 1, Status.F.name());
		   if (tenPlansList != null&&tenPlansList.size()>0) {
			   for(ShopTreasureInfo tenPlans : tenPlansList){
				   if(planStaus(tenPlans.getId()).equals("TBZ")){
					   shengxinPlansList.add(tenPlans);
					   break;
				   }
			   }
		   }

		   //20天省心计划
		   List<ShopTreasureInfo> twentyPlansList = this.bidInfoService.getBidPlansList(new String[]{Status.TBZ.toString()}, null, 20, 20, null, null, 1,  Integer.MAX_VALUE, 1, Status.F.name());
		   if (twentyPlansList != null && twentyPlansList.size()>0) {
			  for(ShopTreasureInfo twentyPlans : twentyPlansList){
				  if(planStaus(twentyPlans.getId()).equals("TBZ")){
					  shengxinPlansList.add(twentyPlans);
					  break;
				  }
			  }

		   }
		   //30天省心计划
		   List<ShopTreasureInfo> thirtyPlansList = this.bidInfoService.getBidPlansList(new String[]{Status.TBZ.toString()}, null, 30, 30, null, null, 1,  Integer.MAX_VALUE, 1, Status.F.name());
		   for(ShopTreasureInfo thirtyPlans : thirtyPlansList){
			   if(planStaus(thirtyPlans.getId()).equals("TBZ")){
				   shengxinPlansList.add(thirtyPlans);
				   break;
			   }
		   }
           if(null!=shengxinPlansList&&shengxinPlansList.size()>0) {
			   List<String> planId = new ArrayList<String>();
			   for (ShopTreasureInfo info : shengxinPlansList) {
				   BidInfoVO vo = new BidInfoVO();
				   planId.add(String.valueOf(info.getId()));
				   vo.setBidId(info.getId());
				   //过滤数字信息
				   String name = info.getName();
				   Pattern pattern = Pattern.compile("\\d+$");
				   Matcher matcher = pattern.matcher(name);
				   if (matcher.find()) {//标题尾部是数字
					   pattern = Pattern.compile("[0-9]+(?=[^0-9]*$)");
					   matcher = pattern.matcher(name);
					   name = matcher.replaceAll("").trim();
				   }


				   vo.setBidTitle(name.replaceAll("(?<=.{12}).+", "…"));
				   vo.setBidYield(String.valueOf(info.getRate()));
				   vo.setLoanDays(info.getLoanDays());
				   vo.setLoanMonth(info.getMonth());
				   vo.setTimestamp(DateUtil.dateToTimestampToSec(info.getPublishDate()));
				   vo.setBidType(info.getBidType());
				   vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType()) ? info.getAssetsType().split(",") : new String[0]);
				   vo.setBidStatus(1);
				   vo.setLoanAmount(info.getLoanAmount().toString());
				   vo.setSurplusAmount(info.getVoteAmount().toString());
				   vo.setProgressRate(info.getLoanAmount(), info.getVoteAmount());
				   vo.setIsNoviceBid(Status.S.name().equals(info.getIsNoviceBid()) ? 1 : 0);
				   //区分数组的item类型(0:标  1:计划)
				   vo.setItemType(info.getItemType() == 1 ? 1 : 0);
				   vo.setInterest(info.getInterest() == 0.0 ? "0" : Double.toString(info.getInterest()));
				   vo.setRepaymentMode(info.getRepaymentMode());
				   voList.add(vo);
			   }
			   map.put("planId",planId.toArray());
		   }*/

			List<ShopTreasureInfo> timeList = new ArrayList<>();
			//抢购标(计时中)
			List<ShopTreasureInfo> preSaleBids = this.bidInfoService.getPreSaleBids(1);
			if (null != preSaleBids && preSaleBids.size() > 0) {
				timeList.addAll(preSaleBids);
			}

			//抢购标(投标中)
			List<ShopTreasureInfo> timingBids = this.bidInfoService.getTimingBids(Integer.MAX_VALUE);
			if (null != timingBids && timingBids.size() > 0) {
				timeList.addAll(timingBids);

			}
			List<BidInfoVO> timeVOList= extendBidInfo(timeList);
			voList.addAll(timeVOList);
		}
		List<ShopTreasureInfo> canInvestBidList = null;
		if(bidOrigin!=null&&"0001".equals(bidOrigin)) {

			canInvestBidList = this.bidInfoService.getInvestmentBidsAndPlan(map);
		}else {
			canInvestBidList = this.bidInfoService.getInvestmentBids(map);
		}

		if(bidOrigin!=null&&"0001".equals(bidOrigin)){
			for(ShopTreasureInfo info : canInvestBidList){
				if (info.getIsNoviceBid() != null && !info.getIsNoviceBid().equals("S") && null == info.getPanicBuyingTime()) {
					BidInfoVO vo = new BidInfoVO();
					if (info.getItemType() == 1) {
						if(planStaus(info.getId()).equals("TBZ")) {
							info.setStatus(planStaus(info.getId()));
							vo.setBidId(info.getId());
							//过滤数字信息
							String name = info.getName();
							Pattern pattern = Pattern.compile("\\d+$");
							Matcher matcher = pattern.matcher(name);
							if (matcher.find()) {//标题尾部是数字
								pattern = Pattern.compile("[0-9]+(?=[^0-9]*$)");
								matcher = pattern.matcher(name);
								name = matcher.replaceAll("").trim();
							}


							vo.setBidTitle(name.replaceAll("(?<=.{12}).+", "…"));
							vo.setBidYield(String.valueOf(info.getRate()));
							vo.setLoanDays(info.getLoanDays());
							vo.setLoanMonth(info.getMonth());
							vo.setTimestamp(DateUtil.dateToTimestampToSec(info.getPublishDate()));
							vo.setBidType(info.getBidType());
							vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType()) ? info.getAssetsType().split(",") : new String[0]);
							vo.setBidStatus(1);
							vo.setIsCG(info.getIsCG());
							if (Status.DFK.name().equals(info.getStatus())) {
								vo.setBidStatus(2);
							}
							if (Status.HKZ.name().equals(info.getStatus())) {
								vo.setBidStatus(4);
							}
							if (Status.YJQ.name().equals(info.getStatus())) {
								vo.setBidStatus(5);
							}
							vo.setLoanAmount(info.getLoanAmount().toString());
							List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(Integer.valueOf(info.getId()), Integer.MAX_VALUE);
							String status = "";
							double voteAmount = 0.00;
							for (PlanBidsStatus planBidsStatus : planBidsList) {
								if (planBidsStatus.getStatus().equals("TBZ") && Double.valueOf(planBidsStatus.getSurplusAmount()) >= 100) {
									voteAmount = voteAmount + Double.valueOf(planBidsStatus.getSurplusAmount());
								}
							}

							vo.setSurplusAmount(String.valueOf(voteAmount));
							vo.setProgressRate(info.getLoanAmount(), info.getVoteAmount());
							vo.setIsNoviceBid(Status.S.name().equals(info.getIsNoviceBid()) ? 1 : 0);
							//区分数组的item类型(0:标  1:计划)
							vo.setItemType(info.getItemType() == 1 ? 1 : 0);
							vo.setInterest(info.getInterest() < 0.001 ? "0" : Double.toString(info.getInterest()));
							vo.setRepaymentMode(info.getRepaymentMode());
							voList.add(vo);
							vo.setPlanType("2");
						}
					} else {
						vo.setBidId(info.getId());
						//过滤数字信息
						String name = info.getName();
						if (showPrecisionTitle) {//如果是消费信贷就显示全名
							vo.setBidTitle(name);
						} else {
							Pattern pattern = Pattern.compile("\\d+$");
							Matcher matcher = pattern.matcher(name);
							if (matcher.find()) {//标题尾部是数字
								pattern = Pattern.compile("[0-9]+(?=[^0-9]*$)");
								matcher = pattern.matcher(name);
								name = matcher.replaceAll("").trim();
							}
							vo.setBidTitle(name.replaceAll("(?<=.{12}).+", "…"));
						}

						vo.setBidYield(String.valueOf(info.getRate()));
						vo.setLoanDays(info.getLoanDays());
						vo.setLoanMonth(info.getMonth());
						vo.setTimestamp(DateUtil.dateToTimestampToSec(info.getPublishDate()));
						vo.setBidType(info.getBidType());
						vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType()) ? info.getAssetsType().split(",") : new String[0]);
						vo.setBidStatus(Status.YFB.name().equals(info.getStatus()) ? 0 : 1);
						vo.setLoanAmount(info.getLoanAmount().toString());
						vo.setSurplusAmount(info.getVoteAmount().toString());
						vo.setProgressRate(info.getLoanAmount(), info.getVoteAmount());
						vo.setIsNoviceBid(Status.S.name().equals(info.getIsNoviceBid()) ? 1 : 0);
						vo.setBidClassify(Status.S.name().equals(info.getIsNoviceBid()) ? 1 : 0);
						vo.setIsCG(info.getIsCG());
						//判断是否定向标
						if (info.getUserTotalAssets() != null) {
							vo.setBidClassify(2);
//				vo.setUserTotalAssets(StringHelper.outputdollars(info.getUserTotalAssets()));
						}
						//设置抢购标参数
						if (info.getPanicBuyingTime() != null) {
							long timeLeft = (info.getPublishDate().getTime() - System.currentTimeMillis()) / 1000 + InterfaceConst.EEROR_SECOND;
							Integer countdown = Integer.valueOf(Config.get("bid.countdown.mimute")) * 60;
							vo.setTimeLeft(timeLeft > 0 ? timeLeft : 0);
							vo.setCountdown(countdown);
							vo.setPanicBuyingTime(DateUtil.dateToTimestampToSec(info.getPublishDate()));
						}
						vo.setRepaymentMode(info.getRepaymentMode());
						//设置存管加息参数
						vo.setInterest(info.getInterest() < 0.001 ? "0" : Double.toString(info.getInterest()));
						vo.setAnytimeQuit(info.getAnytimeQuit());
						List<String> bidLabels = new ArrayList<>();
						if (info.getUserTotalAssets() != null) {
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
						if(info.getAccumulatedIncome()!=null){
							markList.add("累计收益达"+df.format(Double.valueOf(info.getAccumulatedIncome()))+"元客户专享");
						}
						if(info.getUserTotalAssets()!=null){
							markList.add("账户资产达"+df.format(info.getUserTotalAssets())+"元客户专享");
						}
						if(info.getUserInvestAmount()!=null){
							markList.add("在投金额达"+df.format(info.getUserInvestAmount())+"客户专享");
						}
						if(info.getTargetUser()==1){
							markList.add("vip客户专享");
						}
						vo.setMarkArray(markList);
						vo.setItemType(info.getItemType());
						voList.add(vo);

					}
				}

			}
		}else if(bidOrigin!=null&&"0002".equals(bidOrigin)){
			for(ShopTreasureInfo info : canInvestBidList) {
				BidInfoVO vo = new BidInfoVO();
				vo.setBidId(info.getId());
				//过滤数字信息
				String name = info.getName();
				if (showPrecisionTitle) {//如果是消费信贷就显示全名
					vo.setBidTitle(name);
				} else {
					Pattern pattern = Pattern.compile("\\d+$");
					Matcher matcher = pattern.matcher(name);
					if (matcher.find()) {//标题尾部是数字
						pattern = Pattern.compile("[0-9]+(?=[^0-9]*$)");
						matcher = pattern.matcher(name);
						name = matcher.replaceAll("").trim();
					}
					vo.setBidTitle(name.replaceAll("(?<=.{8}).+", "…"));
				}

				vo.setBidYield(String.valueOf(info.getRate()));
				vo.setLoanDays(info.getLoanDays());
				vo.setLoanMonth(info.getMonth());
				vo.setTimestamp(DateUtil.dateToTimestampToSec(info.getPublishDate()));
				vo.setBidType(info.getBidType());
				vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType()) ? info.getAssetsType().split(",") : new String[0]);
				vo.setBidStatus(Status.YFB.name().equals(info.getStatus()) ? 0 : 1);
				vo.setLoanAmount(info.getLoanAmount().toString());
				vo.setSurplusAmount(info.getVoteAmount().toString());
				vo.setProgressRate(info.getLoanAmount(), info.getVoteAmount());
				vo.setIsNoviceBid(Status.S.name().equals(info.getIsNoviceBid()) ? 1 : 0);
				vo.setBidClassify(Status.S.name().equals(info.getIsNoviceBid()) ? 1 : 0);
				vo.setIsCG(info.getIsCG());
				//判断是否定向标
				if (info.getDirectionalBid() != null) {
					vo.setBidClassify(2);
//				vo.setUserTotalAssets(StringHelper.outputdollars(info.getUserTotalAssets()));
				}
				//设置抢购标参数
				if (info.getPanicBuyingTime() != null) {
					long timeLeft = (info.getPublishDate().getTime() - System.currentTimeMillis()) / 1000 + InterfaceConst.EEROR_SECOND;
					Integer countdown = Integer.valueOf(Config.get("bid.countdown.mimute")) * 60;
					vo.setTimeLeft(timeLeft > 0 ? timeLeft : 0);
					vo.setCountdown(countdown);
					vo.setPanicBuyingTime(DateUtil.dateToTimestampToSec(info.getPublishDate()));
				}
				vo.setRepaymentMode(info.getRepaymentMode());
				//设置存管加息参数
				vo.setInterest(info.getInterest()< 0.001?"0":Double.toString(info.getInterest()));
				vo.setAnytimeQuit(info.getAnytimeQuit());
				vo.setItemType(0);
				voList.add(vo);

			}

		}
		List<BidInfoVO> items = new LinkedList<>();
		/*for (BidInfoVO vo:voList ){
			if(vo.getBidClassify()!=1){
				if (vo.getPanicBuyingTime()!=null && vo.getTimeLeft()>0){
					items.add(0, vo); //抢购标未到点时
				}else{
					items.add(vo);
				}
			}
		}
		for (BidInfoVO vo:voList ){
			if (vo.getBidClassify()==1){
				items.add(0, vo); //新手标在最前
			}
		}*/
		items.addAll(voList);
		response.getData().put("items", items);
		return response;
	}

	/**
	 * 已过期的标(满额、到期)
	 * @param paramForm
	 * @param timestamp  排序 (发标时间)
	 * @return
	 */
	@RequestMapping(value = "history/list", method = RequestMethod.GET)
	public HttpResponse getHistoryBid(@ModelAttribute BaseRequestForm  paramForm,
									  @RequestParam(required = false, value="timestamp") String timestamp,String bidOrigin) throws Exception{
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		List<BidInfoVO> voList = new ArrayList<BidInfoVO>();
		List<ShopTreasureInfo> list = new ArrayList<ShopTreasureInfo>();
		String[] status = new String[]{Status.DFK.name(), Status.HKZ.name(), Status.YJQ.name()};

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		map.put("limit", 10);
		if(StringUtils.isNotEmpty(timestamp)){
			Date time = DateUtil.timestampToDateBySec(Long.valueOf(timestamp));
			map.put("time", time);
		}else if(bidOrigin!=null&&!"0002".equals(bidOrigin)){
			List<ShopTreasureInfo> plansList = this.bidInfoService.getBidPlansList(status, null, -1, -1, null, null, 1,-1,20, null,"timeSort");
			list.addAll(plansList);
		}
		map.put("bidOrigin", bidOrigin);


		boolean showPrecisionTitle = bidOrigin!=null&&"0002".equals(bidOrigin);

		List<ShopTreasureInfo> historylist = this.bidInfoService.getBidList(map);
		list.addAll(historylist);
		for (ShopTreasureInfo info : list) {
			BidInfoVO vo = new BidInfoVO();
			if (info.getItemType() == 0) {
				vo.setBidId(info.getId());
				//过滤数字信息
				String name = info.getName();
				if (showPrecisionTitle) {//如果是消费信贷就显示全名
					vo.setBidTitle(name);
				} else {
					Pattern pattern = Pattern.compile("\\d+$");
					Matcher matcher = pattern.matcher(name);
					if (matcher.find()) {//标题尾部是数字
						pattern = Pattern.compile("[0-9]+(?=[^0-9]*$)");
						matcher = pattern.matcher(name);
						name = matcher.replaceAll("").trim();
					}
					vo.setBidTitle(name.replaceAll("(?<=.{12}).+", "…"));
				}
				vo.setBidYield(String.valueOf(info.getRate()));
				vo.setLoanDays(info.getLoanDays());
				vo.setLoanMonth(info.getMonth());
				vo.setTimestamp(DateUtil.dateToTimestampToSec(info.getPublishDate()));
				vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name()) ? 1 : 0);
				vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType()) ? info.getAssetsType().split(",") : new String[0]);
				vo.setBidType(info.getBidType());
				vo.setProgressRate_100(); //已过期的都是100%
				vo.setRepaymentMode(info.getRepaymentMode());
				vo.setIsCG(info.getIsCG());
				//标的状态
				if (Status.DFK.name().equals(info.getStatus())) {
					if (info.getFundraisDate().getTime() > DateUtil.nowDate().getTime()) {
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
				vo.setLoanAmount(info.getLoanAmount().toPlainString());
				//设置存管加息参数
				vo.setInterest(info.getInterest()< 0.001?"0":Double.toString(info.getInterest()));
				vo.setAnytimeQuit(info.getAnytimeQuit());
				List<String> bidLabels = new ArrayList<>();
				if (info.getUserTotalAssets() != null) {
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
					markList.add("累计收益达" + df.format(info.getAccumulatedIncome()) + "元客户专享");
				}
				if (info.getUserTotalAssets() != null) {
					markList.add("账户资产达" + df.format(info.getUserTotalAssets()) + "元客户专享");
				}
				if (info.getUserInvestAmount() != null) {
					markList.add("在投金额达" + df.format(info.getUserInvestAmount()) + "客户专享");
				}
				if (info.getTargetUser() == 1) {
					markList.add("vip客户专享");
				}
				vo.setMarkArray(markList);
				vo.setItemType(0);

				voList.add(vo);
			}
			else if(info.getItemType()==1){
				/*info.setStatus(planStaus(info.getId()));*/
				vo.setBidId(info.getId());
				//过滤数字信息
				String name = info.getName();
				if (showPrecisionTitle) {//如果是消费信贷就显示全名
					vo.setBidTitle(name);
				} else {
					Pattern pattern = Pattern.compile("\\d+$");
					Matcher matcher = pattern.matcher(name);
					if (matcher.find()) {//标题尾部是数字
						pattern = Pattern.compile("[0-9]+(?=[^0-9]*$)");
						matcher = pattern.matcher(name);
						name = matcher.replaceAll("").trim();
					}
					vo.setBidTitle(name.replaceAll("(?<=.{12}).+", "…"));
				}
				vo.setBidYield(String.valueOf(info.getRate()));
				vo.setLoanDays(info.getLoanDays());
				vo.setLoanMonth(info.getMonth());
				vo.setTimestamp(DateUtil.dateToTimestampToSec(info.getPublishDate()));
				vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name()) ? 1 : 0);
				vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType()) ? info.getAssetsType().split(",") : new String[0]);
				vo.setBidType(info.getBidType());
				vo.setProgressRate_100(); //已过期的都是100%
				vo.setRepaymentMode(info.getRepaymentMode());
				vo.setIsCG(info.getIsCG());
				//标的状态
				if (Status.DFK.name().equals(info.getStatus())) {
					vo.setBidStatus(2);
				}
				if (Status.HKZ.name().equals(info.getStatus())) {
					vo.setBidStatus(4);
				}
				if (Status.YJQ.name().equals(info.getStatus())) {
					vo.setBidStatus(5);
				}
				vo.setLoanAmount(info.getLoanAmount().toPlainString());
				//设置存管加息参数
				vo.setInterest(info.getInterest()< 0.001?"0":Double.toString(info.getInterest()));
				vo.setAnytimeQuit(info.getAnytimeQuit());
				List<String> bidLabels = new ArrayList<>();
				if (info.getUserTotalAssets() != null) {
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
				if(info.getAccumulatedIncome()!=null){
					markList.add("累计收益达"+df.format(Double.valueOf(info.getAccumulatedIncome()))+"元客户专享");
				}
				if(info.getUserTotalAssets()!=null){
					markList.add("账户资产达"+df.format(info.getUserTotalAssets())+"元客户专享");
				}
				if(info.getUserInvestAmount()!=null){
					markList.add("在投金额达"+df.format(info.getUserInvestAmount())+"客户专享");
				}
				if(info.getTargetUser()==1){
					markList.add("vip客户专享");
				}
				vo.setMarkArray(markList);
				vo.setItemType(1);
				vo.setPlanType("2");
				voList.add(vo);
			}
		}
		response.getData().put("items", voList);
		return response;
	}

	/**
	 * 新手标  筹款到期时间、发布时间升序，取第一个
	 * @param paramForm
	 * @return
	 */
	@RequestMapping(value = "noviceBid", method = RequestMethod.GET)
	public HttpResponse getNoviceBid(@ModelAttribute BaseRequestForm  paramForm) throws Exception{
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		List<ShopTreasureInfo> list = this.bidInfoService.getBidList(new String[]{Status.TBZ.toString()},null, 1, Status.S.name(),null,null,1);

		//当前新手标
		if(null==list||list.size()==0){
			return response;
		}
		ShopTreasureInfo info = list.get(0);

		NoviceBidVO vo = new NoviceBidVO();
		vo.setBidId(info.getId());
		vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name())?1:0);
		vo.setLoanDays(info.getLoanDays());
		//过滤数字信息
		String name = info.getName();
		Pattern pattern = Pattern.compile("\\d+$");
		Matcher matcher = pattern.matcher(name);
		if(matcher.find()){//标题尾部是数字
			pattern = Pattern.compile("[0-9]+(?=[^0-9]*$)");
			matcher = pattern.matcher(name);
			name=matcher.replaceAll("").trim();
		}
		vo.setNoviceBidTitle(name.replaceAll("(?<=.{12}).+","…"));
		vo.setTimestamp(String.valueOf(DateUtil.dateToTimestampToSec(info.getPublishDate())));
		vo.setYield(String.valueOf(info.getRate()));
		vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType())?info.getAssetsType().split(","):new String[0]);
		//设置存管加息参数
		vo.setInterest("0");
		vo.setIsCG(info.getIsCG());
		vo.setAnytimeQuit(info.getAnytimeQuit());
		vo.setBidLabel(StringUtils.isNotEmpty(info.getBidLabel())?info.getAssetsType().split(","):new String[0]);
		vo.setItemType(0);

		response.setData(CommonTool.toMap(vo));
		return response;
	}

	/**
	 * 标的详情
	 * @param paramForm
	 * @param userId
	 * @param bidId  标的ID
	 * @return
	 */
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public HttpResponse getBidDetail(@ModelAttribute BaseRequestForm  paramForm,
									 String userId,String bidId,String planId) throws Exception{
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate()||(StringUtils.isEmpty(bidId)&&StringUtils.isEmpty(planId))){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		ShopTreasureInfo info = null;
		String id = null;//服务协议等用到的id
		if(!StringUtils.isBlank(bidId)) {
			info = this.bidInfoService.getBidInfo(Integer.parseInt(bidId));
			id=bidId;
		}else if (!StringUtils.isBlank(planId)){//计划详情
			info = this.bidInfoService.getPlanInfo(Integer.parseInt(planId));
			id = planId;
			/* String bidStatus ="DFK";//

				//计划状态
			List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(Integer.parseInt(planId));
			for(PlanBidsStatus planBidsStatus : planBidsList) {
					if (planBidsStatus.getStatus().equals("TBZ")&&Double.valueOf(planBidsStatus.getSurplusAmount())>=100) {
						bidStatus = "TBZ";
					}
				}*/
			info.setStatus(planStaus(Integer.parseInt(planId)));
		}
		if(null==info){
			response.setCodeMessage(ResponseCode.BID_DETAILS_EMPTY);
			return response;
		}
		if (!StringUtils.isBlank(bidId)) {
			BidDetailVO vo = new BidDetailVO();
			vo.setNoviceBIdlimit(Config.get("bid.novice.invest.limit"));//新手标额度
			vo.setBidId(info.getId());
			vo.setBidTitle(info.getName());
			vo.setPrecisionTitle(info.getName());//全称

			vo.setBidYield(String.valueOf(info.getRate()));
			vo.setRepaymentMode(info.getRepaymentMode());
			vo.setLoanDays(info.getLoanDays());
			vo.setLoanMonth(info.getMonth());
			vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name())?1:0);
			vo.setBidClassify(Status.S.name().equals(info.getIsNoviceBid())?1:0);
			//判断是否定向标
			if(info.getUserTotalAssets()!=null){
				vo.setBidClassify(2);
//			DecimalFormat dft = new DecimalFormat("#,##0");
				vo.setUserTotalAssets(info.getUserTotalAssets().toPlainString());
			}
			vo.setBidType(info.getBidType());
			vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType()) ? info.getAssetsType().split(",") : new String[0]);
			vo.setRemark(info.getBorrowingDescribe());
			vo.setBorrowerUrl(Config.get("bid.borrower.url") + bidId);
			vo.setPurchaseTotal(info.getLoanAmount().doubleValue());
			vo.setPurchasedTotal(info.getLoanAmount().subtract(info.getVoteAmount()).doubleValue());
			vo.setProgressRate(info.getLoanAmount(), info.getVoteAmount());
        /*update by laubrence 2016-3-26 19:11:56*/
		/*vo.setBuyTimestamp(DateUtil.nowDate().getTime()/1000);
		//到期时间和计息时间
		if(null!=info.getFundraisDate()){
        	vo.setInterestTimestamp(DateUtil.dateAdd(info.getFundraisDate(),1).getTime());//计息时间
        	if(info.getLoanDays() > 0){
        		vo.setEndTimestamp(DateUtil.dateAdd(info.getFundraisDate(),info.getLoanDays()+1).getTime());//到期时间
        	}
        	if(info.getMonth() > 0){
        		vo.setEndTimestamp(DateUtil.dateAdd(DateUtil.monthAdd(info.getFundraisDate(),info.getMonth()),1).getTime());//到期时间
        	}
        }*/
			//标的状态
			if (Status.TBZ.name().equals(info.getStatus())) {
				vo.setBidStatus(1);
			}
			if (Status.DFK.name().equals(info.getStatus())) {
				if(info.getItemType()==1){
					vo.setBidStatus(2);
				}
				else {
					if (info.getFundraisDate().getTime() > DateUtil.nowDate().getTime()) {
						vo.setBidStatus(2);
					} else {
						vo.setBidStatus(3);
					}
				}
			}
			if (Status.HKZ.name().equals(info.getStatus())) {
				vo.setBidStatus(4);
			}
			if (Status.YJQ.name().equals(info.getStatus())) {
				vo.setBidStatus(5);
			}
			vo.setLawFiles(bidInfoService.getBidPublicAccessoryFiles(Integer.valueOf(id)));
			vo.setLawFileUrl(bidInfoService.getBidPublicAccessoryFileUrl(Integer.valueOf(id)));
			//服务协议
			//String fwxy = bidInfoService.getGroupItemValue(Integer.parseInt(bidId), InterfaceConst.BID_EXTEND_ASSIGNMENT_CODE);
			vo.setFwxyUrl(userInfoService.getUserServiceAgreementUrl(Integer.valueOf(StringUtils.isBlank(userId) ? "-1" : userId)));
			//组信息
			List<BidExtendGroupVO> groupInfoList = this.bidInfoService.getGroupInfoList(Integer.parseInt(id));

			vo.setGroupInfoList(groupInfoList);
			//是否已申购
			if (StringUtils.isNoneEmpty(userId)) {
				BidBaseInfo bidInfo = this.bidInfoService.getBidBaseInfoByUser(Integer.valueOf(userId), Integer.valueOf(id));
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
		if(info.getUserTotalAssets()!=null){
			bidLabels.add("大客户专享");
		}
		if (info.getAnytimeQuit() == 1) {
			bidLabels.add("随时退出");
		}
		if (info.getIsCG() == 2) {
			bidLabels.add("银行存管");
		}
		String[] selfLabel = StringUtils.isNotEmpty(info.getBidLabel())?info.getBidLabel().split(","):new String[0];
		for (int i = 0; i < selfLabel.length; i++) {
			bidLabels.add(selfLabel[i]);
		}
		if(Status.S.name().equals(info.getIsNoviceBid())){
			bidLabels.add("新手专享");
		}
		if (bidLabels != null && bidLabels.size() > 0) {
			bidLabels = bidLabels.subList(0, bidLabels.size()>=2?2:bidLabels.size());
			vo.setBidLabel(bidLabels);
		}
			List<String> markList = new ArrayList<>();
			if(info.getAccumulatedIncome()!=null){
				markList.add("累计收益达"+df.format(Double.valueOf(info.getAccumulatedIncome()))+"元客户专享");
			}
			if(info.getUserTotalAssets()!=null){
				markList.add("账户资产达"+df.format(info.getUserTotalAssets())+"元客户专享");
			}
			if(info.getUserInvestAmount()!=null){
				markList.add("在投金额达"+df.format(info.getUserInvestAmount())+"客户专享");
			}
			if(info.getTargetUser()==1){
				markList.add("vip客户专享");
			}
			vo.setMarkArray(markList);
		vo.setAccumulatedIncome(info.getAccumulatedIncome());
		vo.setTargetUser(info.getTargetUser());
		vo.setInvestingAmount(info.getInvestingAmount());

			if (info.getInterest() != 0) {
				vo.setInterest(info.getInterest()< 0.001?"0":Double.toString(info.getInterest()));
			}else{
				vo.setInterest("0.00");
			}
			vo.setItemType(0);


			response.setData(CommonTool.toMapDefaultNull(vo));
		}else if (!StringUtils.isBlank(planId)){
			PlanDetailVO vo = new PlanDetailVO();
			info.setStatus(planStaus(Integer.valueOf(planId)));
			vo.setPlanId(info.getId());
			vo.setPlanTitle(info.getName());
			vo.setBidYield(String.valueOf(info.getRate()));
			vo.setRepaymentMode(info.getRepaymentMode());
			vo.setLoanDays(info.getLoanDays());
			vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name()) ? 1 : 0);


			vo.setRemark(info.getBorrowingDescribe());
			vo.setPurchaseTotal(String.valueOf(info.getLoanAmount()));
			vo.setPurchasedTotal(String.valueOf(info.getLoanAmount().subtract(info.getVoteAmount())));
			List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(Integer.valueOf(planId),Integer.MAX_VALUE);
			double voteAmount = 0.00;
			for(PlanBidsStatus planBidsStatus : planBidsList) {
				if (planBidsStatus.getStatus().equals("TBZ") && Double.valueOf(planBidsStatus.getSurplusAmount()) >= 100) {
					voteAmount = voteAmount+Double.valueOf(planBidsStatus.getSurplusAmount());
				}
			}
			vo.setVoteAmount(String.valueOf(voteAmount));
			vo.setProgressRate(info.getLoanAmount(),new BigDecimal(voteAmount));
			vo.setRaisedRate(info.getInterest()< 0.001?0:info.getInterest());
			int isPurchased = 0;
			if(!StringUtils.isEmpty(userId)){
				if(tradeService.getpurchasedPlan(Integer.valueOf(userId),Integer.valueOf(planId))>0){
					isPurchased=1;
				}
			}

			vo.setPlanTitle(info.getName());
			vo.setPrecisionTitle(info.getName());//全称

			//标的状态
			if (Status.TBZ.name().equals(info.getStatus())) {
				vo.setPlanStatus(1);
			}
			if (Status.DFK.name().equals(info.getStatus())) {
				if(info.getItemType()==1){
					vo.setPlanStatus(2);
				}
				else {
					if (info.getFundraisDate().getTime() > DateUtil.nowDate().getTime()) {
						vo.setPlanStatus(2);
					} else {
						vo.setPlanStatus(3);
					}
				}
			}
			if (Status.HKZ.name().equals(info.getStatus())) {
				vo.setPlanStatus(4);
			}
			if (Status.YJQ.name().equals(info.getStatus())) {
				vo.setPlanStatus(5);
			}
			vo.setLawFiles(bidInfoService.getBidPublicAccessoryFiles(Integer.valueOf(id)));
			vo.setLawFileUrl(bidInfoService.getBidPublicAccessoryFileUrl(Integer.valueOf(id)));
			//服务协议
			//List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(Integer.valueOf(id));
			String fwxy = bidInfoService.getGroupItemValue(planBidsList.get(0).getBidId(), InterfaceConst.BID_EXTEND_ASSIGNMENT_CODE);

			vo.setFwxyUrl(userInfoService.getUserServicePlanAgreementUrl(Integer.valueOf(StringUtils.isBlank(userId) ? "-1" : userId),info.getIsNoviceBid(),"2"));
			//组信息
			/*List<BidExtendGroupVO> groupInfoList = this.bidInfoService.getGroupInfoList(Integer.parseInt(id));

			vo.setGroupInfoList(groupInfoList);*/
			List<BidExtendGroupVO> groupInfoList = new ArrayList<BidExtendGroupVO>();
			vo.setGroupInfoList(groupInfoList);

			//担保借款合同
			if (BidOriginEnum.qqm.getCode().equals(info.getBidOrigin())) {
				vo.setGuaranteeFileUrl(Config.get("bid.guaranteeFile.url"));
			}

			// TODO: 2016/12/19

			if(info.getItemType()!=1) {
				vo.setInterest("0.00");//加息利率
			}else {
				vo.setInterest(info.getInterest()< 0.001?"0":Double.toString(info.getInterest()));
			}
			vo.setInvestLimit("100");
			if(info.getIsNoviceBid().equals("S")){
				vo.setPlanLimit("10000");
			}
			List<String> bidLabels = new ArrayList<>();
			if (info.getUserTotalAssets() != null) {
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
			if(info.getAccumulatedIncome()!=null){
				markList.add("累计收益达"+df.format(Double.valueOf(info.getAccumulatedIncome()))+"元客户专享");
			}
			if(info.getUserTotalAssets()!=null){
				markList.add("账户资产达"+df.format(info.getUserTotalAssets())+"元客户专享");
			}
			if(info.getUserInvestAmount()!=null){
				markList.add("在投金额达"+df.format(info.getUserInvestAmount())+"客户专享");
			}
			if(info.getTargetUser()==1){
				markList.add("vip客户专享");
			}
			vo.setMarkArray(markList);
			//vo.setBidLabel(StringUtils.isNotEmpty(info.getBidLabel()) ? info.getAssetsType().split(",") : new String[0]);
			vo.setItemType(info.getItemType());
			vo.setPurchasedStatus(isPurchased);
			//前台要求 添加存管参数
			vo.setIsCG(1);
			response.setData(CommonTool.toMapDefaultNull(vo));
		}

		return response;
	}

	/**
	 * 标的投资记录
	 * @param paramForm
	 * @param bidId      标的ID
	 * @param timestamp  投资时间排序
	 * @param isUp
	 * @return
	 */
	@RequestMapping(value = "invest/records", method = RequestMethod.GET)
	HttpResponse bidRecords(@ModelAttribute BaseRequestForm paramForm,
							@RequestParam(required = false, value = "bidId") String bidId,
							@RequestParam(required = false, value = "timestamp") String timestamp,
							@RequestParam(required = false, value = "isUp") String isUp) throws Exception{
		HttpResponse response = new HttpResponse();
		if(!paramForm.validate() || !StringUtils.isNoneEmpty(bidId,isUp)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		List<BidInvestRecordsVO> bidInvestRecordsList = new ArrayList<BidInvestRecordsVO>();
		List<InvestRecordsVO> investList = bidInfoService.getBidInvestRecords(bidId,timestamp,isUp);
		for(InvestRecordsVO record:investList){
			BidInvestRecordsVO vo = new BidInvestRecordsVO();
			UserInfo user =this.userInfoService.getUser(null, null, String.valueOf(record.getInvestId()));
			if(null == user){
				continue;
			}
			String fullName =user.getFullName();
			if(StringUtils.isEmpty(fullName)){
				continue;
			}
			vo.setInvestorName(fullName.substring(0,1) + "**");
			if(record.getPrice().compareTo(new BigDecimal(1))<0){
				vo.setInvestAmount(1f);
			}else{
				vo.setInvestAmount(record.getPrice().intValue());
			}
			vo.setInvestTime(record.getTimestamp().getTime()/1000);
			//设置存管加息参数
			vo.setInterest("0");
			vo.setIsCG(record.getIsCG());
			bidInvestRecordsList.add(vo);
		}
		response.getData().put("items", bidInvestRecordsList);
		return response;
	}
	/**
	 * 标所有的投资记录
	 * @param paramForm
	 * @param bidId ；标的ID
	 * @return
	 */
	@RequestMapping(value = "invest/allRecords", method = RequestMethod.GET)
	HttpResponse bidAllRecords(@ModelAttribute BaseRequestForm paramForm,String bidId) throws Exception{
		HttpResponse response = new HttpResponse();
		if(!paramForm.validate() ){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		List<BidInvestRecordsVO> bidInvestRecordsList = new ArrayList<BidInvestRecordsVO>();
		List<InvestRecordsVO> investList = bidInfoService.getBidInvestAllRecords(bidId);
		for(InvestRecordsVO record:investList){
			BidInvestRecordsVO vo = new BidInvestRecordsVO();
			UserInfo user =this.userInfoService.getUser(null, null, String.valueOf(record.getInvestId()));
			if(null == user){
				continue;
			}
			String fullName =user.getFullName();
			if(StringUtils.isEmpty(fullName)){
				continue;
			}
			vo.setInvestorName(fullName.substring(0,1) + "**");
			if(record.getPrice().compareTo(new BigDecimal(1))<0){
				vo.setInvestAmount(1f);
			}else{
				vo.setInvestAmount(record.getPrice().intValue());
			}
			vo.setInvestTime(record.getTimestamp().getTime()/1000);
			vo.setPhone(record.getPhone());
			//设置存管加息参数
			vo.setInterest("0");
			vo.setIsCG(record.getIsCG());

			bidInvestRecordsList.add(vo);
		}
		response.getData().put("items", bidInvestRecordsList);
		return response;
	}
	/**
	 * 标的借款人信息
	 * ps：修改此接口之前请参考接口文档
	 * @return
	 */
	@RequestMapping(value = "borrower/info", method = RequestMethod.GET)
	HttpResponse borrowerInfo(@ModelAttribute BaseRequestForm paramForm,String bidId) throws Exception{
		HttpResponse response = new HttpResponse();
		//参数验证
		if(!paramForm.validate() || StringUtils.isEmpty(bidId)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}

		BidBorrowerInfo bbInfo = bidInfoService.getBidBorrowerInfo(Integer.valueOf(bidId)); //获取新的数据
		if(bbInfo == null){
			BorrowerInfoVO borrowerInfoVO = new BorrowerInfoVO();
			BorrowerInfo bInfo = bidInfoService.getBorrowerInfo(Integer.valueOf(bidId));
			if(bInfo == null){
				return response;
			}
			borrowerInfoVO.setInfoMsg(bInfo.getInfoMsg());
			borrowerInfoVO.setIdentify(bInfo.getIdentify());
			StringBuffer phoneBuffer = new StringBuffer(bInfo.getPhone());
			borrowerInfoVO.setPhone(phoneBuffer.replace(3,7,"****").toString());
			if(StringUtils.isNoneBlank(bInfo.getCompany()) && bInfo.getCompany().length() >= 4){
				StringBuffer companyBuffer = new StringBuffer(bInfo.getCompany());
				borrowerInfoVO.setCompany(companyBuffer.replace(2,bInfo.getCompany().length()-2, "*******").toString());
			}else{
				borrowerInfoVO.setCompany("");
			}
			borrowerInfoVO.setIncome(bInfo.getIncome());
			borrowerInfoVO.setIsCarCertified("TG".equals(bInfo.getIsCarCertified())?1:0);
			borrowerInfoVO.setIsHouseCertified("TG".equals(bInfo.getIsHouseCertified())?1:0);
			response.setData(CommonTool.toMap(borrowerInfoVO));

		}else{
			BidBorrowerInfoVO vo = new BidBorrowerInfoVO(bbInfo);
			response.setData(CommonTool.toMap(vo));
		}
		return response;
	}

	/**
	 * 证明文件
	 * junda.feng 2016-07-11
	 * @return
	 */
	@RequestMapping(value = "files/list", method = RequestMethod.GET)
	HttpResponse filesList(@ModelAttribute BaseRequestForm paramForm,String bidId) throws Exception{
		HttpResponse response = new HttpResponse();
		//参数验证
		if(!paramForm.validate() || StringUtils.isEmpty(bidId)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}

		List<BidFiles> accessoryList = bidInfoService.getBidPublicAccessoryFilesList(Integer.valueOf(bidId));

		if(accessoryList == null){
			return response;
		}
		List<BidPublicAccessoryFilesVO>  voList=new ArrayList<BidPublicAccessoryFilesVO>();

		for (BidFiles bidFiles : accessoryList) {
			BidPublicAccessoryFilesVO vo=new BidPublicAccessoryFilesVO();
			vo.setRiskId(bidFiles.getTypeId());
			vo.setTitle(bidFiles.getTypename());
			List<String> flist=bidFiles.getFileCodes();
			String[] contractUrls = new String[flist.size()];
			for(int i=0;i<flist.size();i++){
				String fileUrl=FileUtils.getPicURL(flist.get(i),Config.get("contact.url"));
				contractUrls[i] = fileUrl;
			}
			String fileUrl = Config.get("contactInfo.url");
			if(contractUrls!=null && contractUrls.length>0) {
				fileUrl += URLEncoder.encode(JSONArray.toJSONString(contractUrls), "UTF-8");
			}
			vo.setUrl(fileUrl+"&title="+URLEncoder.encode(bidFiles.getTypename(), "UTF-8"));
			voList.add(vo);
		}
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("items", voList);
		response.setData(map);
		return response;
	}

	/**
	 * 判断用户能否投资定向标
	 * junda.feng 2016-08-26
	 * @return
	 */
	@RequestMapping(value = "directional/validate", method = RequestMethod.GET)
	HttpResponse directionalValidate(@ModelAttribute BaseRequestFormExtend paramForm,String bidId) throws Exception{
		HttpResponse response = new HttpResponse();
		//参数验证
		if(!paramForm.validate() || StringUtils.isEmpty(bidId)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}
		int loanId = IntegerParser.parse(bidId);

		//需要验证是否符合条件
		bidInfoService.checkCanInvestBid(loanId,paramForm.getUserId(),"PT");
		Map<String, Object> map=new HashMap<String, Object>();
		response.setData(map);
		return response;
	}

	private String planStaus(int PlanId) {
        List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(PlanId,Integer.MAX_VALUE);
        String status = "";
        int i =0;
        for(PlanBidsStatus planBidsStatus : planBidsList){
            if(planBidsStatus.getStatus().equals("TBZ")&&Double.valueOf(planBidsStatus.getSurplusAmount())>=100){
                status = "TBZ";
                break;
            }else if (planBidsStatus.getStatus().equals("DFK")||(planBidsStatus.getStatus().equals("TBZ")&&Double.valueOf(planBidsStatus.getSurplusAmount())<100)){
                status = "DFK";
                if(i>0){//当有些标处于放款，有些标未放款
                    break;
                }
            }else if(planBidsStatus.getStatus().equals("HKZ")){
                status = "HKZ";
                i=i+1;
				/*break;*/
			}else if(planBidsStatus.getStatus().equals("YLB")){
				status = status;//流标的情况维持原状态不变
			}
			else {
				status = "YJQ";
			}
		}
		return status;
	}
}
