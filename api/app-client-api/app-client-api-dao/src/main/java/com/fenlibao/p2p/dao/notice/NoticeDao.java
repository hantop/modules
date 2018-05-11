/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: t.java 
 * @Prject: app-client-api-dao
 * @Package: com.fenlibao.p2p.dao.notice 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-10 下午9:34:17 
 * @version: V1.1   
 */
package com.fenlibao.p2p.dao.notice;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.notice.*;

public interface NoticeDao {

	/** 
	 * @param noticeType 
	 * @Title: getNoticeList 
	 * @Description: 获取公告列表
	 * @return
	 * @return: List<NoticeInfoVO>
	 */

	List<NoticeInfo> getNoticeList(String noticeType,Integer pageNo, Integer pagesize);

	List<NoticeInfo> getNoticeList(String noticeType, Date pageDate, Integer limit);


	/** 
	 * @Title: getNoticeDetail 
	 * @Description: 获取公告详情
	 * @param noticeId
	 * @return
	 * @return: int
	 */
	NoticeInfoDetail getNoticeDetail(int noticeId);


	/** 
	 * @param ArticleType
	 * @param pageBounds
	 * @Title: getArticleList
	 * @Description: 获取新闻报道列表
	 * @return
	 * @return: List<ArticleInfoVO>
	 */
	List<Article> getArticleList(Map map);

	ArticleDetail getArticleDetail(int articleId);

	List<KnowMore> getKnowMoreList(Map map);

	KnowMore getKnowMoreDetail(int id);


	List<KnowEarly> geKnowEarlyList(Map map);

	KnowEarly getKnowEarlyDetail(int id);

    long getNoticeLastUpdateTime();

    int checkNoticeNew(long updateTime);

	List<ReportDeatil> getReportList(Map map);
}
