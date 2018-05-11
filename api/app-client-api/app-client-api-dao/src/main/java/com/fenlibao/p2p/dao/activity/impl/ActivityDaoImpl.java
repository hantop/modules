package com.fenlibao.p2p.dao.activity.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fenlibao.p2p.model.entity.activity.*;
import com.fenlibao.p2p.model.vo.redpacket.RedPacketOlympicActivityVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.activity.ActivityDao;

@Repository
public class ActivityDaoImpl implements ActivityDao{
	
	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "ActivityMapper.";
	
	@Override
    public int insertActivity(String activityCode, String phone, int isNew){
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("activityCode", activityCode);
    	map.put("phone", phone);
    	map.put("joinTime", new Date());
    	if(isNew == 0){
    		map.put("registType", 1);
    	}else{
    		map.put("registType", 3);
    	}
    	return this.sqlSession.insert(MAPPER+"insertActivity", map);
    }
    
	@Override
    public int validRegistActivity(String activityCode, String phone){
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("activityCode", activityCode);
    	map.put("phone", phone);
    	List list = this.sqlSession.selectList(MAPPER+"validRegistActivity", map);
    	if(list!=null && list.size() >0){
    		return (int) list.get(0);
    	}
    	return 0;
    }

	@Override
	public List<RedPacketOlympicActivityVO> getOlympicRedPacketList(Integer userId, String activityCode) {
		Map<String, Object> param = new HashMap<>(1);
		param.put("userId", userId);
		param.put("activityCode", activityCode);
		return sqlSession.selectList(MAPPER + "getOlympicRedPacketList", param);
	}

	@Override
	public Integer getCurdateUnusedOlympicRedPacket(int userId, String activityCode) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("activityCode", activityCode);
		return sqlSession.selectOne(MAPPER + "getCurdateUnusedOlympicRedPacket", params);
	}

	@Override
	public int insertUserActivityEvent(String phone, int eventType) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("phone", phone);
    	map.put("eventType", eventType);
    	return this.sqlSession.insert(MAPPER+"insertUserActivityEvent", map);
	}

	@Override
	public Integer getUserActivityEvent(String phone,int eventType) {
		Map<String,Object> map = new HashMap<String,Object>();
    	map.put("phone", phone);
    	map.put("eventType", eventType);
		return sqlSession.selectOne(MAPPER + "getUserActivityEvent", map);
	}

	@Override
	public List<VirusSpreadFreinds> getAugustInvitationFriends(
			String phone) {
		Map<String, String> param = new HashMap<>(1);
		param.put("phone", phone);
		return sqlSession.selectList(MAPPER + "getAugustInvitationFriends", param);
	}

	@Override
	public Map<String,Object> minisoPhoneCheck(String phone) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("phone", phone);
		return sqlSession.selectOne(MAPPER + "minisoPhoneCheck", map);
	}

	@Override
	public List<McMemberForCashback> getMcMembersForCashback() {
		return sqlSession.selectList(MAPPER + "getMcMembersForCashback");
	}

	@Override
	public List<McMemberForBillSms> getMcMemberForBillSms() {
		return sqlSession.selectList(MAPPER + "getMcMemberForBillSms");
	}

	@Override
	public int insertMcMemberSeptemberRecord(Map map) {
		return this.sqlSession.insert(MAPPER+"insertActivityMcmember", map);
	}

	@Override
	public int insertMcMemberSeptemberSms(Map map) {
		return this.sqlSession.insert(MAPPER+"insertMcMemberSeptemberSms", map);
	}

	@Override
	public int uptateMcMemberSeptemberRecord(Map map) {
		return sqlSession.update(MAPPER + "uptateMcMemberSeptemberRecord", map);
	}

	@Override
	public Map<String,Object>  isActivityTime(Map map) {
		return sqlSession.selectOne(MAPPER + "isActivityTime", map);
	}

	@Override
	public List<AnniversaryInvestRecord> anniversaryInvestRecords() {
		return sqlSession.selectList(MAPPER + "anniversaryInvestRecords");
	}

	@Override
	public Map<String, Object> myAnniversaryInvestInfo(String userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		return sqlSession.selectOne(MAPPER + "myAnniversaryInvestInfo", map);
	}

	@Override
	public Integer activityState(Map map) {
		return sqlSession.selectOne(MAPPER + "getActivityState", map);
	}

	@Override
	public List<DtbForCashBack> getDtbForCashBack(String activityCode) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("activityCode", activityCode);
		return sqlSession.selectList(MAPPER + "getDtbForCashBack", map);
	}

	@Override
	public int insertActivityCashbackRecord(Map map) {
		return this.sqlSession.insert(MAPPER+"insertActivityCashbackRecord", map);
	}

	@Override
	public int uptateActivityCashbackRecord(Map map) {
		return sqlSession.update(MAPPER + "uptateActivityCashbackRecord", map);
	}

	@Override
	public int deleteActivityCashbackRecord(Map map) {
		return sqlSession.delete(MAPPER + "deleteActivityCashbackRecord", map);
	}


	@Override
	public Integer getUserActivityRecord(String phone, String code) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("phone", phone);
		map.put("code", code);
		return sqlSession.selectOne(MAPPER + "getUserActivityRecord", map);
	}

	@Override
	public List<AnnualMeetingPrize> getAnnualMeetingPrize(String type) {
		Map<String,Object> map = new HashMap<String,Object>();
		if(type != null && !"".equals(type)){
			map.put("type", type);
		}
		return sqlSession.selectList(MAPPER + "getAnnualMeetingPrize", map);
	}

	@Override
	public AnnualMeetingPrize getAnnualMeetingPrize(String type, String prizeCode){
		Map<String,Object> map = new HashMap<String,Object>();
		if(type != null && !"".equals(type)){
			map.put("type", type);
		}
		if(prizeCode != null && !"".equals(prizeCode)){
			map.put("prizeCode", prizeCode);
		}
		return sqlSession.selectOne(MAPPER + "getAnnualMeetingPrizeSingle", map);
	}

	@Override
	public int insertUserActivityRecord(Map map) {
		return this.sqlSession.insert(MAPPER+"insertUserActivityRecord", map);
	}

	@Override
	public int saveDesignated(Map map) {
		return this.sqlSession.insert(MAPPER+"saveDesignated", map);
	}

	@Override
	public List<AnnualMettingRecord>  getIphoneDesignatedist(Integer prizeType,Integer qty) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("type", prizeType);
		map.put("qty", qty);
		return sqlSession.selectList(MAPPER + "getIphoneDesignatedist", map);
	}

	@Override
	public List<AnnualMettingParticipant>  drawRedPacket(Integer normalWinnersNum) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("normalWinnersNum", normalWinnersNum);
		return sqlSession.selectList(MAPPER + "drawRedPacket", map);
	}

	@Override
	public int updatePrizeNum(Map<String,Object> ampMap){
		return sqlSession.update(MAPPER + "updatePrizeNum", ampMap);
	}

	/**
	 * 插入中奖纪录
	 * @return
	 */
	@Override
	public int saveAnnualMettingRecords(Map<String,Object> amrMap){
		return this.sqlSession.insert(MAPPER+"saveAnnualMettingRecords", amrMap);
	}

	@Override
	public List<AnnualMeetingDesignated>  getDesignatedistForRedPacket(Integer qty) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("qty", qty);
		return sqlSession.selectList(MAPPER + "getDesignatedistForRedPacket", map);
	}

	/**
	 * 更新记录
	 * @param map
	 * @return
	 */
	@Override
	public int uptateAnnualMettingRecords(Map map){
		return sqlSession.update(MAPPER + "uptateAnnualMettingRecords", map);
	}

	@Override
	public List<AnnualMeetingPrize> getAnnualMeetingPrize() {
		return sqlSession.selectList(MAPPER + "getAnnualMeetingPrize");
	}


	@Override
	public AnnualMeetingPrize getAnnualMeetingPrizeInfo(Map map) {
		return sqlSession.selectOne(MAPPER + "getAnnualMeetingPrizeInfo", map);
	}

	@Override
	public List<AnnualMettingRecord> getRamdomHM(Map map) {
		return sqlSession.selectList(MAPPER + "getRamdomHM", map);
	}

	@Override
	public int saveAnnualMettingRecord(Map map) {
		return this.sqlSession.insert(MAPPER+"saveAnnualMettingRecord", map);
	}

	@Override
	public int updateAnnualMeetingPrizeInfo(Map map) {
		return sqlSession.update(MAPPER + "updateAnnualMeetingPrizeInfo", map);
	}

	@Override
	public List<AnnualMettingDrawRateRecord> getAnnualMettingDrawRateRecord(Map map) {
		return sqlSession.selectList(MAPPER + "getAnnualMettingDrawRateRecord", map);
	}

	@Override
	public List<AnnualMettingRecord>  getAnnualMeetingRecords(Integer prizeType) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("type", prizeType);
		return sqlSession.selectList(MAPPER + "getAnnualMeetingRecords", map);
	}

	@Override
	public List<AnnualMettingParticipant> getAnnualMeetingParticipants(Integer prizeType, Integer qty){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("type", prizeType);
		map.put("qty", qty);
		return sqlSession.selectList(MAPPER + "getAnnualMeetingParticipants", map);
	}

	@Override
	public List<MoneyTreeFruit> myMoneyTreeFruitList(String userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		return sqlSession.selectList(MAPPER + "myMoneyTreeFruitList", map);
	}

	@Override
	public List<MoneyTreePrize> myMoneyTreePrizeList(String userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		return sqlSession.selectList(MAPPER + "myMoneyTreePrizeList", map);
	}

	@Override
	public boolean checkAndLockFruit(String fruitId, String userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("fruitId", fruitId);
		map.put("userId", userId);
		return sqlSession.selectOne(MAPPER + "checkAndLockFruit", map);
	}

	@Override
	public int updateFruitYZQ(String fruitId, String prizeId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("fruitId", fruitId);
		map.put("prizeId", prizeId);
		return sqlSession.update(MAPPER + "updateFruitYZQ", map);
	}

	@Override
	public int alreadyGetPrizeSum(String prizeId, String userId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("prizeId", prizeId);
		map.put("userId", userId);
		return sqlSession.selectOne(MAPPER + "alreadyGetPrizeSum", map);
	}

	/**
	 * 插入发奖纪录
	 * @param map
	 * @return
	 */
	@Override
	public int insertLotteryBiz(Map map){
		return sqlSession.insert(MAPPER + "insertLotteryBiz", map);
	}

	@Override
	public ActivityEntity getMoneyTreeActityDetail() {
		Map<String,Object> map = new HashMap<String,Object>();
		return sqlSession.selectOne(MAPPER + "getMoneyTreeActityDetail", map);
	}

	/**
	 * 检查并获取满足条件的注册投资用户
	 * @return
	 */
	@Override
	public List<MoneyTreeRegisterCheckEntity> checkAndGetRegister(){
		return sqlSession.selectList(MAPPER + "checkAndGetRegister");
	}

	/**
	 * 保存生成的分利果
	 * @param moneyTreeFruitRecord
	 */
	public int insertMoneyTreeFruit(MoneyTreeFruitRecord moneyTreeFruitRecord){
		return sqlSession.insert(MAPPER + "insertMoneyTreeFruit", moneyTreeFruitRecord);
	}

	@Override
	public Integer getStatus(Integer userId) {
		Map<String,Object> map = new HashMap<>();
		map.put("userId",userId);

		return sqlSession.selectOne(MAPPER + "getPhoneStatus", map);
	}

	@Override
	public void addActivityUserPhone(int userId, String phone, String activityCode) throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("userId",userId);
		map.put("phone",phone);
		map.put("code",activityCode);
		sqlSession.insert(MAPPER + "addActivityUserPhone", map);
	}

	@Override
	public List<AutoRegist> getAutoRegistList(String registStatus,String cardStatus,Integer limit) {
		Map<String,Object> map = new HashMap<>();
		map.put("registStatus",registStatus);
		map.put("cardStatus",cardStatus);
		map.put("limit",limit);
		return sqlSession.selectList(MAPPER + "getAutoRegistList",map);
	}

	@Override
	public void updateAutoRegist(AutoRegist autoRegist) {
		Map<String,Object> map = new HashMap<>();
		map.put("id",autoRegist.getId());
		if(autoRegist.getRegistStatus()!=0) {
			map.put("registStatus", autoRegist.getRegistStatus());
		}
		if(autoRegist.getCardStatus()!=0) {
			map.put("cardStatus", autoRegist.getCardStatus());
		}
		if(autoRegist.getUserId()!=0){
			map.put("userId",autoRegist.getUserId());
		}
		map.put("registLog",autoRegist.getRegistLog());
		map.put("cardLog",autoRegist.getCardLog());
		map.put("phone",autoRegist.getMobile());
		sqlSession.update(MAPPER + "updateAutoRegist", map);
	}
}

