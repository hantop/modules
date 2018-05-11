package com.fenlibao.platform.service.integral.impl;

import com.fenlibao.platform.common.config.Config;
import com.fenlibao.platform.common.exception.BusinessException;
import com.fenlibao.platform.common.util.DateUtil;
import com.fenlibao.platform.dao.CommonMapper;
import com.fenlibao.platform.dao.integral.IntegralMapper;
import com.fenlibao.platform.model.Response;
import com.fenlibao.platform.model.integral.MonthPoints;
import com.fenlibao.platform.model.integral.PointsConfig;
import com.fenlibao.platform.model.integral.PointsRecord;
import com.fenlibao.platform.model.integral.PointsType;
import com.fenlibao.platform.model.member.MemberConsumeRecord;
import com.fenlibao.platform.model.member.MemberPoints;
import com.fenlibao.platform.service.CommonService;
import com.fenlibao.platform.service.integral.IntegralService;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.Date;

/**
 * Created by Lullaby on 2016/2/19.
 */
public class IntegralServiceImpl implements IntegralService {

    @Inject
    private IntegralMapper integralMapper;
    @Inject
    private CommonMapper commonMapper;
    @Inject
    private CommonService commonService;
    private static Config CONFIG = ConfigFactory.create(Config.class);
    /**
     * 获取积分类型
     * @param typecode
     * @return
     */
    public PointsType getPointsType(String typecode) {
        return integralMapper.getPointsType(typecode);
    }

    /**
     * 消费金额转积分
     * @param amount
     * @param pointsTypeId
     * @return
     */
    public int amountToPoints(String amount, int pointsTypeId) throws Exception {
        int result = -1;
        PointsConfig pointsConfig = integralMapper.getPointsConfig(pointsTypeId);
        if (pointsConfig == null) {
        	throw new BusinessException(Response.INTEGRAL_EXCHANGE_RULE_NOT_EXIST);
        }
        int amt = (int) Double.parseDouble(amount);
        result = amt * pointsConfig.getExchangePoint();
        return result;
    }

    /**
     * 保存会员积分记录
     * @param points
     * @return
     */
    public int saveMemberPoints(MemberPoints points) {
        return integralMapper.saveMemberPoints(points);
    }

    public PointsRecord getMemberPointsRecord(int userId) {
        return integralMapper.getMemberPointsRecord(userId);
    }

    public MemberConsumeRecord getMemberConsumeRecord(String pos_sn) {
        return integralMapper.getMemberConsumeRecord(pos_sn);
    }

    public int updateMemberPointsRecord(PointsRecord record) {
        return integralMapper.updateMemberPointsRecord(record);
    }

	@Override
	public Integer getId(Integer userId) {
		return integralMapper.getId(userId);
	}

	@Override
	public Integer getPoints(String appid, String openid) throws Exception {
		String merchantId = commonService.getMerchantId(appid);
		String userId = commonMapper.getUserId(merchantId, openid);
		if (StringUtils.isBlank(userId)) {
            PointsRecord points = getMemberPointsRecordByOpenid(openid);
            if (points == null) {
                throw new BusinessException(Response.MERCHANT_MEMBER_NOT_EXIST);
            }
            return points.getNumbers();
		}
		return integralMapper.getPoints(userId);
	}

    @Override
    public PointsRecord getMemberPointsRecordByOpenid(String openid) {
        return integralMapper.getMemberPointsRecordByOpenid(openid);
    }

    @Override
    public int validateThisMonthPoints(int gainPoints, String typeCode, int pfUserId, String openId) {
        int maxPoints = Integer.valueOf(CONFIG.getMemberPointLimit());
        MonthPoints monthPoints = integralMapper.getThisMonthPoints(typeCode, openId);
        Date date = DateUtil.nowDate();
        if (monthPoints == null) {
            monthPoints = new MonthPoints();
            monthPoints.setMonth(date);
            monthPoints.setOpenId(openId);
            monthPoints.setSumPoints(gainPoints);
            monthPoints.setTypeCode(typeCode);
            monthPoints.setUserId(pfUserId);
            integralMapper.insertMonthPoints(monthPoints);
            return maxPoints > gainPoints ? gainPoints : maxPoints;
        } else {
            if (monthPoints.getSumPoints() >= maxPoints) {
                integralMapper.updateThisMonthPoints(monthPoints.getId(), pfUserId, monthPoints.getSumPoints() + gainPoints);
                return 0;
            } else {
                int addPoints = ((maxPoints - monthPoints.getSumPoints()) > gainPoints ? gainPoints : maxPoints - monthPoints.getSumPoints());
                integralMapper.updateThisMonthPoints(monthPoints.getId(), pfUserId, monthPoints.getSumPoints() + gainPoints);
                return addPoints;
            }
        }
    }
}
