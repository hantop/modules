/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: AccessoryInfoService.java 
 * @Prject: app-client-api-service
 * @Package: com.fenlibao.p2p.service 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-9 下午2:42:58 
 * @version: V1.1   
 */
package com.fenlibao.p2p.service.notice;

import java.util.List;
import java.util.Map;

import com.dimeng.p2p.S62.entities.T6232;
import com.fenlibao.p2p.model.entity.notice.*;

/** 
 * @ClassName: AccessoryInfoService 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-9 下午2:42:58  
 */
public interface NoticeService {

	/** 
	 * @Title: getNoticeList 
	 * @Description: 获取公告列表前台
	 * @return
	 * @return: List<NoticeInfoVO>
	 */
	List<NoticeInfo> getNoticeList(String noticeType,Integer pageNo,Integer pagesize);
	/**
	 * @Title: getNoticeList
	 * @Description: 获取公告列表前台
	 * @return
	 * @return: List<NoticeInfoVO>
	 */
	List<NoticeInfo> getNoticeList_(String noticeType, String timestamp, Integer limit);

	/** 
	 * @Title: getNoticeDetail 
	 * @Description: 获取公告详情
	 * @param noticeId
	 * @return
	 * @return: int
	 */
	NoticeInfoDetail getNoticeDetail(int noticeId);
	
	/** 
	 * @Title: getArticleList 
	 * @Description: 获取媒体报道列表前台
	 * @param pageBounds
	 * @return: List<Article>
	 */
	List<Article> getArticleList(Integer pageNo,Integer pagesize);
	
	ArticleDetail getArticleDetail(int articleId);

	/**
	 * 网贷知多点
	 * @param pageNo
	 * @param pagesize
     * @return
     */
	List<KnowMore> getKnowMoreList(Integer pageNo, Integer pagesize);

	KnowMore getKnowMoreDetail(int id);

	/**
	 * 网贷早知道
	 * @param pageNo
	 * @param pagesize
     * @return
     */
	List<KnowEarly> geKnowEarlyList(Integer pageNo,Integer pagesize);

	KnowEarly getKnowEarlyDetail(int id);

    long getNoticeLastUpdateTime();

	int checkNoticeNew(long updateTime);

	/**
	 * 获取报告列表
	 * @param pageNo
	 * @param pagesize
	 * @return
	 */
	List<ReportDeatil> getRepoetList(Integer pageNo,Integer pagesize,String type);

	/**
	 * @Title: getArticleList
	 * @Description: 获取媒体报道列表前台
	 * @param pageBounds
	 * @return: List<Article>
	 */
	List<Article> getArticleListNew(Integer pageNo,Integer pagesize,String type);
}
