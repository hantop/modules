package com.fenlibao.p2p.service.activity.impl;

import com.fenlibao.p2p.dao.activity.ActivityDao;
import com.fenlibao.p2p.model.entity.activity.MoneyTreeFruitRecord;
import com.fenlibao.p2p.model.entity.activity.MoneyTreeRegisterCheckEntity;
import com.fenlibao.p2p.service.UmengService;
import com.fenlibao.p2p.service.activity.ActivityExtService;
import com.fenlibao.p2p.service.activity.ActivityService;
import com.fenlibao.p2p.service.common.RedisService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Sender;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * activity辅助service
 * Created by xiao on 2017/4/27.
 */
@Service
public class ActivityExtServiceImpl implements ActivityExtService {
    private static final Logger logger = LogManager.getLogger(ActivityExtServiceImpl.class);
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
    @Resource
    private ActivityDao activityDao;
    @Resource
    private RedisService redisService;
    @Resource
    private UmengService umengService;
    @Resource
    private ActivityService activityService;

    /**
     * 检查并生成分利果,并推送消息
     *
     * @param moneyTreeRegisterCheckEntity
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void createFruitAndSendMsg(final MoneyTreeRegisterCheckEntity moneyTreeRegisterCheckEntity) {
        //生成果实
        final MoneyTreeFruitRecord mtfr = new MoneyTreeFruitRecord();
        mtfr.setInviteeId(moneyTreeRegisterCheckEntity.getInviteeId());
        mtfr.setInviterId(moneyTreeRegisterCheckEntity.getInviterId());
        mtfr.setInviteAmount(moneyTreeRegisterCheckEntity.getInvestAmount());

        //受邀人
        mtfr.setUserId(moneyTreeRegisterCheckEntity.getInviteeId());
        int resInvitee = activityDao.insertMoneyTreeFruit(mtfr);
        if (resInvitee != 1) {
            throw new RuntimeException("保存分利果失败:" + mtfr.getInviterId() + "邀请" + mtfr.getInviteeId() + "," + mtfr.getUserId() + "获得果实");
        }

        //邀请人
        if (moneyTreeRegisterCheckEntity.getInvitedNumToday() < 5) {
            mtfr.setUserId(moneyTreeRegisterCheckEntity.getInviterId());
            int resInviter = activityDao.insertMoneyTreeFruit(mtfr);
            if (resInviter != 1) {
                throw new RuntimeException("保存分利果失败:" + mtfr.getInviterId() + "邀请" + mtfr.getInviteeId() + "," + mtfr.getUserId() + "获得果实");
            }
        }

        fixedThreadPool.execute(
                new Runnable() {
                    public void run() {
                        //受邀人推消息
                        try {
                            mtfr.setUserId(moneyTreeRegisterCheckEntity.getInviteeId());
                            sendMsg(mtfr);
                        } catch (Exception e) {
                            logger.error(e, e);
                        }

                        //受邀人发短信
                        try {
                            String inviteeContent = Sender.get("sms.activity.flblcj.inviteeSmsContent").replace("#{shortUrl}", genShortUrl(Config.get("activity.flblcj.activityUrl")));
                            activityService.sendSmsForMoneyTree(String.valueOf(moneyTreeRegisterCheckEntity.getInviteeId()), inviteeContent);
                        } catch (Exception e) {
                            logger.error(e, e);
                        }

                        if (moneyTreeRegisterCheckEntity.getInvitedNumToday() < 5) {
                            //邀请人推消息
                            try {
                                mtfr.setUserId(moneyTreeRegisterCheckEntity.getInviterId());
                                sendMsg(mtfr);
                            } catch (Exception e) {
                                logger.error(e, e);
                            }
                            //受邀人发短信
                            try {
                                String inviterContent = Sender.get("sms.activity.flblcj.inviterSmsContent").replace("#{shortUrl}", genShortUrl(Config.get("activity.flblcj.activityUrl")));
                                activityService.sendSmsForMoneyTree(String.valueOf(moneyTreeRegisterCheckEntity.getInviterId()), inviterContent);
                            } catch (Exception e) {
                                logger.error(e, e);
                            }
                        }
                    }
                });

    }

    /**
     * 推送消息
     * @param moneyTreeFruitRecord
     */
    public void sendMsg(MoneyTreeFruitRecord moneyTreeFruitRecord) {
        int userId = moneyTreeFruitRecord.getUserId();
        String umengDevicetoken = redisService.getRedisValue("umeng:user:"+String.valueOf(userId));
        Map<String, String> map = new HashMap<String, String>();
        if(!StringUtils.isEmpty(umengDevicetoken)){
            String[] strs=umengDevicetoken.split("#");
            String deviceTokens = strs[0];
            String clientType = strs[1];
            map.put("deviceToken",deviceTokens);
            if(clientType.equals("1")){
                map.put("bidOrigin","ios");
            }else {
                map.put("bidOrigin","android");
            }
            if(userId==moneyTreeFruitRecord.getInviterId()) {
                map.put("isInviter", "inviter");//推送用户类型是否是邀请人
            }else {
                map.put("isInviter", "invitee");
            }
        }

        try {
            umengService.sendUmengMessage(map);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


    }


    /**
     * 处理链接
     * @param normalUrl
     * @return
     */
    private String genShortUrl(String normalUrl) {
        try {
            String shortUrl = CommonTool.genShortUrl(normalUrl);
            return StringUtils.isEmpty(shortUrl) ? normalUrl : shortUrl;
        } catch (Exception e) {
            return normalUrl;
        }
    }
}