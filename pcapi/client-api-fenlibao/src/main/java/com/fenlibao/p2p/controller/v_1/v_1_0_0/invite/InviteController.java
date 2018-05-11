/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: InviteController.java 
 * @Prject: client-api-fenlibao
 * @Package: com.fenlibao.p2p.controller.v_1.v_1_3_0.invite 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-10 下午8:54:13 
 * @version: V1.1   
 */
package com.fenlibao.p2p.controller.v_1.v_1_0_0.invite;

import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.invite.BeInviterInfo;
import com.fenlibao.p2p.model.entity.invite.MyAwordInfo;
import com.fenlibao.p2p.model.entity.invite.UserInviteInfo;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.invite.*;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.invite.InviteService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import com.fenlibao.p2p.util.paginator.domain.Pager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/** 
 * @ClassName: InviteController 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-10 下午8:54:13  
 */

@RestController("v_1_0_0/InviteController")
@RequestMapping("invite")
public class InviteController {

	private static final Logger logger= LogManager.getLogger(InviteController.class);
	
	@Resource
	InviteService inviteService;
	
	@Resource
	private UserInfoService userInfoService;

	/**
	 * 邀请好友注册列表
	 * @param pageRequestForm
	 * @param token
	 * @param userId
	 * @param inviteStartTime
	 * @param inviteEndTime
	 * @param beInviterPhonenum
     * @return
     */
	@RequestMapping(value = "beinviterInfos", method = RequestMethod.GET)
	HttpResponse getBeinviterInfoList(PageRequestForm pageRequestForm,
								   String token,
								   String userId,
								   @RequestParam(required = false) String inviteStartTime,
								   @RequestParam(required = false) String inviteEndTime,
								   @RequestParam(required = false) String beInviterPhonenum) throws Exception {
		HttpResponse response = new HttpResponse();
		if (!pageRequestForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}
		if (StringUtils.isNotBlank(beInviterPhonenum)) {
			try {
				beInviterPhonenum = AES.getInstace().decrypt2(beInviterPhonenum);
			} catch (BadPaddingException e) {
				logger.error("[InviteController.getBeinviterInfoList]", e);
				response.setCodeMessage(ResponseCode.COMMON_DECRYPT_FAILURE.getCode(), ResponseCode.COMMON_DECRYPT_FAILURE.getMessage());
				return response;
			}
		}
		PageBounds pageBounds = new PageBounds(Integer.valueOf(pageRequestForm.getPage()), Integer.valueOf(pageRequestForm.getLimit()));
		List<BeInviterInfoVO> voList = new ArrayList<>();
		List<BeInviterInfo> infoList = inviteService.getBeinviterInfoList(Integer.valueOf(userId), inviteStartTime, inviteEndTime, beInviterPhonenum, pageBounds);
		Pager pager = new Pager(infoList);
		BeInviterInfoVO vo = null;
		if (infoList != null && infoList.size() > 0) {
			for (BeInviterInfo info : infoList) {
				vo = new BeInviterInfoVO();
				if(info.getRealname()!=null && !"".equals(info.getRealname())){
					vo.setRealname(info.getRealname().substring(0,1) + "**");
				}
				if(info.getPhonenum()!=null && !"".equals(info.getPhonenum())){
					vo.setPhonenum(info.getPhonenum().substring(0,info.getPhonenum().length()-(info.getPhonenum().substring(3)).length())+"****"+info.getPhonenum().substring(7));
				}
				if(info.getUserInvestSum()==null){
					vo.setHasInvest(false);
				}else{
					vo.setHasInvest(info.getUserInvestSum().compareTo(BigDecimal.ZERO)>0);
				}
				vo.setRegisterDate(info.getRegisterDate().getTime()/1000);
				voList.add(vo);
			}
		}
		pager.setItems(voList);
		response.setData(CommonTool.toMap(pager));
		return response;
	}

	/**
	 * 我的奖励记录
	 * @param pageRequestForm
	 * @param token
	 * @param userId
	 * @param inviteStartTime
	 * @param inviteEndTime
	 * @param beInviterPhonenum 被邀请人手机号(准确查询)
     * @return
     * @throws Exception
     */
	@RequestMapping(value = "awardInfos", method = RequestMethod.GET)
	HttpResponse getMyAwordInfoList(@ModelAttribute PageRequestForm pageRequestForm,
									String token,
									String userId,
									@RequestParam(required = false) String inviteStartTime,
									@RequestParam(required = false) String inviteEndTime,
									@RequestParam(required = false) String beInviterPhonenum) {
		HttpResponse response = new HttpResponse();
		if (!pageRequestForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}
		if (StringUtils.isNotBlank(beInviterPhonenum)) {
			try {
				beInviterPhonenum = AES.getInstace().decrypt2(beInviterPhonenum);
			} catch (BadPaddingException e) {
				logger.error("[InviteController.getMyAwordInfoList]", e);
				response.setCodeMessage(ResponseCode.COMMON_DECRYPT_FAILURE.getCode(), ResponseCode.COMMON_DECRYPT_FAILURE.getMessage());
				return response;
			}
		}
		PageBounds pageBounds = new PageBounds(Integer.valueOf(pageRequestForm.getPage()), Integer.valueOf(pageRequestForm.getLimit()));
		List<MyAwordInfoVO> voList = new ArrayList<>();
		List<MyAwordInfo> infoList = null;
		try {
			infoList = inviteService.getMyAwordInfoList(Integer.valueOf(userId), inviteStartTime, inviteEndTime, beInviterPhonenum, pageBounds);
			Pager pager = new Pager(infoList);
			if (infoList != null && infoList.size() > 0) {
				for (MyAwordInfo myAwordInfo : infoList) {
					MyAwordInfoVO vo = new MyAwordInfoVO();
					vo.setAward(myAwordInfo.getAward());
					vo.setCountDate(myAwordInfo.getCountDate().getTime() / 1000);
					vo.setInvestAmount(myAwordInfo.getInvestAmount());
					vo.setPhonenum(myAwordInfo.getPhonenum());
					if (myAwordInfo.getPhonenum() != null && !"".equals(myAwordInfo.getPhonenum())) {
						vo.setPhonenum(myAwordInfo.getPhonenum().substring(0, myAwordInfo.getPhonenum().length() - (myAwordInfo.getPhonenum().substring(3)).length()) + "****" + myAwordInfo.getPhonenum().substring(7));
					}
					if (myAwordInfo.getRealname() != null && !"".equals(myAwordInfo.getRealname())) {
						vo.setRealname(myAwordInfo.getRealname().substring(0, 1) + "**");
					}
					voList.add(vo);
				}
			}
			pager.setItems(voList);
			response.setData(CommonTool.toMap(pager));
		} catch (Exception e) {
			logger.error("[InviteController.getMyAwordInfoList]", e);
			response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
		}
		return response;
	}


	/**
	 * 分享邀请信息
	 * @param request
	 * @param paramForm
	 * @param token
	 * @param userId
	 * @return
     * @throws Exception
     */
	@RequestMapping(value = "inviteInfo", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse getInviteInfo(HttpServletRequest request,
    		@ModelAttribute BaseRequestForm  paramForm,
    		@RequestParam(required = true, value = "token") String token,
    		@RequestParam(required = true, value = "userId") String userId) {
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
				return response;
			}
			UserInfo userInfo = this.userInfoService.getUser(null, null, userId);

			InviteUrlInfoVO_131 inviteUrlVO = new InviteUrlInfoVO_131();
			inviteUrlVO.setInvitePicUrl(Config.get("invite.pic.url"));
			inviteUrlVO.setFriendShareInviteTitle(Config.get("friend.share.invite.title"));
			inviteUrlVO.setFriendShareInviteMsg(Config.get("friend.share.invite.msg"));
//			inviteUrlVO.setFriendCircleInviteMsg(Config.get("friend.circle.invite.msg"));

			String inviteShortUrl="";
			if(userInfo!=null){
				String userName = "";
				if(userInfo.getFullName()!=null && !"".equals(userInfo.getFullName())) {
                    userName = URLEncoder.encode(userInfo.getFullName(),"UTF-8");
				}
				String inviteUrl = Config.get("invite.url").replace("#{phone}",userInfo.getPhone()).replace("#{name}",userName );
				inviteShortUrl= CommonTool.genShortUrl(inviteUrl);
				inviteUrlVO.setInviteUrl(inviteShortUrl);
			}
			inviteUrlVO.setIsSmrz(0);
//			inviteUrlVO.setPhoneSmsInviteMsg(Config.get("phone.sms.invite.msg").replace("#{inviteShortUrl}", inviteShortUrl));
			if(userInfo!=null && userInfo.getFullName()!=null && !"".equals(userInfo.getFullName())){
				inviteUrlVO.setIsSmrz(1);
//				inviteUrlVO.setPhoneSmsInviteMsg(Config.get("phone.smrz.sms.invite.msg").replace("#{userName}", userInfo.getFullName()).replace("#{inviteShortUrl}", inviteShortUrl));
			}
			response.setData(CommonTool.toMap(inviteUrlVO));
		}catch(Exception e){
			logger.error("[InviteController.getInviteInfo]", e);
			response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
		}
		return response;
	}

    /**
     * 邀请信息统计
     * @param request
     * @param paramForm
     * @param token
     * @param userId
     * @return
     */
	@RequestMapping(value = "inviteInfoStatistics", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse getMyInviteInfoStatistics(HttpServletRequest request,
    		@ModelAttribute BaseRequestForm paramForm,
    		@RequestParam(required = true, value = "token") String token,
    		@RequestParam(required = true, value = "userId") String userId) throws Exception {
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}
		//获取用户邀请注册人数、投资人数
		MyInviteInfoVO myInviteInfoVO = inviteService.getMyInviteInfo(Integer.valueOf(userId));
		response.setData(CommonTool.toMap(myInviteInfoVO));
        return response;
    }
	
	@RequestMapping(value = "getInviteUserInfo", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse inviteUserInfo(HttpServletRequest request,
    		@ModelAttribute BaseRequestForm  paramForm,
    		@RequestParam(required = true, value = "token") String token,
    		@RequestParam(required = true, value = "userId") String userId,
    		@RequestParam(required = false, value = "pageNum") String pageNum) throws Exception{
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
				return response;
			}
			int pageNumIndex =1;
			try{
				if(StringUtils.isNotEmpty(pageNum) && Integer.valueOf(pageNum) > 0){
					pageNumIndex = Integer.valueOf(pageNum);
				}
			}catch(Exception e){
				logger.info(e);
			}
			
			List<UserInviteInfoVO> voList = new ArrayList<UserInviteInfoVO>();
			
			List<UserInviteInfo> infoList = inviteService.getUserInviteInfoList(Integer.valueOf(userId), pageNumIndex);
			if(infoList!=null && infoList.size() > 0){
				for(UserInviteInfo info:infoList){
					UserInviteInfoVO vo = new UserInviteInfoVO();
					if(info.getUserName()!=null && !"".equals(info.getUserName())){
						vo.setUserName(info.getUserName().substring(0,1) + "**");
					}
					if(info.getUserCell()!=null && !"".equals(info.getUserCell())){
						vo.setUserCell(info.getUserCell().substring(0,info.getUserCell().length()-(info.getUserCell().substring(3)).length())+"****"+info.getUserCell().substring(7));
					}
					vo.setUserInvestSum(info.getUserInvestSum());
					vo.setUserRegTime(info.getUserRegTime().getTime()/1000);
					voList.add(vo);
				}
			}
			
			response.getData().put("inviteUserInfoList", voList);
			return response;
		}catch(Exception ex){
			throw ex;
		}
	}
}