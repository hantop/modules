package com.fenlibao.p2p.controller.v_1.v_1_0_0.finacing;

import com.fenlibao.p2p.common.util.repayment.CalCapitalInterestUtil;
import com.fenlibao.p2p.model.entity.bid.BidExtendInfo;
import com.fenlibao.p2p.model.entity.finacing.CreditInfo;
import com.fenlibao.p2p.model.entity.finacing.InvestInfo;
import com.fenlibao.p2p.model.entity.finacing.InvestInfoDetail;
import com.fenlibao.p2p.model.entity.finacing.RepaymentInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.fiancing.InvestInfoDetailVO;
import com.fenlibao.p2p.model.vo.fiancing.InvestInfoVO;
import com.fenlibao.p2p.model.vo.fiancing.RepaymentInfoVO;
import com.fenlibao.p2p.model.vo.fiancing.RepaymentItemVO;
import com.fenlibao.p2p.service.FinacingService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.StatusUtil;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.paginator.domain.Order;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import com.fenlibao.p2p.util.paginator.domain.Pager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用户投资相关接口
 */
@RestController("v_1_0_0/FinacingController")
@RequestMapping("finacing")
public class FinacingController {

	private static final Logger logger = LogManager.getLogger(FinacingController.class);

	@Resource
	private FinacingService finacingService;

	@Resource
	private BidInfoService bidInfoService;

	@Resource
	private ITradeService tradeService;

	/**
	 * 用户投资列表
	 * @param pageRequestForm
	 * @param userId
	 * @param token
	 * @param bidType
	 * @param bidStatus
	 * @return
     * @throws Exception
     */
	@RequestMapping(value = "list", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
	public HttpResponse getUserInvestRecordList(@ModelAttribute PageRequestForm pageRequestForm,
												@RequestParam(required = false, value = "userId") String userId,
												@RequestParam(required = false, value = "token") String token,
												@RequestParam(required = false, value = "bidType") String bidType,
												@RequestParam(required = false, value = "bidStatus") String bidStatus,
												@RequestParam(required = false, value = "versionType") VersionTypeEnum versionType) {
		HttpResponse response = new HttpResponse();

		if (!pageRequestForm.validate()||versionType == null) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}
		String[] bidStatusStr = new String[]{Status.TBZ.name(),Status.DFK.name(),Status.HKZ.name(), Status.YJQ.name()};
		if(StringUtils.isNotEmpty(bidStatus) && !bidStatus.endsWith(",")){
			bidStatusStr = bidStatus.trim().split(",");
		}
		String sortString = "purchaseTime.desc";//如果你想排序的话逗号分隔可以排序多列
		PageBounds pageBounds = new PageBounds(Integer.valueOf(pageRequestForm.getPage()), Integer.valueOf(pageRequestForm.getLimit()) , Order.formString(sortString));

		List<InvestInfo> investInfoList = null;
		try {
			investInfoList = finacingService.getUserInvestList(Integer.valueOf(userId), bidType, bidStatusStr, pageBounds,versionType==null?1:versionType.getIndex());
			Pager pager = new Pager(investInfoList);

			List<InvestInfoVO> investInfoVOList = new ArrayList<>();
			for (InvestInfo investInfo : investInfoList) {
				InvestInfoVO investInfoVO = new InvestInfoVO();
				investInfoVO.setBidId(investInfo.getBidId());
				investInfoVO.setBidTitle(investInfo.getZrId()==0?investInfo.getBidTitle():InterfaceConst.CREDIT_NAME_PREFIX+investInfo.getBidTitle());
				investInfoVO.setCreditFlag(investInfo.getZrId()!=0);
				investInfoVO.setIsNoviceBid("S".equals(investInfo.getIsNoviceBid())?1:0);
				investInfoVO.setCreditId(investInfo.getCreditId());
				investInfoVO.setInvestAmount(investInfo.getInvestAmount());
				investInfoVO.setExpectedProfit(investInfo.getExpectedProfit());
				investInfoVO.setRasiedExpectedProfit(investInfo.getRasiedExpectedProfit());
				investInfoVO.setPurchaseTime(DateUtil.dateToTimestampToSec(investInfo.getPurchaseTime()));
				investInfoVO.setExpireDate(DateUtil.dateToTimestampToSec(investInfo.getExpireDate()));

				investInfoVO.setLoanMonths(investInfo.getLoanMonths());
				investInfoVO.setLoanDays(investInfo.getLoanDays());
				investInfoVO.setYearYield(investInfo.getYearYield());
				investInfoVO.setTotalRepaymentAmount(investInfo.getTotalRepaymentAmount().toString());

				investInfoVO.setJxFlag(investInfo.getJxFlag());
				investInfoVO.setIsDepository(investInfo.getIsDepository());

				investInfoVO.setItemType(investInfo.getItemType());
				investInfoVO.setBidInterestRise(investInfo.getBidInterestRise());
				investInfoVO.setAnytimeQuit(investInfo.getAnytimeQuit());
				investInfoVO.setPlanTitle(investInfo.getPlanTitle());
				investInfoVO.setPlanId(investInfo.getPlanId());
				investInfoVO.setVoteAmount(investInfo.getSurplusAmount());
				// 标的状态
				// 投标中或待放款
				if (Status.TBZ.name().equals(investInfo.getBidStatus()) || Status.DFK.name().equals(investInfo.getBidStatus())) {
					investInfoVO.setInvestStatus(1);
					// 当未放款时，手动去计算此投资的预期收益
					if (investInfo.getLoanMonths() == 0 && investInfo.getLoanDays() > 0) {
						BigDecimal earnings1 = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY( investInfo.getInvestAmount(), new BigDecimal(investInfo.getYearYield()), investInfo.getLoanDays());
						investInfoVO.setExpectedProfit(earnings1);
						if(investInfo.getBidInterestRise().compareTo(BigDecimal.ZERO) != 0){
							BigDecimal earnings2 = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY( investInfo.getInvestAmount(), investInfo.getBidInterestRise(), investInfo.getLoanDays());
							investInfoVO.setRasiedExpectedProfit(earnings2);
						}
					}
					if (investInfo.getLoanMonths() > 0 && investInfo.getLoanDays() == 0) {
						BigDecimal earnings1 = CalCapitalInterestUtil.calTotalInterest(investInfo.getInvestAmount(), new BigDecimal(investInfo.getYearYield()), investInfo.getLoanMonths(), investInfo.getRepaymentMode());
						investInfoVO.setExpectedProfit(earnings1);
						if(investInfo.getBidInterestRise().compareTo(BigDecimal.ZERO) != 0){
							BigDecimal earnings2 = CalCapitalInterestUtil.calTotalInterest(investInfo.getInvestAmount(), investInfo.getBidInterestRise(), investInfo.getLoanMonths(), investInfo.getRepaymentMode());
							investInfoVO.setRasiedExpectedProfit(earnings2);
						}
					}
				}
				// 还款中
				else if (Status.HKZ.name().equals(investInfo.getBidStatus())) {
					// 转让中
					if ("S".equals(investInfo.getIsTransfer())) {
						investInfoVO.setInvestStatus(5);
					}
					// 逾期
					else if (!"F".equals(investInfo.getIsYq())) {
						investInfoVO.setInvestStatus(4);
					} else {
						investInfoVO.setInvestStatus(2);
					}
				}
				// 已结清
				else if (Status.YJQ.name().equals(investInfo.getBidStatus())) {
					investInfoVO.setInvestStatus(3);
				}
				investInfoVO.setBidStatus(investInfo.getBidStatus());
				investInfoVOList.add(investInfoVO);
			}
			pager.setItems(investInfoVOList);
			response.setData(CommonTool.toMap(pager));
		} catch (Exception e) {
			logger.error("[FinacingController.getUserInvestRecordList]", e);
			response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
		}
		return response;
	}

	/**
	 * 用户投资债权详情
	 *
	 * @param paramForm
	 * @param token
	 * @param userId
	 * @param creditId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "detail", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
	HttpResponse getUserFinacingDetail(@ModelAttribute BaseRequestForm paramForm,
									   @RequestParam(required = false, value = "token") String token,
									   @RequestParam(required = false, value = "userId") String userId,
									   @RequestParam(required = false, value = "creditId") String creditId
	) throws Exception {
		HttpResponse response = new HttpResponse();

		if (!paramForm.validate() || StringUtils.isEmpty(userId) || StringUtils.isEmpty(creditId)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}
		InvestInfoDetail infoDetail = finacingService.getUserFinacingDetail(userId, creditId);
		if (infoDetail == null) {
			return response;
		}

		InvestInfoDetailVO investInfoDetailVO = new InvestInfoDetailVO();
		investInfoDetailVO.setBidId(infoDetail.getBidId());
		investInfoDetailVO.setBidTitle(infoDetail.getBidTitle());
		investInfoDetailVO.setIsNoviceBid("S".equals(infoDetail.getIsNoviceBid())?1:0);
		investInfoDetailVO.setCreditId(infoDetail.getCreditId());
		investInfoDetailVO.setInvestAmount(infoDetail.getInvestAmount());
		investInfoDetailVO.setExpectedProfit(infoDetail.getExpectedProfit());
		investInfoDetailVO.setPurchaseTime(DateUtil.dateToTimestampToSec(infoDetail.getPurchaseTime()));
		investInfoDetailVO.setLoanMonths(infoDetail.getLoanMonths());
		investInfoDetailVO.setLoanDays(infoDetail.getLoanDays());
		investInfoDetailVO.setYearYield(infoDetail.getYearYield());

		investInfoDetailVO.setInvestTime(DateUtil.dateToTimestampToSec(infoDetail.getInvestTime()));

		investInfoDetailVO.setInterestTime(DateUtil.dateToTimestampToSec(infoDetail.getInterestTime()));
		investInfoDetailVO.setExpireTime(DateUtil.dateToTimestampToSec(infoDetail.getExpireTime()));
		if(infoDetail.getInterestTime() == null || infoDetail.getExpireTime() == null){
			CreditInfo creditInfo = finacingService.getUserCreditInfo(Integer.valueOf(creditId));
			if(creditInfo==null){
				return response;
			}
			int bidId = creditInfo.getBidId();
			BidExtendInfo bidExtendInfo = bidInfoService.getBidExtendInfo(bidId);
			if(bidExtendInfo==null){
				return response;
			}
			investInfoDetailVO.setInterestTime(DateUtil.dateToTimestampToSec(creditInfo.getInterestDate()));
			investInfoDetailVO.setExpireTime(DateUtil.dateToTimestampToSec(bidExtendInfo.getNextRepaymentDate()));
		}

		investInfoDetailVO.setLawFiles(bidInfoService.getBidPublicAccessoryFiles(infoDetail.getBidId()));
		investInfoDetailVO.setLawFileUrl(bidInfoService.getBidPublicAccessoryFileUrl(infoDetail.getBidId()));
		investInfoDetailVO.setRemark(infoDetail.getRemark());
		investInfoDetailVO.setGroupInfoList(bidInfoService.getGroupInfoList(infoDetail.getBidId()));
		investInfoDetailVO.setBorrowerUrl(Config.get("bid.borrower.url") + infoDetail.getBidId());

		//标的状态
		if (Status.TBZ.name().equals(infoDetail.getBidStatus()) || Status.DFK.name().equals(infoDetail.getBidStatus())) {
			investInfoDetailVO.setInvestStatus(1);
		}
		if (Status.HKZ.name().equals(infoDetail.getBidStatus())) {
			investInfoDetailVO.setInvestStatus(2);
		}
		if (Status.YJQ.name().equals(infoDetail.getBidStatus())) {
			investInfoDetailVO.setInvestStatus(6);
		}
		if ("S".equals(infoDetail.getIsTransfer())) {
			investInfoDetailVO.setInvestStatus(3);
		}
		if (!"F".equals(infoDetail.getIsYq())) {
			investInfoDetailVO.setInvestStatus(5);
		}
		if (infoDetail.getTransferOrderId() > 0) {
			investInfoDetailVO.setExpectedProfit(tradeService.getArrivalEarningsByCreditId(infoDetail.getCreditId()));
			investInfoDetailVO.setInvestStatus(4);
		}
		response.setData(CommonTool.toMap(investInfoDetailVO));
		return response;
	}

	/**
	 * 用户投资债权的还款计划
	 * @param paramForm
	 * @param token
	 * @param userId
	 * @param creditId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "repayment/list", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
	HttpResponse getUserFinacingRepaymentList(@ModelAttribute BaseRequestForm paramForm,
			String token,String userId,String creditId) throws Exception {
		HttpResponse response = new HttpResponse();

		if (!paramForm.validate() || StringUtils.isEmpty(userId) || StringUtils.isEmpty(creditId)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}
		CreditInfo creditInfo = finacingService.getUserCreditInfo( Integer.valueOf(creditId));
		if(creditInfo==null){
			return response;
		}
		int bidId = creditInfo.getBidId();
		BigDecimal collectInterest = finacingService.getUserCollectInterest(Integer.valueOf(userId), Integer.valueOf(creditId));
		BidExtendInfo bidAllInfo = bidInfoService.getBidAllInfo(bidId);
		if(bidAllInfo==null){
			return response;
		}
		RepaymentInfoVO repaymentInfoVO = new RepaymentInfoVO();
		repaymentInfoVO.setCollectInterest(collectInterest.toString());
		repaymentInfoVO.setLeftPeriod(bidAllInfo.getLeftRepaymentPeriod());
		repaymentInfoVO.setRepaymentMode(bidAllInfo.getRepaymentMode());
		repaymentInfoVO.setNextRepaymentDate(DateUtil.dateToTimestampToSec(bidAllInfo.getNextRepaymentDate()));
		repaymentInfoVO.setExpireRepaymentDate(DateUtil.dateToTimestampToSec(bidAllInfo.getExpireDate()));
		repaymentInfoVO.setTotalRepaymentStatus(bidAllInfo.getBidStatus());
		//兼容数据 如果到期时间字段为空则到期时间为下次还款日
		//create by laubrence 2016-3-29 15:44:36
		if(bidAllInfo.getExpireDate() == null){
			repaymentInfoVO.setExpireRepaymentDate(DateUtil.dateToTimestampToSec(bidAllInfo.getNextRepaymentDate()));
		}

		List<RepaymentItemVO> repaymentItemVOs = new ArrayList<RepaymentItemVO>();
		List<RepaymentInfo> repaymentInfos = finacingService.getUserRepaymentItem(Integer.valueOf(userId), Integer.valueOf(creditId));

		//合并 2016-09-23
		for(int i=0; i<repaymentInfos.size(); i++){
			RepaymentInfo repaymentInfo = repaymentInfos.get(i);
			if(repaymentInfo.getTradeType() == 7004 || repaymentInfo.getTradeType() == 7005){
				//将7004和7005的金额合并
				repaymentInfo.setTradeTypeName("其他");
				for(int j=i+1; j<repaymentInfos.size(); j++){
					RepaymentInfo riftemp = repaymentInfos.get(j);
					boolean tradeTypeFlag = (riftemp.getTradeType() == 7004 || riftemp.getTradeType() == 7005);
					boolean periodFlag = (repaymentInfo.getPeriod() == riftemp.getPeriod());
					if(tradeTypeFlag && periodFlag){
						long timeInte = repaymentInfo.getExpectedRepaymentDate().getTime() - riftemp.getExpectedRepaymentDate().getTime();
						if(timeInte>0){
							repaymentInfo.setRepaymentAmount(repaymentInfo.getRepaymentAmount().add(riftemp.getRepaymentAmount()));//合并金额
							repaymentInfo.setRepaymentStatus(checkRepaymentStatus(repaymentInfo.getRepaymentStatus(),riftemp.getRepaymentStatus()));//合并还款状态
							repaymentInfos.remove(j);
							j--;
						}else{
							riftemp.setRepaymentAmount(repaymentInfo.getRepaymentAmount().add(riftemp.getRepaymentAmount()));
							riftemp.setRepaymentStatus(checkRepaymentStatus(riftemp.getRepaymentStatus(),repaymentInfo.getRepaymentStatus()));
							riftemp.setTradeTypeName("其他");
							repaymentInfos.remove(i);
							i--;
							break;
						}
					}
				}
			}
			//先将7002和7023合并
			if(repaymentInfo.getTradeType() == 7002 || repaymentInfo.getTradeType() == 7023){
				for(int j=i+1; j<repaymentInfos.size(); j++){
					RepaymentInfo riftemp = repaymentInfos.get(j);
					boolean tradeTypeFlag = (riftemp.getTradeType() == 7002 || riftemp.getTradeType() == 7023);
					boolean periodFlag = (repaymentInfo.getPeriod() == riftemp.getPeriod());
					if(tradeTypeFlag && periodFlag){
						if(repaymentInfo.getTradeType() == 7002){
							repaymentInfo.setRepaymentAmount(repaymentInfo.getRepaymentAmount().add(riftemp.getRepaymentAmount()));
							repaymentInfo.setRepaymentStatus(checkRepaymentStatus(repaymentInfo.getRepaymentStatus(),riftemp.getRepaymentStatus()));
							repaymentInfos.remove(j);
							j--;
						}
						if(repaymentInfo.getTradeType() == 7023){
							riftemp.setRepaymentAmount(repaymentInfo.getRepaymentAmount().add(riftemp.getRepaymentAmount()));
							riftemp.setRepaymentStatus(checkRepaymentStatus(riftemp.getRepaymentStatus(),repaymentInfo.getRepaymentStatus()));
							repaymentInfos.remove(i);
							i--;
							break;
						}
					}
				}
			}
			//将7002和7022合并
			if(repaymentInfo.getTradeType() == 7002 || repaymentInfo.getTradeType() == 7022){
				for(int j=i+1; j<repaymentInfos.size(); j++){
					RepaymentInfo riftemp = repaymentInfos.get(j);
					boolean tradeTypeFlag = (riftemp.getTradeType() == 7002 || riftemp.getTradeType() == 7022);
					boolean periodFlag = (repaymentInfo.getPeriod() == riftemp.getPeriod());
					if(tradeTypeFlag && periodFlag){
						if(repaymentInfo.getTradeType() == 7002){
							repaymentInfo.setRepaymentAmount(repaymentInfo.getRepaymentAmount().add(riftemp.getRepaymentAmount()));
							repaymentInfo.setRepaymentStatus(checkRepaymentStatus(repaymentInfo.getRepaymentStatus(),riftemp.getRepaymentStatus()));
							repaymentInfos.remove(j);
							j--;
						}
						if(repaymentInfo.getTradeType() == 7022){
							riftemp.setRepaymentAmount(repaymentInfo.getRepaymentAmount().add(riftemp.getRepaymentAmount()));
							riftemp.setRepaymentStatus(checkRepaymentStatus(riftemp.getRepaymentStatus(),repaymentInfo.getRepaymentStatus()));
							repaymentInfos.remove(i);
							i--;
							break;
						}
					}
				}
			}
		}

		for(RepaymentInfo repaymentInfo : repaymentInfos){
			RepaymentItemVO repaymentItemVO = new RepaymentItemVO();
			repaymentItemVO.setRepaymentAmount(repaymentInfo.getRepaymentAmount().toString());
			repaymentItemVO.setTradeType(repaymentInfo.getTradeType());
			repaymentItemVO.setRepaymentDate(DateUtil.dateToTimestampToSec(repaymentInfo.getExpectedRepaymentDate()));
			repaymentItemVO.setRepaymentStatus(repaymentInfo.getRepaymentStatus());
			repaymentItemVOs.add(repaymentItemVO);
		}
		repaymentInfoVO.setRepaymentItemList(repaymentItemVOs);
		response.setData(CommonTool.toMap(repaymentInfoVO));
		return response;
	}

	private String checkRepaymentStatus(String a, String b){
		try {
			String[] tagArr = {"WH","HKZ","DF","TQH","YH"};
			List<String> tag = Arrays.asList(tagArr);
			int indexA = tag.indexOf(a);
			int indexB = tag.indexOf(b);
			int index = indexA > indexB ? indexB : indexA;
			return tag.get(index);
		}catch (Exception e){
			if(StringUtils.isNotBlank(a)){
				return a;
			}
			if(StringUtils.isNotBlank(b)){
				return b;
			}
			return "";
		}
	}

	@RequestMapping(value = "nearList", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
	public HttpResponse getNearInvestRecordList(@ModelAttribute PageRequestForm pageRequestForm,
												@RequestParam(required = false, value = "userId") String userId,
												@RequestParam(required = false, value = "token") String token,
												@RequestParam(required = false, value = "versionType") String versionType) {
		HttpResponse response = new HttpResponse();

		if (!pageRequestForm.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}
		String sortString = "purchaseTime.desc";//如果你想排序的话逗号分隔可以排序多列
		PageBounds pageBounds = new PageBounds(Integer.valueOf(pageRequestForm.getPage()), Integer.valueOf(pageRequestForm.getLimit()) , Order.formString(sortString));

		VersionTypeEnum vte = VersionTypeEnum.parse(versionType);
		vte = vte != null ? vte : VersionTypeEnum.PT;

		List<InvestInfo> investInfoList = null;
		try {
			investInfoList = finacingService.getNearInvestList(Integer.valueOf(userId), vte, pageBounds);
			Pager pager = new Pager(investInfoList);

			List<InvestInfoVO> investInfoVOList = new ArrayList<>();
			for (InvestInfo investInfo : investInfoList) {
				InvestInfoVO investInfoVO = new InvestInfoVO();
				investInfoVO.setBidId(investInfo.getBidId());
				investInfoVO.setBidTitle(investInfo.getZrId()==0?investInfo.getBidTitle():InterfaceConst.CREDIT_NAME_PREFIX+investInfo.getBidTitle());
				investInfoVO.setCreditFlag(investInfo.getZrId()!=0);
				investInfoVO.setIsNoviceBid("S".equals(investInfo.getIsNoviceBid())?1:0);
				investInfoVO.setCreditId(investInfo.getCreditId());
				investInfoVO.setInvestAmount(investInfo.getInvestAmount());
				investInfoVO.setPurchaseTime(DateUtil.dateToTimestampToSec(investInfo.getPurchaseTime()));
				investInfoVO.setExpireDate(investInfo.getExpireDate()==null?null:DateUtil.dateToTimestampToSec(investInfo.getExpireDate()));

				investInfoVO.setLoanMonths(investInfo.getLoanMonths());
				investInfoVO.setLoanDays(investInfo.getLoanDays());
				investInfoVO.setYearYield(investInfo.getYearYield());
				//investInfoVO.setTotalRepaymentAmount(investInfo.getTotalRepaymentAmount().toString());

				investInfoVO.setJxFlag(investInfo.getJxFlag());
				investInfoVO.setIsDepository(investInfo.getIsDepository());

				investInfoVO.setRasiedExpectedProfit(investInfo.getRasiedExpectedProfit());
				investInfoVO.setItemType(investInfo.getItemType());
				investInfoVO.setBidInterestRise(investInfo.getBidInterestRise());
				investInfoVO.setAnytimeQuit(investInfo.getAnytimeQuit());
				investInfoVO.setNewPlan(investInfo.getNewPlan());
				investInfoVO.setVoteAmount(investInfo.getSurplusAmount());

				if (investInfo.getNewPlan() == 1) {
					investInfo.setBidStatus(StatusUtil.status(investInfo.getBidStatus()));
				}
				// 标的状态
				// 投标中或待放款
				if (Status.TBZ.name().equals(investInfo.getBidStatus()) || Status.DFK.name().equals(investInfo.getBidStatus())) {
					investInfoVO.setInvestStatus(1);
				}
				// 还款中
				else if (Status.HKZ.name().equals(investInfo.getBidStatus())) {
					// 转让中
					if ("S".equals(investInfo.getIsTransfer())) {
						investInfoVO.setInvestStatus(5);
					}
					// 逾期
					else if (!"F".equals(investInfo.getIsYq())) {
						investInfoVO.setInvestStatus(4);
					} else {
						investInfoVO.setInvestStatus(2);
					}
				}
				// 已结清
				else if (Status.YJQ.name().equals(investInfo.getBidStatus())) {
					investInfoVO.setInvestStatus(3);
				}
				if (investInfo.getPlanTitle() != null) {
					investInfoVO.setBidTitle(investInfo.getPlanTitle());
				}
				investInfoVO.setPlanTitle(investInfo.getPlanTitle());
				investInfoVO.setPlanId(investInfo.getPlanId());
				investInfoVO.setItemType(investInfo.getItemType());
				investInfoVO.setBidStatus(investInfo.getBidStatus());
				investInfoVOList.add(investInfoVO);
				if (investInfoVOList.size() == 5) {
					break;
				}
			}
			pager.setItems(investInfoVOList);
			response.setData(CommonTool.toMap(pager));
		} catch (Exception e) {
			logger.error("[FinacingController.getUserInvestRecordList]", e);
			response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
		}
		return response;
	}



}
