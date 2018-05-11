package com.fenlibao.p2p.dao.channel;


import com.fenlibao.p2p.model.channel.vo.ChannelVO;

import java.util.Map;

public interface ChannelDao {

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
}
