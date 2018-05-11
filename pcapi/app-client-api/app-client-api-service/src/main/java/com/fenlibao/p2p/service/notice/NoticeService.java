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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.notice.*;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

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
	 * @Title: getArticleList
	 * @Description: 获取媒体报道列表前台
	 * @param pageBounds
	 * @return: List<Article>
	 */
	List<Article> getArticleList( Map map,PageBounds pageBounds);

	ArticleDetail getArticleDetail(int articleId);

	/**
	 * @Title: getFriendLink
	 * @Description: 友情链接
	 * @return: List<FriendLink>
	 */
	List<FriendLink> getFriendLink();

	/**
	 * @Title: InformationList
	 * @Description: 获取行业资讯列表前台
	 * @param pageBounds
	 * @return: List<Information>
	 */
	List<Information> getInformationList( String informationType,PageBounds pageBounds);

	/**
	 * 行业资讯详情
	 */
	InformationDetail getInformationDetail(Map map);

	/**
	 * 网贷知多点
	 * @return
	 */
	List<KnowMore> getKnowMoreList(PageBounds pageBounds);

	KnowMore getKnowMoreDetail(int id);

	/**
	 * 网贷早知道
	 * @return
	 */
	List<KnowEarly> geKnowEarlyList(PageBounds pageBounds);

	KnowEarly getKnowEarlyDetail(int id);
}
