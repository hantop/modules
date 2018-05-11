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

import java.sql.SQLException;
import java.util.*;

import javax.annotation.Resource;

import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.entity.activity.AnniversaryInvestRecord;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.redpacket.RedPacketActivityVO;
import com.fenlibao.p2p.model.vo.redpacket._RedPacketVO;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import org.springframework.stereotype.Service;

import com.dimeng.p2p.S62.entities.T6232;
import com.fenlibao.p2p.dao.PublicAccessoryDao;
import com.fenlibao.p2p.dao.activity.ActivityDao;
import com.fenlibao.p2p.service.AccessoryInfoService;
import com.fenlibao.p2p.service.activity.ActivityService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName: AccessoryInfoServiceImpl
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-9 下午2:43:34
 */
@Service
public class ActivityServiceImpl implements ActivityService {

	@Resource
	ActivityDao activityDao;
	@Resource
	private RedpacketService redpacketService;

	@Override
	public int insertActivity(String activityCode, String phone, int isNew) {
		return activityDao.insertActivity(activityCode, phone, isNew);
	}

	@Override
	public int validIsRegistActivity(String activityCode, String phone) {
		return activityDao.validRegistActivity(activityCode, phone);
	}

	@Override
	public boolean isActivityTime(String activityCode) {
		Map map=new HashMap();
		map.put("activityCode",activityCode);
		Map<String,Object> resultMap=activityDao.isActivityTime(map);
		if(resultMap==null){
			throw new BusinessException(ResponseCode.ACTIVITY_NOT_ACTIVITY_TIME.getCode(),
					ResponseCode.ACTIVITY_NOT_ACTIVITY_TIME.getMessage());
		}else{
			return true;
		}
	}

	@Override
	public List<AnniversaryInvestRecord> anniversaryInvestRecords(String activityCode) {
		return activityDao.anniversaryInvestRecords();
	}

	@Override
	public Map<String, Object> myAnniversaryInvestInfo(String activityCode,String userId) {
		return activityDao.myAnniversaryInvestInfo(userId);
	}

	@Override
	public List<RedPacketActivityVO> getRedPacketList(Integer userId, String activityCode) {
		List<RedPacketActivityVO> list = activityDao.getRedPacketList(userId, activityCode);
		Date now = new Date();
		for (RedPacketActivityVO vo : list) {
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
	public void receiveRedPacket(int userId, int redPacketId, String activityCode) throws Exception {
		Integer unusedRedPacketId = activityDao.getCurdateUnusedRedPacket(userId, activityCode);
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
		redpacketService.addUserRedpackets(redPacketList,String.valueOf(userId),null,null,null); //不发送短信、站内信
	}

	@Override
	public Integer getStatus(Integer userId) {
		return activityDao.getStatus(userId);
	}

	@Override
	public void addActivityUserPhone(int userId, String phone, String activityCode) throws Exception {
		 activityDao.addActivityUserPhone(userId,phone,activityCode);
	}
}
