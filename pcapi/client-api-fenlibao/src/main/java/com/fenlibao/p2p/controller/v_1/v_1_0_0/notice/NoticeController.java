/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: t.java 
 * @Prject: client-api-fenlibao
 * @Package: com.fenlibao.p2p.controller.v_1.v_1_3_0.notice 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-10 下午9:31:45 
 * @version: V1.1   
 */
package com.fenlibao.p2p.controller.v_1.v_1_0_0.notice;

import com.fenlibao.p2p.model.entity.notice.NoticeInfo;
import com.fenlibao.p2p.model.entity.notice.NoticeInfoDetail;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.notice.NoticeInfoDetailVO;
import com.fenlibao.p2p.model.vo.notice.NoticeInfoVO;
import com.fenlibao.p2p.service.notice.NoticeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.paginator.domain.Order;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import com.fenlibao.p2p.util.paginator.domain.Pager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/** 
 * @ClassName: InviteController 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-10 下午8:54:13  
 */

@RestController("v_1_0_0/NoticeController")
@RequestMapping("notice")
public class NoticeController {
	
	private static final Logger logger= LogManager.getLogger(NoticeController.class);

	@Resource
	NoticeService noticeService;
	
	@RequestMapping(value = "getNoticeListForFront", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse getNoticeListForFront(HttpServletRequest request,
    		@ModelAttribute BaseRequestForm  paramForm) throws Exception{
		HttpResponse response = new HttpResponse();
		if(!paramForm.validate()){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		List<NoticeInfoVO> voList = new ArrayList<NoticeInfoVO>();
		String noticeType = "GG";
		String sortString = "createTime.desc";//如果你想排序的话逗号分隔可以排序多列
		PageBounds pageBounds = new PageBounds(1, 2 , Order.formString(sortString));
		List<NoticeInfo> infoList = noticeService.getNoticeList(noticeType, pageBounds);
		for(NoticeInfo info:infoList) {
			NoticeInfoVO vo = new NoticeInfoVO();
			vo.setNoticeId(info.getNoticeId());
			//首页只过滤公告且只返回2条
			vo.setNoticeTitle(info.getNoticeTitle());
			vo.setNoticeType(info.getNoticeType());
			vo.setCreatetime(DateUtil.dateToTimestampToSec(info.getCreatetime()));
			vo.setNoticeDetailUrl(Config.get("noticeDetail.url")+info.getNoticeId());
			voList.add(vo);
		}
		response.getData().put("noticeList", voList);
		return response;
	}
	
	@RequestMapping(value = "getNoticeList", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse getNoticeList(HttpServletRequest request,
    		@ModelAttribute PageRequestForm pageRequestForm,
    		@RequestParam(required = false, value = "noticeType") String noticeType) throws Exception{
		HttpResponse response = new HttpResponse();

		if(!pageRequestForm.validate()){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		List<NoticeInfoVO> voList = new ArrayList<NoticeInfoVO>();
		String sortString = "forder.asc,createTime.desc";//如果你想排序的话逗号分隔可以排序多列
		PageBounds pageBounds = new PageBounds(Integer.valueOf(pageRequestForm.getPage()), Integer.valueOf(pageRequestForm.getLimit()) , Order.formString(sortString));

		List<NoticeInfo> infoList = noticeService.getNoticeList(noticeType,pageBounds);

		Pager pager = new Pager(infoList);
		if(infoList != null && infoList.size() >0){
			for(NoticeInfo info:infoList) {
				NoticeInfoVO vo = new NoticeInfoVO();
				vo.setNoticeId(info.getNoticeId());
				vo.setNoticeType(info.getNoticeType());
				vo.setNoticeTitle(info.getNoticeTitle());
				vo.setCreatetime(DateUtil.dateToTimestampToSec(info.getCreatetime()));
				vo.setNoticeDetailUrl(Config.get("noticeDetail.url")+info.getNoticeId());
				voList.add(vo);
			}
		}
		pager.setItems(voList);
		response.setData(CommonTool.toMap(pager));
		return response;
	}
	

	@RequestMapping(value = "getNoticeInfo", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse getNoticeInfo(HttpServletRequest request,
    		@ModelAttribute BaseRequestForm  paramForm,
    		@RequestParam(required = true, value = "noticeId") String noticeId,
			@RequestParam(required = false, value = "noticeType") String noticeType) throws Exception{
		
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate() || StringUtils.isEmpty(noticeId)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
				return response;
			}
			NoticeInfoDetailVO vo = new NoticeInfoDetailVO();
			NoticeInfoDetail infoDetail = noticeService.getNoticeDetail(noticeType, Integer.valueOf(noticeId));
			if(infoDetail!=null){
				vo.setNoticeId(infoDetail.getNoticeId());
				vo.setNoticeTitle(infoDetail.getNoticeTitle());
				if(infoDetail.getNoticeType()!=null && "XT".equals(infoDetail.getNoticeType())){
					vo.setNoticeType(0);
				}
				if(infoDetail.getNoticeType()!=null && "HD".equals(infoDetail.getNoticeType())){
					vo.setNoticeType(1);
				}
				vo.setCreateTime(infoDetail.getCreateTime().getTime()/1000);
				vo.setKeyword(infoDetail.getKeyword());
				vo.setIntroduction(infoDetail.getIntroduction());
				String contentString = com.fenlibao.p2p.util.StringHelper.format(infoDetail.getContent(), Config.get("static.file.url"));
				
				vo.setContent(contentString);

				vo.setPreNoticeId(infoDetail.getPreNoticeId());
				vo.setPreNoticeTitle(infoDetail.getPreNoticeTitle());
				vo.setNextNoticeId(infoDetail.getNextNoticeId());
				vo.setNextNoticeTitle(infoDetail.getNextNoticeTitle());
			}
			response.setData(CommonTool.toMap(vo));
			return response;
		}catch(Exception ex){
			throw ex;
		}
	}
	
}