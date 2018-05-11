package com.fenlibao.p2p.controller.v_1.v_1_0_0.bid;

import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.p2p.model.entity.ExperienceGoldInfo;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.channel.RegisterChannelVO;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.*;
import com.fenlibao.p2p.service.experiencegold.IExperienceGoldService;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.service.trade.bid.BidManageService;
import com.fenlibao.p2p.service.trade.coupon.CouponManageService;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;

import com.fenlibao.p2p.service.xinwang.bid.XWBidService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Sender;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by LouisWang on 2015/8/14.
 */
@RestController("v_1_0_0/BidController")
@RequestMapping("bid")
public class BidController {
    private static final Logger logger = LogManager.getLogger(BidController.class);

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
    private PreDoBidService preDoBidService;
    @Resource
    XWUserInfoService xwUserInfoService;
    @Resource
    XWBidService xwBidService;
    @Resource
    PlanExtService planExtService;
    @Resource
    CouponManageService couponManageService;
    @Resource
    BidManageService bidManageService;
    @Resource
    RedpacketService redpacketService;
    //投标
    @RequestMapping(value = "doBid", method = RequestMethod.POST, headers = APIVersion.V_1_0_0)
    public HttpResponse doBid(
            @ModelAttribute BaseRequestFormExtend paramForm,
            @RequestParam(required = true, value = "bidId") String bidId,
            @RequestParam(required = true, value = "sgSum") String sgSum,
            @RequestParam(required = false, value = "isCheck") String isCheck,   //体验金的判断没有为空
            @RequestParam(required = false, value = "fxhbIds") String fxhbIds,   //是否使用红包
            @RequestParam(required = false, value = "jxqId") String jxqId,   //是否使用加息券
            @RequestParam(required = false, value = "dealPassword") String dealPassword,  //交易密码
            @RequestParam(required = false, value = "versionType") VersionTypeEnum versionType) throws Throwable {
        HttpResponse apiResponse = new HttpResponse();
        //参数验证
        if (!paramForm.validate()
                || StringUtils.isBlank(bidId)
                || StringUtils.isBlank(sgSum)||StringUtils.isEmpty(versionType.getCode())) {
            apiResponse.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(), ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
            return apiResponse;
        }

        //红包和加息卷不能同时使用
        if(!StringUtils.isBlank(fxhbIds) && !StringUtils.isBlank(jxqId)){
            apiResponse.setCodeMessage(ResponseEnum.RESPONSE_INVALID_PARAM.getCode(), ResponseEnum.RESPONSE_INVALID_PARAM.getMessage());
            return apiResponse;
        }

        try {
            //kdbPlantId = AES.getInstace().decrypt(kdbPlantId);
            //sgSum = AES.getInstace().decrypt(sgSum);
            //判断是否为存管项目
            if (versionType.getCode().equals(VersionTypeEnum.CG.getCode()) && bidInfoService.isCGBid(Integer.valueOf(bidId)) != 2) {
                apiResponse.setCodeMessage(ResponseCode.BID_NOT_CG);
                return apiResponse;
            }

            //判断是否为存管用户
            if (versionType.getCode().equals(VersionTypeEnum.CG.getCode()) && StringUtils.isEmpty(userInfoService.isXWaccount(paramForm.getUserId()))) {
                apiResponse.setCodeMessage(ResponseCode.USER_NOT_XW_ACCOUNT);
                return apiResponse;
            }



            //未实名认证
           if (!versionType.getCode().equals( VersionTypeEnum.CG.getCode())) {
               if (!userDmService.isSmrz(paramForm.getUserId())) {
                   apiResponse.setCodeMessage(ResponseEnum.RESPONSE_BANK_CERTIFICATION.getCode(), ResponseEnum.RESPONSE_BANK_CERTIFICATION.getMessage());
                   return apiResponse;
               }
           }

            BigDecimal amount = BigDecimalParser.parse(sgSum);  //出借金额
            //整数判断
            BigDecimal amountTemp = amount.stripTrailingZeros();
            if (amountTemp.toPlainString().contains(".")) {
                throw new BusinessException(ResponseCode.BID_INVESTMENT_AMOUNT_INTEGER); //投标金额必须为整数
            }

            int loanId = IntegerParser.parse(bidId);   // 标的ID
            //需要验证是否符合条件
            boolean canInvest = preDoBidService.checkCanInvestBid(loanId,paramForm.getUserId(),versionType);

            if (!canInvest) {
                apiResponse.setCodeMessage(ResponseCode.BID_DIRECTIONAL_TOTAL_USER_ASSETS);
                return apiResponse;
            }

            //存管跳过密码验证
            if (!versionType.getCode().equals(VersionTypeEnum.CG.getCode())) {
                //首先验证交易密码是否正确
                if (!bidService.isValidPassword(paramForm.getUserId(), dealPassword)) {
                    apiResponse.setCodeMessage("40615", "交易密码不正确，建议您重置交易密码");//交易密码错误
                    return apiResponse;
                }
            }



//            boolean isNovice = bidInfoService.isNovice(paramForm.getUserId()); //这个判断要在出借前！！！！

            //wangyunjing 20151019 添加返现红包功能

            if (!versionType.getCode().equals(VersionTypeEnum.CG.getCode())) {
                //wangyunjing 20151019 添加返现红包功能
                bidService.doBid(loanId, amount, paramForm.getUserId(), isCheck, fxhbIds, jxqId);
            } else {
                BigDecimal min = new BigDecimal(InterfaceConst.BID_MIX_AMOUNT);  //PS:这里应该是动态获取资源
                if (amount.compareTo(min) < 0) {
                    throw new LogicalException("30606"); //投标金额不能低于最低起投金额
                }

                //XinwangUserInfo xwUserInfo = xwUserInfoService.queryUserInfo(UserRole.INVESTOR.getCode() + paramForm.getUserId());
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
                xwBidService.doBid(orderId,userJxqId,redpacketIdsArr);
                //获取用户信息
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("userId", paramForm.getUserId());
                UserInfo userInfo = userInfoService.getUserInfoByPhoneNumOrUsername(userMap);
                if (redpacketIdsArr != null) {
                    //出借成功使用返现
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("fxhbIdArr", redpacketIdsArr);
                    List<UserRedPacketInfo> userRedPacketInfos = redpacketService.getBidRedpacket(params);
                    //消费返现红包
                    redpacketService.grantRedPackets(orderId,userRedPacketInfos, String.valueOf(paramForm.getUserId()), userInfo.getPhone(), FeeCode.REGISTERRETURNCACH_REDPACKET, VersionTypeEnum.CG);
                }
                try {
                    ShopTreasureInfo bidInfo = bidInfoService.getBidInfo(loanId);
                    String content = Sender.get("sms.dobid.content").replace("#{bidTitle}", bidInfo.getName()).replace("#{investSum}", amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    planExtService.sendSmsAndLetter(userInfo.getPhone(), String.valueOf(userInfo.getUserId()), content, VersionTypeEnum.CG);
                } catch (Throwable t) {
                    logger.error("出借计划后短信和站内信异常", t);
                }

            }



//            if (isNovice) {
//                grantExperienceGold(paramForm.getUserId(), bidId, amount);
//            }
            apiResponse.setCodeMessage(ResponseEnum.RESPONSE_SUCCESS.getCode(), "出借成功!");

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            String message = throwable.getMessage();
            if (!StringUtils.isEmpty(message)) {
                if ("30600".equals(message)) {
                    apiResponse.setCodeMessage("30600", "出借失败");    //借款逾期未还，不能投标！
                } else if ("30601".equals(message)) {
                    apiResponse.setCodeMessage("30601", "出借金额需为100元的整数倍"); //投标金额必须为整数
                } else if ("30602".equals(message)) {
                    apiResponse.setCodeMessage("40602", "您加入的标已过期，请于下次出借日加入");    //指定的标记录不存在
                } else if ("30603".equals(message)) {
                    apiResponse.setCodeMessage("30603", "出借失败");    //您是该标的借款人，不能投标
                } else if ("30604".equals(message)) {
                    apiResponse.setCodeMessage("30604", "出借失败");        //指定的标不是投标中状态,不能投标
                } else if ("30605".equals(message)) {
                    apiResponse.setCodeMessage("30605", "出借金额不正确");    //投标金额大于可投金额
                } else if ("30606".equals(message)) {
                    apiResponse.setCodeMessage("30606", "出借金额不满100元"); //投标金额不能低于最低起投金额
                } else if ("30607".equals(message)) {
                    apiResponse.setCodeMessage("30607", "出借失败");    //剩余可投金额不能低于最低起投金额
                } else if ("30608".equals(message)) {
                    apiResponse.setCodeMessage("30608", "出借失败");    //用户往来账户不存在
                } else if ("30609".equals(message)) {
                    apiResponse.setCodeMessage("40609", "您的账户余额不足，请及时充值");  //账户余额不足
                } else if ("30610".equals(message)) {
                    apiResponse.setCodeMessage("30610", "出借失败");    //订单明细记录不存在
                } else if ("30611".equals(message)) {
                    apiResponse.setCodeMessage("30611", "出借失败");    //不可投本账号发的标
                } else if ("30612".equals(message)) {
                    apiResponse.setCodeMessage("30612", "出借失败");    //平台账号不存在
                } else if ("30613".equals(message)) {
                    apiResponse.setCodeMessage("30613", "出借失败");    //出借人往来账户不存在
                } else if ("30614".equals(message)) {
                    apiResponse.setCodeMessage("30614", "出借失败");    //出借人锁定账户不存在
                } else if ("30615".equals(message)) {
                    apiResponse.setCodeMessage("40615", "交易密码不正确，建议您重置交易密码");   //交易密码错误
                } else if ("30616".equals(message)) {
                    apiResponse.setCodeMessage("30616", "出借失败");    //查询记录有误,没有查询到用户信息
                } else if ("30617".equals(message)) {
                    apiResponse.setCodeMessage("30617", "未设置交易密码");
                } else if ("30621".equals(message)) {
                    apiResponse.setCodeMessage("30621", "交易密码验证失败");//交易密码验证出现异常
                } else if ("30623".equals(message)) {
                    apiResponse.setCodeMessage("30623", "出借金额必须为100的整数倍");//投标金额必须为100的整数倍
                }else if ("120319".equals(message)) {
                    apiResponse.setCodeMessage("120319", "加息券使用条件不满足");//投标金额必须为100的整数倍
                } else if (ResponseCode.USER_IS_HAVE_BID.getCode().equals(message)) {
                    apiResponse.setCodeMessage(ResponseCode.USER_IS_HAVE_BID.getCode(), ResponseCode.USER_IS_HAVE_BID.getMessage());//投标金额必须为100的整数倍
                } else if (ResponseCode.NOVICE_OVER_MAX_AMOUNT.getCode().endsWith(message)) {
                    apiResponse.setCodeMessage(ResponseCode.NOVICE_OVER_MAX_AMOUNT.getCode(), ResponseCode.NOVICE_OVER_MAX_AMOUNT.getMessage());
                } else if (ResponseCode.NOVICE_NOT_USE_FXHB.getCode().equals(message)) {
                    apiResponse.setCodeMessage(ResponseCode.NOVICE_NOT_USE_FXHB.getCode(), ResponseCode.NOVICE_NOT_USE_FXHB.getMessage());
                } else if (ResponseCode.NOVICE_NOT_NEW_USER.getCode().equals(message)) {
                    apiResponse.setCodeMessage(ResponseCode.NOVICE_NOT_NEW_USER.getCode(), ResponseCode.NOVICE_NOT_NEW_USER.getMessage());
                } else if (ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT.getCode().equals(message)) {
                    apiResponse.setCodeMessage(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT.getCode(), ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT.getMessage());
                } else {
                    //扩展返现红包提示 throw new LogicalException("30703&" + useMoney);
                    if (!StringUtils.isBlank(message)) {
                        String[] msgArr = message.split("\\&");
                        String msgTip = "";
                        if (msgArr.length > 1) {
                            String code = msgArr[0];
                            String tips = msgArr[1];
                            //返现红包错误提示
                            if ("30701".equals(code)) {
                                apiResponse.setCodeMessage("30701", tips + "返现红包已经使用");
                            } else if ("30702".equals(code)) {
                                apiResponse.setCodeMessage("30702", tips + "返现红包已经过期");
                            } else if ("30703".equals(code)) {
                                apiResponse.setCodeMessage("30703", "出借金额不能使用" + tips + "返现红包");
                            } else {
                                apiResponse.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
                            }
                        } else {
                            if ("30700".equals(msgArr[0])) {
                                apiResponse.setCodeMessage("30700", "返现红包不存在");
                            } else if ("30704".equals(msgArr[0])) {
                                apiResponse.setCodeMessage("30704", "返现红包使用不符合规则");//返现红包使用不符合规则
                            } else {
                                apiResponse.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
                            }
                        }
                    }
                }
            } else {
                apiResponse.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
            }
        }

        return apiResponse;
    }

    /**
     * 新手出借送体验金
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
                        List<String> list = Arrays.asList(parentIds);
                        if (!list.contains(parentId)) { //注册渠道非商务合作的才送
                            experienceGoldService.addUserExperienceGold(experienceGoldInfoList, String.valueOf(userId), userInfo.getPhone());
                        }
                    } else { //运营需求，当渠道来源没有的时候，默认为官网
                        logger.info("渠道为空或父渠道为空, userId[{}]", userId);
                        experienceGoldService.addUserExperienceGold(experienceGoldInfoList, String.valueOf(userId), userInfo.getPhone());
                    }
                }
            }
        } catch (Exception e) { //这里catch，失败不影响出借，补发
            logger.error("新手出借送体验金失败 >>> userId=[{}],bidId=[{}],amount=[{}]", userId, bidId, amount);
            logger.error("新手出借送体验金失败", e);
        }
    }
}
