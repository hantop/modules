package com.fenlibao.service.pms.da.reward.common.impl;

import com.fenlibao.common.pms.util.tool.StringHelper;
import com.fenlibao.dao.pms.da.reward.common.RewardMapper;
import com.fenlibao.model.pms.common.global.SystemType;
import com.fenlibao.model.pms.da.reward.RewardRecord;
import com.fenlibao.model.pms.da.reward.RewardRecordCondition;
import com.fenlibao.service.pms.da.reward.common.RewardService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RewardServiceImpl implements RewardService{

    @Resource
    RewardMapper rewardMapper;
	
	@Override
	public List<RewardRecord> getRewardRecords(RewardRecordCondition rewardRecordCondition, RowBounds bounds) {
		String endDate=rewardRecordCondition.getEndDate();
		if(!StringHelper.isNull(endDate)){
			rewardRecordCondition.setEndDate(endDate + " 23:59:59");
		}
		List<Integer> recordIds = new ArrayList<>();
		List<RewardRecord> list=rewardMapper.getRewardRecords(rewardRecordCondition,bounds);
		if(list.size() > 0){
			for (RewardRecord re: list) {
				if(re.getSysType() != 0){
					re.setSysTypeLabel(SystemType.getByValue(re.getSysType()).getLabel());
				}
				recordIds.add(re.getId());
			}
		}
		if(recordIds.size() > 0){
			// 总要求发送数量, 总要求发送金额
			List<Map<String, Object>> recordCount = rewardMapper.getRecordCount(recordIds);
			List<Map<String, Object>> recordSum = rewardMapper.getRecordSum(recordIds);
			if(recordCount.size() > 0 && recordSum.size() > 0){
				for (RewardRecord re: list) {
					re.setGrantCountTotal(Integer.valueOf(getTotalById(recordCount, re.getId(), 1).toString()));
					re.setGrantSumTotal(new BigDecimal(getTotalById(recordSum, re.getId(), 2).toString()));
					re.setCountPersent(re.getGrantCount() + " / " + re.getGrantCountTotal());
					re.setSumPersent(re.getGrantSum() + " / " + re.getGrantSumTotal());
				}
			}
		}
		rewardRecordCondition.setEndDate(endDate);
		return list;
	}

	@Override
	public RewardRecord getRewardRecordById(RewardRecord rewardRecord) {
		return rewardMapper.getRewardRecordById(rewardRecord);
	}

    @Override
    public Integer getInServiceRewards() {
        return rewardMapper.getInServiceRewards();
    }

    @Override
    public Byte getGrantedById(Integer id) {
        return rewardMapper.getIsGrantedById(id);
    }

	@Override
	public void updateRewardRecord(RewardRecord rewardRecord) {
		rewardMapper.updateRewardRecord(rewardRecord);
	}

	private Object getTotalById(List<Map<String, Object>> totalMap, Integer recordId, int type){
		if (totalMap.size() > 0){
			for (Map<String, Object> map: totalMap) {
				String recordIdTemp = map.get("recordId").toString();
				if (recordIdTemp != null && recordId.toString().equals(recordIdTemp)) {
					if(type == 1){
						if(map.get("recordCount").toString() != null){
							return map.get("recordCount").toString();
						}
					}else {
						if(map.get("recordSum").toString() != null){
							return map.get("recordSum").toString();
						}
					}
				}
			}
		}
		return null;
	}

}
