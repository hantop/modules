package com.fenlibao.p2p.controller.v_4.v_4_0_0.bid;

import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.entity.ExperienceGoldInfo;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.enums.SpecialUserType;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.channel.RegisterChannelVO;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.bid.BidService;
import com.fenlibao.p2p.service.bid.IUserDmService;
import com.fenlibao.p2p.service.bid.PlanExtService;
import com.fenlibao.p2p.service.common.RedisService;
import com.fenlibao.p2p.service.experiencegold.IExperienceGoldService;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.service.trade.bid.BidManageService;
import com.fenlibao.p2p.service.trade.coupon.CouponManageService;
import com.fenlibao.p2p.service.user.RiskTestService;
import com.fenlibao.p2p.service.user.SpecialUserService;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.service.xinwang.bid.XWBidService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Sender;
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
import java.util.*;

/**
 * Created by LouisWang on 2015/8/14.
 */
@RestController("v_4_0_0/BidController")
@RequestMapping(value = "bid", headers = APIVersion.v_4_0_0)
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
    @Resource
    CouponManageService couponManageService;
    @Resource
    BidManageService bidManageService;
    @Resource
    XWUserInfoService xwUserInfoService;
    @Resource
    XWBidService xwBidService;
    @Resource
    PlanExtService planExtService;
    @Resource
    private SpecialUserService specialUserService;
    @Resource
    private RedpacketService redpacketService;
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
                               String isCheck,String fxhbIds,String jxqId,String dealPassword,String versionType)  throws Throwable{
        HttpResponse apiResponse = new HttpResponse();
        //参数验证
        if(!paramForm.validate() || StringUtils.isBlank(bidId)
                || StringUtils.isBlank(sgSum)||StringUtils.isBlank(versionType)){
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
        int cgNum = VersionTypeEnum.PT.getIndex();
        if(StringUtils.isNotEmpty(versionType)&&versionType.equals(VersionTypeEnum.CG.getCode())){
            cgNum = VersionTypeEnum.CG.getIndex();
        }

        try {
            //判断是否为存管项目
            if (cgNum == VersionTypeEnum.CG.getIndex() && bidInfoService.isCGBid(Integer.valueOf(bidId)) != 2) {
                apiResponse.setCodeMessage(ResponseCode.BID_NOT_CG);
                return apiResponse;
            }

            //判断是否为存管用户
            if (cgNum == VersionTypeEnum.CG.getIndex() && StringUtils.isEmpty(userInfoService.isXWaccount(paramForm.getUserId()))) {
                apiResponse.setCodeMessage(ResponseCode.USER_NOT_XW_ACCOUNT);
                return apiResponse;
            }

            //风险测试校验 Mingway 20161221
            if (!riskTestService.queryHadTestedByUid(paramForm.getUserId())) {
                apiResponse.setCodeMessage(ResponseCode.USER_RISK_UNTEST);
                return apiResponse;
            }



            //kdbPlantId = AES.getInstace().decrypt(kdbPlantId);
            //sgSum = AES.getInstace().decrypt(sgSum);
            if (cgNum != VersionTypeEnum.CG.getIndex()) {
                //未实名认证
                if (!userDmService.isSmrz(paramForm.getUserId())) {
                    apiResponse.setCodeMessage(ResponseCode.USER_IDENTITY_UNAUTH);
                    return apiResponse;
                }
            }
            BigDecimal amount = BigDecimalParser.parse(sgSum);  //投资金额

            //整数判断
            BigDecimal amountTemp = amount.stripTrailingZeros();
            if (amountTemp.toPlainString().contains(".")) {
                throw new BusinessException(ResponseCode.BID_INVESTMENT_AMOUNT_INTEGER); //投标金额必须为整数
            }
            int loanId = IntegerParser.parse(bidId);   // 标的ID

            //需要验证是否符合条件
            bidInfoService.checkCanInvestBid(loanId, paramForm.getUserId(),versionType);
            //如果是存管跳过密码验证
            if (cgNum != VersionTypeEnum.CG.getIndex()) {
                //首先验证交易密码是否正确
                if (!tradeService.isValidUserPwd(paramForm.getUserId(), dealPassword)) {
                    apiResponse.setCodeMessage(ResponseCode.USER_TRADE_PASSWORD_ERROR);//交易密码错误
                    return apiResponse;
                }
            }
            /*BigDecimal amount = BigDecimalParser.parse(sgSum);  //投资金额*/
            if (cgNum != VersionTypeEnum.CG.getIndex()) {
                //wangyunjing 20151019 添加返现红包功能
                bidService.doBid(loanId, amount, paramForm.getUserId(), isCheck, fxhbIds, jxqId);
            } else {
                if (!specialUserService.isSpecial(String.valueOf(paramForm.getUserId()), SpecialUserType.ALL)) { //特殊用户不进行校验
                    // 校验最低起投金额
                    BigDecimal min = new  BigDecimal(InterfaceConst.BID_MIX_AMOUNT);  //PS:这里应该是动态获取资源
                    if (amount.compareTo(min) < 0) {
                        throw new BusinessException(ResponseCode.BID_INVESTMENT_AMOUNT_LT100); //投标金额不能低于最低起投金额
                    }
                }

                XWFundAccount xwUserInfo = xwUserInfoService.getFundAccount(paramForm.getUserId(), SysFundAccountType.XW_INVESTOR_WLZH);
                if (xwUserInfo == null) {
                    apiResponse.setCodeMessage(XWResponseCode.USER_XW_ACCOUNT_NOT_EXIST);
                    return apiResponse;
                }
                String[] redpacketIdsArr = StringUtils.isBlank(fxhbIds) ? null : fxhbIds.split("\\|");
                xwUserInfoService.validateAmount(xwUserInfo, amount);
                Integer userJxqId = null;
                if (StringUtils.isNotEmpty(jxqId)) {
                    userJxqId = Integer.valueOf(jxqId);
                }
                couponManageService.validateUserCoupons(paramForm.getUserId(), loanId, amount, userJxqId, redpacketIdsArr);
                int orderId = bidManageService.tender(loanId, amount, paramForm.getUserId());
                xwBidService.doBid(orderId, userJxqId,redpacketIdsArr);
                UserInfo userInfo = userInfoService.getUserInfo(paramForm.getUserId());
                if (redpacketIdsArr != null) {
                    //投资成功使用返现
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("fxhbIdArr", redpacketIdsArr);
                    List<UserRedPacketInfo> userRedPacketInfos = redpacketService.getBidRedpacket(params);
                    //消费返现红包
                    redpacketService.grantRedPackets(orderId, userRedPacketInfos, String.valueOf(paramForm.getUserId()), userInfo.getPhone(), FeeCode.REGISTERRETURNCACH_REDPACKET, VersionTypeEnum.CG);
                }
                try {
                    ShopTreasureInfo bidInfo = bidInfoService.getBidInfo(loanId);
                    String content = Sender.get("sms.dobid.content").replace("#{bidTitle}", bidInfo.getName()).replace("#{investSum}", amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    planExtService.sendSmsAndLetter(userInfo.getPhone(), String.valueOf(userInfo.getUserId()), content, VersionTypeEnum.CG);
                } catch (Throwable t) {
                    logger.error("投资计划后短信和站内信异常", t);
                }

            }

        } catch (APIException apie) {
            logger.debug("投资失败，bidId=[{}],投资金额=[{}],是否使用红包=[{}],userId=[{}],msg=[{}]", bidId, sgSum, fxhbIds, paramForm.getUserId(), apie.getMessage());
            apiResponse.setCodeMessage(apie);
        } catch (BusinessException busi) {
            logger.debug("投资失败，bidId=[{}],投资金额=[{}],是否使用红包=[{}],userId=[{}],msg=[{}]", bidId, sgSum, fxhbIds, paramForm.getUserId(), busi.getMessage());
            apiResponse.setCodeMessage(busi);
        } catch (Throwable throwable) {
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
