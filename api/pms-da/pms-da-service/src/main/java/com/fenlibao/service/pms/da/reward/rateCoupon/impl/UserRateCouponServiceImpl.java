package com.fenlibao.service.pms.da.reward.rateCoupon.impl;

import com.fenlibao.common.pms.util.exception.ImportExcelException;
import com.fenlibao.common.pms.util.loader.Message;
import com.fenlibao.common.pms.util.tool.StringHelper;
import com.fenlibao.dao.pms.da.marketing.activity.ActivityMapper;
import com.fenlibao.dao.pms.da.reward.common.RewardMapper;
import com.fenlibao.dao.pms.da.reward.rateCoupon.RateCouponMapper;
import com.fenlibao.dao.pms.da.reward.rateCoupon.UserRateCouponMapper;
import com.fenlibao.model.pms.da.reward.*;
import com.fenlibao.model.pms.da.reward.form.RateCouponDetailForm;
import com.fenlibao.service.pms.da.exception.ExcelException;
import com.fenlibao.service.pms.da.reward.rateCoupon.UserRateCouponService;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

import static com.fenlibao.common.pms.util.tool.POIUtil.getStringNoBlank;


/**
 * Created by chenzhixuan on 2016/8/25.
 */
@Service
public class UserRateCouponServiceImpl implements UserRateCouponService {
    @Autowired
    private RateCouponMapper rateCouponMapper;
    @Autowired
    private UserRateCouponMapper userRateCouponMapper;
    @Autowired
    private RewardMapper rewardMapper;
    @Autowired
    private ActivityMapper activityMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> addGrantRateCoupon(RewardRecord newRecord, List<String[]> list, int activityId) throws ExcelException {
        List<String> result = new ArrayList<>();
        Set<UserRateCoupon> insertList = new TreeSet<>();
        newRecord.setGrantTime(new Timestamp(System.currentTimeMillis()));
        if (rewardMapper.insertSelective(newRecord) > 0) {
            for (int i = 0; i < list.size(); i++) {
                String[] row = list.get(i);
                if (row == null) {
                    continue;
                }
                Integer userId = rewardMapper.getUserIdbyPhone(row[0]);
                if (userId == null) {
                    result.add("行数" + (i + 2) + ",手机号：" + row[0] + "不存在");
                    continue;
                }
                RateCoupon rateCoupon = rateCouponMapper.getRateCouponByCode(row[1]);
                if (rateCoupon == null) {
                    result.add("行数" + (i + 2) + ",加息券：" + row[1] + "不存在");
                    continue;
                }
                UserRateCoupon userRateCoupon = new UserRateCoupon();
                userRateCoupon.setUserId(userId);// 用户id
                userRateCoupon.setRateCouponId(rateCoupon.getId());// 加息券id
                userRateCoupon.setActivityId(activityId);// 活动id
                userRateCoupon.setGrantId(newRecord.getId());// 发放记录id
                userRateCoupon.setGrantStatus((byte) 0);// 发放状态（0：未发放，1：已发放，2：发放失败）
                userRateCoupon.setCouponStatus((byte) 1);
//                userRateCoupon.setActivityCode(activityCode);// 活动编码
                insertList.add(userRateCoupon);
            }
            if (insertList.size() == 0) {
                result.add("没有可用的数据导入");
                throw new ExcelException("导入的excel有误", result);
            }
            if (result.isEmpty()) {
                if (insertList.size() > 50000) {
                    result.add("一次不能导入超过50000条记录");
                    throw new ExcelException("导入的excel有误", result);
                }
                userRateCouponMapper.batchInsert(insertList);
                return result;
            }
        }
        throw new ExcelException("导入的excel有误", result);
    }

    @Override
    public int getExcelHeaderActiviyId(MultipartFile file) throws Throwable {
        int activityId = 0;
        if (!StringHelper.isEmpty(file.getContentType())
                && (file.getContentType().equals("application/vnd.ms-excel") || file.getContentType()
                .equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = null;
            if (inputStream != null) {
                if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                    workbook = new XSSFWorkbook(inputStream);
                } else {
                    workbook = new HSSFWorkbook(inputStream);
                }
            } else {
                throw new ImportExcelException("文件里没有数据");
            }
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            // 验证标题
            Cell titleCell = headerRow.getCell(0);
            if (titleCell == null) {
                throw new ImportExcelException("导入的数据与模板不符");
            }
            titleCell.setCellType(Cell.CELL_TYPE_STRING);
            String header = getStringNoBlank(titleCell.getStringCellValue());
            if (!"活动编码".equals(header)) {
                throw new ImportExcelException("导入的数据与模板不符");
            }
            // 活动编码
            Cell activityCodeCell = headerRow.getCell(1);
            activityCodeCell.setCellType(Cell.CELL_TYPE_STRING);
            String activityCode = getStringNoBlank(activityCodeCell.getStringCellValue());
            // 验证活动编码是否存在
            Integer id = activityMapper.getActivityIdByCode(activityCode);
            if (id == null) {
                throw new ImportExcelException("活动编码不存在");
            }
            activityId = id;
        }
        return activityId;
    }

    @Override
    public List<RateCouponGrantStatistics> getRateCouponGrantStatistics(Integer grantId) {
        return userRateCouponMapper.getRateCouponGrantStatistics(grantId);
    }

    @Override
    public List<UserRateCoupon> findPager(RateCouponDetailForm rateCouponDetailForm, RowBounds bounds) {
        if (rateCouponDetailForm.getActivityCode() != null && "".equals(rateCouponDetailForm.getActivityCode().trim())) {
            rateCouponDetailForm.setActivityCode(null);
        }
        if (rateCouponDetailForm.getGrantStatus() != null) {
            if (rateCouponDetailForm.getGrantStatus() < 0) {
                rateCouponDetailForm.setGrantStatus(null);
            }
        }
        return userRateCouponMapper.findPager(rateCouponDetailForm, bounds);
    }

    @Override
    public List<UserRateCoupon> findUserRateCouponAll(RewardRecord rewardRecord, RateCouponDetailForm rateCouponDetailForm) {
        rateCouponDetailForm.setGrantId(rewardRecord.getId());
        List<UserRateCoupon> userRateCoupons = userRateCouponMapper.findAll(rateCouponDetailForm);
        return userRateCoupons;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void grantRateCoupons(RewardRecord rewardRecord, List<UserRateCoupon> userRateCoupons) {
        Message.loadProperties();
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        long nowMiniSecond = startTime.getTimeInMillis();
        // 批量插入T6124和T1041
        List<T6124> t6124sToInsert = new ArrayList<>();
        List<T1041> t1041sToInsert = new ArrayList<>();
        String symbol = ":";
        // 用户ID，用户拥有的加息券数量
        Map<String, Integer> userIdRateCouponCountMap = new HashMap<>();
        // 用户ID，用户拥有的最高加息幅度
        Map<Integer, BigDecimal> userIdMaxRateScoreMap = new HashMap<>();
        // 用户ID
        Integer userId;
        RateCoupon rateCouponByCode;
        String tempKey;
        for (UserRateCoupon item : userRateCoupons) {
            userId = item.getUserId();
            tempKey = userId.toString().concat(symbol).concat(item.getPhone());
            // 根据编码获取加息券
            rateCouponByCode = rateCouponMapper.getRateCouponByCode(item.getCouponCode());
            long endMiniSecond = nowMiniSecond + ((long) rateCouponByCode.getEffectDay() * 24 * 3600 * 1000) - 1000;
            item.setValidTime(new Timestamp(endMiniSecond));
            item.setGrantStatus((byte) 1);

            if (userIdRateCouponCountMap.containsKey(tempKey)) {
                userIdRateCouponCountMap.put(tempKey, userIdRateCouponCountMap.get(tempKey) + 1);
            } else {
                userIdRateCouponCountMap.put(tempKey, 1);
            }

            if (!userIdMaxRateScoreMap.containsKey(userId)) {
                userIdMaxRateScoreMap.put(userId, item.getScope());
            }
        }
        Set<Map.Entry<String, Integer>> entries = userIdRateCouponCountMap.entrySet();
        String key;
        Integer value;
        String[] grantNameArr = rewardRecord.getGrantName().split("_");
        String[] arr;
        Integer uId;
        String uPhone;
        // 加息幅度
        BigDecimal score;
        // 加息幅度转为百分比
        String scorePercent;
        // 加息幅度格式
        DecimalFormat scorePercentDF = new DecimalFormat("###.##");
        String percentStr = "%";

        for (Map.Entry<String, Integer> entry : entries) {
            key = entry.getKey();
            arr = key.split(symbol);
            uId = Integer.valueOf(arr[0]);
            uPhone = arr[1];
            value = entry.getValue();
            // 发站内信 标题
            T6123 t6123Letter = new T6123();
            t6123Letter.setF02(uId);
            t6123Letter.setF03("系统消息");
            t6123Letter.setF04(new Timestamp(System.currentTimeMillis()));
            t6123Letter.setF05("WD");
            rewardMapper.insertT6123(t6123Letter);

            // 发站内信 内容
            T6124 t6124Letter = new T6124();
            t6124Letter.setF01(t6123Letter.getF01());
            StringBuilder internalMessage = new StringBuilder(Message.get("internal.system.rateCoupon"));
            if (grantNameArr.length > 0) {
                internalMessage.replace(internalMessage.indexOf("{title}"),
                        internalMessage.indexOf("{title}") + "{title}".length(),
                        "" + grantNameArr[grantNameArr.length - 1]);
            } else {
                internalMessage.replace(internalMessage.indexOf("{title}"),
                        internalMessage.indexOf("{title}") + "{title}".length(), "" + "");
            }
            internalMessage.replace(internalMessage.indexOf("{num}"),
                    internalMessage.indexOf("{num}") + "{num}".length(), "" + value);
            // 加息幅度转为百分比
            score = userIdMaxRateScoreMap.get(uId).multiply(new BigDecimal(100));
            scorePercent = scorePercentDF.format(score).concat(percentStr);
            internalMessage.replace(internalMessage.indexOf("{scorePercent}"),
                    internalMessage.indexOf("{scorePercent}") + "{scorePercent}".length(), scorePercent);

            t6124Letter.setF02(internalMessage.toString());
            t6124sToInsert.add(t6124Letter);

            // 发短信 内容
            T1040 t1040Message = new T1040();
            t1040Message.setF02(0);
            StringBuilder shortMessage = new StringBuilder(Message.get("short.system.rateCoupon"));
            if (grantNameArr.length > 0) {
                shortMessage.replace(shortMessage.indexOf("{title}"),
                        shortMessage.indexOf("{title}") + "{title}".length(),
                        "" + grantNameArr[grantNameArr.length - 1]);
            } else {
                shortMessage.replace(shortMessage.indexOf("{title}"),
                        shortMessage.indexOf("{title}") + "{title}".length(), "" + "");
            }
            shortMessage.replace(shortMessage.indexOf("{num}"), shortMessage.indexOf("{num}") + "{num}".length(),
                    "" + value);
            shortMessage.replace(shortMessage.indexOf("{scorePercent}"),
                    shortMessage.indexOf("{scorePercent}") + "{scorePercent}".length(), scorePercent);
            t1040Message.setF03(shortMessage.toString());
            t1040Message.setF04(new Timestamp(System.currentTimeMillis()));
            t1040Message.setF05("W");
            rewardMapper.insertT1040(t1040Message);
            // 发短信 标题
            T1041 t1041Message = new T1041();
            t1041Message.setF01(t1040Message.getF01());
            t1041Message.setF02(uPhone);
            t1041sToInsert.add(t1041Message);
        }
        // 更新用户加息券
        userRateCouponMapper.batchUpdateUserRateCoupon(userRateCoupons);
        // 批量插入t6124
        rewardMapper.batchInsertT6124(t6124sToInsert);
        // 批量插入t1041
        rewardMapper.batchInsertT1041(t1041sToInsert);
        // 发放数量
        rewardRecord.setGrantCount(userRateCoupons.size());
        rewardMapper.cumsumRewardRecord(rewardRecord);
    }

    @Transactional
    @Override
    public String cancelRateCoupon(RewardRecord rewardRecord, RateCouponDetailForm rateCouponDetailForm) {
        rewardRecord.setGranted((byte) 3);
        rewardMapper.updateRewardRecord(rewardRecord);
        rateCouponDetailForm.setGrantId(rewardRecord.getId());
        List<UserRateCoupon> list = userRateCouponMapper.findAll(rateCouponDetailForm);
        for (UserRateCoupon item : list) {
            item.setGrantStatus((byte) 3);
        }
        if (!list.isEmpty()) {
            userRateCouponMapper.batchUpdateUserRateCoupon(list);
        }
        RewardRecord r = rewardMapper.getRewardRecordById(rewardRecord);
        return r.getGrantName() + "已作废";
    }

    private List<UserRateCoupon> getUserRateCouponByGrantIdAndPhone(UserRateCoupon item) {
        return userRateCouponMapper.getUserRateCouponByGrantIdAndPhone(item);
    }
}

