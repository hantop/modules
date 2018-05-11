/**
 * Copyright © 2015 fenlibao.com. All rights reserved.
 *
 * @Title: AccessoryInfoServiceImpl.java
 * @Prject: app-client-api-service
 * @Package: com.fenlibao.p2p.service.impl
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-9 下午2:43:34
 * @version: V1.1
 */
package com.fenlibao.p2p.service.activity.impl;

import com.fenlibao.p2p.dao.SendSmsRecordDao;
import com.fenlibao.p2p.dao.SendSmsRecordExtDao;
import com.fenlibao.p2p.dao.UserInfoDao;
import com.fenlibao.p2p.dao.activity.ActivityDao;
import com.fenlibao.p2p.dao.redpacket.RedpacketDao;
import com.fenlibao.p2p.model.entity.*;
import com.fenlibao.p2p.model.entity.activity.*;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.enums.activity.LtPrizeTypeEnum;
import com.fenlibao.p2p.model.enums.activity.PrizeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.FeeCode;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.lottery.entity.LotteryActivityInfo;
import com.fenlibao.p2p.model.lottery.entity.LotteryPrizeInfo;
import com.fenlibao.p2p.model.lottery.vo.LotteryPrizeInfoVO;
import com.fenlibao.p2p.model.vo.redpacket.RedPacketOlympicActivityVO;
import com.fenlibao.p2p.model.vo.redpacket._RedPacketVO;
import com.fenlibao.p2p.service.UmengService;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.activity.ActivityExtService;
import com.fenlibao.p2p.service.activity.ActivityService;
import com.fenlibao.p2p.service.common.RedisService;
import com.fenlibao.p2p.service.funds.IFundsService;
import com.fenlibao.p2p.service.lottery.LotteryActivityService;
import com.fenlibao.p2p.service.lottery.LotteryPrizeService;
import com.fenlibao.p2p.service.privateMessage.PrivateMessageService;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * @ClassName: AccessoryInfoServiceImpl
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-9 下午2:43:34
 */

@Service
public class ActivityServiceImpl implements ActivityService {
    private static final Logger logger = LogManager.getLogger(ActivityServiceImpl.class);

    @Resource
    ActivityDao activityDao;
    @Resource
    RedpacketDao redpacketDao;
    @Resource
    RedpacketService redpacketService;
    @Resource
    SendSmsRecordDao sendSmsRecordDao;
    @Resource
    SendSmsRecordExtDao sendSmsRecordExtDao;
    @Resource
    IFundsService iFundsService;
    @Resource
    LotteryActivityService lotteryActivityService;
    @Resource
    PrivateMessageService privateMessageService;
    @Resource
    UserInfoDao userInfoDao;
    @Resource
    UserInfoService userInfoService;
    @Resource
    LotteryPrizeService lotteryPrizeService;
    @Resource
    ActivityExtService activityExtService;
    @Resource
    RedisService redisService;
    @Resource
    UmengService umengService;

    /**
     * 抽奖
     *
     * @param orignalRates 原始的概率列表，保证顺序和实际物品对应
     * @return 物品的索引
     */
    public static int lottery(List<Double> orignalRates) {
        if (orignalRates == null || orignalRates.isEmpty()) {
            return -1;
        }
        int size = orignalRates.size();
        // 计算总概率，这样可以保证不一定总概率是1
        double sumRate = 0d;
        for (double rate : orignalRates) {
            sumRate += rate;
        }
        // 计算每个物品在总概率的基础下的概率情况
        List<Double> sortOrignalRates = new ArrayList<Double>(size);
        Double tempSumRate = 0d;
        for (double rate : orignalRates) {
            tempSumRate += rate;
            sortOrignalRates.add(tempSumRate / sumRate);
        }
        // 根据区块值来获取抽取到的物品索引
        double nextDouble = Math.random();
        sortOrignalRates.add(nextDouble);
        Collections.sort(sortOrignalRates);

        return sortOrignalRates.indexOf(nextDouble);
    }

    /**
     * 组建map
     *
     * @param annualMettingRecord
     * @return
     */
    private static Map<String, Object> getAnnualMettingRecordMap(AnnualMettingRecord annualMettingRecord) {
        Map<String, Object> params = new HashMap<String, Object>(0);
        params.put("name", annualMettingRecord.getName());
        params.put("phone", annualMettingRecord.getPhone());
        params.put("type", annualMettingRecord.getType());
        params.put("prizeCode", annualMettingRecord.getPrizeCode());
        params.put("realFlag", annualMettingRecord.getRealFlag());
        return params;
    }

    @Override
    public int insertActivity(String activityCode, String phone, int isNew) {
        return activityDao.insertActivity(activityCode, phone, isNew);
    }

    @Override
    public int validIsRegistActivity(String activityCode, String phone) {
        return activityDao.validRegistActivity(activityCode, phone);
    }

    @Override
    public List<RedPacketOlympicActivityVO> getOlympicRedPacketList(Integer userId, String activityCode) {
        List<RedPacketOlympicActivityVO> list = activityDao.getOlympicRedPacketList(userId, activityCode);
        Date now = new Date();
        for (RedPacketOlympicActivityVO vo : list) {
            if (!now.after(vo.getStartTime())) {
                vo.setActivityStatus(-1); //未开始
            }
            if (now.after(vo.getEndTime())) {
                vo.setActivityStatus(1);//已结束
            }
        }
        return list;
    }

    @Transactional
    @Override
    public void receiveOlympicRedPacket(int userId, int redPacketId, String activityCode) throws Exception {
        Integer unusedRedPacketId = activityDao.getCurdateUnusedOlympicRedPacket(userId, activityCode);
        if (unusedRedPacketId != null && unusedRedPacketId > 0) {
            throw new BusinessException(ResponseCode.ACTIVITY_OLYMPIC_REDPACKET_UNUSED);
        }
        _RedPacketVO redPacket = redpacketService.getById(redPacketId);
        List<UserRedPacketInfo> redPacketList = new ArrayList<>(1);
        if (redPacket == null
                || !activityCode.equals(redPacket.getActivityCode())) {
            throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_NOT_EXIST);
        }
        Date now = new Date();
        if (!now.after(redPacket.getActivityStartTime())) {
            throw new BusinessException(ResponseCode.ACTIVITY_NOT_ACTIVITY_TIME);
        }
        if (now.after(redPacket.getActivityEndTime())) {
            throw new BusinessException(ResponseCode.ACTIVITY_END);
        }
        UserRedPacketInfo ur = new UserRedPacketInfo();
        ur.setHbId(redPacket.getId());
        ur.setHbBalance(redPacket.getAmount());
        ur.setEffectDay(redPacket.getEffectDays());
        redPacketList.add(ur);
        redpacketService.addUserRedpackets(redPacketList, String.valueOf(userId), null, null, null, false); //不发送短信、站内信
    }

    @Override
    public int insertUserActivityEvent(String phone, int eventType) {
        return activityDao.insertUserActivityEvent(phone, eventType);
    }

    @Override
    public Integer getUserActivityEvent(String phone, int eventType) {
        return activityDao.getUserActivityEvent(phone, eventType);
    }

    @Override
    public List<VirusSpreadFreinds> getAugustInvitationFriends(
            String phone) {
        List<VirusSpreadFreinds> list = activityDao.getAugustInvitationFriends(phone);
        if (list != null) {
            for (VirusSpreadFreinds virusSpreadFreinds : list) {
                if (virusSpreadFreinds.getAvatar() == null)
                    virusSpreadFreinds.setAvatar("static/fenlibao/images/ui/lg-pic.png");
                String phonenum = virusSpreadFreinds.getPhone();
                virusSpreadFreinds.setPhone("*******" + phonenum.substring(phonenum.length() - 4, phonenum.length()));
            }
        }
        return list;
    }

    @Override
    public Map<String, Object> minisoPhoneCheck(String phone, String activityCode) {
        return activityDao.minisoPhoneCheck(phone);
    }

    @Override
    public List<McMemberForCashback> getMcMembersForCashback() {
        return activityDao.getMcMembersForCashback();
    }

    @Override
    public List<McMemberForBillSms> getMcMemberForBillSms() {
        return activityDao.getMcMemberForBillSms();
    }

    @Override
    public int insertMcMemberSeptemberRecord(McMemberForCashback mcMember) {
        Map map = new HashMap();
        map.put("userId", mcMember.getUserId());
        map.put("recordId", mcMember.getRecordId());
        map.put("amount", McMemberForCashback.amount);
        return activityDao.insertMcMemberSeptemberRecord(map);
    }

    @Override
    public int insertMcMemberSeptemberSms(McMemberForBillSms mcMember) {
        Map map = new HashMap();
        map.put("userId", mcMember.getUserId());
        map.put("recordId", mcMember.getRecordId());
        map.put("parvalue", mcMember.getParvalue());
        return activityDao.insertMcMemberSeptemberSms(map);
    }

    @Override
    public void sendSms(String content, String phone) {
        Timestamp outTime = new Timestamp(System.currentTimeMillis() / 1000 * 1000 + 30 * 60 * 1000);
        SendSmsRecord record = new SendSmsRecord();
        record.setType(0);
        record.setOutTime(outTime);
        record.setContent(content);
        record.setCreateTime(new Date());
        record.setStatus("W");
        sendSmsRecordDao.insertSendSmsRecord(record);

        SendSmsRecordExt recordExt = new SendSmsRecordExt();
        recordExt.setId(record.getId());
        recordExt.setPhoneNum(phone);
        sendSmsRecordExtDao.insertSendSmsRecordExt(recordExt);
    }

    @Override
    public int uptateMcMemberSeptemberRecord(McMemberForCashback mcMember, String grantState, String failLog) {
        Map map = new HashMap();
        map.put("userId", mcMember.getUserId());
        map.put("grantState", grantState);
        map.put("failLog", failLog);
        return activityDao.uptateMcMemberSeptemberRecord(map);
    }

    @Transactional
    @Override
    public void grantMcMembersCashback(McMemberForCashback mcMember) throws Exception {
        //账号类型（WLZH,LDZH...）
        String accountTypeWlzh = InterfaceConst.ACCOUNT_TYPE_WLZH;
        // 发红包的账户名称
        String salayKey = Config.get("redpacket.grantAccount");
        //现金红包编码
        FeeType feeType = redpacketDao.getFeeType(FeeCode.CACH_REDPACKET);
        //发放现金
        iFundsService.transfer(mcMember.getUserId(), McMemberForCashback.amount,
                feeType, accountTypeWlzh, salayKey);
        //更新发放状态
        uptateMcMemberSeptemberRecord(mcMember, "S", null);
    }

    @Override
    public boolean isActivityTime(String activityCode) {
        Map map = new HashMap();
        map.put("activityCode", activityCode);
        Map<String, Object> resultMap = activityDao.isActivityTime(map);
        if (resultMap == null) {
            throw new BusinessException(ResponseCode.ACTIVITY_NOT_ACTIVITY_TIME.getCode(),
                    ResponseCode.ACTIVITY_NOT_ACTIVITY_TIME.getMessage());
        } else {
            return true;
        }
    }

    @Override
    public int activityState(String activityCode, int mode) {
        Map map = new HashMap();
        map.put("activityCode", activityCode);
        Integer resultMap = activityDao.activityState(map);
        if (resultMap == null) {
            throw new BusinessException(ResponseCode.ACTIVITY_NOT_ACTIVITY_TIME.getCode(),
                    ResponseCode.ACTIVITY_NOT_ACTIVITY_TIME.getMessage());
        }

        if (resultMap == 1) {
            throw new BusinessException(ResponseCode.ACTIVITY_NO_START.getCode(),
                    ResponseCode.ACTIVITY_NO_START.getMessage());
        } else if (resultMap == 3 && mode != 1) {
            throw new BusinessException(ResponseCode.ACTIVITY_END.getCode(),
                    ResponseCode.ACTIVITY_END.getMessage());
        }
        return resultMap;
    }

    @Override
    public List<AnniversaryInvestRecord> anniversaryInvestRecords(String activityCode) {
        activityState(activityCode, 1);
        return activityDao.anniversaryInvestRecords();
    }

    @Override
    public Map<String, Object> myAnniversaryInvestInfo(String activityCode, String userId) {
        activityState(activityCode, null);
        return activityDao.myAnniversaryInvestInfo(userId);
    }

    @Override
    public int activityState(String activityCode, Integer mode) {
        if (StringUtils.isBlank(activityCode)) {
            throw new BusinessException(ResponseCode.ACTIVITY_END.getCode(),
                    ResponseCode.ACTIVITY_END.getMessage());
        }
        Map map = new HashMap();
        map.put("activityCode", activityCode);
        Integer resultMap = activityDao.activityState(map);
        if (resultMap == null) {
            throw new BusinessException(ResponseCode.ACTIVITY_NOT_ACTIVITY_TIME.getCode(),
                    ResponseCode.ACTIVITY_NOT_ACTIVITY_TIME.getMessage());
        }

        if (resultMap == 1) {
            throw new BusinessException(ResponseCode.ACTIVITY_NO_START.getCode(),
                    ResponseCode.ACTIVITY_NO_START.getMessage());
        } else if (resultMap == 3 && mode == null) {
            throw new BusinessException(ResponseCode.ACTIVITY_END.getCode(),
                    ResponseCode.ACTIVITY_END.getMessage());
        }
        return resultMap;
    }

    @Override
    public List<DtbForCashBack> getDtbForCashBack(String activityCode) {
        return activityDao.getDtbForCashBack(activityCode);
    }

    @Override
    public int insertActivityCashbackRecord(DtbForCashBack dtb) {
        Map map = new HashMap();
        map.put("userId", dtb.getUserId());
        map.put("recordId", dtb.getRecordId());
        map.put("amount", dtb.getAmount());
        map.put("code", DtbForCashBack.ACTIVITY_CODE);

        return activityDao.insertActivityCashbackRecord(map);
    }

    @Override
    public int uptateActivityCashbackRecord(Map map) {
        return activityDao.uptateActivityCashbackRecord(map);
    }

    @Transactional
    @Override
    public void grantActivityCashback(Map map) throws Exception {
        //账号类型（WLZH,LDZH...）
        String accountTypeWlzh = InterfaceConst.ACCOUNT_TYPE_WLZH;
        // 发红包的账户名称
        String salayKey = Config.get("redpacket.grantAccount");
        //现金红包编码
        FeeType feeType = redpacketDao.getFeeType(FeeCode.CACH_REDPACKET);
        //发放现金
        iFundsService.transfer((Integer) map.get("userId"), (BigDecimal) map.get("amount"),
                feeType, accountTypeWlzh, salayKey);
        //更新发放状态
        map.put("grantState", "S");
        uptateActivityCashbackRecord(map);
    }

    @Override
    public int deleteActivityCashbackRecord(Map map) {
        return activityDao.deleteActivityCashbackRecord(map);
    }

    @Override
    public void saveUserActivityRecord(String phone, String code) {
        Integer id = activityDao.getUserActivityRecord(phone, code);
        if (id == null) {
            Map map = new HashMap();
            map.put("phone", phone);
            map.put("code", code);
            activityDao.insertUserActivityRecord(map);
        }
    }

    @Override
    public List<AnnualMeetingPrize> getAnnualMeetingPrize(String type) {
        return activityDao.getAnnualMeetingPrize(type);
    }

    @Override
    public AnnualMeetingPrize getAnnualMeetingPrize(String type, String prizeCode) {
        return activityDao.getAnnualMeetingPrize(type, prizeCode);
    }

    @Override
    public int saveDesignated(String phone, String prizeCode, String name) {
        Map map = new HashMap();
        map.put("phone", phone);
        map.put("prizeCode", prizeCode);
        map.put("name", name);
        return activityDao.saveDesignated(map);
    }

    @Override
    public List<AnnualMettingRecord> getIphoneDesignatedist(Integer prizeType, Integer qty) {
        return activityDao.getIphoneDesignatedist(prizeType, qty);
    }

    /**
     * 年会抽红包用户
     *
     * @return
     */
    public List<AnnualMettingParticipant> drawRedPacket(int normalWinnersNum) {
        return activityDao.drawRedPacket(normalWinnersNum);
    }

    /**
     * 年会抽红包--查询指定中间名单
     */
    public List<AnnualMeetingDesignated> getDesignatedistForRedPacket(Integer qty) {
        return activityDao.getDesignatedistForRedPacket(qty);
    }

    /**
     * 年会抽红包--发送现金和短信
     */
    @Transactional
    @Override
    public void grantActivityCashbackForAnnualMeetingRedPacket(AnnualMettingRecord amr) throws Exception {
        //查询用户userId
        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("phoneNum", amr.getPhone());
        UserInfo userInfo = userInfoDao.getUserInfoByPhoneNumOrUsername(userInfoMap);

        //发放现金
        String accountTypeWlzh = InterfaceConst.ACCOUNT_TYPE_WLZH;
        String salayKey = Config.get("redpacket.grantAccount");
        FeeType feeType = redpacketDao.getFeeType(FeeCode.CACH_REDPACKET);
        iFundsService.transfer(Integer.parseInt(userInfo.getUserId()), amr.getAmout(), feeType, accountTypeWlzh, salayKey);

        //更新发放状态
        Map<String, Object> uptateAnnualMettingRecordsMap = new HashMap<>();
        uptateAnnualMettingRecordsMap.put("grantState", 1);
        uptateAnnualMettingRecordsMap.put("phone", amr.getPhone());
        uptateAnnualMettingRecordsMap.put("type", amr.getType());
        uptateAnnualMettingRecords(uptateAnnualMettingRecordsMap);

        //发送短信和站内信
        String content = Config.get("annualMeeting.redpacketWinner.msg");
        content = content.replace("#{amount}", amr.getAmout().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sendSmsAndLetter(userInfo.getPhone(), userInfo.getUserId(), content);
    }

    /**
     * 更新中奖纪录，已发奖
     *
     * @param map
     * @return
     */
    @Override
    public int uptateAnnualMettingRecords(Map map) {
        return activityDao.uptateAnnualMettingRecords(map);
    }

    /**
     * 发送短信和站内信
     *
     * @param phoneNum
     * @param userId
     */
    private void sendSmsAndLetter(String phoneNum, String userId, String content) {
        //发短信
        try {
            sendSms(content, phoneNum);
        } catch (Throwable t) {
            logger.error("发送短信失败", t);
        }
        //站内信
        try {
            privateMessageService.sendLetter(userId, InterfaceConst.PRIVATEMESSAGE_TITLE, content, VersionTypeEnum.PT);
        } catch (Throwable t) {
            logger.error("发送站内信失败", t);
        }
    }

    @Override
    public List<AnnualMeetingPrize> getAnnualMeetingPrize() {
        return activityDao.getAnnualMeetingPrize();
    }

    @Override
    public AnnualMeetingPrize getAnnualMeetingPrizeInfo(String code) {
        Map map = new HashMap();
        map.put("code", code);
        return activityDao.getAnnualMeetingPrizeInfo(map);
    }

    @Override
    public List<AnnualMettingRecord> getDrawHM(int qty) {
        Map map = new HashMap();
        map.put("qty", qty);
        List<AnnualMettingRecord> recordList = activityDao.getRamdomHM(map);
        if (recordList.size() < qty) {
//			throw new BusinessException("110","参与人员不够抽奖数量！");
        }
        return recordList;
    }

    @Transactional
    @Override
    public int saveAnnualMeetingHmRecord(List<AnnualMettingRecord> recordList) {
        //锁住奖品信息
        AnnualMeetingPrize prize = getAnnualMeetingPrizeInfo(AnnualMeeting.HM_CODE);
        if (prize.getQty() < recordList.size() || prize.getQty() == 0) {
            throw new BusinessException("110", "奖品已抽完");
        }
        for (AnnualMettingRecord record : recordList) {
            Map map = new HashMap();
            map.put("name", record.getName());
            map.put("phone", record.getPhone());
            map.put("type", record.getType());
            map.put("prizeCode", record.getPrizeCode());
            map.put("realFlag", record.getRealFlag());
            int re = activityDao.saveAnnualMettingRecord(map);
            if (re != 1) {
                throw new BusinessException("110", "网络异常，操作失败！");
            }
            //查询用户userId
            Map<String, Object> userInfoMap = new HashMap<>();
            userInfoMap.put("phoneNum", record.getPhone());
            UserInfo userInfo = userInfoDao.getUserInfoByPhoneNumOrUsername(userInfoMap);
            //发送短信和站内信
            String content = Config.get("annualMeeting.phonetWinner.msg");
            content = content.replace("#{amount}", prize.getPrizeName());
            sendSmsAndLetter(userInfo.getPhone(), userInfo.getUserId(), content);
        }
        //奖品数量更新
        Map map = new HashMap();
        map.put("prizeCode", AnnualMeeting.HM_CODE);
        map.put("qty", recordList.size());
        return activityDao.updateAnnualMeetingPrizeInfo(map);
    }

    @Transactional
    @Override
    public AnnualMettingRecord getDrawIphone(int qty) {
        //锁住奖品信息
        AnnualMeetingPrize prize = getAnnualMeetingPrizeInfo(AnnualMeeting.IPHONE_CODE);
        if (prize.getQty() == 0 || prize.getQty() < qty) {
            throw new BusinessException("110", "奖品已抽完");
        }
        AnnualMettingRecord result = null;
        //获取苹果指定人员名单
        List<AnnualMettingRecord> recordList = getIphoneDesignatedist(PrizeEnum.IPHONE.getCode(), qty);
        if (recordList == null || recordList.size() < qty) {//没有指定的时候
            //根据投资金额随机产生中奖用户
            List<AnnualMettingDrawRateRecord> drawRateRecordList = activityDao.getAnnualMettingDrawRateRecord(null);
            List<Double> orignalRates = new ArrayList<Double>(drawRateRecordList.size());
            if (drawRateRecordList == null || drawRateRecordList.size() == 0) {
                throw new BusinessException("110", "没有参与人员！");
            }
            for (AnnualMettingDrawRateRecord prizeInfo : drawRateRecordList) {
                double probability = prizeInfo.getDrawRate().doubleValue();
                if (probability < 0) {
                    probability = 0;
                }
                orignalRates.add(probability);
            }
            int drawIndex = lottery(orignalRates);
            AnnualMettingDrawRateRecord record = drawRateRecordList.get(drawIndex);
            result = new AnnualMettingRecord();
            result.setName(record.getName());
            result.setPhone(record.getPhone());

            //保存中奖纪录
            Map map = new HashMap();
            map.put("name", record.getName());
            map.put("phone", record.getPhone());
            map.put("type", PrizeEnum.IPHONE.getCode());
            map.put("prizeCode", AnnualMeeting.IPHONE_CODE);
            map.put("realFlag", 1);
            int re = activityDao.saveAnnualMettingRecord(map);
            if (re != 1) {
                throw new BusinessException("110", "网络异常，操作失败！");
            }
        } else {
            result = recordList.get(0);
            //保存中奖纪录
            Map map = new HashMap();
            map.put("name", result.getName());
            map.put("phone", result.getPhone());
            map.put("type", PrizeEnum.IPHONE.getCode());
            map.put("prizeCode", AnnualMeeting.IPHONE_CODE);
            map.put("realFlag", 1);
            int re = activityDao.saveAnnualMettingRecord(map);
            if (re != 1) {
                throw new BusinessException("110", "网络异常，操作失败！");
            }
        }
        //奖品数量更新
        Map map = new HashMap();
        map.put("prizeCode", AnnualMeeting.IPHONE_CODE);
        map.put("qty", qty);
        activityDao.updateAnnualMeetingPrizeInfo(map);


        //查询用户userId
        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("phoneNum", result.getPhone());
        UserInfo userInfo = userInfoDao.getUserInfoByPhoneNumOrUsername(userInfoMap);
        if (userInfo != null) {
            //发送短信和站内信
            String content = Config.get("annualMeeting.phonetWinner.msg");
            content = content.replace("#{amount}", prize.getPrizeName());
            sendSmsAndLetter(userInfo.getPhone(), userInfo.getUserId(), content);
        }

        return result;
    }

    /**
     * 更新奖品数量
     *
     * @return
     */
    private void updatePrizeNum(AnnualMettingRecord amr) throws Throwable {
        if ("1".equals(amr.getRealFlag())) {
            //减少奖品数量
            Map<String, Object> ampMap = new HashMap<String, Object>();
            ampMap.put("prizeCode", amr.getPrizeCode());
            ampMap.put("qty", 1);
            int res = activityDao.updatePrizeNum(ampMap);
            if (res <= 0) {
                throw new Throwable("该奖品已抽完：" + amr.getPrizeCode());
            }
        }
    }

    /**
     * 插入中奖纪录
     *
     * @return
     */
    private int saveAnnualMettingRecords(AnnualMettingRecord amr) {
        return activityDao.saveAnnualMettingRecords(getAnnualMettingRecordMap(amr));
    }

    /**
     * 更新奖品数量,插入中奖纪录
     *
     * @param amr
     * @throws Throwable
     */
    @Transactional
    @Override
    public void saveAnnualMettingRecordsAndUpdatePrizeNum(AnnualMettingRecord amr) throws Throwable {
        updatePrizeNum(amr);
        saveAnnualMettingRecords(amr);
    }

    /**
     * 模拟抽奖
     *
     * @param prizeType        奖品类型
     * @param normalWinnersNum 预计抽出奖品数量
     */
    public List<AnnualMeetingPrize> lottery(List<AnnualMeetingPrize> effectivePrizeInfos, String prizeType, int normalWinnersNum) {
        //奖品数量
        int remainingPrizeNum = 0;
        for (AnnualMeetingPrize amp : effectivePrizeInfos) {
            remainingPrizeNum += amp.getQty();
        }

        Set<Integer> set = new HashSet<Integer>();
        int count = 0;

        while (count < (normalWinnersNum < remainingPrizeNum ? normalWinnersNum : remainingPrizeNum)) {
            int tmp = (int) (Math.random() * remainingPrizeNum) + 1;
            if (set.add(tmp)) {
                count++;
            }
        }

        List<AnnualMeetingPrize> finalWinningPrizes = new ArrayList<>();
        Iterator<Integer> iter = set.iterator();
        while (iter.hasNext()) {
            int index = iter.next();
            int startNum = 0;
            int endNum = 0;
            for (int i = 0; i < effectivePrizeInfos.size(); i++) {
                AnnualMeetingPrize amp = effectivePrizeInfos.get(i);
                endNum += amp.getQty();
                if (index > startNum && index <= endNum) {
                    finalWinningPrizes.add(amp);
                    break;
                }
                startNum += amp.getQty();
            }
        }
        return finalWinningPrizes;
    }

    /**
     * 获取中奖纪录
     *
     * @param prizeType
     * @return
     */
    public List<AnnualMettingRecord> getAnnualMeetingRecords(Integer prizeType) {
        return activityDao.getAnnualMeetingRecords(prizeType);
    }

    /**
     * 获取弹幕
     *
     * @param prizeType
     * @return
     */
    public List<AnnualMettingParticipant> getAnnualMeetingParticipants(Integer prizeType, Integer qty) {
        return activityDao.getAnnualMeetingParticipants(prizeType, qty);
    }

    @Override
    public List<MoneyTreeFruit> myMoneyTreeFruitList(String activityCode, String userId) {
        activityState(activityCode, null);
        return activityDao.myMoneyTreeFruitList(userId);
    }

    @Override
    public List<MoneyTreePrize> myMoneyTreePrizeList(String activityCode, String userId) {
        activityState(activityCode, null);
        return activityDao.myMoneyTreePrizeList(userId);
    }

    @Override
    public boolean checkAndLockFruit(String activityCode, String fruitId, String userId) {
        activityState(activityCode, null);
        return activityDao.checkAndLockFruit(fruitId, userId);
    }

    @Override
    public int updateFruitYZQ(String fruitId, String prizeId) {
        return activityDao.updateFruitYZQ(fruitId, prizeId);
    }

    @Override
    public int alreadyGetPrizeSum(String prizeId, String userId) {
        return activityDao.alreadyGetPrizeSum(prizeId, userId);
    }

    @Transactional
    @Override
    public LotteryPrizeInfoVO pickFruit(String activityCode, String fruitId, String userId) {
        try {
            //检查该果实是否可抽奖
            boolean checkRes = checkAndLockFruit(activityCode, fruitId, userId);
            if (!checkRes) {
                throw new BusinessException(ResponseCode.ACTIVITY_FRUIT_BE_PICKED);
            }
            //抽奖
            LotteryActivityInfo lotteryActivityInfo = lotteryActivityService.getLotteryActivityInfoNoState(activityCode);
            int activityId = lotteryActivityInfo.getActivityId();

            UserInfo userInfo = userInfoService.getUser(null, null, userId);
            String cellNumber = userInfo == null ? "" : userInfo.getPhone();
            String cellTailNumber = "".equals(cellNumber) ? "" : cellNumber.substring(cellNumber.length() - 4, cellNumber.length());

            //获取中奖奖品信息
            LotteryPrizeInfo drawPrizeInfo = null;
            int flag = 0;

            do {
                //每人限中电影票一次
                drawPrizeInfo = lotteryPrizeService.getLotteryDrawPrize(activityId);
                if (drawPrizeInfo.getPrizeId() == Integer.valueOf(Config.get("activity.flblcj.singlePrizeId"))
                        && alreadyGetPrizeSum(String.valueOf(drawPrizeInfo.getPrizeId()), userId) > 0) {
                    flag++;
                    drawPrizeInfo = null;
                } else {
                    flag = 0;
                }
            } while (flag > 0 && flag <= 5);

            if (drawPrizeInfo == null) {
                throw new BusinessException(ResponseCode.ACTIVITY_FRUIT_PICK_ERROR);
            }
            int prizeId = lotteryPrizeService.addLotteryDrawRecord(activityId, Integer.valueOf(userId), cellTailNumber, drawPrizeInfo.getPrizeId());
            if (prizeId <= 0) {
                throw new BusinessException(ResponseCode.ACTIVITY_FRUIT_PICK_ERROR);
            }
            //减少奖品数量
            lotteryPrizeService.updatePrizeQuantity(activityId, drawPrizeInfo.getPrizeId(), 1);

            //回填分利果中奖结果并修改状态
            int res = updateFruitYZQ(fruitId, String.valueOf(prizeId));
            if (res <= 0) {
                throw new BusinessException(ResponseCode.ACTIVITY_FRUIT_PICK_ERROR);
            }

            LotteryPrizeInfoVO lotteryPrizeInfoVO = new LotteryPrizeInfoVO();
            lotteryPrizeInfoVO.setPrizeId(drawPrizeInfo.getPrizeId());
            lotteryPrizeInfoVO.setPrizeName(drawPrizeInfo.getPrizeName());
            lotteryPrizeInfoVO.setPrizeLogo(drawPrizeInfo.getPrizeLogo());
            lotteryPrizeInfoVO.setPrizeType(drawPrizeInfo.getPrizeType());
            lotteryPrizeInfoVO.setListOrder(drawPrizeInfo.getListOrder());
            lotteryPrizeInfoVO.setPrizeRecordId(prizeId);
            return lotteryPrizeInfoVO;
        } catch (BusinessException e) {
            logger.error(e, e);
            throw e;
        } catch (Exception e) {
            logger.error(e, e);
            throw new BusinessException(ResponseCode.ACTIVITY_FRUIT_PICK_ERROR);
        }
    }

    @Transactional
    @Override
    public void grantActivityCashbackForMoneyTree(BigDecimal amount, String userId, int prizeRecordId) throws Exception {
        //查询用户userId
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("userId", userId);
        UserInfo userInfo = this.userInfoDao.getUserInfo(userMap);

        //发放现金
        String accountTypeWlzh = InterfaceConst.ACCOUNT_TYPE_WLZH;
        String salayKey = Config.get("redpacket.grantAccount");
        FeeType feeType = redpacketDao.getFeeType(FeeCode.CACH_REDPACKET);
        int recordId = iFundsService.transfer(Integer.parseInt(userInfo.getUserId()), amount, feeType, accountTypeWlzh, salayKey);

        //更新lt_lottery_biz
        Map<String, Object> uptateRecordsMap = new HashMap<>();
        uptateRecordsMap.put("prizeRecordId", prizeRecordId);
        uptateRecordsMap.put("prizeType", LtPrizeTypeEnum.XJHB.getCode());
        uptateRecordsMap.put("bizId", recordId);
        insertLotteryBiz(uptateRecordsMap);

//        //发送短信和站内信
//        String content = Config.get("annualMeeting.redpacketWinner.msg");
//        content = content.replace("#{amount}", amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
//        sendSmsAndLetter(userInfo.getPhone(), userInfo.getUserId(), content);
    }

    /**
     * 插入发奖纪录
     *
     * @param map
     * @return
     */
    @Override
    public int insertLotteryBiz(Map map) {
        return activityDao.insertLotteryBiz(map);
    }

    @Override
    public ActivityEntity getMoneyTreeActityDetail() {
        return activityDao.getMoneyTreeActityDetail();
    }

    /**
     * 开始检查并生成分利果
     */
    @Override
    public void startCheckAndCreateFruit() {
        //查找今天投资的用户（邀请用户）
        List<MoneyTreeRegisterCheckEntity> list = activityDao.checkAndGetRegister();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                MoneyTreeRegisterCheckEntity moneyTreeRegisterCheckEntity = list.get(i);
                try {
                    activityExtService.createFruitAndSendMsg(moneyTreeRegisterCheckEntity);
                } catch (Exception e) {
                    logger.error(e, e);
                }
            }
        }
    }

    /**
     * 获得分利果之后发送短信
     * @param userId
     * @param content
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void sendSmsForMoneyTree(String userId, String content){
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("userId", userId);
        UserInfo userInfo = userInfoDao.getUserInfo(userMap);
        if (userInfo != null) {
            sendSms(content, userInfo.getPhone());
        }
    }

    @Override
    public Integer getStatus(Integer userId) {
        return activityDao.getStatus(userId);
    }

    @Override
    public void addActivityUserPhone(int userId, String phone, String activityCode) throws Exception {
        activityDao.addActivityUserPhone(userId,phone,activityCode);
    }

    @Override
    public List<AutoRegist> getAutoRegistList(String registStatus,String cardStatus,Integer limit) {
        return activityDao.getAutoRegistList(registStatus,cardStatus,limit);
    }

    @Override
    public void updateAutoRegist(AutoRegist autoRegist) {
        activityDao.updateAutoRegist(autoRegist);
    }
}
