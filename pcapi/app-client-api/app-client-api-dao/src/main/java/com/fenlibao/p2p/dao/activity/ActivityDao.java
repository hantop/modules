package com.fenlibao.p2p.dao.activity;


import com.fenlibao.p2p.model.entity.activity.AnniversaryInvestRecord;
import com.fenlibao.p2p.model.vo.redpacket.RedPacketActivityVO;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    public int insertActivity(String activityCode, String phone, int isNew);

	int validRegistActivity(String activityCode, String phone);

    /**
     * 判断活动是否结束
     */
    Map<String,Object> isActivityTime(Map map);

    /**
     * 土豪pk榜接口
     * @return
     */
    List<AnniversaryInvestRecord> anniversaryInvestRecords();

    /**
     * 我的冲榜信息
     * @return
     */
    Map<String,Object> myAnniversaryInvestInfo(String userId);

    /**
     * 获取活动返现券列表
     * @param userId
     * @param activityCode
     * @return
     */
    List<RedPacketActivityVO> getRedPacketList(Integer userId, String activityCode);

    /**
     * 获取用户当天未使用红包ID
     * @param userId
     * @return
     */
    Integer getCurdateUnusedRedPacket(int userId, String activityCode);

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
}
