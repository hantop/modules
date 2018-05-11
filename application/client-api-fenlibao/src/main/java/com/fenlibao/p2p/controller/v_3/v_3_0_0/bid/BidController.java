package com.fenlibao.p2p.controller.v_3.v_3_0_0.bid;

import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.p2p.model.entity.ExperienceGoldInfo;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.channel.RegisterChannelVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.bid.BidService;
import com.fenlibao.p2p.service.bid.IUserDmService;
import com.fenlibao.p2p.service.common.RedisService;
import com.fenlibao.p2p.service.experiencegold.IExperienceGoldService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.service.user.RiskTestService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.redis.RedisConst;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by LouisWang on 2015/8/14.
 */
@RestController("v_3_0_0/BidController")
@RequestMapping(value = "bid", headers = APIVersion.v_3_0_0)
public class BidController {
    private static final Logger logger= LogManager.getLogger(BidController.class);

    @Resource
    private BidService bidService;
    @Resource
    private IUserDmService userDmService;
    @Resource
    private ITradeService tradeService;
    @Resource
    private BidInfoService bidInfoService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private IExperienceGoldService experienceGoldService;
    @Resource
    private RedisService redisService;
    @Resource
    private RiskTestService riskTestService;


    /**
     *  投标
     * @param paramForm//
     * @param bidId//标id
     * @param sgSum//申购金额
     * @param isCheck//体验金的判断没有为空
     * @param fxhbIds//是否使用红包
     * @param dealPassword//交易密码
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "doBid", method = RequestMethod.POST)
    public HttpResponse doBid( @ModelAttribute BaseRequestFormExtend paramForm, String bidId,String sgSum,
                               String isCheck,String fxhbIds,String jxqId,String dealPassword)  throws Throwable{
        HttpResponse apiResponse = new HttpResponse();
        //参数验证
        if(!paramForm.validate() || StringUtils.isBlank(bidId)
                || StringUtils.isBlank(sgSum)){
            apiResponse.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return apiResponse;
        }
        //红包和加息卷不能同时使用
        if(!StringUtils.isBlank(fxhbIds) && !StringUtils.isBlank(jxqId)){
            apiResponse.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
            return apiResponse;
        }
        String requestCacheKey = RedisConst.$REQUEST_CACHE_KEY_USERID.concat(paramForm.getUserId().toString());
        if (redisService.existsKey(requestCacheKey)) {
            apiResponse.setCodeMessage(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT);
            return apiResponse;
        }
        try{
            //风险测试校验 Mingway 20161221
            if(!riskTestService.queryHadTestedByUid(paramForm.getUserId())){
                apiResponse.setCodeMessage(ResponseCode.USER_RISK_UNTEST);
                return apiResponse;
            }
            //kdbPlantId = AES.getInstace().decrypt(kdbPlantId);
            //sgSum = AES.getInstace().decrypt(sgSum);
            //未实名认证
            if(!userDmService.isSmrz(paramForm.getUserId())){
                apiResponse.setCodeMessage(ResponseCode.USER_IDENTITY_UNAUTH);
                return apiResponse;
            }

            int loanId = IntegerParser.parse(bidId);   // 标的ID

            //需要验证是否符合条件
            bidInfoService.checkCanInvestBid(loanId,paramForm.getUserId(),"PT");
            //首先验证交易密码是否正确
            if(!tradeService.isValidUserPwd(paramForm.getUserId(), dealPassword)){
                apiResponse.setCodeMessage(ResponseCode.USER_TRADE_PASSWORD_ERROR);//交易密码错误
                return apiResponse;
            }

            BigDecimal amount = BigDecimalParser.parse(sgSum);  //投资金额

//            boolean isNovice = bidInfoService.isNovice(paramForm.getUserId()); //这个判断要在投资前！！！！

            //wangyunjing 20151019 添加返现红包功能
            bidService.doBid(loanId, amount, paramForm.getUserId(),isCheck,fxhbIds,jxqId);

//            if (isNovice) {
//                grantExperienceGold(paramForm.getUserId(),bidId,amount);
//            }
        } catch (BusinessException busi) {
            logger.debug("投资失败，bidId=[{}],投资金额=[{}],是否使用红包=[{}],userId=[{}],msg=[{}]", bidId, sgSum, fxhbIds, paramForm.getUserId(),busi.getMessage());
            apiResponse.setCodeMessage(busi);
        } catch (Throwable throwable){
            logger.error("投资失败，bidId=[{}],投资金额=[{}],是否使用红包=[{}],userId=[{}]", bidId, sgSum, fxhbIds, paramForm.getUserId());
            logger.error("投资失败", throwable);
            apiResponse.setCodeMessage(ResponseCode.FAILURE);
        } finally {
            redisService.removeKey(requestCacheKey);
        }
        return apiResponse;
    }

    /**
     * 新手投资送体验金
     */
    public void grantExperienceGold(int userId, String bidId, BigDecimal amount) {
        try {
            List<ExperienceGoldInfo> experienceGoldInfoList = experienceGoldService.getActivityByType(InterfaceConst.INVESTMENT_EXPERIENCEGOLD);
            if (experienceGoldInfoList != null && experienceGoldInfoList.size() > 0) {
                logger.debug("体验金size >>> " + experienceGoldInfoList.size());
                UserInfo userInfo = userInfoService.getUser(null, null, String.valueOf(userId));
                Date registerTime = userInfo.getRegisterTime();
                String grantDate = Config.get("investment.grant.expgold.date.start");
                logger.debug("registerTime=[{}], grantDate=[{}]", registerTime, grantDate);
                if (DateUtil.StringToDate(grantDate, DateUtil.DATE_FORMAT).before(registerTime)) {
                    RegisterChannelVO channel = userInfoService.getChannelInfo(userId);
                    if (channel != null && channel.getParentId() != null) {
                        String parentId = channel.getParentId().toString();
                        String parentIds = Config.get("investment.not.grant.expgold.parentIds");
                        logger.debug("parentId=[{}], parentIds=[{}]", parentId, parentIds);
                        String[] parentIdsArr = parentIds.split(",");
                        List<String> list = Arrays.asList(parentIdsArr);
                        if (!list.contains(parentId)) { //注册渠道非商务合作的才送
                            experienceGoldService.addUserExperienceGold(experienceGoldInfoList, String.valueOf(userId), userInfo.getPhone());
                        }
                    } else { //运营需求，当渠道来源没有的时候，默认为官网
                        logger.info("渠道为空或父渠道为空, userId[{}]", userId);
                        experienceGoldService.addUserExperienceGold(experienceGoldInfoList, String.valueOf(userId), userInfo.getPhone());
                    }
                }
            }
        } catch (Exception e) { //这里catch，失败不影响投资，补发
            logger.error("新手投资送体验金失败 >>> userId=[{}],bidId=[{}],amount=[{}]", userId, bidId, amount);
            logger.error("新手投资送体验金失败", e);
        }
    }
}
