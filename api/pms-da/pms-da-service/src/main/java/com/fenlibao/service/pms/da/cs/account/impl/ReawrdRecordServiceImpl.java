package com.fenlibao.service.pms.da.cs.account.impl;

import com.fenlibao.dao.pms.da.cs.account.ReawrdRecordMapper;
import com.fenlibao.model.pms.da.bidType.BidType;
import com.fenlibao.model.pms.da.cs.account.ReawrdRecord;
import com.fenlibao.model.pms.da.cs.account.vo.RateCouponReawrdRecordVO;
import com.fenlibao.model.pms.da.cs.form.TransactionForm;
import com.fenlibao.model.pms.da.marketing.activity.Activity;
import com.fenlibao.model.pms.da.reward.RewardRecord;
import com.fenlibao.model.pms.da.reward.UserRedpackets;
import com.fenlibao.service.pms.da.bidtype.BidTypeService;
import com.fenlibao.service.pms.da.cs.account.ReawrdRecordService;
import com.fenlibao.service.pms.da.marketing.activity.ActivityService;
import com.fenlibao.service.pms.da.reward.common.RewardService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2016/1/4.
 */
@Service
public class ReawrdRecordServiceImpl implements ReawrdRecordService {
    @Resource
    private ReawrdRecordMapper reawrdRecordMapper;
    @Resource
    private BidTypeService bidTypeService;
    @Resource
    private ActivityService activityService;
    @Resource
    private RewardService rewardService;

    @Override
    public List<ReawrdRecord> findReawrdRecord(TransactionForm transaction, RowBounds bounds) {
        return reawrdRecordMapper.findReawrdRecord(transaction, bounds);
    }

    @Override
    public List<UserRedpackets> getUserRedpackets(TransactionForm transaction, RowBounds bounds) {
        return reawrdRecordMapper.getUserRedpackets(transaction, bounds);
    }

    @Override
    public List<ReawrdRecord> getCashRedPackets(TransactionForm transaction, RowBounds bounds) {
        return reawrdRecordMapper.getCashRedPackets(transaction, bounds);
    }

    @Override
    public List<ReawrdRecord> getUserExperienceGold(TransactionForm transaction, RowBounds bounds) {
        return reawrdRecordMapper.getUserExperienceGold(transaction, bounds);
    }

    @Override
    public List<ReawrdRecord> getUserRateCoupon(TransactionForm transaction, RowBounds bounds) {
        List<ReawrdRecord> list = reawrdRecordMapper.getUserRateCoupon(transaction, bounds);
        List<Integer> bidTypeIds;
        List<BidType> bidTypes = bidTypeService.getAllUsedBidType();
        StringBuffer bidTypeSb;
        Map<Integer, String> bidTypeMap = new HashMap<>();
        // 手动发放奖励ID
        Set<Integer> manualGrantIds = new HashSet<>();
        // 自动发放奖励ID
        Set<Integer> autoActivityIds = new HashSet<>();
        Map<Integer, String> activitys = new HashMap<>();
        Map<Integer, String> grants = new HashMap<>();

        RateCouponReawrdRecordVO rateCoupon;
        for (BidType bidType : bidTypes) {
            bidTypeMap.put(bidType.getId(), bidType.getTypeName());
        }
        for (ReawrdRecord reawrdRecord : list) {
            rateCoupon = (RateCouponReawrdRecordVO) reawrdRecord;
            bidTypeSb = new StringBuffer();
            bidTypeIds = this.bidTypeService.getBidTypeIdsByRateCouponId(rateCoupon.getCouponId());
            for (Integer bidTypeId : bidTypeIds) {
                bidTypeSb.append(bidTypeMap.get(bidTypeId)).append(",");
            }
            if(bidTypeSb.length() > 0) {
                bidTypeSb.deleteCharAt(bidTypeSb.length() - 1);
                rateCoupon.setBidTypeStr(bidTypeSb.toString());
            }
            rateCoupon.setScope(rateCoupon.getScope().multiply(new BigDecimal(100)));
            // 判断手动发放还是自动发放
            if (rateCoupon.getGrantId() > 0) {// 手动发放
                manualGrantIds.add(rateCoupon.getGrantId());
            } else {// 自动发放
                autoActivityIds.add(rateCoupon.getActivityId());
            }
        }
        if (manualGrantIds.size() > 0) {
            RewardRecord rewardRecord;
            RewardRecord rewardRecordById;
            for (Integer grantId : manualGrantIds) {
                rewardRecord = new RewardRecord();
                rewardRecord.setId(grantId);
                rewardRecordById = rewardService.getRewardRecordById(rewardRecord);
                if (rewardRecordById != null)
                    grants.put(grantId, rewardRecordById.getGrantName());
            }
        }
        if (autoActivityIds.size() > 0) {
            Activity activity;
            for (Integer activityid : autoActivityIds) {
                activity = activityService.getActivityById(activityid);
                if (activity != null)
                    activitys.put(activityid, activity.getName());
            }
        }
        for (ReawrdRecord reawrdRecord : list) {
            rateCoupon = (RateCouponReawrdRecordVO) reawrdRecord;
            // 判断手动发放还是自动发放
            if (rateCoupon.getGrantId() > 0) {// 自动发放
                rateCoupon.setChannelName(grants.get(rateCoupon.getGrantId()));
            } else {
                rateCoupon.setChannelName(activitys.get(rateCoupon.getActivityId()));
            }
        }
        return list;
    }
}
