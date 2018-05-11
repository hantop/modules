package com.fenlibao.p2p.controller.v_3.v_3_0_0.bid;

import com.alibaba.fastjson.JSONArray;
import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.bid.*;
import com.fenlibao.p2p.model.enums.bid.BidOriginEnum;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.InvestRecordsVO;
import com.fenlibao.p2p.model.vo.NoviceBidVO;
import com.fenlibao.p2p.model.vo.bidinfo.*;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.StringHelper;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by laubrence on 2016/2/20.
 */
@RestController("v_3_0_0/bidInfoController")
@RequestMapping(value = "bidInfo", headers = APIVersion.v_3_0_0)
public class BidInfoController {

	@Resource
	private BidInfoService bidInfoService;

	@Resource
	private UserInfoService userInfoService;


	/**
	 * 首页
	 ***************************************************************
	 1 推荐产品规则
	 推荐位置显示规则：
	 存在很多可投标：
	 未登录/未投资：显示新手标
	 已登录非新手：显示以下规则推荐标：利率（最高）>期限（最长）>发布时间（最近）；

	 2 只存在一个新手标可投的情况：
	 未登录/未投资：显示可投的新手标；
	 已登录非新手：显示最近满标的新手标；

	 3 不存在可投标：
	 未投资和已投资均显示最近满标的新手标

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

		List<ShopTreasureInfo> hotList = new ArrayList<>();
		List<ShopTreasureInfo> recommendList = new ArrayList<>();

		String[] status = new String[]{Status.DFK.name(), Status.HKZ.name(), Status.YJQ.name()};
		boolean isNovice ;
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

		if (isNovice) {
			noviceBidList = this.bidInfoService.getBidList(new String[]{Status.TBZ.toString()}, null, 1, Status.S.name(), null,0,BigDecimal.ZERO);
		}
		if (!isNovice || noviceBidList.size() == 0 || noviceBidList == null) {
			String[] recommendStatus = new String[]{Status.TBZ.name()};
			noviceBidList = this.bidInfoService.getBidRecommedList(recommendStatus, 1, Status.F.name(),0,BigDecimal.ZERO);
			if (null == noviceBidList || 0 == noviceBidList.size()) {
				noviceBidList = this.bidInfoService.getNoviceBidList(new String[]{Status.DFK.name(), Status.HKZ.name(), Status.YJQ.name()}, 1,0,BigDecimal.ZERO);
			}
		}

		if (null != noviceBidList && noviceBidList.size() > 0) {
			ShopTreasureInfo noviceBid = noviceBidList.get(0);
			recommendList.add(noviceBid);
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

		//短期标2个（消费信贷1个+常规标1个）
		//消费信贷标
		List<ShopTreasureInfo> xfxdBidList = this.bidInfoService.getXfxdShortBidList(null);
		if (null != xfxdBidList && xfxdBidList.size() > 0) {
			for (ShopTreasureInfo rateBid : xfxdBidList) {
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
			vo.setBidTitle(name.replaceAll("(?<=.{8}).+","…"));
			vo.setBidType(info.getBidType());
			vo.setBidYield(String.valueOf(info.getRate()));
			vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name())?1:0);
			vo.setLoanDays(info.getLoanDays());
			vo.setLoanMonth(info.getMonth());
			vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType())?info.getAssetsType().split(","):new String[0]);
			vo.setTimestamp(DateUtil.dateToTimestampToSec(info.getPublishDate()));
			vo.setLoanAmount(info.getLoanAmount().toString());
			if (Status.HKZ.name().equals(info.getStatus()) || Status.YJQ.name().equals(info.getStatus()) || Status.DFK.name().equals(info.getStatus())) {
				vo.setSurplusAmount(BigDecimal.ZERO.toString());
			} else {
				vo.setSurplusAmount(info.getVoteAmount().toString());
			}
			vo.setProgressRate(info.getLoanAmount(), info.getVoteAmount());
			vo.setBidClassify(Status.S.name().equals(info.getIsNoviceBid())?1:0);
			vo.setIsCG(info.getIsCG());
			vo.setInterest(info.getInterest()< 0.001?"0":Double.toString(info.getInterest()));
			//判断是否定向标
			if(info.getDirectionalBid()!=null&&info.getUserTotalAssets()!=null){
				vo.setBidClassify(2);
				vo.setUserTotalAssets(StringHelper.outputdollars(info.getUserTotalAssets()));
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
				if(info.getFundraisDate().getTime() > DateUtil.nowDate().getTime()){
					vo.setBidStatus(2);
				}else{
					vo.setBidStatus(3);
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
			if (info.getDirectionalBid() != null&&info.getAnytimeQuit()!=1&&info.getInterest()==0.0) {
				if (info.getUserTotalAssets() != null) {
					voList.add(vo);
				}
			}else if(info.getAnytimeQuit()!=1&&info.getInterest()==0.0){
				voList.add(vo);
			}
		}
		List<BidInfoVO> items = new ArrayList();//首页
		for (BidInfoVO vo:voList ){

			if(vo.getBidClassify()!=1){
				if (vo.getPanicBuyingTime()!=null && vo.getTimeLeft()>0){
					items.add(0, vo); //抢购标未到点时
				}else{
					items.add(vo);
				}
			}
		}
		for (BidInfoVO vo:voList ){
			vo.setInvestLimit("100");

			if (vo.getBidClassify()==1){
				items.add(0, vo); //新手标在最前
			}
		}
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
										String sortType,String sortBy,String bidOrigin) throws Exception{
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		List<BidInfoVO> voList = new ArrayList();

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
		map.put("limit", 50);
		boolean showPrecisionTitle = bidOrigin!=null&&"0002".equals(bidOrigin);

		List<ShopTreasureInfo> canInvestBidList = this.bidInfoService.getInvestmentBids(map);

		for(ShopTreasureInfo info : canInvestBidList){
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
			vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType())?info.getAssetsType().split(","):new String[0]);
			vo.setBidStatus(Status.YFB.name().equals(info.getStatus())?0:1);
			vo.setLoanAmount(info.getLoanAmount().toString());
			vo.setSurplusAmount(info.getVoteAmount().toString());
			vo.setProgressRate(info.getLoanAmount(), info.getVoteAmount());
			vo.setIsNoviceBid(Status.S.name().equals(info.getIsNoviceBid())?1:0);
			vo.setBidClassify(Status.S.name().equals(info.getIsNoviceBid())?1:0);
			vo.setIsCG(info.getIsCG());
			//判断是否定向标
			if(info.getDirectionalBid()!=null&&info.getUserTotalAssets()!=null){
				vo.setBidClassify(2);
				vo.setUserTotalAssets(StringHelper.outputdollars(info.getUserTotalAssets()));
			}
			//设置抢购标参数
			if (info.getPanicBuyingTime()!=null){
				long timeLeft=(info.getPublishDate().getTime()-System.currentTimeMillis())/1000+InterfaceConst.EEROR_SECOND;
				Integer countdown=Integer.valueOf(Config.get("bid.countdown.mimute"))*60;
				vo.setTimeLeft(timeLeft>0?timeLeft:0);
				vo.setCountdown(countdown);
				vo.setPanicBuyingTime(DateUtil.dateToTimestampToSec(info.getPublishDate()));
			}
			vo.setRepaymentMode(info.getRepaymentMode());
			vo.setInterest(info.getInterest()< 0.001?"0":Double.toString(info.getInterest()));

			if (info.getDirectionalBid() != null&&info.getAnytimeQuit()!=1&&info.getInterest()==0.0) {
				if (info.getUserTotalAssets() != null) {
					voList.add(vo);
				}
			}else if(info.getAnytimeQuit()!=1&&info.getInterest()==0.0){
				voList.add(vo);
			}
		}
		List<BidInfoVO> items = new ArrayList();

		for (BidInfoVO vo:voList ){
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
		}
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
		String[] status = new String[]{Status.DFK.name(), Status.HKZ.name(), Status.YJQ.name()};

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		map.put("limit", 10);
		if(StringUtils.isNotEmpty(timestamp)){
			Date time = DateUtil.timestampToDateBySec(Long.valueOf(timestamp));
			map.put("time", time);
		}
		map.put("bidOrigin", bidOrigin);
		boolean showPrecisionTitle = bidOrigin!=null&&"0002".equals(bidOrigin);

		List<ShopTreasureInfo> list = this.bidInfoService.getBidList(map);

		for (ShopTreasureInfo info : list) {
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
			vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name())?1:0);
			vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType())?info.getAssetsType().split(","):new String[0]);
			vo.setBidType(info.getBidType());
			vo.setProgressRate_100(); //已过期的都是100%
			vo.setRepaymentMode(info.getRepaymentMode());
			vo.setIsCG(info.getIsCG());
			//标的状态
			if(Status.DFK.name().equals(info.getStatus())){
				if( info.getFundraisDate().getTime() > DateUtil.nowDate().getTime()){
					vo.setBidStatus(2);
				}else{
					vo.setBidStatus(3);
				}
			}
			if(Status.HKZ.name().equals(info.getStatus())){
				vo.setBidStatus(4);
			}
			if(Status.YJQ.name().equals(info.getStatus())){
				vo.setBidStatus(5);
			}
			vo.setLoanAmount(info.getLoanAmount().toPlainString());
			//设置存管加息参数
			vo.setInterest(info.getInterest()< 0.001?"0":Double.toString(info.getInterest()));

			voList.add(vo);
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
		vo.setNoviceBidTitle(name.replaceAll("(?<=.{8}).+","…"));
		vo.setTimestamp(String.valueOf(DateUtil.dateToTimestampToSec(info.getPublishDate())));
		vo.setYield(String.valueOf(info.getRate()));
		vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType())?info.getAssetsType().split(","):new String[0]);
		//设置存管加息参数
		vo.setInterest("0");
		vo.setIsCG(info.getIsCG());

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
									 String userId,String bidId) throws Exception{
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate()||StringUtils.isEmpty(bidId)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		ShopTreasureInfo info=this.bidInfoService.getBidInfo(Integer.parseInt(bidId));
		if(null==info){
			response.setCodeMessage(ResponseCode.BID_DETAILS_EMPTY);
			return response;
		}
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
		if(info.getDirectionalBid()!=null&&info.getUserTotalAssets()!=null){
			vo.setBidClassify(2);
//			DecimalFormat dft = new DecimalFormat("#,##0");
			vo.setUserTotalAssets(info.getUserTotalAssets().toPlainString());
		}
		vo.setBidType(info.getBidType());
		vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType())?info.getAssetsType().split(","):new String[0]);
		vo.setRemark(info.getBorrowingDescribe());
		vo.setBorrowerUrl(Config.get("bid.borrower.url")+bidId);
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
		if(Status.TBZ.name().equals(info.getStatus())){
			vo.setBidStatus(1);
		}
		if(Status.DFK.name().equals(info.getStatus())){
			if(info.getFundraisDate().getTime() > DateUtil.nowDate().getTime()){
				vo.setBidStatus(2);
			}else{
				vo.setBidStatus(3);
			}
		}
		if(Status.HKZ.name().equals(info.getStatus())){
			vo.setBidStatus(4);
		}
		if(Status.YJQ.name().equals(info.getStatus())){
			vo.setBidStatus(5);
		}
		vo.setLawFiles(bidInfoService.getBidPublicAccessoryFiles(Integer.valueOf(bidId)));
		vo.setLawFileUrl(bidInfoService.getBidPublicAccessoryFileUrl(Integer.valueOf(bidId)));
		//服务协议
		//String fwxy = bidInfoService.getGroupItemValue(Integer.parseInt(bidId), InterfaceConst.BID_EXTEND_ASSIGNMENT_CODE);
		vo.setFwxyUrl(userInfoService.getUserServiceAgreementUrl(Integer.valueOf(StringUtils.isBlank(userId) ? "-1" : userId)));
		//组信息
		List<BidExtendGroupVO> groupInfoList = this.bidInfoService.getGroupInfoList(Integer.parseInt(bidId));
		vo.setGroupInfoList(groupInfoList);
		//是否已申购
		if(StringUtils.isNoneEmpty(userId)){
			BidBaseInfo bidInfo = this.bidInfoService.getBidBaseInfoByUser(Integer.valueOf(userId),Integer.valueOf(bidId));
			if(bidInfo!=null){
				vo.setPurchasedStatus(1);
			}else{
				vo.setPurchasedStatus(0);
			}
		}
		//担保借款合同
		if(BidOriginEnum.qqm.getCode().equals(info.getBidOrigin())){
			vo.setGuaranteeFileUrl(Config.get("bid.guaranteeFile.url"));
		}
		//设置抢购标参数 20161214 by:kris
		if (info.getPanicBuyingTime()!=null){
			long timeLeft=(info.getPublishDate().getTime()-System.currentTimeMillis())/1000+InterfaceConst.EEROR_SECOND;
			Integer countdown=Integer.valueOf(Config.get("bid.countdown.mimute"))*60;
			vo.setTimeLeft(timeLeft>0?timeLeft:0);
			vo.setCountdown(countdown);
			vo.setPanicBuyingTime(DateUtil.dateToTimestampToSec(info.getPublishDate()));
		}

		// TODO: 2016/12/19
		vo.setInterest(info.getInterest()< 0.001?"0":Double.toString(info.getInterest()));
		vo.setIsCG(info.getIsCG());//是否是存管类型的标 1:不是 2:是
		vo.setInvestLimit("100");

		response.setData(CommonTool.toMapDefaultNull(vo));
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

		//如果是定向标 需要验证是否符合条件
		DirectionalBid directionalBid=bidInfoService.getDirectionalBid(loanId);
		if(directionalBid!=null){
			//用户资产总额
			if(directionalBid.getTotalUserAssets()!=null){
				BigDecimal totalAssets =  userInfoService.getUserTotalAssets(paramForm.getUserId());
				if(directionalBid.getTotalUserAssets().compareTo(totalAssets)>0){
					response.setCodeMessage(ResponseCode.BID_DIRECTIONAL_TOTAL_USER_ASSETS);
					return response;
				}
			}
		}
		Map<String, Object> map=new HashMap<String, Object>();
		response.setData(map);
		return response;
	}
}
