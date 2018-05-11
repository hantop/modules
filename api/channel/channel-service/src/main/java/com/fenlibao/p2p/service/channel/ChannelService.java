package com.fenlibao.p2p.service.channel;

import com.fenlibao.p2p.model.channel.vo.ChannelVO;

import java.util.Map;

/**
 * 渠道service
 * Created by chenzhixuan on 2015/9/24.
 */
public interface ChannelService {
    /**
     * 根据渠道code获取渠道VO
     * @param channelCode
     * @return
     */
    ChannelVO getChannelByCode(String channelCode);

    /**
     * 获取渠道
     * @param paramMap
     * @return
     */
    ChannelVO getChannel(Map<String, Object> paramMap);

    /**
     * 新增用户来源
     * @param paramMap
     * @return
     */
    int addUserOrigin(Map<String, Object> paramMap);

}
