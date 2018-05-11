package com.fenlibao.service.pms.da.channel.channel.impl;

import com.fenlibao.dao.pms.da.channel.channel.ChannelMapper;
import com.fenlibao.model.pms.da.channel.vo.ChannelVO;
import com.fenlibao.service.pms.da.channel.channel.ChannelService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 渠道service
 * Created by chenzhixuan on 2015/9/24.
 */
@Service
public class ChannelServiceImpl implements ChannelService {
    @Resource
    private ChannelMapper channelMapper;

    @Override
    public List<ChannelVO> getParentChannels() {
        return channelMapper.getParentChannels();
    }

    @Override
    public List<ChannelVO> getChannelsByParent(int parentId) {
        return channelMapper.getChannelsByParent(parentId);
    }

    @Override
    public List<ChannelVO> getChannels(String parentId, String channelId, RowBounds bounds) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("parentId", parentId);
        paramMap.put("channelId", channelId);
        return channelMapper.getChannels(paramMap, bounds);
    }

    @Override
    public ChannelVO getChannelByCode(String channelCode) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("code", channelCode);
        return getChannel(paramMap);
    }

    @Override
    public ChannelVO getChannel(Map<String, Object> paramMap) {
        return channelMapper.getChannel(paramMap);
    }

    @Override
    public int addUserOrigin(Map<String, Object> paramMap) {
        return channelMapper.addUserOrigin(paramMap);
    }

    /**
     * 定义错误结构信息：
     * -1： 名称重复
     * -2：code重复
     *
     * @param channel
     * @return
     */
    @Override
    public int insertSelective(ChannelVO channel) {
        int repeat = this.channelMapper.getChannelCountByName(channel.getName(), channel.getParentId(), channel.getId());
        if (repeat > 0) return -1;
        if (channel.getParentId() > 0) {
            //code 检查
            repeat = this.channelMapper.getChannelCountByCode(channel.getCode());
            if (repeat > 0) return -2;
        }
        return channelMapper.insertSelective(channel);
    }

    @Override
    public List<ChannelVO> findChildPager(ChannelVO channelDTO, RowBounds bounds) {
        return channelMapper.findChildPager(channelDTO, bounds);
    }

    @Override
    public List<ChannelVO> findParentPager(ChannelVO channelDTO, RowBounds bounds) {
        return channelMapper.findParentPager(channelDTO, bounds);
    }

    @Override
    public int deleteParent(List<Integer> channelVOs) {
        for (Integer id : channelVOs) {
            List<ChannelVO> children = getChannelsByParent(id);
            if (children != null && !children.isEmpty()) {
                return -1;
            }
        }
        return channelMapper.delete(channelVOs);
    }

    @Override
    public int deleteChild(List<Integer> channelVOs) {
        return channelMapper.delete(channelVOs);
    }

    /**
     * 根据主键查询渠道
     *
     * @param id
     * @return
     */
    @Override
    public ChannelVO getChannelById(int id) {
        return channelMapper.getChannelById(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ChannelVO channel) {
        int count = channelMapper.getChannelCountByName(channel.getName(), channel.getParentId(), channel.getId());
        if (count > 0) {
            //表示该名称已经存在
            return -3;
        }
        return channelMapper.updateByPrimaryKeySelective(channel);
    }
}






















