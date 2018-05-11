package com.fenlibao.p2p.service.bid.impl;

import com.fenlibao.p2p.dao.*;
import com.fenlibao.p2p.dao.user.AutoBidDao;
import com.fenlibao.p2p.model.entity.SendSmsRecord;
import com.fenlibao.p2p.model.entity.SendSmsRecordExt;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.user.UserAutobidSetting;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.vo.bid.AutoDoBidResVO;
import com.fenlibao.p2p.service.bid.AutoDoBidService;
import com.fenlibao.p2p.service.bid.BidService;
import com.fenlibao.p2p.service.privateMessage.PrivateMessageService;
import com.fenlibao.p2p.util.loader.Sender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by xiao on 2016/11/13.
 */
@Service
public class AutoDoBidServiceImpl implements AutoDoBidService {

    private static final Logger logger = LogManager.getLogger(AutoDoBidServiceImpl.class);

    private static final int RETRY_NUM = 3;

    @Resource
    private BidService bidService;
    @Resource
    private PrivateMessageService privateMessageService;

    @Resource
    private ShopTreasureDao shopTreasureDao;
    @Resource
    private AutoBidDao autoBidDao;
    @Resource
    private UserAccountDao userAccountDao;
    @Resource
    private UserInfoDao userInfoDao;
    @Resource
    private SendSmsRecordDao sendSmsRecordDao;
    @Resource
    private SendSmsRecordExtDao sendSmsRecordExtDao;

    /**
     * 自动投标入口
     */
    @Override
    public void startAutoDoBid() {
        //获取数据库时间
        Timestamp dbCurrentTime = autoBidDao.getDBCurrentTime();
        while (true) {
            try {
                //获取队首的用户规则
                UserAutobidSetting userAutobidSetting = autoBidDao.getFirstRationalRole(dbCurrentTime);
                if (userAutobidSetting == null) {
                    break;
                }
                logger.info("用户[" + userAutobidSetting.getUserId() + "]的[" + userAutobidSetting.getId() + "]号投标规则自动投标开始");

                //投标，返回值不为空
                List<AutoDoBidResVO> successBidList = autoDoBid(userAutobidSetting, 1);

                try {
                    Map map = new HashMap();
                    map.put("id", userAutobidSetting.getId());
                    //更新规则排位
                    autoBidDao.updateRoleRank(map);
                    //更新投标成功时间
                    if (successBidList.size() > 0) {
                        autoBidDao.updateRoleLastBidTime(map);
                    }
                } catch (Throwable t) {
                    logger.info("用户[" + userAutobidSetting.getUserId() + "]的[" + userAutobidSetting.getId() + "]号投标规则自动投标后更新规则排位处理异常");
                }

                //获取用户信息
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("userId", userAutobidSetting.getUserId());
                UserInfo userInfo = userInfoDao.getUserInfoByPhoneNumOrUsername(userMap);
                BigDecimal totalPurchaseAmount = BigDecimal.ZERO;
                for (AutoDoBidResVO adbrVO : successBidList) {
                    totalPurchaseAmount = totalPurchaseAmount.add(adbrVO.getPurchaseAmount());
                }

                try {
                    //发送站内信和短信
                    if (successBidList.size() > 0 && totalPurchaseAmount.compareTo(BigDecimal.ZERO) == 1) {
                        String content = Sender.get("sms.autobid.success.content");
                        //String content = "尊敬的用户：您通过自动投标成功投资了#{totalNum}个项目，投资金额总计#{amount}元。感谢您对我们的关注和支持。";
                        content = content.replace("#{totalNum}", String.valueOf(successBidList.size())).replace("#{amount}", totalPurchaseAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        sendSmsAndLetter(userInfo.getPhone(), String.valueOf(userAutobidSetting.getUserId()), content, VersionTypeEnum.PT);
                    }
                } catch (Throwable t) {
                    logger.info("用户[" + userAutobidSetting.getUserId() + "]的[" + userAutobidSetting.getId() + "]号投标规则自动投标后发送站内信和短信处理异常");
                }

                try {
                    //检查规则生效时长，处理超过一年的未匹配的规则
                    if (successBidList.size() == 0) {
                        checkAutoDoBidRole(userAutobidSetting, userInfo, VersionTypeEnum.PT);
                    }
                } catch (Throwable t) {
                    logger.info("用户[" + userAutobidSetting.getUserId() + "]的[" + userAutobidSetting.getId() + "]号投标规则自动投标后处理超过一年的未匹配的规则时异常");
                }

                logger.info("用户[" + userAutobidSetting.getUserId() + "]的[" + userAutobidSetting.getId() + "]号投标规则自动投标结束：共投标" + successBidList.size() + "个，共" + totalPurchaseAmount + "元。");
            } catch (Throwable t) {
                logger.error("自动投标失败", t);
            }
        }

    }

    /**
     * 检查匹配时长超过一年的规则
     */
    private void checkAutoDoBidRole(UserAutobidSetting userAutobidSetting, UserInfo userInfo, VersionTypeEnum versionTypeEnum) {
        //获取距离上次匹配成功的时间
        int lastBidDateDiff = autoBidDao.getLastBidDateDiff(userAutobidSetting.getId());
        //如果已经到一年，标志位置为删除
        if (lastBidDateDiff >= 365) {
            autoBidDao.deleteAutobidSetting(Integer.parseInt(userInfo.getUserId()), userAutobidSetting.getId());
        } else if (lastBidDateDiff >= 365 - 3) {
            //如果还有三天到一年，发信
            String content = Sender.get("sms.autobid.invalid.content");
            //String content = "尊敬的用户：您设置的自动投标规则在1年内未匹配成功，系统将于3天后删除该条规则，请及时调整，感谢您对我们的关注和支持。";
            //查询是否发送过短信
            int lastSmsDateDiff = -1;
            try {
                lastSmsDateDiff = sendSmsRecordDao.getSmsDateDiff(content, userInfo.getPhone());
            } catch (Exception ee) {
            }
            if (lastSmsDateDiff > 3 || lastSmsDateDiff == -1) {
                sendSmsAndLetter(userInfo.getPhone(), String.valueOf(userAutobidSetting.getUserId()), content, versionTypeEnum);
            }
        }
    }

    /**
     * 投标及投标准备
     *
     * @param userAutobidSetting
     * @param reTryNum
     * @return
     */
    private List<AutoDoBidResVO> autoDoBid(UserAutobidSetting userAutobidSetting, int reTryNum) {
        //预留金额
        BigDecimal userReservedAmount = userAutobidSetting.getReserve() == null ? BigDecimal.ZERO : userAutobidSetting.getReserve();

        //获取用户余额
        BigDecimal userSurplusAmount = userAccountDao.getWLZHBalance(userAutobidSetting.getUserId());

        //根据规则获取标列表
        List<ShopTreasureInfo> shopTreasureInfoList = getBidList(userAutobidSetting.getInterestRate(), userAutobidSetting.getTimeMin(), userAutobidSetting.getMinMark(), userAutobidSetting.getTimeMax(), userAutobidSetting.getMaxMark(), userAutobidSetting.getBidType(), userAutobidSetting.getRepaymentMode());

        //匹配目标标的
        List<ShopTreasureInfo> tarBidList = getTargetShopTreasureInfoList(shopTreasureInfoList, userSurplusAmount, userReservedAmount);

        //投标
        List<AutoDoBidResVO> finalSuccessBidList = autoBidForYLJEMode(tarBidList, userAutobidSetting, userSurplusAmount);

        //如果有部分没有买标成功,重新尝试
        if ((finalSuccessBidList == null || tarBidList == null || tarBidList.size() == 0 ||
                finalSuccessBidList.size() == 0 || finalSuccessBidList.size() < tarBidList.size()) && reTryNum < RETRY_NUM) {
            logger.info("开始为用户[" + userAutobidSetting.getUserId() + "]的[" + userAutobidSetting.getId() + "]号自动投标重新追加匹配");
            reTryNum++;
            List<AutoDoBidResVO> successBidList = autoDoBid(userAutobidSetting, reTryNum);
            if (finalSuccessBidList != null) {
                finalSuccessBidList.addAll(successBidList);
            }
        }
        return finalSuccessBidList;
    }

    /**
     * 获取符合规则的标
     *
     * @param interestRate
     * @param minCycle
     * @param maxCycle
     * @param bidType
     * @param repaymentType
     * @return
     */
    private List<ShopTreasureInfo> getBidList(BigDecimal interestRate, int minCycle, String minMark, int maxCycle, String maxMark, String bidType, String repaymentType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("interestRate", interestRate);

        if ("M".equalsIgnoreCase(minMark)) {
            map.put("minCycle", minCycle * 30);
        } else {
            map.put("minCycle", minCycle);
        }

        if ("M".equalsIgnoreCase(maxMark)) {
            map.put("maxCycle", maxCycle * 30);
        } else {
            map.put("maxCycle", maxCycle);
        }

        map.put("bidType", bidType);
        map.put("repaymentType", repaymentType);

        List<ShopTreasureInfo> list = this.shopTreasureDao.getAutoBidShopTreasure(map);
        return list;
    }

    /**
     * 匹配
     *
     * @param shopTreasureInfoList
     * @param userSurplusAmount    用户余额
     * @param userReservedAmount   预留金额
     * @return
     */
    private List<ShopTreasureInfo> getTargetShopTreasureInfoList(List<ShopTreasureInfo> shopTreasureInfoList, BigDecimal userSurplusAmount, BigDecimal userReservedAmount) {
        List<ShopTreasureInfo> tarBidList = new ArrayList<ShopTreasureInfo>();
        boolean flag = false;
        for (int i = 0; i < shopTreasureInfoList.size(); i++) {
            ShopTreasureInfo shopTreasureInfo = shopTreasureInfoList.get(i);
            if (flag) {
                //匹配
                tarBidList.add(shopTreasureInfo);
                userSurplusAmount = userSurplusAmount.subtract(shopTreasureInfo.getVoteAmount());
                if (userSurplusAmount.subtract(userReservedAmount).compareTo(BigDecimal.ZERO) == 1) {//还有余额
                    continue;
                } else { //正好用完或余额小于标的
                    break;
                }
            } else {
                //寻址
                int bg = shopTreasureInfo.getVoteAmount().setScale(0, BigDecimal.ROUND_DOWN).compareTo(userSurplusAmount.subtract(userReservedAmount).setScale(0, BigDecimal.ROUND_DOWN));
                if (bg == -1) {
                    i = (i >= 1) ? i - 2 : i - 1;
                    flag = true;
                    continue;
                }
                //正好匹配到或者已经到达最后
                if (bg == 0 || i == shopTreasureInfoList.size() - 1) {
                    i--;
                    flag = true;
                    continue;
                }
            }
        }
        return tarBidList;
    }

    /**
     * 预留金额方式的自动投标处理
     *
     * @param tarBidList
     */
    private List<AutoDoBidResVO> autoBidForYLJEMode(List<ShopTreasureInfo> tarBidList, UserAutobidSetting userAutobidSetting, BigDecimal userSurplusAmount) {
        List<AutoDoBidResVO> autoDoBidResVoList = new ArrayList<>();
        for (ShopTreasureInfo shopTreasureInfo : tarBidList) {
            try {
                AutoDoBidResVO autoDoBidResVO = new AutoDoBidResVO(shopTreasureInfo.getId(), shopTreasureInfo.getName());
                BigDecimal thisPurchaseAmount;

                if (userSurplusAmount.subtract(userAutobidSetting.getReserve()).compareTo(shopTreasureInfo.getVoteAmount()) == -1) {
                    thisPurchaseAmount = userSurplusAmount.subtract(userAutobidSetting.getReserve()).setScale(0, BigDecimal.ROUND_DOWN);
                } else {
                    thisPurchaseAmount = shopTreasureInfo.getVoteAmount().setScale(0, BigDecimal.ROUND_DOWN);
                }

                if (thisPurchaseAmount.compareTo(new BigDecimal(100)) != -1) {
                    bidService.doBid(shopTreasureInfo.getId(), thisPurchaseAmount, userAutobidSetting.getUserId(), null, null, null, true);
                    userSurplusAmount = userSurplusAmount.subtract(thisPurchaseAmount);
                    autoDoBidResVO.setPurchaseAmount(thisPurchaseAmount);
                    autoDoBidResVoList.add(autoDoBidResVO);
                }
            } catch (Throwable throwable) {
                logger.error("单个标(" + shopTreasureInfo.getName() + ")自动投标失败", throwable);
            }
        }
        return autoDoBidResVoList;
    }

    /**
     * 发送短信和站内信
     *
     * @param phoneNum
     * @param userId
     */
    private void sendSmsAndLetter(String phoneNum, String userId, String content, VersionTypeEnum versionTypeEnum) {
        String znxSuffixContent = Sender.get("znx.suffix.content");
        //String znxSuffixContent = "如果您需要更多帮助，请查看帮助中心。您也可以随时拨打分利宝的客服热线400-930-5559，或发送邮件至kf@fenlibao.com，我们的客服人员会尽快帮您解答。";
        //发短信
        try {
            sendMsg(phoneNum, content);
        } catch (Throwable t) {
            logger.error("发送短信失败", t);
        }
        //站内信
        try {
            privateMessageService.sendLetter(userId, InterfaceConst.PRIVATEMESSAGE_TITL_TZTZ, content + znxSuffixContent, versionTypeEnum);
        } catch (Throwable t) {
            logger.error("发送站内信失败", t);
        }
    }

    /**
     * 发送短信
     *
     * @param phoneNum
     * @param content
     */
    private void sendMsg(String phoneNum, String content) {
        Timestamp outTime = new Timestamp(System.currentTimeMillis() / 1000 * 1000 + 30 * 60 * 1000);//过期时间30分钟
        SendSmsRecord record = new SendSmsRecord();
        record.setContent(content);
        record.setCreateTime(new Date());
        record.setStatus("W");
        record.setType(0);
        record.setOutTime(outTime);
        sendSmsRecordDao.insertSendSmsRecord(record);

        SendSmsRecordExt recordExt = new SendSmsRecordExt();
        recordExt.setId(record.getId());
        recordExt.setPhoneNum(phoneNum);
        sendSmsRecordExtDao.insertSendSmsRecordExt(recordExt);
    }

}
