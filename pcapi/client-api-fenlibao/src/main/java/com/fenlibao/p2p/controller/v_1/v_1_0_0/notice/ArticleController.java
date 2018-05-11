/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 */
package com.fenlibao.p2p.controller.v_1.v_1_0_0.notice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fenlibao.p2p.model.entity.notice.Article;
import com.fenlibao.p2p.model.entity.notice.ArticleDetail;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.notice.ArticleDetailVo;
import com.fenlibao.p2p.model.vo.notice.ArticleVo;
import com.fenlibao.p2p.service.notice.NoticeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import com.fenlibao.p2p.util.paginator.domain.Pager;


/** 媒体报道
 * @author: junda.feng2
 */
@RestController("v_1_0_0/ArticleController")
@RequestMapping("info")
public class ArticleController {
	
	private static final Logger logger= LogManager.getLogger(ArticleController.class);

	@Resource
	NoticeService noticeService;
	
	/**
	 * 1.5.1媒体报道列表
	 * @param request
	 * @param paramForm
	 * @param page
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "getArticleList", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse getArticleList(HttpServletRequest request,
    		@ModelAttribute BaseRequestForm  paramForm,Integer page,Integer limit) throws Exception{
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
				return response;
			}
			List<ArticleVo> voList = new ArrayList<ArticleVo>();
			PageBounds pageBounds=new PageBounds(page, limit);
			List<Article> infoList = noticeService.getArticleList(new HashMap<>(), pageBounds);
			Pager pager = new Pager(infoList);
			if(infoList != null && infoList.size() >0){
				for(Article info:infoList) {
					ArticleVo vo = new ArticleVo();
					vo.setArticleId(info.getArticleId());
					vo.setArticleTitle(info.getArticleTitle());
					String url=FileUtils.getPicURL(info.getImgcode(),Config.get("static.file.url"));
					vo.setImgUrl(url);
					vo.setIntroduction(info.getIntroduction());
					vo.setPublishTime(info.getPublishTime());
					vo.setSource(info.getSource());
					vo.setChannel(info.getChannel());
					//拼详情链接
					vo.setArticleDetailUrl("/info/getArticle?clientType=7&articleId="+info.getArticleId());
					voList.add(vo);
				}
			}
			pager.setItems(voList);
			response.setData(CommonTool.toMap(pager));
			return response;
		}catch(Exception ex){
			throw ex;
		}
	}
	
	/**
	 * 1.5.2.	媒体报道详细
	 * @param  paramForm
	 * @param  articleId
	 * @return response
	 * @throws Exception
	 */
	@RequestMapping(value = "getArticle", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse getArticle(@ModelAttribute BaseRequestForm  paramForm,String articleId) throws Exception{
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate() || StringUtils.isEmpty(articleId)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
				return response;
			}
			ArticleDetailVo vo = new ArticleDetailVo();
			ArticleDetail infoDetail = noticeService.getArticleDetail(Integer.valueOf(articleId));
			if(infoDetail!=null){
				String contentString = com.fenlibao.p2p.util.StringHelper.format(infoDetail.getContent(), Config.get("static.file.url"));
				vo.setContent(contentString);
				vo.setIntroduction(infoDetail.getIntroduction());
				vo.setPublishTime(infoDetail.getPublishTime());
				vo.setSource(infoDetail.getSource());
				vo.setArticleId(infoDetail.getArticleId());
				vo.setArticleTitle(infoDetail.getArticleTitle());
				vo.setKeyword(infoDetail.getKeyword());
				String url=FileUtils.getPicURL(infoDetail.getImgcode(),Config.get("static.file.url"));
				vo.setImgUrl(url);
				vo.setChannel(infoDetail.getChannel());

				vo.setPreArticleId(infoDetail.getPreArticleId());
				vo.setPreArticleTitle(infoDetail.getPreArticleTitle());
				vo.setNextArticleId(infoDetail.getNextArticleId());
				vo.setNextArticleTitle(infoDetail.getNextArticleTitle());
			}
			response.setData(CommonTool.toMap(vo));
			return response;
		}catch(Exception ex){
			throw ex;
		}
	}
	
	
}