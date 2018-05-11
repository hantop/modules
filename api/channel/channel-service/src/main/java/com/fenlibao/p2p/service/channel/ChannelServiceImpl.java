package com.fenlibao.p2p.service.channel;

import com.fenlibao.p2p.dao.channel.ChannelDao;
import com.fenlibao.p2p.model.channel.vo.ChannelVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 渠道service
 * Created by chenzhixuan on 2015/9/24.
 */
@Service
public class ChannelServiceImpl implements ChannelService {
    @Resource
    private ChannelDao channelDao;

    @Override
    public ChannelVO getChannelByCode(String channelCode) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("code", channelCode);
        return getChannel(paramMap);
    }

    @Override
    public ChannelVO getChannel(Map<String, Object> paramMap) {
        return channelDao.getChannel(paramMap);
    }

    @Override
    public int addUserOrigin(Map<String, Object> paramMap) {
        return channelDao.addUserOrigin(paramMap);
    }
}
