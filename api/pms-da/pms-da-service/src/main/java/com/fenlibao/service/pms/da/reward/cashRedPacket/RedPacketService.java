package com.fenlibao.service.pms.da.reward.cashRedPacket;

import com.fenlibao.model.pms.da.reward.RedPacket;
import com.fenlibao.model.pms.da.reward.RewardRecord;
import com.fenlibao.model.pms.da.reward.UserCashRedPacket;
import org.apache.ibatis.session.RowBounds;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by Bogle on 2015/11/26.
 */
public interface RedPacketService {

    /**
     * 分页获取返现券或红包信息
     *
     * @param redPacket
     * @param bounds
     * @return
     */
    List<RedPacket> findRedPacketPager(RedPacket redPacket, RowBounds bounds);

    /**
     * 根据id获取券或红包
     *
     * @param id
     * @return
     */
    RedPacket getRedPacketById(int id);

    /**
     * 新增或修改
     *
     * @param redPacket
     * @return
     */
    int saveOrUpdateRedPacket(RedPacket redPacket);

    /**
     * 批量删除
     *
     * @param redPackets
     * @return
     */
    int redpacketRemove(LinkedList<Integer> redPackets);

    /**
     * 根据code查询
     * @param activityCode
     * @param tradeType
     * @return
     */
    RedPacket findRedPacketByCode(String activityCode, int tradeType);

    /**
     * 判断返现券是否已经发送
     * @param redPacketId
     * @return
     */
	Boolean isGrantRedPacket(Integer redPacketId);

    /**
     * 发放现金红包(存管版)
     * @param rewardRecord
     * @param userCashRedPackets
     * @return
     * @throws Exception
     */
    String grantCustodyCashRedPacket(RewardRecord rewardRecord, List<UserCashRedPacket> userCashRedPackets) throws Exception;
}
