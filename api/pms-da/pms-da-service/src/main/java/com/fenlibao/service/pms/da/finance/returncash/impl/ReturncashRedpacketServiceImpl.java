package com.fenlibao.service.pms.da.finance.returncash.impl;

import com.fenlibao.dao.pms.da.finance.returncash.ReturncashRedpacketMapper;
import com.fenlibao.model.pms.da.finance.vo.ReturncachRedpacketVO;
import com.fenlibao.model.pms.da.finance.vo.UserUsedReturncachRedpacketVO;
import com.fenlibao.service.pms.da.finance.returncash.ReturncashRedpacketService;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/1/12.
 */
@Service
public class ReturncashRedpacketServiceImpl implements ReturncashRedpacketService {
    @Resource
    private ReturncashRedpacketMapper returncashRedpacketMapper;

    @Override
    public BigDecimal findUsedTotalCost(Date startDate, Date endDate, String activityCode, boolean systemgrantFlag) {
        return returncashRedpacketMapper.findUsedTotalCost(startDate, endDate, activityCode, systemgrantFlag);
    }

    @Override
    public List<UserUsedReturncachRedpacketVO> findUserUsedReturncachRedpackets(Date startDate, Date endDate, Integer redpacketId, boolean systemgrantFlag, String phoneNum, RowBounds bounds) {
        return returncashRedpacketMapper.findUserUsedReturncachRedpackets(startDate, endDate, redpacketId, systemgrantFlag, phoneNum, bounds);
    }

    @Override
    public List<ReturncachRedpacketVO> findUsedReturncachRedpackets(Date startDate, Date endDate, String activityCode, boolean systemgrantFlag, RowBounds bounds) {
        return returncashRedpacketMapper.findUsedReturncachRedpackets(startDate, endDate, activityCode, systemgrantFlag, bounds);
    }

	@Override
	public Integer findTotlActiveCount(Date startDate, Date endDate, String activityCode, boolean systemgrantFlag) {
		return this.returncashRedpacketMapper.findTotlActiveCount(startDate, endDate, activityCode, systemgrantFlag);
	}

	@Override
	public Integer findTotlRedNumber(Date startDate, Date endDate, String activityCode, boolean systemgrantFlag) {
		return this.returncashRedpacketMapper.findTotlRedNumber(startDate,endDate,activityCode,systemgrantFlag);
	}

	@Override
	public Integer getEachRedPacketSendAmount(Date startDate,Date endDate,String activityCode,Integer redpacketId,boolean systemgrantFlag) {
		return this.returncashRedpacketMapper.getEachRedPacketSendAmount(startDate,endDate,activityCode,redpacketId,systemgrantFlag);
	}

}
