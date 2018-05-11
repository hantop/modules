package com.fenlibao.p2p.controller.v_1.v_1_0_0.bid;

import com.dimeng.util.parser.IntegerParser;

import com.fenlibao.p2p.common.util.json.Jackson;
import com.fenlibao.p2p.common.util.repayment.CalCapitalInterestUtil;
import com.fenlibao.p2p.model.entity.PlanBidsStatus;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.bid.*;
import com.fenlibao.p2p.model.entity.borrow.BorrowerDetail;
import com.fenlibao.p2p.model.entity.creditassignment.TransferInInfo;
import com.fenlibao.p2p.model.entity.creditassignment.UserCoupons;
import com.fenlibao.p2p.model.entity.finacing.PlanFinacing;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.enums.bid.GuaranteeMeasureEunm;
import com.fenlibao.p2p.model.enums.bid.RepaymentModeEnum;
import com.fenlibao.p2p.model.enums.loan.LoanUserEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.InvestRecordsVO;
import com.fenlibao.p2p.model.vo.bidinfo.*;
import com.fenlibao.p2p.service.AccessoryInfoService;
import com.fenlibao.p2p.service.FinacingService;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.bid.PlanInfoService;
import com.fenlibao.p2p.service.bid.PreDoBidService;
import com.fenlibao.p2p.service.borrow.BorrowInfoService;
import com.fenlibao.p2p.service.creditassignment.TransferInService;
import com.fenlibao.p2p.service.thirdparty.ThirdpartyService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.StatusUtil;

import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.api.ssq.ShangshangqianUtil;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.paginator.domain.Order;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import com.fenlibao.p2p.util.paginator.domain.Pager;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedHashTreeMap;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by laubrence on 2016/2/20.
 */
@RestController("v_1_0_0/bidInfoController")
@RequestMapping("bidInfo")
public class BidInfoController {
    private static final Logger logger = LogManager.getLogger(BidInfoController.class);

    public static DecimalFormat df = new DecimalFormat("#,###");

    @Resource
    private BidInfoService bidInfoService;

    @Resource
    private AccessoryInfoService accessoryInfoService;

    @Resource
    private FinacingService finacingService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ITradeService tradeService;

    @Resource
    private PreDoBidService preDoBidService;

    @Resource
    TransferInService transferInService;

    @Resource
    PlanInfoService planInfoService;

    @Resource
    BorrowInfoService borrowInfoService;

    @Resource
    ThirdpartyService thirdpartyService;

//    /**
//     * 可投资的标的
//     * @param paramForm
//     * @param sortType   排序类型(1:时间 2：利率 3：进度)
//     * @param sortBy     排序方式(1: 降序2：升序)，默认降序
//     * @return
//     */
//    @RequestMapping(value = "caninvest/list", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
//	public HttpResponse getCaninvestBid(@ModelAttribute BaseRequestForm  paramForm,
//			@RequestParam(required = false, value="sortType") String sortType,
//			@RequestParam(required = false, value="sortBy") String sortBy) throws Exception{
//		HttpResponse response = new HttpResponse();
//		if (!paramForm.validate()) {
//			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
//			return response;
//		}
//		List<BidInfoVO> voList = new ArrayList<BidInfoVO>();
//
//		if(StringUtils.isNotEmpty(sortType)){
//			sortType = SortEnum.getOrderType(sortType);
//		}
//        if(StringUtils.isNotEmpty(sortBy)){
//        	sortBy = sortBy.equals("1")?InterfaceConst.ORDER_TYPE_DESC:InterfaceConst.ORDER_TYPE_ASC;
//		}
//
//		List<ShopTreasureInfo> canInvestBidList = this.bidInfoService.getBidList(new String[]{Status.TBZ.name()},sortType , sortBy, 0,null);
//
//		for(ShopTreasureInfo info : canInvestBidList){
//			BidInfoVO vo = new BidInfoVO();
//			vo.setBidId(info.getId());
//			vo.setBidTitle(info.getName());
//			vo.setBidYield(String.valueOf(info.getRate()));
//			vo.setLoanDays(info.getLoanDays());
//			vo.setLoanMonth(info.getMonth());
//			vo.setTimestamp(DateUtil.dateToTimestampToSec(info.getPublishDate()));
//			vo.setIsNoviceBid(Status.S.name().equals(info.getIsNoviceBid())?1:0);
//			vo.setBidType(info.getBidType());
//			vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType())?info.getAssetsType().split(","):new String[0]);
//			vo.setBidStatus(1);
//			voList.add(vo);
//		}
//		response.getData().put("items", voList);
//		return response;
//	}
//
//    /**
//     * 已过期的标(满额、到期)
//     * @param paramForm
//     * @param timestamp  排序 (发标时间)
//     * @return
//     */
//    @RequestMapping(value = "history/list", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
//	public HttpResponse getHistoryBid(@ModelAttribute BaseRequestForm  paramForm,
//			@RequestParam(required = false, value="timestamp") String timestamp) throws Exception{
//		HttpResponse response = new HttpResponse();
//		if (!paramForm.validate()) {
//			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
//			return response;
//		}
//		List<BidInfoVO> voList = new ArrayList<BidInfoVO>();
//		String[] status = new String[]{Status.DFK.name(), Status.HKZ.name(), Status.YJQ.name()};
//		List<ShopTreasureInfo> list = this.bidInfoService.getBidList(status,timestamp, 10,null,null);
//
//		for (ShopTreasureInfo info : list) {
//			BidInfoVO vo = new BidInfoVO();
//			vo.setBidId(info.getId());
//			vo.setBidTitle(info.getName());
//			vo.setBidYield(String.valueOf(info.getRate()));
//			vo.setLoanDays(info.getLoanDays());
//			vo.setLoanMonth(info.getMonth());
//			vo.setTimestamp(DateUtil.dateToTimestampToSec(info.getPublishDate()));
//			vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name())?1:0);
//			vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType())?info.getAssetsType().split(","):new String[0]);
//			vo.setBidType(info.getBidType());
//
//            //标的状态
//            if(Status.DFK.name().equals(info.getStatus())){
//            	if( info.getFundraisDate().getTime() > DateUtil.nowDate().getTime()){
//                	vo.setBidStatus(2);
//                }else{
//                	vo.setBidStatus(3);
//                }
//            }
//			if(Status.HKZ.name().equals(info.getStatus())){
//				vo.setBidStatus(4);
//			}
//			if(Status.YJQ.name().equals(info.getStatus())){
//				vo.setBidStatus(5);
//			}
//            voList.add(vo);
//		}
//		response.getData().put("items", voList);
//		return response;
//	}
//
//    /**
//	 * 新手标  筹款到期时间、发布时间升序，取第一个
//	 * @param paramForm
//	 * @return
//	 */
//	@RequestMapping(value = "noviceBid", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
//	public HttpResponse getNoviceBid(@ModelAttribute BaseRequestForm  paramForm) throws Exception{
//		HttpResponse response = new HttpResponse();
//		if (!paramForm.validate()) {
//			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
//			return response;
//		}
//		List<ShopTreasureInfo> list = this.bidInfoService.getBidList(new String[]{Status.TBZ.toString()},null, 1, Status.S.name(),null);
//
//		//当前新手标
//		if(null==list||list.size()==0){
//			return response;
//		}
//		ShopTreasureInfo info = list.get(0);
//
//		NoviceBidVO vo = new NoviceBidVO();
//		vo.setBidId(info.getId());
//		vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name())?1:0);
//		vo.setLoanDays(info.getLoanDays());
//		vo.setNoviceBidTitle(info.getName());
//		vo.setTimestamp(String.valueOf(DateUtil.dateToTimestampToSec(info.getPublishDate())));
//		vo.setYield(String.valueOf(info.getRate()));
//		vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType())?info.getAssetsType().split(","):new String[0]);
//
//		response.setData(CommonTool.toMap(vo));
//		return response;
//	}

    @RequestMapping(value = "/list", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse getBidInfoList(@ModelAttribute PageRequestForm pageRequestForm,
                                       @RequestParam(required = false, value = "minDays") String minDays,
                                       @RequestParam(required = false, value = "maxDays") String maxDays,
                                       @RequestParam(required = false, value = "bidType") String bidType,
                                       @RequestParam(required = false, value = "sortType") String sortType,
                                       @RequestParam(required = false, value = "sortBy") String sortBy,
                                       @RequestParam(required = false, value = "productType") String productType,
                                       @RequestParam(required = false, value = "versionType") VersionTypeEnum versionType,
                                       @RequestParam(required = false, value = "userId") String userId) {
        HttpResponse response = new HttpResponse();
        if (!pageRequestForm.validate()) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        if(StringUtils.isEmpty(productType)){
            productType = "plan";
        }
        int minDaysInteger = -1;
        int maxDaysInteger = -1;
        int limit = Integer.valueOf(pageRequestForm.getLimit());
        int page = Integer.valueOf(pageRequestForm.getPage());
        // 可投资的标的数量
        int canInvestBidCount = 0;

        if (StringUtils.isNotEmpty(minDays)) {
            minDaysInteger = Integer.valueOf(minDays);
        }
        if (StringUtils.isNotEmpty(maxDays)) {
            maxDaysInteger = Integer.valueOf(maxDays);
        }

        if (StringUtils.isNotEmpty(sortType)) {
            sortType = SortEnum.getOrderType(sortType);
        }
        if (StringUtils.isNotEmpty(sortBy)) {
            sortBy = sortBy.equals("1") ? InterfaceConst.ORDER_TYPE_ASC : InterfaceConst.ORDER_TYPE_DESC;
        }

        try {
            List<BidInfoVO> bidVOs = new ArrayList<>();
            String[] canInvestBidStatuses = null;
            PageBounds pageBounds = new PageBounds(page, limit);
            //统计平台可投产品
            int count = this.bidInfoService.countIvestProduct(productType,String.valueOf(versionType.getIndex()));
            //是否需要历史记录列表 1：需要 0：不需要
            int isNeedHis = 0;
            if(count<10){
                isNeedHis = 1;
            }
            int isNoviceUser = 1;
            if(!StringUtils.isEmpty(userId)){
                boolean isNovice = bidInfoService.isNovice(Integer.valueOf(userId)); //是否是新手
                if(!isNovice){
                    isNoviceUser = 0;
               }
            }


            List<ShopTreasureInfo> bids = this.bidInfoService.getBidInfoAndPlanAndHisBidList(bidType, canInvestBidStatuses, minDaysInteger, maxDaysInteger, sortType, sortBy, pageBounds,productType,versionType==null?1:versionType.getIndex(),isNeedHis,isNoviceUser);

            Pager pager = new Pager(bids);
           if(count<10){
                pager.setTotalCount(10);
                pager.setTotalPages(1);
            }
            BigDecimal unberMoney = new BigDecimal(100);

            for (ShopTreasureInfo info : bids) {
                BidInfoVO vo = new BidInfoVO();
                if (info.getItemType() == 1) {
                    //info.setStatus(planStaus(info.getId()));
                    info.setStatus(StatusUtil.status(info.getStatus()));
                    vo.setPlanId(info.getId());
                    if(!StringUtils.isEmpty(info.getNumber())){
                        vo.setPlanTitle(info.getName()+info.getNumber());
                    }else {
                        vo.setPlanTitle(info.getName());
                    }
                    // vo.setPlanTitle(info.getName());
                    vo.setComment(info.getComment());
                    vo.setHighRate(String.valueOf(info.getHighRate()));
                    vo.setLowRate(String.valueOf(info.getLowRate()));
                    vo.setPlanType(info.getPlanType());
                } else {
                    vo.setBidId(info.getId());
                    vo.setBidTitle(info.getName());
                }
                vo.setBidYield(String.valueOf(info.getRate()));
                vo.setLoanDays(info.getLoanDays());
                vo.setLoanMonth(info.getMonth());
                vo.setTimestamp(DateUtil.dateToTimestampToSec(info.getPublishDate()));
                vo.setIsNoviceBid(Status.S.name().equals(info.getIsNoviceBid()) ? 1 : 0);
                vo.setBidType(info.getBidType());
                vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType()) ? info.getAssetsType().split(",") : new String[0]);
                vo.setLoanAmount(info.getLoanAmount().toString());
                vo.setSurplusAmount(info.getVoteAmount().toString());
                vo.setBidClassify(Status.S.name().equals(info.getIsNoviceBid()) ? 1 : 0);
                //判断是否定向标
                if (info.getDirectionalBid() != null) {
                    vo.setBidClassify(2);
                }
                //设置抢购标参数
                if (info.getPanicBuyingTime() != null) {
                    long timeLeft = (info.getPublishDate().getTime() - System.currentTimeMillis()) / 1000 + InterfaceConst.EEROR_SECOND;
                    Integer countdown = Integer.valueOf(Config.get("bid.countdown.mimute")) * 60;
                    vo.setTimeLeft(timeLeft > 0 ? timeLeft : 0);
                    vo.setCountdown(countdown);
                    vo.setPanicBuyingTime(DateUtil.dateToTimestampToSec(info.getPublishDate()));
                    vo.setBidClassify(3);
                }
                vo.setRepaymentMode(info.getRepaymentMode());
                // 标的状态
                if (Status.TBZ.name().equals(info.getStatus())) {
                    vo.setBidStatus(1);
                    // 如果可投金额低于100改为已满额状态
                    if (unberMoney.compareTo(info.getVoteAmount()) == 1) {
                        vo.setBidStatus(2);
                    }
                }
                if (Status.DFK.name().equals(info.getStatus())) {
                    if (info.getItemType() == 1) {
                        vo.setBidStatus(2);
                    } else {
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
                vo.setIsDepository(info.getIsDepository());//是否是存管标

                List<String> bidLabels = new ArrayList<>();
                if (Status.S.name().equals(info.getIsNoviceBid())) {
                    bidLabels.add("新手专享");
                }
                if (info.getUserTotalAssets() != null||info.getAccumulatedIncome() != null||info.getUserInvestAmount() != null||info.getTargetUser() == 1) {
                    bidLabels.add("大客户专享");
                }
                if (info.getAnytimeQuit() == 1) {
                    bidLabels.add("随时退出");
                }
                if (info.getIsDepository() == 2) {
                    bidLabels.add("银行存管");
                }
                String[] selfLabel = StringUtils.isNotEmpty(info.getBidLabel()) ? info.getBidLabel().split(",") : new String[0];
                for (int i = 0; i < selfLabel.length; i++) {
                    bidLabels.add(selfLabel[i]);
                }

                if (bidLabels != null && bidLabels.size() > 0) {
                    bidLabels = bidLabels.subList(0, bidLabels.size() >= 2 ? 2 : bidLabels.size());
                    vo.setBidLabel(bidLabels);
                }
                vo.setBidInterestRise(info.getBidInterestRise());
                vo.setItemType(info.getItemType());
                vo.setAnytimeQuit(info.getAnytimeQuit());
                vo.setBidInterestRise(info.getBidInterestRise());
                vo.setPlanType(info.getPlanType());
                vo.setComment(info.getComment());
                vo.setLowRate(String.valueOf(info.getLowRate()));
                vo.setHighRate(String.valueOf(info.getHighRate()));
                List<String> markList = new ArrayList<>();
                if (info.getAccumulatedIncome() != null) {
                    markList.add("累计利息达" + df.format(Double.valueOf(info.getAccumulatedIncome())) + "元客户专享");
                }
                if (info.getUserTotalAssets() != null) {
                    markList.add("账户资产达" + df.format(info.getUserTotalAssets()) + "元客户专享");
                }
                if (info.getUserInvestAmount() != null) {
                    markList.add("已出借额达" + df.format(Double.valueOf(info.getUserInvestAmount())) + "客户专享");
                }
                if (info.getTargetUser() == 1) {
                    markList.add("vip客户专享");
                }
                vo.setMarkArray(markList);

                bidVOs.add(vo);
            }
            pager.setItems(bidVOs);
            response.setData(CommonTool.toMap(pager));
        } catch (Exception e) {
            logger.error("[BidInfoController.getBidInfoList]", e);
            response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
        }
        return response;
    }

    /**
     * 标的详情
     *
     * @param paramForm
     * @param token
     * @param userId
     * @param bidId     标的ID
     * @return
     */
    @RequestMapping(value = "detail", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse getBidDetail(@ModelAttribute BaseRequestForm paramForm,
                                     @RequestParam(required = false, value = "token") String token,
                                     @RequestParam(required = false, value = "userId") String userId,
                                     @RequestParam(required = false, value = "bidId") String bidId,
                                     @RequestParam(required = false, value = "planId") String planId,
                                     @RequestParam(required = false, value = "creditId") String creditId) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate() || (StringUtils.isEmpty(bidId) && StringUtils.isEmpty(planId))) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        ShopTreasureInfo info = null;

        if (!StringUtils.isEmpty(bidId)) {
            info = this.bidInfoService.getBidInfo(Integer.parseInt(bidId));
        } else if (!StringUtils.isEmpty(planId)) {
            info = this.bidInfoService.getPlanDetail(Integer.parseInt(planId));
        }
        if (null == info) {
            response.setCodeMessage(ResponseEnum.RESPONSE_BID_EMPTY.getCode(), ResponseEnum.RESPONSE_BID_EMPTY.getMessage());
            return response;
        }
        double bidInterestRise = info.getBidInterestRise();
        if(!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(creditId)) {
            Integer bidRecord = transferInService.getBidRecordId(Integer.valueOf(creditId));
            List<Integer> tenderIdList = new ArrayList<Integer>();
            tenderIdList.add(bidRecord);
            Map<String, Object> tenderMap = new HashMap<>();
            tenderMap.put("list", tenderIdList);
            List<UserCoupons> userCouponsList = new ArrayList<UserCoupons>();
            if (!tenderIdList.isEmpty()) {
                userCouponsList = transferInService.getUserCoupons(tenderMap);
            }

            if (!userCouponsList.isEmpty()) {
                for (UserCoupons userCoupons : userCouponsList) {
                    if (bidRecord == userCoupons.getTenderId()) {
                        bidInterestRise = bidInterestRise + userCoupons.getScope();
                    }
                }
            }
        }

        BidDetailVO vo = new BidDetailVO();
        if (info.getItemType() == 0) {
            vo.setBidId(info.getId());
            vo.setBidTitle(info.getName());
            vo.setBidYield(String.valueOf(info.getRate()));
            vo.setRepaymentMode(info.getRepaymentMode());
            vo.setLoanDays(info.getLoanDays());
            vo.setLoanMonth(info.getMonth());
            vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name()) ? 1 : 0);
            if (info.getIsNoviceBid().equals("S")) {
                vo.setPlanCanQuit(0);
            }
            //判断是否定向标
            vo.setBidClassify(Status.S.name().equals(info.getIsNoviceBid()) ? 1 : 0);
            int rulesCount = 0;
            if (info.getDirectionalBid() != null) {
                vo.setBidClassify(2);
//			DecimalFormat dft = new DecimalFormat("#,##0");
                if (info.getUserTotalAssets() != null) {
                    vo.setUserTotalAssets(info.getUserTotalAssets().toPlainString());
                    rulesCount ++;
                }
                if (info.getTargetUser() == 1) {
                    vo.setTargetUser(info.getTargetUser());
                    rulesCount ++;
                }
                if (info.getUserAccumulatedIncome() != null) {
                    vo.setUserAccumulatedIncome(info.getUserAccumulatedIncome());
                    rulesCount ++;
                }
                if (info.getUserInvestingAmount() != null) {
                    vo.setUserInvestingAmount(info.getUserInvestingAmount());
                    rulesCount ++;
                }
                vo.setRulesCount(rulesCount);
            }
            //设置抢购标参数
            if (info.getPanicBuyingTime() != null) {
                long timeLeft = (info.getPublishDate().getTime() - System.currentTimeMillis()) / 1000 + InterfaceConst.EEROR_SECOND;
                Integer countdown = Integer.valueOf(Config.get("bid.countdown.mimute")) * 60;
                vo.setTimeLeft(timeLeft > 0 ? timeLeft : 0);
                vo.setCountdown(countdown);
                vo.setPanicBuyingTime(DateUtil.dateToTimestampToSec(info.getPublishDate()));
                vo.setBidClassify(3);
            }
            vo.setBidType(info.getBidType());
            vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType()) ? info.getAssetsType().split(",") : new String[0]);
            vo.setRemark(info.getBorrowingDescribe());
            vo.setBorrowerUrl(Config.get("bid.borrower.url") + bidId);
            vo.setLoanAmount(info.getLoanAmount().toString());
            vo.setSurplusAmount(info.getVoteAmount().toString());
            if (info.getBidReviewedTime() != null) {
                long curTime = DateUtil.dateToTimestampToSec(new Date());
                if (info.getBidFullTime() != null) {
                    curTime = DateUtil.dateToTimestampToSec(info.getBidFullTime());
                }
                vo.setDiffBidtimes(curTime - DateUtil.dateToTimestampToSec(info.getBidReviewedTime()));
            }
            vo.setTotalInvestPers(info.getTotalInvestPers());
            vo.setBidInterestRise(bidInterestRise);
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

            if(!StringUtils.isEmpty(creditId)) {
                if (Integer.valueOf(transferInService.getSuccessTransferDetail(Integer.valueOf(userId), Integer.valueOf(creditId))) > 0) {
                    vo.setBidStatus(5);
                }
            }
            vo.setLawFiles(bidInfoService.getBidPublicAccessoryFiles(Integer.valueOf(bidId)));
            vo.setLawFileUrl(bidInfoService.getBidPublicAccessoryFileUrl(Integer.valueOf(bidId)));

            //服务协议
            String fwxy = bidInfoService.getGroupItemValue(Integer.parseInt(bidId), InterfaceConst.BID_EXTEND_ASSIGNMENT_CODE);
            vo.setFwxyUrl(fwxy);

            //组信息
            List<BidExtendGroupVO> groupInfoList = this.bidInfoService.getGroupInfoList(Integer.parseInt(bidId));
            vo.setGroupInfoList(groupInfoList);

            //是否已申购
            if (StringUtils.isNoneEmpty(userId)) {
                BidBaseInfo bidInfo = this.bidInfoService.getBidBaseInfoByUser(Integer.valueOf(userId), Integer.valueOf(bidId));
                if (bidInfo != null) {
                    vo.setPurchasedStatus(1);
                } else {
                    vo.setPurchasedStatus(0);
                }
            }
            List<String> bidLabels = new ArrayList<>();
            if (Status.S.name().equals(info.getIsNoviceBid())) {
                bidLabels.add("新手专享");
            }
            if (info.getUserTotalAssets() != null||info.getAccumulatedIncome() != null||info.getUserInvestAmount() != null||info.getTargetUser() == 1) {
                bidLabels.add("大客户专享");
            }
            if (info.getAnytimeQuit() == 1) {
                bidLabels.add("随时退出");
            }
            if (info.getIsDepository() == 2) {
                bidLabels.add("银行存管");
            }
            String[] selfLabel = StringUtils.isNotEmpty(info.getBidLabel()) ? info.getBidLabel().split(",") : new String[0];
            for (int i = 0; i < selfLabel.length; i++) {
                bidLabels.add(selfLabel[i]);
            }

            if (bidLabels != null && bidLabels.size() > 0) {
                bidLabels = bidLabels.subList(0, bidLabels.size() >= 2 ? 2 : bidLabels.size());
                vo.setBidLabel(bidLabels);
            } else {
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
                markList.add("已出借额达" + df.format(Double.valueOf(info.getUserInvestAmount())) + "客户专享");
            }
            if (info.getTargetUser() == 1) {
                markList.add("vip客户专享");
            }
            vo.setMarkArray(markList);
            vo.setAnytimeQuit(info.getAnytimeQuit());
            vo.setItemType(0);
            vo.setIsDepository(info.getIsDepository());
        } else {
            //info.setStatus(planStaus(info.getId()));
            info.setStatus(StatusUtil.status(info.getStatus()));
            if(!StringUtils.isEmpty(info.getNumber())){
                vo.setPlanTitle(info.getName()+info.getNumber());
            }else {
                vo.setPlanTitle(info.getName());
            }
            vo.setPlanId(info.getId());
            // vo.setPlanTitle(info.getName());
            vo.setBidYield(String.valueOf(info.getRate()));
            vo.setRepaymentMode(info.getRepaymentMode());
            vo.setLoanDays(info.getLoanDays());
            vo.setLoanMonth(info.getMonth());
            vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name()) ? 1 : 0);
            vo.setTotalInvestPers(info.getTotalInvestPers());
            //判断是否定向标
            vo.setBidClassify(Status.S.name().equals(info.getIsNoviceBid()) ? 1 : 0);
            if (info.getDirectionalBid() != null) {
                vo.setBidClassify(2);
//			DecimalFormat dft = new DecimalFormat("#,##0");
                if (info.getUserTotalAssets() != null) {
                    vo.setUserTotalAssets(info.getUserTotalAssets().toPlainString());
                }
            }
            vo.setBidType(info.getBidType());
            vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType()) ? info.getAssetsType().split(",") : new String[0]);
            vo.setRemark(info.getBorrowingDescribe());
            vo.setBorrowerUrl(Config.get("bid.borrower.url") + bidId);
            vo.setLoanAmount(info.getLoanAmount().toString());
            vo.setSurplusAmount(info.getVoteAmount().toString());
            if (info.getBidReviewedTime() != null) {
                long curTime = DateUtil.dateToTimestampToSec(new Date());
                if (info.getBidFullTime() != null) {
                    curTime = DateUtil.dateToTimestampToSec(info.getBidFullTime());
                }
                vo.setDiffBidtimes(curTime - DateUtil.dateToTimestampToSec(info.getBidReviewedTime()));
            }
            if (info.getPanicBuyingTime() != null) {
                long timeLeft = (info.getPublishDate().getTime() - System.currentTimeMillis()) / 1000 + InterfaceConst.EEROR_SECOND;
                Integer countdown = Integer.valueOf(Config.get("bid.countdown.mimute")) * 60;
                vo.setTimeLeft(timeLeft > 0 ? timeLeft : 0);
                vo.setCountdown(countdown);
                vo.setPanicBuyingTime(DateUtil.dateToTimestampToSec(info.getPublishDate()));
                vo.setBidClassify(3);
            }
            vo.setTotalInvestPers(info.getTotalInvestPers());

            //标的状态
            if (Status.TBZ.name().equals(info.getStatus())) {
                vo.setBidStatus(1);
            }
            if (Status.DFK.name().equals(info.getStatus())) {
                vo.setBidStatus(2);
            }
            if (Status.HKZ.name().equals(info.getStatus())) {
                vo.setBidStatus(4);
            }
            if (Status.YJQ.name().equals(info.getStatus())) {
                vo.setBidStatus(5);
            }
            int isPurchasedPlan = 0;
            if (!StringUtils.isEmpty(userId)) {
                if (this.bidInfoService.getpurchasedPlan(Integer.valueOf(userId), Integer.valueOf(planId)) > 0) {
                    isPurchasedPlan = 1;
                }
            }
            vo.setPurchasedStatus(isPurchasedPlan);

            //服务协议
            // String fwxy = bidInfoService.getGroupItemValue(Integer.parseInt(bidId), InterfaceConst.BID_EXTEND_ASSIGNMENT_CODE);
            vo.setFwxyUrl(userInfoService.getUserServiceAgreementUrl(StringUtils.isBlank(userId) ? "-1" : userId));
            vo.setLawFiles(null);
            vo.setLawFileUrl(null);
            //组信息
            List<BidExtendGroupVO> groupInfoList = new ArrayList<>();
            vo.setGroupInfoList(groupInfoList);

            vo.setAnytimeQuit(info.getAnytimeQuit());
            vo.setBidInterestRise(info.getBidInterestRise());
            vo.setItemType(1);
            List<String> bidLabels = new ArrayList<>();
            if (Status.S.name().equals(info.getIsNoviceBid())) {
                bidLabels.add("新手专享");
            }
            if (info.getUserTotalAssets() != null||info.getAccumulatedIncome() != null||info.getUserInvestAmount() != null||info.getTargetUser() == 1) {
                bidLabels.add("大客户专享");
            }
            if (info.getAnytimeQuit() == 1) {
                bidLabels.add("随时退出");
            }
            if (info.getIsDepository() == 2) {
                bidLabels.add("银行存管");
            }
            String[] selfLabel = StringUtils.isNotEmpty(info.getBidLabel()) ? info.getBidLabel().split(",") : new String[0];
            for (int i = 0; i < selfLabel.length; i++) {
                bidLabels.add(selfLabel[i]);
            }

            if (bidLabels != null && bidLabels.size() > 0) {
                bidLabels = bidLabels.subList(0, bidLabels.size() >= 2 ? 2 : bidLabels.size());
                vo.setBidLabel(bidLabels);
            } else {
                vo.setBidLabel(bidLabels);
            }
            if (info.getPlanType().equals("2") && !info.getIsNoviceBid().equals("S")) {
                if (info.getMonth() != 0 && info.getMonth() > 1) {
                    vo.setPlanCanQuit(1);
                } else if (info.getLoanDays() != 0 && info.getLoanDays() > 30) {
                    vo.setPlanCanQuit(1);
                }

            } else if (info.getPlanType().equals("1") && !info.getIsNoviceBid().equals("S")) {
                vo.setPlanCanQuit(1);
            }

            List<String> markList = new ArrayList<>();
            if (info.getAccumulatedIncome() != null) {
                markList.add("累计利息达" + df.format(Double.valueOf(info.getAccumulatedIncome())) + "元客户专享");
            }
            if (info.getUserTotalAssets() != null) {
                markList.add("账户资产达" + df.format(info.getUserTotalAssets()) + "元客户专享");
            }
            if (info.getUserInvestAmount() != null) {
                markList.add("已出借额达" + df.format(Double.valueOf(info.getUserInvestAmount())) + "客户专享");
            }
            if (info.getTargetUser() == 1) {
                markList.add("vip客户专享");
            }
            vo.setMarkArray(markList);
            vo.setLowRate(String.valueOf(info.getLowRate()));
            vo.setHighRate(String.valueOf(info.getHighRate()));
            vo.setBonusRate(String.valueOf(info.getBonusRate()));
            vo.setComment(info.getComment());
            vo.setPlanType(info.getPlanType());
            vo.setNewPlan(1);
        }
        vo.setProjectInfo(ProjectInfoToJson(info,vo.getFwxyUrl()));
        if(info.getItemType()==0) {
            vo.setBorrowerInfo(BorrowerInfoToJson(borrowInfoService.getBorrowerDetail(Integer.valueOf(bidId))));
        }else {
            vo.setBorrowerInfo(new LinkedHashMap<String, Object>());
        }
      /*  LinkedHashMap<String,Object> map = new LinkedHashMap<String,Object>();
        if(!StringUtils.isEmpty(info.getBorrowingDescribe())){
            String[] array = info.getBorrowingDescribe().split("\n\n");
            for(int i =0;i<array.length;i++){
                if(i==0){
                    map.put("groupDescr",array[i]);
                }else  if(i==1){
                    map.put("riskMeasures",array[i]);
                }else  if(i==2){
                    map.put("exitRule",array[i]);
                }

            }
        }
        vo.setRemarkArray(map);*/
        vo.setIsShowFile(showAfterLoanInfo(info.getLoanDays(),info.getMonth(),info.getInterestDate()));

        //募集期
        if (info.getPublishDate() != null && info.getFundraisDate() != null){
            String publishDate = com.fenlibao.p2p.util.api.DateUtil.DateToString(info.getPublishDate(), com.fenlibao.p2p.util.api.DateUtil.yyyy_MM_dd);
            Date expiryDateSubOneSecond = com.fenlibao.p2p.util.api.DateUtil.timeAddOrSub(info.getFundraisDate() , Calendar.SECOND, -1);
            String expiryDate = com.fenlibao.p2p.util.api.DateUtil.DateToString(expiryDateSubOneSecond, com.fenlibao.p2p.util.api.DateUtil.yyyy_MM_dd);
            String recruitmentPeriod = publishDate + "至" + expiryDate + "。如到期仍未满标，出借资金将原路返还至用户账户。";
            vo.setRecruitmentPeriod(recruitmentPeriod);
        }

        response.setData(CommonTool.toMap(vo));
        return response;
    }


    /**
     * 推荐投资的标的
     *
     * @param paramForm
     * @return
     *
     * ****************************************************************
    新手投资
    1、首页-新手投资默认显示1个可投的新手标A（最早发布的）和1个可投的新手计划（期限最短的）
    ①若没有在投的新手标时，则显示2个可投的新手计划（优先顺序：10天>20天>30天）
    ②若没有在投的新手计划时， 显示2个最近满额的新手计划（优先顺序：已满额>利息中>已结清）
    ③若没有在投的新手标和新手计划时，显示2个最近满额的新手标/新手计划（优先顺序：已满额>利息中>已结清）

    计划投资
    1、1个在投的省心计划（利率最高的）和1个在投的月升计划（利率最高的）
    ①若没有在投的省心计划和月升计划，则显示2个最近满额的省心计划/月升计划
    ②若没有在投的月升计划，则显示2个利率较高的省心计划
    ③若没有在投的省心计划，则显示2个利率较高的月升计划

    散标投资
    1、首页-散标投资显示2~6条记录，以预期年利息降序排列，若利率相同则选择发布时间最早的
    2、默认为2~6个可投状态的常规标（开店融资、车贷、房贷等），可投标少于6个时，显示实际数量的常规标
    3、 若无可投标的，则显示2个已满额/利息中/已结清标的，以满额时间降序排列

     ****************************************************************/
    @RequestMapping(value = "recommend", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse getRecommendBid(@ModelAttribute BaseRequestForm paramForm, VersionTypeEnum versionType,BaseRequestFormExtend form) throws Exception {
        HttpResponse response = new HttpResponse();
        int orderLimit = 10; //显示的数量
        int limit = 2;
        int page = 1;
        String bidType = "XFXD";
        String[] statusNoTBZ = new String[]{Status.DFK.toString(),Status.HKZ.toString(),Status.YJQ.toString()};
        if (!paramForm.validate()||versionType == null) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        List<BidInfoVO> voList = new ArrayList<BidInfoVO>();

        List<ShopTreasureInfo> allList = new ArrayList<ShopTreasureInfo>();
        List<ShopTreasureInfo> noviceBidAndPlanList = new ArrayList<ShopTreasureInfo>();
        List<ShopTreasureInfo> planInvestList = new ArrayList<ShopTreasureInfo>();
        List<ShopTreasureInfo> bidList = new ArrayList<ShopTreasureInfo>();
        int noviceLimit = 2;
        /**新手投资**/
        List<ShopTreasureInfo> plansList = this.bidInfoService.getPlansList(new String[]{Status.TBZ.toString()}, null, -1, -1, SortEnum.getOrderType("4"), InterfaceConst.ORDER_TYPE_ASC, -1, -1, 2, Status.S.name(), -1, "timeSort", -1,versionType==null?1:versionType.getIndex());
        if(versionType!=null&&versionType.getCode().equals("PT")) {
            List<ShopTreasureInfo> noviceBidList = this.bidInfoService.getBidList(new String[]{Status.TBZ.toString()}, SortEnum.getOrderType("1"), InterfaceConst.ORDER_TYPE_ASC, 2, Status.S.name(), -1, -1, null, versionType == null ? 1 : versionType.getIndex());

            // List<ShopTreasureInfo> noviceBidList = this.bidInfoService.getBidListOrderByStatus(new String[]{Status.TBZ.toString()}, null, 1, Status.S.name(), null, null, -1, -1);

            //暂时去掉组合模块
            if (null != noviceBidList && noviceBidList.size() > 0) {//存在可投新手标
                if (null != plansList && plansList.size() > 0) {//存在可投新手计划
                    noviceBidAndPlanList.add(noviceBidList.get(0));
                    noviceBidAndPlanList.add(plansList.get(0));
                } else {//不存在可投的新手计划
                    if (noviceBidList.size() >= 2) {
                        noviceBidAndPlanList.addAll(noviceBidList);
                    } else {
                        List<ShopTreasureInfo> enInvestPlanList = this.bidInfoService.getPlansList(statusNoTBZ, null, -1, -1, SortEnum.getOrderType("5"), InterfaceConst.ORDER_TYPE_ASC, -1, -1, 1, Status.S.name(), -1, "timeSort", -1, versionType == null ? 1 : versionType.getIndex());
                        noviceBidAndPlanList.addAll(noviceBidList);
                        noviceBidAndPlanList.addAll(enInvestPlanList);
                    }
                }
            } else {//不存在可投新手标
                if (null != plansList && plansList.size() > 0) {//存在可投新手计划
                    if (plansList.size() < 2) {//只存在一个可投新手计划
                        List<ShopTreasureInfo> enInvestNoviceList = this.bidInfoService.getBidList(statusNoTBZ, SortEnum.getOrderType("5"), InterfaceConst.ORDER_TYPE_ASC, 1, Status.S.name(), -1, -1, null, versionType == null ? 1 : versionType.getIndex());
                        if (enInvestNoviceList != null && enInvestNoviceList.size() > 0) {//存在不可投的新手标
                            noviceBidAndPlanList.addAll(enInvestNoviceList);
                            noviceBidAndPlanList.addAll(plansList);
                        } else {//不存在不可投的新手标
                            List<ShopTreasureInfo> enInvestPlanList = this.bidInfoService.getPlansList(statusNoTBZ, null, -1, -1, SortEnum.getOrderType("5"), InterfaceConst.ORDER_TYPE_ASC, -1, -1, 1, Status.S.name(), -1, "timeSort", -1, versionType == null ? 1 : versionType.getIndex());
                            noviceBidAndPlanList.addAll(plansList);
                            noviceBidAndPlanList.addAll(enInvestPlanList);
                        }
                    } else {//存在两个可投新手计划
                        noviceBidAndPlanList.addAll(plansList);
                    }

                } else {//不存在可投新手计划
                    List<ShopTreasureInfo> enInvestNoviceList = this.bidInfoService.getBidList(statusNoTBZ, SortEnum.getOrderType("5"), InterfaceConst.ORDER_TYPE_ASC, 2, Status.S.name(), -1, -1, null, versionType == null ? 1 : versionType.getIndex());
                    List<ShopTreasureInfo> enInvestPlanList = this.bidInfoService.getPlansList(statusNoTBZ, null, -1, -1, SortEnum.getOrderType("5"), InterfaceConst.ORDER_TYPE_ASC, -1, -1, 2, Status.S.name(), -1, "timeSort", -1, versionType == null ? 1 : versionType.getIndex());
                    if (enInvestNoviceList != null && enInvestNoviceList.size() > 0) {
                        if (enInvestPlanList != null && enInvestPlanList.size() > 0) {
                            noviceBidAndPlanList.add(enInvestNoviceList.get(0));
                            noviceBidAndPlanList.add(enInvestPlanList.get(0));
                        } else {
                            noviceBidAndPlanList.addAll(enInvestNoviceList);
                        }
                    } else if (enInvestPlanList != null && enInvestPlanList.size() > 0) {
                        noviceBidAndPlanList.addAll(enInvestPlanList);
                    }
                }
            }
        }else {
            //新手模块去掉组合
            List<ShopTreasureInfo> noviceBidList = this.bidInfoService.getBidList(new String[]{Status.TBZ.toString()}, SortEnum.getOrderType("1"), InterfaceConst.ORDER_TYPE_ASC, 2, Status.S.name(), -1, -1, null, versionType == null ? 1 : versionType.getIndex());
            List<ShopTreasureInfo> enInvestNoviceList = this.bidInfoService.getBidList(statusNoTBZ, SortEnum.getOrderType("5"), InterfaceConst.ORDER_TYPE_ASC, 2, Status.S.name(), -1, -1, null, versionType == null ? 1 : versionType.getIndex());
            noviceBidAndPlanList.addAll(noviceBidList);
            noviceBidAndPlanList.addAll(enInvestNoviceList);
        }


        int versionLimit = 2;
        if(versionType.getCode().equals("CG")) {
            versionLimit = 3;
        }
        /**计划投资**/
        List<ShopTreasureInfo> monthPlanList = this.bidInfoService.getPlansList(new String[]{Status.TBZ.toString()}, null, -1, -1, SortEnum.getOrderType("2"), InterfaceConst.ORDER_TYPE_DESC, -1, -1, versionLimit, Status.F.name(), 1, "timeSort", -1, versionType == null ? 1 : versionType.getIndex());
        List<ShopTreasureInfo> shengxinPlanList = this.bidInfoService.getPlansList(new String[]{Status.TBZ.toString()}, null, -1, -1, SortEnum.getOrderType("2"), InterfaceConst.ORDER_TYPE_DESC, -1, -1, versionLimit, Status.F.name(), 2, "timeSort", -1, versionType == null ? 1 : versionType.getIndex());
        if (null != monthPlanList && monthPlanList.size() > 0) {//存在月升计划
            if (null != shengxinPlanList && shengxinPlanList.size() > 0) {//存在省心计划
                planInvestList.add(shengxinPlanList.get(0));
                planInvestList.add(monthPlanList.get(0));
            } else {//不存在可投的省心计划
                if (monthPlanList.size() >= versionLimit) {
                    planInvestList.addAll(monthPlanList);
                } else {
                    int planLimit = versionLimit - monthPlanList.size();
                    List<ShopTreasureInfo> enInvestShengXinList = this.bidInfoService.getPlansList(statusNoTBZ, null, -1, -1, SortEnum.getOrderType("5"), InterfaceConst.ORDER_TYPE_ASC, -1, -1, planLimit, Status.F.name(), 2, "timeSort", -1, versionType == null ? 1 : versionType.getIndex());
                    planInvestList.addAll(monthPlanList);
                    planInvestList.addAll(enInvestShengXinList);
                }
            }
        } else {//不存在可投月升计划
            if (null != shengxinPlanList && shengxinPlanList.size() > 0) {//存在可投省心计划
                if (plansList.size() < versionLimit) {//只存在一个可投省心计划
                    int planLimit = versionLimit - shengxinPlanList.size();
                    List<ShopTreasureInfo> enInvestMonthList = this.bidInfoService.getPlansList(statusNoTBZ, null, -1, -1, SortEnum.getOrderType("5"), InterfaceConst.ORDER_TYPE_ASC, -1, -1, planLimit, Status.F.name(), 2, "timeSort", -1, versionType == null ? 1 : versionType.getIndex());
                    planInvestList.addAll(shengxinPlanList);
                    planInvestList.addAll(enInvestMonthList);
                } else {//存在三个可投省心计划
                    planInvestList.addAll(shengxinPlanList);
                }

            } else {//不存在可投省心计划
                List<ShopTreasureInfo> enInvestShengXinList = this.bidInfoService.getPlansList(statusNoTBZ, null, -1, -1, SortEnum.getOrderType("5"), InterfaceConst.ORDER_TYPE_ASC, -1, -1, 1, Status.F.name(), 2, "timeSort", -1, versionType == null ? 1 : versionType.getIndex());
                List<ShopTreasureInfo> enInvestMonthList = this.bidInfoService.getPlansList(statusNoTBZ, null, -1, -1, SortEnum.getOrderType("5"), InterfaceConst.ORDER_TYPE_ASC, -1, -1, 3, Status.F.name(), 1, "timeSort", -1, versionType == null ? 1 : versionType.getIndex());
                if (enInvestMonthList != null && enInvestMonthList.size() > 0) {
                    planInvestList.addAll(enInvestMonthList);
                    planInvestList.add(enInvestShengXinList.get(0));
                } else {
                    planInvestList.addAll(enInvestShengXinList);
                }
            }
        }
        if ((planInvestList != null) && (planInvestList.size() > 0) && (planInvestList.size() < versionLimit)) {
            int planLimit = versionLimit - planInvestList.size();
            List<ShopTreasureInfo> enInvestShengXinList = this.bidInfoService.getPlansList(statusNoTBZ, null, -1, -1, SortEnum.getOrderType("5"), "ASC", Integer.valueOf(-1), Integer.valueOf(-1), planLimit, Status.F.name(), 2, "timeSort", -1, versionType == null ? 1 : versionType.getIndex());
            if ((enInvestShengXinList != null) && (enInvestShengXinList.size() > 0)) {
                planInvestList.addAll(enInvestShengXinList);
            }
        }

        if (planInvestList != null && planInvestList.size() >= versionLimit) {
            planInvestList = planInvestList.subList(0, versionLimit);
        }

        if(versionType.getCode().equals("CG")){
            planInvestList = new ArrayList<>();
        }
        /***散标投资***/
        int commonBidLimit = 6;
        List<ShopTreasureInfo> commonBidList = this.bidInfoService.getBidList(new String[]{Status.TBZ.toString(),Status.YFB.toString()}, SortEnum.getOrderType("5"), InterfaceConst.ORDER_TYPE_DESC, 6, Status.F.name(), -1, -1, null,versionType==null?1:versionType.getIndex());
        if (null != commonBidList && commonBidList.size() > 0) {
            if(commonBidList.size()>=6){
                bidList.addAll(commonBidList);
            }else {
              /* List<ShopTreasureInfo> enInvestBidList = this.bidInfoService.getBidList(statusNoTBZ,SortEnum.getOrderType("5"), InterfaceConst.ORDER_TYPE_DESC, 1, Status.F.name(), -1, -1, null);
                bidList.addAll(commonBidList);
                bidList.addAll(enInvestBidList);*/
                List<ShopTreasureInfo> enInvestBidList = this.bidInfoService.getBidList(statusNoTBZ,SortEnum.getOrderType("5"), InterfaceConst.ORDER_TYPE_DESC, commonBidLimit-commonBidList.size(), Status.F.name(), -1, -1, "NXFXD",versionType==null?1:versionType.getIndex());
                bidList.addAll(commonBidList);
                bidList.addAll(enInvestBidList);
            }
        }else {
            List<ShopTreasureInfo> enInvestBidList = this.bidInfoService.getBidList(statusNoTBZ,SortEnum.getOrderType("5"), InterfaceConst.ORDER_TYPE_DESC, 6, Status.F.name(), -1, -1, "NXFXD",versionType==null?1:versionType.getIndex());
            bidList.addAll(enInvestBidList);
        }
        if(noviceBidAndPlanList!=null){
            if(noviceBidAndPlanList.size()>=2){
                allList.addAll(noviceBidAndPlanList.subList(0,2));
            }else {
                allList.addAll(noviceBidAndPlanList);
            }
        }
        if(planInvestList!=null){
            if(planInvestList.size()>=3){
                allList.addAll(planInvestList.subList(0,3));
            }else {
                allList.addAll(planInvestList);
            }
        }
        allList.addAll(bidList);

        for (ShopTreasureInfo info : allList) {
            BidInfoVO vo = new BidInfoVO();
            if (info.getItemType() == 1) {
              /*  info.setStatus(planStaus(info.getId()));*/
                vo.setPlanId(info.getId());
                if(!StringUtils.isEmpty(info.getNumber())){
                    vo.setPlanTitle(info.getName()+info.getNumber());
                }else {
                    vo.setPlanTitle(info.getName());
                }
            } else {
                vo.setBidId(info.getId());
                vo.setBidTitle(info.getName());
            }

            vo.setBidType(info.getBidType());
            vo.setBidYield(String.valueOf(info.getRate()));
            vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name()) ? 1 : 0);
            vo.setLoanDays(info.getLoanDays());
            vo.setLoanMonth(info.getMonth());
            vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType()) ? info.getAssetsType().split(",") : new String[0]);
            vo.setTimestamp(DateUtil.dateToTimestampToSec(info.getPublishDate()));
            vo.setLoanAmount(info.getLoanAmount().toString());
            vo.setSurplusAmount(info.getVoteAmount().toString());
            vo.setBidClassify(Status.S.name().equals(info.getIsNoviceBid()) ? 1 : 0);
            vo.setItemType(info.getItemType());

            //判断是否定向标
            if (info.getDirectionalBid() != null) {
                vo.setBidClassify(2);
            }
            //设置抢购标参数
            if (info.getPanicBuyingTime() != null) {
                long timeLeft = (info.getPublishDate().getTime() - System.currentTimeMillis()) / 1000 + InterfaceConst.EEROR_SECOND;
                Integer countdown = Integer.valueOf(Config.get("bid.countdown.mimute")) * 60;
                vo.setTimeLeft(timeLeft > 0 ? timeLeft : 0);
                vo.setCountdown(countdown);
                vo.setPanicBuyingTime(DateUtil.dateToTimestampToSec(info.getPublishDate()));
                vo.setBidClassify(3);
            }
            vo.setRepaymentMode(info.getRepaymentMode());
            if(info.getItemType()==1){
                info.setStatus(StatusUtil.status(info.getStatus()));
                if(!info.getIsNoviceBid().equals("S")){
                    vo.setPlanCanQuit(1);
                }
            }else {
                vo.setPlanCanQuit(1);
                if(info.getIsNoviceBid().equals("S")){
                    vo.setPlanCanQuit(0);
                }
            }
            //标的状态
            if (Status.TBZ.name().equals(info.getStatus())) {
                vo.setBidStatus(1);
            }
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

            List<String> bidLabels = new ArrayList<>();
            if (Status.S.name().equals(info.getIsNoviceBid())) {
                bidLabels.add("新手专享");
            }
            if (info.getUserTotalAssets() != null||info.getAccumulatedIncome() != null||info.getUserInvestAmount() != null||info.getTargetUser() == 1) {
                bidLabels.add("大客户专享");
            }
            if (info.getAnytimeQuit() == 1) {
                bidLabels.add("随时退出");
            }
            if (info.getIsDepository() == 2) {
                bidLabels.add("银行存管");
            }
            String[] selfLabel = StringUtils.isNotEmpty(info.getBidLabel()) ? info.getBidLabel().split(",") : new String[0];
            for (int i = 0; i < selfLabel.length; i++) {
                bidLabels.add(selfLabel[i]);
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
                markList.add("已出借额达" + df.format(Double.valueOf(info.getUserInvestAmount())) + "客户专享");
            }
            if (info.getTargetUser() == 1) {
                markList.add("vip客户专享");
            }
            vo.setMarkArray(markList);
            vo.setAnytimeQuit(info.getAnytimeQuit());
            vo.setBidInterestRise(info.getBidInterestRise());
            vo.setPlanType(info.getPlanType());
            vo.setComment(info.getComment());
            vo.setLowRate(String.valueOf(info.getLowRate()));
            vo.setHighRate(String.valueOf(info.getHighRate()));
            voList.add(vo);
        }

       /* List<BidInfoVO> items = new ArrayList();//首页
        for (BidInfoVO vo : voList) {
            if (vo.getBidClassify() != 1) {
                if (vo.getPanicBuyingTime() != null && vo.getTimeLeft() > 0) {
                    items.add(0, vo); //抢购标未到点时
                } else {
                    items.add(vo);
                }
            }
        }
        for (BidInfoVO vo : voList) {
            if (vo.getBidClassify() == 1) {
                items.add(0, vo); //新手标在最前
            }
        }*/
        response.getData().put("items", voList);
        return response;
    }

    /**
     * 标的投资记录
     *
     * @param bidId 标的ID
     * @return
     */
    @RequestMapping(value = "invest/records", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse bidRecords(@ModelAttribute PageRequestForm pageRequestForm,
                            @RequestParam(required = false, value = "bidId") String bidId) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!pageRequestForm.validate() || !StringUtils.isNoneEmpty(bidId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        List<BidInvestRecordsVO> bidInvestRecordsVOList = new ArrayList<BidInvestRecordsVO>();
        String sortString = "timestamp.desc";//如果你想排序的话逗号分隔可以排序多列
        PageBounds pageBounds = new PageBounds(Integer.valueOf(pageRequestForm.getPage()), Integer.valueOf(pageRequestForm.getLimit()), Order.formString(sortString));

        List<InvestRecordsVO> investList = bidInfoService.getBidInvestRecords(bidId, pageBounds);
        Pager pager = new Pager(investList);
        for (InvestRecordsVO record : investList) {
            BidInvestRecordsVO vo = new BidInvestRecordsVO();
            UserInfo user = this.userInfoService.getUser(null, null, String.valueOf(record.getInvestId()));
            if (null == user) {
                continue;
            }
            String fullName = user.getFullName();
            if (StringUtils.isEmpty(fullName)) {
                continue;
            }
            vo.setInvestorName(fullName.substring(0, 1) + "**");
            vo.setInvestAmount(record.getPrice().doubleValue());
            vo.setInvestTime(DateUtil.dateToTimestampToSec(record.getTimestamp()));
            vo.setAutoBidFlag(record.getAutoBidFlag());
            bidInvestRecordsVOList.add(vo);
        }
        pager.setItems(bidInvestRecordsVOList);
        response.setData(CommonTool.toMap(pager));
        return response;
    }

    /**
     * 标的借款人信息
     *
     * @return
     */
    @RequestMapping(value = "borrower/info", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse borrowerInfo(@ModelAttribute BaseRequestForm paramForm,
                              @RequestParam(required = false, value = "bidId") String bidId) throws Exception {
        HttpResponse response = new HttpResponse();
        //参数验证
        if (!paramForm.validate() || StringUtils.isEmpty(bidId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }

        BidBorrowerInfo bbInfo = bidInfoService.getBidBorrowerInfo(Integer.valueOf(bidId));
        if (bbInfo == null) {
            BorrowerInfoVO borrowerInfoVO = new BorrowerInfoVO();
            BorrowerInfo bInfo = bidInfoService.getBorrowerInfo(Integer.valueOf(bidId));
            if (bInfo == null) {
                return response;
            }
            borrowerInfoVO.setInfoMsg(bInfo.getInfoMsg());
            borrowerInfoVO.setIdentify(bInfo.getIdentify());
            StringBuffer phoneBuffer = new StringBuffer(bInfo.getPhone());
            borrowerInfoVO.setPhone(phoneBuffer.replace(3, 7, "****").toString());
            if (StringUtils.isNoneBlank(bInfo.getCompany()) && bInfo.getCompany().length() >= 4) {
                StringBuffer companyBuffer = new StringBuffer(bInfo.getCompany());
                borrowerInfoVO.setCompany(companyBuffer.replace(2, bInfo.getCompany().length() - 2, "*******").toString());
            } else {
                borrowerInfoVO.setCompany("");
            }
            borrowerInfoVO.setIncome(bInfo.getIncome());
            borrowerInfoVO.setIsCarCertified("TG".equals(bInfo.getIsCarCertified()) ? 1 : 0);
            borrowerInfoVO.setIsHouseCertified("TG".equals(bInfo.getIsHouseCertified()) ? 1 : 0);
            response.setData(CommonTool.toMap(borrowerInfoVO));

        } else {
            BidBorrowerInfoVO vo = new BidBorrowerInfoVO(bbInfo);
            response.setData(CommonTool.toMap(vo));
        }
        return response;
    }

    /**
     * 证明文件
     * junda.feng 2016-07-11
     *
     * @return
     */
    @RequestMapping(value = "files/list", method = RequestMethod.GET)
    HttpResponse filesList(@ModelAttribute BaseRequestForm paramForm, String bidId) throws Exception {
        HttpResponse response = new HttpResponse();
        //参数验证
        if (!paramForm.validate() || StringUtils.isEmpty(bidId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }

        List<BidFiles> accessoryList = bidInfoService.getBidPublicAccessoryFilesList(Integer.valueOf(bidId));
        if (accessoryList == null) {
            return response;
        }
        List<BidPublicAccessoryFilesVO> voList = new ArrayList<BidPublicAccessoryFilesVO>();
        if(accessoryList != null) {
            for (BidFiles bidFiles : accessoryList) {
                BidPublicAccessoryFilesVO vo = new BidPublicAccessoryFilesVO();
                vo.setRiskId(bidFiles.getTypeId());
                vo.setTitle(bidFiles.getTypename());
                List<String> flist = bidFiles.getFileCodes();
//				String[] contractUrls = new String[flist.size()];
                String fileUrls = "";
                for (int i = 0; i < flist.size(); i++) {
                    String fileUrl = FileUtils.getPicURL(flist.get(i), Config.get("contact.url"));
//						contractUrls[i] = fileUrl;
                    fileUrls += fileUrl + ",";
                }
                if (flist.size() > 0)
                    fileUrls = fileUrls.substring(0, fileUrls.length() - 1);

//				String fileUrl = Config.get("contactInfo.url");
//				if(fileUrls!=null && fileUrls.length()>0) {
//					fileUrl += URLEncoder.encode(JSONArray.toJSONString(fileUrls), "UTF-8");
//				}
                vo.setImages(fileUrls);
                voList.add(vo);
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", voList);
        response.setData(map);
        return response;
    }

    /**
     * 判断用户能否投资定向标
     * junda.feng 2016-08-26
     *
     * @return
     */
    @RequestMapping(value = "directional/validate", method = RequestMethod.GET)
    HttpResponse directionalValidate(@ModelAttribute BaseRequestFormExtend paramForm, String bidId,String planId,VersionTypeEnum versionType) throws Exception {
        HttpResponse response = new HttpResponse();
        //参数验证
        if (!paramForm.validate() || (StringUtils.isEmpty(bidId)&&StringUtils.isEmpty(planId))||versionType == null) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }
        int productId = 0;
        //需要验证是否符合条件
        boolean canInvest = false;
        try{
            if(StringUtils.isNotEmpty(bidId)){
                productId = IntegerParser.parse(bidId);
                canInvest = preDoBidService.checkCanInvestBid(productId,paramForm.getUserId(),versionType);
            }
            if(StringUtils.isNotEmpty(planId)){
                productId = IntegerParser.parse(planId);
                canInvest = planInfoService.checkCanInvest(productId,paramForm.getUserId(),versionType);
            }

            if (!canInvest) {
                response.setCodeMessage(ResponseCode.BID_DIRECTIONAL_TOTAL_USER_ASSETS);
                return response;
            }
        }catch (BusinessException busi){
            response.setCodeMessage(busi);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        response.setData(map);
        return response;
    }

    private String planStaus(int PlanId) {
        List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(PlanId);
        String status = "";
        for (PlanBidsStatus planBidsStatus : planBidsList) {
            if (planBidsStatus.getStatus().equals("TBZ") && Double.valueOf(planBidsStatus.getSurplusAmount()) >= 100) {
                status = "TBZ";
                break;
            } else if (planBidsStatus.getStatus().equals("DFK") || (planBidsStatus.getStatus().equals("TBZ") && Double.valueOf(planBidsStatus.getSurplusAmount()) < 100)) {
                status = "DFK";
            } else if (planBidsStatus.getStatus().equals("HKZ")) {
                status = "HKZ";
                break;
            } else {
                status = "YJQ";
            }
        }
        return status;
    }

    /**
     * 计划的详情
     *
     * @param paramForm
     * @param token
     * @param userId
     * @return
     */
    @RequestMapping(value = "myPlanDetail", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse myPlanDetail(@ModelAttribute BaseRequestForm paramForm,
                                     @RequestParam(required = false, value = "token") String token,
                                     @RequestParam(required = false, value = "userId") String userId,
                                     @RequestParam(required = false, value = "planId") String planId,
                                     @RequestParam(required = false, value = "planRecordId") String planRecordId,
                                     @RequestParam(required = false, value = "newPlan") String newPlan

    ) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate() || (StringUtils.isEmpty(planRecordId))||(StringUtils.isEmpty(newPlan))) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        PlanFinacing info = null;
        double planCollectInterest = 0.00;
        BidDetailVO vo = new BidDetailVO();
        if(newPlan.equals("0")){
            info = this.finacingService.getUserPlanDetail(Integer.parseInt(userId), Integer.parseInt(planRecordId));
            if (null == info) {
                response.setCodeMessage(ResponseEnum.RESPONSE_BID_EMPTY.getCode(), ResponseEnum.RESPONSE_BID_EMPTY.getMessage());
                return response;
            }
            List<Double> collectInterestList = finacingService.getPlanCollectInterest(Integer.parseInt(planRecordId));
            if (null != collectInterestList && collectInterestList.size() > 0) {
                for (Double collectInteres : collectInterestList) {
                    planCollectInterest = planCollectInterest + collectInteres;
                }
            }
            int isPurchasedPlan = 0;
            if (!StringUtils.isBlank(userId)) {
                if (this.bidInfoService.getpurchasedPlan(Integer.valueOf(userId), Integer.valueOf(planId)) > 0) {
                    isPurchasedPlan = 1;
                }
            }
            vo.setPurchasedStatus(isPurchasedPlan);
            info.setInvestStatus(planStaus(info.getId()));
            vo.setPlanType("2");
            vo.setQuitStatus(4);
        }else {
            info = this.finacingService.getUserNewPlanDetail(Integer.parseInt(userId), Integer.parseInt(planRecordId));
            if (null == info) {
                response.setCodeMessage(ResponseEnum.RESPONSE_BID_EMPTY.getCode(), ResponseEnum.RESPONSE_BID_EMPTY.getMessage());
                return response;
            }

            int holdDays = 0;//持有天数
            long interestTime = 0;
            if (info.getInterestTime() != null) {
                interestTime = info.getInterestTime().getTime();
                if (info.getOwnsStatus() == 0 || info.getOwnsStatus() == 1) {
                    holdDays = DateUtil.daysOfTwo(info.getInterestTime(), new Date());
                } else {
                    holdDays = DateUtil.daysOfTwo(info.getInterestTime(), info.getExitTime());
                }
            }

            BigDecimal rate = new BigDecimal("0");
            if (info.getPlanType().equals("1")) {
                int month = 0;
                if (info.getOwnsStatus() == 0 || info.getOwnsStatus() == 1) {
                    if(interestTime>0) {
                        month = finacingService.getPlanMonthNum(info.getOwnsStatus(), interestTime, 0);
                    }
                    rate = new BigDecimal(info.getHighRate() + info.getBidInterest() + info.getInterestRise());
                    if (info.getLoanType().equals("d")) {
                        planCollectInterest = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(info.getInvestAmount()), rate, info.getLoanDate()).doubleValue();
                    } else {
                        planCollectInterest = CalCapitalInterestUtil.calTotalInterest(new BigDecimal(info.getInvestAmount()), rate, info.getLoanDate(), info.getRepaymentMode()).doubleValue();
                    }
                } else if (info.getOwnsStatus() == 2) {//已退出
                    month = finacingService.getPlanMonthNum(info.getOwnsStatus(), info.getInterestTime().getTime(), info.getExitTime().getTime());
                    rate = new BigDecimal(info.getLowRate() + info.getBonusRate() * month + info.getBidInterest() + info.getInterestRise());
                    planCollectInterest = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(info.getInvestAmount()), rate, holdDays).doubleValue();

                }
                vo.setPresentRate(info.getLowRate() + info.getBonusRate() * (month-1)+info.getBidInterest()+ info.getInterestRise());
                vo.setMonth(month);
                if(!StringUtils.isEmpty(info.getExpectedProfit())){
                    planCollectInterest = 0;
                }else {
                    planCollectInterest = planCollectInterest + Double.valueOf(info.getInvestAmount());
                }
            } else {
                rate = new BigDecimal(info.getYearYield() + info.getBidInterest() + info.getInterestRise());
                if (info.getOwnsStatus() == 0 || info.getOwnsStatus() == 1) {
                    if (info.getLoanType().equals("d")) {
                        planCollectInterest = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(info.getInvestAmount()), rate, info.getLoanDate()).doubleValue();
                    } else {
                        planCollectInterest = CalCapitalInterestUtil.calTotalInterest(new BigDecimal(info.getInvestAmount()), rate, info.getLoanDate(), info.getRepaymentMode()).doubleValue();
                    }
                } else {

                    planCollectInterest = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(info.getInvestAmount()), rate, holdDays).doubleValue();

                }
                if(!StringUtils.isEmpty(info.getExpectedProfit())){
                    planCollectInterest = 0;
                }else {
                    planCollectInterest = planCollectInterest + Double.valueOf(info.getInvestAmount());
                }
            }

            vo.setHoldDays(holdDays);
            int isPurchasedPlan = 0;
            if (!StringUtils.isBlank(userId)) {
                if (this.bidInfoService.getNewPurchasedPlan(Integer.valueOf(userId), Integer.valueOf(planId)) > 0) {
                    isPurchasedPlan = 1;
                }
            }
            vo.setPurchasedStatus(isPurchasedPlan);
            info.setInvestStatus(StatusUtil.status(info.getInvestStatus()));
            if(Status.YJQ.name().equals(info.getInvestStatus())){
                vo.setQuitStatus(0);
                vo.setExpireTime(DateUtil.dateToTimestampToSec(info.getExpireTime()));
            }else if(info.getOwnsStatus() == 1){//申请退出
                vo.setQuitStatus(1);
                vo.setApplyTime(DateUtil.dateToTimestampToSec(info.getApplyTime()));
            }else if (info.getOwnsStatus() == 2&&!Status.YJQ.name().equals(info.getInvestStatus())){//提前退出成功
                vo.setQuitStatus(2);
                vo.setExitTime(DateUtil.dateToTimestampToSec(info.getExitTime()));
            }else {
                vo.setQuitStatus(4);
            }

        }

        DecimalFormat df = new DecimalFormat("######0.00");


        vo.setPlanId(info.getId());
        vo.setPlanTitle(info.getBidTitle());
        vo.setCollectInteres(df.format(planCollectInterest));
        vo.setBidYield(String.valueOf(info.getYearYield()));
        vo.setRepaymentMode(info.getRepaymentMode());
        vo.setLoanDays(info.getLoanDate());
        vo.setLoanMonth(0);
        vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name()) ? 1 : 0);
        vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType()) ? info.getAssetsType().split(",") : new String[0]);
        vo.setRemark(info.getRemark());
        /*vo.setBorrowerUrl(Config.get("bid.borrower.url") + bidId);*/
        vo.setLoanAmount(info.getLoanAmount());
        vo.setSurplusAmount(info.getVoteAmount());
      /*  int isPurchased = 0;
        if(this.bidInfoService.getpurchasedPlan(Integer.valueOf(userId),Integer.valueOf(planId))>0){
            isPurchased =1;
        }
        vo.setPurchasedStatus(isPurchased);*/
        if (info.getBidReviewedTime() != null) {
            long curTime = DateUtil.dateToTimestampToSec(new Date());
            if (info.getBidFullTime() != null) {
                curTime = DateUtil.dateToTimestampToSec(info.getBidFullTime());
            }
            vo.setDiffBidtimes(curTime - DateUtil.dateToTimestampToSec(info.getBidReviewedTime()));
        }
        /*vo.setTotalInvestPers(info.getTotalInvestPers())*///投资人数

        //标的状态
        if (Status.TBZ.name().equals(info.getInvestStatus())) {
            vo.setBidStatus(1);
        }
        if (Status.DFK.name().equals(info.getInvestStatus())||(Status.TBZ.name().equals(info.getInvestStatus())&&Double.valueOf(info.getVoteAmount())<100)) {
            vo.setBidStatus(2);
        }
        if (Status.HKZ.name().equals(info.getInvestStatus())) {
            vo.setBidStatus(4);
            vo.setExpireTime(DateUtil.dateToTimestampToSec(info.getExpireTime()));
        }
        if (Status.YJQ.name().equals(info.getInvestStatus())) {
            vo.setBidStatus(5);
        }
       /* int isPurchasedPlan = 0;
        if(!StringUtils.isEmpty(userId)){
            if(this.bidInfoService.getpurchasedPlan(Integer.valueOf(userId),Integer.valueOf(planId))>0){
                isPurchasedPlan =1 ;
            }
        }*/
        /*vo.setPurchasedStatus(isPurchasedPlan);*/


        //服务协议
        // String fwxy = bidInfoService.getGroupItemValue(Integer.parseInt(bidId), InterfaceConst.BID_EXTEND_ASSIGNMENT_CODE);
        vo.setFwxyUrl(userInfoService.getUserServiceAgreementUrl(StringUtils.isBlank(userId) ? "-1" : userId));
        vo.setLawFiles(null);
        vo.setLawFileUrl(null);
        //组信息
        List<BidExtendGroupVO> groupInfoList = new ArrayList<>();
        vo.setGroupInfoList(groupInfoList);

        vo.setBidInterestRise(info.getBidInterest()+info.getInterestRise());
        vo.setInterestRise(info.getInterestRise());
        vo.setItemType(1);
        vo.setTotalInvestPers(info.getTotalInvestPers());
        vo.setBidNum(info.getBidNum());
        List<String> bidLabels = new ArrayList<>();
        if (Status.S.name().equals(info.getIsNoviceBid())) {
            bidLabels.add("新手专享");
        }
        if(info.getUserTotalAssets() != null||info.getAccumulatedIncome() != null||info.getUserInvestAmount() != null||info.getTargetUser() == 1){
            bidLabels.add("大客户专享");
        }
        if (info.getAnytimeQuit() == 1) {
            bidLabels.add("随时退出");
        }
        if (info.getIsDepository() == 2) {
            bidLabels.add("银行存管");
        }
        String[] selfLabel = StringUtils.isNotEmpty(info.getBidLabel())?info.getBidLabel().split(","):new String[0];
        for (int i = 0; i < selfLabel.length; i++) {
            bidLabels.add(selfLabel[i]);
        }

        if (bidLabels != null && bidLabels.size() > 0) {
            bidLabels = bidLabels.subList(0, bidLabels.size() >= 2 ? 2 : bidLabels.size());
            vo.setBidLabel(bidLabels);
        } else {
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
            markList.add("已出借额达" + df.format(Double.valueOf(info.getUserInvestAmount())) + "客户专享");
        }
        if (info.getTargetUser() == 1) {
            markList.add("vip客户专享");
        }
        vo.setMarkArray(markList);
        if(newPlan.equals("1")) {
            if (info.getPlanType().equals("1")) {
                long naturaDate = 0;
                if (info.getYearYield() < info.getHighRate() && info.getInterestTime() != null) {
                    for (int i = 1; i < 13; i++) {
                        try {
                            naturaDate = DateUtil.rollNaturalMonth(info.getInterestTime().getTime(), i);//起息时间经自然月推算到当前月经过了i个月得到准确日期
                        } catch (Exception e) {

                        }
                        if (new Date().getTime() < naturaDate) {
                            break;
                        }
                    }
                }
                vo.setNextRaiseDate(naturaDate);
            }
            vo.setLowRate(String.valueOf(info.getLowRate()));
            vo.setHighRate(String.valueOf(info.getHighRate()));
            vo.setBonusRate(String.valueOf(info.getBonusRate()));
            vo.setPlanType(info.getPlanType());
        }
        vo.setComment(info.getComment());
        vo.setNewPlan(Integer.valueOf(newPlan));
        vo.setProjectInfo(ProjectInfoToJson(this.bidInfoService.getPlanDetail(Integer.valueOf(planId)),null));
        vo.setBorrowerInfo(new LinkedHashMap<String, Object>());
        response.setData(CommonTool.toMap(vo));
        return response;
    }

    /**
     * 计划的详情3.1.0版本适用
     *
     * @param paramForm
     * @param token
     * @param userId
     * @param bidId
     * @return
     */
    @RequestMapping(value = "planDetail", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse getPlanDetail(@ModelAttribute BaseRequestForm paramForm,
                                      @RequestParam(required = false, value = "token") String token,
                                      @RequestParam(required = false, value = "userId") String userId,
                                      @RequestParam(required = false, value = "bidId") String bidId,
                                      @RequestParam(required = false, value = "planId") String planId,
                                      @RequestParam(required = false, value = "creditId") String creditId) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate() || (StringUtils.isEmpty(bidId) && StringUtils.isEmpty(planId))) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        ShopTreasureInfo info = null;
        if (!StringUtils.isEmpty(planId)) {
            info = this.bidInfoService.getPlanInfo(Integer.parseInt(planId));
        }
        if (null == info) {
            response.setCodeMessage(ResponseEnum.RESPONSE_BID_EMPTY.getCode(), ResponseEnum.RESPONSE_BID_EMPTY.getMessage());
            return response;
        }
        double bidInterestRise = info.getBidInterestRise();
        if(!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(creditId)) {
            Integer bidRecord = transferInService.getBidRecordId(Integer.valueOf(creditId));
            List<Integer> tenderIdList = new ArrayList<Integer>();
            tenderIdList.add(bidRecord);
            Map<String, Object> tenderMap = new HashMap<>();
            tenderMap.put("list", tenderIdList);
            List<UserCoupons> userCouponsList = new ArrayList<UserCoupons>();
            if (!tenderIdList.isEmpty()) {
                userCouponsList = transferInService.getUserCoupons(tenderMap);
            }

            if (!userCouponsList.isEmpty()) {
                for (UserCoupons userCoupons : userCouponsList) {
                    if (bidRecord == userCoupons.getTenderId()) {
                        bidInterestRise = bidInterestRise + userCoupons.getScope();
                    }
                }
            }
        }

        BidDetailVO vo = new BidDetailVO();
        info.setStatus(planStaus(info.getId()));
        vo.setPlanId(info.getId());
        vo.setPlanTitle(info.getName());
        vo.setBidYield(String.valueOf(info.getRate()));
        vo.setRepaymentMode(info.getRepaymentMode());
        vo.setLoanDays(info.getLoanDays());
        vo.setLoanMonth(info.getMonth());
        vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name()) ? 1 : 0);

        //判断是否定向标
        vo.setBidClassify(Status.S.name().equals(info.getIsNoviceBid()) ? 1 : 0);
        if (info.getDirectionalBid() != null) {
            vo.setBidClassify(2);
//			DecimalFormat dft = new DecimalFormat("#,##0");
            vo.setUserTotalAssets(info.getUserTotalAssets().toPlainString());
        }
        vo.setBidType(info.getBidType());
        vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType()) ? info.getAssetsType().split(",") : new String[0]);
        vo.setRemark(info.getBorrowingDescribe());
        vo.setBorrowerUrl(Config.get("bid.borrower.url") + bidId);
        vo.setLoanAmount(info.getLoanAmount().toString());
        vo.setSurplusAmount(info.getVoteAmount().toString());
        if (info.getBidReviewedTime() != null) {
            long curTime = DateUtil.dateToTimestampToSec(new Date());
            if (info.getBidFullTime() != null) {
                curTime = DateUtil.dateToTimestampToSec(info.getBidFullTime());
            }
            vo.setDiffBidtimes(curTime - DateUtil.dateToTimestampToSec(info.getBidReviewedTime()));
        }
        vo.setTotalInvestPers(info.getTotalInvestPers());

        //标的状态
        if (Status.TBZ.name().equals(info.getStatus())) {
            vo.setBidStatus(1);
        }
        if (Status.DFK.name().equals(info.getStatus())) {
            vo.setBidStatus(2);
        }
        if (Status.HKZ.name().equals(info.getStatus())) {
            vo.setBidStatus(4);
        }
        if (Status.YJQ.name().equals(info.getStatus())) {
            vo.setBidStatus(5);
        }
        int isPurchasedPlan = 0;
        if (!StringUtils.isEmpty(userId)) {
            if (this.bidInfoService.getpurchasedPlan(Integer.valueOf(userId), Integer.valueOf(planId)) > 0) {
                isPurchasedPlan = 1;
            }
        }
        vo.setPurchasedStatus(isPurchasedPlan);

        //服务协议
        // String fwxy = bidInfoService.getGroupItemValue(Integer.parseInt(bidId), InterfaceConst.BID_EXTEND_ASSIGNMENT_CODE);
        vo.setFwxyUrl(userInfoService.getUserServiceAgreementUrl(StringUtils.isBlank(userId) ? "-1" : userId));
        vo.setLawFiles(null);
        vo.setLawFileUrl(null);
        //组信息
        List<BidExtendGroupVO> groupInfoList = new ArrayList<>();
        vo.setGroupInfoList(groupInfoList);

        vo.setAnytimeQuit(info.getAnytimeQuit());
        vo.setBidInterestRise(info.getBidInterestRise());
        vo.setItemType(1);
        List<String> bidLabels = new ArrayList<>();
        if (Status.S.name().equals(info.getIsNoviceBid())) {
            bidLabels.add("新手专享");
        }
        if (info.getUserTotalAssets() != null||info.getAccumulatedIncome() != null||info.getUserInvestAmount() != null||info.getTargetUser() == 1) {
            bidLabels.add("大客户专享");
        }
        if (info.getAnytimeQuit() == 1) {
            bidLabels.add("随时退出");
        }
        if (info.getIsDepository() == 2) {
            bidLabels.add("银行存管");
        }
        String[] selfLabel = StringUtils.isNotEmpty(info.getBidLabel()) ? info.getBidLabel().split(",") : new String[0];
        for (int i = 0; i < selfLabel.length; i++) {
            bidLabels.add(selfLabel[i]);
        }

        if (bidLabels != null && bidLabels.size() > 0) {
            bidLabels = bidLabels.subList(0, bidLabels.size() >= 2 ? 2 : bidLabels.size());
            vo.setBidLabel(bidLabels);
        } else {
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
            markList.add("已出借额达" + df.format(Double.valueOf(info.getUserInvestAmount())) + "客户专享");
        }
        if (info.getTargetUser() == 1) {
            markList.add("vip客户专享");
        }
        vo.setMarkArray(markList);
        vo.setPlanType("2");
        vo.setNewPlan(0);
        vo.setProjectInfo(ProjectInfoToJson(info,null));
        vo.setBorrowerInfo(new LinkedHashMap<String, Object>());
        response.setData(CommonTool.toMap(vo));
        return response;
    }

    private LinkedHashMap ProjectInfoToJson(ShopTreasureInfo info,String fwUrl){
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setName(info.getName());
        projectInfo.setLoanAmount(info.getLoanAmount());
        projectInfo.setRate(info.getRate());
        if(!StringUtils.isEmpty(info.getGuaranteeMeasure())){
            projectInfo.setGuarantee(GuaranteeMeasureEunm.get(info.getGuaranteeMeasure()));
        }
        projectInfo.setInterestDate("放款起息");
        projectInfo.setInterestType("按天计息");
        projectInfo.setRepaymentMode(RepaymentModeEnum.get(info.getRepaymentMode()));
        projectInfo.setFwUrl(fwUrl);
        if(info.getAnytimeQuit()==1){
            projectInfo.setIsEarlyExit("支持");
        }else {
            projectInfo.setIsEarlyExit("不支持");
        }

        projectInfo.setLoanPurpose(info.getLoanUsage());
        if(info.getLoanDays()==0){
            projectInfo.setLoanTerm(String.valueOf(info.getMonth()).concat("个月"));
        }else{
            projectInfo.setLoanTerm(String.valueOf(info.getLoanDays()).concat("天"));
        }
        projectInfo.setAssetsType(info.getAssetsType());
        projectInfo.setRepaymentOrigin(info.getRepaymentOrigin());
        String str = Jackson.getBaseJsonData(projectInfo);
        Gson gson = new Gson();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map = gson.fromJson(str, map.getClass());
        return map;
    }

    private LinkedHashMap BorrowerInfoToJson(BorrowerDetail detail){
        if(!StringUtils.isBlank(detail.getAddress())){
            /*int replaceIndex = detail.getAddress().indexOf("市");*/
            StringBuffer buffer = new StringBuffer(detail.getAddress());
            String address = null;
            if(detail.getAddress().length()>=5) {
                address  = buffer.replace(5, detail.getAddress().length(), "*****").toString();
            }else {
                address  = buffer.append("*************").substring(0,10);
            }
            detail.setAddress(address);
        }
        detail.setSubjectType(LoanUserEnum.get(Integer.valueOf(detail.getSubjectType())));

        if(detail.getBorrowerType()!=null&&detail.getBorrowerType().equals("FZRR")&&detail.getEnterpriseCode()!=null&&!StringUtils.isEmpty(detail.getEnterpriseName())){
            detail.setIdCard( detail.getEnterpriseCode().substring(0, 2)+"************" + detail.getEnterpriseCode().substring(detail.getEnterpriseCode().length()-4,detail.getEnterpriseCode().length()));
            detail.setName(detail.getEnterpriseName().substring(0, 2)+"*******" + detail.getEnterpriseName().substring(detail.getEnterpriseName().length()-1,detail.getEnterpriseName().length()));
        }
        String str = Jackson.getBaseJsonData(detail);
        Gson gson = new Gson();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map = gson.fromJson(str, map.getClass());
        return map;
    }

    private int showAfterLoanInfo(int loanDay , int loanMonth ,Date interestDate) {
        if(interestDate!=null&&((loanDay!=0 && loanDay<180)||(loanMonth!=0 && loanMonth<6))){
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(interestDate);
            startCalendar.add(Calendar.MONTH, +1);
            startCalendar.set(Calendar.DAY_OF_MONTH, 1);
            startCalendar.set(Calendar.HOUR_OF_DAY, 0);
            startCalendar.set(Calendar.SECOND,0);
            startCalendar.set(Calendar.MINUTE,0);
            if(new Date().getTime()>startCalendar.getTime().getTime()){
                return 1;
            }else {
                return 0;
            }
        }
        else if(interestDate!=null&&((loanDay!=0 && loanDay>=180)||(loanMonth!=0 && loanMonth>=6))){
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(interestDate);
            startCalendar.set(Calendar.MONTH, ((int) startCalendar.get(Calendar.MONTH) / 3 + 1) * 3);
            startCalendar.set(Calendar.DAY_OF_MONTH, 1);
            startCalendar.set(Calendar.HOUR_OF_DAY, 0);
            startCalendar.set(Calendar.SECOND,0);
            startCalendar.set(Calendar.MINUTE,0);
            Date a = startCalendar.getTime();
            if(new Date().getTime()>startCalendar.getTime().getTime()){
                return 1;
            }else {
                return 0;
            }
        }
        return 0;
    }


    /**
     * 企业公章图片
     *
     * @return
     */
    @RequestMapping(value = "files/enterpriseImage", method = RequestMethod.GET)
    HttpResponse enterpriseImage(@ModelAttribute BaseRequestForm paramForm, String bidId) throws Exception {
        HttpResponse response = new HttpResponse();
        //参数验证
        if (!paramForm.validate() || StringUtils.isEmpty(bidId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }
        String fileUrl = null;
        //已经放款的拿脱敏文件
        try{
            fileUrl =  thirdpartyService.getFileUrl(Integer.valueOf(bidId));
        }catch (Exception e){
            e.printStackTrace();
            response.setCodeMessage(ResponseCode.FAILURE);
            return  response;
        }

        if(StringUtils.isEmpty(fileUrl)) {
            String enterpriseCode = null;
            try {
                enterpriseCode = bidInfoService.getenterpriseSealCode(Integer.valueOf(bidId));
            } catch (Exception e) {
                response.setCodeMessage(ResponseCode.FAILURE);
                return response;
            }
            response.setCodeMessage(ResponseCode.SUCCESS);

            if (StringUtils.isEmpty(enterpriseCode)) {
                return response;
            }
            fileUrl = FileUtils.getFileURL(enterpriseCode, Config.get("resources.server.path"));
        }
        /*String fileUrl = FileUtils.getPicURL(flist.get(i), Config.get("contact.url"));
        List<BidPublicAccessoryFilesVO> voList = new ArrayList<BidPublicAccessoryFilesVO>();
        if(accessoryList != null) {
            for (BidFiles bidFiles : accessoryList) {
                BidPublicAccessoryFilesVO vo = new BidPublicAccessoryFilesVO();
                vo.setRiskId(bidFiles.getTypeId());
                vo.setTitle(bidFiles.getTypename());
                List<String> flist = bidFiles.getFileCodes();
//				String[] contractUrls = new String[flist.size()];
                String fileUrls = "";
                for (int i = 0; i < flist.size(); i++) {
                    String fileUrl = FileUtils.getPicURL(flist.get(i), Config.get("contact.url"));
//						contractUrls[i] = fileUrl;
                    fileUrls += fileUrl + ",";
                }
                if (flist.size() > 0)
                    fileUrls = fileUrls.substring(0, fileUrls.length() - 1);

//				String fileUrl = Config.get("contactInfo.url");
//				if(fileUrls!=null && fileUrls.length()>0) {
//					fileUrl += URLEncoder.encode(JSONArray.toJSONString(fileUrls), "UTF-8");
//				}
                vo.setImages(fileUrls);
                voList.add(vo);
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", voList);
        response.setData(map);*/
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fileUrl", fileUrl);
        response.setData(map);
        return response;
    }
}
