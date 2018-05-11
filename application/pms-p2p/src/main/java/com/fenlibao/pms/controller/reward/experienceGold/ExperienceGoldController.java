package com.fenlibao.pms.controller.reward.experienceGold;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.exception.ImportExcelException;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.common.pms.util.tool.StringHelper;
import com.fenlibao.model.pms.da.reward.ExperienceGold;
import com.fenlibao.model.pms.da.reward.ExperienceGoldStatistics;
import com.fenlibao.model.pms.da.reward.RewardRecord;
import com.fenlibao.model.pms.da.reward.RewardRecordCondition;
import com.fenlibao.model.pms.da.reward.UserExperience;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.reward.cashRedPacket.UserRedpacketsService;
import com.fenlibao.service.pms.da.reward.common.RewardService;
import com.fenlibao.service.pms.da.reward.experienceGold.ExperienceGoldService;

@Controller
@RequestMapping("reward/experienceGold")
public class ExperienceGoldController {

    private static final Logger LOG = LoggerFactory.getLogger(ExperienceGoldController.class);

    @Resource
    private RewardService rewardService;

    @Resource
    private ExperienceGoldService experienceGoldService;
    
    @Autowired
   	private UserRedpacketsService userRedpacketsService;


    /**
     * 查询体验金记录
     *
     * @param page
     * @param limit
     * @return
     */
    @RequiresPermissions("experienceGold:view")
    @RequestMapping
    public ModelAndView getExperienceGoldRecords(
            RewardRecordCondition rewardRecordCondition,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit
    ) {
        rewardRecordCondition.setRewardType((byte) 1);
		// 默认当天
		String nowDateStr = DateUtil.getDate(DateUtil.nowDate());
		if(StringHelper.isNull(rewardRecordCondition.getStartDate())){
			rewardRecordCondition.setStartDate(nowDateStr);
		}
		if(StringHelper.isNull(rewardRecordCondition.getEndDate())){
			rewardRecordCondition.setEndDate(nowDateStr);
		}
        RowBounds bounds = new RowBounds(page, limit);
        List<RewardRecord> list = rewardService.getRewardRecords(rewardRecordCondition, bounds);
        PageInfo<RewardRecord> paginator = new PageInfo<>(list);
        return new ModelAndView("reward/experienceGold/index").addObject("list", list).addObject("paginator", paginator).addObject("rewardRecordCondition",rewardRecordCondition);
    }

    /**
     * 导入体验金记录
     *
     * @param file
     * @return
     * @throws Throwable
     */
    @RequiresPermissions("experienceGold:import")
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public
    @ResponseBody
    Object importExperienceGold(RewardRecord rewardRecord, @RequestParam("file") MultipartFile file) throws Throwable {
        List<String> result = new ArrayList<>();
        String grantName = file.getOriginalFilename();
        if (grantName.indexOf(".") > 0) {
            grantName = grantName.substring(0, grantName.lastIndexOf("."));
        }

        String operator = rewardRecord.getOperator();
        String headers[] = {"发送手机号", "体验金类型"};
        List<String[]> importList = null;
        try {
            importList = POIUtil.getImportedData(file, headers);
        } catch (ImportExcelException e) {
            String fileError = e.getMessage();
            result.add(fileError);
            return result;
        }
        //开始插入记录
        RewardRecord newRecord = new RewardRecord();
        newRecord.setGrantName(grantName);
        newRecord.setOperator(operator);
        newRecord.setGranted((byte) 0);//未发放
        newRecord.setRewardType((byte) 1);//体验金
        long miniSecond = System.currentTimeMillis();
        Timestamp datetime = new Timestamp(miniSecond);
        newRecord.setGrantTime(datetime);//未发送取导入时间
        result.addAll(experienceGoldService.insertExperienceGoldRecords(newRecord, importList));
        return result;
    }

    @RequiresPermissions("experienceGoldDetail:view")
    @RequestMapping("/detail")
    public ModelAndView getExperienceGoldRecordDetail(
            UserExperience userExperience,
            @RequestParam(required = false, defaultValue="false") boolean granted,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        RowBounds bounds = new RowBounds(page, limit);
        List<UserExperience> list = experienceGoldService.getExperienceGoldRecordDetail(userExperience, bounds);
        PageInfo<UserExperience> paginator = new PageInfo<>(list);
        List<ExperienceGoldStatistics> statisticsList=null;
        if(granted){
        	statisticsList=experienceGoldService.experienceGoldStatistics(userExperience.getGrantId());
        }
        return findAllActivityCode(new ModelAndView("reward/experienceGold/detail")
                .addObject("list", list)
                .addObject("paginator", paginator)
                .addObject("userExperience", userExperience), userExperience)
        		.addObject("statisticsList", statisticsList)
        		.addObject("granted", granted);
    }


    private ModelAndView findAllActivityCode(ModelAndView modelAndView, UserExperience userExperience) {
        List<UserExperience> activityCodes = this.experienceGoldService.findAllActivityCode(userExperience);
        modelAndView.addObject("experienceList", activityCodes);
        return modelAndView;
    }

    @RequiresPermissions("experienceGoldDetail:export")
    @RequestMapping("/detailExport")
    public void exportExperienceGoldRecordDetail(UserExperience userExperience, HttpServletResponse response) {
        RowBounds bounds = new RowBounds();
        List<UserExperience> list = experienceGoldService.getExperienceGoldRecordDetail(userExperience, bounds);
        List<DetailExport> list2 = new ArrayList<DetailExport>();
        for (UserExperience item : list) {
            DetailExport d = new DetailExport();
            d.phone = item.getPhone();
            d.activityCode = item.getActivityCode();
            byte status = item.getGrantStatus();
            if (status == 0) {
                d.grantStatus = "未发送";
            } else if (status == 1) {
                d.grantStatus = "已成功";
            } else if (status == 2) {
                d.grantStatus = "发送失败";
            } else if (status == 3) {
                d.grantStatus = "已作废";
            }
            list2.add(d);
        }
        String headers[] = {"用户手机号", "体验金类型", "发送状态"};
        String fieldNames[] = {"phone", "activityCode", "grantStatus"};
        POIUtil.export(response, headers, fieldNames, list2, "体验金发送详情");
    }

    private class DetailExport {
        String phone;
        String activityCode;
        String grantStatus;
    }

    @RequiresPermissions("experienceGold:grant")
    @RequestMapping(value = "/grant", consumes = {"application/json; charset=utf-8"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public Object grantExperienceGold(
            @RequestBody RewardRecord rewardRecord
    ) {
        String message = "发送完成";
        RewardRecord temp = userRedpacketsService.getRewardRecordById(rewardRecord);
        if (temp != null && temp.getInService() == (byte) 1) {
			message = "体验金发放中,请勿重新操作!";
		}else if (temp != null && temp.getGranted() == (byte) 1) {
			message = "已发放过，不可重复发放";
		}else {
	        try {
	            synchronized (this) {
	                message = experienceGoldService.grantExperienceGold(rewardRecord);
	            }
	        } catch (Exception e) {
	        	message = "发送失败";
	        	rewardRecord.setInService((byte) 0);
				rewardRecord.setGranted((byte) 0);
				userRedpacketsService.updateRewardRecord(rewardRecord);
	            e.printStackTrace();
	        }
		}
        JSONObject result = new JSONObject();
        result.put("message", message);
        return result;
    }

    @RequiresPermissions("experienceGold:cancel")
    @RequestMapping("/cancel")
    public
    @ResponseBody
    Object cancelExperienceGold(
            @RequestBody RewardRecord rewardRecord
    ) {
        String message = "";
        try {
            message = experienceGoldService.cancelExperienceGold(rewardRecord);
        } catch (Exception e) {
            message = "作废失败";
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        return jsonObject;
    }

    /********************************************************
     * 体验金类型 增删该查
     ************************************************/

    @RequiresPermissions("experienceGoldEdit:view")
    @RequestMapping("/experience-gold-list")
    public ModelAndView listChild(
            ExperienceGold experienceGold,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        RowBounds bounds = new RowBounds(page, limit);
        List<ExperienceGold> list = experienceGoldService.findExperienceGoldPager(experienceGold, bounds);
        PageInfo<ExperienceGold> paginator = new PageInfo<>(list);
        return new ModelAndView("reward/experienceGold/experience-gold-list")
                .addObject("list", list)
                .addObject("paginator", paginator)
                .addObject("experienceGold", experienceGold);
    }

    /**
     * 进入编辑页面，进行新增或修改操作
     *
     * @param id
     * @return
     */
    @RequiresPermissions(value={"experienceGoldEdit:create","experienceGoldEdit:edit"},logical=Logical.OR)
    @RequestMapping(value = "/experience-gold-edit", method = RequestMethod.GET)
    public ModelAndView experienceGoldEdit(@RequestParam(required = false, defaultValue = "0") int id) {
        ExperienceGold experienceGold = new ExperienceGold();
        String title = "新增";
        if (id > 0) {
            title = "编辑";
            experienceGold = experienceGoldService.getExperienceGoldById(id);
        }
        return new ModelAndView("reward/experienceGold/experience-gold-edit")
                .addObject("experienceGold", experienceGold)
                .addObject("title", title);
    }

    /**
     * 新增或编辑体验金信息
     *
     * @param experienceGold
     * @param result
     * @return
     */
    @RequiresPermissions(value={"experienceGoldEdit:create","experienceGoldEdit:edit"},logical=Logical.OR)
    @RequestMapping(value = "/experience-gold-edit", method = RequestMethod.POST)
    public ModelAndView experienceGoldEdit(@Valid @ModelAttribute("experienceGold") ExperienceGold experienceGold, BindingResult result) {
        String title = "新增";
        if (experienceGold.getId() > 0) {
            title = "编辑";
        }
        String editUrl = "reward/experienceGold/experience-gold-edit";
        if (result.hasErrors()) {
            LOG.error("提交数据格式有错误");
            return new ModelAndView(editUrl)
                    .addObject("experienceGold", experienceGold)
                    .addObject("title", title);
        }
        int id = experienceGoldService.experienceGoldEdit(experienceGold);
        String url = "redirect:/" + editUrl + "?success=" + (id > 0 ? true : false);
        ModelAndView modelAndView = new ModelAndView().addObject("title", title).addObject("code", id);
        if (id < 0) {
            url = editUrl;
        }
        modelAndView.setViewName(url);
        return modelAndView;
    }

    @RequiresPermissions("experienceGoldEdit:delete")
    @RequestMapping(value = "experience-gold-remove", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ResponseBody
    ResponseEntity experienceGoldRemove(@RequestBody @NotEmpty List<Integer> experienceGolds, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        boolean flag = experienceGoldService.experienceGoldRemove(experienceGolds) > 0 ? true : false;
        return new ResponseEntity<>(flag, flag ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

}
