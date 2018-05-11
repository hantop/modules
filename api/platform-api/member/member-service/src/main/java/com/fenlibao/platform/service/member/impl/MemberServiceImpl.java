package com.fenlibao.platform.service.member.impl;

import com.fenlibao.platform.common.consts.GlobalConsts;
import com.fenlibao.platform.common.exception.BusinessException;
import com.fenlibao.platform.common.util.DateUtil;
import com.fenlibao.platform.common.util.PasswordCryptUtil;
import com.fenlibao.platform.dao.CommonMapper;
import com.fenlibao.platform.dao.member.MemberMapper;
import com.fenlibao.platform.model.RedisConst;
import com.fenlibao.platform.model.Response;
import com.fenlibao.platform.model.Restriction;
import com.fenlibao.platform.model.integral.PointsRecord;
import com.fenlibao.platform.model.integral.PointsType;
import com.fenlibao.platform.model.member.MemberConsumeRecord;
import com.fenlibao.platform.model.member.MemberPoints;
import com.fenlibao.platform.model.member.MerchantMember;
import com.fenlibao.platform.model.p2p.enums.T6110_F06;
import com.fenlibao.platform.model.p2p.enums.T6110_F07;
import com.fenlibao.platform.model.p2p.enums.T6110_F08;
import com.fenlibao.platform.model.p2p.enums.T6110_F10;
import com.fenlibao.platform.model.p2p.model.UserInfo;
import com.fenlibao.platform.model.p2p.model.UserRedPacketInfo;
import com.fenlibao.platform.model.p2p.model.UserSecurityAuthentication;
import com.fenlibao.platform.model.p2p.situation.AccountEnum;
import com.fenlibao.platform.model.p2p.situation.InterfaceConst;
import com.fenlibao.platform.model.p2p.situation.Status;
import com.fenlibao.platform.service.CommonService;
import com.fenlibao.platform.service.RedisService;
import com.fenlibao.platform.service.integral.IntegralService;
import com.fenlibao.platform.service.member.MemberService;
import com.fenlibao.platform.common.config.Config;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.guice.transactional.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

public class MemberServiceImpl implements MemberService {
	
	protected static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

	@Inject
	private MemberMapper memberMapper;
//	@Inject
//	private MessageService messageService;
    @Inject
    private IntegralService integralService;
    @Inject
    private RedisService redisService;
    @Inject
    private CommonMapper commonMapper;
    @Inject
    private CommonService commonService;
    private static Config CONFIG = ConfigFactory.create(Config.class);
    /*
    20170204
    名创的粉丝注册名创会员时，不再注册默认注册分利宝的会员，
    用户注册后积分自动迁移到用户的积分
     */
    @Transactional
	public String register(String appid, String phoneNum) throws Exception {
		String openid = UUID.randomUUID().toString().replaceAll("-", "");
		Integer userId = memberMapper.getIdByPhoneNum(phoneNum);
		if (userId != null && userId > 0) {
			String result = memberMapper.getOpenid(userId.toString());
			if (StringUtils.isNotBlank(result)) {
				openid = result;
			} else {
                int memberId = createMemberInfo(appid, openid, userId.toString());
				Integer pointsAccountId = integralService.getId(userId);
				if (pointsAccountId == null || pointsAccountId < 1) {
                    Map<String, String> params = new HashMap<>(1);
                    params.put("userId", userId.toString());
					memberMapper.initIntegralAccount(params);
                    pointsAccountId = Integer.parseInt(String.valueOf(params.get("pointsAccountId")));
				}
                memberMapper.addMemberPhoneNum(phoneNum, memberId, pointsAccountId, getChannelCode(appid));
//				暂不发送短息
//				MessageConfig messageConfig = ConfigFactory.create(MessageConfig.class);
//				messageService.sendSMS(SMSType.REGISTER_OLD.getCode(), phoneNum, messageConfig.getRegisterOld());
			}
		} else {
//			autoRegister(appid, openid, phoneNum);
            //不再自动注册分利宝账户20170207
            String result = memberMapper.getOpenidByPhoneNum(phoneNum);
            if (StringUtils.isNotBlank(result)) {
                return result;
            }
            int memberId = createMemberInfo(appid, openid, null);
            Map<String, String> params = new HashMap<>(1);
            memberMapper.initIntegralAccount(params);
            int pointsAccountId = Integer.parseInt(String.valueOf(params.get("pointsAccountId")));
            //记录会员手机号码，将积分账户和openid关联起来（其实用户未注册分利宝 不应产生积分账户）
            //当这些会员在分利宝注册的时候，将通过pf.pf_member_info获取会员积分账号ID更新mp.mp_member_points的userId
            //通过mp.mp_member_consume_sheet更新mp.mp_member_points_sheet(积分记录)的userId
            memberMapper.addMemberPhoneNum(phoneNum, memberId, pointsAccountId, getChannelCode(appid));
		}
		return openid;
	}

	private int createMemberInfo(String appid, String openid, String userId) throws Exception {
        Map<String, String> params = new HashMap<>(3);
        params.put("businessId", commonService.getMerchantId(appid));
        params.put("openid", openid);
        if (StringUtils.isNotBlank(userId)) {
            params.put("userId", userId);
        }
		memberMapper.createMemberInfo(params);
        String memberId = String.valueOf(params.get("memberId"));
        return Integer.parseInt(memberId);
	}

	private void autoRegister(String appid, String openid, String phoneNum) throws Exception {
		String pwd = getRandomPwd();
		//普通注册分利宝账号
		String userId = addUserInfo(phoneNum, pwd);
		if (StringUtils.isBlank(userId)) {
			throw new BusinessException(Response.MEMBER_CREATE_USER_FAILURE);
		}
		addUserSecurityAuthentication(phoneNum, userId);
        memberMapper.addUserBaseInfo(userId);
        // 分别新增资金账户(往来账户、风险保证金账户、锁定账户)
        addUserAccount(phoneNum, userId);
        // 用户信用账户表
        memberMapper.addUserCredit(userId);
        // 用户理财统计表
        memberMapper.addUserFinancing(userId);
        // 优选理财统计表
        memberMapper.addUserBestFinancing(userId);
        // 获取所有信用认证项(T5123)，并新增用户认证信息(T6120)
        addUserCreditAuthInfo(userId);
        // 用户信用档案表(T6144)
        memberMapper.addUserCreditArchive(userId);
        // 新增用户来源
        memberMapper.addUserOrigin(userId, getChannelCode(appid), 0);
        createMemberInfo(appid, openid, userId);
        Map<String, String> params = new HashMap<>(1);
        params.put("userId", userId);
        memberMapper.initIntegralAccount(params);

        /**
         * 产品需求改为注册奖励在首次登陆时候发
         grantCashBackRedPackets(userId); //送返现红包
         **/
        //发送短信告知用户账号和密码（取消！）
//		MessageConfig messageConfig = ConfigFactory.create(MessageConfig.class);
//		String msg = messageConfig.getRegisterFresh(phoneNum, pwd);
//		messageService.sendSMS(SMSType.REGISTER_NEW.getCode(), phoneNum, msg); //定义一个短信类型，加密保存，解密再发送
	}

	private static String getRandomPwd() {
		Random random = new Random();
		Integer pwd = random.nextInt(899999) + 100000;
		return pwd.toString();
	}

	/**
     * 新增用户基本信息
     * @param phoneNum
     * @param pwd
     */
    private String addUserInfo(String phoneNum, String pwd) throws Exception {
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
        userInfo.setPassword(PasswordCryptUtil.crypt(pwd));
        memberMapper.addUser(userInfo);
        return userInfo.getUserId();
    }
    
    /**
     * 新增用户安全认证表
     * @param phoneNum
     * @param userId
     */
    private void addUserSecurityAuthentication(String phoneNum, String userId) throws Exception {
        UserSecurityAuthentication authentication = new UserSecurityAuthentication();
        authentication.setUserId(userId);
        authentication.setPhone(phoneNum);
        authentication.setCardIDAuth(Status.BTG.name());
        authentication.setPhoneAuth(Status.TG.name());
        authentication.setEmailAuth(Status.BTG.name());
        authentication.setTradPasswordAuth(InterfaceConst.AUTH_TRADPASSWORD_NOTSET);
        memberMapper.addUserSecurityAuthentication(authentication);
    }
    
    /**
     * 分别新增资金账户(往来账户、风险保证金账户、锁定账户)
     * @param phoneNum
     * @param userId
     * @throws Exception 
     */
    private void addUserAccount(String phoneNum, String userId) throws Exception {
        Map<String, String> userAccountParamMap = new HashMap<>();
        userAccountParamMap.put("userId", userId);
        for (AccountEnum e : AccountEnum.values()) {
            userAccountParamMap.put("type", e.toString());
            userAccountParamMap.put("account", getAccount(e.toString(), userId));
            userAccountParamMap.put("accountName", phoneNum);
            memberMapper.addUserAccount(userAccountParamMap);
        }
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
    
    /**
     * 获取所有信用认证项(T5123)，并新增用户认证信息(T6120)
     * @param userId
     */
    private void addUserCreditAuthInfo(String userId) {
        // 新增用户认证信息(T6120)
        List<Integer> creditAuthItem = memberMapper.getCreditAuthItem();
        for (Integer creditAuthInfoId : creditAuthItem) {
            memberMapper.addUserCreditAuthInfo(userId, creditAuthInfoId);
        }
    }

    public MerchantMember getMerchantMember(String openid) {
        return memberMapper.getMerchantMember(openid);
    }
    
    /**
     * 获取渠道号
     * @param appid
     * @return
     */
    private String getChannelCode(String appid) throws Exception {
    	String channelCode = null;
    	String key = RedisConst.$MERCHANT_CHANNEL_CODE_PREFIX.concat(appid);
    	try (Jedis jedis = redisService.getInstance(RedisConst.DEFAULT_PLATFORM_REDIS_INDEX)) {
    		channelCode = jedis.get(key);
    		if (StringUtils.isBlank(channelCode)) {
    			channelCode = commonMapper.getChannelCode(Integer.valueOf(commonService.getMerchantId(appid)));
    			if (StringUtils.isBlank(channelCode)) {
    				throw new BusinessException(Response.MERCHANT_CHANNEL_CODE_NOT_EXIST);
    			}
    			jedis.set(key, channelCode);
    		}
    	}
    	return channelCode;
    }

    @Transactional
    public void doConsumeRecord(String openid, String amount, String typecode, String pos_sn, MerchantMember member) throws Exception {
    	MemberConsumeRecord cRecord = integralService.getMemberConsumeRecord(pos_sn);
    	if (cRecord != null) {
    		throw new BusinessException(Response.INTEGRAL_POS_SN_EXISTS);
    	}
        PointsType pointsType = integralService.getPointsType(typecode);
        if (pointsType == null) {
        	throw new BusinessException(Response.INTEGRAL_TYPE_NOT_EXIST);
        }
        int gainPoints = integralService.amountToPoints(amount, pointsType.getId());
        MemberPoints points = new MemberPoints();
        if (gainPoints > 0) { //如果消费金额为负数（退款），那么只保存消费记录，不对积分进行操作

            PointsRecord pointsRecord = integralService.getMemberPointsRecord(member.getPfUserId());
            if (pointsRecord == null) {
                pointsRecord = integralService.getMemberPointsRecordByOpenid(openid);
            }
            if (pointsRecord == null) {
                throw new BusinessException(Response.INTEGRAL_ACCOUNT_NOT_EXIST);
            }
            String typeCode = CONFIG.getMiniTypeCode();
            // 如果是线下消费 积分收入
            if (pointsType.getChangeType() == 1 && typeCode.equals(typecode)) {
                // 如果是名创优品线下消费，则做限制，每月限制3000积分
                // 本月可添加积分 0：则不做记录
                int addPoints = integralService.validateThisMonthPoints(gainPoints, typecode, member.getPfUserId(), openid);
                if (addPoints != 0) {
                    saveRecordAndModifyPoint(points, pointsType, member, pointsRecord, openid, addPoints);
                }
            } else {
                saveRecordAndModifyPoint(points, pointsType, member, pointsRecord, openid, gainPoints);
            }
        }
        //保存消费记录
        MemberConsumeRecord consumeRecord = new MemberConsumeRecord();
        consumeRecord.setOpenid(openid);
        consumeRecord.setmId(member.getmId());
        consumeRecord.setAmount(Double.parseDouble(amount));
        consumeRecord.setdId(points.getId());
        consumeRecord.setPosCId(pos_sn);
        memberMapper.saveConsumeRecord(consumeRecord);
    }

    private void saveRecordAndModifyPoint(MemberPoints points, PointsType pointsType, MerchantMember member, PointsRecord pointsRecord, String openid, int gainPoints) throws Exception {
        //保存积分消费记录
        points.settId(pointsType.getId());
        points.setUserId(member.getPfUserId() == 0 ? null : member.getPfUserId());//如果没有注册，先记录，等注册时再关联起来
        points.setNumbers(gainPoints);
        points.setChangeType(pointsType.getChangeType());
        integralService.saveMemberPoints(points);
        if (pointsType.getChangeType() == 1) {
            pointsRecord.setNumbers(pointsRecord.getNumbers() + gainPoints);
        } else {
            pointsRecord.setNumbers(pointsRecord.getNumbers() - gainPoints);
        }
        integralService.updateMemberPointsRecord(pointsRecord);
    }

    public Restriction getAppidAndSecret(String appid, String secret) {
        return memberMapper.getAppidAndSecret(appid, secret);
    }

	public String getSecret(String appid) {
		return memberMapper.getSecret(appid);
	}

	/**
	 * 发放返现红包
	 * @param userId
	 */
	private void grantCashBackRedPackets(String userId) {
		List<UserRedPacketInfo> redPacketInfo = null;
		StringBuilder sb = new StringBuilder();
		try {
			redPacketInfo = memberMapper.getActivityRedPacketByType(GlobalConsts.REDPACKET_CASHBACK);
			if (redPacketInfo != null && redPacketInfo.size() > 0) {
				for (UserRedPacketInfo info : redPacketInfo) {
					// 用户红包有效时间
			        Calendar calendar = buildUserRedpacketValidTime(info.getEffectDay());
			        // 红包有效期
			        Timestamp validTime = new Timestamp(calendar.getTimeInMillis());
			        String validTimeStr = DateUtil.getDateTime(validTime);
					memberMapper.addUserRedpacket(validTimeStr, info.getHbId(), userId);
					sb.append(info.getHbId());
				}
			}
		} catch (Exception e) {
			logger.warn("注册送返现红包失败 >>> [userId:{},redPacketId:{}]", userId, sb);
		}
	}
	
    /**
     * 用户红包有效时间
     * @param effectDay
     * @return
     */
    private Calendar buildUserRedpacketValidTime(Integer effectDay) {
        Calendar calendar = Calendar.getInstance();
        // 用户红包有效时间为当前时间加上红包有效天数
        calendar.add(Calendar.DATE, effectDay);
        // 时间到23:59:59
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar;
    }

    /**
     * 商户委托开户前注册分利宝账户
     * @param phoneNum
     * @return
     * @throws Exception
     */
    @Transactional
    @Override
    public Integer businessRegister(String phoneNum) throws Exception {
        Integer userId = memberMapper.getIdByPhoneNum(phoneNum);
        if (userId != null && userId > 0) {
         return userId;
        } else {
            register(phoneNum);
            return  memberMapper.getIdByPhoneNum(phoneNum);
        }
    }



    private void register(String phoneNum) throws Exception {
        String pwd = getRandomPwd();
        //普通注册分利宝账号
        String userId = addUserInfo(phoneNum, pwd);
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException(Response.MEMBER_CREATE_USER_FAILURE);
        }
        addUserSecurityAuthentication(phoneNum, userId);
        memberMapper.addUserBaseInfo(userId);
        // 分别新增资金账户(往来账户、风险保证金账户、锁定账户)
        addUserAccount(phoneNum, userId);
        // 用户信用账户表
        memberMapper.addUserCredit(userId);
        // 用户理财统计表
        memberMapper.addUserFinancing(userId);
        // 优选理财统计表
        memberMapper.addUserBestFinancing(userId);
        // 获取所有信用认证项(T5123)，并新增用户认证信息(T6120)
        addUserCreditAuthInfo(userId);
        // 用户信用档案表(T6144)
        memberMapper.addUserCreditArchive(userId);

    }
}
