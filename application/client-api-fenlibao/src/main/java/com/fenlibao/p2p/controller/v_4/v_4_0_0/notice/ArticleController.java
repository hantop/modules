package com.fenlibao.p2p.controller.v_4.v_4_0_0.notice;

import com.fenlibao.p2p.model.entity.notice.Article;
import com.fenlibao.p2p.model.entity.notice.ArticleDetail;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.notice.ArticleDetailVo;
import com.fenlibao.p2p.model.vo.notice.ArticleVo;
import com.fenlibao.p2p.service.notice.NoticeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** 媒体报道
 */
@RestController("v_4_0_0/ArticleController")
@RequestMapping(value = "info", headers = APIVersion.v_4_0_0)
public class ArticleController {
	
	private static final Logger logger= LogManager.getLogger(ArticleController.class);

	@Resource
	NoticeService noticeService;
	
	/**
	 * 1.5.1媒体报道列表
	 * @param request
	 * @param paramForm
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "getArticleList", method = RequestMethod.GET)
    HttpResponse getArticleList(HttpServletRequest request,
    		@ModelAttribute BaseRequestForm  paramForm,Integer pageNo,Integer pagesize) throws Exception{
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
				return response;
			}
			
			List<ArticleVo> voList = new ArrayList<ArticleVo>();
			List<Article> infoList = noticeService.getArticleList(pageNo,pagesize);
			
			if(infoList != null && infoList.size() >0){
				for(Article info:infoList) {
					ArticleVo vo = new ArticleVo();
					vo.setArticleId(info.getArticleId());
					vo.setArticleTitle(info.getArticleTitle());
					String url=FileUtils.getPicURL(info.getImgcode(),Config.get("static.file.url"));
					vo.setImgUrl(url);
					vo.setIntroduction(info.getIntroduction());
					vo.setPublishTime(info.getPublishTime().getTime()/1000);
					vo.setSource(info.getSource());
					//拼详情链接
					vo.setArticleDetailUrl(Config.get("articleDetail.url")+info.getArticleId());
					vo.setChannel(info.getChannel());
					voList.add(vo);
				}
			}
			Map map=new HashMap<>();
			map.put("items",voList);
			response.setData(map);
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
	@RequestMapping(value = "getArticle", method = RequestMethod.GET)
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
				vo.setPublishTime(infoDetail.getPublishTime().getTime()/1000);
				vo.setSource(infoDetail.getSource());
				vo.setArticleId(infoDetail.getArticleId());
				vo.setArticleTitle(infoDetail.getArticleTitle());
				String url=FileUtils.getPicURL(infoDetail.getImgcode(),Config.get("static.file.url"));
				vo.setImgUrl(url);
				vo.setChannel(infoDetail.getChannel());
			}
			response.setData(CommonTool.toMap(vo));
			return response;
		}catch(Exception ex){
			throw ex;
		}
	}
	
	
}