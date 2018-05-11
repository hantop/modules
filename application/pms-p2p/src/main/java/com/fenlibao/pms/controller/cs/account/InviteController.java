package com.fenlibao.pms.controller.cs.account;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.model.pms.da.cs.Invite;
import com.fenlibao.model.pms.da.cs.form.PageForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.cs.InviteService;

@RestController
@RequestMapping("cs/account")
public class InviteController {
	private static final Logger LOG = LoggerFactory.getLogger(InviteController.class);
	
	@Resource
	private InviteService inviteService;
	
	@RequestMapping("invite")
    public Object inviteList(
    	    @Valid PageForm pageForm, BindingResult result,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int pageNum,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit
    ) {
		List<Invite> list=null;
		if(!result.hasErrors()){
	        RowBounds bounds = new RowBounds(pageNum+1, limit);
			// 默认昨天
//			Date yesterdayDate = DateUtil.dateAdd(DateUtil.nowDate(), -1);
//			String yesterdayDateStr = DateUtil.getDate(yesterdayDate);
//			if(StringHelper.isNull(pageForm.getStartDate())){
//				pageForm.setStartDate(yesterdayDateStr);
//			}
//			if(StringHelper.isNull(pageForm.getEndDate())){
//				pageForm.setEndDate(yesterdayDateStr);
//			}
	        list = inviteService.inviteList(pageForm,bounds);
		}
        PageInfo<Invite> paginator = new PageInfo<>(list);
        return paginator;
    }
}
