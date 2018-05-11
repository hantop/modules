/**
 * Copyright © 2015 fenlibao.com. All rights reserved.
 *
 * @Title: AccessoryInfoService.java
 * @Prject: app-client-api-service
 * @Package: com.fenlibao.p2p.service
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-9 下午2:42:58
 * @version: V1.1
 */
package com.fenlibao.p2p.service.activity;

import com.fenlibao.p2p.model.entity.activity.*;
import com.fenlibao.p2p.model.lottery.vo.LotteryPrizeInfoVO;
import com.fenlibao.p2p.model.vo.redpacket.RedPacketOlympicActivityVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: AccessoryInfoService
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-9 下午2:42:58
 */
public interface ActivityService {


    int insertActivity(String activityCode, String phone, int isNew);

    int validIsRegistActivity(String activityCode, String phone);

    /**
     * 获取活动返现券列表
     * （在原有的奥运活动基础上更改为通用的活动红包获取）
     *
     * @param userId
     * @param activityCode
     * @return
     */
    List<RedPacketOlympicActivityVO> getOlympicRedPacketList(Integer userId, String activityCode);

    /**
     * 领取活动返现券
     * （在原有的奥运活动基础上更改为通用的活动红包获取）
     *
     * @param userId
     * @param redPacketId
     * @throws Exception
     */
    void receiveOlympicRedPacket(int userId, int redPacketId, String activityCode) throws Exception;

    /**
     * 8月好友邀请活动
     *
     * @param phone
     * @param eventType
     * @return
     */
    int insertUserActivityEvent(String phone, int eventType);

    /**
     * 获取8月好友邀请活动事件记录
     *
     * @param phone
     * @param eventType
     * @return
     */
    Integer getUserActivityEvent(String phone, int eventType);

    /**
     * 获取8月好友邀请活动事件记录
     *
     * @param phone
     * @return
     */
    List<VirusSpreadFreinds> getAugustInvitationFriends(String phone);

    /**
     * 9月名创会员首投活动-检验手机号码是否可以参加活动
     *
     * @param phone
     * @return
     */
    Map<String, Object> minisoPhoneCheck(String phone, String activityCode);

    /**
     * 9月名创会员首投活动-获取发放返现用户列表
     *
     * @return
     */
    List<McMemberForCashback> getMcMembersForCashback();

    /**
     * 9月名创会员首投活动-送话费
     *
     * @return
     */
    List<McMemberForBillSms> getMcMemberForBillSms();

    /**
     * 9月名创会员首投活动-发放记录
     *
     * @return
     */
    int insertMcMemberSeptemberRecord(McMemberForCashback mcMember);

    /**
     * 9月名创会员首投活动-送话费
     *
     * @return
     */
    int insertMcMemberSeptemberSms(McMemberForBillSms mcMember);

    /**
     * 9月名创会员首投活动-发短信
     *
     * @return
     */
    void sendSms(String content, String phone);

    /**
     * 9月名创会员首投活动-更新发放记录
     *
     * @return
     */
    int uptateMcMemberSeptemberRecord(McMemberForCashback mcMember, String result, String failLog);

    /**
     * 9月名创会员首投活动-发放现金
     */
    void grantMcMembersCashback(McMemberForCashback mcMember) throws Exception;

    /**
     * 判断活动是否结束
     *
     * @param activityCode//活動編碼
     * @return
     */
    boolean isActivityTime(String activityCode);

    /**
     * 判断活动状态
     *
     * @param activityCode
     * @return
     */
    int activityState(String activityCode, int mode);

    /**
     * 土豪pk榜接口
     *
     * @return
     */
    List<AnniversaryInvestRecord> anniversaryInvestRecords(String activityCode);

    /**
     * 我的冲榜信息
     *
     * @return
     */
    Map<String, Object> myAnniversaryInvestInfo(String activityCode, String userId);

    /**
     * 地铁报活动-获取发放返现用户列表
     *
     * @return
     */
    List<DtbForCashBack> getDtbForCashBack(String activityCode);

    /**
     * 插入发放记录
     *
     * @return
     */
    int insertActivityCashbackRecord(DtbForCashBack dtb);

    /**
     * 插入发放记录
     *
     * @return
     */
    int uptateActivityCashbackRecord(Map map);

    /**
     * 发放现金
     */
    void grantActivityCashback(Map map) throws Exception;

    /**
     * 删除失败的发放记录
     *
     * @return
     */
    int deleteActivityCashbackRecord(Map map);

    /**
     * 获取用户参加某个活动事件记录
     *
     * @param phone
     * @param code
     * @return
     */
    void saveUserActivityRecord(String phone, String code);

    /**
     * 年会--获取奖品列表
     */
    List<AnnualMeetingPrize> getAnnualMeetingPrize(String type);

    /**
     * 年会--获取奖品
     */
    AnnualMeetingPrize getAnnualMeetingPrize(String type, String prizeCode);

    /**
     * 年会--插入指定中奖名单
     */
    int saveDesignated(String phone, String prizeCode, String name);

    /**
     * 年会抽中红包用户
     *
     * @return
     */
    List<AnnualMettingParticipant> drawRedPacket(int normalWinnersNum);

    /**
     * 年会抽红包--查询指定中间名单
     */
    List<AnnualMeetingDesignated> getDesignatedistForRedPacket(Integer qty);

    /**
     * 发放奖金和短信
     *
     * @param amr
     * @throws Exception
     */
    void grantActivityCashbackForAnnualMeetingRedPacket(AnnualMettingRecord amr) throws Exception;

    /**
     * 更新记录
     *
     * @param map
     * @return
     */
    int uptateAnnualMettingRecords(Map map);

    /**
     * 年会--获取奖品列表
     */
    List<AnnualMeetingPrize> getAnnualMeetingPrize();

    /**
     * 年会--获取指定iphone中奖名单
     */
    List<AnnualMettingRecord> getIphoneDesignatedist(Integer prizeType, Integer qty);

    /**
     * 年会--查询奖品信息
     */
    AnnualMeetingPrize getAnnualMeetingPrizeInfo(String code);


    /**
     * 年会--随机抽取红米手机
     */
    List<AnnualMettingRecord> getDrawHM(int QTY);


    /**
     * 年会--保存中奖结果
     */
    int saveAnnualMeetingHmRecord(List<AnnualMettingRecord> recordList);

    /**
     * 年会--抽取苹果手机
     */
    AnnualMettingRecord getDrawIphone(int QTY);

    /**
     * 更新奖品数量,插入中奖纪录
     *
     * @param amr
     * @throws Throwable
     */
    void saveAnnualMettingRecordsAndUpdatePrizeNum(AnnualMettingRecord amr) throws Throwable;

    /**
     * 模拟抽奖
     *
     * @param prizeType        奖品类型
     * @param normalWinnersNum 预计抽出奖品数量
     */
    List<AnnualMeetingPrize> lottery(List<AnnualMeetingPrize> effectivePrizeInfos, String prizeType, int normalWinnersNum);

    /**
     * 获取中奖纪录
     *
     * @param prizeType
     * @return
     */
    List<AnnualMettingRecord> getAnnualMeetingRecords(Integer prizeType);

    /**
     * 获取中奖弹幕
     *
     * @param prizeType
     * @return
     */
    List<AnnualMettingParticipant> getAnnualMeetingParticipants(Integer prizeType, Integer qty);

    /**
     * 获取活动状态
     * mode： =null包含3种状态 !=null（不包含结束状态）
     */
    int activityState(String activityCode, Integer mode);

    /**
     * 获取我的未掉落的果实列表
     *
     * @return
     */
    List<MoneyTreeFruit> myMoneyTreeFruitList(String activityCode, String userId);

    /**
     * 获取获奖列表
     *
     * @return
     */
    List<MoneyTreePrize> myMoneyTreePrizeList(String activityCode, String userId);

    /**
     * 检查分利果抽奖资格，并锁记录
     *
     * @param fruitId
     * @return
     */
    boolean checkAndLockFruit(String activityCode, String fruitId, String userId);

    /**
     * 更新分利果状态，并回填中奖纪录id
     *
     * @param fruitId
     * @param prizeId
     * @return
     */
    int updateFruitYZQ(String fruitId, String prizeId);

    /**
     * 点击果实抽奖
     *
     * @param activityCode
     * @param fruitId
     * @param userId
     * @return
     */
    LotteryPrizeInfoVO pickFruit(String activityCode, String fruitId, String userId);

    /**
     * 获取已经获得的该奖品的数量
     *
     * @param prizeId
     * @return
     */
    int alreadyGetPrizeSum(String prizeId, String userId);

    /**
     * 发放奖金
     */
    void grantActivityCashbackForMoneyTree(BigDecimal amount, String userId, int prizeRecordId) throws Exception;

    /**
     * 插入发奖纪录
     */
    int insertLotteryBiz(Map map);

    /**
     * 获取摇钱树活动信息
     * @return
     */
    ActivityEntity getMoneyTreeActityDetail();

    /**
     * 开始检查并生成分利果
     */
    void startCheckAndCreateFruit();

    /**
     * 获得分利果之后发送短信
     * @param userId
     * @param content
     */
    void sendSmsForMoneyTree(String userId, String content);

    /**
     * 0:未登记 1：已登记
     * 获取存管开户手机登记状态
     */
    Integer getStatus(Integer userId);

    /**
     * 登记活动手机号
     * @param userId
     * @param phone
     * @param activityCode
     * @throws Exception
     */
    void addActivityUserPhone(int userId, String phone, String activityCode) throws Exception;

    /**
     * 获取需要自动注册的用户
     * @return
     */
    List<AutoRegist> getAutoRegistList(String registStatus,String cardStatus,Integer limit);

    /**
     * 更新批量自动注册表单数据
     * @param autoRegist
     */
    void updateAutoRegist(AutoRegist autoRegist);
}
