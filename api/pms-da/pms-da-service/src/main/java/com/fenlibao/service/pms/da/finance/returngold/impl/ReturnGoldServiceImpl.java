package com.fenlibao.service.pms.da.finance.returngold.impl;

import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.dao.pms.da.finance.returngold.ReturnGoldMapper;
import com.fenlibao.model.pms.da.finance.form.ReturnGoldForm;
import com.fenlibao.model.pms.da.finance.vo.ReturnExperienceGoldVO;
import com.fenlibao.service.pms.da.finance.returngold.ReturnGoldService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Louis Wang on 2016/1/12.
 */
@Service
public class ReturnGoldServiceImpl implements ReturnGoldService {
    @Resource
    private ReturnGoldMapper returnGoldMapper;

    @Override
    public List<ReturnExperienceGoldVO> getExperienceGoldCostList(ReturnGoldForm returnGoldForm, RowBounds bounds) {
        List<ReturnExperienceGoldVO> experienceGolds = null;

        Map<String, Object> paramMap = setParam(returnGoldForm);
        if(!paramMap.isEmpty() && paramMap.size() > 0){
            //查询体验金金额
            experienceGolds = returnGoldMapper.getExperienceGoldCostList(paramMap, bounds);

            if(experienceGolds != null && experienceGolds.size() > 0){
                for (int i = 0; i <experienceGolds.size() ; i++) {
                    ReturnExperienceGoldVO exp = experienceGolds.get(i);
                    if(exp != null){
                        String activityCode = exp.getActivityCode();
                        if(StringUtils.isEmpty(activityCode)){
                            int ii = i+1;
                            exp.setActivityCode("系统自动发放" + ii);
                        }
                    }

                    String sy = exp.getUserspay();
                    if(!StringUtils.isEmpty(sy)){
                        BigDecimal tmpSy = new BigDecimal(exp.getUserspay());
                        tmpSy = tmpSy.setScale(2,BigDecimal.ROUND_DOWN);
                        exp.setUserspay(tmpSy.toString());
                    }else{
                        exp.setUserspay("0.00");
                    }
                }
            }
        }
        return experienceGolds;
    }

    private Map<String, Object> setParam(ReturnGoldForm returnGoldForm) {
        Map<String, Object> paramMap = new HashMap<>();
        if(!StringUtils.isEmpty(returnGoldForm)){
            paramMap.put("goldCode", returnGoldForm.getGoldCode());

            String beginDate = returnGoldForm.getStartTime();
            String endDate = returnGoldForm.getEndTime();
            Date costStartDate;
            Date costEndDate;
            /**
             * 这个统计的时间是开始得到收益的时间，我们计算收益的时间是T+1,
             * 所以查询范围内的体验金应该要提前一天
             */
            if(!StringUtils.isEmpty(beginDate)){
                costStartDate = DateUtil.StringToDate(beginDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
                costStartDate = DateUtil.dateAdd(costStartDate,-1);
                paramMap.put("costStartTime",costStartDate);
            }else {
               /* Date yesterdayDate = DateUtil.dateAdd(DateUtil.nowDate(), -1);
                String yesterdayDateStr = DateUtil.getDate(yesterdayDate);
                costStartDate = DateUtil.StringToDate(yesterdayDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
                paramMap.put("costStartTime",costStartDate);
                returnGoldForm.setStartTime(yesterdayDateStr);*/
                String defaultStartDate =  defaultDate(0);
                Date pageStartDate = DateUtil.StringToDate(defaultStartDate, "yyyy-MM-dd HH:mm:ss");
                costStartDate = DateUtil.dateAdd(pageStartDate,-1);
                paramMap.put("costStartTime",costStartDate);
                returnGoldForm.setStartTime(DateUtil.getDate(pageStartDate));
            }
            if(!StringUtils.isEmpty(endDate)){
                costEndDate = DateUtil.StringToDate(endDate + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
                costEndDate = DateUtil.dateAdd(costEndDate,-1);
                paramMap.put("costEndTime",costEndDate);
            }else {
                //    Date yesterdayDate = DateUtil.dateAdd(DateUtil.nowDate(), -1);
                /*String yesterdayDateStr = DateUtil.getDate(DateUtil.nowDate());
                costEndDate = DateUtil.StringToDate(yesterdayDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
                paramMap.put("costEndTime",costEndDate);
                returnGoldForm.setEndTime(yesterdayDateStr);*/
                String defaultEndDate =  defaultDate(1);
                Date pageEndDate = DateUtil.StringToDate(defaultEndDate, "yyyy-MM-dd HH:mm:ss");
                costEndDate = DateUtil.dateAdd(pageEndDate,-1);
                paramMap.put("costEndTime",costEndDate);
                returnGoldForm.setEndTime(DateUtil.getDate(pageEndDate));
            }
        }
        return paramMap;
    }

    /**
     * 默认自然月
     * @param i 0 开始 1 结束
     */
    private String defaultDate(int i) {
        Calendar calTime = Calendar.getInstance();
        //设置天数为-1天，表示当月减一天即为上一个月的月末时间
        calTime.set(Calendar.DAY_OF_MONTH, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if(i == 0){
            calTime.set(calTime.get(Calendar.YEAR), calTime.get(Calendar.MONTH),1,0,0,0);
        }else if(i == 1){
            calTime.set(calTime.get(Calendar.YEAR), calTime.get(Calendar.MONTH),calTime.get(Calendar.DAY_OF_MONTH),23,59,59);
        }

        return sdf.format(calTime.getTime());
    }

    @Override
    public List<ReturnExperienceGoldVO> getExperienceGolddetails(Integer id, String startTime, String endTime, String telphone, RowBounds bounds) {
        List<ReturnExperienceGoldVO> experienceGolds = null;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("gid",id);
        if(!StringUtils.isEmpty(telphone)){
            paramMap.put("telphone",telphone);
        }
        if(!StringUtils.isEmpty(startTime)){
            Date costStartDate = DateUtil.StringToDate(startTime + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            costStartDate = DateUtil.dateAdd(costStartDate,-1);
            paramMap.put("costStartTime",costStartDate);
        }
        if(!StringUtils.isEmpty(endTime)){
            Date costEndDate = DateUtil.StringToDate(endTime + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            costEndDate = DateUtil.dateAdd(costEndDate,-1);
            paramMap.put("costEndTime",costEndDate);
        }
        experienceGolds = returnGoldMapper.getExperienceGolddetails(paramMap,bounds);
        return experienceGolds;
    }

    @Override
    public BigDecimal getExperienceGoldTotal(ReturnGoldForm returnGoldForm) {
        Map<String, Object> paramMap = setParam(returnGoldForm);
        BigDecimal tmpSy = returnGoldMapper.getExperienceGoldTotal(paramMap);
        if(!StringUtils.isEmpty(tmpSy)){
            tmpSy = tmpSy.setScale(2,BigDecimal.ROUND_DOWN);
        }else{
            tmpSy = new BigDecimal(0);
        }
        return tmpSy;
    }
}
