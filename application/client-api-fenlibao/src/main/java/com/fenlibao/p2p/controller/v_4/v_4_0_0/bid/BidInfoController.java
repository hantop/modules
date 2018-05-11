package com.fenlibao.p2p.controller.v_4.v_4_0_0.bid;

import com.alibaba.fastjson.JSONArray;
import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.p2p.model.entity.PlanBidsStatus;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.bid.BidBaseInfo;
import com.fenlibao.p2p.model.entity.bid.BidBorrowerInfo;
import com.fenlibao.p2p.model.entity.bid.BidFiles;
import com.fenlibao.p2p.model.entity.bid.BorrowerInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.enums.bid.BidOriginEnum;
import com.fenlibao.p2p.model.enums.bid.LoanTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.InvestRecordsVO;
import com.fenlibao.p2p.model.vo.NoviceBidVO;
import com.fenlibao.p2p.model.vo.PlanDetailVO;
import com.fenlibao.p2p.model.vo.bidinfo.*;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.bid.PlanInfoService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.LoanTypeUtil;
import com.fenlibao.p2p.util.StatusUtil;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by laubrence on 2016/2/20.
 */
@RestController("v_4_0_0/bidInfoController")
@RequestMapping(value = "bidInfo", headers = APIVersion.v_4_0_0)
public class BidInfoController {

    public static DecimalFormat df = new DecimalFormat("#,###");

    public static Integer planVersion = Integer.valueOf(1);
    @Resource
    private BidInfoService bidInfoService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ITradeService tradeService;

    @Resource
    private PlanInfoService planInfoService;

    @Resource
    private com.fenlibao.p2p.controller.v_3.v_3_1_0.bid.BidInfoController bidInfoController;

    /**
     * 首页
     * **************************************************************
     *  按产品期限划分为5类，不区分标的和计划
     *  类别1：期限≤1个月
     *  类别2：1个月<期限≤3个月
     *  类别3：3个月<期限≤6个月
     *  类别4：6个月<期限≤12个月
     *  类别5：期限>12个月
     *  注：30天算1个月
     * 规则：
     * 1、若有月升计划，则显示月升计划，若有多个则随机选择一个
     * 2、若无月升计划，则从产品类别1~4种选择一个利率最高的，若有多个则随机选择一个
     * 3、定时产品根据后台是否勾选推荐到首页来显示，优先级最高
     *
     *************************************************************/
    @RequestMapping(value = "recommend", method = RequestMethod.GET)
    public HttpResponse getRecommendBid(@ModelAttribute BaseRequestForm paramForm,String versionType,
                                        @RequestParam(required = false, value = "userId") String userId) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate()||StringUtils.isEmpty(versionType)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        int cgNum = VersionTypeEnum.PT.getIndex();
        if(StringUtils.isNotEmpty(versionType)&&versionType.equals(VersionTypeEnum.CG.getCode())){
            cgNum = VersionTypeEnum.CG.getIndex();
        }
        List<ShopTreasureInfo> hotList = new LinkedList<>();
        List<ShopTreasureInfo> recommendList = new ArrayList<>();
        //热门产品类别
        int recommendType = 0;
        int [] loanType = new int[]{LoanTypeEnum.ONE.getCode(),LoanTypeEnum.TWO.getCode(),LoanTypeEnum.THREE.getCode(),LoanTypeEnum.FOUR.getCode(),LoanTypeEnum.FIVE.getCode()};
        int [] hostType = new int[5];
        String[] status = new String[]{Status.DFK.name(), Status.HKZ.name(), Status.YJQ.name()};


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

        //勾选推荐产品
        List<ShopTreasureInfo> stickList = bidInfoService.getStickBidAndPlanList(new String[]{Status.TBZ.name(),Status.YFB.name()}, 1, !isNovice?Status.F.name():null, null,cgNum,2);
        //获取所有投标中与预发布的标与组合
        List<ShopTreasureInfo> caninvestList = this.bidInfoService.getPlansListOrderBy(new String[]{Status.TBZ.name(),Status.YFB.name()}, 1,!isNovice?Status.F.name():null , SortEnum.RATE.name(),cgNum,-1, -1,null);
        if (null != stickList && stickList.size() > 0) {
            recommendList.add(stickList.get(0));
            if(stickList.get(0).getLoanDays()>0) {
                recommendType = LoanTypeUtil.type(stickList.get(0).getLoanDays());
            }else {
                recommendType = LoanTypeUtil.type(stickList.get(0).getMonth()*30);
            }
        } else {
            List<ShopTreasureInfo> tbzList = new ArrayList<>();  //投标中列表
            if (caninvestList != null && caninvestList.size() > 0) {
                for (ShopTreasureInfo tbz : caninvestList){
                    if (Status.TBZ.name().equals(tbz.getStatus()) || "4".equals(tbz.getStatus())){ //计划投标中状态为4
                        if (getLoanDays(tbz) <= 12 * 30){ //只取前4类产品
                            tbzList.add(tbz);
                        }
                    }
                }
            }
            //利率最高的可投计划或者标
            if (tbzList != null && tbzList.size() > 0) {
                ShopTreasureInfo bestRate = tbzList.get(0);
                if (caninvestList.size() >= 2){
                    for (int i = 1 ; i < tbzList.size() ; i++){
                        if (getRate(bestRate).compareTo(getRate(tbzList.get(i))) == -1){
                            bestRate = tbzList.get(i);
                        }
                    }
                }

                recommendList.add(bestRate);
                if (bestRate.getLoanDays() > 0) {
                    recommendType = LoanTypeUtil.type(bestRate.getLoanDays());
                } else {
                    recommendType = LoanTypeUtil.type(bestRate.getMonth() * 30);
                }
            }
        }
        int j = 0;
        for (int i : loanType) {
            if (recommendType != i) {
                hostType[j++] = i;
            }
        }

        //产品分类
        List<ShopTreasureInfo> p1List = new ArrayList<>();
        List<ShopTreasureInfo> p2List = new ArrayList<>();
        List<ShopTreasureInfo> p3List = new ArrayList<>();
        List<ShopTreasureInfo> p4List = new ArrayList<>();
        List<ShopTreasureInfo> p5List = new ArrayList<>();

        int[] loanDayArry = new int[2];
        if(caninvestList != null && caninvestList.size() > 0) {
            for (int i = 0; i < caninvestList.size(); i++) {
                //热门产品(存放4个不同类别的产品)
                loanDayArry = LoanTypeUtil.dayArry(hostType[3]);
                if (getLoanDays(caninvestList.get(i)) > loanDayArry[0] && getLoanDays(caninvestList.get(i))  <= loanDayArry[1]){
                    p1List.add(caninvestList.get(i));
                    continue;
                }

                loanDayArry = LoanTypeUtil.dayArry(hostType[2]);
                if (getLoanDays(caninvestList.get(i)) > loanDayArry[0] && getLoanDays(caninvestList.get(i))  <= loanDayArry[1]){
                    p2List.add(caninvestList.get(i));
                    continue;
                }

                loanDayArry = LoanTypeUtil.dayArry(hostType[1]);
                if (getLoanDays(caninvestList.get(i)) > loanDayArry[0] && getLoanDays(caninvestList.get(i))  <= loanDayArry[1]){
                    p3List.add(caninvestList.get(i));
                    continue;
                }

                loanDayArry = LoanTypeUtil.dayArry(hostType[0]);
                if (getLoanDays(caninvestList.get(i)) > loanDayArry[0] && getLoanDays(caninvestList.get(i))  <= loanDayArry[1]){
                    p4List.add(caninvestList.get(i));
                    continue;
                }

                if (recommendList.size() == 0){  //推荐列表为空的情况，这个时候一共有5类产品
                    loanDayArry = LoanTypeUtil.dayArry(hostType[4]);
                    if (getLoanDays(caninvestList.get(i)) > loanDayArry[0] && getLoanDays(caninvestList.get(i))  <= loanDayArry[1]){
                        p5List.add(caninvestList.get(i));
                        continue;
                    }
                }

            }
        }
        //排序,取出最终结果
        ShopTreasureInfo p1 = getHotSortResult(p1List);
        ShopTreasureInfo p2 = getHotSortResult(p2List);
        ShopTreasureInfo p3 = getHotSortResult(p3List);
        ShopTreasureInfo p4 = getHotSortResult(p4List);
        ShopTreasureInfo p5 = getHotSortResult(p5List);

        //以下按照5个类别降序排列
        if (p5 != null){
            hotList.add(p5);
        }
        if (p1 != null){
            hotList.add(p1);
        }
        if (p2 != null){
            hotList.add(p2);
        }
        if (p3 != null){
            hotList.add(p3);
        }
        if (p4 != null){
            hotList.add(p4);
        }


        if (hotList.size() >= 5){//无推荐，并且热门列表为5，去除掉发布时间最新的
            //取出最新发布时间
            long publicTime = hotList.get(0).getPublishDate().getTime();
            for (ShopTreasureInfo s : hotList){
                if (publicTime < s.getPublishDate().getTime()){
                    publicTime = s.getPublishDate().getTime();
                }
            }

            //去除发布时间最新的
            Iterator<ShopTreasureInfo> i = hotList.iterator();
            while (i.hasNext()){
                if (i.next().getPublishDate().getTime() == publicTime){
                    i.remove();
                    break;
                }
            }
        }

        response.getData().put("hotList", extendBidInfo(hotList));
        //判断新手标还是推荐标
        List<BidInfoVO> recommendOrNovice = extendBidInfo(recommendList);
        if (0 != recommendList.size() && recommendOrNovice.size() != 0 && 1 != recommendOrNovice.get(0).getIsNoviceBid()) {
            recommendOrNovice.get(0).setBidClassify(3);
        }
        response.getData().put("recommendList", recommendOrNovice);
        return response;

    }

    /**
     * 热门列表单类排序
     * @param pList
     * @return
     */
    private ShopTreasureInfo getHotSortResult(List<ShopTreasureInfo> pList) {
        ShopTreasureInfo result = null;
        if (pList != null && pList.size() > 0){
            result = pList.get(0);
        }
        if (pList != null && pList.size() > 1){
            for (int i = 1 ; i < pList.size() ; i++){
                if (pList.get(i).getVoteAmount().compareTo(result.getVoteAmount()) == -1){ //可投金额最小的
                    result = pList.get(i);
                }else if (pList.get(i).getVoteAmount().compareTo(result.getVoteAmount()) == 0){  //可投金额相同
                    if (getRate(pList.get(i)).compareTo(getRate(result)) == 1){  //利率最高的
                        result = pList.get(i);
                    }
                }
            }
        }
        return result;
    }

    /**
     *  获取借款周期（天）
     * @param shopTreasureInfo
     * @return
     */
    private int getLoanDays(ShopTreasureInfo shopTreasureInfo){
        if (shopTreasureInfo.getMonth() != 0){
            return shopTreasureInfo.getMonth() * 30;
        }else {
            return shopTreasureInfo.getLoanDays();
        }
    }

    /**
     * 获取产品利率，包括产品加息
     * @param shopTreasureInfo
     * @return
     */
    private BigDecimal getRate(ShopTreasureInfo shopTreasureInfo){
        double rate = shopTreasureInfo.getRate();
        if (shopTreasureInfo.getInterest() != 0){
            rate += shopTreasureInfo.getInterest();
        }
        return new BigDecimal(rate);
    }

    private List<BidInfoVO> extendBidInfo(List<ShopTreasureInfo> hotList) {
        List<BidInfoVO> voList = new ArrayList<BidInfoVO>();
        for (ShopTreasureInfo info : hotList) {
            BidInfoVO vo = new BidInfoVO();
            vo.setBidId(info.getId());
            //过滤数字信息
            String name = info.getName();

            vo.setPlanCanQuit(1);
            if (info.getIsNoviceBid().equals("S")) {
                vo.setPlanCanQuit(0);
            }

            vo.setBidTitle(name);
            vo.setBidType(info.getBidType());
            vo.setBidYield(String.valueOf(info.getRate()));
            vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name()) ? 1 : 0);
            vo.setLoanDays(info.getLoanDays());
            vo.setLoanMonth(info.getMonth());
//            vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType()) ? info.getAssetsType().split(",") : new String[0]);
            vo.setTimestamp(DateUtil.dateToTimestampToSec(info.getPublishDate()));
            vo.setLoanAmount(info.getLoanAmount().toString());
            vo.setInterest(info.getInterest() < 0.001 ? "0" : Double.toString(info.getInterest()));
            if (Status.HKZ.name().equals(info.getStatus()) || Status.YJQ.name().equals(info.getStatus()) || Status.DFK.name().equals(info.getStatus())) {
                vo.setSurplusAmount(BigDecimal.ZERO.toString());
            } else {
                if (info.getVoteAmount() != null) {
                    vo.setSurplusAmount(info.getVoteAmount().toString());
                } else {
                    vo.setSurplusAmount(info.getLoanAmount().toString());
                }


            }
            vo.setProgressRate(info.getLoanAmount(), info.getVoteAmount());
            vo.setBidClassify(Status.S.name().equals(info.getIsNoviceBid()) ? 1 : 0);
            vo.setIsCG(info.getIsCG());
            vo.setInvestLimit("100");
            //判断是否定向标
            if (info.getUserTotalAssets() != null) {
                vo.setBidClassify(2);
            }
            //设置抢购标参数
            if (info.getPanicBuyingTime() != null) {
                long timeLeft = (info.getPublishDate().getTime() - System.currentTimeMillis()) / 1000 + InterfaceConst.EEROR_SECOND;
                ;
                Integer countdown = Integer.valueOf(Config.get("bid.countdown.mimute")) * 60;
                vo.setTimeLeft(timeLeft > 0 ? timeLeft : 0);
                vo.setCountdown(countdown);
                vo.setPanicBuyingTime(DateUtil.dateToTimestampToSec(info.getPublishDate()));
            }
            vo.setRepaymentMode(info.getRepaymentMode());
            if (info.getItemType() == 1) {
                vo.setNewPlan(1);
                info.setStatus(StatusUtil.status(info.getStatus()));
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
            if (Status.YFB.name().equals(info.getStatus())) {
                vo.setBidStatus(0);
            }
            //todo 标的说明
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
            if (info.getPanicBuyingTime() != null) {
                if (bidLabels != null && bidLabels.size() > 0) {
                    bidLabels = bidLabels.subList(0, bidLabels.size() >= 1 ? 1 : bidLabels.size());
                    vo.setBidLabel(bidLabels);
                }
            }else {
                if (bidLabels != null && bidLabels.size() > 0) {
                    bidLabels = bidLabels.subList(0, bidLabels.size() >= 2 ? 2 : bidLabels.size());
                    vo.setBidLabel(bidLabels);
                }
            }
            List<String> markList = new ArrayList<>();
            if (info.getAccumulatedIncome() != null) {
                markList.add("累计收益达" + df.format(Double.valueOf(info.getAccumulatedIncome())) + "元客户专享");
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
            vo.setItemType(info.getItemType());

            vo.setPlanType(info.getPlanType());
            vo.setComment(info.getComment());
            vo.setLowRate(String.valueOf(info.getLowRate()));
            vo.setHighRate(String.valueOf(info.getHighRate()));
            voList.add(vo);
        }

        List<BidInfoVO> items = new LinkedList<>();//首页
        items.addAll(voList);
        return items;
    }

    /**
     * 投资-可投资的标的
     *
     * @param paramForm
     * @param sortType  排序类型(1:时间 2：利率 3：进度)
     * @param sortBy    排序方式(1: 降序2：升序)，默认降序
     * @param bidOrigin 标的来源：（分利宝：0001\缺钱么：0002）
     *                  ***************************************************************
     *                  rule：1、显示顺序：新手标>新手计划>定时标的/计划1个（等待抢购的）>月升计划>省心计划3个（顺序10天>20天>30天>3月>6月......）>定时标的/计划（最近正在抢购的）>其他类型标>满额标（默认不显示），其他类型以期限升序排列
     *                  2、多个定时标（最近正在抢购的）以发布时间降序排列
     *                  3、当可投标和可投计划小于5个时，显示最近满额的标的/计划，保证页面上不少于5个标的/计划
     *                  4、用户未投资&未登录，显示全部标的和计划；用户已投资，不再显示新手标&新手计划
     *                  5、满额项目只显示最近的20个标的/计划，以满额时间降序排列
     *                  6、若后台对标的/计划置顶，则优先以置顶时间倒序显示
     ****************************************************************/
    @RequestMapping(value = "caninvest/list", method = RequestMethod.GET)
    public HttpResponse getCaninvestBid(@ModelAttribute BaseRequestForm paramForm,
                                        String sortType, String sortBy, String bidOrigin, String productType,String versionType, @RequestParam(required = false, value = "userId") String userId) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate()||StringUtils.isEmpty(versionType)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        int cgNum = VersionTypeEnum.PT.getIndex();

        List<BidInfoVO> voList = new LinkedList<>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", new String[]{Status.TBZ.name(),Status.YFB.toString() });
        if (StringUtils.isNotEmpty(sortType)) {
            sortType = SortEnum.getOrderType(sortType);
            map.put("sortType", sortType);
        }
        if (StringUtils.isNotEmpty(productType)) {
            map.put("productType", productType);
        } else {
            map.put("productType", "plan");
        }
        if (StringUtils.isNotEmpty(sortBy)) {
            sortBy = sortBy.equals("1") ? InterfaceConst.ORDER_TYPE_DESC : InterfaceConst.ORDER_TYPE_ASC;
            map.put("sortBy", sortBy);
        }
        if (StringUtils.isNotEmpty(versionType)&&versionType.equals(VersionTypeEnum.CG.getCode())) {
            cgNum = VersionTypeEnum.CG.getIndex();
        }
        map.put("cgNum",cgNum);
        map.put("bidOrigin", bidOrigin);
        boolean showPrecisionTitle = bidOrigin != null && "0002".equals(bidOrigin);
        if (showPrecisionTitle) {
            //消费信贷列表只显示50条
            map.put("limit", 50);
        }
        boolean isNovice;
        String noviceStatus = null;
        try {
            if (StringUtils.isNotBlank(userId) && Integer.valueOf(userId) > 0) {
                isNovice = bidInfoService.isNovice(Integer.valueOf(userId));
            } else {
                isNovice = true;
            }
        } catch (Throwable e) {
            isNovice = true;
        }
        if(!isNovice){
            noviceStatus = Status.F.name();
            map.put("isNoviceBid",Status.F.name());
            map.put("isNovicePlan",0);
        }
        List<ShopTreasureInfo> canInvestBidList = null;
        //默认列表
        if (sortType.equals("BIDTIME") && sortBy.equals(InterfaceConst.ORDER_TYPE_ASC)) {
            if (productType != null && "bid".equals(productType)) {//默认散标列表
                // /新手标
                List<ShopTreasureInfo> noviceBidList = null;
                List<ShopTreasureInfo> commonBidList = new ArrayList<ShopTreasureInfo>();
                List<ShopTreasureInfo> bidList = new ArrayList<>();
                //置顶标
                List<ShopTreasureInfo> stickList = bidInfoService.getStickBidAndPlanList(new String[]{Status.TBZ.name(),Status.YFB.name()}, -1, noviceStatus, "bid",cgNum,1);
                if (null != stickList && stickList.size() > 0) {
                    bidList.addAll(stickList);
                    for (ShopTreasureInfo info : stickList) {
                        if(info.getVoteAmount().doubleValue()>=100) {
                            commonBidList.add(info);
                        }
                    }
                }
                if (isNovice) {//添加新手标
                    noviceBidList = this.bidInfoService.getBidList(new String[]{Status.TBZ.toString()}, null, Integer.MAX_VALUE, Status.S.name(), null, null,cgNum);
                    if (noviceBidList != null && noviceBidList.size() > 0) {
                        bidList.addAll(noviceBidList);
                        commonBidList.addAll(noviceBidList);
                    }
                }
                //定时标（等待抢购）
                List<ShopTreasureInfo> preSaleBids = this.bidInfoService.getPreSaleBids(-1,cgNum);
                if (null != preSaleBids && preSaleBids.size() > 0) {
                    for (ShopTreasureInfo info : preSaleBids) {
                        if (!commonBidList.contains(info)&&info.getVoteAmount().doubleValue()>=100) {
                            bidList.add(info);
                            commonBidList.add(info);
                        }
                    }

                }
                //抢购标(投标中)
                List<ShopTreasureInfo> timingBids = this.bidInfoService.getTimingBids(-1,cgNum);
                if (null != timingBids && timingBids.size() > 0) {
                    for (ShopTreasureInfo info : timingBids) {
                        if (!commonBidList.contains(info)&&info.getVoteAmount().doubleValue()>=100) {
                            bidList.add(info);
                            commonBidList.add(info);
                        }
                    }

                }

                //其他类型的标
                List<ShopTreasureInfo> otherBidList = this.bidInfoService.getOtherBidList(new String[]{Status.TBZ.toString()}, null, -1, noviceStatus, null, null,cgNum);
                if (otherBidList != null && otherBidList.size() > 0) {
                    for (ShopTreasureInfo info : otherBidList) {
                        if (!commonBidList.contains(info)&&info.getVoteAmount().doubleValue()>=100) {
                            bidList.add(info);
                            commonBidList.add(info);
                        }
                    }
                }
                /*String[] status = new String[]{Status.DFK.name(), Status.HKZ.name(), Status.YJQ.name()};

                Map<String, Object> fullMap = new HashMap<String, Object>();
                fullMap.put("status", status);
                fullMap.put("cgNum",cgNum);
                fullMap.put("fullTime","1");
                List<ShopTreasureInfo> bidFullList = new ArrayList<ShopTreasureInfo>();
                if(null==bidList){
                     fullMap.put("limit",5);
                     bidFullList = this.bidInfoService.getBidList(fullMap);
                }else if(bidList.size()<5){
                    fullMap.put("limit",5-bidList.size());
                    bidFullList = this.bidInfoService.getBidList(fullMap);
                }
                bidList.addAll(bidFullList);*/
                List<BidInfoVO> bidVOList = extendBidInfo(bidList);
                voList.addAll(bidVOList);
            } else {
                //默认计划投资列表
                if (sortType.equals("BIDTIME") && sortBy.equals(InterfaceConst.ORDER_TYPE_ASC)) {
                    List<ShopTreasureInfo> commonPlanList = new ArrayList<ShopTreasureInfo>();
                    List<ShopTreasureInfo> bidList = new ArrayList<>();
                    //置顶标
                    List<ShopTreasureInfo> stickList = bidInfoService.getStickBidAndPlanList(new String[]{Status.TBZ.name()}, -1, noviceStatus, "plan",cgNum,1);
                    if (null != stickList && stickList.size() > 0) {
                        bidList.addAll(stickList);
                        for (ShopTreasureInfo info : stickList) {
                            commonPlanList.add(info);
                        }
                    }

                    if (isNovice) {//添加新手标
                        //添加新手计划
                        List<ShopTreasureInfo> plansList = this.bidInfoService.getPlansList(new String[]{Status.TBZ.toString()}, null, -1, -1, null, null, 1, -1, -1, Status.S.name(), -1, "timeSort", -1,cgNum);
                        if (plansList != null && plansList.size() > 0) {
                            for (ShopTreasureInfo info : plansList) {
                                if (!commonPlanList.contains(info)) {
                                    bidList.add(info);
                                    commonPlanList.add(info);
                                }
                            }
                        }
                    }
                    //定时计划（等待抢购）
                    List<ShopTreasureInfo> yfbPlanList = this.bidInfoService.getPlansList(new String[]{Status.YFB.toString()}, null, -1, -1, "BUYTIME", null, 1, -1, -1, Status.F.name(), -1, "timeSort", 1,cgNum);
                    if (null != yfbPlanList && yfbPlanList.size() > 0) {
                        for (ShopTreasureInfo info : yfbPlanList) {
                            if (!commonPlanList.contains(info)) {
                                bidList.add(info);
                                commonPlanList.add(info);
                            }
                        }
                    }

                    //抢购计划
                    List<ShopTreasureInfo> timingPlanList = this.bidInfoService.getPlansList(new String[]{Status.TBZ.toString()}, null, -1, -1, "BUYTIME", null, 1, -1, -1, Status.F.name(), -1, "timeSort", 1,cgNum);
                    if (null != timingPlanList && timingPlanList.size() > 0) {
                        for (ShopTreasureInfo info : timingPlanList) {
                            if (!commonPlanList.contains(info)) {
                                bidList.add(info);
                                commonPlanList.add(info);
                            }
                        }
                    }

                    //月升计划
                    List<ShopTreasureInfo> monthPlanList = this.bidInfoService.getPlansList(new String[]{Status.TBZ.toString()}, null, -1, -1, "BIDTIME", null, 1, -1, -1, Status.F.name(), 1, "timeSort", -1,cgNum);
                    if (null != monthPlanList && monthPlanList.size() > 0) {
                        for (ShopTreasureInfo info : monthPlanList) {
                            if (!commonPlanList.contains(info)) {
                                bidList.add(info);
                                commonPlanList.add(info);
                            }
                        }
                        //bidList.addAll(monthPlanList);
                    }
                    //省心计划
                    List<ShopTreasureInfo> shengxinPlanList = this.bidInfoService.getPlansList(new String[]{Status.TBZ.toString()}, null, -1, -1, "BIDTIME", null, 1, -1, -1, Status.F.name(), 2, "timeSort", -1,cgNum);
                    if (null != shengxinPlanList && shengxinPlanList.size() > 0) {
                        for (ShopTreasureInfo info : shengxinPlanList) {
                            if (!commonPlanList.contains(info)) {
                                bidList.add(info);
                                commonPlanList.add(info);
                            }
                        }
                    }
                   /* List<ShopTreasureInfo> plansFullList = new ArrayList<ShopTreasureInfo>();
                    if(null==bidList){
                        plansFullList  = this.bidInfoService.getPlansList(new String[]{Status.DFK.name(), Status.HKZ.name(), Status.YJQ.name()}, null, -1, -1, null, null, 1, -1, 5, null, -1, "timeSort", -1,cgNum);
                    }else if(bidList.size()<5){
                        plansFullList  = this.bidInfoService.getPlansList(new String[]{Status.DFK.name(), Status.HKZ.name(), Status.YJQ.name()}, null, -1, -1, null, null, 1, -1, 5-bidList.size(), null, -1, "timeSort", -1,cgNum);
                    }
                    bidList.addAll(plansFullList);*/
                    List<BidInfoVO> bidVOList = extendBidInfo(bidList);
                    voList.addAll(bidVOList);
                }
            }
        } else {//非默认列表
            canInvestBidList = this.bidInfoService.getBidListAndPlanList(map);

            if (null != canInvestBidList && canInvestBidList.size() > 0) {
                for (ShopTreasureInfo info : canInvestBidList) {
                    boolean panicBuyingTime = true;
                    if(info.getStatus().equals(Status.YFB.toString())&&info.getPanicBuyingTime().getTime()>new Date().getTime()){
                        panicBuyingTime = false;//还未上架到列表展示
                    }
                    if (info.getVoteAmount().doubleValue() >= 100&&panicBuyingTime) {
                        BidInfoVO vo = new BidInfoVO();
                        if (info.getItemType() == 1) {
                            info.setStatus(StatusUtil.status(info.getStatus()));
                            vo.setBidStatus(Status.YFB.name().equals(info.getStatus()) ? 0 : 1);
                            vo.setBidId(info.getId());
                            vo.setPlanCanQuit(0);

                            if (!info.getIsNoviceBid().equals("S")) {
                                vo.setPlanCanQuit(1);
                            }

                            vo.setBidTitle(info.getName());
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
                            if (info.getVoteAmount() != null) {
                                vo.setSurplusAmount(info.getVoteAmount().toString());
                            } else {
                                vo.setSurplusAmount(info.getLoanAmount().toString());
                            }
                            //vo.setSurplusAmount(String.valueOf(info.getVoteAmount()));
                            vo.setProgressRate(info.getLoanAmount(), info.getVoteAmount());
                            vo.setIsNoviceBid(Status.S.name().equals(info.getIsNoviceBid()) ? 1 : 0);
                            //区分数组的item类型(0:标  1:计划)
                            vo.setItemType(info.getItemType() == 1 ? 1 : 0);
                            vo.setInterest(info.getInterest() < 0.001 ? "0" : Double.toString(info.getInterest()));
                            vo.setRepaymentMode(info.getRepaymentMode());
                            //设置抢购标参数
                            if (info.getPanicBuyingTime() != null) {
                                long timeLeft = (info.getPublishDate().getTime() - System.currentTimeMillis()) / 1000 + InterfaceConst.EEROR_SECOND;
                                Integer countdown = Integer.valueOf(Config.get("bid.countdown.mimute")) * 60;
                                vo.setTimeLeft(timeLeft > 0 ? timeLeft : 0);
                                vo.setCountdown(countdown);
                                vo.setPanicBuyingTime(DateUtil.dateToTimestampToSec(info.getPublishDate()));
                            }
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
                            if (info.getPanicBuyingTime() != null) {
                                if (bidLabels != null && bidLabels.size() > 0) {
                                    bidLabels = bidLabels.subList(0, bidLabels.size() >= 1 ? 1 : bidLabels.size());
                                    vo.setBidLabel(bidLabels);
                                }
                            }else {
                                if (bidLabels != null && bidLabels.size() > 0) {
                                    bidLabels = bidLabels.subList(0, bidLabels.size() >= 2 ? 2 : bidLabels.size());
                                    vo.setBidLabel(bidLabels);
                                }
                            }
                            List<String> markList = new ArrayList<>();
                            if (info.getAccumulatedIncome() != null) {
                                markList.add("累计收益达" + df.format(Double.valueOf(info.getAccumulatedIncome())) + "元客户专享");
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
                            vo.setItemType(info.getItemType());
                            vo.setComment(info.getComment());
                            vo.setPlanType(info.getPlanType());
                            vo.setHighRate(String.valueOf(info.getHighRate()));
                            vo.setLowRate(String.valueOf(info.getLowRate()));
                            vo.setNewPlan(1);
                            voList.add(vo);

                        } else {
                            vo.setBidId(info.getId());
                            //过滤数字信息
                            String name = info.getName();
                            vo.setBidTitle(name);

                            vo.setPlanCanQuit(1);
                            if (info.getIsNoviceBid().equals("S")) {
                                vo.setPlanCanQuit(0);
                            }
                            vo.setBidYield(String.valueOf(info.getRate()));
                            vo.setLoanDays(info.getLoanDays());
                            vo.setLoanMonth(info.getMonth());
                            vo.setTimestamp(DateUtil.dateToTimestampToSec(info.getPublishDate()));
                            vo.setBidType(info.getBidType());
                            vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType()) ? info.getAssetsType().split(",") : new String[0]);
                            vo.setBidStatus(Status.YFB.name().equals(info.getStatus()) ? 0 : 1);
                            vo.setLoanAmount(info.getLoanAmount().toString());
                            if (info.getVoteAmount() != null) {
                                vo.setSurplusAmount(info.getVoteAmount().toString());
                            } else {
                                vo.setSurplusAmount(info.getLoanAmount().toString());
                            }
                            vo.setProgressRate(info.getLoanAmount(), info.getVoteAmount());
                            vo.setIsNoviceBid(Status.S.name().equals(info.getIsNoviceBid()) ? 1 : 0);
                            vo.setBidClassify(Status.S.name().equals(info.getIsNoviceBid()) ? 1 : 0);
                            vo.setIsCG(info.getIsCG());
                            //判断是否定向标
                            if (info.getUserTotalAssets() != null) {
                                vo.setBidClassify(2);
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
                                markList.add("累计收益达" + df.format(Double.valueOf(info.getAccumulatedIncome())) + "元客户专享");
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
                            vo.setItemType(info.getItemType());
                            vo.setComment(info.getComment());
                            vo.setPlanType(info.getPlanType());
                            voList.add(vo);

                        }
                    }
                }
            }
            /*List<ShopTreasureInfo> fullList = new ArrayList<ShopTreasureInfo>();
            if(productType!=null&&productType.equals("bid")) {
                Map<String, Object> fullMap = new HashMap<String, Object>();
                String[] status = new String[]{Status.DFK.name(), Status.HKZ.name(), Status.YJQ.name()};
                fullMap.put("status", status);
                fullMap.put("cgNum", cgNum);
                fullMap.put("fullTime", "1");

                if (null == voList) {
                    fullMap.put("limit", 5);
                    fullList = this.bidInfoService.getBidList(fullMap);
                } else if (voList.size() < 5) {
                    fullMap.put("limit", 5 - voList.size());
                    fullList = this.bidInfoService.getBidList(fullMap);
                }
            }else {
                if(null==voList){
                    fullList  = this.bidInfoService.getPlansList(new String[]{Status.DFK.name(), Status.HKZ.name(), Status.YJQ.name()}, null, -1, -1, null, null, 1, -1, 5, null, -1, "timeSort", -1,cgNum);
                }else if(voList.size()<5){
                    fullList  = this.bidInfoService.getPlansList(new String[]{Status.DFK.name(), Status.HKZ.name(), Status.YJQ.name()}, null, -1, -1, null, null, 1, -1, 5-voList.size(), null, -1, "timeSort", -1,cgNum);
                }
            }
            List<BidInfoVO> bidVOList = extendBidInfo(fullList);
            voList.addAll(bidVOList);*/
        }
        List<BidInfoVO> items = new LinkedList<>();
        items.addAll(voList);
        response.getData().put("items", items);
        return response;
    }






    /**
     * 已过期的标(满额、到期)
     *
     * @param paramForm
     * @param timestamp 排序 (发标时间)
     * @return
     */
    @RequestMapping(value = "history/list", method = RequestMethod.GET)
    public HttpResponse getHistoryBid(@ModelAttribute BaseRequestForm paramForm,
                                      @RequestParam(required = false, value = "timestamp") String timestamp, String bidOrigin, String productType,String versionType) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate()) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        int cgNum = VersionTypeEnum.PT.getIndex();
        if(StringUtils.isNotEmpty(versionType)&&versionType.equals(VersionTypeEnum.CG.getCode())){
            cgNum = VersionTypeEnum.CG.getIndex();
        }

            List<BidInfoVO> voList = new ArrayList<BidInfoVO>();
            List<ShopTreasureInfo> list = new ArrayList<ShopTreasureInfo>();
            String[] status = new String[]{Status.DFK.name(), Status.HKZ.name(), Status.YJQ.name()};

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", status);
            map.put("cgNum",cgNum);
            map.put("fullTime","1");
            if (StringUtils.isEmpty(bidOrigin)) {//新
                map.put("limit", 20);
                //map.put("bidOrigin",bidOrigin);
                if (StringUtils.isNotEmpty(timestamp)) {
                    Date time = DateUtil.timestampToDateBySec(Long.valueOf(timestamp));
                    map.put("time", time);
                } else if (!StringUtils.isEmpty(productType) && "bid".equals(productType)) {
                    List<ShopTreasureInfo> historylist = this.bidInfoService.getBidList(map);
                    list.addAll(historylist);
                } else if (StringUtils.isEmpty(productType) || "plan".equals(productType)) {
                    List<ShopTreasureInfo> plansFullList = this.bidInfoService.getPlansList(status, null, -1, -1, null, null, 1, -1, 20, null, -1, "timeSort", -1,cgNum);
                    //List<ShopTreasureInfo> plansList = this.bidInfoService.getPlansList(status, null, -1, -1, null, null, 1, -1, 20, null, -1, "timeSort", -1);
                    //List<ShopTreasureInfo> plansList = this.bidInfoService.getPlansListOrderBy(status,20,null,null);
                    list.addAll(plansFullList);
                }
            } else {
                map.put("limit", 10);
                map.put("bidOrigin", bidOrigin);
                if (StringUtils.isNotEmpty(timestamp)) {
                    Date time = DateUtil.timestampToDateBySec(Long.valueOf(timestamp));
                    map.put("time", time);
                } else if (bidOrigin != null && !"0002".equals(bidOrigin)) {
                    //List<ShopTreasureInfo> plansList = this.bidInfoService.getPlansList(status, null, -1, -1, null, null, 1, -1, 20, null, -1, "timeSort", -1);
                    List<ShopTreasureInfo> plansList = this.bidInfoService.getPlansListOrderBy(status, 20, null, null,cgNum,-1,-1,null);
                    list.addAll(plansList);
                } else if (bidOrigin != null && "0002".equals(bidOrigin)) {
                    List<ShopTreasureInfo> historylist = this.bidInfoService.getBidList(map);
                    list.addAll(historylist);
                }
            }


            for (ShopTreasureInfo info : list) {
                BidInfoVO vo = new BidInfoVO();
                if (info.getItemType() == 0) {
                    vo.setBidId(info.getId());
                    //过滤数字信息
                    String name = info.getName();
                    vo.setBidTitle(name);
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
                        if (info.getFundraisDate()!=null&&info.getFundraisDate().getTime() > DateUtil.nowDate().getTime()) {
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
                    vo.setInterest(info.getInterest() < 0.001 ? "0" : Double.toString(info.getInterest()));
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
                    if (info.getPanicBuyingTime() != null) {
                        if (bidLabels != null && bidLabels.size() > 0) {
                            bidLabels = bidLabels.subList(0, bidLabels.size() >= 1 ? 1 : bidLabels.size());
                            vo.setBidLabel(bidLabels);
                        }
                    }else {
                        if (bidLabels != null && bidLabels.size() > 0) {
                            bidLabels = bidLabels.subList(0, bidLabels.size() >= 2 ? 2 : bidLabels.size());
                            vo.setBidLabel(bidLabels);
                        }
                    }
                    List<String> markList = new ArrayList<>();
                    if (info.getAccumulatedIncome() != null) {
                        markList.add("累计收益达" + df.format(Double.valueOf(info.getAccumulatedIncome())) + "元客户专享");
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
                    vo.setComment(info.getComment());
                    voList.add(vo);
                } else if (info.getItemType() == 1) {
                    info.setStatus(StatusUtil.status(info.getStatus()));
                    vo.setBidId(info.getId());
                    //过滤数字信息
                    String name = info.getName();
                    vo.setBidTitle(name);
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
                    vo.setInterest(info.getInterest() < 0.001 ? "0" : Double.toString(info.getInterest()));
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
                    if (info.getPanicBuyingTime() != null) {
                        if (bidLabels != null && bidLabels.size() > 0) {
                            bidLabels = bidLabels.subList(0, bidLabels.size() >= 1 ? 1 : bidLabels.size());
                            vo.setBidLabel(bidLabels);
                        }
                    }else {
                        if (bidLabels != null && bidLabels.size() > 0) {
                            bidLabels = bidLabels.subList(0, bidLabels.size() >= 2 ? 2 : bidLabels.size());
                            vo.setBidLabel(bidLabels);
                        }
                    }
                    List<String> markList = new ArrayList<>();
                    if (info.getAccumulatedIncome() != null) {
                        markList.add("累计收益达" + df.format(Double.valueOf(info.getAccumulatedIncome())) + "元客户专享");
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
                    vo.setItemType(1);
                    vo.setHighRate(String.valueOf(info.getHighRate()));
                    vo.setLowRate(String.valueOf(info.getLowRate()));
                    vo.setComment(info.getComment());
                    vo.setPlanType(info.getPlanType());
                    vo.setNewPlan(1);
                    voList.add(vo);
                }
            }
            response.getData().put("items", voList);
            return response;

    }

    /**
     * 新手标  筹款到期时间、发布时间升序，取第一个
     *
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "noviceBid", method = RequestMethod.GET)
    public HttpResponse getNoviceBid(@ModelAttribute BaseRequestForm paramForm,String versionType) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate()) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        int cgNum = 1;
        if(StringUtils.isNotEmpty(versionType)&&versionType.equals(VersionTypeEnum.CG.getName())){
            cgNum = 2;
        }
        List<ShopTreasureInfo> list = this.bidInfoService.getBidList(new String[]{Status.TBZ.toString()}, null, 1, Status.S.name(), null, null,cgNum);

        //当前新手标
        if (null == list || list.size() == 0) {
            return response;
        }
        ShopTreasureInfo info = list.get(0);

        NoviceBidVO vo = new NoviceBidVO();
        vo.setBidId(info.getId());
        vo.setIsNoviceBid(info.getIsNoviceBid().equals(Status.S.name()) ? 1 : 0);
        vo.setLoanDays(info.getLoanDays());
        //过滤数字信息
        String name = info.getName();
        vo.setNoviceBidTitle(name);
		/*Pattern pattern = Pattern.compile("\\d+$");
		Matcher matcher = pattern.matcher(name);
		if(matcher.find()){//标题尾部是数字
			pattern = Pattern.compile("[0-9]+(?=[^0-9]*$)");
			matcher = pattern.matcher(name);
			name=matcher.replaceAll("").trim();
		}*/
        vo.setNoviceBidTitle(name.replaceAll("(?<=.{12}).+", "…"));
        vo.setTimestamp(String.valueOf(DateUtil.dateToTimestampToSec(info.getPublishDate())));
        vo.setYield(String.valueOf(info.getRate()));
        vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType()) ? info.getAssetsType().split(",") : new String[0]);
        //设置存管加息参数
        vo.setInterest("0");
        vo.setIsCG(info.getIsCG());
        vo.setAnytimeQuit(info.getAnytimeQuit());
        vo.setBidLabel(StringUtils.isNotEmpty(info.getBidLabel()) ? info.getAssetsType().split(",") : new String[0]);
        vo.setItemType(0);

        response.setData(CommonTool.toMap(vo));
        return response;
    }

    /**
     * 标的详情
     *
     * @param paramForm
     * @param userId
     * @param bidId     标的ID
     * @return
     */
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public HttpResponse getBidDetail(@ModelAttribute BaseRequestForm paramForm,
                                     String userId, String bidId, String planId
            , VersionTypeEnum versionType) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate() || (StringUtils.isEmpty(bidId) && StringUtils.isEmpty(planId))
                || versionType == null ) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        if (planVersion == 1) {
            ShopTreasureInfo info = null;
            String id = null;//服务协议等用到的id
            if (!StringUtils.isBlank(bidId)) {
                info = this.bidInfoService.getBidInfo(Integer.parseInt(bidId),versionType.getIndex());
                id = bidId;
            } else if (!StringUtils.isBlank(planId)) {//计划详情
                info = this.bidInfoService.getPlanDetail(Integer.parseInt(planId),versionType.getIndex());
                id = planId;
            }
            if (null == info) {
                response.setCodeMessage(ResponseCode.BID_DETAILS_EMPTY);
                return response;
            }
            if (!StringUtils.isBlank(bidId)) {
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
//			DecimalFormat dft = new DecimalFormat("#,##0");
                    vo.setUserTotalAssets(info.getUserTotalAssets().toPlainString());
                }
                vo.setBidType(info.getBidType());
//                vo.setAssetTypes(StringUtils.isNotEmpty(info.getAssetsType()) ? info.getAssetsType().split(",") : new String[0]);
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
                    if (info.getFundraisDate()!=null&&(info.getFundraisDate().getTime() > DateUtil.nowDate().getTime())) {
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
                vo.setLawFiles(bidInfoService.getBidPublicAccessoryFiles(Integer.valueOf(id)));
                vo.setLawFileUrl(bidInfoService.getBidPublicAccessoryFileUrl(Integer.valueOf(id)));
                //服务协议
                //String fwxy = bidInfoService.getGroupItemValue(Integer.parseInt(bidId), InterfaceConst.BID_EXTEND_ASSIGNMENT_CODE);
                vo.setFwxyUrl(userInfoService.getUserServiceAgreementUrl(Integer.valueOf(StringUtils.isBlank(userId) ? "-1" : userId)));
                //组信息
//                List<BidExtendGroupVO> groupInfoList = this.bidInfoService.getGroupInfoList(Integer.parseInt(id));

//                vo.setGroupInfoList(groupInfoList);
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
                if (info.getPanicBuyingTime() != null) {
                    if (bidLabels != null && bidLabels.size() > 0) {
                        bidLabels = bidLabels.subList(0, bidLabels.size() >= 1 ? 1 : bidLabels.size());
                        vo.setBidLabel(bidLabels);
                    }
                }else {
                    if (bidLabels != null && bidLabels.size() > 0) {
                        bidLabels = bidLabels.subList(0, bidLabels.size() >= 2 ? 2 : bidLabels.size());
                        vo.setBidLabel(bidLabels);
                    }
                }
                List<String> markList = new ArrayList<>();
                if (info.getAccumulatedIncome() != null) {
                    markList.add("累计收益达" + df.format(Double.valueOf(info.getAccumulatedIncome())) + "元客户专享");
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

                //借款人信息
                BidBorrowerInfo bbInfo = bidInfoService.getBidBorrowerInfo(Integer.valueOf(bidId)); //获取新的数据
                vo.setHistoryInfoFlag(true);
                if(bbInfo != null){
                    vo.setBase(bbInfo.getBorrower_info());
                    vo.setCredit(bbInfo.getCredit());
                    vo.setBankTransaction(bbInfo.getBank_transaction());
                    vo.setRisk(bbInfo.getRisk());
                    vo.setHistoryInfoFlag(false);
                }
                List ignore = new ArrayList();
                ignore.add("groupInfoList");
                ignore.add("assetTypes");
                response.setData(CommonTool.toMapDefaultNull(vo,ignore));
            } else if (!StringUtils.isBlank(planId)) {
                PlanDetailVO vo = new PlanDetailVO();
                info.setStatus(StatusUtil.status(info.getStatus()));
			/*info.setStatus(planStaus(Integer.valueOf(planId)));*/
				/*if (info.getPlanType().equals("2") && !info.getIsNoviceBid().equals("S")) {
					if (info.getMonth() != 0 && info.getMonth() > 1) {
						vo.setPlanCanQuit(1);
					} else if (info.getLoanDays() != 0 && info.getLoanDays() > 30) {
						vo.setPlanCanQuit(1);
					}

				} else if (info.getPlanType().equals("1") && !info.getIsNoviceBid().equals("S")) {
					vo.setPlanCanQuit(1);
				}*/
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
			/*List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(Integer.valueOf(planId),Integer.MAX_VALUE);
			double voteAmount = 0.00;
			for(PlanBidsStatus planBidsStatus : planBidsList) {
				if (planBidsStatus.getStatus().equals("TBZ") && Double.valueOf(planBidsStatus.getSurplusAmount()) >= 100) {
					voteAmount = voteAmount+Double.valueOf(planBidsStatus.getSurplusAmount());
				}
			}*/
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
                vo.setLawFiles(bidInfoService.getBidPublicAccessoryFiles(Integer.valueOf(id)));
                vo.setLawFileUrl(bidInfoService.getBidPublicAccessoryFileUrl(Integer.valueOf(id)));
                //服务协议
                //List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(Integer.valueOf(id));
                String fwxy = bidInfoService.getGroupItemValue(info.getId(), InterfaceConst.BID_EXTEND_ASSIGNMENT_CODE);
                if(versionType.getCode().equals("CG")){
                    vo.setFwxyUrl(userInfoService.getUserServicePlanAgreementUrlByCG(Integer.valueOf(StringUtils.isBlank(userId) ? "-1" : userId), info.getIsNoviceBid(), info.getPlanType()));
                }else {
                    vo.setFwxyUrl(userInfoService.getUserServicePlanAgreementUrl(Integer.valueOf(StringUtils.isBlank(userId) ? "-1" : userId), info.getIsNoviceBid(), info.getPlanType()));

                } //组信息
                //List<BidExtendGroupVO> groupInfoList = this.bidInfoService.getGroupInfoList(Integer.parseInt(id));
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
                //vo.setBidLabel(StringUtils.isNotEmpty(info.getBidLabel()) ? info.getAssetsType().split(",") : new String[0]);
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
                if (info.getPanicBuyingTime() != null) {
                    if (bidLabels != null && bidLabels.size() > 0) {
                        bidLabels = bidLabels.subList(0, bidLabels.size() >= 1 ? 1 : bidLabels.size());
                        vo.setBidLabel(bidLabels);
                    }
                }else {
                    if (bidLabels != null && bidLabels.size() > 0) {
                        bidLabels = bidLabels.subList(0, bidLabels.size() >= 2 ? 2 : bidLabels.size());
                        vo.setBidLabel(bidLabels);
                    }
                }
                List<String> markList = new ArrayList<>();
                if (info.getAccumulatedIncome() != null) {
                    markList.add("累计收益达" + df.format(Double.valueOf(info.getAccumulatedIncome())) + "元客户专享");
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
                response.setData(CommonTool.toMapDefaultNull(vo));
            }

            return response;
        } else {
            return bidInfoController.getBidDetail(paramForm, userId, bidId, planId);
        }
    }

    /**
     * 标的投资记录
     *
     * @param paramForm
     * @param bidId     标的ID
     * @param timestamp 投资时间排序
     * @param isUp
     * @return
     */
    @RequestMapping(value = "invest/records", method = RequestMethod.GET)
    HttpResponse bidRecords(@ModelAttribute BaseRequestForm paramForm,
                            @RequestParam(required = false, value = "bidId") String bidId,
                            @RequestParam(required = false, value = "timestamp") String timestamp,
                            @RequestParam(required = false, value = "isUp") String isUp) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate() || !StringUtils.isNoneEmpty(bidId, isUp)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        List<BidInvestRecordsVO> bidInvestRecordsList = new ArrayList<BidInvestRecordsVO>();
        List<InvestRecordsVO> investList = bidInfoService.getBidInvestRecords(bidId, timestamp, isUp);
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
            if (record.getPrice().compareTo(new BigDecimal(1)) < 0) {
                vo.setInvestAmount(1f);
            } else {
                vo.setInvestAmount(record.getPrice().intValue());
            }
            vo.setInvestTime(record.getTimestamp().getTime() / 1000);
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
     *
     * @param paramForm
     * @param bidId     ；标的ID
     * @return
     */
    @RequestMapping(value = "invest/allRecords", method = RequestMethod.GET)
    HttpResponse bidAllRecords(@ModelAttribute BaseRequestForm paramForm, String bidId) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate()) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        List<BidInvestRecordsVO> bidInvestRecordsList = new ArrayList<BidInvestRecordsVO>();
        List<InvestRecordsVO> investList = bidInfoService.getBidInvestAllRecords(bidId);
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
            if (record.getPrice().compareTo(new BigDecimal(1)) < 0) {
                vo.setInvestAmount(1f);
            } else {
                vo.setInvestAmount(record.getPrice().intValue());
            }
            vo.setInvestTime(record.getTimestamp().getTime() / 1000);
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
     *
     * @return
     */
    @RequestMapping(value = "borrower/info", method = RequestMethod.GET)
    HttpResponse borrowerInfo(@ModelAttribute BaseRequestForm paramForm, String bidId) throws Exception {
        HttpResponse response = new HttpResponse();
        //参数验证
        if (!paramForm.validate() || StringUtils.isEmpty(bidId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }

        BidBorrowerInfo bbInfo = bidInfoService.getBidBorrowerInfo(Integer.valueOf(bidId)); //获取新的数据
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

        for (BidFiles bidFiles : accessoryList) {
            BidPublicAccessoryFilesVO vo = new BidPublicAccessoryFilesVO();
            vo.setRiskId(bidFiles.getTypeId());
            vo.setTitle(bidFiles.getTypename());
            List<String> flist = bidFiles.getFileCodes();
            String[] contractUrls = new String[flist.size()];
            for (int i = 0; i < flist.size(); i++) {
                String fileUrl = FileUtils.getPicURL(flist.get(i), Config.get("contact.url"));
                contractUrls[i] = fileUrl;
            }
            String fileUrl = Config.get("contactInfo.url");
            if (contractUrls != null && contractUrls.length > 0) {
                fileUrl += URLEncoder.encode(JSONArray.toJSONString(contractUrls), "UTF-8");
            }
            vo.setUrl(fileUrl + "&title=" + URLEncoder.encode(bidFiles.getTypename(), "UTF-8"));
            voList.add(vo);
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
    HttpResponse directionalValidate(@ModelAttribute BaseRequestFormExtend paramForm, String bidId, String planId,VersionTypeEnum versionType) throws Exception {
        HttpResponse response = new HttpResponse();
        //参数验证
        if (!paramForm.validate() || (StringUtils.isEmpty(bidId) && StringUtils.isEmpty(planId))) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }
        versionType = versionType == null ? VersionTypeEnum.PT : versionType;
        int productId = 0;
        try{
            //需要验证是否符合条件
            if (StringUtils.isNotEmpty(bidId)) {
                productId = IntegerParser.parse(bidId);
                bidInfoService.checkCanInvestBid(productId, paramForm.getUserId(),versionType.getCode());
            }
            if (StringUtils.isNotEmpty(planId)) {
                productId = IntegerParser.parse(planId);
                planInfoService.checkCanInvest(productId, paramForm.getUserId(),versionType);
            }
        }catch (BusinessException busi){
            response.setCodeMessage(busi);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        response.setData(map);
        return response;
    }

    private String planStaus(int PlanId) {
        List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(PlanId, Integer.MAX_VALUE);
        String status = "";
        int i = 0;
        for (PlanBidsStatus planBidsStatus : planBidsList) {
            if (planBidsStatus.getStatus().equals("TBZ") && Double.valueOf(planBidsStatus.getSurplusAmount()) >= 100) {
                status = "TBZ";
                break;
            } else if (planBidsStatus.getStatus().equals("DFK") || (planBidsStatus.getStatus().equals("TBZ") && Double.valueOf(planBidsStatus.getSurplusAmount()) < 100)) {
                status = "DFK";
                if (i > 0) {//当有些标处于放款，有些标未放款
                    break;
                }
            } else if (planBidsStatus.getStatus().equals("HKZ")) {
                status = "HKZ";
                i = i + 1;
				/*break;*/
            } else if (planBidsStatus.getStatus().equals("YLB")) {
                status = status;//流标的情况维持原状态不变
            } else {
                status = "YJQ";
            }
        }
        return status;
    }
}
