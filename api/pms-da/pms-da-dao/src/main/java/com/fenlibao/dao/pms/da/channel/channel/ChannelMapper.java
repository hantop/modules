package com.fenlibao.dao.pms.da.channel.channel;


import com.fenlibao.model.pms.da.channel.vo.ChannelVO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface ChannelMapper {

    /**
     * 获取渠道
     *
     * @param paramMap
     * @param bounds
     * @return
     */
    List<ChannelVO> getChannels(Map<String, Object> paramMap, RowBounds bounds);

    /**
     * 获取所有渠道
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
     * @param channelDTO
     * @param bounds
     * @return
     */
    List<ChannelVO> findParentPager(ChannelVO channelDTO, RowBounds bounds);

    /**
     * 批量删除
     *
     * @param channelVOs
     * @return
     */
    int delete(List<Integer> channelVOs);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    ChannelVO getChannelById(int id);

    /**
     * 修改渠道信息
     *
     * @param channel
     * @return
     */
    int updateByPrimaryKeySelective(ChannelVO channel);


    /**
     * 根据名称查询获取条数
     *
     * @param name
     * @return
     */
    int getChannelCountByName(String name, Integer parentId,Integer id);

    /**
     * 根据code查询获取条数
     *
     * @param code
     * @return
     */
    int getChannelCountByCode(String code);
}
