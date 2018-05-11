package com.fenlibao.p2p.service.channel.impl;

import com.fenlibao.p2p.dao.channel.UserOriginDao;
import com.fenlibao.p2p.model.entity.channel.UserOrigin;
import com.fenlibao.p2p.service.channel.UserOriginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/12/16.
 */
@Service
public class UserOriginServiceImpl implements UserOriginService {
    @Autowired
    private UserOriginDao userOriginDao;

    @Override
    public boolean validUserOrigin(String userId, String channelCode) {
        boolean result = false;
        UserOrigin userOrigin = userOriginDao.getUserOrigin(Integer.valueOf(userId));
        if (StringUtils.isBlank(channelCode) || userOrigin == null || StringUtils.isBlank(userOrigin.getChannelCode())) {
            result = false;
        } else if (userOrigin.getChannelCode().equals(channelCode)) {
            result = true;
        }
        return result;
    }
}
