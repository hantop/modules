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

import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.notice.*;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

public interface NoticeDao {

	/**
	 * @param noticeType
	 * @param pageBounds
	 * @Title: getNoticeList
	 * @Description: 获取公告列表
	 * @return
	 * @return: List<NoticeInfoVO>
	 */
	List<NoticeInfo> getNoticeList(String noticeType, PageBounds pageBounds);

	/**
	 * @Title: getNoticeDetail
	 * @Description: 获取公告详情
	 * @param noticeId
	 * @return
	 * @return: int
	 */
	NoticeInfoDetail getNoticeDetail(String noticeType, Integer noticeId);

	/**
	 * @param ArticleType
	 * @param pageBounds
	 * @Title: getArticleList
	 * @Description: 获取新闻报道列表
	 * @return
	 * @return: List<ArticleInfoVO>
	 */
	List<Article> getArticleList(Map map,PageBounds pageBounds);

	ArticleDetail getArticleDetail(int articleId);

	/**
	 * 友情链接
	 * @return
	 */
	List<FriendLink> getFriendLink();

	/**
	 *
	 * @param pageBounds
	 * @Title: getInformationList
	 * @Description: 获取行业资讯列表
	 * @return
	 * @return: List<InfoamationVO>
	 */
	List<Information> getInformationList(String informationType,PageBounds pageBounds);

	InformationDetail getInformationDetail(Map map);

	List<KnowMore> getKnowMoreList(PageBounds pageBounds);

	KnowMore getKnowMoreDetail(int id);


	List<KnowEarly> geKnowEarlyList(PageBounds pageBounds);

	KnowEarly getKnowEarlyDetail(int id);
}
