package com.fenlibao.p2p.service.activity.impl;

import com.fenlibao.p2p.dao.lottery.LotteryPrizeDao;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.lottery.entity.LotteryPrizeInfo;
import com.fenlibao.p2p.service.activity.SecondAnniversaryService;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.lottery.LotteryUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/29.
 */
@Service
public class SecondAnniversaryServiceImpl implements SecondAnniversaryService{

    private static final Logger logger = LogManager.getLogger(SecondAnniversaryServiceImpl.class);

    @Resource
    LotteryPrizeDao lotteryPrizeDao;


    @Override
    public List<LotteryPrizeInfo> getLotteryPrizeInfos(int activityId) throws Exception {
        return lotteryPrizeDao.getLotteryPrizeInfos(activityId);
    }

    @Transactional
    @Override
    public LotteryPrizeInfo lotteryAndUpdatePrizeQuantity(int activityId, int userId, String cellTailNumber) throws Exception {
        //获取中奖奖品信息
        LotteryPrizeInfo drawPrizeInfo = null;
        boolean enoughPrizeFlag = false;
        try{
            drawPrizeInfo = getLotteryDrawPrize(activityId);
            if(drawPrizeInfo==null){
                throw new Exception();
            }
            enoughPrizeFlag = true;
        }catch (Exception e){
            //奖品不够或者抽奖异常，给予指定奖品
            logger.warn(ResponseCode.ACTIVITY_GIVE_PRIZE_ERROR);
            String benchPrizeId = Config.get("activity.secondAnniversary.benchPrizeId");
            List<LotteryPrizeInfo> LotteryPrizeInfos = getLotteryPrizeInfos(activityId);
            for(LotteryPrizeInfo lpf: LotteryPrizeInfos){
                if(lpf.getPrizeId() == Integer.parseInt(benchPrizeId)){
                    drawPrizeInfo = lpf;
                    break;
                }
            }
        }

        if(drawPrizeInfo==null){
            throw new BusinessException(ResponseCode.ACTIVITY_MISS_PRIZE_ERROR);
        }

        //if(enoughPrizeFlag){
            //减少奖品数量
            //updatePrizeQuantity(activityId, drawPrizeInfo.getPrizeId(), 1);
            //更新奖品概率
            //updatePrizeProbability(activityId, drawPrizeInfo.getPrizeId(),1);
        //}

        //插入中奖纪录
        int drawRecordFlag = lotteryPrizeDao.addLotteryDrawRecord(activityId ,userId, cellTailNumber, drawPrizeInfo.getPrizeId());
        if(drawRecordFlag <= 0){
            logger.warn(ResponseCode.ACTIVITY_LOTTERY_DRAW_RECORD_NOT_FOUND);
            throw new BusinessException(ResponseCode.ACTIVITY_USER_LOTTERY_DRAW_ERROR);
        }

        return drawPrizeInfo;
    }

    public LotteryPrizeInfo getLotteryDrawPrize(int activityId){
        List<LotteryPrizeInfo> effectivePrizeInfos = lotteryPrizeDao.getEffectivePrizeInfos(activityId);
        if(effectivePrizeInfos ==null || effectivePrizeInfos.size() == 0){
            throw new BusinessException(ResponseCode.ACTIVITY_LOTTERY_LACK_EFFECTIVE_PRIZES.getCode(),ResponseCode.ACTIVITY_LOTTERY_LACK_EFFECTIVE_PRIZES.getMessage());
        }
        List<Double> orignalRates = new ArrayList<Double>(effectivePrizeInfos.size());
        for (LotteryPrizeInfo prizeInfo : effectivePrizeInfos) {
            double probability = prizeInfo.getProbability().doubleValue();
            if (probability < 0) {
                probability = 0;
            }
            orignalRates.add(probability);
        }
        int drawIndex = LotteryUtil.lottery(orignalRates);
        LotteryPrizeInfo drawPrizeInfo = effectivePrizeInfos.get(drawIndex);
        return drawPrizeInfo;
    }
    @Override
    public int updatePrizeQuantity(int activityId, int prizeId, int quantity) {
        return lotteryPrizeDao.updatePrizeQuantity(activityId ,prizeId, quantity);
    }

    @Override
    public int updatePrizeProbability(int activityId, int prizeId, int quantity) {
        return lotteryPrizeDao.updatePrizeProbability(activityId ,prizeId, quantity);
    }

}
