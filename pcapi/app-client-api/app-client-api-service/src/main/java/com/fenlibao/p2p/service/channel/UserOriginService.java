package com.fenlibao.p2p.service.channel;

/**
 * Created by Administrator on 2016/12/16.
 */
public interface UserOriginService {
    /**
     * 判断用户是否某个渠道
     *
     * @param userId
     * @param channelCode
     * @return
     */
    boolean validUserOrigin(String userId, String channelCode);
}
