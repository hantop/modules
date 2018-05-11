package com.fenlibao.p2p.controller.v_4.v_4_0_0.invite;

import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.invite.MyAwordInfo;
import com.fenlibao.p2p.model.entity.invite.UserInviteInfo;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.invite.CountMyInviteInfoVO;
import com.fenlibao.p2p.model.vo.invite.InviteUrlInfoVO_131;
import com.fenlibao.p2p.model.vo.invite.MyAwordInfoVO;
import com.fenlibao.p2p.model.vo.invite.MyInviteVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.invite.InviteService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController("v_4_0_0/InviteController")
@RequestMapping(value = "invite", headers = APIVersion.v_4_0_0)
public class InviteController {

	private static final Logger logger= LogManager.getLogger(InviteController.class);
	
	@Resource
	InviteService inviteService;
	
	@Resource
	private UserInfoService userInfoService;
	
	@RequestMapping(value = "getInviteInfo", method = RequestMethod.GET)
    HttpResponse getInviteInfo(HttpServletRequest request,
    		@ModelAttribute BaseRequestForm  paramForm,
    		@RequestParam(required = true, value = "token") String token,
    		@RequestParam(required = true, value = "userId") String userId) throws Exception{
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
			inviteUrlVO.setFriendCircleInviteMsg(Config.get("friend.circle.invite.msg"));
			inviteUrlVO.setSlogan(Config.get("friend.share.invite.slogan"));
			
			String inviteShortUrl="";
			if(userInfo!=null){
				String userName = "";
				if(userInfo.getFullName()!=null && !"".equals(userInfo.getFullName())) {
                    userName = URLEncoder.encode(userInfo.getFullName(),"UTF-8");
				}
				String inviteUrl = Config.get("invite.url").replace("#{phone}",userInfo.getPhone()).replace("#{name}",userName );
				inviteShortUrl= CommonTool.genShortUrl(inviteUrl);
				inviteUrlVO.setInviteUrl(inviteShortUrl);
				inviteUrlVO.setNormalInviteUrl(inviteUrl);
			}
			inviteUrlVO.setIsSmrz(0);
			inviteUrlVO.setPhoneSmsInviteMsg(Config.get("phone.sms.invite.msg").replace("#{inviteShortUrl}", inviteShortUrl));
			if(userInfo!=null && userInfo.getFullName()!=null && !"".equals(userInfo.getFullName())){
				inviteUrlVO.setIsSmrz(1);
				inviteUrlVO.setPhoneSmsInviteMsg(Config.get("phone.smrz.sms.invite.msg").replace("#{userName}", userInfo.getFullName()).replace("#{inviteShortUrl}", inviteShortUrl));
			}
			
			response.setData(CommonTool.toMap(inviteUrlVO));
			return response;
		}catch(Exception ex){
			throw ex;
		}
	}
	
	/**
	 * 我的邀请人数和奖励统计
	 * @param request
	 * @param paramForm
	 * @param token
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getMyInviteInfo", method = RequestMethod.GET)
    HttpResponse myInviteInfo(HttpServletRequest request,
    		@ModelAttribute BaseRequestForm  paramForm,
    		@RequestParam(required = true, value = "token") String token,
    		@RequestParam(required = true, value = "userId") String userId) throws Exception{
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
				return response;
			}
			//获取用户邀请注册人数、奖励
			Map<String,Object> infoMap = inviteService.countMyInviteInfo(Integer.valueOf(userId));
			
			CountMyInviteInfoVO myInviteInfoVO = new CountMyInviteInfoVO();
			myInviteInfoVO.setAword(infoMap.get("amount")!=null?BigDecimal.valueOf(Double.parseDouble(infoMap.get("amount").toString())):BigDecimal.ZERO);
			myInviteInfoVO.setInviteUserRegNum(infoMap.get("inviteUserRegNum")!=null?Integer.valueOf(String.valueOf(infoMap.get("inviteUserRegNum"))):0);
			
			response.setData(CommonTool.toMap(myInviteInfoVO));
			return response;
		}catch(Exception ex){
			throw ex;
		}
	}
	/**
	 * 我邀请的用户注册信息列表 
	 * @param request
	 * @param paramForm
	 * @param token
	 * @param userId
	 * @param pageNo
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getInviteUserInfo", method = RequestMethod.GET)
    HttpResponse inviteUserInfo(HttpServletRequest request,@ModelAttribute BaseRequestForm  paramForm,
    			String token,String userId,String pageNo,String pagesize) throws Exception{
		    HttpResponse response = new HttpResponse();
			if(!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
				return response;
			}
			int pageNumIndex =1;
			int pageRows =InterfaceConst.PAGESIZE;
			try{
				if(StringUtils.isNotEmpty(pageNo) && Integer.valueOf(pageNo) > 0){
					pageNumIndex = Integer.valueOf(pageNo);
				}
				if(StringUtils.isNotEmpty(pagesize) && Integer.valueOf(pagesize) > 0){
					pageRows = Integer.valueOf(pagesize);
				}
			}catch(Exception e){
				logger.info(e);
			}
			
			List<MyInviteVO> voList = new ArrayList<MyInviteVO>();
			List<UserInviteInfo> infoList = inviteService.getMyInviteInfoList(Integer.valueOf(userId), pageNumIndex,pageRows);
			
			if(infoList!=null && infoList.size() > 0){
				for(UserInviteInfo info:infoList){
					MyInviteVO vo = new MyInviteVO();
					if(info.getUserName()!=null && !"".equals(info.getUserName())){
						vo.setUserName(info.getUserName().substring(0,1) + "**");
					}
					if(info.getUserCell()!=null && !"".equals(info.getUserCell())){
						vo.setUserCell(info.getUserCell().substring(0,info.getUserCell().length()-(info.getUserCell().substring(3)).length())+"****"+info.getUserCell().substring(7));
					}
					if(info.getUserInvestSum()==null){
						vo.setHasInvest(false);
					}else{
						vo.setHasInvest(info.getUserInvestSum().compareTo(BigDecimal.ZERO)>0);
					}
					vo.setUserRegTime(info.getUserRegTime().getTime()/1000);
					voList.add(vo);
				}
			}
			response.getData().put("inviteUserInfoList", voList);
			return response;
	}
	
	
	/**
	 * 我的奖励记录
	 * @param request
	 * @param paramForm
	 * @param token
	 * @param userId
	 * @param pageNo
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getMyAwordInfoList", method = RequestMethod.GET)
    HttpResponse getMyAwordInfoList(HttpServletRequest request, @ModelAttribute BaseRequestForm  paramForm,
    			String token,String userId,String pageNo,String pagesize) throws Exception{
			HttpResponse response = new HttpResponse();
			if(!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
				return response;
			}
			int pageNumIndex =1;
			int pageRows =InterfaceConst.PAGESIZE;
			try{
				if(StringUtils.isNotEmpty(pageNo) && Integer.valueOf(pageNo) > 0){
					pageNumIndex = Integer.valueOf(pageNo);
				}
				if(StringUtils.isNotEmpty(pagesize) && Integer.valueOf(pagesize) > 0){
					pageRows = Integer.valueOf(pagesize);
				}
			}catch(Exception e){
				logger.info(e);
			}
			
			List<MyAwordInfoVO> voList = new ArrayList<MyAwordInfoVO>();
			List<MyAwordInfo> infoList = inviteService.getMyAwordInfoList(Integer.valueOf(userId), pageNumIndex,pageRows);
			if(infoList!=null && infoList.size() > 0){
				for (MyAwordInfo myAwordInfo : infoList) {
					MyAwordInfoVO vo=new MyAwordInfoVO();
					vo.setAward(myAwordInfo.getAward());
					vo.setCountDate(myAwordInfo.getCountDate().getTime()/1000);
					vo.setInvestAmout(myAwordInfo.getInvestAmout());
					vo.setPhonenum(myAwordInfo.getPhonenum());
					if(myAwordInfo.getPhonenum()!=null && !"".equals(myAwordInfo.getPhonenum())){
						vo.setPhonenum(myAwordInfo.getPhonenum().substring(0,myAwordInfo.getPhonenum().length()-(myAwordInfo.getPhonenum().substring(3)).length())+"****"+myAwordInfo.getPhonenum().substring(7));
					}
					if(myAwordInfo.getRealname()!=null && !"".equals(myAwordInfo.getRealname())){
						vo.setRealname(myAwordInfo.getRealname().substring(0,1) + "**");
					}
					voList.add(vo);
				}
			}
			response.getData().put("awordInfoList", voList);
			return response;
	}
}