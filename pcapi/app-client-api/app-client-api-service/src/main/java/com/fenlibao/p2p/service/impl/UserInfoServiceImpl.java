package com.fenlibao.p2p.service.impl;

import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
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
import com.fenlibao.p2p.dao.user.RiskTestDao;
import com.fenlibao.p2p.model.entity.*;
import com.fenlibao.p2p.model.entity.salary.BidInfo;
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
import com.fenlibao.p2p.model.vo.user.AccountNoVO;
import com.fenlibao.p2p.model.vo.user.UserMemberInfoVO;
import com.fenlibao.p2p.model.xinwang.entity.account.XWBankInfo;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.trade.SysPaymentInstitution;
import com.fenlibao.p2p.service.FinacingService;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.UserTokenService;
import com.fenlibao.p2p.service.bank.BankService;
import com.fenlibao.p2p.service.bid.BidExtendService;
import com.fenlibao.p2p.service.channel.ChannelService;
import com.fenlibao.p2p.service.coupon.CouponService;
import com.fenlibao.p2p.service.privateMessage.PrivateMessageService;
import com.fenlibao.p2p.service.recharge.IRechargeService;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.service.register.IUserManagerService;
import com.fenlibao.p2p.service.salary.SalaryService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.service.user.LoginStateService;
import com.fenlibao.p2p.service.xinwang.account.XWUserAuthService;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.service.xinwang.bank.XWBankService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.email.EmailParam;
import com.fenlibao.p2p.util.email.EmailUtils;
import com.fenlibao.p2p.util.email.GenerateLinkUtils;
import com.fenlibao.p2p.util.api.file.FileInformation;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.formater.certification.BankCardFormater;
import com.fenlibao.p2p.util.formater.certification.IDCardNameFormater;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Message;
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
    private CouponService couponService;
    @Resource
    private PrivateMessageService privateMessageService;

    @Resource
    private RiskTestDao riskTestDao;

    @Resource
    private XWUserInfoService xwUserInfoService;

    @Resource
    private XWBankService xwBankService;

    @Resource
    private IRechargeService rechargeService;

    @Resource
    private XWUserAuthService xwUserAuthService;


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
        sendSmsAndPrivatemessage(phoneNum, userId, content, privatemessageTitle, content + letterSuffix);
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
        sendSmsAndPrivatemessage(phoneNum, userId, content, privatemessageTitle, content + letterSuffix);
    }

    /**
     * 发送短信
     *
     * @param phoneNum
     * @param content
     */
    private void sendMsg(String phoneNum, String content) {
        Timestamp outTime = new Timestamp(System.currentTimeMillis() / 1000 * 1000 + 30 * 60 * 1000);//过期时间30分钟
        SendSmsRecord record = new SendSmsRecord();
        record.setContent(content);
        record.setCreateTime(new Date());
        record.setStatus("W");
        record.setType(0);
        record.setOutTime(outTime);
        sendSmsRecordDao.insertSendSmsRecord(record);
        SendSmsRecordExt recordExt = new SendSmsRecordExt();
        recordExt.setId(record.getId());
        recordExt.setPhoneNum(phoneNum);
        sendSmsRecordExtDao.insertSendSmsRecordExt(recordExt);
    }

    /**
     * 发送短信、站内信
     *
     */
    private void sendSmsAndPrivatemessage(String phoneNum, String userId, String smsContent, String privatemessageTitle, String privatemessageContent) {
        // 短信
        sendMsg(phoneNum, smsContent);
        // 站内信
        privateMessageService.sendLetter(userId, privatemessageTitle, privatemessageContent);
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
                BigDecimal withdrawFreezeSum = withdrawDao.getWithdrawFreezeSum(userId);
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
    public int isBidNewUser(int userId) throws Exception{
        return userInfoDao.isBidNewUser(userId);
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
                response.setCodeMessage(Message.STATUS_1016, Message.get(Message.STATUS_1016));
            } else {
                // AES解码
                phoneNum = AES.getInstace().decrypt(phoneNum);
                // 手机号格式验证
                if (phoneNum.matches(phonePattern)) {
                    Map paramMap = new HashMap();
                    paramMap.put("phoneNum", phoneNum);
                    UserInfo userInfo = userInfoDao.getUserInfo(paramMap);
                    if (userInfo == null) {
                        response.setMessage("用户不存在!");
                        response.setCodeMessage(Message.STATUS_1014, Message.get(Message.STATUS_1014));
                    } else {
                        paramMap.clear();
                        paramMap.put("password", PasswordCryptUtil.crypt(decryptPassword));
                        paramMap.put("userId", userInfo.getUserId());
                        // 更新密码
                        int updatePassword = userInfoDao.updatePassword(paramMap);
                        // 修改密码失败
                        if (updatePassword == 0) {
                            response.setCodeMessage(Message.STATUS_500, Message.get(Message.STATUS_500));
                        }
                    }
                } else {
                    // 手机号码格式不正确
                    response.setCodeMessage(Message.STATUS_1019, Message.get(Message.STATUS_1019));
                }
            }
        } catch (Exception e) {
            response.setCodeMessage(
                    ResponseEnum.RESPONSE_RETRIEVEPASSWORD_ERROR.getCode(),
                    ResponseEnum.RESPONSE_RETRIEVEPASSWORD_ERROR.getMessage());
            logger.debug(e.getMessage());
        }
        return response;
    }

    /**
     * 获取用户账户信息字段Map
     */
    //@Override
    private Map<String, Object> getUserAccountInfoDataMap(UserAccountInfoVO userAccountInfoVO, boolean containBaofoo) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> accountCheckStatusMap = new HashMap<>();
        data.put("token", userAccountInfoVO.getToken());
        String userId = userAccountInfoVO.getUserId();

        //2.1.2 增加风险压力测试
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
        data.put("phoneWithStar", StringHelper.getPhoneReplace4To7(userAccountInfoVO.getPhone()));
        if (!containBaofoo) {
            data.put("realPhone", userAccountInfoVO.getPhone());
        } else {
            data.put("realPhone", "");
        }


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
        List<Map<String, Object>> resultList = bankService.buildBankCardsResult(bankCards,containBaofoo);
        data.put("bankList", resultList);

        // 绑定的身份证姓名(AES加密)
        String identityName = userAccountInfoVO.getIdentityName();
        String idCardNameFormater = IDCardNameFormater.format(identityName);
        data.put("identityName", idCardNameFormater);
        data.put("identityNameDecrypt", identityName);
        // 绑定的身份证
        data.put("identitycard", userAccountInfoVO.getIdentitycard());
        data.put("identitycardEncrypt", userAccountInfoVO.getIdentitycard2());
        try {
            data.put("identitycardDecrypt", StringHelper.decode(userAccountInfoVO.getIdentitycard2()));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        //增加字段   junda.feng  2016-5-11
        String birthday="";
        if(userAccountInfoVO.getBirthday()!=null){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            birthday = sdf.format(userAccountInfoVO.getBirthday());
        }
        data.put("birthday", birthday);
        data.put("companyIndustry", userAccountInfoVO.getCompanyIndustry());
        data.put("companySize", userAccountInfoVO.getCompanySize());
        data.put("schoole", userAccountInfoVO.getSchoole());
        data.put("income", userAccountInfoVO.getIncome());

        //投资用户是否可以解绑银行卡
        Integer applyTimes = userSecurityDao.getApplyUnbindTimes(Integer.parseInt(userId), UserRole.INVESTOR.getCode());
        data.put("allowUnbind", applyTimes > 0 ? 1 : 0);
        //借款用户是否可以解绑银行卡
        Integer borrowApplyTimes = userSecurityDao.getApplyUnbindTimes(Integer.parseInt(userId), UserRole.BORROWERS.getCode());
        data.put("borrowAllowUnbind", borrowApplyTimes > 0 ? 1 : 0);

        String temp = userAccountInfoVO.getIdentitycard2();
        if(temp !=null ){
            try {
                temp=StringHelper.decode(temp);
                if (Integer.parseInt(temp.substring(temp.length() - 2, temp.length() - 1)) % 2 != 0) {
                    data.put("sex", "男");
                }else{
                    data.put("sex", "女");
                }
            } catch (Throwable e) {
                e.printStackTrace();
                data.put("sex", null);
            }
        }else{
            data.put("sex", null);
        }

        data.put("position", userAccountInfoVO.getPosition());
        return data;
    }

    @Override
    public Map<String, Object> getUserAccountInfoDataMap(UserAccountInfoVO userAccountInfoVO, String paymentChannelCode, boolean containBaofoo,VersionTypeEnum versionType) {
        Map<String, Object> data = this.getUserAccountInfoDataMap(userAccountInfoVO, containBaofoo);
        BigDecimal balance = BigDecimal.ZERO;
        //余额查询
        if(versionType.getCode().equals("CG")){
            balance = userAccountDao.getCgBalance(Integer.valueOf(userAccountInfoVO.getUserId()));
        }else {
            balance = userAccountDao.getWLZHBalance(Integer.valueOf(userAccountInfoVO.getUserId()));
        }
        data.put("balance",balance);
        /**
         * 连连通道的银行卡信息
         */
        List<Map<String, Object>> lianlianBankList = (List<Map<String, Object>>) data.get("bankList");

        /**
         * Baofoo通道银行卡信息
         */
        List<Map<String, Object>> baofooBankList = new ArrayList<>();

        //借款银行卡列表
        List<Map<String, Object>> borrowBankList = new ArrayList<>();

        if(versionType == VersionTypeEnum.CG){

            Map<String,Object> cardMap  = getXwCardCode(Integer.valueOf(userAccountInfoVO.getUserId()),"INVESTOR");

            Map<String,Object> borrowCardMap  = getXwCardCode(Integer.valueOf(userAccountInfoVO.getUserId()),"BORROWERS");
            //投资端绑卡信息
            if(cardMap!=null && cardMap.size()>0){
                data.put("isBinkCards","1");
                XWBankInfo bankInfo = xwBankService.getBankInfo((String)cardMap.get("bankcode"));
                Map<String, Object> bankParam = new HashMap<>();
                bankParam.put("bankName", bankInfo.getBankName());

                bankParam.put("bankCode", bankInfo.getBankCode());

                bankParam.put("bankCardId", bankInfo.getId());
                String temp = (String)cardMap.get("bankNum");
                bankParam.put("bankNumEncrypt", temp);
                try {
                    // 格式化
                    temp = StringHelper.decode(temp);
                    String bankCard = BankCardFormater.format(temp);
                    bankParam.put("bankNum", bankCard);
                } catch (Throwable e) {
                    e.printStackTrace();
            }
                baofooBankList.add(bankParam);
            }else {
                data.put("isBinkCards","0");
            }

            //借款端绑卡信息
            if(borrowCardMap!=null && borrowCardMap.size()>0){
                data.put("isBorrowBinkCards","1");
                XWBankInfo bankInfo = xwBankService.getBankInfo((String)borrowCardMap.get("bankcode"));
                Map<String, Object> bankParam = new HashMap<>();
                bankParam.put("borrowBankName", bankInfo.getBankName());

                bankParam.put("borrowBankCode", bankInfo.getBankCode());

                bankParam.put("borrowBankCardId", bankInfo.getId());
                String temp = (String)borrowCardMap.get("bankNum");
                bankParam.put("bankNumEncrypt", temp);
                try {
                    // 格式化
                    temp = StringHelper.decode(temp);
                    String bankCard = BankCardFormater.format(temp);
                    bankParam.put("borrowBankNum", bankCard);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                borrowBankList.add(bankParam);
            }else {
                data.put("isBorrowBinkCards","0");
            }
        }else {
            if (containBaofoo) {
                baofooBankList = this.getUserBaofooCardInfo(Integer.valueOf(userAccountInfoVO.getUserId()));
                if (baofooBankList != null && baofooBankList.size() > 0) {
                    Map<String, Object> card = baofooBankList.get(0);
                    Map<String, Object> bankParam = new HashMap<>();

                    String temp = (String) card.get("bankNum");
                    try {
                        // 格式化
                        temp = StringHelper.decode(temp);
                        String bankCard = BankCardFormater.format(temp);
                        bankParam.put("bankNum", bankCard);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                    bankParam.put("bankName", card.get("bankName"));

                    bankParam.put("bankCode", card.get("bankCode"));

                    bankParam.put("bankType", card.get("bankType"));

                    bankParam.put("bankCardId", card.get("bankCardId"));

                    bankParam.put("bankInfoCompleteFlag", card.get("bankInfoCompleteFlag"));

                    bankParam.put("bankAuthStatus", card.get("bankAuthStatus"));

                    baofooBankList.clear();
                    baofooBankList.add(bankParam);
                }
            }
        }
        if (StringUtils.isNotBlank(paymentChannelCode) && paymentChannelCode.equals("102")) {
            data.put("borrowBankList", borrowBankList);
            data.put("bankList", baofooBankList);
            data.put("otherList", lianlianBankList.size()==0?"":lianlianBankList);
        } else {
            data.put("otherList", baofooBankList);
            data.put("bankList", lianlianBankList);
            data.put("borrowBankList", borrowBankList);
        }


        if(versionType == VersionTypeEnum.CG) {
            Map<String,Object> map = new HashMap<>();
            map.put("userId",Integer.valueOf(userAccountInfoVO.getUserId()));
            String isXwAccount = userInfoDao.isXWaccount(map);
            if (!StringUtils.isEmpty(isXwAccount)) {
                data.put("isCgAccount", 1);
            } else {
                data.put("isCgAccount", 0);
            }
            String isBorrower = userInfoDao.isBorrower(map);
            if (!StringUtils.isEmpty(isBorrower)&&isBorrower.equals("PASSED")) {
                data.put("isBorrower", 1);
            }else if(!StringUtils.isEmpty(isBorrower)&&isBorrower.equals("AUDIT")) {
                data.put("isBorrower", 2);
            }else if(!StringUtils.isEmpty(isBorrower)&&isBorrower.equals("BACK")){
                data.put("isBorrower", 3);
            }else if(!StringUtils.isEmpty(isBorrower)&&isBorrower.equals("REFUSED")){
                data.put("isBorrower", 4);
            }else {
                data.put("isBorrower", 0);
            }

            Map<String, Object> param = new  HashMap<>();
            param.put("userId", Integer.valueOf(userAccountInfoVO.getUserId()));
            param.put("userRole", "INVESTOR");
            Integer isActive = userInfoDao.isActive(param);
            param.put("userRole", "BORROWERS");
            Integer isBorrowerActive = userInfoDao.isActive(param);
            if(isActive == null){
                isActive = 0;
            }
            if(isBorrowerActive == null){
                isBorrowerActive = 0;
            }
            data.put("isActive", isActive);
            data.put("isBorrowerActive", isBorrowerActive);

            if(userInfoDao.isEnterpriseUser(Integer.valueOf(userAccountInfoVO.getUserId()))){
                dataAddEnterpriseInfo(data, Integer.valueOf(userAccountInfoVO.getUserId()));
            }
        }
        data.put("userType", userAccountInfoVO.getUserType());
        //是否限制普通版资金操作
        data.put("limitPT", "1");
        return data;
    }

    private void dataAddEnterpriseInfo(Map<String, Object> data, Integer userId) {
        Map<String, Object> map = userInfoDao.getEnterpriseInfo(userId);
        data.put("enterpriseName", map.get("enterpriseName"));
        data.put("taxNo", map.get("taxNo"));
        data.put("bankLicense", map.get("bankLicense"));
        try {
            data.put("bankcardNo", StringHelper.decode((String) map.get("bankcardNo")));
        } catch (Exception e) {
            logger.error("企业对公账号解密出错");
            e.printStackTrace();
        }
        data.put("legal", map.get("legal"));
        data.put("legalIdCardNo", map.get("legalIdCardNo"));
        data.put("businessLicense", map.get("businessLicense"));
        data.put("creditCode", map.get("creditCode"));
        data.put("orgNo", map.get("orgNo"));
        data.put("unifiedCode", map.get("unifiedCode"));
        data.put("contact", map.get("contact"));
        data.put("contactPhone", map.get("contactPhone"));
    }

    @Override
    public List<Map<String,Object>> getUserBaofooCardInfo(int userId) {
        //用户信息
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("userId", userId);
        List<Map<String, Object>> userBaofooCardInfo = this.userInfoDao.getUserBaofooCardInfo(userMap);
        return userBaofooCardInfo;
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
    public UserAccountInfoVO getUserAccountInfo(String userId, String userType) {
        // 根据用户id查询用户账户信息
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("userType", userType);
        UserAccountInfoVO userAccountInfoVO = userInfoDao.getUserAccountInfo(paramMap);
        return userAccountInfoVO;
    }

    @Override
    public HttpResponse setUserName(String userId, String username) throws Exception {
        HttpResponse response = new HttpResponse();
        username = AES.getInstace().decrypt(username);
        if (!username.matches(usernamePattern)) {
            response.setCodeMessage(Message.STATUS_1022, Message.get(Message.STATUS_1022));
            return response;
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("userId", userId);
        UserInfo user = userInfoDao.getUserInfo(paramMap);
        if (null == user) {
            response.setCodeMessage(Message.STATUS_1024, Message.get(Message.STATUS_1024));
            return response;
        }
        if (StringUtils.isNotBlank(user.getUsername())) {
            response.setCodeMessage(Message.STATUS_1023, Message.get(Message.STATUS_1023));
            return response;
        }
        Integer srcUserId = userInfoDao.existUsername(username);
        if (srcUserId == null) {
            paramMap.clear();
            paramMap.put("userId", userId);
            paramMap.put("username", username);
            int result = userInfoDao.updateUserName(paramMap);
            if (result <= 0) {
                response.setCodeMessage(Message.STATUS_500, Message.get(Message.STATUS_500));
            }
        } else {
            response.setCodeMessage(Message.STATUS_1020, Message.get(Message.STATUS_1020));
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
        addUserInfo(phoneNum, phoneNum, password, T6110_F06.ZRR);
        // 根据手机号获取用户信息
        UserInfo uInfo = userInfoDao.getUserInfo(paramMap);
        // 获取用户ID
        String userId = uInfo.getUserId();
        // 新增用户安全认证表
        addUserSecurityAuthentication(phoneNum, userId);
        // 新增用户基础信息，兴趣类型为投资
        addUserBaseInfo(userId, Status.LC);
        // 分别新增资金账户(往来账户、风险保证金账户、锁定账户)
        addUserAccount(phoneNum, userId);
        // 用户信用账户表
        addUserCredit(userId);
        // 用户投资统计表
        addUserFinancing(userId);
        // 优选投资统计表
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
        addMemberPointsAccount(userId,phoneNum);
        return uInfo;
    }

    @Transactional(readOnly = false)
    @Override
    public UserInfo registerForCompany(BaseRequestForm baseForm, RegisterForm registerForm) throws Exception {
        String phoneNum = registerForm.getPhoneNum();
        String password = registerForm.getPassword();

        String zhName = getZhName();//账号
        Map paramMap = new HashMap();
        paramMap.put("phoneNum", phoneNum);
        paramMap.put("userType", T6110_F06.FZRR);

        // 新增用户信息
        addUserInfo(zhName, phoneNum, password, T6110_F06.FZRR);
        // 根据手机号获取用户信息
        UserInfo uInfo = userInfoDao.getUserInfo(paramMap);
        // 获取用户ID
        String userId = uInfo.getUserId();
        // 新增用户安全认证表
        addUserSecurityAuthentication(phoneNum, userId);

        // 新增用户基础信息，兴趣类型为借款
        addUserBaseInfo(userId, Status.JK);

        // 分别新增资金账户(往来账户、风险保证金账户、锁定账户)
        addUserAccount(uInfo.getUsername(), userId);

        // 用户信用账户表
        addUserCredit(userId);
        // 用户投资统计表
        addUserFinancing(userId);

        // 用户信用档案表(T6144)
        addUserCreditArchive(userId);

        //企业联系信息
        //addCompanyContactInfo(userId, lxName, lxTel);
        //企业介绍资料
        addCompanyProfileInfo(userId);
        // 企业基础信息
        addCompanyBaseInfo(userId, uInfo.getUsername(), registerForm);

        // 优选投资统计表
        addUserBestFinancing(userId);
        // 推荐人相关表
        String spreadPhoneNum = registerForm.getSpreadPhoneNum();
        addSpread(userId, spreadPhoneNum, phoneNum);

        // 获取所有信用认证项(T5123)，并新增用户认证信息(T6120)
        addUserCreditAuthInfo(userId);
        // 新增用户来源
        addUserOrigin(userId, registerForm.getChannelCode(), baseForm.getClientType());
        //增加用户积分账户   modify by laubrence  2016-2-20 15:58:57
        addMemberPointsAccount(userId, phoneNum);

        try{
            // 发送短信和站内信
            String smsContent = Sender.get("sms.register.company.content").replace("#{username}", uInfo.getUsername());
            sendMsg(phoneNum, smsContent);
        }catch (Exception e){
            logger.error(e, e);
        }
        return uInfo;
    }

    /**
     * 生成企业账号
     * @return
     */
    private String getZhName(){
       return userInfoDao.getZhName();
    }

    /**
     * @Title: addMemberPointsAccount
     * @Description: 增加用户积分账户
     * @param userId
     * @return: void
     */
    private void addMemberPointsAccount(String userId, String phoneNum) {
        // modify by zeronx add
        UserMemberInfoVO memberInfo = userInfoDao.getMemberInfo(phoneNum);
        if (memberInfo != null) {
            userInfoDao.updatePointsAccount(userId, memberInfo.getPointsId());//将会员积分账户关联起来
            userInfoDao.updatePointsConsumeRecord(userId, memberInfo.getMemberId());//将会员未注册分利宝之前的积分消费记录关联起来
            userInfoDao.updateMerchantMemberUserId(userId, memberInfo.getMemberId());
            return;
        }
        // add end
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
     * 优选投资统计表
     * @param userId
     */
    private void addUserBestFinancing(String userId) {
        Map<String, Object> userBestFinancingParamMap = new HashMap<>();
        userBestFinancingParamMap.put("userId", userId);
        userFinancingDao.addUserBestFinancing(userBestFinancingParamMap);
    }

    /**
     * 用户投资统计表
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
     * @param userId
     */
    private void addUserAccount(String username, String userId) {
        Map<String, Object> userAccountParamMap = new HashMap<>();
        userAccountParamMap.put("userId", userId);
        for (AccountEnum e : AccountEnum.values()) {
            userAccountParamMap.put("type", e.toString());
            userAccountParamMap.put("account", getAccount(e.toString(), userId));
            userAccountParamMap.put("accountName", username);
            userAccountDao.addUserAccount(userAccountParamMap);
        }
    }

    /**
     * 新增用户基础信息，兴趣类型为投资
     * @param userId
     */
    private void addUserBaseInfo(String userId, Status interestType) {
        Map<String, Object> userBaseInfoParamMap = new HashMap<>();
        userBaseInfoParamMap.put("userId", userId);
        if(Status.JK.equals(interestType)){
            userBaseInfoParamMap.put("type", Status.JK.name());
        }else{
            userBaseInfoParamMap.put("type", Status.LC.name());
        }
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
    private void addUserInfo(String userName, String phoneNum, String password, T6110_F06 userType) {
        // 新增用户基本信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userName);
        userInfo.setPhone(phoneNum);
        userInfo.setUserStatus(T6110_F07.QY.name());
        userInfo.setRegisterOrigin(T6110_F08.ZC.name());
        // 非担保方
        userInfo.setGuarantorFlag(T6110_F10.F.name());
        // 加密
        userInfo.setPassword(PasswordCryptUtil.crypt(password));
        if(T6110_F06.FZRR.equals(userType)){
            userInfo.setUserType(T6110_F06.FZRR.name());
        }else{
            userInfo.setUserType(T6110_F06.ZRR.name());
        }
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
                response.setCodeMessage(Message.STATUS_1016, Message.get(Message.STATUS_1016));
            } else if (!newPasswordMatches) {
                response.setCodeMessage(Message.STATUS_1016, Message.get(Message.STATUS_1016));
            } else if (oldPassword.equals(newPassword)) {
                // 新旧密码一致
                response.setCodeMessage(Message.STATUS_1017, Message.get(Message.STATUS_1017));
            } else {
                // 判断旧密码是否正确
                UserInfo userInfo = userInfoDao.getUserInfoById(IntegerParser.parse(userId));
                if (userInfo == null) {
                    response.setCodeMessage(Message.STATUS_1014, Message.get(Message.STATUS_1014));
                } else {
                    String pwd = userInfo.getPassword();
                    // 加密后的旧密码与原密码进行对比
                    if (PasswordCryptUtil.crypt(oldPassword).equals(pwd)) {
                        String passWord = PasswordCryptUtil.crypt(newPassword);
                        Map<String, Object> map = new HashMap<>(2);
                        map.put("userId", userId);
                        map.put("password", passWord);
                        // 更新密码
                        userInfoDao.updatePassword(map);
                    } else {
                        response.setCodeMessage(ResponseCode.USER_OLD_PWD_NOT_RIGHT);
                    }
                }

            }
        } catch (Exception e) {
            response.setCodeMessage(
                    ResponseEnum.RESPONSE_MODIFYPASSWORD_ERROR.getCode(),
                    ResponseEnum.RESPONSE_MODIFYPASSWORD_ERROR.getMessage());
            logger.error("[UserInfoServiceImpl.modifyPassword]", e);
        }

        return response;
    }

    @Override
    public UserAccountInfoVO getUserAccountInfoByPhoneNumOrUsername(String username, String phoneNum, String userType) {
        Map<String, String> paramMap = new HashMap<>();
        if (StringUtils.isNotBlank(username)) {
            paramMap.put("username", username);
        } else {
            paramMap.put("phoneNum", phoneNum);
        }
        paramMap.put("userType", userType);
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
        if(type == 202){//重置交易密码
            content = Sender.get("sms.resetTradePwd.content").replace("#code", randomNum);
        }else if(type == 207){//找回登录密码
            content = Sender.get("sms.findLoginPwd.content").replace("#code", randomNum);
        }else if(type == 208){//注册
            content = Sender.get("sms.register.content").replace("#code", randomNum);
        }else{
            logger.info("phoneNum:{}发送验证码类型type:{}错误",new Object[]{phoneNum,type});
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
    @Transactional
    @Override
    public HttpResponse modifyInfoById(Integer userId,String nickname,String schoole,String companyIndustry,
                                       String companySize, String position, String income) {
        Map<String, Object> params = new HashMap<String, Object>();
        HttpResponse respone = new HttpResponse();
        params.put("userId", userId);
        params.put("nickname", nickname==null?"":nickname);
        params.put("schoole", schoole==null?"":schoole);
        params.put("companyIndustry", companyIndustry==null?"":companyIndustry);
        params.put("companySize", companySize==null?"":companySize);
        params.put("position", position==null?"":position);
        params.put("income", income==null?"":income);
        int result = 0;
        result +=this.userInfoDao.modifyNickName(params);
        SchooleInfo schooleInfo= userInfoDao.getSchooleInfo(params);
        if(schooleInfo==null){
            schooleInfo=new SchooleInfo();
            schooleInfo.setUserId(userId);
            schooleInfo.setSchoole(schoole);
            schooleInfo.setMajor("");
            SimpleDateFormat sdf=new SimpleDateFormat("YYYY");
            schooleInfo.setEnrollmentYear(Integer.parseInt(sdf.format(new Date())));
            result +=this.userInfoDao.addSchooleInfo(schooleInfo);
        }else{
            result +=this.userInfoDao.modifySchooleInfo(params);
        }
        WorkInfo workInfo=userInfoDao.getWorkInfo(params);
        if(workInfo==null){
            workInfo=new WorkInfo();
            workInfo.setUserId(userId);
            workInfo.setState("ZZ");
            workInfo.setCompanyIndustry(companyIndustry);
            workInfo.setCompanySize(companySize);
            workInfo.setPosition(position);
            workInfo.setIncome(income);
            result +=this.userInfoDao.addWorkInfo(workInfo);
        }else{
            result += this.userInfoDao.modifyWorkInfo(params);
        }
        if (result < 3) {
            respone.setMessage("修改失败");
        }
        return respone;
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
    public ResponseEnum verifySmsCode(String phoneNum, String type, String smsCode) {
        SmsValidcode code = this.getLastSmsCode(phoneNum, type);
        if (null != code) {
            long outTime = code.getOutTime().getTime();
            if (outTime <= System.currentTimeMillis()) {
                return ResponseEnum.RESPONSE_CAPTCHA_TIMEOUT;
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
                return ResponseEnum.RESPONSE_CAPTCHA_INVALID;
            }
        }
        return ResponseEnum.RESPONSE_CAPTCHA_INVALID;
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
                BigDecimal withdrawFreezeSum = withdrawDao.getWithdrawFreezeSum(userId);
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

    @Override
    public Map<String, Object> getUserAssets(Integer userId) {
        Map<String, Object> data = new LinkedHashMap<>();
        BigDecimal balance = userAccountDao.getWLZHBalance(userId); //余额
        //3.1.0版本以前使用投资冻结资金
        //BigDecimal tenderFreezeAmount = bidExtendDao.getTenderFreezeSum(userId.toString()); //投资冻结金额
        BigDecimal withdrawFreezeAmount = withdrawDao.getCGWithdrawFreezeSum(userId,VersionTypeEnum.PT.getCode()); //提现冻结金额

        //3.2.0版本用户投资冻结资金
        //BigDecimal tenderFreezeAmount  = userAccountDao.getSdUserFreezeSum(userId).add(userAccountDao.getPlanFreezeSum(userId)).subtract(withdrawFreezeAmount); //投资冻结金额
        BigDecimal tenderFreezeAmount = userAccountDao.getNewTenderFreezeSum(userId,VersionTypeEnum.PT.getIndex());
//      BigDecimal totalCash = balance.add(tenderFreezeAmount).add(withdrawFreezeAmount); //现金总资产

        BigDecimal totalGainsYesterday = tradeService.getEarningsYesterdaySum(userId); //昨天总收益
//      BigDecimal totalEarning = tradeService.getAccumulativeEarnings(userId); //总收益
        //（这里应该获取还款计划里面的原始债权的金额，不能统计购买时的价格！！！！）
//        BigDecimal zqzrAssets = finacingService.getZqzrAssets(userId.toString()); //债权转让投资总额


//        Map<String, Object> gains = tradeService.getEarningsRecordList(userId); //这里的收益是动态的，2.0不需要
        /*DueInAmount dueInAmount = tradeService.getDueInAmount(userId); //待收本息
        BigDecimal YHGains =  tradeService.getYHGains(String.valueOf(userId)); //已获收益*/
        BigDecimal totalAssets = balance.add(tenderFreezeAmount).add(withdrawFreezeAmount);

        BigDecimal dueInPrincipal = BigDecimal.ZERO;
        BigDecimal dueInterest = BigDecimal.ZERO;
        BigDecimal dueOthers = BigDecimal.ZERO;
        /*if (dueInAmount != null) {
            dueInPrincipal = dueInAmount.getPrincipal();
            dueInterest = dueInAmount.getInterest();
            dueOthers = dueInAmount.getOthers();
            totalAssets = totalAssets.add(dueInPrincipal).add(dueInterest).add(dueOthers);
        }*/
        DueInAmount dueInAmount = tradeService.getNewDueInAmount(userId,VersionTypeEnum.PT);//3.2.0版本适用待收本息
        DueInAmount planDueInAmount = tradeService.getPlanDueInAmount(userId,VersionTypeEnum.PT);//计划待收本息
        BigDecimal YHGains =    YHGains = tradeService.getNewYHGains(String.valueOf(userId), VersionTypeEnum.PT);//3.2.0已获收益
        BigDecimal PlanYHGains =  tradeService.getPlanYHGains(String.valueOf(userId),VersionTypeEnum.PT);
        if (dueInAmount != null&&planDueInAmount!=null) {
            dueInPrincipal = dueInAmount.getPrincipal().add(planDueInAmount.getPrincipal());
            dueInterest = dueInAmount.getInterest().add(planDueInAmount.getInterest());
            dueOthers = dueInAmount.getOthers().add(planDueInAmount.getOthers());
            totalAssets = totalAssets.add(dueInPrincipal).add(dueInterest).add(dueOthers);
        }else if(null ==dueInAmount &&planDueInAmount!=null){
            dueInPrincipal =planDueInAmount.getPrincipal();
            dueInterest =planDueInAmount.getInterest();
            dueOthers = planDueInAmount.getOthers();
            totalAssets = totalAssets.add(dueInPrincipal).add(dueInterest).add(dueOthers);
        }else if(dueInAmount != null&&null == planDueInAmount){
            dueInPrincipal = dueInAmount.getPrincipal();
            dueInterest = dueInAmount.getInterest();
            dueOthers = dueInAmount.getOthers();
            totalAssets = totalAssets.add(dueInPrincipal).add(dueInterest).add(dueOthers);
        }
        if(PlanYHGains!=null){
            YHGains = PlanYHGains.add(YHGains);
        }


        //TODO 获取用户投资项目数量
        VersionTypeEnum versionTypeEnum = VersionTypeEnum.PT;

        Map<String, Object> projectsQty = new HashMap<>(3);
        Map<String, Map<String, String>> qty = finacingService.getInvestmentQty(userId, versionTypeEnum);
        projectsQty.put(Status.TBZ.name(), qty.get(Status.TBZ.name()) == null ? 0 : qty.get(Status.TBZ.name()).get("qty"));
        projectsQty.put(Status.HKZ.name(), qty.get(Status.HKZ.name()) == null ? 0 : Integer.parseInt(qty.get(Status.HKZ.name()).get("qty")));
        projectsQty.put(Status.YJQ.name(), qty.get(Status.YJQ.name()) == null ? 0 : qty.get(Status.YJQ.name()).get("qty"));
        projectsQty.put(Status.DFK.name(), qty.get(Status.DFK.name()) == null ? 0 : qty.get(Status.DFK.name()).get("qty"));

        data.put("projectsQty", projectsQty);

        data.put("totalGainsYesterday", totalGainsYesterday.toString());
        data.put("dueInPrincipal", dueInPrincipal.toString());
        data.put("balance", balance.toString());
        data.put("tenderFreezeAmount", tenderFreezeAmount.toString());
        data.put("withdrawFreezeAmount", withdrawFreezeAmount.toString());
        data.put("totalAssets", totalAssets.toString());
        data.put("expectGains", dueInterest.add(dueOthers).toString());
        data.put("historyGains", YHGains.toString());
        data.put("totalGains", dueInterest.add(dueOthers).add(YHGains).toString());

        data.put("investAsset",  dueInPrincipal.add( dueInterest.add(dueOthers)).toString());
        //现金资产
        data.put("cashAsset", (balance.add(tenderFreezeAmount).add(withdrawFreezeAmount)).toString());
        //账户总资产
        data.put("totalAsset", totalAssets.toString());
        return data;
    }


    @Override
    public BigDecimal getUserTotalAssets(int userId) {
        BigDecimal balance = userAccountDao.getWLZHBalance(userId); //余额
        BigDecimal tenderFreezeAmount = bidExtendDao.getTenderFreezeSum(String.valueOf(userId)); //投资冻结金额
        BigDecimal withdrawFreezeAmount = withdrawDao.getWithdrawFreezeSum(String.valueOf(userId)); //提现冻结金额

        DueInAmount dueInAmount = tradeService.getDueInAmount(userId); //待收本息
        BigDecimal dueInPrincipal = BigDecimal.ZERO;
        BigDecimal dueInterest = BigDecimal.ZERO;
        BigDecimal dueOthers = BigDecimal.ZERO;
        if (dueInAmount != null) {
            dueInPrincipal = dueInAmount.getPrincipal();
            dueInterest = dueInAmount.getInterest();
            dueOthers = dueInAmount.getOthers();
        }

        BigDecimal totalAssets = balance.add(tenderFreezeAmount).add(withdrawFreezeAmount).add(dueInPrincipal).add(dueInterest).add(dueOthers);
        return totalAssets;
    }

    @Override
    public int isDepository(int userId) throws Exception {
        return userInfoDao.isDepository(userId);
    }

    @Override
    public Map<String, Object> getUserAssetsByDepository(Integer userId, String depository,BigDecimal balanceByDepository) {
        Map<String, Object> data = new LinkedHashMap<>();
        BigDecimal balance = BigDecimal.ZERO; //余额
        //BigDecimal tenderFreezeAmount = bidExtendDao.getTenderFreezeSumByDepository(userId.toString(),depository); //投资冻结金额

        BigDecimal withdrawFreezeAmount = BigDecimal.ZERO;
        if(!StringUtils.isBlank(depository)&&depository.equals("1")){
            withdrawFreezeAmount = withdrawDao.getWithdrawFreezeSum(userId.toString());
            balance = userAccountDao.getWLZHBalance(userId);
        }
        else{
            withdrawFreezeAmount = withdrawDao.getWithdrawFreezeSumByDepository(userId.toString());
            balance=balanceByDepository;
        }
        //3.2.0版本用户投资冻结资金
        //BigDecimal tenderFreezeAmount = userAccountDao.getSdUserFreezeSum(userId).add(userAccountDao.getPlanFreezeSum(userId)).subtract(withdrawFreezeAmount); //投资冻结金额
        BigDecimal tenderFreezeAmount = userAccountDao.getNewTenderFreezeSum(userId,VersionTypeEnum.PT.getIndex());

        //提现冻结金额
        //DueInAmount dueInAmount = tradeService.getDueInAmountByDepository(userId.toString(),depository); //待收本息
        BigDecimal totalAssets = balance.add(tenderFreezeAmount).add(withdrawFreezeAmount);
        BigDecimal dueInPrincipal = BigDecimal.ZERO;
        BigDecimal dueInterest = BigDecimal.ZERO;
        BigDecimal dueOthers = BigDecimal.ZERO;
        BigDecimal investAsset = BigDecimal.ZERO;

        //3.2.0待收本息
     /*   DueInAmount dueInAmount = tradeService.getNewDueInAmount(userId, VersionTypeEnum.CG);//3.2.0版本适用待收本息
        DueInAmount planDueInAmount = tradeService.getPlanDueInAmount(userId, VersionTypeEnum.CG);//计划待收本息
        BigDecimal YHGains =  tradeService.getNewYHGains(String.valueOf(userId));//3.2.0已获收益
        BigDecimal PlanYHGains =  tradeService.getPlanYHGains(String.valueOf(userId));*/
        DueInAmount dueInAmount = tradeService.getNewDueInAmount(userId,VersionTypeEnum.PT);//3.2.0版本适用待收本息
        DueInAmount planDueInAmount = tradeService.getPlanDueInAmount(userId,VersionTypeEnum.PT);//计划待收本息
        BigDecimal YHGains =  tradeService.getNewYHGains(String.valueOf(userId),VersionTypeEnum.PT);//3.2.0已获收益
        BigDecimal PlanYHGains =  tradeService.getPlanYHGains(String.valueOf(userId),VersionTypeEnum.PT);
        if (dueInAmount != null&&planDueInAmount!=null) {
            dueInPrincipal = dueInAmount.getPrincipal().add(planDueInAmount.getPrincipal());
            dueInterest = dueInAmount.getInterest().add(planDueInAmount.getInterest());
            dueOthers = dueInAmount.getOthers().add(planDueInAmount.getOthers());
            totalAssets = totalAssets.add(dueInPrincipal).add(dueInterest).add(dueOthers);
        }else if(null ==dueInAmount &&planDueInAmount!=null){
            dueInPrincipal =planDueInAmount.getPrincipal();
            dueInterest =planDueInAmount.getInterest();
            dueOthers = planDueInAmount.getOthers();
            totalAssets = totalAssets.add(dueInPrincipal).add(dueInterest).add(dueOthers);
        }else if(dueInAmount != null&&null == planDueInAmount){
            dueInPrincipal = dueInAmount.getPrincipal();
            dueInterest = dueInAmount.getInterest();
            dueOthers = dueInAmount.getOthers();
            totalAssets = totalAssets.add(dueInPrincipal).add(dueInterest).add(dueOthers);
        }
        if(PlanYHGains!=null){
            YHGains = PlanYHGains.add(YHGains);
        }
        /*if (dueInAmount != null) {
            dueInPrincipal = dueInAmount.getPrincipal();
            dueInterest = dueInAmount.getInterest();
            dueOthers = dueInAmount.getOthers();
            totalAssets = totalAssets.add(dueInPrincipal).add(dueInterest).add(dueOthers);
        }*/

        //投资资产
        data.put("investAsset",  dueInPrincipal.add( dueInterest.add(dueOthers)).toString());
        //现金资产
        data.put("cashAsset", (balance.add(tenderFreezeAmount).add(withdrawFreezeAmount)).toString());
        //账户总资产
        data.put("totalAsset", totalAssets.toString());
        return data;
    }

    @Override
    public AccountNoVO getAccountNo(int userId) {
        return userInfoDao.getAccountNo(userId);
    }

    /**
     * 用户上一次投资该计划距离现在时间的分钟数
     * @param accountId
     * @param planId
     * @return
     */
    @Override
    public int getUserPlanLastOrder(int accountId, int planId){
        return userInfoDao.getUserPlanLastOrder(accountId, planId);
    }

    /**
     * 用户上一次投资该计划距离现在时间的分钟数
     * @param accountId
     * @param planId
     * @return
     */
    @Override
    public int getUserInvestPlanLastOrder(int accountId, int planId){
        return userInfoDao.getUserInvestPlanLastOrder(accountId, planId);
    }

    @Override
    public String getUserServiceAgreementUrl(String userId) {
        String serviceArgeementUrl = Config.get("userService.agreement.url");
        UserInfo userInfo = getUserInfo(userId);
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

    /**
     * 检查用户是否白名单用户
     * @param userId
     * @return 1：是  ； 0：否
     */
    @Override
    public int checkWhiteBoard(Integer userId) {
        //用户信息
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("userId", userId);
        return this.userInfoDao.checkWhiteBoard(userMap);
    }

    @Override
    public String isXWaccount(int userId) {
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("userId", userId);
        return this.userInfoDao.isXWaccount(userMap);
    }

    /**
     * 检查三证或者统一社会信用代码
     * @return
     */
    @Override
    public boolean checkEnterpriseCertificate(RegisterForm registerForm){
        //用户信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("businessLicenseNumber", registerForm.getBusinessLicenseNumber());
        map.put("organizingInstitutionBarCode", registerForm.getOrganizingInstitutionBarCode());
        map.put("taxRegistrationId", registerForm.getTaxRegistrationId());
        map.put("unifiedSocialCreditIdentifier", registerForm.getUnifiedSocialCreditIdentifier());
        int count =  this.userInfoDao.checkEnterpriseCertificate(map);
        return count > 0 ? false : true;
    }

    /**
     * 企业联系信息
     */
    private void addCompanyContactInfo(String userId, String lxName, String lxTel){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("lxName", lxName);
        paramMap.put("lxTel", lxTel);
        // 企业联系信息
        userInfoDao.addCompanyContactInfo(paramMap);
    }

    /**
     * 企业介绍资料
     */
    private void addCompanyProfileInfo(String userId){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        // 企业介绍资料
        userInfoDao.addCompanyProfileInfo(paramMap);
    }

    /**
     * 企业基础信息
     */
    private void addCompanyBaseInfo(String userId,String username, RegisterForm registerForm){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("username", username);

        if(StringUtils.isBlank(registerForm.getUnifiedSocialCreditIdentifier())){
            paramMap.put("businessLicenseNumber", registerForm.getBusinessLicenseNumber());
            paramMap.put("taxRegistrationId", registerForm.getTaxRegistrationId());
            paramMap.put("organizingInstitutionBarCode", registerForm.getOrganizingInstitutionBarCode());
        }else{
            paramMap.put("unifiedSocialCreditIdentifier", registerForm.getUnifiedSocialCreditIdentifier());
        }
        // 企业基础信息
        userInfoDao.addCompanyBaseInfo(paramMap);
    }

    @Override
    public Map<String, Object> getUserAssetsByXW(Integer userId) {
        Map<String, Object> data = new LinkedHashMap<>();
        BigDecimal balance = userAccountDao.getCgBalance(userId); //新网投资账户余额
        if ( balance == null) {//可能系统没有保存用户的资金账户
            balance = BigDecimal.ZERO;
        }
        StringBuffer pplatform = new StringBuffer("INVESTOR");

        BigDecimal withdrawFreezeAmount  = withdrawDao.getCGWithdrawFreezeSum(userId, VersionTypeEnum.CG.getCode()); //提现冻结金额


        BigDecimal tenderFreezeAmount = userAccountDao.getNewTenderFreezeSum(userId,VersionTypeEnum.CG.getIndex());


        //BigDecimal totalGainsYesterday = tradeService.getEarningsYesterdaySum(userId); //昨天总收益

        BigDecimal totalAssets = balance.add(tenderFreezeAmount).add(withdrawFreezeAmount);

        BigDecimal dueInPrincipal = BigDecimal.ZERO;
        BigDecimal dueInterest = BigDecimal.ZERO;
        BigDecimal dueOthers = BigDecimal.ZERO;

        DueInAmount dueInAmount = tradeService.getNewDueInAmount(userId,VersionTypeEnum.CG);//3.2.0版本适用待收本息
        DueInAmount planDueInAmount = tradeService.getPlanDueInAmount(userId,VersionTypeEnum.CG);//计划待收本息
        BigDecimal YHGains =  tradeService.getNewYHGains(String.valueOf(userId),VersionTypeEnum.CG);//3.2.0已获收益
        BigDecimal PlanYHGains =  tradeService.getPlanYHGains(String.valueOf(userId),VersionTypeEnum.CG);
        if (dueInAmount != null&&planDueInAmount!=null) {
            dueInPrincipal = dueInAmount.getPrincipal().add(planDueInAmount.getPrincipal());
            dueInterest = dueInAmount.getInterest().add(planDueInAmount.getInterest());
            dueOthers = dueInAmount.getOthers().add(planDueInAmount.getOthers());
            totalAssets = totalAssets.add(dueInPrincipal).add(dueInterest).add(dueOthers);
        }else if(null ==dueInAmount &&planDueInAmount!=null){
            dueInPrincipal =planDueInAmount.getPrincipal();
            dueInterest =planDueInAmount.getInterest();
            dueOthers = planDueInAmount.getOthers();
            totalAssets = totalAssets.add(dueInPrincipal).add(dueInterest).add(dueOthers);
        }else if(dueInAmount != null&&null == planDueInAmount){
            dueInPrincipal = dueInAmount.getPrincipal();
            dueInterest = dueInAmount.getInterest();
            dueOthers = dueInAmount.getOthers();
            totalAssets = totalAssets.add(dueInPrincipal).add(dueInterest).add(dueOthers);
        }
        if(PlanYHGains!=null){
            YHGains = PlanYHGains.add(YHGains);
        }


        Map<String, Object> projectsQty = new HashMap<>(3);
        Map<String, Map<String, String>> qty = finacingService.getInvestmentQty(userId, VersionTypeEnum.CG);
        projectsQty.put(Status.TBZ.name(), qty.get(Status.TBZ.name()) == null ? 0 : qty.get(Status.TBZ.name()).get("qty"));
        projectsQty.put(Status.HKZ.name(), qty.get(Status.HKZ.name()) == null ? 0 : Integer.parseInt(qty.get(Status.HKZ.name()).get("qty")));
        projectsQty.put(Status.YJQ.name(), qty.get(Status.YJQ.name()) == null ? 0 : qty.get(Status.YJQ.name()).get("qty"));
        projectsQty.put(Status.DFK.name(), qty.get(Status.DFK.name()) == null ? 0 : qty.get(Status.DFK.name()).get("qty"));

        data.put("projectsQty", projectsQty);

        //data.put("totalGainsYesterday", totalGainsYesterday.toString());
        data.put("dueInPrincipal", dueInPrincipal.toString());
        data.put("balance", balance.toString());
        data.put("tenderFreezeAmount", tenderFreezeAmount.toString());
        data.put("withdrawFreezeAmount", withdrawFreezeAmount.toString());
        data.put("totalAssets", totalAssets.toString());
        data.put("expectGains", dueInterest.add(dueOthers).toString());
        data.put("historyGains", YHGains.toString());
        data.put("totalGains", dueInterest.add(dueOthers).add(YHGains).toString());

        data.put("investAsset",  dueInPrincipal.add( dueInterest.add(dueOthers)).toString());
        //现金资产
        data.put("cashAsset", (balance.add(tenderFreezeAmount).add(withdrawFreezeAmount)).toString());
        //账户总资产
        data.put("totalAsset", totalAssets.toString());
        return data;
    }

    @Override
    public Map<String, Object> getXWCardInfo(Integer userId,String role) {
        Integer applyTimes = userSecurityDao.getApplyUnbindTimes(userId, role);
        XinwangUserInfo userInfo = xwUserInfoService.queryUserInfo(role + userId);
        Map<String, Object> xwcgInfo = new HashMap<>();
        if (userInfo != null) {
            xwcgInfo.put("isXWOpenAccount", userInfo.getErrorCode()!=null?0:1);
            xwcgInfo.put("isAccountActivity", userInfo.getBankcardNo()!=null?1:0);
            String cgAccount = userInfo.getBankcardNo();

            xwcgInfo.put("cgAccount", BankCardFormater.format(cgAccount));
            xwcgInfo.put("allowUnbind", applyTimes > 0 ? 1 : 0);
            XWBankInfo bankInfo = xwBankService.getBankInfo(userInfo.getBankcode());
            xwcgInfo.put("bankCode", bankInfo.getBankCode());
            xwcgInfo.put("bankName", bankInfo.getBankName());
            List<PaymentLimitVO> limitVOList = new ArrayList<>(1);
            if (bankInfo.getBankCode() != null) {
                limitVOList = rechargeService.getLimitList(bankInfo.getBankCode(), SysPaymentInstitution.XW.getChannelCode()+"");

            }
            xwcgInfo.put("singleLimit",limitVOList.size() == 0?null: limitVOList.get(0).getSingleLimit());
            xwcgInfo.put("dailyLimit", limitVOList.size() == 0?null:limitVOList.get(0).getDailyLimit());
            xwcgInfo.put("monthlyLimit", limitVOList.size() == 0?null:limitVOList.get(0).getMonthlyLimit());

        }
        return xwcgInfo;
    }

    @Override
    public Map<String,Object> getXwCardCode(Integer userId,String userType) {
        return userInfoDao.getXwCardCode(userId,userType);
    }

    @Override
    public UserBaseInfo getuserBaseInfo(Integer userId) {
        return userBaseInfoDao.getBaseInfo(userId);
    }

    @Override
    public String isBorrower(int userId) {
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("userId", userId);
        return this.userInfoDao.isBorrower(userMap);
    }

    @Override
    public String getAuthStatusList(int userId,String role) {
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("userId", userId);
        userMap.put("role", role);
        return this.userInfoDao.getAuthStatusList(userMap);
    }

    @Override
    public Map<String,Object>  updateAuthStatus(int userId,String role,String status, String redirectUrl) throws Exception {
        Map<String, Object> userMap = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<>();
        userMap.put("userId", userId);
        userMap.put("role", role);
        userMap.put("status", status);
        if ("1".equals(status)) { // 1:用户授权
            resultMap = xwUserAuthService.doUserAuth(userId, role, null, null, null, redirectUrl);
        } else if ("0".equals(status)){ // 0:用户取消授权
            if (xwUserAuthService.cancelUserAuth(userId, role, null)) {
//                this.userInfoDao.updateAuthStatus(userMap); 这个更新操作放在 新网那边
                resultMap.put("isSuccess", true);
            }
        }
        return resultMap;
    }
}
