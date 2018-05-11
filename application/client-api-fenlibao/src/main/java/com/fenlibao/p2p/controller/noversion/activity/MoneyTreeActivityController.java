package com.fenlibao.p2p.controller.noversion.activity;

import com.fenlibao.p2p.dao.lottery.LotteryPrizeDao;
import com.fenlibao.p2p.model.entity.activity.ActivityEntity;
import com.fenlibao.p2p.model.entity.activity.MoneyTreeFruit;
import com.fenlibao.p2p.model.entity.activity.MoneyTreePrize;
import com.fenlibao.p2p.model.enums.activity.ActivityStatusEnum;
import com.fenlibao.p2p.model.enums.activity.LtPrizeTypeEnum;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.lottery.vo.LotteryPrizeInfoVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.UserTokenService;
import com.fenlibao.p2p.service.activity.ActivityService;
import com.fenlibao.p2p.service.lottery.LotteryPrizeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 分利宝投资节-摇钱树
 */
@RestController
@RequestMapping("new/activity/moneyTree")
public class MoneyTreeActivityController {

    private static final Logger logger = LogManager.getLogger(MoneyTreeActivityController.class);
    private static String ACTIVITY_CODE = Config.get("activity.flblcj.activityCode");//518摇钱树活动code
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
    @Resource
    UserInfoService userInfoService;
    @Resource
    LotteryPrizeService lotteryPrizeService;
    @Resource
    LotteryPrizeDao lotteryPrizeDao;
    @Resource
    private ActivityService activityService;
    @Resource
    private UserTokenService userTokenService;

    /**
     * 获取我的未掉落的果实列表
     *
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "myFruitList", method = RequestMethod.GET)
    HttpResponse myFruitList(BaseRequestForm paramForm, String token, String userId) {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate() || StringUtils.isEmpty(userId) || StringUtils.isEmpty(token)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        if (userTokenService.isInvalidToken(token, userId, paramForm.getClientType())) {
            response.setCodeMessage(ResponseCode.COMMON_NOT_VALID_TOKEN);
            return response;
        }
        List<MoneyTreeFruit> list = activityService.myMoneyTreeFruitList(ACTIVITY_CODE, userId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("items", list);
        response.setData(data);
        return response;
    }

    /**
     * 获取我的抽奖结果列表
     *
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "myPrizeList", method = RequestMethod.GET)
    HttpResponse myPrizeList(BaseRequestForm paramForm, String token, String userId) {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate() || StringUtils.isEmpty(userId) || StringUtils.isEmpty(token)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        if (userTokenService.isInvalidToken(token, userId, paramForm.getClientType())) {
            response.setCodeMessage(ResponseCode.COMMON_NOT_VALID_TOKEN);
            return response;
        }

        List<MoneyTreePrize> list = activityService.myMoneyTreePrizeList(ACTIVITY_CODE, userId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("items", list);
        response.setData(data);
        return response;
    }

    /**
     * 点击果实抽奖
     *
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "pickFruit", method = RequestMethod.GET)
    HttpResponse pickFruit(BaseRequestForm paramForm, String token, final String userId, String fruitId) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate() || StringUtils.isEmpty(userId) || StringUtils.isEmpty(token) || StringUtils.isEmpty(fruitId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        if (userTokenService.isInvalidToken(token, userId, paramForm.getClientType())) {
            response.setCodeMessage(ResponseCode.COMMON_NOT_VALID_TOKEN);
            return response;
        }

        final LotteryPrizeInfoVO lotteryPrizeInfoVO = activityService.pickFruit(ACTIVITY_CODE, fruitId, userId);

        // 发送现金红包
        final BigDecimal amount = getAmount(lotteryPrizeInfoVO.getPrizeId());
        if (lotteryPrizeInfoVO != null && lotteryPrizeInfoVO.getPrizeType() == LtPrizeTypeEnum.XJHB.getCode() && amount.compareTo(BigDecimal.ZERO) > 0) {
            fixedThreadPool.execute(
                    new Runnable() {
                        public void run() {
                            try {
                                //发送返现红包
                                activityService.grantActivityCashbackForMoneyTree(amount, userId, lotteryPrizeInfoVO.getPrizeRecordId());
                            } catch (Exception e) {
                                logger.error("摇钱树发送返现红包失败：" + lotteryPrizeInfoVO.getPrizeName() + "|" + userId, e);
                            }
                        }
                    });
        }

        response.setData(CommonTool.toMap(lotteryPrizeInfoVO));
        return response;
    }

    /**
     * 获取活动信息
     *
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "getActivityDetail", method = RequestMethod.GET)
    HttpResponse getActivityDetail(BaseRequestForm paramForm, String token, String userId) {
        HttpResponse response = new HttpResponse();
        /*if (!paramForm.validate() || StringUtils.isEmpty(userId) || StringUtils.isEmpty(token)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        if (userTokenService.isInvalidToken(token, userId, paramForm.getClientType())) {
            response.setCodeMessage(ResponseCode.COMMON_NOT_VALID_TOKEN);
            return response;
        }*/

        ActivityEntity activity = activityService.getMoneyTreeActityDetail();
        activity.setActivityUrl(Config.get("activity.flblcj.activityUrl"));

        Date date = new Date();
        if (activity.getStartTime().getTime() < date.getTime()) {
            activity.setStatus(ActivityStatusEnum.WKS.getName());
        } else if (activity.getStartTime().getTime() <= date.getTime() && date.getTime() <= activity.getEndTime().getTime()) {
            activity.setStatus(ActivityStatusEnum.JXZ.getName());
        } else if (date.getTime() >= activity.getEndTime().getTime()) {
            activity.setStatus(ActivityStatusEnum.YJS.getName());
        }
        try {
            response.setData(CommonTool.toMap(activity));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private BigDecimal getAmount(int tarId) {
        String source = Config.get("activity.flblcj.xjhb.idAmonut");
        String[] p = source.split("\\|");
        for (int i = 0; i < p.length; i++) {
            String r = p[i];
            String[] t = r.split(",");
            int id = Integer.parseInt(t[0]);
            BigDecimal amount = BigDecimal.valueOf(Integer.parseInt(t[1]));
            if (tarId == id) {
                return amount;
            }
        }
        return BigDecimal.ZERO;
    }
}
