package com.fenlibao.dao.pms.da.reward.cashRedPacket;

import com.fenlibao.model.pms.da.reward.RedPacket;
import com.fenlibao.model.pms.da.reward.UserRedpackets;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface RedPacketMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RedPacket record);

    Integer insertSelective(RedPacket record);//修改,需要返回插入数据后的改返现券的id

    RedPacket selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RedPacket record);

    int updateByPrimaryKey(RedPacket record);

    /**
     * 分页获取返现券或红包信息
     *
     * @param redPacket
     * @param bounds
     * @return
     */
    List<RedPacket> findRedPacketPager(RedPacket redPacket, RowBounds bounds);

    /**
     * 根据code查询是否存在该返现券
     *
     * @param code
     * @param id
     * @param redType 红包活动类型,1:注册返金红包 2:注册现金红包;2:生日;3:充值;4:投资倍数;5:投资额度
     * @return
     */
    int selectRedPacketCountByCode(RedPacket redPacket);

    /**
     * 批量删除
     *
     * @param redPackets
     * @return
     */
    int redpacketRemove(List<Integer> redPackets);

    /**
     * 根据code查询
     * @param activityCode
     * @param tradeType
     * @return
     */
    RedPacket findRedPacketByCode(String activityCode, int tradeType);

    List<UserRedpackets> getRedPacketActivateCode(UserRedpackets item);

    /**
     * 
     * @param redPacketId
     * @return
     */
	Integer isGrantRedPacket(Integer redPacketId);

}