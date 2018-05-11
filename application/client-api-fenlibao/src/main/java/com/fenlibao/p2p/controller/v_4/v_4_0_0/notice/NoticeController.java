package com.fenlibao.p2p.controller.v_4.v_4_0_0.notice;

import com.fenlibao.p2p.model.entity.notice.NoticeInfo;
import com.fenlibao.p2p.model.entity.notice.NoticeInfoDetail;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.notice.NoticeInfoDetailVO;
import com.fenlibao.p2p.model.vo.notice.NoticeInfoVO;
import com.fenlibao.p2p.service.notice.NoticeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController("v_4_0_0/NoticeController")
@RequestMapping(value = "notice", headers = APIVersion.v_4_0_0)
public class NoticeController {
	
	private static final Logger logger= LogManager.getLogger(NoticeController.class);

	@Resource
	NoticeService noticeService;
	
	@RequestMapping(value = "getNoticeListForFront", method = RequestMethod.GET)
    HttpResponse getNoticeListForFront(HttpServletRequest request,
    		@ModelAttribute BaseRequestForm  paramForm) throws Exception{
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
				return response;
			}
			List<NoticeInfoVO> voList = new ArrayList<NoticeInfoVO>();
			
			List<NoticeInfo> infoList = noticeService.getNoticeList("GG",1,3); //首页轮播只显示3条公告
			if(infoList != null && infoList.size() >0){
				for(NoticeInfo info:infoList) {
					NoticeInfoVO vo = new NoticeInfoVO();
					vo.setNoticeId(info.getNoticeId());
					vo.setNoticeTitle(info.getNoticeTitle());
					vo.setNoticeType(info.getNoticeType());
					vo.setCreatetime(DateUtil.dateToTimestampToSec(info.getCreatetime()));
					vo.setNoticeDetailUrl(Config.get("noticeDetail.url")+info.getNoticeId());
					voList.add(vo);
				}
			}
			response.getData().put("noticeList", voList);
			return response;
		}catch(Exception ex){
			throw ex;
		}
	}
	
	@RequestMapping(value = "getNoticeList", method = RequestMethod.GET)
    HttpResponse getNoticeList(HttpServletRequest request,
    		@ModelAttribute BaseRequestForm  paramForm,String noticeType,Integer pageNo,Integer pagesize) throws Exception{
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
				return response;
			}
			List<NoticeInfoVO> voList = new ArrayList<NoticeInfoVO>();

			List<NoticeInfo> infoList = noticeService.getNoticeList(noticeType,pageNo,pagesize);
			long updateTime = noticeService.getNoticeLastUpdateTime();
			if(infoList != null && infoList.size() >0){
				for(NoticeInfo info:infoList) {
					NoticeInfoVO vo = new NoticeInfoVO();
					vo.setNoticeId(info.getNoticeId());
					vo.setNoticeType(info.getNoticeType());
					vo.setNoticeTitle(info.getNoticeTitle());
					vo.setCreatetime(info.getCreatetime().getTime()/1000);
					vo.setNoticeDetailUrl(Config.get("noticeDetail.url")+info.getNoticeId());
//					String s=info.getSummary();
//					s=subString(s,40);//20中文或40字符
					vo.setSummary(info.getSummary());
					voList.add(vo);
				}
			}
			response.getData().put("updateTime", updateTime);
			response.getData().put("noticeList", voList);

			return response;
		}catch(Exception ex){
			throw ex;
		}
	}
	
	
	public static String subString(String s,int l){
		if(StringUtils.isBlank(s)){
			return "";
		}
		int index=0;
		int count=0;
		for(int i=0;i<s.length();i++){
			char c=s.charAt(i);
			if(isChinese(c)){
				count++;
			}
			count++;
			if(count>=l){
				index=i;
				break;
			}
			index=i;
		}
		String result=s.substring(0,index+1);
		if(count>=l)result+="···";
		return result;
	}
	
	
	public static  boolean isChinese(char c) {   
	    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  
	    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
	            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
	            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
	            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
	            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
	            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
	        return true;  
	    }  
	    return false;  
	}  

	@RequestMapping(value = "getNoticeInfo", method = RequestMethod.GET)
    HttpResponse getNoticeInfo(HttpServletRequest request,
    		@ModelAttribute BaseRequestForm  paramForm,
    		@RequestParam(required = true, value = "noticeId") String noticeId) throws Exception{
		
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate() || StringUtils.isEmpty(noticeId)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
				return response;
			}
			NoticeInfoDetailVO vo = new NoticeInfoDetailVO();
			NoticeInfoDetail infoDetail = noticeService.getNoticeDetail(Integer.valueOf(noticeId));
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
				
				String contentString = com.fenlibao.p2p.util.StringHelper.format(infoDetail.getContent(), Config.get("static.file.url"));
				
				vo.setContent(contentString);
			}
			response.setData(CommonTool.toMap(vo));
			return response;
		}catch(Exception ex){
			throw ex;
		}
	}

	@RequestMapping(value = "checkNoticeNew", method = RequestMethod.GET)
	HttpResponse checkNoticeNew(HttpServletRequest request,
							   @ModelAttribute BaseRequestForm  paramForm,@RequestParam(required = false, value="updateTime") String updateTime) throws Exception{
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
				return response;
			}
			long timstamp = 0;
			int showFlag = 0;
			if (StringUtils.isEmpty(updateTime)) {
				showFlag = 1;
			}else{
				try {
					timstamp = Long.valueOf(updateTime);
				} catch (Throwable throwable) {
					response.getData().put("showFlag", showFlag);
					return response;
				}
				showFlag = noticeService.checkNoticeNew(timstamp);
			}

			response.getData().put("showFlag", showFlag);

			return response;
		}catch(Exception ex){
			throw ex;
		}
	}


}