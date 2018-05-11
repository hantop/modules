package com.fenlibao.pms.controller.marketing.activity;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.model.pms.da.bidType.BidType;
import com.fenlibao.model.pms.da.marketing.activity.Activity;
import com.fenlibao.model.pms.da.marketing.activity.ActivityRateCoupon;
import com.fenlibao.model.pms.da.marketing.activity.form.ActivityForm;
import com.fenlibao.model.pms.da.reward.RateCoupon;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.bidtype.BidTypeService;
import com.fenlibao.service.pms.da.marketing.activity.ActivityService;
import com.fenlibao.service.pms.da.reward.rateCoupon.RateCouponService;

/**
 * 营销管理 / 活动设置
 */
@RestController
@RequestMapping("marketing/activity")
public class ActivityController {

    @Resource
    ActivityService activityService;
    
    @Autowired
    private RateCouponService rateCouponService;
    
    @Autowired
    private BidTypeService bidTypeService;
    
    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @RequiresPermissions("activity:view")
    @RequestMapping
    public ModelAndView list(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                             @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        ModelAndView view = new ModelAndView("marketing/activity/index");
        RowBounds bounds = new RowBounds(page, limit);
        List<Activity> activityList = activityService.getActivityList(bounds);	
        PageInfo<Activity> paginator = new PageInfo<>(activityList);
        view.addObject("list", activityList);
        view.addObject("paginator", paginator);
        view.addObject("total", paginator.getPageSize());
        return view;
    }


    /**
     * 新增编辑页面
     * @param id
     * @return
     */
	@RequiresPermissions(value = { "activity:create", "activity:edit" }, logical = Logical.OR)
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam(required = false, defaultValue = "0") int id,
			 @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
	            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
		 RowBounds bounds = new RowBounds(page, limit);
		ActivityForm activityForm = new ActivityForm();
		Activity activity = new Activity();
		List<Integer> ids = new ArrayList<>();
		String title = "新增";
		//默认今天
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDateStr=format.format(date);
        if (activity.getTimeStart() == null) {
        	activity.setTimeStart(DateUtil.StringToDate(nowDateStr, "yyyy-MM-dd HH:mm:ss"));
		}
        if (activity.getTimeEnd() == null) {
        	activity.setTimeEnd(DateUtil.StringToDate(nowDateStr, "yyyy-MM-dd HH:mm:ss"));
        }
        if (id > 0) {
			title = "编辑";
			activity = activityService.getActivityById(id);
			List<ActivityRateCoupon> couponList = activityService.getCouponListByActivityId(id);
			if (couponList.size() > 0) {
				for (ActivityRateCoupon activityRateCoupon : couponList) {
					ids.add(activityRateCoupon.getId());
				}
			}
			activityForm.setId(activity.getId());
			activityForm.setCode(activity.getCode());
			activityForm.setStatus(activity.getStatus());
			activityForm.setName(activity.getName());
			activityForm.setTimeStart(format.format(activity.getTimeStart()));
			activityForm.setTimeEnd(format.format(activity.getTimeEnd()));
			activityForm.setCouponList(couponList);
			for (ActivityRateCoupon activityRateCoupon : couponList) {
				 activityRateCoupon.setScope(activityRateCoupon.getScope().multiply(new BigDecimal(100)));
			}
			if (ids != null && ids.size() > 0) {
				activityForm.setCouponIds(ids);
			}else {
				activityForm.setCouponIds(null);
			}
		}
        List<RateCoupon> list = rateCouponService.findRateCoupons(null, bounds);
        PageInfo<RateCoupon> paginator = new PageInfo<>(list);
		return new ModelAndView().addObject("activityForm", activityForm).addObject("title", title).addObject("list", list).addObject("paginator", paginator);
	}
	
	/**
	 * 编辑操作
	 * @param activityForm
	 * @param result
	 * @return
	 */
	@RequiresPermissions(value = { "activity:create", "activity:edit" }, logical = Logical.OR)
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView edit(@RequestParam(required = false) String ids,@Valid @ModelAttribute("activityForm") ActivityForm activityForm, BindingResult result) {
		List<Integer> couponIds = new ArrayList<>();
		if (ids != null && ids != "") {
			String[] str1 = ids.split(",");
	        for (int i = 0; i < str1.length; i++) {
	        	couponIds.add(Integer.valueOf(str1[i])) ;
	        }
		}
		Activity activity = new Activity();
		activity.setCode(activityForm.getCode());
		activity.setName(activityForm.getName());
		activity.setTimeStart(DateUtil.StringToDate(activityForm.getTimeStart(), "yyyy-MM-dd HH:mm:ss"));
		activity.setTimeEnd(DateUtil.StringToDate(activityForm.getTimeEnd(), "yyyy-MM-dd HH:mm:ss"));
		if (couponIds.size() > 0) {
			activity.setCouponIds(couponIds);
		}else {
			activityForm.setCouponList(null);
		}
		String title = "新增";
		if (activityForm.getId() > 0) {
			activity.setId(activityForm.getId());
			title = "编辑";
		}
		if (result.hasErrors()) {
			return new ModelAndView("marketing/activity/edit").addObject("activity", activity)
					.addObject("title", title);
		}
		int id = 0;
		int idTemp = 0;
		try {
			 id = activityService.saveOrUpdateActivity(activity);
			 idTemp = activityService.addRateCoupons(activity.getId(), couponIds);
			 String url = "redirect:/marketing/activity/edit?success=" + (id > 0 ? true : false);
				ModelAndView modelAndView = new ModelAndView().addObject("title", title).addObject("activity", activity)
						.addObject("code", id);
				if (id < 0) {
					url = "marketing/activity/edit";
				}
				modelAndView.setViewName(url);
				return modelAndView;
		} catch (Exception e) {
			logger.error("[ActivityController.edit]", e);
			return new ModelAndView("marketing/activity/edit").addObject("activity", activity)
					.addObject("title", title).addObject("code", id);
		}
			
	}
	
	//@RequiresPermissions("couponList:view")
	@RequestMapping("/couponList")
	public ModelAndView couponList(
			@RequestParam(required = false, defaultValue = "0") int id,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
            RateCoupon rateCoupon) {
        RowBounds bounds = new RowBounds(page, limit);
        ModelAndView view = new ModelAndView("marketing/activity/couponList");
        List<ActivityRateCoupon> list = activityService.findAllRateCoupons(bounds);
        List<Integer> bidTypeIds;
        StringBuffer bidTypeSb;
        List<BidType> bidTypes = bidTypeService.getAllUsedBidType();
        Map<Integer, String> bidTypeMap = new HashMap<>();
        for (BidType bidType : bidTypes) {
            bidTypeMap.put(bidType.getId(), bidType.getTypeName());
        }
        for (ActivityRateCoupon activityRateCoupon : list) {
            bidTypeIds = this.bidTypeService.getBidTypeIdsByRateCouponId(activityRateCoupon.getId());
            if (bidTypeIds.size() > 0) {
            	bidTypeSb = new StringBuffer();
            	for (Integer bidTypeId : bidTypeIds) {
                    bidTypeSb.append(bidTypeMap.get(bidTypeId)).append(",");
                }
            	if(bidTypeSb.length() > 0) {
                    bidTypeSb.deleteCharAt(bidTypeSb.length() - 1);
                    activityRateCoupon.setBidTypeStr(bidTypeSb.toString());
                }
			}
            activityRateCoupon.setScope(activityRateCoupon.getScope().multiply(new BigDecimal(100)));
        }
        PageInfo<ActivityRateCoupon> paginator = new PageInfo<>(list);
        view.addObject("list", list).addObject("paginator", paginator);
        return view;
    }
	
}
