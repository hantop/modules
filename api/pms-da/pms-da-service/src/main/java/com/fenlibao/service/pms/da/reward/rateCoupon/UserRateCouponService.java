package com.fenlibao.service.pms.da.reward.rateCoupon;

import com.fenlibao.model.pms.da.reward.RateCouponGrantStatistics;
import com.fenlibao.model.pms.da.reward.RewardRecord;
import com.fenlibao.model.pms.da.reward.UserRateCoupon;
import com.fenlibao.model.pms.da.reward.form.RateCouponDetailForm;
import com.fenlibao.service.pms.da.exception.ExcelException;
import org.apache.ibatis.session.RowBounds;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户加息券
 * <p>
 * Created by chenzhixuan on 2016/9/7.
 */
public interface UserRateCouponService {

    /**
     * 新增加息券发送详情
     *
     * @param newRecord
     * @param list
     * @param activityId
     * @return
     */
    List<String> addGrantRateCoupon(RewardRecord newRecord, List<String[]> list, int activityId) throws ExcelException;

    /**
     * 获取Excel头的活动编码校验返回活动ID
     *
     * @param file
     * @return
     */
    int getExcelHeaderActiviyId(MultipartFile file) throws Throwable;

    List<RateCouponGrantStatistics> getRateCouponGrantStatistics(Integer grantId);

    List<UserRateCoupon> findPager(RateCouponDetailForm rateCouponDetailForm, RowBounds bounds);

    List<UserRateCoupon> findUserRateCouponAll(RewardRecord rewardRecord, RateCouponDetailForm rateCouponDetailForm);

    /**
     * 发放
     *
     * @param rewardRecord
     * @param userRateCoupons
     * @return
     */
    void grantRateCoupons(RewardRecord rewardRecord, List<UserRateCoupon> userRateCoupons);

    /**
     * 作废
     *
     * @param rewardRecord
     * @param rateCouponDetailForm
     * @return
     */
    String cancelRateCoupon(RewardRecord rewardRecord, RateCouponDetailForm rateCouponDetailForm);
}
