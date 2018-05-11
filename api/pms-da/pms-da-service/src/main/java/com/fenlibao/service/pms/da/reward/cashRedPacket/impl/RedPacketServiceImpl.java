package com.fenlibao.service.pms.da.reward.cashRedPacket.impl;

import com.fenlibao.dao.pms.da.bidType.BidTypeMapper;
import com.fenlibao.dao.pms.da.reward.cashRedPacket.RedPacketMapper;
import com.fenlibao.model.pms.da.reward.RedPacket;
import com.fenlibao.model.pms.da.reward.RewardRecord;
import com.fenlibao.model.pms.da.reward.UserCashRedPacket;
import com.fenlibao.service.pms.da.reward.cashRedPacket.RedPacketService;
import com.fenlibao.service.pms.da.reward.cashRedPacket.UserRedpacketsService;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Bogle on 2015/11/26.
 */
@Service
public class RedPacketServiceImpl implements RedPacketService {

	private static final Logger LOG = LoggerFactory.getLogger(RedPacketServiceImpl.class);

	@Autowired
	private BidTypeMapper bidTypeMapper;

	@Autowired
	private RedPacketMapper redPacketMapper;

	@Autowired
	private UserRedpacketsService userRedpacketsService;

	@Override
	public List<RedPacket> findRedPacketPager(RedPacket redPacket, RowBounds bounds) {
		return redPacketMapper.findRedPacketPager(redPacket, bounds);
	}

	@Override
	public RedPacket getRedPacketById(int id) {
		return redPacketMapper.selectByPrimaryKey(id);
	}

	@Override
	public int saveOrUpdateRedPacket(RedPacket redPacket) {
		int count = redPacketMapper.selectRedPacketCountByCode(redPacket);
		if (count > 0) {
			return -1;
		} 
		if (redPacket.getId() > 0) {
			// 当前的返现券对应的原来的标的类型
			List<Integer> oldBidTypeIds = this.bidTypeMapper.getBidTypeIdsByRedPacketId(redPacket.getId());
			// 在还没有发送之前还是允许修改的
			this.updateByPrimaryKeySelective(redPacket);
			// 在更新完返现券之后,需要对对应的中间表进行操作
			for (Integer oldType : oldBidTypeIds) {
				// 之前的对应的标的类型
				if (!redPacket.getBidTypeIds().contains(oldType)) {
					// 不包含原来的,先删除原来的
					this.bidTypeMapper.deleteRedPackageBidType(redPacket.getId(),oldType);
				}
			}
			for (Integer bidTypeId : redPacket.getBidTypeIds()) {
				// 需要执行删除以及新增加的(传进来的不包含原来的===>中间表需要删除;)
				// 原来的标的限制不包含传进来的id====>增加类型限制
				if (!oldBidTypeIds.contains(bidTypeId)) {
					this.bidTypeMapper.insertRedPackageBidType(redPacket.getId(), bidTypeId);
				}
			}
			return 1;
		} else {
			this.insertSelective(redPacket);
			// 批量插入中间表信息(需要在成功添加返现券之后才能够插入)
			for (Integer bidTypeId : redPacket.getBidTypeIds()) {
				this.bidTypeMapper.insertRedPackageBidType(redPacket.getId(), bidTypeId);
			}
			return 1;
		}
		/*if (redPacket.getId() > 0) {
			return updateByPrimaryKeySelective(redPacket);
		} else {
			return insertSelective(redPacket);
		}*/
	}

	private int updateByPrimaryKeySelective(RedPacket redPacket) {
		return redPacketMapper.updateByPrimaryKeySelective(redPacket);
	}

	private int insertSelective(RedPacket redPacket) {
		return redPacketMapper.insertSelective(redPacket);
	}

	@Override
	public int redpacketRemove(LinkedList<Integer> redPackets) {
		// 通过迭代器遍历，如果有已经发放的过的，就剔除掉。
		Iterator<Integer> iterator = redPackets.iterator();
		// 返现券ID
		Integer id;
		while (iterator.hasNext()) {
			id = iterator.next();
			// 判断返现券是否发放过
			if (!this.isGrantRedPacket(id)) {
				iterator.remove();
			}
		}
		// 删除未发放过的返现券
		return redPackets.size() > 0 ? redPacketMapper.redpacketRemove(redPackets) : -1;
	}

	@Override
	public RedPacket findRedPacketByCode(String activityCode, int tradeType) {
		return redPacketMapper.findRedPacketByCode(activityCode, tradeType);
	}

	@Override
	public Boolean isGrantRedPacket(Integer redPacketId) {
		return this.redPacketMapper.isGrantRedPacket(redPacketId) == null;
	}

	@Override
	public String grantCustodyCashRedPacket(RewardRecord rewardRecord, List<UserCashRedPacket> userCashRedPackets) throws Exception {
		String message = null;
		userRedpacketsService.doAlternativeRechargeReward(rewardRecord, userCashRedPackets);
		try {
			// 修改本地信息
			message = userRedpacketsService.modifyLocalDetail(rewardRecord, userCashRedPackets);
		}catch (Exception e) {
			LOG.error("[(现金红包)本地操作异常:]" + e.getMessage(), e);
			return "操作异常!";
		}
		return message;
	}
}
