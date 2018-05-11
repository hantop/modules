package com.fenlibao.p2p.service.notice.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fenlibao.p2p.model.entity.notice.*;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.springframework.stereotype.Service;

import com.fenlibao.p2p.dao.notice.NoticeDao;
import com.fenlibao.p2p.service.notice.NoticeService;

@Service
public class NoticeServiceImpl implements NoticeService {

	@Resource
	NoticeDao noticeDao;

	/**
	 * @Title: getNoticeList
	 * @Description: 获取公告列表
	 * @return
	 * @return: List<NoticeInfoVO>
	 */
	@Override
	public List<NoticeInfo> getNoticeList(String noticeType, PageBounds pageBounds){
		return noticeDao.getNoticeList(noticeType,pageBounds);
	}

	/**
	 * @Title: getNoticeDetail
	 * @Description: 获取公告详情
	 * @param noticeId
	 * @return
	 * @return: int
	 */
	@Override
	public NoticeInfoDetail getNoticeDetail(String noticeType, Integer noticeId){
		return noticeDao.getNoticeDetail(noticeType, noticeId);
	}

	@Override
	public List<Article> getArticleList(Map map, PageBounds pageBounds){
		return noticeDao.getArticleList(map,pageBounds);
	}

	@Override
	public ArticleDetail getArticleDetail(int articleId) {
		return noticeDao.getArticleDetail(articleId);
	}

	@Override
	public List<FriendLink> getFriendLink() {
		return noticeDao.getFriendLink();
	}

	@Override
	public List<Information> getInformationList(String informationType, PageBounds pageBounds) {
		return noticeDao.getInformationList(informationType,pageBounds);
	}

	@Override
	public InformationDetail getInformationDetail(Map map) {
		return noticeDao.getInformationDetail(map);
	}

	@Override
	public List<KnowEarly> geKnowEarlyList(PageBounds pageBounds) {
		return noticeDao.geKnowEarlyList(pageBounds);
	}

	@Override
	public List<KnowMore> getKnowMoreList(PageBounds pageBounds) {
		/*if(pageNo==null)pageNo=1;
		if(pagesize==null)pagesize=InterfaceConst.PAGESIZE;
		Map map=new HashMap<>();
		map.put("pageNo", (pageNo-1)*pagesize);
		map.put("pagesize", pagesize);*/
		return noticeDao.getKnowMoreList(pageBounds);
	}

	@Override
	public KnowMore getKnowMoreDetail(int id) {
		return noticeDao.getKnowMoreDetail(id);
	}

	@Override
	public KnowEarly getKnowEarlyDetail(int id) {
		return noticeDao.getKnowEarlyDetail(id);
	}
}
