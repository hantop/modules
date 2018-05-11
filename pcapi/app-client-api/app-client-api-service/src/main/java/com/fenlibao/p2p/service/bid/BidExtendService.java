package com.fenlibao.p2p.service.bid;

import com.fenlibao.p2p.model.entity.UserInfo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by LouisWang on 2015/8/14.
 */
public interface BidExtendService {

    /**
     * 获取活期宝指定日期的收益
     * @param userId
     * @param earnDate
     * @return
     */
    BigDecimal getHqbEarning(String userId, Date earnDate);

    /**
     * 获取还款记录列表
     * @param
     * @return
     */
    List getBidInvestRecords(String bid,String timestamp,String isUp) throws Exception;

    /**
     * 获取用户信息
     * @param userId 用户Id
     * @return UserInfo 用户信息
     */
    UserInfo getUserInfo(Integer userId) throws Exception;

    /**
     * 获取我的红包
     * @param userId
     * @param timestamp
     * @param isUp
     * @param type
     * @param status
     * @return
     * @throws Throwable
     */
    Map<String,Object> getRedPackets(int userId,String timestamp,String isUp,String type,String status)throws Throwable;
}
