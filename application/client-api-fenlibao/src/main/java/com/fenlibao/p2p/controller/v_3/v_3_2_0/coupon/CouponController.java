package com.fenlibao.p2p.controller.v_3.v_3_2_0.coupon;

import com.fenlibao.p2p.dao.bid.BidInfoDao;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.bid.BidBaseInfo;
import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.entity.bid.Plan;
import com.fenlibao.p2p.model.entity.coupons.UserCouponInfo;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.bid.PlanService;
import com.fenlibao.p2p.service.coupon.CouponService;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.axis.utils.StringUtils;
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

@RestController("v_3_2_0/CouponController")
@RequestMapping(value = "coupons", headers = APIVersion.v_3_2_0)
public class CouponController {
    private static final Logger logger= LogManager.getLogger(CouponController.class);

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private CouponService couponservice;
    @Resource
    private RedpacketService redpacketService;
    @Resource
    private BidInfoService bidInfoService;
    @Resource
    private BidInfoDao bidInfoDao;
    @Resource
    private PlanService planService;

    /**
     * 加息券列表
     * @param status //查询加息券状态1:未使用 2:已使用  3:已过期
     * @return HttpResponse
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public HttpResponse getList(BaseRequestFormExtend params,
                                Integer page,Integer limit,String status) {
        HttpResponse response = new HttpResponse();
        if (!params.validate() || StringUtils.isEmpty(status)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }

        if(page==null||page<1)page=1;
        if(limit==null)limit=InterfaceConst.PAGESIZE;

        Map<String, Object> paramMap=new HashMap<>();
        paramMap.put("userId",params.getUserId());
        paramMap.put("status",status);
        paramMap.put("page",(page-1)*limit);
        paramMap.put("limit",limit);

        List<UserCouponInfo> coupons=couponservice.getCoupons(paramMap);

        Map<String, Object> map=new HashMap<>();
        map.put("items",coupons);
        response.setData(map);
        return response;
    }

    /**
     * 加息券列表-投资使用
     * @param bidId //标id
     * @return HttpResponse
     */
    @RequestMapping(value = "addinterest/list", method = RequestMethod.GET)
    public HttpResponse getAddinterestList(BaseRequestFormExtend params,
                                Integer page,Integer limit,Integer bidId, Integer planId, Integer newPlan) {
        HttpResponse response = new HttpResponse();
        if (!params.validate() || (bidId==null && planId==null)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }

        //不能同时使用
        if(bidId!=null && planId!=null){
            response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
            return response;
        }

        if(bidId!=null){
            BidBaseInfo bidBaseInfo=bidInfoDao.getBidBaseInfo(bidId);
            if(bidBaseInfo == null || "S".equals(bidBaseInfo.getIsNoviceBid())){
                return response;
            }
        }

        if(planId!=null){
            if(newPlan != null && newPlan == 1){
                InvestPlan investPlan = planService.getInvestPlan(planId);
                if(investPlan == null || investPlan.getIsNovice() == 1){
                    return response;
                }
            }else{
                Plan plan = planService.getPlan(planId);
                if(plan == null || "S".equals(plan.getIsNoviceBid())){
                    return response;
                }
            }
        }

        if(page==null||page<1)page=1;
        if(limit==null)limit=InterfaceConst.PAGESIZE;

        Map<String, Object> paramMap=new HashMap<>();
        paramMap.put("userId",params.getUserId());
        paramMap.put("bidId",bidId);
        paramMap.put("planId",planId);
        paramMap.put("newPlan",newPlan);
        paramMap.put("page",(page-1)*limit);
        paramMap.put("limit",limit);

        List<UserCouponInfo> coupons=couponservice.getAddinterestList(paramMap);

        Map<String, Object> map=new HashMap<>();
        map.put("items",coupons);
        response.setData(map);
        return response;
    }
    /**
     *  获取用户可使用投资券数量
     * @param bidId //标id
     * @return HttpResponse
     */
    @RequestMapping(value = "usable/quantity", method = RequestMethod.GET)
    public HttpResponse getCouponsCount(BaseRequestFormExtend params, Integer bidId, Integer planId,String newPlan) {
        HttpResponse response = new HttpResponse();
        if (!params.validate()&& StringUtils.isEmpty(newPlan)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }

        if (StringUtils.isEmpty(newPlan)) {
            newPlan = "0";
        }
        //不能同时使用
        if(bidId!=null && planId!=null){
            response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
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

        if(planId!=null&&newPlan.equals("0")){
            Plan plan = planService.getPlan(planId);
            if("S".equals(plan.getIsNoviceBid())){
                Map<String, Object> map=new HashMap<>();
                map.put("redEnvelopeQty",0);
                map.put("interestRiseQty",0);
                response.setData(map);
                return response;
            }
        }

        if (planId != null && newPlan.equals("1")) {
            InvestPlan investPlan = planService.getInvestPlan(planId);
            if ("1".equals(investPlan.getIsNovice())) {
                Map<String, Object> map=new HashMap<>();
                map.put("redEnvelopeQty",0);
                map.put("interestRiseQty",0);
                response.setData(map);
                return response;
            }
        }



        Map<String, Object> paramMap=new HashMap<>();
        paramMap.put("userId",params.getUserId());
        paramMap.put("bidId",bidId);
        paramMap.put("planId",planId);
        paramMap.put("newPlan",Integer.valueOf(newPlan));
        //加息卷数量
        Integer interestRiseQty=couponservice.getCouponsCount(paramMap);

        //根据条件查询用户可以使用的红包
        paramMap = new HashMap();
        paramMap.put("userId",params.getUserId());
        paramMap.put("status",1);
        paramMap.put("bidId",bidId);
        paramMap.put("planId",planId);
        paramMap.put("newPlan", Integer.valueOf(newPlan));
        int redpacketCount = redpacketService.getRedpacketCount(paramMap);

        Map<String, Object> map=new HashMap<>();
        map.put("redEnvelopeQty",redpacketCount);
        map.put("interestRiseQty",interestRiseQty);
        response.setData(map);
        return response;
    }

    /**
     * 获取赠送加息券短信内容
     * @param params
     * @return
     */
    @RequestMapping(value = "addinterest/sms", method = RequestMethod.GET)
    public HttpResponse getAddinterestSms(BaseRequestFormExtend params){
        HttpResponse response = new HttpResponse();
        try{
            if (!params.validate()) {
                response.setCodeMessage(ResponseCode.EMPTY_PARAM);
                return response;
            }
            String userId = params.getUserId().toString();
            UserInfo userInfo = userInfoService.getUser(null, null, userId);
            if (userInfo == null) {
                throw new BusinessException(ResponseCode.USER_NOT_EXIST);
            }
            if (userInfo.getFullName()==null) {
                throw new BusinessException(ResponseCode.USER_IDENTITY_UNAUTH);
            }
            String userName = URLEncoder.encode(userInfo.getFullName(),"UTF-8");
            String inviteUrl = Config.get("invite.url").replace("#{phone}",userInfo.getPhone()).replace("#{name}",userName);
            String inviteShortUrl= CommonTool.genShortUrl(inviteUrl);
            String smsContent=String.format(Config.get("addinterest.grant.sms"),
                    userInfo.getFullName(), inviteShortUrl).replaceAll("#", "%");
            Map<String,Object> map=new HashMap();
            map.put("smsContent",smsContent);
            response.setData(map);
            return response;
        }catch(Exception ex){
            logger.error("getAddinterestSms fail", ex);
            response.setCodeMessage(ResponseCode.FAILURE);
            return response;
        }
    }

}
