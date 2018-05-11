package com.fenlibao.pms.controller.reward.ratecoupon;

import com.alibaba.fastjson.JSONObject;
import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.exception.ImportExcelException;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.common.pms.util.tool.StringHelper;
import com.fenlibao.model.pms.da.bidType.BidType;
import com.fenlibao.model.pms.da.reward.*;
import com.fenlibao.model.pms.da.reward.form.RateCouponDetailForm;
import com.fenlibao.model.pms.da.reward.form.RateCouponEditForm;
import com.fenlibao.model.pms.da.reward.form.RateCouponForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.bidtype.BidTypeService;
import com.fenlibao.service.pms.da.exception.ExcelException;
import com.fenlibao.service.pms.da.reward.common.RewardService;
import com.fenlibao.service.pms.da.reward.common.async.GrantRateCouponAsyncService;
import com.fenlibao.service.pms.da.reward.rateCoupon.RateCouponService;
import com.fenlibao.service.pms.da.reward.rateCoupon.UserRateCouponService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加息券
 *
 * Created by chenzhixuan on 2016/8/24.
 */
@RestController
@RequestMapping("reward/rateCoupon")
public class RateCouponController {
    private static final Logger LOG = LoggerFactory.getLogger(RateCouponController.class);

    private static int REWARD_TYPE = 4;// 导入记录加息券
    private static int GRANT_STATUS = 0;// 发放状态
    
    @Autowired
    private RateCouponService rateCouponService;
    @Autowired
    private UserRateCouponService userRateCouponService;
    @Autowired
    private BidTypeService bidTypeService;
    @Autowired
    private RewardService rewardService;
    @Autowired
    private GrantRateCouponAsyncService asyncService;

    @RequestMapping(value="/async-grant", method = RequestMethod.POST)
    public @ResponseBody Object asyncGrantRateCoupon(@RequestBody RewardRecord rewardRecord){
        boolean dispose = true;
        RateCouponDetailForm rateCouponDetailForm = new RateCouponDetailForm();
        rateCouponDetailForm.setRewardType((byte) REWARD_TYPE);// 导入时记录，奖励类型(1:体验金 2：现金红包 3：返现券 4：加息券)
        rateCouponDetailForm.setGrantStatus((byte) GRANT_STATUS);// 发放的状态
        String message = "加息券正在发放，请注意发放状态";

        synchronized (this){
            //查询当前是否有在发放的红包
            Integer running = rewardService.getInServiceRewards();
            if(running != null && running > 0){
                dispose = false;
                message = "加息券后台发放中，请等待发送完成后再继续发放！！！";
            }

            if (dispose){
                //查询是否已经发放过了
                Byte granted = rewardService.getGrantedById(rewardRecord.getId());
                if (granted != null && granted == 0) {
                    //将发放状态修改为发放中...
                    RewardRecord doInService = new RewardRecord();
                    doInService.setId(rewardRecord.getId());
                    doInService.setInService((byte) 1);
                    rewardService.updateRewardRecord(doInService);
                }else {
                    dispose = false;
                    message = "该加息券已经发放";
                }
            }
        }

        //异步的发放红包操作
        if(dispose){
            rewardRecord = rewardService.getRewardRecordById(rewardRecord);
            asyncService.multiThreadGrantRateCoupon(rewardRecord, rateCouponDetailForm);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        return jsonObject;
    }

    /**
     * 发放加息券详情记录表
     *
     * @param page
     * @param limit
     * @return
     */
    @RequiresPermissions("rateCouponDetail:view")
    @RequestMapping("/rate-coupon-detial-list")
    public ModelAndView rateCouponDetialList(RateCouponDetailForm rateCouponDetailForm,
                                              @RequestParam(required = false, defaultValue = "false") boolean granted,
                                              @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                              @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        RowBounds bounds = new RowBounds(page, limit);
        // 2:注册现金红包;2:生日;3:充值;4:投资倍数;5:投资额度
        rateCouponDetailForm.setRewardType((byte) REWARD_TYPE);// 导入时记录，奖励类型(1:体验金 2：现金红包 3：返现券 4：加息券)
        List<UserRateCoupon> list = userRateCouponService.findPager(rateCouponDetailForm, bounds);
        PageInfo<UserRateCoupon> paginator = new PageInfo<>(list);
        List<RateCouponGrantStatistics> statisticsList = null;
        if (granted) {
            statisticsList = userRateCouponService.getRateCouponGrantStatistics(rateCouponDetailForm.getGrantId());
        }
        return new ModelAndView("reward/rateCoupon/rate-coupon-detial-list")
                .addObject("list", list).addObject("paginator", paginator).addObject("userRedpackets", rateCouponDetailForm)
                .addObject("statisticsList", statisticsList).addObject("granted", granted);
    }

    @RequiresPermissions("rateCouponEdit:view")
    @RequestMapping("/rate-coupon-type-list")
    public ModelAndView rateCouponList(
            RateCouponForm rateCouponForm,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        RowBounds bounds = new RowBounds(page, limit);
        List<RateCoupon> list = rateCouponService.findRateCoupons(rateCouponForm, bounds);
        List<Integer> bidTypeIds;
        List<BidType> bidTypes = bidTypeService.getAllUsedBidType();
        StringBuffer bidTypeSb;
        Map<Integer, String> bidTypeMap = new HashMap<>();
        for (BidType bidType : bidTypes) {
            bidTypeMap.put(bidType.getId(), bidType.getTypeName());
        }
        for (RateCoupon rateCoupon : list) {
            bidTypeSb = new StringBuffer();
            bidTypeIds = this.bidTypeService.getBidTypeIdsByRateCouponId(rateCoupon.getId());
            for (Integer bidTypeId : bidTypeIds) {
                bidTypeSb.append(bidTypeMap.get(bidTypeId)).append(",");
            }
            if(bidTypeSb.length() > 0) {
                bidTypeSb.deleteCharAt(bidTypeSb.length() - 1);
                rateCoupon.setBidTypeStr(bidTypeSb.toString());
            }
            rateCoupon.setScope(rateCoupon.getScope().multiply(new BigDecimal(100)));
            // 如果被发送名单和活动关联则不能修改
            boolean canOperate = rateCouponService.isCanOperate(rateCoupon.getId());
            rateCoupon.setCanOperate(canOperate);
        }
        PageInfo<RateCoupon> paginator = new PageInfo<>(list);
        return new ModelAndView("reward/rateCoupon/rate-coupon-type-list")
                .addObject("list", list)
                .addObject("paginator", paginator)
                .addObject("rateCouponForm", rateCouponForm);
    }

    /**
     * 加息券管理首页
     *
     * @param page
     * @param limit
     * @return
     */
    @RequiresPermissions("rateCoupon:view")
    @RequestMapping
    public ModelAndView index(RewardRecordCondition rewardRecordCondition,
                                    @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                    @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        rewardRecordCondition.setRewardType((byte) REWARD_TYPE);
        RowBounds bounds = new RowBounds(page, limit);
        // 默认当天
        String nowDateStr = DateUtil.getDate(DateUtil.nowDate());
        if (StringHelper.isNull(rewardRecordCondition.getStartDate())) {
            rewardRecordCondition.setStartDate(nowDateStr);
        }
        if (StringHelper.isNull(rewardRecordCondition.getEndDate())) {
            rewardRecordCondition.setEndDate(nowDateStr);
        }
        List<RewardRecord> list = rewardService.getRewardRecords(rewardRecordCondition, bounds);
        //查询当前是否有在发放的红包
        Integer running = rewardService.getInServiceRewards();
        PageInfo<RewardRecord> paginator = new PageInfo<>(list);
        return new ModelAndView("reward/rateCoupon/index").addObject("list", list).addObject("running",running)
                .addObject("paginator", paginator).addObject("rewardRecordCondition", rewardRecordCondition);
    }

    @RequiresPermissions("rateCoupon:cancel")
    @RequestMapping("rate-coupon-cancel")
    public @ResponseBody Object cancelRateCoupon(@RequestBody RewardRecord rewardRecord) {
        String message = "";
        RateCouponDetailForm rateCouponDetailForm = new RateCouponDetailForm();
        rateCouponDetailForm.setRewardType((byte) REWARD_TYPE);// 导入时记录，奖励类型(1:体验金 2：现金红包 3：返现券 4：加息券)
        try {
            message = userRateCouponService.cancelRateCoupon(rewardRecord, rateCouponDetailForm);
        } catch (Exception e) {
            message = "作废失败";
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        return jsonObject;
    }

    @RequiresPermissions("rateCouponDetail:export")
    @RequestMapping("/rate-coupon-detial-export")
    public void backVoucherDetialExport(RateCouponDetailForm rateCouponDetailForm, HttpServletResponse response) {
        RowBounds bounds = new RowBounds();
        rateCouponDetailForm.setRewardType((byte) REWARD_TYPE);// 导入时记录，奖励类型(1:体验金 2：现金红包 3：返现券 4：加息券)
        List<UserRateCoupon> list = userRateCouponService.findPager(rateCouponDetailForm, bounds);
        for (UserRateCoupon userRateCoupon : list) {
            switch (userRateCoupon.getGrantStatus()) {
                case 0:
                    userRateCoupon.setGrantStatusName("未发放");
                    break;
                case 1:
                    userRateCoupon.setGrantStatusName("已发放");
                    break;
                case 2:
                    userRateCoupon.setGrantStatusName("发放失败");
                    break;
                case 3:
                    userRateCoupon.setGrantStatusName("已作废");
                    break;
                default:
                    userRateCoupon.setGrantStatusName("错误状态");
                    break;
            }
        }
        POIUtil.export(response, new String[] { "发送手机号", "加息券类型", "发送状态" },
                new String[] { "phone", "couponCode", "grantStatusName" }, list, "加息券发送详情");
    }

    /**
     * 导入加息券列表信息
     *
     * @param rewardRecord
     * @param file
     * @return
     */
    @RequiresPermissions("rateCoupon:import")
    @RequestMapping(value = "/import-rate-coupon", method = RequestMethod.POST)
    public @ResponseBody Object importRateCoupon(RewardRecord rewardRecord, @RequestParam("file") MultipartFile file)
            throws Throwable {
        String operator = rewardRecord.getOperator();
        String headers[] = { "发送手机号", "加息券类型" };
        int activityId;
        List<String[]> list = null;
        List<String> result = new ArrayList<>();
        try {
            activityId = userRateCouponService.getExcelHeaderActiviyId(file);
            list = POIUtil.getImportedData(file, headers, 1);
        } catch (ImportExcelException e) {
            result.add(e.getMessage());
            return result;
        }
        String grantName = file.getOriginalFilename();
        if (grantName.indexOf(".") > 0) {
            grantName = grantName.substring(0, grantName.lastIndexOf("."));
        }
        // 开始插入记录
        RewardRecord newRecord = new RewardRecord();
        newRecord.setGrantName(grantName);
        newRecord.setOperator(operator);
        newRecord.setGranted((byte) 0);// 未发放
        newRecord.setRewardType((byte) REWARD_TYPE);// 加息券

        try {
            result = userRateCouponService.addGrantRateCoupon(newRecord, list, activityId);
        } catch (ExcelException e) {
            return e.getErrors();
        }
        return result;
    }


    @RequiresPermissions("rateCouponEdit:delete")
    @RequestMapping(value = "rate-coupon-type-remove", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ResponseBody
    ResponseEntity rateCouponRemove(@RequestBody @NotEmpty List<Integer> ids, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        boolean flag = rateCouponService.rateCouponRemove(ids) > 0 ? true : false;
        return new ResponseEntity<>(flag, flag ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    /**
     * 进入编辑页面，进行新增或修改操作
     *
     * @param id
     * @return
     */
    @RequiresPermissions(value={"rateCouponEdit:create","rateCouponEdit:edit"},logical= Logical.OR)
    @RequestMapping(value = "/rate-coupon-type-edit", method = RequestMethod.GET)
    public ModelAndView rateCouponEdit(@RequestParam(required = false, defaultValue = "0") int id) {
        RateCoupon rateCoupon = new RateCoupon();
        List<BidType> bidTypes = bidTypeService.getAllUsedBidType();
        List<Integer> bidTypeIds = null;
        String title = "新增";
        if (id > 0) {
            title = "编辑";
            rateCoupon = rateCouponService.getRateCouponById(id);
            rateCoupon.setScope(rateCoupon.getScope().multiply(new BigDecimal(100)));
            bidTypeIds = this.bidTypeService.getBidTypeIdsByRateCouponId(id);
            if (rateCoupon.getMinInvestDay() != null || rateCoupon.getMaxInvestDay() != null) {
                rateCoupon.setInvestDeadLineType(true);
            }
        } else {
            bidTypeIds = new ArrayList<>();
            for (BidType bidType : bidTypes) {
                bidTypeIds.add(bidType.getId());
            }
        }
        rateCoupon.setBidTypeIds(bidTypeIds);
        return new ModelAndView("reward/rateCoupon/rate-coupon-type-edit")
                .addObject("rateCoupon", rateCoupon)
                .addObject("bidTypes", bidTypes)
                .addObject("title", title);
    }

    /**
     * 新增或编辑加息券信息
     *
     * @param rateCouponEditForm
     * @param result
     * @return
     */
    @RequiresPermissions(value={"rateCouponEdit:create","rateCouponEdit:edit"},logical=Logical.OR)
    @RequestMapping(value = "/rate-coupon-type-edit", method = RequestMethod.POST)
    public ModelAndView rateCouponEdit(@Valid @ModelAttribute("rateCouponEditForm") RateCouponEditForm rateCouponEditForm, BindingResult result) {
        String title = "新增";
        int code = -1;
        if (rateCouponEditForm.getId() > 0) {
            title = "编辑";
        }
        List<BidType> bidTypes = bidTypeService.getAllUsedBidType();
        String editUrl = "reward/rateCoupon/rate-coupon-type-edit";
        if (result.hasErrors()) {
            LOG.error("提交数据格式有错误");
            return new ModelAndView(editUrl)
                    .addObject("rateCoupon", rateCouponEditForm)
                    .addObject("title", title)
                    .addObject("bidTypes", bidTypes);
        }
        // 如果被发送名单和活动关联则不能修改
        boolean canOperate = rateCouponService.isCanOperate(rateCouponEditForm.getId());
        if (!canOperate) {
            code = -2;
        } else {
            rateCouponEditForm.setScope(rateCouponEditForm.getScope().divide(new BigDecimal(100)));
            if (!rateCouponEditForm.isInvestDeadLineType()) {
                rateCouponEditForm.setMinInvestDay(null);
                rateCouponEditForm.setMaxInvestDay(null);
            }
            // 新增或更新加息券
            int affectCount = rateCouponService.saveOrUpdateRateCoupon(rateCouponEditForm);
            if (affectCount > 0) {
                code = 1;
            }
        }
        String url = "redirect:/" + editUrl + "?success=" + (code > 0 ? true : false);
        ModelAndView modelAndView = new ModelAndView()
                .addObject("title", title)
                .addObject("code", code)
                .addObject("bidTypes", bidTypes)
                .addObject("rateCoupon", rateCouponEditForm);
        if (code < 0) {
            url = editUrl;
        }
        modelAndView.setViewName(url);
        return modelAndView;
    }
}
