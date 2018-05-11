package com.fenlibao.p2p.controller.v_1.v_1_0_0.couponController;

import com.fenlibao.p2p.dao.bid.BidInfoDao;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.bid.BidBaseInfo;
import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.entity.bid.Plan;
import com.fenlibao.p2p.model.entity.coupons.UserCouponInfo;
import com.fenlibao.p2p.model.entity.coupons.UserCouponStatisticsInfo;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.BaseRequestFormExtend;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.PlanService;
import com.fenlibao.p2p.service.coupon.CouponService;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.paginator.domain.Pager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("v_1_0_0/CouponController")
@RequestMapping("coupons")
public class CouponController {
    private static final Logger logger = LogManager.getLogger(CouponController.class);

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private CouponService couponservice;
    @Resource
    private RedpacketService redpacketService;
    @Resource
    private BidInfoDao bidInfoDao;
    @Resource
    private PlanService planService;

    /**
     * 加息券列表
     *
     * @param status //查询加息券状态1:未使用 2:已使用  3:已过期
     * @return HttpResponse
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public HttpResponse getList(BaseRequestFormExtend params,
                                Integer page, Integer limit, String status) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!params.validate() || StringUtils.isEmpty(status)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }

        if (page == null || page < 1) page = 1;
        if (limit == null) limit = InterfaceConst.PAGESIZE;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", params.getUserId());
        paramMap.put("status", status);
        paramMap.put("page", (page - 1) * limit);
        paramMap.put("limit", limit);

        List list = couponservice.getCoupons(paramMap);
        int totalCount = couponservice.getMyConponsCount(paramMap);

        Pager pager = new Pager();
        pager.setTotalCount(totalCount);
        pager.setPage(page);
        pager.setLimit(limit);
        pager.setTotalPages(totalCount%limit==0?totalCount/limit:totalCount/limit+1);
        pager.setItems(list);
        response.setData(CommonTool.toMap(pager));
        return response;
    }

    /**
     * 加息券列表-投资使用
     *
     * @param bidId //标id
     * @return HttpResponse
     */
    @RequestMapping(value = "addinterest/list", method = RequestMethod.GET)
    public HttpResponse getAddinterestList(BaseRequestFormExtend params,
                                           String page, String limit, String bidId, String planId, Integer newPlan) {

        Integer thisPage = Integer.parseInt(page);
        Integer thisLimit = Integer.parseInt(limit);


        HttpResponse response = new HttpResponse();
        if (!params.validate()) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }

        //不能同时使用
        if((StringUtils.isNotBlank(bidId)&& StringUtils.isNotBlank(planId))
                || (StringUtils.isBlank(bidId) && StringUtils.isBlank(planId))){
            response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
            return response;
        }

        Integer thisBidId = StringUtils.isNotBlank(bidId) ? Integer.parseInt(bidId) : null;
        Integer thisPlanId = StringUtils.isNotBlank(planId) ? Integer.parseInt(planId) : null;

        if(StringUtils.isNotBlank(bidId)){
            BidBaseInfo bidBaseInfo=bidInfoDao.getBidBaseInfo(Integer.parseInt(bidId));
            if("S".equals(bidBaseInfo.getIsNoviceBid())){
                return response;
            }
        }

        if(StringUtils.isNotBlank(planId)){
            if(newPlan == 1){
                InvestPlan investPlan = planService.getInvestPlan(Integer.parseInt(planId));
                if(investPlan.getIsNovice() == 1){
                    return response;
                }
            }else{
                Plan plan = planService.getPlan(Integer.parseInt(planId));
                if("S".equals(plan.getIsNoviceBid())){
                    return response;
                }
            }
        }

        if (thisPage == null || thisPage < 1) thisPage = 1;
        if (thisLimit == null) thisLimit = InterfaceConst.PAGESIZE;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", params.getUserId());
        paramMap.put("bidId", thisBidId);
        paramMap.put("planId", thisPlanId);
        paramMap.put("newPlan",newPlan);
        paramMap.put("page", (thisPage - 1) * thisLimit);
        paramMap.put("limit", thisLimit);

        List<UserCouponInfo> coupons = couponservice.getAddinterestList(paramMap);

        Map<String, Object> map = new HashMap<>();
        map.put("items", coupons);
        response.setData(map);
        return response;
    }

    public static void main(String[] args) {
        System.out.println("xxxxx"+Integer.parseInt(""));
    }

    /**
     * 获取用户可使用投资券数量
     *
     * @param bidId //标id
     * @return HttpResponse
     */
    @RequestMapping(value = "usable/quantity", method = RequestMethod.GET)
    public HttpResponse getCouponsCount(BaseRequestFormExtend params, Integer bidId) {
        HttpResponse response = new HttpResponse();
        if (!params.validate()) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        if(bidId!=null){
            BidBaseInfo bidBaseInfo=bidInfoDao.getBidBaseInfo(bidId);
            if("S".equals(bidBaseInfo.getIsNoviceBid())){
                Map<String, Object> map=new HashMap<>();
                map.put("redEnvelopeQty",0);
                map.put("interestRiseQty",0);
                response.setData(map);
                return response;
            }
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", params.getUserId());
        paramMap.put("bidId", bidId);

        //加息卷数量
        Integer interestRiseQty = couponservice.getCouponsCount(paramMap);

        //根据条件查询用户可以使用的红包
        paramMap = new HashMap();
        paramMap.put("userId", params.getUserId());
        paramMap.put("status", 1);
        paramMap.put("bidId", bidId);
        int redpacketCount = redpacketService.getRedpacketCount(paramMap);

        Map<String, Object> map = new HashMap<>();
        map.put("redEnvelopeQty", redpacketCount);
        map.put("interestRiseQty", interestRiseQty);
        response.setData(map);
        return response;
    }

    /**
     * 获取赠送加息券短信内容
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "addinterest/sms", method = RequestMethod.GET)
    public HttpResponse getAddinterestSms(BaseRequestFormExtend params) {
        HttpResponse response = new HttpResponse();
        try {
            if (!params.validate()) {
                response.setCodeMessage(ResponseCode.EMPTY_PARAM);
                return response;
            }
            String userId = params.getUserId().toString();
            UserInfo userInfo = userInfoService.getUser(null, null, userId);
            if (userInfo == null) {
                throw new BusinessException(ResponseCode.USER_NOT_EXIST);
            }
            if (userInfo.getFullName() == null) {
                throw new BusinessException(ResponseCode.USER_IDENTITY_UNAUTH);
            }
            String userName = URLEncoder.encode(userInfo.getFullName(), "UTF-8");
            String inviteUrl = Config.get("invite.url").replace("#{phone}", userInfo.getPhone()).replace("#{name}", userName);
            String inviteShortUrl = CommonTool.genShortUrl(inviteUrl);
            String smsContent = String.format(Config.get("addinterest.grant.sms"),
                    userInfo.getFullName(), inviteShortUrl).replaceAll("#", "%");
            Map<String, Object> map = new HashMap();
            map.put("smsContent", smsContent);
            response.setData(map);
            return response;
        } catch (Exception ex) {
            logger.error("getAddinterestSms fail", ex);
            response.setCodeMessage(ResponseCode.FAILURE);
            return response;
        }
    }

    /**
     * 加息券统计
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "userCouponStatistics", method = RequestMethod.GET)
    public HttpResponse getUserCouponStatistics(BaseRequestFormExtend params) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!params.validate() ) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", params.getUserId());
        UserCouponStatisticsInfo ucsi= couponservice.getUserCouponStatistics(paramMap);
        response.setData(CommonTool.toMap(ucsi));
        return response;
    }
}
