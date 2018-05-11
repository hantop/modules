package com.fenlibao.p2p.service.bid.impl;

import com.fenlibao.p2p.dao.bid.BidInfoDao;
import com.fenlibao.p2p.dao.bid.TenderShareDao;
import com.fenlibao.p2p.dao.bid.TenderShareSettingDao;
import com.fenlibao.p2p.dao.coupon.CouponDao;
import com.fenlibao.p2p.dao.creditassignment.CreditAssigmentDao;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.entity.coupons.UserCouponInfo;
import com.fenlibao.p2p.model.entity.redenvelope.ReceiveTenderShareEntity;
import com.fenlibao.p2p.model.entity.redenvelope.ShareRewardEntity;
import com.fenlibao.p2p.model.entity.redenvelope.TenderShareEntity;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.invite.InviteUrlInfoVO_131;
import com.fenlibao.p2p.model.vo.share.InvestShareVO;
import com.fenlibao.p2p.model.vo.share.ShareRecordVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.TenderShareService;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.RandomUtils;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Sender;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by zcai on 2016/6/25.
 */
@Service
public class TenderShareServiceImpl implements TenderShareService {
    private static final Logger logger = LogManager.getLogger(TenderShareServiceImpl.class);
    private static final int BID = 0;
    private static final int OLD_PLAN = 1;
    private static final int CREDIT = 2;
    private static final int NEW_PLAN = 3;

    @Resource
    private TenderShareDao tenderShareDao;
    @Resource
    private BidInfoDao bidInfoDao;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private RedpacketService redpacketService;
    @Resource
    private ITradeService tradeService;
    @Resource
    private CreditAssigmentDao creditAssigmentDao;
    @Resource
    private CouponDao couponDao;
    @Resource
    private TenderShareSettingDao tenderShareSettingDao;

    @Transactional
    @Override
    public InviteUrlInfoVO_131 addTenderShare(int userId, int shareId, int itemType) throws Exception {
        InvestShareVO investVO;
        switch (itemType) {
            case BID:
                investVO = bidInfoDao.getTenderIdLatest(userId, shareId);
                break;
            case OLD_PLAN:
                investVO = tradeService.getTenderIdOldPlan(userId, shareId);
                break;
            case CREDIT:
                investVO = creditAssigmentDao.getTenderIdLatest(userId, shareId);
                break;
            case NEW_PLAN:
                investVO = tradeService.getTenderIdNewPlan(userId, shareId);
                break;
            default:
                throw new BusinessException(ResponseCode.BID_SHARE_WITHOUT_INVESTMENT);
        }
        //校验分享次数
        validateShareRecord(userId,investVO.getId(),itemType);
        //添加分享记录
        Map<String, Object> resultEntity = tenderShareSettingDao.getTenderShareSettingId(investVO.getBuyAmount());
        TenderShareEntity entity = new TenderShareEntity();
        entity.setTenderId(investVO.getId());
        Date effectiveTime = DateUtil.dateAdd(new Date(), Integer.valueOf(Config.get("invest.share.redenvelope.effective.days"))); //分享记录有效天数
        entity.setUserId(Integer.valueOf(userId));
        entity.setCode(UUID.randomUUID().toString().replaceAll("-", ""));
        entity.setEffectiveTime(effectiveTime);
        entity.setQuantity(Integer.valueOf(resultEntity.get("qty").toString()));
        entity.setRemainingQty(Integer.valueOf(resultEntity.get("qty").toString()));
        entity.setSettingId((int)resultEntity.get("settingId"));
        entity.setItemType(itemType);
        tenderShareDao.addTenderShare(entity);
        //分享信息
        UserInfo userInfo = userInfoService.getUserInfo(userId);
        InviteUrlInfoVO_131 inviteUrlVO = new InviteUrlInfoVO_131();
        String normalInviteUrl = Config.get("invest.share.redenvelope.url").replace("#{code}", entity.getCode());
        String shortUrl = CommonTool.genShortUrl(normalInviteUrl);
        inviteUrlVO.setInvitePicUrl(Config.get("invest.share.redenvelope.icon"));
        inviteUrlVO.setFriendShareInviteTitle(Config.get("invest.share.redenvelope.title"));
        inviteUrlVO.setFriendShareInviteMsg(Config.get("invest.share.redenvelope.moments"));
        inviteUrlVO.setFriendCircleInviteMsg(Config.get("invest.share.redenvelope.moments"));
        inviteUrlVO.setInviteUrl(shortUrl);
        inviteUrlVO.setNormalInviteUrl(normalInviteUrl);
        inviteUrlVO.setPhoneSmsInviteMsg(String.format(Config.get("invest.share.redenvelope.sms"),
                userInfo.getFullName(), shortUrl).replaceAll("#", "%"));
        return inviteUrlVO;
    }

    private void validateShareRecord(int userId,int tenderId,int itemType) {
        int count = tenderShareDao.getShareCount(String.valueOf(userId), tenderId,itemType);
        if (count > 0) {
            throw new BusinessException(ResponseCode.BID_SHARE_ALREADY);
        }
    }

    @Transactional
    @Override
    public Map<String, Object> addReceiveRecord(String phoneNum, String redEnvelopeCode) throws Exception {
        Map<String, Object> result = null;
        TenderShareEntity shareEntity = tenderShareDao.getRecordByCode(redEnvelopeCode);
        ReceiveTenderShareEntity entity = new ReceiveTenderShareEntity();
        if (shareEntity == null) {
            throw new BusinessException(ResponseCode.BID_SHARE_RECORD_NOT_EXIST);
        }
        int count = tenderShareDao.getReceiveCount(phoneNum, shareEntity.getId());
        if (count > 0) {
            throw new BusinessException(ResponseCode.BID_SHARE_ALREADY_GET);
        }
        if (DateUtil.nowDate().after(shareEntity.getEffectiveTime())) { //分享已过期
            throw new BusinessException(ResponseCode.BID_SHARE_FINISH); //产品要求提示这个
        }
        UserInfo userInfo = userInfoService.getUserInfo(phoneNum);
        if (0 == shareEntity.getRemainingQty()) {
            throw new BusinessException(ResponseCode.BID_SHARE_FINISH);
        }
        if (userInfo != null) { //已注册的用户现发放红包，未注册的注册的时候再发放

            tenderShareDao.updateShareRemainingQty(shareEntity.getId(), shareEntity.getRemainingQty() - 1);

            result = grantRedEnvelopeOrRateCoupon(userInfo.getUserId(),userInfo.getPhone(),redEnvelopeCode,shareEntity.getSettingId(),0);
        } else {

            tenderShareDao.updateShareRemainingQty(shareEntity.getId(), shareEntity.getRemainingQty() - 1);
            result = grantRedEnvelopeOrRateCoupon(null,phoneNum,redEnvelopeCode,shareEntity.getSettingId(),1);
        }
        entity.setPhoneNum(phoneNum);
        entity.setInvestmentShareId(shareEntity.getId());
        entity.setRedEnvelopeId((Integer) result.get("id"));
        entity.setCouponType((Integer) result.get("type"));

        tenderShareDao.addReceiveRecord(entity);
        result.remove("id");
        return result;
    }

    /**
     * 投资分享可以是返现券也可以加息券
     * @param userId
     * @param phoneNum
     * @param settingId
     * @return
     */
    private Map<String,Object> grantRedEnvelopeOrRateCoupon(String userId, String phoneNum,String redEnvelopeCode, int settingId,int isNovice)  throws Exception {
        Map<String, Object> result = new HashMap<>();

        List<ShareRewardEntity> rewardEntitieList = tenderShareSettingDao.getTenderShareEntityList(settingId,isNovice);
        if (rewardEntitieList == null || rewardEntitieList.size() == 0) {
            throw new BusinessException(ResponseCode.BID_SHARE_RECORD_NOT_EXIST);
        }

        //读取分享剩余的可获取次数
        List<ShareRecordVO> shareRecords = tenderShareDao.getRestShareNum(redEnvelopeCode);
        if (shareRecords != null && shareRecords.size() > 0) {
            ShareRewardEntity userCouponInfo = null;
            ShareRecordVO shareRecordVO = null;
            //移除超过分享次数的优惠券
            List<ShareRewardEntity> tempList = rewardEntitieList;
            for (int i = 0; i < tempList.size(); i++) {
                userCouponInfo = tempList.get(i);
                for (int j = 0; j < shareRecords.size(); j++) {
                    shareRecordVO = shareRecords.get(j);
                    if (shareRecordVO.getRedEnvelopId() == userCouponInfo.getRewardId() && shareRecordVO.getTotal() >= userCouponInfo.getGetTimes()) {
                        rewardEntitieList.remove(i);
                        i--;
                    }
                }
            }
        }
        //如果已经被领取完返回
        if (rewardEntitieList == null || rewardEntitieList.size() == 0) {
            throw new BusinessException(ResponseCode.BID_SHARE_FINISH);
        }

        //判断是否含有返现券
        boolean containRate = false;
        boolean containRed = false;
        for (int i = 0; i < rewardEntitieList.size(); i++) {
            if (rewardEntitieList.get(i).getRewardType() == 2) {
                containRate = true;
            }
            if (rewardEntitieList.get(i).getRewardType() == 1) {
                containRed = true;
            }
        }
        if(containRate&&containRed){//同时含有加息和返现 随机抽奖
            if (RandomUtils.getRandomInt(10) % 2 == 0) {
                result = grantRedEnvelope(userId, phoneNum, settingId, isNovice,shareRecords);
            }else{
                result = grantRateCoupon(userId, settingId,isNovice,shareRecords);
            }
        }else if(containRed&&!containRate){//只含有返现
            result = grantRedEnvelope(userId, phoneNum,settingId,isNovice,shareRecords);
        } else if (!containRed && containRate) {//只含有加息
            result = grantRateCoupon(userId,settingId,isNovice,shareRecords);
        }
        return result;
    }

    /**
     * 发放加息券-固定设置
     * @param userId
     * @param shareRecords
     * @return
     * @throws Exception
     */
    private Map<String, Object> grantRateCoupon(String userId, int settingId, int isNovice, List<ShareRecordVO> shareRecords) throws Exception {
        Map<String, Object> result = new HashMap<>(3);

        Map<String, Object> param = new HashMap<>();
        param.put("activityCode",InterfaceConst.TENDER_SHARE_CODE);
        param.put("settingId", settingId);
        param.put("isNovice", isNovice);
        List<UserCouponInfo> couponList = couponDao.getByTenderSetting(param);//获取投资分享的优惠券
        if (couponList == null || couponList.size() == 0) {
            throw new BusinessException(ResponseCode.BID_SHARE_FINISH);
        }

        //读取分享剩余的可获取次数
        if (shareRecords != null && shareRecords.size() > 0) {
            UserCouponInfo userCouponInfo = null;
            ShareRecordVO shareRecordVO = null;
            //移除超过分享次数的优惠券
            List<UserCouponInfo> tempList = couponList;
            for (int i = 0; i < tempList.size(); i++) {
                userCouponInfo = tempList.get(i);
                for (int j = 0; j < shareRecords.size(); j++) {
                    shareRecordVO = shareRecords.get(j);
                    if (shareRecordVO.getRedEnvelopId() == userCouponInfo.getCouponId() && shareRecordVO.getTotal() >= userCouponInfo.getTimes()) {
                        couponList.remove(i);
                        i--;
                    }
                }
            }
        }

        //如果已经被领取完返回
        if (couponList == null || couponList.size() == 0) {
            throw new BusinessException(ResponseCode.BID_SHARE_FINISH);
        }

        UserCouponInfo couponInfo = RandomUtils.getRandomElement(couponList);
        if (StringUtils.isNotBlank(userId)) {
            couponInfo.setUserId(Integer.valueOf(userId));
            Date date = DateUtil.getLastTimeOfToday(new Date());
            couponInfo.setValidTime(DateUtil.dateAdd(date, couponInfo.getEffectDay()));
            couponDao.grantRateCoupon(couponInfo);
        }
        result.put("amount", couponInfo.getScope().multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_FLOOR).toString());
        result.put("type", 2);//加息券
        result.put("id", couponInfo.getCouponId());
        return result;

    }

    /**
     * 发放红包-固定设置
     * @param userId
     * @param phoneNum
     * @param shareRecords
     * @throws Exception
     */
    private Map<String, Object> grantRedEnvelope(String userId, String phoneNum, int settingId, int isNovice, List<ShareRecordVO> shareRecords) throws Exception {
        Map<String, Object> result = new HashMap<>(3);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("type",InterfaceConst.REDPACKET_TENDER_SHARE);
        paramMap.put("currentTime",new Timestamp(System.currentTimeMillis()));
        paramMap.put("settingId",settingId);
        paramMap.put("isNovice",isNovice);
        List<UserRedPacketInfo> rList = redpacketService.getActivityRedBagBySetting(paramMap);
        if (rList == null || rList.size() == 0) {
            throw new BusinessException(ResponseCode.BID_SHARE_FINISH);
        }

        //读取分享剩余的可获取次数
        if (shareRecords != null && shareRecords.size() > 0) {
            UserRedPacketInfo userCouponInfo = null;
            ShareRecordVO shareRecordVO = null;
            //移除超过分享次数的优惠券
            for (int i = 0; i < rList.size(); i++) {
                userCouponInfo = rList.get(i);
                for (int j = 0; j < shareRecords.size(); j++) {
                    shareRecordVO = shareRecords.get(j);
                    if (shareRecordVO.getRedEnvelopId() == userCouponInfo.getHbId() && shareRecordVO.getTotal() >= userCouponInfo.getTimes()) {
                        rList.remove(i);
                        i--;
                    }
                }
            }
        }

        //如果已经被领取完返回
        if (rList == null || rList.size() == 0) {
            throw new BusinessException(ResponseCode.BID_SHARE_FINISH);
        }

        UserRedPacketInfo userRedPacketInfo = RandomUtils.getRandomElement(rList);
        if (StringUtils.isNotBlank(userId)) {
            rList.clear();
            rList.add(userRedPacketInfo);
            String smsTemplate = Sender.get("invest.share.redpacket.receive");
            String letterSuffix = Sender.get("znx.suffix.content");
            redpacketService.addUserRedpackets(rList, userId, phoneNum, smsTemplate, letterSuffix, false); //不发送短信
        }
        result.put("amount", userRedPacketInfo.getHbBalance().setScale(0, BigDecimal.ROUND_FLOOR).toString());
        result.put("type", 1);//返现券
        result.put("id", userRedPacketInfo.getHbId());
        return result;
    }

    @Transactional
    @Override
    public void grantRedEnvelopeForRegister(String userId, String phoneNum) {
        try {
            List<ReceiveTenderShareEntity> redEnvelopeIds = tenderShareDao.getRedEnvelopeQty(phoneNum);
            if (redEnvelopeIds != null || redEnvelopeIds.size() > 0) {
                for (ReceiveTenderShareEntity rts : redEnvelopeIds) {
                    if (1 == rts.getCouponType()) {
                        grantRedEnvelopeByRegister(rts.getRedEnvelopeId(), rts.getRedEnvelopeQty(), userId, phoneNum);
                    } else if (2 == rts.getCouponType()) {
                        grantRateCouponByRegister(Integer.valueOf(userId), rts.getRedEnvelopeId(), rts.getRedEnvelopeQty());
                    }
                }
            }
        } catch (Exception e) { //这里不抛出,不能影响注册
            logger.error("注册发放注册前获取的投标分享的红包失败,userId=["+userId+"]", e);
        }
    }

    /**
     * 发放用户未注册时领取的返现券
     * @param redEnvelopeId
     * @param qty
     */
    private void grantRedEnvelopeByRegister(Integer redEnvelopeId, int qty, String userId, String phoneNum) throws Exception {
        List<UserRedPacketInfo> grantList = new ArrayList<>(); //发放红包总数
        List<UserRedPacketInfo> rList = redpacketService.getActivityRedBagByType(InterfaceConst.REDPACKET_TENDER_SHARE);
        if (rList == null || rList.size() == 0) {
            throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_TYPE_NOT_EXIST);
        }
        for (UserRedPacketInfo redPacketInfo : rList) {
            if (redEnvelopeId.equals(redPacketInfo.getHbId())) {
                for (int i = qty; i > 0; i--) {
                    grantList.add(redPacketInfo);
                }
                break;
            }
        }
        if (grantList.size() > 0) {
            String smsTemplate = Sender.get("invest.share.redpacket.receive");
            String letterSuffix = Sender.get("znx.suffix.content");
            redpacketService.addUserRedpackets(grantList, userId, phoneNum, smsTemplate, letterSuffix, false); //不发送短信
        } else {
            logger.warn("userId=[{}],用户注册领取出借红包失败，不存在用户领取的红包", userId);
        }
    }

    /**
     * 发放用户未注册时领取的加息券
     * @param couponId
     * @param qty
     */
    private void grantRateCouponByRegister(Integer userId, Integer couponId, int qty) throws Exception {
        List<UserCouponInfo> couponLisst = couponDao.getByActivityCode(InterfaceConst.TENDER_SHARE_CODE);//获取投资分享的优惠券
        for (UserCouponInfo coupon : couponLisst) {
            if (coupon.getCouponId().equals(couponId)) {
                coupon.setUserId(userId);
                coupon.setValidTime(DateUtil.dateAdd(new Date(), coupon.getEffectDay()));
                for (int i = qty; i > 0; i--) {
                    couponDao.grantRateCoupon(coupon);
                }
                break;
            }
        }
    }

    /**
     * 对返现券和加息券随机
     * @param userId
     * @param phoneNum
     * @return
     * @throws Exception
     */
    private Map<String, Object> randomGrant(String userId, String phoneNum,int settingId,int isNovice) throws Exception {
        if (RandomUtils.getRandomInt(10) % 2 == 0) {
            return grantRedEnvelope(userId, phoneNum);
        }
        return grantRateCoupon(userId, phoneNum);
    }

    /**
     * 发放红包 从3.1.0开始废弃
     * @param userId
     * @param phoneNum
     * @throws Exception
     */
    @Deprecated
    private Map<String, Object> grantRedEnvelope(String userId, String phoneNum) throws Exception {
        Map<String, Object> result = new HashMap<>(3);
        List<UserRedPacketInfo> rList = redpacketService.getActivityRedBagByType(InterfaceConst.REDPACKET_TENDER_SHARE);
        if (rList == null || rList.size() == 0) {
            throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_TYPE_NOT_EXIST);
        }
        UserRedPacketInfo userRedPacketInfo = RandomUtils.getRandomElement(rList);
        if (StringUtils.isNotBlank(userId)) {
            rList.clear();
            rList.add(userRedPacketInfo);
            String smsTemplate = Sender.get("invest.share.redpacket.receive");
            String letterSuffix = Sender.get("znx.suffix.content");
            redpacketService.addUserRedpackets(rList, userId, phoneNum, smsTemplate, letterSuffix, false); //不发送短信
        }
        result.put("amount", userRedPacketInfo.getHbBalance().setScale(0, BigDecimal.ROUND_FLOOR).toString());
        result.put("type", 1);//返现券
        result.put("id", userRedPacketInfo.getHbId());
        return result;
    }

    /**
     * 发放加息券  从3.1.0开始废弃
     * @param userId
     * @param phoneNum
     * @return
     * @throws Exception
     */
    @Deprecated
    private Map<String, Object> grantRateCoupon(String userId, String phoneNum) throws Exception {
        Map<String, Object> result = new HashMap<>(3);
        List<UserCouponInfo> couponLisst = couponDao.getByActivityCode(InterfaceConst.TENDER_SHARE_CODE);//获取投资分享的优惠券
        if (couponLisst != null && couponLisst.size() > 0) {
            UserCouponInfo couponInfo = RandomUtils.getRandomElement(couponLisst);
            if (StringUtils.isNotBlank(userId)) {
                //发送站内信
//                String smsTemplate = Sender.get("invest.share.redpacket.receive");
//                String letterSuffix = Sender.get("znx.suffix.content");
                couponInfo.setUserId(Integer.valueOf(userId));
                couponInfo.setValidTime(DateUtil.dateAdd(new Date(), couponInfo.getEffectDay()));
                couponDao.grantRateCoupon(couponInfo);
            }
            result.put("amount", couponInfo.getScope().multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_FLOOR).toString());
            result.put("type", 2);//加息券
            result.put("id", couponInfo.getCouponId());
            return result;
        }
        return result;
    }


}
