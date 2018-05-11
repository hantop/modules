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

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.dimeng.p2p.S62.entities.T6232;
import com.fenlibao.p2p.model.entity.activity.AnniversaryInvestRecord;
import com.fenlibao.p2p.model.vo.redpacket.RedPacketActivityVO;

/** 
 * @ClassName: AccessoryInfoService 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-9 下午2:42:58  
 */
public interface ActivityService {
	

    public int insertActivity(String activityCode, String phone, int isNew);

	public int validIsRegistActivity(String activityCode, String phone);

    /**
     * 判断活动是否结束
     * @param activityCode//活動編碼
     * @return
     */
    boolean isActivityTime(String activityCode);

    /**
     * 土豪pk榜接口
     * @return
     */
    List<AnniversaryInvestRecord>  anniversaryInvestRecords(String activityCode);

    /**
     * 我的冲榜信息
     * @return
     */
    Map<String,Object> myAnniversaryInvestInfo(String activityCode, String userId);

    /**
     * 获取活动返现券列表
     * @param userId
     * @param activityCode
     * @return
     */
    List<RedPacketActivityVO> getRedPacketList(Integer userId, String activityCode);

    /**
     * 领取活动返现券
     * @param userId
     * @param redPacketId
     * @throws Exception
     */
    void receiveRedPacket(int userId, int redPacketId, String activityCode) throws Exception;

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
