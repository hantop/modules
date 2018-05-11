package com.fenlibao.service.pms.da.channel.channel;


import com.fenlibao.model.pms.da.channel.vo.ChannelVO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 渠道service
 * Created by chenzhixuan on 2015/9/24.
 */
public interface ChannelService {

    /**
     * 获取所有一级渠道
     *
     * @return
     */
    List<ChannelVO> getParentChannels();

    /**
     * 根据一级渠道ID获取二级渠道
     *
     * @param parentId
     * @return
     */
    List<ChannelVO> getChannelsByParent(int parentId);

    /**
     * 获取渠道
     *
     * @param parentId
     * @param channelId
     * @param bounds
     * @return
     */
    List<ChannelVO> getChannels(String parentId, String channelId, RowBounds bounds);

    /**
     * 根据渠道code获取渠道VO
     *
     * @param channelCode
     * @return
     */
    ChannelVO getChannelByCode(String channelCode);

    /**
     * 获取渠道
     *
     * @param paramMap
     * @return
     */
    ChannelVO getChannel(Map<String, Object> paramMap);

    /**
     * 新增用户来源
     *
     * @param paramMap
     * @return
     */
    int addUserOrigin(Map<String, Object> paramMap);

    /**
     * 添加
     * @param channel
     * @return
     */
    int insertSelective(ChannelVO channel);

    /**
     * 分页获取2级渠道信息
     *
     * @param channelDTO
     * @param bounds
     * @return
     */
    List<ChannelVO> findChildPager(ChannelVO channelDTO, RowBounds bounds);

    /**
     * 分页获取1级渠道信息
     *
     * @param channel
     * @param bounds
     * @return
     */
    List<ChannelVO> findParentPager(ChannelVO channel, RowBounds bounds);

    /**
     * 删除付节点，在删除节点之前检查是否有子节点，如果有子节点不能删除
     * @param channelVOs
     * @return
     */
    int deleteParent(List<Integer> channelVOs);

    /**
     * 批量子节点删除
     *
     * @param channelVOs
     * @return
     */
    int deleteChild(List<Integer> channelVOs);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    ChannelVO getChannelById(int id);

    /**
     * 修改渠道
     *
     * @param channel
     * @return
     */
    int updateByPrimaryKeySelective(ChannelVO channel);


}
