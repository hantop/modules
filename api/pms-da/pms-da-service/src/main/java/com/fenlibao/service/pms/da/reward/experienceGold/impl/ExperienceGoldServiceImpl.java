package com.fenlibao.service.pms.da.reward.experienceGold.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fenlibao.common.pms.util.loader.Message;
import com.fenlibao.dao.pms.da.reward.common.RewardMapper;
import com.fenlibao.dao.pms.da.reward.experienceGold.ExperienceGoldMapper;
import com.fenlibao.model.pms.da.reward.ExperienceGoldStatistics;
import com.fenlibao.model.pms.da.reward.ExperienceFunding;
import com.fenlibao.model.pms.da.reward.ExperienceGold;
import com.fenlibao.model.pms.da.reward.RewardRecord;
import com.fenlibao.model.pms.da.reward.T1040;
import com.fenlibao.model.pms.da.reward.T1041;
import com.fenlibao.model.pms.da.reward.T6123;
import com.fenlibao.model.pms.da.reward.T6124;
import com.fenlibao.model.pms.da.reward.UserExperience;
import com.fenlibao.service.pms.da.reward.experienceGold.ExperienceGoldService;

@Service
public class ExperienceGoldServiceImpl implements ExperienceGoldService {

    @Resource
    ExperienceGoldMapper experienceGoldMapper;

    @Resource
    RewardMapper rewardMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> insertExperienceGoldRecords(RewardRecord record, List<String[]> detailList) {
        List<String> phoneColumn = new ArrayList<String>();
        List<String> codeColumn = new ArrayList<String>();
        List<String> phoneAndCode = new ArrayList<String>();
        List<String> contentErrorList = new ArrayList<String>();
        List<Integer> userIdList = new ArrayList<Integer>();
        // 查出所有体验金代码
        RowBounds bounds = new RowBounds();
        List<ExperienceGold> experienceGoldList = findExperienceGoldPager(null, bounds);
        List<String> goldCodeList = new ArrayList<String>();
        for (ExperienceGold e : experienceGoldList) {
            goldCodeList.add(e.getActivityCode());
        }

        for (int i = 0; i < detailList.size(); i++) {
            if (detailList.get(i) != null) {
                String row[] = detailList.get(i);
                phoneColumn.add(row[0]);
                codeColumn.add(row[1]);
                phoneAndCode.add(row[0] + " , " + row[1]);
                Integer userId = rewardMapper.getUserIdbyPhone(row[0]);
                userIdList.add(userId);
            } else {
                phoneColumn.add(null);
                codeColumn.add(null);
                phoneAndCode.add(null);
                userIdList.add(null);
            }

        }

        for (int i = 0; i < detailList.size(); i++) {
            if (detailList.get(i) != null) {
                StringBuilder message = new StringBuilder();
                message.append("第" + (i + 2) + "行：");
                boolean noProblem = true;
                // 重复的
                String pAndC = phoneAndCode.get(i);
                if (Collections.frequency(phoneAndCode, pAndC) > 1) {
                    message.append(" 重复的数据  ");
                    noProblem = false;
                }
                // 用户不存在
                Integer uId = userIdList.get(i);
                if (uId == null) {
                    message.append(" 用户" + phoneColumn.get(i) + "不存在  ");
                    noProblem = false;
                }
                // 体验金代码不存在
                String c = codeColumn.get(i);
                if (!goldCodeList.contains(c)) {
                    message.append(" 体验金类型“" + c + "”不存在  ");
                    noProblem = false;
                }
                if (!noProblem) {
                    contentErrorList.add(message.toString());
                }
            }
        }

        if (contentErrorList.isEmpty()) {
            rewardMapper.insertRewardRecord(record);
            List<UserExperience> insertList = new ArrayList<UserExperience>();
            for (int i = 0; i < detailList.size(); i++) {
                if (detailList.get(i) != null) {
                    String row[] = detailList.get(i);
                    Integer userId = userIdList.get(i);
                    ExperienceGold experienceGold = experienceGoldMapper.getExperienceGoldbyCode(row[1]);
                    UserExperience item = new UserExperience();
                    Calendar calendar = Calendar.getInstance();
                    Date date = new Date();
                    calendar.setTime(date);
                    calendar.add(Calendar.YEAR, 10);// 将时间设为10年后
                    date = calendar.getTime();
                    item.setUserId(userId);
                    item.setGoldId(experienceGold.getId());
                    item.setStatus((byte) 1);// 未使用
                    item.setStartTime(date);
                    item.setYearYield(experienceGold.getYearYield());
                    item.setEndTime(date);
                    item.setYieldStatus("WJX");// 未计息
                    item.setGrantId(record.getId());
                    item.setGrantStatus((byte) 0);// 未发放
                    insertList.add(item);
                }
            }
            if (insertList.size() > 5000) {
                rewardMapper.deleteRewardRecord(record);
                contentErrorList.add("一次不能导入超过5000条记录！");
            } else {
                if (!insertList.isEmpty()) {
                    experienceGoldMapper.insertExperienceGoldRecordDetail(insertList);
                } else {
                	rewardMapper.deleteRewardRecord(record);
                    contentErrorList.add("没可用数据可导入");
                }
            }
        }
        return contentErrorList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String grantExperienceGold(RewardRecord rewardRecord) throws Exception {
    //    Byte granted = rewardMapper.selectGrantedById(rewardRecord.getId());
        RewardRecord record = rewardMapper.getRewardRecordById(rewardRecord);
        if (record != null) {

            //短信站内信添加标题
            String grantName = record.getGrantName();
            String[] grantNameArr = null;
            if (!StringUtils.isBlank(grantName)){
                grantNameArr = grantName.split("_");
            }

            long startMiniSecond = System.currentTimeMillis();
            Timestamp datetime = new Timestamp(startMiniSecond);
            rewardRecord.setGrantTime(datetime);
            rewardRecord.setGranted((byte) 1);
            
            UserExperience userExperience = new UserExperience();
            userExperience.setGrantId(rewardRecord.getId());
            RowBounds bounds = new RowBounds();
            List<UserExperience> userExperienceList = experienceGoldMapper.getExperienceGoldRecordDetail(userExperience,
                    bounds);
            List<ExperienceFunding> experienceFundingList = new ArrayList<ExperienceFunding>();

            List<T6123> t6123sToInsert = new ArrayList<>();
            List<T1040> t1040sToInsert = new ArrayList<>();
            Message.loadProperties();
            List<ExperienceGold> userExperienceGoldTypeList = new ArrayList<>();
            BigDecimal grantSum=new BigDecimal(0);
            for (UserExperience item : userExperienceList) {
                datetime = new Timestamp(System.currentTimeMillis());
                item.setStartTime(datetime);

                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, 0);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.SECOND, 0);
                startTime.set(Calendar.MILLISECOND, 0);
                startMiniSecond = startTime.getTimeInMillis();

                ExperienceGold experienceGold = experienceGoldMapper.getExperienceGoldbyCode(item.getActivityCode());
                grantSum=grantSum.add(experienceGold.getExperienceGold());
                userExperienceGoldTypeList.add(experienceGold);
                long endMiniSecond = startMiniSecond + ((experienceGold.getEffectDay()) * (24 * 3600 * 1000)) - 1000;
                item.setEndTime(new Timestamp(endMiniSecond));
                item.setGrantStatus((byte) 1);
                item.setStatus((byte) 2);//已使用是已开始使用的意思
                ExperienceFunding fundItem = new ExperienceFunding();
                fundItem.setUserId(item.getUserId());
                fundItem.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
                fundItem.setIncome(experienceGold.getExperienceGold());
                fundItem.setOverage(experienceGold.getExperienceGold());
                fundItem.setOperatorType("1");
                fundItem.setExpId(item.getGoldId());
                experienceFundingList.add(fundItem);
                // 发站内信
                T6123 t6123Letter = new T6123();
                t6123Letter.setF02(item.getUserId());
                t6123Letter.setF03("体验金发放成功");
                t6123Letter.setF04(datetime);
                t6123Letter.setF05("WD");
                rewardMapper.insertT6123(t6123Letter);
                t6123sToInsert.add(t6123Letter);
                // 发短信
                T1040 t1040Message = new T1040();
                t1040Message.setF02(0);
                StringBuilder shortMessage = new StringBuilder(Message.get("short.system.experiencegold"));//后台发送短信
                if (grantNameArr.length > 0) {
                    shortMessage.replace(shortMessage.indexOf("{title}"), shortMessage.indexOf("{title}") + "{title}".length(), "" + grantNameArr[grantNameArr.length - 1]);
                }else{
                    shortMessage.replace(shortMessage.indexOf("{title}"), shortMessage.indexOf("{title}") + "{title}".length(), "" + "");
                }
                shortMessage.replace(shortMessage.indexOf("{money}"), shortMessage.indexOf("{money}") + "{money}".length(), "" + experienceGold.getExperienceGold());
                shortMessage.replace(shortMessage.indexOf("{endtime}"), shortMessage.indexOf("{endtime}") + "{endtime}".length(), (new SimpleDateFormat("yyyy-MM-dd")).format(item.getEndTime()));
                t1040Message.setF03(shortMessage.toString());
                t1040Message.setF04(datetime);
                t1040Message.setF05("W");
                rewardMapper.insertT1040(t1040Message);
                t1040sToInsert.add(t1040Message);
            }
            rewardRecord.setGrantCount(userExperienceList.size());
            rewardRecord.setGrantSum(grantSum);
            rewardRecord.setInService((byte) 0);
            rewardMapper.updateRewardRecord(rewardRecord);
            experienceGoldMapper.updateExperienceGoldRecordDetail(userExperienceList);
            experienceGoldMapper.insertExperienceFunding(experienceFundingList);
            // 批量插入T6124和T1041
            List<T6124> t6124sToInsert = new ArrayList<>();
            List<T1041> t1041sToInsert = new ArrayList<>();
            for (int i = 0; i < userExperienceList.size(); i++) {
                T6124 t6124Letter = new T6124();
                t6124Letter.setF01(t6123sToInsert.get(i).getF01());
                StringBuilder internalMessage = new StringBuilder(Message.get("internal.system.experiencegold"));
                if (grantNameArr.length > 0) {
                    internalMessage.replace(internalMessage.indexOf("{title}"), internalMessage.indexOf("{title}") + "{title}".length(), "" + grantNameArr[grantNameArr.length - 1]);
                }else{
                    internalMessage.replace(internalMessage.indexOf("{title}"), internalMessage.indexOf("{title}") + "{title}".length(), "" + "");
                }
                internalMessage.replace(internalMessage.indexOf("{money}"), internalMessage.indexOf("{money}") + "{money}".length(), "" + userExperienceGoldTypeList.get(i).getExperienceGold());
                internalMessage.replace(internalMessage.indexOf("{endtime}"), internalMessage.indexOf("{endtime}") + "{endtime}".length(), (new SimpleDateFormat("yyyy-MM-dd")).format(userExperienceList.get(i).getEndTime()));
                t6124Letter.setF02(internalMessage.toString());
                t6124sToInsert.add(t6124Letter);
                T1041 t1041Message = new T1041();
                t1041Message.setF01(t1040sToInsert.get(i).getF01());
                t1041Message.setF02(userExperienceList.get(i).getPhone());
                t1041sToInsert.add(t1041Message);
            }
            // 批量插入t6124
            rewardMapper.batchInsertT6124(t6124sToInsert);
            // 批量插入t1041
            rewardMapper.batchInsertT1041(t1041sToInsert);
            return "发放完成";
        } else {
            return "已发放过，不可重复发放";
        }

    }

    @Override
    public List<ExperienceGold> findExperienceGoldPager(ExperienceGold experienceGold, RowBounds bounds) {
        return experienceGoldMapper.findExperienceGoldPager(experienceGold, bounds);
    }

    @Override
    public ExperienceGold getExperienceGoldById(int id) {
        return experienceGoldMapper.selectByPrimaryKey(id);
    }

    @Override
    public int experienceGoldRemove(List<Integer> experienceGolds) {
        return experienceGoldMapper.experienceGoldRemove(experienceGolds);
    }

    @Override
    public int experienceGoldEdit(ExperienceGold experienceGold) {
        int count = experienceGoldMapper.getExperienceGoldCountByCode(experienceGold.getActivityCode(),
                experienceGold.getId());
        if (count > 0) {
            return -1;
        }
        if (experienceGold.getId() > 0) {
            return this.updateByPrimaryKeySelective(experienceGold);
        } else {
            return this.insertSelective(experienceGold);
        }
    }

    private int updateByPrimaryKeySelective(ExperienceGold experienceGold) {
        return experienceGoldMapper.updateByPrimaryKeySelective(experienceGold);
    }

    private int insertSelective(ExperienceGold experienceGold) {
        return experienceGoldMapper.insertSelective(experienceGold);
    }

    @Override
    public List<UserExperience> getExperienceGoldRecordDetail(UserExperience userExperience, RowBounds bounds) {
        return experienceGoldMapper.getExperienceGoldRecordDetail(userExperience, bounds);
    }

    /**
     * 发放记录作废
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String cancelExperienceGold(RewardRecord rewardRecord) throws Exception {
        rewardRecord.setGranted((byte) 3);
        rewardMapper.updateRewardRecord(rewardRecord);
        UserExperience userExperience = new UserExperience();
        userExperience.setGrantId(rewardRecord.getId());
        RowBounds bounds = new RowBounds();
        List<UserExperience> userExperienceList = experienceGoldMapper.getExperienceGoldRecordDetail(userExperience, bounds);
        for (UserExperience item : userExperienceList) {
            item.setGrantStatus((byte) 3);
        }
        experienceGoldMapper.updateExperienceGoldRecordDetail(userExperienceList);
        RewardRecord r = rewardMapper.getRewardRecordById(rewardRecord);
        return r.getGrantName() + "已作废";
    }

    @Override
    public List<UserExperience> findAllActivityCode(UserExperience userExperience) {
        return experienceGoldMapper.findAllActivityCode(userExperience);
    }

	@Override
	public List<ExperienceGoldStatistics> experienceGoldStatistics(Integer grantId) {
		return experienceGoldMapper.experienceGoldStatistics(grantId);
	}
}
