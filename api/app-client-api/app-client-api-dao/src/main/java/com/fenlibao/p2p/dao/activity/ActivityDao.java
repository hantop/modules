package com.fenlibao.p2p.dao.activity;


import com.fenlibao.p2p.model.entity.activity.*;
import com.fenlibao.p2p.model.vo.redpacket.RedPacketOlympicActivityVO;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    int insertActivity(String activityCode, String phone, int isNew);

	int validRegistActivity(String activityCode, String phone);

    /**
     * 获取8月奥运红包列表
     * @param userId
     * @return
     */
    List<RedPacketOlympicActivityVO> getOlympicRedPacketList(Integer userId, String activityCode);

    /**
     * 获取用户当天未使用红包ID
     * @param userId
     * @return
     */
    Integer getCurdateUnusedOlympicRedPacket(int userId, String activityCode);
    
    /**
     * 8月好友邀请活动
     * @param phone
     * @param eventType
     * @return
     */
    int insertUserActivityEvent(String phone,int eventType);

    /**
     * 获取8月好友邀请活动事件记录
     * @param phone
     * @param eventType
     * @return
     */
    Integer getUserActivityEvent(String phone,int eventType);
    
    /**
     * 8月好友邀请活动用户的邀请好友
     * @param phone
     * @return
     */
    List<VirusSpreadFreinds>  getAugustInvitationFriends(String phone);

    /**
     * 9月名创会员首投活动-检验手机号码是否可以参加活动
     * @param phone
     * @return
     */
    Map<String,Object> minisoPhoneCheck(String phone);

    /**
     * 9月名创会员首投活动-获取发放返现用户列表
     * @return
     */
    List<McMemberForCashback> getMcMembersForCashback();

    /**
     * 9月名创会员首投活动-送话费
     * @return
     */
    List<McMemberForBillSms> getMcMemberForBillSms();
    /**
     * 9月名创会员首投活动-发放记录
     * @return
     */
    int insertMcMemberSeptemberRecord(Map map);
    /**
     * 9月名创会员首投活动-送话费
     * @return
     */
    int insertMcMemberSeptemberSms(Map mcMember);
    /**
     * 9月名创会员首投活动-更新发放记录
     * @return
     */
    int uptateMcMemberSeptemberRecord(Map map);

    /**
     * 判断活动是否结束
     */
    Map<String,Object>  isActivityTime(Map map);

    /**
     * 判断活动是否结束
     */
    Integer  activityState(Map map);

    /**
     * 土豪pk榜接口
     * @return
     */
    List<AnniversaryInvestRecord>  anniversaryInvestRecords();

    /**
     * 我的冲榜信息
     */
    Map<String,Object> myAnniversaryInvestInfo(String userId);

    /**
     * 地铁报活动-获取发放返现用户列表
     */
    List<DtbForCashBack> getDtbForCashBack(String activityCode);

    /**
     * 插入发放记录
     */
    int insertActivityCashbackRecord(Map map);

    /**
     * 更新
     */
    int uptateActivityCashbackRecord(Map map);

    /**
     * 删除失败的发放记录
     * @return
     */
    int deleteActivityCashbackRecord(Map map);

    /**
     * 获取用户参加某个活动事件记录
     * @return
     */
    Integer getUserActivityRecord(String phone,String code);

    /**
     * 年会--获取奖品列表
     */
    List<AnnualMeetingPrize> getAnnualMeetingPrize(String type);
    /**
     * 年会--获取奖品
     */
    AnnualMeetingPrize getAnnualMeetingPrize(String type, String prizeCode);
    /**
     * 插入用户参加某个活动事件记录
     */
    int insertUserActivityRecord(Map map);

    /**
     * 年会--插入指定中奖名单
     */
    int saveDesignated(Map map);


    /**
     * 年会--抽红包
     */
    List<AnnualMettingParticipant> drawRedPacket(Integer normalWinnersNum);

    /**
     * 更新奖品数量
     * @return
     */
    int updatePrizeNum(Map<String,Object> ampMap);

    /**
     * 插入中奖纪录
     * @return
     */
    int saveAnnualMettingRecords(Map<String,Object> amrMap);


    /**
     * 年会抽红包--查询指定中间名单
     */
    List<AnnualMeetingDesignated>  getDesignatedistForRedPacket(Integer qty);

    /**
     * 更新记录
     * @param map
     * @return
     */
    int uptateAnnualMettingRecords(Map map);

    /**
     * 年会--查询指定iphone中奖名单
     */
    List<AnnualMettingRecord>  getIphoneDesignatedist(Integer prizeType,Integer qty);

    /**
     * 年会--获取奖品列表
     */
    List<AnnualMeetingPrize> getAnnualMeetingPrize();
    /**
     * 年会--获取某个奖品信息
     */
    AnnualMeetingPrize  getAnnualMeetingPrizeInfo(Map map);

    /**
     * 年会--随机抽取红米手机
     */
    List<AnnualMettingRecord>  getRamdomHM(Map map);
    /**
     * 年会--保存中奖结果
     */
    int saveAnnualMettingRecord(Map map);
    /**
     * 年会--更新奖品信息
     */
    int updateAnnualMeetingPrizeInfo(Map map);
    /**
     * 年会--计算iPhone用户中奖概率实体类
     */
    List<AnnualMettingDrawRateRecord> getAnnualMettingDrawRateRecord(Map map);

    /**
     * 获取中奖纪录
     * @param prizeType
     * @return
     */
    List<AnnualMettingRecord> getAnnualMeetingRecords(Integer prizeType);

    /**
     * 获取弹幕
     * @param prizeType
     * @return
     */
    List<AnnualMettingParticipant> getAnnualMeetingParticipants(Integer prizeType, Integer qty);

    /**
     * 获取未掉落的果实列表
     */
    List<MoneyTreeFruit> myMoneyTreeFruitList(String userId);

    /**
     * 获取获奖列表
     */
    List<MoneyTreePrize> myMoneyTreePrizeList(String userId);

    /**
     * 检查分利果抽奖资格
     */
    boolean checkAndLockFruit(String fruitId, String userId);

    /**
     * 更新分利果状态，并回填中奖纪录id
     */
    int updateFruitYZQ(String fruitId, String prizeId);

    /**
     * 获取已经获得的该奖品的数量
     * @param prizeId
     * @return
     */
    int alreadyGetPrizeSum(String prizeId, String userId);

    /**
     * 插入发奖纪录
     * @param map
     * @return
     */
    int insertLotteryBiz(Map map);

    /**
     * 获取活动信息
     */
    ActivityEntity getMoneyTreeActityDetail();

    /**
     * 检查并获取满足条件的注册投资用户
     * @return
     */
    List<MoneyTreeRegisterCheckEntity> checkAndGetRegister();

    /**
     * 保存生成的分利果
     * @param moneyTreeFruitRecord
     */
    int insertMoneyTreeFruit(MoneyTreeFruitRecord moneyTreeFruitRecord);

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
