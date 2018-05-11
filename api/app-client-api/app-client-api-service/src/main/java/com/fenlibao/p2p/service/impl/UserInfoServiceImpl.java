package com.fenlibao.p2p.service.impl;

import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.common.util.encrypt.PasswordCryptUtil;
import com.fenlibao.p2p.dao.*;
import com.fenlibao.p2p.dao.bid.BidExtendDao;
import com.fenlibao.p2p.dao.credit.UserCreditDao;
import com.fenlibao.p2p.dao.experiencegold.impl.ExperienceGoldDaoImpl;
import com.fenlibao.p2p.dao.financing.FinacingDao;
import com.fenlibao.p2p.dao.financing.UserFinancingDao;
import com.fenlibao.p2p.dao.salary.SalaryDao;
import com.fenlibao.p2p.dao.spread.UserSpreadDao;
import com.fenlibao.p2p.dao.trade.WithdrawDao;
import com.fenlibao.p2p.dao.user.AutoBidDao;
import com.fenlibao.p2p.dao.user.RiskTestDao;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.model.entity.*;
import com.fenlibao.p2p.model.entity.activity.AutoRegist;
import com.fenlibao.p2p.model.entity.salary.BidInfo;
import com.fenlibao.p2p.model.enums.CaptchaType;
import com.fenlibao.p2p.model.enums.SpecialUserType;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.form.BindIdCardForm;
import com.fenlibao.p2p.model.form.ModifyPasswordForm;
import com.fenlibao.p2p.model.form.RegisterForm;
import com.fenlibao.p2p.model.form.RetrievePasswordForm;
import com.fenlibao.p2p.model.form.trade.DueInAmount;
import com.fenlibao.p2p.model.form.user.Auth;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.*;
import com.fenlibao.p2p.model.vo.channel.RegisterChannelVO;
import com.fenlibao.p2p.model.vo.pay.PaymentLimitVO;
import com.fenlibao.p2p.model.vo.user.AutobidSettingVO;
import com.fenlibao.p2p.model.vo.user.UserMemberInfoVO;
import com.fenlibao.p2p.model.xinwang.entity.account.XWBankInfo;
import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.common.CGModeEnum;
import com.fenlibao.p2p.model.xinwang.enums.trade.SysPaymentInstitution;
import com.fenlibao.p2p.service.FinacingService;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.UserTokenService;
import com.fenlibao.p2p.service.activity.ActivityService;
import com.fenlibao.p2p.service.bank.BankService;
import com.fenlibao.p2p.service.bid.BidExtendService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.channel.ChannelService;
import com.fenlibao.p2p.service.coupon.CouponService;
import com.fenlibao.p2p.service.privateMessage.PrivateMessageService;
import com.fenlibao.p2p.service.recharge.HXAccountService;
import com.fenlibao.p2p.service.recharge.IRechargeService;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.service.register.IUserManagerService;
import com.fenlibao.p2p.service.salary.SalaryService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.service.user.LoginStateService;
import com.fenlibao.p2p.service.user.SpecialUserService;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.service.xinwang.bank.XWBankService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.email.EmailParam;
import com.fenlibao.p2p.util.email.EmailUtils;
import com.fenlibao.p2p.util.email.GenerateLinkUtils;
import com.fenlibao.p2p.util.api.file.FileInformation;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.formater.certification.BankCardFormater;
import com.fenlibao.p2p.util.formater.certification.IDCardNameFormater;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Sender;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class UserInfoServiceImpl implements UserInfoService {

    private static final Logger logger = LogManager.getLogger(UserInfoServiceImpl.class);

    @Resource
    private UserInfoDao userInfoDao;

    @Resource
    private UserSecurityDao userSecurityDao;

    @Resource
    private CheckErrorDao checkErrorDao;

    @Resource
    private SendEmailDao sendEmailDao;

    @Resource
    private AccessoryInfoDao accessoryInfoDao;

    @Resource
    private UserBaseInfoDao userBaseInfoDao;

    @Resource
    private SendSmsRecordDao sendSmsRecordDao;

    @Resource
    private SendSmsRecordExtDao sendSmsRecordExtDao;

    @Resource
    private SmsValidCodeDao smsValidCodeDao;

    @Resource
    private PrivateMessageDao privateMessageDao;

    @Resource
    private UserTokenService userTokenService;

    @Resource
    private TUserIconDao userIconDao;

    @Resource
    private TProductInfoDao productInfoDao;

    @Resource
    private ShopTreasureDao shopTreasureDao;

    @Resource
    private EnumDao enumDao;

    @Resource
    private UserAccountDao userAccountDao;

    @Resource
    private BidRefundDao bidRefundDao;

    @Resource
    private FinacingDao finacingDao;
    @Resource
    private IUserManagerService umgrservice;

    @Resource
    private BidExtendDao bidExtendDao;
    @Resource
    private WithdrawDao withdrawDao;
    @Resource
    private UserCreditDao userCreditDao;
    @Resource
    private UserFinancingDao userFinancingDao;
    @Resource
    private SalaryDao salaryDao;

    @Resource
    private SalaryService salaryService;
    @Resource
    private FinacingService finacingService;
    @Resource
    private BidExtendService bidExtendService;

    @Resource
    private UserSpreadDao userSpreadDao;

    @Resource
    private BankService bankService;

    @Resource
    private ChannelService channelService;

    @Resource
    private RedpacketService redpacketService;

    @Resource
    private ITradeService tradeService;

    @Resource
    private LoginStateService loginStateService;

    @Resource
    private ExperienceGoldDaoImpl  experienceGoldDaoImpl;

    @Resource
    private  RiskTestDao riskTestDao;
    @Resource
    private BidInfoService bidInfoService;
    @Resource
    private SpecialUserService specialUserService;
    @Resource
    private CouponService couponService;
    @Resource
    private PrivateMessageService privateMessageService;
    @Resource
    private HXAccountService hxAccountService;
    @Resource
    private AutoBidDao autoBidDao;
    @Resource
    private XWUserInfoService xwUserInfoService;
    @Resource
    private XWBankService xwBankService;
    @Resource
    private IRechargeService rechargeService;
    @Resource
    private XWAccountDao xwAccountDao;
    @Resource
    private ActivityService activityService;
    /**
     * 用户密码正则表达式
     */
//	private final String pwdPattern = "^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{8,12}$";
    private final String pwdPattern = "[a-zA-Z0-9]{6,20}";

    /**
     * 手机号正则表达式
     */
    private final String phonePattern = "^(13|14|15|16|17|18)[0-9]{9}$";

    /**
     * 用户名正在表达式
     */
    private final String usernamePattern = "^(?![0-9]+$)[a-zA-Z0-9]{6,20}$"; //必须为6-20位的英文或英文、数字组合，不能是纯数字

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void grantAwardRegister(String phoneNum, String userId) {
        BigDecimal reawrdAmount = BigDecimal.ZERO;
        List<String> scorePercents = new LinkedList<>();
        try {
            // 发放注册返现券并获取返现券总额
            reawrdAmount = redpacketService.grantRedpacketRegister(phoneNum, userId);
        } catch (Exception e) {
        }
        try {
            // 发放注册加息券并获取所有加息券加息幅度
            scorePercents = couponService.grantRateCouponRegister(phoneNum, userId);
        } catch (Exception e) {
        }
        try {
            // 发短信和站内信
            sendGrantAwardRegister(phoneNum, userId, reawrdAmount, scorePercents);
        } catch (Exception e) {
            String message = "[注册发送短信站内信异常：]" + e.getMessage();
            logger.error(message, e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void grantAwardFirstLogin(String phoneNum, String userId) {
        BigDecimal reawrdAmount = BigDecimal.ZERO;
        List<String> scorePercents = new LinkedList<>();
        try {
            // 发放注册返现券并获取返现券总额
            reawrdAmount = redpacketService.grantRedpacketFirstLogin(phoneNum, userId);
        } catch (Exception e) {
        }
        try {
            // 发放注册加息券并获取所有加息券加息幅度
            scorePercents = couponService.grantRateCouponFirstLogin(phoneNum, userId);
        } catch (Exception e) {
        }
        try {
            // 发短信和站内信
            sendGrantAwardFirstLogin(phoneNum, userId, reawrdAmount, scorePercents);
        } catch (Exception e) {
            String message = "[首次登陆发送注册奖励发送短信站内信异常：]" + e.getMessage();
            logger.error(message, e);
        }
    }

    private void sendGrantAwardFirstLogin(String phoneNum, String userId, BigDecimal reawrdAmount, List<String> scorePercents) {
        String smsTemplate = Sender.get("sms.firstlogin.reawrd.content");
        String letterSuffix = Sender.get("znx.suffix.content");
        // 站内信标题
        String privatemessageTitle = InterfaceConst.PRIVATEMESSAGE_TITLE;
        // 所有加息券的百分比
        StringBuilder rateCouponPercents = new StringBuilder();
        String symbol = "、";
        if (scorePercents != null && scorePercents.size() > 0) {
            for (String scorePercent : scorePercents) {
                rateCouponPercents.append(scorePercent).append(symbol);
            }
            rateCouponPercents.deleteCharAt(rateCouponPercents.length() - 1);
        }
        // 信息内容
        String content = smsTemplate
                .replace("#{rateCouponPercents}", rateCouponPercents.toString())
                .replace("#{rateCouponCount}", String.valueOf(scorePercents.size()))
                .replace("#{redpacketAmount}", reawrdAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        // 发送短信和站内信
        sendSmsAndPrivatemessage(phoneNum, userId, content, privatemessageTitle, content + letterSuffix, VersionTypeEnum.PT);
    }

    /**
     * 发送注册奖励短信和站内信
     *
     * @param phoneNum
     * @param userId
     * @param reawrdAmount
     * @param scorePercents
     */
    private void sendGrantAwardRegister(String phoneNum, String userId, BigDecimal reawrdAmount, List<String> scorePercents) {
        String smsTemplate = Sender.get("sms.register.reawrd.content");
        String letterSuffix = Sender.get("znx.suffix.content");
        // 站内信标题
        String privatemessageTitle = InterfaceConst.PRIVATEMESSAGE_TITLE;
        // 所有加息券的百分比
        StringBuilder rateCouponPercents = new StringBuilder();
        String symbol = "、";
        if (scorePercents != null && scorePercents.size() > 0) {
            for (String scorePercent : scorePercents) {
                rateCouponPercents.append(scorePercent).append(symbol);
            }
            rateCouponPercents.deleteCharAt(rateCouponPercents.length() - 1);
        }
        // 信息内容
        String content = smsTemplate
                .replace("#{rateCouponPercents}", rateCouponPercents.toString())
                .replace("#{rateCouponCount}", String.valueOf(scorePercents.size()))
                .replace("#{redpacketAmount}", reawrdAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        // 发送短信和站内信
        sendSmsAndPrivatemessage(phoneNum, userId, content, privatemessageTitle, content + letterSuffix, VersionTypeEnum.PT);
    }

    private void sendSmsAndPrivatemessage(String phoneNum, String userId, String smsContent, String privatemessageTitle, String privatemessageContent, VersionTypeEnum versionTypeEnum) {
        // 短信
        Timestamp outTime = new Timestamp(System.currentTimeMillis() / 1000 * 1000 + 30 * 60 * 1000);//验证码过期时间30分钟
        SendSmsRecord record = new SendSmsRecord();
        record.setContent(smsContent);
        record.setCreateTime(new Date());
        record.setStatus("W");
        record.setType(0);
        record.setOutTime(outTime);
        sendSmsRecordDao.insertSendSmsRecord(record);
        SendSmsRecordExt recordExt = new SendSmsRecordExt();
        recordExt.setId(record.getId());
        recordExt.setPhoneNum(phoneNum);
        sendSmsRecordExtDao.insertSendSmsRecordExt(recordExt);
        // 站内信
        privateMessageService.sendLetter(userId, privatemessageTitle, privatemessageContent, versionTypeEnum);
    }

    @Override
    public UserInfo getUserInfoByPhoneNumOrUsername(Map<String, Object> map){
        return userInfoDao.getUserInfoByPhoneNumOrUsername(map);
    }

    @Transactional(readOnly = false)
	@Override
	public boolean bindIdCard(BindIdCardForm bindIdCardForm)throws Throwable {
        boolean result = true;

		/*String idCard = bindIdCardForm.getIdCardNum();
		String name = bindIdCardForm.getIdCardFullName();
		int userId = bindIdCardForm.getUserId();
		MerRegisterPerson regPerson = umgrservice.createRegisterPerson(idCard, name, userId);
		Map<String, String> reqMap = Mer2PlatUtils.makeReqData(regPerson);
        MerRegisterPersonRet rpr =
        		HttpClientUtilsExt.postReturnBodyAsEntity(MerRegisterPersonRet.class, Constant.UMPAY_SERVICE_URL, reqMap);
        if ("0000".equals(rpr.getRet_code()))
        {
			umgrservice.updateUserInfo(rpr, regPerson, userId);
        }
        return rpr;*/

        // 实名认证接口调用
        // ...

            // 用户ID
            Integer userId = bindIdCardForm.getUserId();
            // 姓名
            String idCardFullName = bindIdCardForm.getIdCardFullName();
            // 实名认证状态
            String realNameAuth = Status.TG.toString();
            // 身份证号码
            String idcard = bindIdCardForm.getIdCardNum();
            // 身份证号码,3-18位星号替换
            String idcardStar = idcard.substring(0, 2)+"************" + idcard.substring(idcard.length()-4,idcard.length());
            // 身份证号码AES加密
            String idcardEncrypt = StringHelper.encode(idcard);
            // 出生日期
            Timestamp birthDate = getBirthday(idcard);
            Map<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            map.put("userName", idCardFullName);
            map.put("realNameAuth", realNameAuth);
            map.put("cardID", idcardStar);
            map.put("cardIDEncrypt", idcardEncrypt);
            map.put("birthDate", birthDate);
            // 修改用户基础信息
            userBaseInfoDao.updateUserBaseInfo(map);
            // 修改实名认证状态
            map.clear();
            map.put("userId", userId);
            map.put("idcardAuth", realNameAuth);
            userSecurityDao.updateUserSecurity(map);

        return result;
    }

    /**
     * 根据身份证得到出生年月
     * @param cardID
     * @return
     */
    private static Timestamp getBirthday(String cardID) {
        StringBuffer tempStr = new StringBuffer("");
        if (cardID != null && cardID.trim().length() > 0) {
            if (cardID.trim().length() == 15) {
                tempStr.append(cardID.substring(6, 12));
                tempStr.insert(4, '-');
                tempStr.insert(2, '-');
                tempStr.insert(0, "19");
            } else if (cardID.trim().length() == 18) {
                tempStr = new StringBuffer(cardID.substring(6, 14));
                tempStr.insert(6, '-');
                tempStr.insert(4, '-');
            }
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return new Timestamp(fmt.parse(tempStr.toString()).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public UserAssetInfoVO getUserAssetInfo(String userId, Date yesterdayDate) {
        UserAssetInfoVO result = null;

        if (StringUtils.isNotBlank(userId)) {
            // 获取资金账户(T6101)的往来账户余额
            String accountType = InterfaceConst.ACCOUNT_TYPE_WLZH;
            UserAccount account = getUserAccount(userId, accountType);
            // 设置用户余额
            if (null != account) {
                result = new UserAssetInfoVO();
                // 用户余额
                BigDecimal balance = account.getBalance();
                result.setBalance(balance);
                // 根据用户ID获取标的状态为投标中的投标记录金额(投资冻结金额)。
                BigDecimal tenderFreezeSum = bidExtendDao.getTenderFreezeSum(userId);
                result.setTzdjSum(tenderFreezeSum);
                // 提现冻结金额
                BigDecimal withdrawFreezeSum = withdrawDao.getWithdrawFreezeSum(userId,CGModeEnum.PT.getName());
                result.setTxdjSum(withdrawFreezeSum);
                // 现金资产总额
                BigDecimal cachSum = balance.add(tenderFreezeSum).add(withdrawFreezeSum);
                result.setCashSum(cachSum);

                // 活期宝投资总额
                BigDecimal hqbInvestSum = getHqbInvestSum(userId);
                result.setHqbSum(hqbInvestSum);
                // 开店宝投资总额
                BigDecimal kdbInvestSum = getKdbInvestSum(userId);
                result.setKdbSum(kdbInvestSum);
                // 薪金宝投资总额
                BigDecimal xjbInvestSum = getXjbInvestSum(userId);
                result.setXjbSum(xjbInvestSum);
                // 投资总额(活期宝总额+开店宝总额+薪金宝总额)
                BigDecimal investSum = hqbInvestSum.add(kdbInvestSum).add(xjbInvestSum);
                result.setInvestSum(investSum);

                // 活期宝昨日收益
                BigDecimal hqbEarning = bidExtendService.getHqbEarning(userId, yesterdayDate);
                result.setHqbEarning(hqbEarning);
                // 开店宝昨日收益
                BigDecimal kdbEarning = finacingService.getKdbEarning(userId, yesterdayDate);
                result.setKdbEarning(kdbEarning);
                // 薪金宝昨日收益
                BigDecimal xjbEarning = salaryService.getXjbEarning(userId, yesterdayDate);
                result.setXjbEarning(xjbEarning);
                //债权转让昨天收益
                BigDecimal zqzrEarnings = finacingService.getZqzrEarning(userId);
                result.setZqzrEarnings(zqzrEarnings);
                // 昨日总收益(活期宝昨日收益+开店宝昨日收益+薪金宝昨日收益)
                BigDecimal yesterdayEarningSum = hqbEarning.add(kdbEarning).add(xjbEarning).add(zqzrEarnings);
                result.setYesterdayEarningSum(yesterdayEarningSum);
                // 累计总收益
                BigDecimal earningSum = getEarningSum(userId);
                result.setEarningSum(earningSum);

                // 用户总资产(投资资产+现金资产总额)
                BigDecimal totalSum = investSum.add(cachSum);
                result.setTotalSum(totalSum);
            }
        }
        return result;
    }

    /**
     * 获取用户账户余额
     * @param userId
     * @param accountType
     * @return
     */
    @Override
    public UserAccount getUserAccount(String userId, String accountType) {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("userId", userId);
        userMap.put("type", accountType);
        return userAccountDao.getUserAccount(userMap);
    }

    @Override
    public RegisterChannelVO getChannelInfo(int userId) {
        return userInfoDao.getChannelInfo(userId);
    }

    /**
     * 获取用户活期宝投资总额
     * @param userId
     * @return
     */
    private BigDecimal getHqbInvestSum(String userId) {
        return bidExtendDao.getHqbInvestSum(userId);
    }

    /**
     * 获取开店宝投资总额
     * @param userId
     * @return
     */
    private BigDecimal getKdbInvestSum(String userId) {
        return bidExtendDao.getKdbInvestSum(userId);
    }

    /**
     * 获取薪金宝投资总额
     * @return
     * @param userId
     */
    private BigDecimal getXjbInvestSum(String userId) {
        return salaryDao.getXjbInvestSum(userId);
    }

    /**
     * 获取累计总收益
     * @param userId
     * @return
     */
    private BigDecimal getEarningSum(String userId) {
//        Map<String, Object> earntMap = new HashMap<>();
//        earntMap.put("feeTypes", new int[]{InterfaceConst.TZ_LX, InterfaceConst.TZ_WYJ, InterfaceConst.TZ_FX});
//        earntMap.put("status", Status.YH.name());
//        earntMap.put("userType", InterfaceConst.USER_TYPE_ZRR);
//        earntMap.put("userId", userId);
//        return shopTreasureDao.getUserEarnStatistics(earntMap);

    	return tradeService.getAccumulativeEarnings(Integer.valueOf(userId));
    }

    @Override
    public HttpResponse retrievePassword(RetrievePasswordForm retrievePasswordForm) {
        HttpResponse response = new HttpResponse();
        String phoneNum = retrievePasswordForm.getPhoneNum();
        String newPassword = retrievePasswordForm.getNewPassword();

        try {
            // 密码进行AES解码
            String decryptPassword = AES.getInstace().decrypt(newPassword);

            if (!decryptPassword.matches(pwdPattern)) {
                response.setCodeMessage(ResponseCode.USER_PASSWORD_ERROR);
            } else {
                // AES解码
                phoneNum = AES.getInstace().decrypt(phoneNum);
                // 手机号格式验证
                if (phoneNum.matches(phonePattern)) {
                    Map paramMap = new HashMap();
                    paramMap.put("phoneNum", phoneNum);
                    UserInfo userInfo = userInfoDao.getUserInfo(paramMap);
                    if (userInfo == null) {
                        response.setCodeMessage(ResponseCode.USER_NOT_EXIST_FOR_USER);
                    } else {
                        paramMap.clear();
                        paramMap.put("password", PasswordCryptUtil.crypt(decryptPassword));
                        paramMap.put("userId", userInfo.getUserId());
                        // 更新密码
                        int updatePassword = userInfoDao.updatePassword(paramMap);
                        // 修改密码失败
                        if (updatePassword == 0) {
                            response.setCodeMessage(ResponseCode.FAILURE);
                        }
                    }
                } else {
                    // 手机号码格式不正确
                    response.setCodeMessage(ResponseCode.COMMON_PHONE_FORMAT_WRONG);
                }
            }
        } catch (Exception e) {
            response.setCodeMessage(ResponseCode.FAILURE);
            logger.debug(e.getMessage());
        }
        return response;
    }

    /**
     * 获取用户账户信息字段Map
     */
    @Override
    public Map<String, Object> getUserAccountInfoDataMap(UserAccountInfoVO userAccountInfoVO,int versiongControl) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> accountCheckStatusMap = new HashMap<>();
        String userId = userAccountInfoVO.getUserId();
        boolean isNovice = bidInfoService.isNovice(Integer.valueOf(userId)); //是否是新手
        if (specialUserService.isSpecial(userId, SpecialUserType.WXSXZ)) {
            isNovice = true;
        }
        data.put("isNovice", isNovice ? 1 : 0);
        data.put("token", userAccountInfoVO.getToken());
        //2.0增加风险压力测试-junda.feng
        Map map=riskTestDao.getTestResultByUid(Integer.parseInt(userId));
        data.put("RiskTestResult",map==null?0:map.get("resultId"));
        data.put("userId", userId);
        data.put("userUrl", userAccountInfoVO.getUserUrl());
        // AES加密
        String username = userAccountInfoVO.getUsername();
        if(StringUtils.isNotBlank(username)) {
            username = AES.getInstace().encrypt(username.getBytes());
        }
        data.put("username", username);
        data.put("nickName", userAccountInfoVO.getNickName());
        // 加密手机号
        data.put("phone", AES.getInstace().encrypt(userAccountInfoVO.getPhone().getBytes()));

        // 免交易密码开启状态(0:关闭  1:开启)
        accountCheckStatusMap.put("noDealpasswordFlag", userAccountInfoVO.getNoDealpasswordFlag());
        // 是否设置了交易密码(0:没有  1:有)
        int dealpassword;
        String dealpasswordFlag = userAccountInfoVO.getDealpasswordFlag();
        if (StringUtils.isBlank(dealpasswordFlag) || dealpasswordFlag.equals(Status.WSZ.toString())) {
            dealpassword = 0;
        } else {
            dealpassword = 1;
        }
        accountCheckStatusMap.put("dealpasswordFlag", dealpassword);
        String bPhone;
        String bIdentitycard;
        UserSecurityAuthentication userSecurityAuth = userSecurityDao.getUserSecurity(userId);
        if (userSecurityAuth == null) {
            bPhone = "0";
            bIdentitycard = "0";
        } else {
            bIdentitycard = convertResultStatus(userSecurityAuth.getCardIDAuth());
            bPhone = convertResultStatus(userSecurityAuth.getPhoneAuth());
        }
        accountCheckStatusMap.put("bPhone", bPhone);
        accountCheckStatusMap.put("bIdentitycard", bIdentitycard);
        data.put("bAccountInfo", accountCheckStatusMap);

        List<BankCard> bankCards = userAccountInfoVO.getBankCards();
        List<Map<String, Object>> resultList = bankService.buildBankCardsResult(bankCards,versiongControl);
        data.put("bankList", resultList);

        // 绑定的身份证姓名(AES加密)
        String identityName = userAccountInfoVO.getIdentityName();
        String idCardNameFormater = IDCardNameFormater.format(identityName);
        data.put("identityName", idCardNameFormater);
        // 绑定的身份证
        data.put("identitycard", userAccountInfoVO.getIdentitycard());

        List<AutobidSettingVO> settingVOs = autoBidDao.getAutobidSettingList(Integer.valueOf(userAccountInfoVO.getUserId()));
        //20161228 消费信贷提示显示开关
        data.put("xfxdShowFlag", 1);
        if (settingVOs != null && settingVOs.size() > 0) {
            if (settingVOs.get(0).isActive()) {
                data.put("xfxdShowFlag", 0);
            }
        }
        //20170602 存管信息查询
        Map<String, Object> xwcgInfo = getXWCardInfo(IntegerParser.parse(userId));
        data.put("xwcgInfo", xwcgInfo);

        return data;
    }

    /**
     * 获取用户账户基础信息字段Map
     */
    @Override
    public Map<String, Object> getUserBaseAccountInfoDataMap(UserAccountInfoVO userAccountInfoVO,int versiongControl) {
        Map<String, Object> data = new HashMap<>();
        String userId = userAccountInfoVO.getUserId();
        boolean isNovice = bidInfoService.isNovice(Integer.valueOf(userId)); //是否是新手
        if (specialUserService.isSpecial(userId, SpecialUserType.WXSXZ)) {
            isNovice = true;
        }
        data.put("isNovice", isNovice ? 1 : 0);
        data.put("token", userAccountInfoVO.getToken());
        //2.0增加风险压力测试-junda.feng
        Map map=riskTestDao.getTestResultByUid(Integer.parseInt(userId));
        data.put("RiskTestResult",map==null?0:map.get("resultId"));
        data.put("userId", userId);
        data.put("userUrl", userAccountInfoVO.getUserUrl());
        // AES加密
        String username = userAccountInfoVO.getUsername();
        if(StringUtils.isNotBlank(username)) {
            username = AES.getInstace().encrypt(username.getBytes());
        }
        data.put("username", username);
        data.put("nickName", userAccountInfoVO.getNickName());
        // 加密手机号
        data.put("phone", AES.getInstace().encrypt(userAccountInfoVO.getPhone().getBytes()));

        // 绑定的身份证姓名(AES加密)
        data.put("identityName", IDCardNameFormater.format(userAccountInfoVO.getIdentityName()));
        // 绑定的身份证
        data.put("identitycard", userAccountInfoVO.getIdentitycard());
        return data;
    }

    @Override
    public boolean updateUserFirstLoginState(int userId){
        int flag= userInfoDao.updateUserFirstLoginState(userId,Status.F.name());
        if(flag>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserFirstLogin(int userId){
        String state = userInfoDao.getUserFirstLoginState(userId);
        if("S".equals(state)){
            return true;
        }
        return false;
    }

    @Override
    public UserAccountInfoVO getUserAccountInfo(String userId) {
        // 根据用户id查询用户账户信息
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        UserAccountInfoVO userAccountInfoVO = userInfoDao.getUserAccountInfo(paramMap);
        return userAccountInfoVO;
    }

    @Override
    public HttpResponse setUserName(String userId, String username) throws Exception {
        HttpResponse response = new HttpResponse();
		username = AES.getInstace().decrypt(username);
        if (!username.matches(usernamePattern)) {
            response.setCodeMessage(ResponseCode.USER_NAME_FORMAT_WRONG);
            return response;
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("userId", userId);
        UserInfo user = userInfoDao.getUserInfo(paramMap);
        if (null == user) {
            response.setCodeMessage(ResponseCode.USER_NOT_EXIST);
            return response;
        }
        if (StringUtils.isNotBlank(user.getUsername())) {
            response.setCodeMessage(ResponseCode.USER_NAME_NOT_BE_CHANGED);
            return response;
        }
        Integer srcUserId = userInfoDao.existUsername(username);
        if (srcUserId == null) {
            paramMap.clear();
            paramMap.put("userId", userId);
            paramMap.put("username", username);
            int result = userInfoDao.updateUserName(paramMap);
            if (result <= 0) {
                response.setCodeMessage(ResponseCode.FAILURE);
            }
        } else {
            response.setCodeMessage(ResponseCode.USER_NAME_IS_EXIST);
        }
        return response;
    }

    /**
     * 获取手机号对应的用户个数
     * @param paramMap
     * @return
     */
    @Override
    public int getUserCount(Map paramMap) {
        return userInfoDao.checkUserCount(paramMap);
    }

    @Transactional(readOnly = false)
    @Override
    public UserInfo register(BaseRequestForm baseForm, RegisterForm registerForm) throws Exception {
        String phoneNum = registerForm.getPhoneNum();
        String password = registerForm.getPassword();

        Map paramMap = new HashMap();
        paramMap.put("phoneNum", phoneNum);
        // 新增用户信息
        addUserInfo(phoneNum, password);
        // 根据手机号获取用户信息
        UserInfo uInfo = userInfoDao.getUserInfo(paramMap);
        // 获取用户ID
        String userId = uInfo.getUserId();
        // 新增用户安全认证表
        addUserSecurityAuthentication(phoneNum, userId);
        // 新增用户基础信息，兴趣类型为理财
        addUserBaseInfo(userId);
        // 分别新增资金账户(往来账户、风险保证金账户、锁定账户)
        addUserAccount(phoneNum, userId);
        // 用户信用账户表
        addUserCredit(userId);
        // 用户理财统计表
        addUserFinancing(userId);
        // 优选理财统计表
        addUserBestFinancing(userId);
        // 推荐人相关表
        String spreadPhoneNum = registerForm.getSpreadPhoneNum();
        addSpread(userId, spreadPhoneNum, phoneNum);
        /*// 用户站内信
        Map<String,Object> messageParamMap = new HashMap<>();
        messageParamMap.put("userId", userId);
        messageParamMap.put("title", "注册成功");
        messageParamMap.put("sendTime", new Timestamp(System.currentTimeMillis()));
        privateMessageDao.addMessage(messageParamMap);*/
        // 用户站内信内容
        // ...
        // 获取所有信用认证项(T5123)，并新增用户认证信息(T6120)
        addUserCreditAuthInfo(userId);
        // 用户信用档案表(T6144)
        addUserCreditArchive(userId);
        // 新增用户来源
        addUserOrigin(userId, registerForm.getChannelCode(), baseForm.getClientType());
        //增加用户积分账户   modify by laubrence  2016-2-20 15:58:57
        addMemberPointsAccount(userId, phoneNum);
        return uInfo;
    }

    @Transactional(readOnly = false)
    @Override
    public UserInfo autoRegister(BaseRequestForm baseForm, RegisterForm registerForm, AutoRegist autoRegist) throws Exception {
        String phoneNum = registerForm.getPhoneNum();
        String password = registerForm.getPassword();

        Map paramMap = new HashMap();
        paramMap.put("phoneNum", phoneNum);
        // 新增用户信息
        autoAddUserInfo(phoneNum, password,null);
        // 根据手机号获取用户信息
        UserInfo uInfo = userInfoDao.getUserInfo(paramMap);
        // 获取用户ID
        String userId = uInfo.getUserId();
        // 新增用户安全认证表
        addUserSecurityAuthentication(phoneNum, userId);
        // 新增用户基础信息，兴趣类型为理财
        addUserBaseInfo(userId);
        // 分别新增资金账户(往来账户、风险保证金账户、锁定账户)
        addUserAccount(phoneNum, userId);
        // 用户信用账户表
        addUserCredit(userId);
        // 用户理财统计表
        addUserFinancing(userId);
        // 优选理财统计表
        addUserBestFinancing(userId);
        // 推荐人相关表
        /*// 用户站内信
        Map<String,Object> messageParamMap = new HashMap<>();
        messageParamMap.put("userId", userId);
        messageParamMap.put("title", "注册成功");
        messageParamMap.put("sendTime", new Timestamp(System.currentTimeMillis()));
        privateMessageDao.addMessage(messageParamMap);*/
        // 用户站内信内容
        // ...
        // 获取所有信用认证项(T5123)，并新增用户认证信息(T6120)
        addUserCreditAuthInfo(userId);
        // 用户信用档案表(T6144)
        addUserCreditArchive(userId);
        // 新增用户来源
        addUserOrigin(userId, registerForm.getChannelCode(), baseForm.getClientType());
        //增加用户积分账户   modify by laubrence  2016-2-20 15:58:57
        addMemberPointsAccount(userId, phoneNum);

       //是否需要发放投资奖励
        BigDecimal reawrdAmount = BigDecimal.ZERO;
        List<String> scorePercents = new LinkedList<>();
        try {
            // 发放注册返现券并获取返现券总额
            reawrdAmount = redpacketService.grantRedpacketRegister(phoneNum, userId);
        } catch (Exception e) {
        }
        try {
            // 发放注册加息券并获取所有加息券加息幅度
            scorePercents = couponService.grantRateCouponRegister(phoneNum, userId);
        } catch (Exception e) {
        }
        autoRegist.setUserId(Integer.valueOf(userId));
        activityService.updateAutoRegist(autoRegist);
        return uInfo;
    }

    /** 
     * @Title: addMemberPointsAccount 
     * @Description: 增加用户积分账户
     * @param userId
     * @return: void
     */
    private void addMemberPointsAccount(String userId, String phoneNum) {
        UserMemberInfoVO memberInfo = userInfoDao.getMemberInfo(phoneNum);
        if (memberInfo != null) {
            userInfoDao.updatePointsAccount(userId, memberInfo.getPointsId());//将会员积分账户关联起来
            userInfoDao.updatePointsConsumeRecord(userId, memberInfo.getMemberId());//将会员未注册分利宝之前的积分消费记录关联起来
            // add by zeronx 2018-1-4
            userInfoDao.updateMerchantMemberUserId(userId, memberInfo.getMemberId());
            return;
        }
    	Date nowDatetime = new Date();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("nowDatetime", nowDatetime);
        userInfoDao.addMemberPointsAccount(paramMap);
    }

    /**
     * 新增用户来源
     * @param userId
     * @param channelCode
     * @param clientType
     * @return
     */
    private void addUserOrigin(String userId, String channelCode, String clientType) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("channelCode", channelCode);
        paramMap.put("clientTypeId", clientType);
        // 新增用户来源
        channelService.addUserOrigin(paramMap);
    }

    /**
     * 用户信用档案表(T6144)
     * @param userId
     */
    private void addUserCreditArchive(String userId) {
        Map<String, Object> userCreditArchiveParamMap = new HashMap<>();
        userCreditArchiveParamMap.put("userId", userId);
        userCreditDao.addUserCreditArchive(userCreditArchiveParamMap);
    }

    /**
     * 获取所有信用认证项(T5123)，并新增用户认证信息(T6120)
     * @param userId
     */
    private void addUserCreditAuthInfo(String userId) {
        Map<String, Object> creditAuthInfoIdParamMap = new HashMap<>();
        creditAuthInfoIdParamMap.put("userId", userId);
        // 新增用户认证信息(T6120)
        List<Integer> creditAuthItem = userCreditDao.getCreditAuthItem();
        for (Integer creditAuthInfoId : creditAuthItem) {
            creditAuthInfoIdParamMap.put("creditAuthInfoId", creditAuthInfoId);
            userCreditDao.addUserCreditAuthInfo(creditAuthInfoIdParamMap);
        }
    }

    /**
     * 优选理财统计表
     * @param userId
     */
    private void addUserBestFinancing(String userId) {
        Map<String, Object> userBestFinancingParamMap = new HashMap<>();
        userBestFinancingParamMap.put("userId", userId);
        userFinancingDao.addUserBestFinancing(userBestFinancingParamMap);
    }

    /**
     * 用户理财统计表
     * @param userId
     */
    private void addUserFinancing(String userId) {
        Map<String, Object> userFinancingParamMap = new HashMap<>();
        userFinancingParamMap.put("userId", userId);
        userFinancingDao.addUserFinancing(userFinancingParamMap);
    }

    /**
     * 用户信用账户表
     * @param userId
     */
    private void addUserCredit(String userId) {
        Map<String, Object> userCreditParamMap = new HashMap<>();
        userCreditParamMap.put("userId", userId);
        userCreditDao.addUserCredit(userCreditParamMap);
    }

    /**
     * 分别新增资金账户(往来账户、风险保证金账户、锁定账户)
     * @param phoneNum
     * @param userId
     */
    private void addUserAccount(String phoneNum, String userId) {
        Map<String, Object> userAccountParamMap = new HashMap<>();
        userAccountParamMap.put("userId", userId);
        for (AccountEnum e : AccountEnum.values()) {
            userAccountParamMap.put("type", e.toString());
            userAccountParamMap.put("account", getAccount(e.toString(), userId));
            userAccountParamMap.put("accountName", phoneNum);
            userAccountDao.addUserAccount(userAccountParamMap);
        }
    }

    /**
     * 新增用户基础信息，兴趣类型为理财
     * @param userId
     */
    private void addUserBaseInfo(String userId) {
        Map<String, Object> userBaseInfoParamMap = new HashMap<>();
        userBaseInfoParamMap.put("userId", userId);
        userBaseInfoParamMap.put("type", Status.LC.name());
        userBaseInfoDao.addUserBaseInfo(userBaseInfoParamMap);
    }

    /**
     * 新增用户安全认证表
     * @param phoneNum
     * @param userId
     */
    private void addUserSecurityAuthentication(String phoneNum, String userId) {
        UserSecurityAuthentication authentication = new UserSecurityAuthentication();
        authentication.setUserId(userId);
        authentication.setPhone(phoneNum);
        authentication.setCardIDAuth(Status.BTG.name());
        authentication.setPhoneAuth(Status.TG.name());
        authentication.setEmailAuth(Status.BTG.name());
        authentication.setTradPasswordAuth(InterfaceConst.AUTH_TRADPASSWORD_NOTSET);
        userInfoDao.addUserSecurityAuthentication(authentication);
    }

    /**
     * 新增用户基本信息
     * @param phoneNum
     * @param password
     */
    private void addUserInfo(String phoneNum, String password) {
        // 新增用户基本信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(phoneNum);
        userInfo.setPhone(phoneNum);
        userInfo.setUserType(T6110_F06.ZRR.name());
        userInfo.setUserStatus(T6110_F07.QY.name());
        userInfo.setRegisterOrigin(T6110_F08.ZC.name());
        // 非担保方
        userInfo.setGuarantorFlag(T6110_F10.F.name());
        // 加密
        userInfo.setPassword(PasswordCryptUtil.crypt(password));
        userInfoDao.addUser(userInfo);
    }


    /**
     * 推广相关表
     * @param userId
     * @param spreadPhoneNum
     * @param phoneNum
     */
    private void addSpread(String userId, String spreadPhoneNum, String phoneNum) {
        // 推广奖励统计表
        Map<String, Object> userSpreadParamMap = new HashMap<>();
        userSpreadParamMap.put("userId", userId);
        userSpreadDao.addSpreadAwardStatistics(userSpreadParamMap);

        if(StringUtils.isNotBlank(spreadPhoneNum)) {
            // 根据推荐人手机号获取用户ID
            Map<String, Object> userInfoParamMap = new HashMap<>();
            userInfoParamMap.put("phoneNum", spreadPhoneNum);
            UserInfo userInfo = userInfoDao.getUserInfo(userInfoParamMap);
            // 如果推荐人手机号存在
            if(userInfo != null) {
                // 推荐人ID
                String spreadUserId = userInfo.getUserId();
                // 新增首次充值奖励记录
                Map<String, Object> spreadUserParamMap = new HashMap<>();
                spreadUserParamMap.put("spreadUserId", spreadUserId);
                spreadUserParamMap.put("userId", userId);
                userSpreadDao.addFirstChargeAward(spreadUserParamMap);
                // 修改推荐人推广奖励统计次数+1
                Map<String, Object> spreadAwardStatisticsParamMap = new HashMap<>();
                spreadAwardStatisticsParamMap.put("userId", spreadUserId);
                userSpreadDao.updateSpreadAwardStatistics(spreadAwardStatisticsParamMap);
            }
        }

        // 新增用户推广信息
        Map<String, Object> spreadInfoParamMap = new HashMap<>();
        spreadInfoParamMap.put("userId", userId);
        spreadInfoParamMap.put("phoneNum", phoneNum);
        spreadInfoParamMap.put("spreadPhoneNum", spreadPhoneNum);
        userSpreadDao.addSpreadInfo(spreadInfoParamMap);
    }

    /**
     * 生成资金账户 账号
     *
     * @param type
     * @param id
     * @return
     */
    private String  getAccount(String type, String id) {
        DecimalFormat df = new DecimalFormat("00000000000");
        StringBuilder sb = new StringBuilder();
        sb.append(type.substring(0, 1));
        sb.append(df.format(Integer.parseInt(id)));
        return sb.toString();
    }

    @Override
    public HttpResponse modifyPassword(ModifyPasswordForm modifyPasswordForm) {
        HttpResponse response = new HttpResponse();
        String userId = modifyPasswordForm.getUserId();
        // 正则，数字+字母+特殊字符，8-12位长度
        String oldPassword = modifyPasswordForm.getOldPassword();
        String newPassword = modifyPasswordForm.getNewPassword();
        try {
            // 密码进行AES解码
            oldPassword = AES.getInstace().decrypt(oldPassword);
            newPassword = AES.getInstace().decrypt(newPassword);

            // 判断旧密码规则
            boolean oldPasswordMatches = oldPassword.matches(pwdPattern);
            // 判断新密码规则
            boolean newPasswordMatches = newPassword.matches(pwdPattern);
            if (!oldPasswordMatches) {
                response.setCodeMessage(ResponseCode.USER_PASSWORD_ERROR);
            } else if (!newPasswordMatches) {
                response.setCodeMessage(ResponseCode.USER_PASSWORD_ERROR);
            } else if (oldPassword.equals(newPassword)) {
                // 新旧密码一致
                response.setCodeMessage(ResponseCode.USER_PASSWORLD_OLD_NEW_SAME);
            } else {
                // 判断旧密码是否正确
                Map map = new HashMap();
                map.put("userId", userId);
                UserInfo userInfo = userInfoDao.getUserInfo(map);
                if (userInfo == null) {
                    response.setCodeMessage(ResponseCode.USER_NOT_EXIST);
                } else {
                    String pwd = userInfo.getPassword();
                    // 加密后的旧密码与原密码进行对比
                    if (PasswordCryptUtil.crypt(oldPassword).equals(pwd)) {
                        String passWord = PasswordCryptUtil.crypt(newPassword);
                        map.put("password", passWord);
                        // 更新密码
                        userInfoDao.updatePassword(map);
                    } else {
                        response.setCodeMessage(ResponseCode.USER_PASSWORLD_OLD_WRONG);
                    }
                }

            }
        } catch (Exception e) {
            response.setCodeMessage(ResponseCode.FAILURE);
            logger.error("[UserInfoServiceImpl.modifyPassword]", e);
        }

        return response;
    }

    @Override
    public UserAccountInfoVO getUserAccountInfoByPhoneNumOrUsername(String username, String phoneNum) {
        Map<String, String> paramMap = new HashMap<>();
        if (StringUtils.isNotBlank(username)) {
            paramMap.put("username", username);
        } else {
            paramMap.put("phoneNum", phoneNum);
        }
        // 根据密码或登陆名查询用户信息
        UserAccountInfoVO userAccountInfoVO = userInfoDao.getUserAccountInfo(paramMap);
        return userAccountInfoVO;
    }



    /**
     * 将数据表的TG、BTG转为0、1
     */
    private String convertResultStatus(String status) {
        if (StringUtils.isBlank(status) || status.equals(Status.BTG.toString())) {
            return "0";
        } else {
            return "1";
        }
    }

    @Override
    public boolean checkPhone(String phone) {
        Map map = new HashMap();
        map.put("phoneNum", phone);
        if (this.userInfoDao.getUserCount(map) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void updatePhoneSecurity(String phone, String userId, String status) {
        Map map = new HashMap();
        map.put("phoneNum", phone);
        map.put("userId", userId);
        userInfoDao.updateUser(map);

        map.put("phoneAuth", status);
        userSecurityDao.updateUserSecurity(map);
    }

    @Override
    public void updateEmailSecurity(String email, String userId, String status) {
        Map map = new HashMap();
        map.put("email", email);
        map.put("userId", userId);
        userInfoDao.updateUser(map);

        map.put("emailAuth", status);
        userSecurityDao.updateUserSecurity(map);
    }

    @Override
    public int matchVerifyCodeErrorCount(int sendType, String sendTo) {
        Map map = new HashMap();
        map.put("sendType", sendType);
        map.put("sendTo", sendTo);
        return checkErrorDao.matchVerifyCodeErrorCount(map);
    }

    @Override
    public void insertMatchVerifyCodeError(CheckError checkError) {
        checkErrorDao.insertMatchVerifyCodeError(checkError);
    }

    @Override
    public boolean checkEmail(String email) {
        Map map = new HashMap();
        map.put("email", email);
        if (this.userInfoDao.getUserCount(map) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void insertSendEmail(String userId, String email, String resource) {
        /**
         * 邮件信息
         */
        String subject = Sender.get("email.authentication.subject");//邮件标题
        int type = Integer.parseInt(Sender.get("email.authentication.type"));//邮件类型
        Timestamp outDate = new Timestamp(System.currentTimeMillis() / 1000 * 1000 + 24 * 60 * 60 * 1000);
        EmailParam param = new EmailParam();
        param.setUserId(userId);
        param.setExceedTime(outDate);
        param.setType(type);
        param.setEmail(email);
        param.setRedirecturl(resource + "/user/checkEmailAuth");
        String sign = GenerateLinkUtils.generateCheckcode(param);
        param.setSign(sign);

        String link = GenerateLinkUtils.generateEmailCheckLink(param);//验证的链接
        String content = Sender.get("email.authentication.content").replace("#link", link);
        /**
         * 添加发送记录
         */
        SendEmail sendEmail = new SendEmail();
        sendEmail.setSubject(subject);
        sendEmail.setContent(content);
        sendEmail.setSendStatus("Y");
        sendEmail.setSender(Integer.parseInt(userId));
        sendEmail.setCreateDate(new Date());
        sendEmail.setOutDate(outDate);
        sendEmail.setType(type);
        this.sendEmailDao.addSendEmail(sendEmail);

        /**
         * 发送邮件
         */
        EmailUtils.sendEmail(subject, email, link);
    }

    @Override
    public SendEmail getLastEmail(String type, String userId) {
        Map map = new HashMap();
        map.put("type", type);
        map.put("userId", userId);
        List<SendEmail> list = this.sendEmailDao.getList(map);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public UserSecurityAuthentication getUserSecurity(String userId) {
        return this.userSecurityDao.getUserSecurity(userId);
    }

    @Override
    public String uploadUserPic(int userId, MultipartFile file, int type, String userPicUrl) throws IOException {
        if (type == 1) {//第三方头像
            TUserIcon icon = new TUserIcon();
            icon.setPicUrl(userPicUrl);
            icon.setType(type);
            icon.setUserId(userId);
            this.userIconDao.insertUserIcon(icon);
            return userPicUrl;
        }
        if (null != file) {
            //添加附件信息
            String originalFileName = file.getOriginalFilename();
            String suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
            AccessoryInfo info = new AccessoryInfo();
            info.setSuffix(suffix);
            info.setType(InterfaceConst.USER_IMAGE_TYPE);
            info.setCreateTime(new Date());
            int id = accessoryInfoDao.insertAccessoryInfo(info);

            if (id > 0) {
                //更新个人基础信息中的头像编码
                FileInformation information = FileUtils.getByDate(info.getCreateTime());
                information.setSuffix(suffix);
                information.setFileType(InterfaceConst.USER_IMAGE_TYPE);
                information.setId(info.getId());
                String newCode = FileUtils.newCode(information);

                Map map = new HashMap();
                map.put("headPicCode", newCode);
                map.put("userId", userId);
                userBaseInfoDao.updateUserBaseInfo(map);

                //上传图片
                information.setBasepath(Config.get("resources.server.path") + Config.get("user.avatar.path"));
                String picUrl = FileUtils.savepic(file.getInputStream(), information);

                TUserIcon icon = new TUserIcon();
                icon.setPicFilename(newCode);
                icon.setType(type);
                icon.setPicUrl(picUrl);
                icon.setUserId(userId);
                userIconDao.insertUserIcon(icon);

                return picUrl;
            }
        }
        return null;
    }

    @Transactional
    @Override
    public void sendVerifySms(String phoneNum, int type, String userId,String userIp) {
        String randomNum = CommonTool.randomNumber(4);
        String content="";
        CaptchaType captchaType = CaptchaType.getByCode(type);
        if (captchaType != null) {
            content = Sender.get(captchaType.getTemplateKey()).replace("#code", randomNum);
        } else {
            logger.warn("phoneNum:{}发送验证码类型type:{}错误",new Object[]{phoneNum,type});
            return;
        }

        Timestamp outTime = new Timestamp(System.currentTimeMillis() / 1000 * 1000 + 30 * 60 * 1000);//验证码过期时间30分钟
        SendSmsRecord record = new SendSmsRecord();
        record.setContent(content);
        record.setCreateTime(new Date());
        record.setStatus("W");
        record.setType(type);
        record.setOutTime(outTime);
        record.setUserId(StringUtils.isNotEmpty(userId) ? Integer.valueOf(userId) : null);
        this.sendSmsRecordDao.insertSendSmsRecord(record);

        SendSmsRecordExt recordExt = new SendSmsRecordExt();
        recordExt.setId(record.getId());
        recordExt.setPhoneNum(phoneNum);
        this.sendSmsRecordExtDao.insertSendSmsRecordExt(recordExt);

        //添加短信验证码记录
        SmsValidcode code = new SmsValidcode();
        code.setPhoneNum(phoneNum);
        code.setSendType(type);
        code.setValidCode(randomNum);
        code.setOutTime(outTime);
        code.setUserIp(userIp);
        this.smsValidCodeDao.addSmsCode(code);
    }

    @Override
    public int getSendPhoneCount(String phone, int type) {
        Map map = new HashMap();
        map.put("phone", phone);
        map.put("type", type);
        return sendSmsRecordDao.userSendPhoneCount(map);
    }

    @Override
    public UserInfo getUser(String phoneNum, String email,String userId) {
        Map map = new HashMap();
        if (!StringUtils.isEmpty(phoneNum)) {
            map.put("phoneNum", phoneNum);
        }
        if (!StringUtils.isEmpty(email)) {
            map.put("email", email);
        }if (!StringUtils.isEmpty(userId)) {
            map.put("userId", userId);
        }
        return this.userInfoDao.getUserInfo(map);
    }

    @Override
    public SmsValidcode getLastSmsCode(String phoneNum, String type) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phoneNum", phoneNum);
        map.put("type", type);
        List<SmsValidcode> list = this.smsValidCodeDao.getCode(map);
        if (null != list && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public HttpResponse modifyNickName(Integer userId, String nickname) {
        Map<String, Object> params = new HashMap<String, Object>();
        HttpResponse respone = new HttpResponse();
        params.put("userId", userId);
        params.put("nickname", nickname);
        int result = 0;
        result = this.userInfoDao.modifyNickName(params);
        if (result < 1) {
            respone.setMessage("修改昵称失败");
        }
        return respone;
    }

    @Override
    public boolean verify(Integer userId, String password) throws Exception {
        HttpResponse respone = new HttpResponse();
        if (StringUtils.isBlank(password)) {
            return false;
        }
        // 密码进行AES解码后加密
        password = PasswordCryptUtil.cryptAESPassword(password);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("password", password);
        Integer result = userInfoDao.verify(params);
        if (result == null) {
            return false;
        }
        return true;
    }

    @Override
    public List<PrivateMessage> getUserMessage(String userId, String timestamp, String isUp, VersionTypeEnum versionType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("isUp", isUp);
        if (StringUtils.isNotEmpty(timestamp)) {
            map.put("time", DateUtil.timestampToDate(Long.valueOf(timestamp)));
        }
        map.put("status", new String[]{Status.YD.name(),Status.WD.name()});
        map.put("versionType", versionType == null ? VersionTypeEnum.PT.getIndex() : versionType.getIndex());

        List<PrivateMessage> list = this.privateMessageDao.getMessageByUserId(map);
        
        //修改用户所有消息为已读
        Map<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("status", Status.YD.name());
        updateMap.put("userId", userId);
        updateMap.put("whereStatus", Status.WD.name());
        this.privateMessageDao.updateMessageStatus(updateMap);
        
        return list;
    }

    @Override
    public PlatformInfoVo getPlatformInfo(String userId,boolean userInvalid) {
        PlatformInfoVo vo = new PlatformInfoVo();
        List<TProductInfo> list = this.productInfoDao.getProductInfo();
        List<ProductInfoVo> infoList = new ArrayList<ProductInfoVo>();
        for (TProductInfo info : list) {
        	TEnum enumEn = this.enumDao.getEnum("t_product_info", "f_status", String.valueOf(info.getStatus()));
        	
            ProductInfoVo infoVo = new ProductInfoVo();
            
            if(info.getType()==3){//体验金
            	if(!userInvalid){//用户已登录状态
            		Map<String, Object> expParams = new HashMap<>();
            		expParams.put("userId", userId);
            		expParams.put("status", InterfaceConst.EXPERIENCEGOLD_YES_TYPE);
            		List<ExperienceGoldInfo>  expGoldList = experienceGoldDaoImpl.getExperienceGolds(expParams);
            		if(expGoldList==null||expGoldList.size()==0){
            			enumEn = this.enumDao.getEnum("t_product_info", "f_status","4");
            		}
            	}
            }
            if(null == enumEn){
            	continue;
            }
            infoVo.setProType(String.valueOf(info.getType()));
            infoVo.setProStatus(enumEn.getEnumValue());
            infoVo.setRemain(Integer.parseInt(enumEn.getEnumKey()));
            infoList.add(infoVo);
        }
        vo.setProsList(infoList);

        //需获取平台总投资金额
        Map<String, Object> investMap = new HashMap<String, Object>();
        investMap.put("status", new String[]{Status.YDF.name(), Status.YJQ.name(), Status.HKZ.name()});
        BigDecimal investMoney = this.shopTreasureDao.getInvestStatistics(investMap);
        vo.setPlatformSum(investMoney.setScale(0,BigDecimal.ROUND_HALF_UP).longValue());

        //获取用户总收益
        Map<String, Object> earntMap = new HashMap<String, Object>();
        earntMap.put("feeTypes", new int[]{InterfaceConst.TZ_LX, InterfaceConst.TZ_WYJ, InterfaceConst.TZ_FX});
        earntMap.put("status", Status.YH.name());
        BigDecimal earnMoney = this.shopTreasureDao.getEarnStatistics(earntMap);
        vo.setPlatformEaring(earnMoney.setScale(0, BigDecimal.ROUND_HALF_UP).longValue());
        return vo;
    }

    @Override
    public List<ShopTreasureVo> getShopTreasureList(String time,String userId,String isUp) {
        List<ShopTreasureVo> voList = new ArrayList<ShopTreasureVo>();

        //3个月
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("limit", 1);
        map.put("status", new String[]{Status.TBZ.toString()});
        map.put("proType", InterfaceConst.PRO_TYPE_KDB);
        //map.put("userType", InterfaceConst.USER_TYPE_FZRR);
        map.put("month", 3);
        ShopTreasureInfo month3 = shopTreasureDao.getShopTreasure(map);
        if (null != month3) {
            ShopTreasureVo vo3 = new ShopTreasureVo();
            vo3.setKdbPlantId(month3.getId());
            vo3.setKdbPlanTitle(month3.getName());
            vo3.setKdbSum(month3.getLoanAmount().doubleValue());
            vo3.setBuySum(month3.getLoanAmount().subtract(month3.getVoteAmount()).doubleValue());
            vo3.setKdbYield(String.valueOf(month3.getRate()));
            vo3.setKdbStatus(1);
            vo3.setKdbType(InterfaceConst.KDB_MONTH3_TYPE);
            vo3.setTimestamp(String.valueOf(month3.getFundraisDate().getTime()));
            
            //用户是否申购该期的开店宝计划
            if(StringUtils.isNotEmpty(userId)){
            	Map<String, Object> finacingMap = new HashMap<String, Object>();
                finacingMap.put("userId", userId);
                finacingMap.put("proType", InterfaceConst.PRO_TYPE_KDB);
                finacingMap.put("bidId", month3.getId());
                List<Finacing> finacingList=this.finacingDao.getFinacingByBid(finacingMap);
                if(null != finacingList&&finacingList.size()>0){
                	vo3.setKdbUserStatus(1);
                }else{
                	vo3.setKdbUserStatus(0);
                }
            }
            
            voList.add(vo3);
        }

        //6个月
        map.put("month", 6);
        ShopTreasureInfo month6 = shopTreasureDao.getShopTreasure(map);
        if (null != month6) {
            ShopTreasureVo vo6 = new ShopTreasureVo();
            vo6.setKdbPlantId(month6.getId());
            vo6.setKdbPlanTitle(month6.getName());
            vo6.setKdbSum(month6.getLoanAmount().doubleValue());
            vo6.setBuySum(month6.getLoanAmount().subtract(month6.getVoteAmount()).doubleValue());
            vo6.setKdbYield(String.valueOf(month6.getRate()));
            vo6.setKdbStatus(1);
            vo6.setKdbType(InterfaceConst.KDB_MONTH6_TYPE);
            vo6.setTimestamp(String.valueOf(month6.getFundraisDate().getTime()));
            
            //用户是否申购该期的开店宝计划
            if(StringUtils.isNotEmpty(userId)){
            	Map<String, Object> finacingMap = new HashMap<String, Object>();
                finacingMap.put("userId", userId);
                finacingMap.put("proType", InterfaceConst.PRO_TYPE_KDB);
                finacingMap.put("bidId", month6.getId());
                List<Finacing> finacingList=this.finacingDao.getFinacingByBid(finacingMap);
                if(null != finacingList&&finacingList.size()>0){
                	vo6.setKdbUserStatus(1);
                }else{
                	vo6.setKdbUserStatus(0);
                }
            }
            
            voList.add(vo6);
        }

        //12个月
        map.put("month", 12);
        ShopTreasureInfo month12 = shopTreasureDao.getShopTreasure(map);
        if (null != month12) {
            ShopTreasureVo vo12 = new ShopTreasureVo();
            vo12.setKdbPlantId(month12.getId());
            vo12.setKdbPlanTitle(month12.getName());
            vo12.setKdbSum(month12.getLoanAmount().doubleValue());
            vo12.setBuySum(month12.getLoanAmount().subtract(month12.getVoteAmount()).doubleValue());
            vo12.setKdbYield(String.valueOf(month12.getRate()));
            vo12.setKdbStatus(1);
            vo12.setKdbType(InterfaceConst.KDB_MONTH12_TYPE);
            vo12.setTimestamp(String.valueOf(month12.getFundraisDate().getTime()));
            
            //用户是否申购该期的开店宝计划
            if(StringUtils.isNotEmpty(userId)){
            	Map<String, Object> finacingMap = new HashMap<String, Object>();
                finacingMap.put("userId", userId);
                finacingMap.put("proType", InterfaceConst.PRO_TYPE_KDB);
                finacingMap.put("bidId", month12.getId());
                List<Finacing> finacingList=this.finacingDao.getFinacingByBid(finacingMap);
                if(null != finacingList&&finacingList.size()>0){
                	vo12.setKdbUserStatus(1);
                }else{
                	vo12.setKdbUserStatus(0);
                }
            }
            voList.add(vo12);
        }

        //已满人
        String[] closeStatus = new String[]{Status.DFK.name(), Status.HKZ.name(), Status.YJQ.name()};
        Map<String, Object> closeMap = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(time)) {
            closeMap.put("time", DateUtil.timestampToDate(Long.valueOf(time)));
        }
        closeMap.put("status", closeStatus);
        closeMap.put("proType", InterfaceConst.PRO_TYPE_KDB);
        closeMap.put("isNoviceBid", Status.F.name());
        closeMap.put("limit", 10);
        List<ShopTreasureInfo> list = this.shopTreasureDao.getCloseShopTreasure(closeMap);
        for (ShopTreasureInfo info : list) {
            ShopTreasureVo vo = new ShopTreasureVo();
            vo.setKdbPlantId(info.getId());
            vo.setKdbPlanTitle(info.getName());
            vo.setKdbSum(info.getLoanAmount().doubleValue());
            vo.setBuySum(info.getLoanAmount().subtract(info.getVoteAmount()).doubleValue());
            vo.setKdbYield(String.valueOf(info.getRate()));
            vo.setTimestamp(String.valueOf(info.getFundraisDate().getTime()));
            
            //开店宝计划状态
            if(info.getStatus().equals(Status.HKZ.name())){
            	vo.setKdbStatus(4);
            }
            if(info.getEndDate()!=null||info.getStatus().equals(Status.YJQ.name())){
            	vo.setKdbStatus(5);
            }
            if(info.getStatus().equals(Status.DFK.name())){
            	if(info.getVoteAmount().intValue()==0&&info.getFundraisDate().getTime()>DateUtil.nowDate().getTime()){//计划金额已满，筹款到期日期未到
                	vo.setKdbStatus(2);
                }else if(info.getVoteAmount().intValue()>0&&info.getFundraisDate().getTime()<DateUtil.nowDate().getTime()){//计划金额未满，筹款到期日期已到
                	vo.setKdbStatus(3);
                }else{
                	vo.setKdbStatus(3);
                }
            }
            
            //开店宝计划类型
            if(info.getMonth()==12){
            	vo.setKdbType(InterfaceConst.KDB_MONTH12_TYPE);
            }else if(info.getMonth()==6){
            	vo.setKdbType(InterfaceConst.KDB_MONTH6_TYPE);
            }else if(info.getMonth()==3){
            	vo.setKdbType(InterfaceConst.KDB_MONTH3_TYPE);
            }
            
            //用户是否申购该期的开店宝计划
            if(StringUtils.isNotEmpty(userId)){
            	Map<String, Object> finacingMap = new HashMap<String, Object>();
                finacingMap.put("userId", userId);
                finacingMap.put("proType", InterfaceConst.PRO_TYPE_KDB);
                finacingMap.put("bidId", info.getId());
                List<Finacing> finacingList=this.finacingDao.getFinacingByBid(finacingMap);
                if(null != finacingList&&finacingList.size()>0){
                	vo.setKdbUserStatus(1);
                }else{
                	vo.setKdbUserStatus(0);
                }
            }
            
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public ShopTreasureInfoVo getShopTreasureInfo(String id, String userId) {
        ShopTreasureInfoVo vo = new ShopTreasureInfoVo();

        //获取开店宝计划详情
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        //map.put("userType", InterfaceConst.USER_TYPE_FZRR);
        map.put("proType", InterfaceConst.PRO_TYPE_KDB);//产品类型
        ShopTreasureInfo info = this.shopTreasureDao.getShopTreasureInfo(map);
        if (null == info) {
            return null;
        }
        vo.setKdbSum(info.getLoanAmount().doubleValue());
        vo.setKdbPlantId(info.getId());
        vo.setKdbPlanTitle(info.getName());
        vo.setBuySum(info.getLoanAmount().subtract(info.getVoteAmount()).doubleValue());
        vo.setKdbYield(String.valueOf(info.getRate()));
        vo.setShopInfoUrl(Config.get("shop.info.url")+info.getId());
        
        if(info.getMonth()==12){
        	vo.setKdbType(InterfaceConst.KDB_MONTH12_TYPE);//开店宝类型
        	vo.setKdbfwxyUrl(Config.get("shop.serviceAgreementC.url"));//开店宝服务协议
        }else if(info.getMonth()==6){
        	vo.setKdbType(InterfaceConst.KDB_MONTH6_TYPE);
        	vo.setKdbfwxyUrl(Config.get("shop.serviceAgreementB.url"));
        }else if(info.getMonth()==3){
        	vo.setKdbType(InterfaceConst.KDB_MONTH3_TYPE);
        	vo.setKdbfwxyUrl(Config.get("shop.serviceAgreementA.url"));
        }
        
        //开店宝计划状态
        if(info.getStatus().equals(Status.TBZ.name())){
        	vo.setKdbStatus(1);
        }else if(info.getStatus().equals(Status.HKZ.name())){
        	vo.setKdbStatus(4);
        }else if(info.getStatus().equals(Status.DFK.name())){
        	if(info.getVoteAmount().intValue()==0&&info.getFundraisDate().getTime()>DateUtil.nowDate().getTime()){//计划金额已满，筹款到期日期未到
            	vo.setKdbStatus(2);
            }else if(info.getVoteAmount().intValue()>0&&info.getFundraisDate().getTime()<DateUtil.nowDate().getTime()){//计划金额未满，筹款到期日期已到
            	vo.setKdbStatus(3);
            }else{
            	vo.setKdbStatus(3);
            }
        }else if(info.getEndDate()!=null||info.getStatus().equals(Status.YJQ.name())){
        	vo.setKdbStatus(5);
        }else{
        	vo.setKdbStatus(3);
        }
  
        vo.setBuyTimestamp(DateUtil.nowDate().getTime());
        if(null!=info.getFundraisDate()){//筹款到期时间
        	vo.setInterestTimestamp(DateUtil.dateAdd(info.getFundraisDate(),1).getTime());//计息时间
        	
        	if("S".equals(info.getIsNoviceBid())){
        		vo.setEndTimestamp(DateUtil.dateAdd(info.getFundraisDate(),info.getLoanDays()+1).getTime());//到期时间
        	}else{
        		vo.setEndTimestamp(DateUtil.dateAdd(DateUtil.monthAdd(info.getFundraisDate(),info.getMonth()),1).getTime());//到期时间
        	}
        }
        
        //判断当前用户是否可以申购
		if(StringUtils.isNoneEmpty(userId)){
			BidInfo bidInfo = salaryService.isUserCanBid(Integer.valueOf(id), Integer.valueOf(userId));
            if(bidInfo!=null){
            	vo.setCanbuy(1);
          	}else{
          		vo.setCanbuy(0);
          		//判断新手是否新手购买
          		if("S".equals(info.getIsNoviceBid())){
          			int investId = shopTreasureDao.isUserInvest(Integer.valueOf(userId));
              		if(investId>0){
              			vo.setCanbuy(1);
              		}
            	}
          	}
		}

        //获取用户账户余额
        if (StringUtils.isNotEmpty(userId)) {
            Map<String, String> userMap = new HashMap<String, String>();
            userMap.put("userId", userId);
            userMap.put("type", InterfaceConst.ACCOUNT_TYPE_WLZH);
            UserAccount account = userAccountDao.getUserAccount(userMap);
            if (null != account) {
                vo.setBalance(account.getBalance());
            }
        }
        vo.setCpmxUrl(Config.get("shop.detail.url")+id);
        
        //判断是否是新手标  (laubrence 2015-12-15 17:40:28)
        vo.setIsNoviceBid(0);
        if("S".equals(info.getIsNoviceBid())){
            vo.setIsNoviceBid(1);
            vo.setKdbfwxyUrl(Config.get("shop.novicebid.serviceAgreement.url"));
        }
        vo.setLoanDays(info.getLoanDays());
        
        return vo;
    }
    
    

	@Override
	public String getLoginPassword(int userId) {
		Map<String, Object> params =new HashMap<String, Object>();
		params.put("userId", userId);
		UserInfo user = userInfoDao.getUserInfo(params);
		return user.getPassword();
	}

    @Override
	public ResponseCode verifySmsCode(String phoneNum, String type, String smsCode) {
		SmsValidcode code = this.getLastSmsCode(phoneNum, type);
		if (null != code) {
			long outTime = code.getOutTime().getTime();
			if (outTime <= System.currentTimeMillis()) {
				return ResponseCode.COMMON_CAPTCHA_TIMEOUT;
			} else if(code.getValidCode().equals(smsCode)) {
				return null;
			} else {
				//添加验证错误记录
				CheckError checkError=new CheckError();
				checkError.setSendType(1);
				checkError.setSendTo(phoneNum);
				checkError.setVeryCode(smsCode);
				checkError.setCrateTime(new Date());
				this.insertMatchVerifyCodeError(checkError);
                return ResponseCode.COMMON_CAPTCHA_INVALID;
			}
		}
		return ResponseCode.COMMON_CAPTCHA_INVALID;
	}

    @Override
    @Transactional(readOnly = false)
    public void insertlianLianAuth(Map authMap) throws Exception {
        userInfoDao.insertlianLianAuth(authMap);
    }

    /**
     * 获取保存的实名记录的错误
     * @param userName
     */
    public int getUserLoginError(String userName)throws Exception {
        return  userInfoDao.getUserLoginError(userName);
    }

    /**
     * 获取连连实名认证的记录次数
     * @param id
     */
    public int getUserAuthError(int id)throws Exception {
        return  userInfoDao.getUserAuthError(id);
    }

    /**
     * 更新的实名记录的错误
     * @param userName
     */
    @Transactional(readOnly = false)
    public void updateUserLoginError(String userName,String addr) throws Exception{
        Map map = new HashMap();
        if (!StringUtils.isEmpty(userName)) {
            map.put("userName", userName);
        }
        if (!StringUtils.isEmpty(addr)) {
            map.put("addr", addr);
        }
        userInfoDao.updateUserLoginError(map);
    }

	@Override
	public int getUserMessageCount(String userId, String status, VersionTypeEnum vte) {
		Map<String, Object> params =new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("status", status);
        params.put("versionType", vte == null ? VersionTypeEnum.PT.getIndex() : vte.getIndex());

		return this.privateMessageDao.getUserMessageCount(params);
	}

	@Override
	public void updateUserMessageStatus(String messageIds, int userId, String status) {
		 Map<String,Object> map = new HashMap<String,Object>();
		 map.put("userId", userId);
		 map.put("status", status);
		 if(StringUtils.isNotEmpty(messageIds)){
			 map.put("messageIds", messageIds.split(","));
		 }
		 this.privateMessageDao.updateMessageStatus(map);
	}

	@Override
	public boolean verifyIdentity(String identityNo, String identityName, Integer userId) throws Throwable {
		identityNo = StringHelper.encode(identityNo);
		return this.userInfoDao.verifyIdentity(identityNo, identityName, userId);
	}

	@Override
	public UserAssetInfoVO getUserAssetInfo(String userId) {
		UserAssetInfoVO result = null;
        if (StringUtils.isNotBlank(userId)) {
            // 获取资金账户(T6101)的往来账户余额
            String accountType = InterfaceConst.ACCOUNT_TYPE_WLZH;
            UserAccount account = getUserAccount(userId, accountType);
            // 设置用户余额
            if (null != account) {
                result = new UserAssetInfoVO();
                // 用户余额
                BigDecimal balance = account.getBalance();
                result.setBalance(balance);
                // 根据用户ID获取标的状态为投标中的投标记录金额(投资冻结金额)。
                BigDecimal tenderFreezeSum = bidExtendDao.getTenderFreezeSum(userId);
                result.setTzdjSum(tenderFreezeSum);
                // 提现冻结金额
                BigDecimal withdrawFreezeSum = withdrawDao.getWithdrawFreezeSum(userId,CGModeEnum.PT.getName());
                result.setTxdjSum(withdrawFreezeSum);
                // 现金资产总额
                BigDecimal cachSum = balance.add(tenderFreezeSum).add(withdrawFreezeSum);
                result.setCashSum(cachSum);

                // 开店宝投资总额
                BigDecimal kdbInvestSum = getKdbInvestSum(userId);
                result.setKdbSum(kdbInvestSum);
                //债权转让投资总额
                BigDecimal zqzrAssets = finacingService.getZqzrAssets(userId);
                result.setZqzrAssets(zqzrAssets);
                // 投资总额(开店宝总额+薪金宝总额)
                BigDecimal investSum = kdbInvestSum.add(zqzrAssets);
                result.setInvestSum(investSum);

                // 开店宝昨日收益
                BigDecimal kdbEarning = finacingService.getKdbEarning(userId, null);
                result.setKdbEarning(kdbEarning);
                //债权转让昨天收益
                BigDecimal zqzrEarnings = finacingService.getZqzrEarning(userId);
                result.setZqzrEarnings(zqzrEarnings);
                // 昨日总收益(活期宝昨日收益+开店宝昨日收益+薪金宝昨日收益)
                BigDecimal yesterdayEarningSum = kdbEarning.add(zqzrEarnings);
                result.setYesterdayEarningSum(yesterdayEarningSum);
                // 累计总收益
                BigDecimal earningSum = getEarningSum(userId);
                result.setEarningSum(earningSum);

                // 用户总资产(投资资产+现金资产总额)
                BigDecimal totalSum = investSum.add(cachSum);
                result.setTotalSum(totalSum);
            }
        }
        return result;
	}

	@Override
	public Auth getAuthStatus(int userId) {
		return this.userInfoDao.getAuthStatus(userId);
	}

	@Override
	public List<ShopTreasureInfo> getCanInvestBidList() {
		List<ShopTreasureInfo> list = new ArrayList<ShopTreasureInfo>();
		
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("limit", 1);
        map.put("status", new String[]{Status.TBZ.toString()});
        map.put("proType", InterfaceConst.PRO_TYPE_KDB);
        map.put("isNoviceBid", Status.F.name());
        map.put("month", 3);
        
        ShopTreasureInfo month3 = shopTreasureDao.getShopTreasure(map);//3个月借款期限的开店宝
        if(null != month3){
        	list.add(month3);
        }
        
        map.put("month", 6);
        ShopTreasureInfo month6 = shopTreasureDao.getShopTreasure(map);//6个月借款期限的开店宝
        if(null != month6){
        	list.add(month6);
        }
        
        map.put("month", 12);
        ShopTreasureInfo month12 = shopTreasureDao.getShopTreasure(map);//12个月借款期限的开店宝
        if(null != month12){
        	list.add(month12);
        }
        
		return list;
	}

    @Override
    public UserInfo getUserInfo(String phoneNum){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("phoneNum",phoneNum);
        return userInfoDao.getUserInfo(map);
    }

	@Override
	public List<ShopTreasureInfo> getBidList(String time,String[] status,String proType,int limit,String isNoviceBid) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(time)) {
        	map.put("time", DateUtil.timestampToDateBySec(Long.valueOf(time)));
        }
        if(StringUtils.isNotEmpty(isNoviceBid)){
        	map.put("isNoviceBid", isNoviceBid);
        }
        map.put("status", status);
        map.put("proType", proType);
        map.put("limit", limit);
        List<ShopTreasureInfo> list = this.shopTreasureDao.getCloseShopTreasure(map);
		return list;
	}

	@Override
	public int getSendSmsCount(String userIp, String phoneNum, Date halfhour, Date day) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(userIp)){
			map.put("userIp", userIp);
		}
		if(StringUtils.isNotEmpty(phoneNum)){
			map.put("phoneNum", phoneNum);
		}
		if(null != halfhour){
			map.put("halfhour", halfhour);
		}
		if(null != day){
			map.put("day", day);
		}
		return this.smsValidCodeDao.getSendSmsCount(map);
	}

    /**
     * 注释的代码先不要删，不知产品哪天又要改回来！！
     * @param userId
     * @return
     */
	@Override
	public Map<String, Object> getUserAssets(Integer userId) {
		Map<String, Object> data = new LinkedHashMap<>();
//		BigDecimal totalInvestment = BigDecimal.ZERO;
//		BigDecimal amount = BigDecimal.ZERO;
//		AccountAssetsForm form;
//		List<AccountAssetsForm> total = new ArrayList<>();
//		List<AccountAssetsForm> cash = new ArrayList<>();
		
		BigDecimal balance = userAccountDao.getWLZHBalance(userId); //连连余额
        //3.1.0版本之前的投资冻结金额，无法核算未匹配的计划的冻结金额
        //BigDecimal tenderFreezeAmount = bidExtendDao.getTenderFreezeSum(userId.toString()); //投资冻结金额

        BigDecimal withdrawFreezeAmount = withdrawDao.getWithdrawFreezeSum(userId.toString(), CGModeEnum.PT.getName()); //提现冻结金额


        //3.2.0版本投资冻结金额（总冻结金额减去提现冻结金额）
        //BigDecimal userFreezeSum = userAccountDao.getSdUserFreezeSum(userId).add(userAccountDao.getPlanFreezeSum(userId)).subtract(withdrawFreezeAmount);
        BigDecimal userFreezeSum = userAccountDao.getNewTenderFreezeSum(userId,VersionTypeEnum.PT.getIndex());
//        BigDecimal totalCash = balance.add(tenderFreezeAmount).add(withdrawFreezeAmount); //现金总资产

       // BigDecimal totalGainsYesterday = BigDecimal.ZERO;
        BigDecimal totalGainsYesterday = tradeService.getEarningsYesterdaySum(userId); //昨天总收益
//        BigDecimal totalEarning = tradeService.getAccumulativeEarnings(userId); //总收益
        //（这里应该获取还款计划里面的原始债权的金额，不能统计购买时的价格！！！！）
//        BigDecimal zqzrAssets = finacingService.getZqzrAssets(userId.toString()); //债权转让投资总额

        BigDecimal dueInPrincipal = BigDecimal.ZERO;
        BigDecimal dueInGains = BigDecimal.ZERO;
//        Map<String, Object> gains = tradeService.getEarningsRecordList(userId); //这里的收益是动态的，2.0不需要
        //DueInAmount dueInAmount = tradeService.getDueInAmount(userId); //3.1.0之前的待收本息
        //BigDecimal YHGains =  tradeService.getYHGains(String.valueOf(userId)); //3.1.0之前的已获收益
        DueInAmount dueInAmount = tradeService.getNewDueInAmount(userId,VersionTypeEnum.PT.getIndex());//3.2.0版本适用待收本息
        DueInAmount planDueInAmount = tradeService.getPlanDueInAmount(userId,VersionTypeEnum.PT.getIndex());//计划待收本息
        BigDecimal YHGains =  tradeService.getNewYHGains(String.valueOf(userId),VersionTypeEnum.PT.getIndex());//3.2.0已获收益
        BigDecimal PlanYHGains =  tradeService.getPlanYHGains(String.valueOf(userId),VersionTypeEnum.PT.getIndex());
        BigDecimal totalAssets = balance.add(userFreezeSum).add(withdrawFreezeAmount);
        if (dueInAmount != null&&planDueInAmount!=null) {
            dueInPrincipal = dueInAmount.getPrincipal().add(planDueInAmount.getPrincipal());
            dueInGains = dueInAmount.getGains().add(planDueInAmount.getGains());
            totalAssets = totalAssets.add(dueInPrincipal).add(dueInGains);
        }else if(null ==dueInAmount &&planDueInAmount!=null){
            dueInPrincipal = planDueInAmount.getPrincipal();
            dueInGains = planDueInAmount.getGains();
            totalAssets = totalAssets.add(dueInPrincipal).add(dueInGains);
        }else if(dueInAmount != null&&null == planDueInAmount){
            dueInPrincipal = dueInAmount.getPrincipal();
            dueInGains = dueInAmount.getGains();
            totalAssets = totalAssets.add(dueInPrincipal).add(dueInGains);
        }
        if(PlanYHGains!=null){
            YHGains = PlanYHGains.add(YHGains);
        }
        data.put("totalGainsYesterday", totalGainsYesterday.toString());
        data.put("dueInPrincipal", dueInPrincipal.toString());
        data.put("balance", balance.toString());
       /* data.put("tenderFreezeAmount", tenderFreezeAmount.toString());*/
        data.put("tenderFreezeAmount", userFreezeSum.toString());
        data.put("withdrawFreezeAmount", withdrawFreezeAmount.toString());
        data.put("totalAssets", totalAssets.toString());
        data.put("expectGains", dueInGains.toString());
        data.put("historyGains", YHGains.toString());
        data.put("totalGains", dueInGains.add(YHGains).toString());
//        totalInvestment = totalInvestment.add(zqzrAssets);
        
//        List<AccountAssetsForm> investment = bidExtendDao.getInvestmentAssets(userId);
//        for (AccountAssetsForm i : investment) {
//        	amount = new BigDecimal(i.getAmount());
//        	totalInvestment = totalInvestment.add(amount);
//        }
//		form = new AccountAssetsForm(AccountAssetsForm.ZQZR, zqzrAssets.toString());
//		investment.add(form);
//		data.put(AccountAssetsForm.KEY_TOTAL, total);
//		data.put(AccountAssetsForm.KEY_INVESTMENT, investment);
//        form = new AccountAssetsForm(AccountAssetsForm.TOTAL, totalInvestment.toString());
//        investment.add(form);
//
//        BigDecimal totalAssets = totalInvestment.add(totalCash);
//
//		form = new AccountAssetsForm(AccountAssetsForm.INVESTMENT, totalInvestment.toString());
//		total.add(form);
//		form = new AccountAssetsForm(AccountAssetsForm.CASH, totalCash.toString());
//		total.add(form);
//		form = new AccountAssetsForm(AccountAssetsForm.TOTAL, totalAssets.toString());
//		total.add(form);
//
//		form = new AccountAssetsForm(AccountAssetsForm.BALANCE, balance.toString());
//		cash.add(form);
//		form = new AccountAssetsForm(AccountAssetsForm.INVESTMENT_FREEZE, tenderFreezeAmount.toString());
//		cash.add(form);
//		form = new AccountAssetsForm(AccountAssetsForm.WITHDRAW_FREEZE, withdrawFreezeAmount.toString());
//		cash.add(form);
//		form = new AccountAssetsForm(AccountAssetsForm.TOTAL, totalCash.toString());
//		cash.add(form);
//		data.put(AccountAssetsForm.KEY_CASH, cash);
		
		return data;
	}

    @Override
    public BigDecimal getUserTotalAssets(int userId) {
        BigDecimal balance = userAccountDao.getWLZHBalance(userId); //余额
        BigDecimal tenderFreezeAmount = bidExtendDao.getTenderFreezeSum(String.valueOf(userId)); //投资冻结金额
        BigDecimal withdrawFreezeAmount = withdrawDao.getWithdrawFreezeSum(String.valueOf(userId),CGModeEnum.PT.getName()); //提现冻结金额
        DueInAmount dueInAmount = tradeService.getDueInAmount(userId); //待收本息
        BigDecimal totalAssets = balance.add(tenderFreezeAmount).add(withdrawFreezeAmount);
        if (dueInAmount != null) {//新用户待收金额 dueInAmount 可能为空
            totalAssets = balance.add(tenderFreezeAmount).add(withdrawFreezeAmount)
                    .add(dueInAmount.getGains()).add(dueInAmount.getPrincipal());
        }

        return totalAssets;
    }

    /**
     * @Title: getUserInfo
     * @Description: 获取用户信息
     * @param userId
     * @return
     * @return: UserInfo
     */
    @Override
    public UserInfo getUserInfo(int userId){
        //用户信息
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("userId", userId);
        return this.userInfoDao.getUserInfoByPhoneNumOrUsername(userMap);
    }

    @Override
    public String getUserServiceAgreementUrl(int userId){
        String serviceArgeementUrl = Config.get("userService.agreement.url");
        UserInfo userInfo = getUserInfo(Integer.valueOf(userId));
        if (userInfo == null) {
            return serviceArgeementUrl;
        }
        //用户服务协议参数
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("userName", "");
        if (StringUtils.isNotBlank(userInfo.getFullName())) {
            paramMap.put("userName", userInfo.getFullName().substring(0, 1) + "**");
        }
        paramMap.put("phone", "");
        if (StringUtils.isNotBlank(userInfo.getUsername()) && userInfo.getUsername().length()>7) {
            paramMap.put("phone", com.fenlibao.p2p.util.StringHelper.replace(3, 7, userInfo.getUsername(), "****"));
        }
        paramMap.put("IDNumber", "");
        if (StringUtils.isNotBlank(userInfo.getIdCard())) {
            paramMap.put("IDNumber", userInfo.getIdCard());
        }

        if (StringUtils.isNotBlank(serviceArgeementUrl)) {
            serviceArgeementUrl = CommonTool.convertParamToUrl(serviceArgeementUrl, paramMap);
        }
        return serviceArgeementUrl;
    }

    @Override
    public int checkWhiteBoard(Integer userId) {
        //用户信息
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("userId", userId);
        return this.userInfoDao.checkWhiteBoard(userMap);
    }

    @Override
    public List<Map<String, Object>> getUserBaofooCardInfo(Integer userId) {
        //用户信息
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("userId", userId);
        return this.userInfoDao.getUserBaofooCardInfo(userMap);
    }

    @Override
    public String getUserServicePlanAgreementUrl(int userId, String isNovice,String planType) {
        String serviceArgeementUrl = null;
        if(!StringUtils.isEmpty(isNovice)&&isNovice.equals("S")){
            serviceArgeementUrl = Config.get("userService.novice.url");
        }else{
            if(!StringUtils.isEmpty(planType)&&planType.equals("2")) {
                serviceArgeementUrl = Config.get("userService.shengxin.url");
            }else if(!StringUtils.isEmpty(planType)&&planType.equals("1")){
                serviceArgeementUrl = Config.get("userService.yuesheng.url");
            }else {
                serviceArgeementUrl = Config.get("userService.shengxin.url");
            }
        }

        UserInfo userInfo = getUserInfo(Integer.valueOf(userId));
        if (userInfo == null) {
            return serviceArgeementUrl;
        }
        //用户服务协议参数
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("userName", "");
        if (StringUtils.isNotBlank(userInfo.getFullName())) {
            paramMap.put("userName", userInfo.getFullName().substring(0, 1) + "**");
        }
        paramMap.put("phone", "");
        if (StringUtils.isNotBlank(userInfo.getUsername()) && userInfo.getUsername().length()>7) {
            paramMap.put("phone", com.fenlibao.p2p.util.StringHelper.replace(3, 7, userInfo.getUsername(), "****"));
        }
        paramMap.put("IDNumber", "");
        if (StringUtils.isNotBlank(userInfo.getIdCard())) {
            paramMap.put("IDNumber", userInfo.getIdCard());
        }

        if (StringUtils.isNotBlank(serviceArgeementUrl)) {
            serviceArgeementUrl = CommonTool.convertParamToUrl(serviceArgeementUrl, paramMap);
        }
        return serviceArgeementUrl;
    }

    @Override
    public Map<String, Object> getXWCardInfo(Integer userId) {
        Integer applyTimes = userSecurityDao.getApplyUnbindTimes(userId, UserRole.INVESTOR.getCode());
        XinwangAccount xinwangAccount = xwAccountDao.getXinwangAccount(UserRole.INVESTOR.getCode() + userId);
        XinwangUserInfo userInfo = null;
        if(xinwangAccount != null && xinwangAccount.getImportUserActivate()){//数据库中有该用户，并且已经激活
            userInfo = new XinwangUserInfo();
            userInfo.setErrorCode(null);
            try {
                if(xinwangAccount.getBankcardNo() != null){
                    userInfo.setBankcardNo(StringHelper.decode(xinwangAccount.getBankcardNo()));
                }
            } catch (Throwable throwable) {
                logger.error("解密银行卡错误");
                throwable.printStackTrace();
            }
            userInfo.setIsImportUserActivate(xinwangAccount.getImportUserActivate());
            userInfo.setBankcode(xinwangAccount.getBankcode());
        }else {
            userInfo = xwUserInfoService.queryUserInfo(UserRole.INVESTOR.getCode() + userId);
        }

        Map<String, Object> xwcgInfo = new HashMap<>();
        if (userInfo != null) {
            xwcgInfo.put("isXWOpenAccount", userInfo.getErrorCode()!=null?0:1);
            xwcgInfo.put("isAccountActivity", userInfo.getBankcardNo()!=null?1:0);
            xwcgInfo.put("isImportUserActivate", userInfo.getIsImportUserActivate() ? 1 : 0);
            String cgAccount = userInfo.getBankcardNo();
            xwcgInfo.put("cgAccount", BankCardFormater.format(cgAccount));
            xwcgInfo.put("allowUnbind", applyTimes > 0 ? 1 : 0);

            XWBankInfo bankInfo = xwBankService.getBankInfo(userInfo.getBankcode());
            xwcgInfo.put("bankCode",bankInfo!=null?bankInfo.getBankCode():null);
            xwcgInfo.put("bankName", bankInfo!=null?bankInfo.getBankName():null);

            List<PaymentLimitVO> limitVOList = new ArrayList<>(1);
            if (bankInfo.getBankCode() != null) {
                limitVOList = rechargeService.getLimitList(bankInfo.getBankCode(), SysPaymentInstitution.XW.getChannelCode() + "");
            }
            xwcgInfo.put("singleLimit", limitVOList.size() == 0 ? null : limitVOList.get(0).getSingleLimit());
            xwcgInfo.put("dailyLimit", limitVOList.size() == 0 ? null : limitVOList.get(0).getDailyLimit());
            xwcgInfo.put("monthlyLimit", limitVOList.size() == 0 ? null : limitVOList.get(0).getMonthlyLimit());
        }
        return xwcgInfo;
    }


    @Override
    public String isXWaccount(int userId) {
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("userId", userId);
        return this.userInfoDao.isXWaccount(userMap);
    }

    @Override
    public Map<String, Object> getUserAssetsByXW(Integer userId) {
        Map<String, Object> data = new LinkedHashMap<>();
        BigDecimal balance = userAccountDao.getCgBalance(userId); //新网投资账户余额
        if ( balance == null) {//可能系统没有保存用户的资金账户
            balance = BigDecimal.ZERO;
        }
        StringBuffer pplatform = new StringBuffer("INVESTOR");

        BigDecimal withdrawFreezeAmount = withdrawDao.getWithdrawFreezeSum(String.valueOf(userId),CGModeEnum.CG.getName()); //提现冻结金额
        BigDecimal userFreezeSum = userAccountDao.getNewTenderFreezeSum(userId,VersionTypeEnum.CG.getIndex());
        BigDecimal dueInPrincipal = BigDecimal.ZERO;
        BigDecimal dueInGains = BigDecimal.ZERO;
        DueInAmount dueInAmount = tradeService.getNewDueInAmount(userId,VersionTypeEnum.CG.getIndex());//标待收本息
        DueInAmount planDueInAmount = tradeService.getPlanDueInAmount(userId,VersionTypeEnum.CG.getIndex());//计划待收本息
        BigDecimal YHGains =  tradeService.getNewYHGains(String.valueOf(userId),VersionTypeEnum.CG.getIndex());//标已获收益
        BigDecimal PlanYHGains =  tradeService.getPlanYHGains(String.valueOf(userId),VersionTypeEnum.CG.getIndex());//计划已获收益
        BigDecimal totalAssets = balance.add(userFreezeSum).add(withdrawFreezeAmount);
        if (dueInAmount != null&&planDueInAmount!=null) {
            dueInPrincipal = dueInAmount.getPrincipal().add(planDueInAmount.getPrincipal());
            dueInGains = dueInAmount.getGains().add(planDueInAmount.getGains());
            totalAssets = totalAssets.add(dueInPrincipal).add(dueInGains);
        }else if(null ==dueInAmount &&planDueInAmount!=null){
            dueInPrincipal = planDueInAmount.getPrincipal();
            dueInGains = planDueInAmount.getGains();
            totalAssets = totalAssets.add(dueInPrincipal).add(dueInGains);
        }else if(dueInAmount != null&&null == planDueInAmount){
            dueInPrincipal = dueInAmount.getPrincipal();
            dueInGains = dueInAmount.getGains();
            totalAssets = totalAssets.add(dueInPrincipal).add(dueInGains);
        }
        if(PlanYHGains!=null){
            YHGains = PlanYHGains.add(YHGains);
        }
        data.put("dueInPrincipal", dueInPrincipal.toString());
        data.put("balance", balance.toString());
        data.put("tenderFreezeAmount", userFreezeSum.toString());
        data.put("withdrawFreezeAmount", withdrawFreezeAmount.toString());
        data.put("totalAssets", totalAssets.toString());
        data.put("expectGains", dueInGains.toString());
        data.put("historyGains", YHGains.toString());
        data.put("totalGains", dueInGains.add(YHGains).toString());
        return data;
    }

    @Override
    public UserBaseInfo getuserBaseInfo(Integer userId) {
        return userBaseInfoDao.getBaseInfo(userId);
    }

    @Override
    public List<PlatformUser> getPlatformUserNo(String status,int limit) {
        return userBaseInfoDao.getPlatformUserNo(status,limit);
    }

    @Override
    public String getUserServicePlanAgreementUrlByCG(int userId, String isNovice, String planType) {
        String serviceArgeementUrl = null;
        if(!StringUtils.isEmpty(isNovice)&&isNovice.equals("S")){
            serviceArgeementUrl = Config.get("userService.noviceCG.url");
        }else{
            if(!StringUtils.isEmpty(planType)&&planType.equals("2")) {
                serviceArgeementUrl = Config.get("userService.shengxinCG.url");
            }else if(!StringUtils.isEmpty(planType)&&planType.equals("1")){
                serviceArgeementUrl = Config.get("userService.yuesheng.url");
            }else {
                serviceArgeementUrl = Config.get("userService.shengxinCG.url");
            }
        }

        UserInfo userInfo = getUserInfo(Integer.valueOf(userId));
        if (userInfo == null) {
            return serviceArgeementUrl;
        }
        //用户服务协议参数
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("userName", "");
        if (StringUtils.isNotBlank(userInfo.getFullName())) {
            paramMap.put("userName", userInfo.getFullName().substring(0, 1) + "**");
        }
        paramMap.put("phone", "");
        if (StringUtils.isNotBlank(userInfo.getUsername()) && userInfo.getUsername().length()>7) {
            paramMap.put("phone", com.fenlibao.p2p.util.StringHelper.replace(3, 7, userInfo.getUsername(), "****"));
        }
        paramMap.put("IDNumber", "");
        if (StringUtils.isNotBlank(userInfo.getIdCard())) {
            paramMap.put("IDNumber", userInfo.getIdCard());
        }

        if (StringUtils.isNotBlank(serviceArgeementUrl)) {
            serviceArgeementUrl = CommonTool.convertParamToUrl(serviceArgeementUrl, paramMap);
        }
        return serviceArgeementUrl;
    }

    @Override
    public Map<String, Object> getEnterpriseInfo(Integer userId){
        return userInfoDao.getEnterpriseInfo(userId);
    }

    /**
     * 新增用户基本信息
     * @param phoneNum
     * @param password
     */
    private void autoAddUserInfo(String phoneNum, String password,String id) {
        // 新增用户基本信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(id);
        userInfo.setUsername(phoneNum);
        userInfo.setPhone(phoneNum);
        userInfo.setUserType(T6110_F06.ZRR.name());
        userInfo.setUserStatus(T6110_F07.QY.name());
        userInfo.setRegisterOrigin(T6110_F08.ZC.name());
        // 非担保方
        userInfo.setGuarantorFlag(T6110_F10.F.name());
        // 加密
        userInfo.setPassword(PasswordCryptUtil.crypt(password));
        userInfoDao.addUser(userInfo);
    }
}
