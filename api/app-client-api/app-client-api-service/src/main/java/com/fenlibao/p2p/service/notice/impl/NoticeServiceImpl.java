package com.fenlibao.p2p.service.notice.impl;

import com.fenlibao.p2p.dao.notice.NoticeDao;
import com.fenlibao.p2p.model.entity.notice.*;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.service.notice.NoticeService;
import com.fenlibao.p2p.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public List<NoticeInfo> getNoticeList(String noticeType,Integer pageNo,Integer pagesize){
    	
		return noticeDao.getNoticeList(noticeType,pageNo,pagesize);
	}

	@Override
	public List<NoticeInfo> getNoticeList_(String noticeType, String timestamp, Integer limit) {
		Date pageDate = null;
		if (StringUtils.isNotEmpty(timestamp)) {
			pageDate =  DateUtil.timestampToDateBySec(Long.valueOf(timestamp));
		}
		return noticeDao.getNoticeList(noticeType,pageDate, limit);
	}

	/** 
	 * @Title: getNoticeDetail 
	 * @Description: 获取公告详情
	 * @param noticeId
	 * @return
	 * @return: int
	 */
    @Override
	public NoticeInfoDetail getNoticeDetail(int noticeId){
		return noticeDao.getNoticeDetail(noticeId);
	}


    @Override
  	public List<Article> getArticleList(Integer pageNo,Integer pagesize){
    	if(pageNo==null)pageNo=1;
		if(pagesize==null)pagesize=InterfaceConst.PAGESIZE;
		Map map=new HashMap<>();
		map.put("pageNo", (pageNo-1)*pagesize);
		map.put("pagesize", pagesize);
  		return noticeDao.getArticleList(map);
  	}

	@Override
	public ArticleDetail getArticleDetail(int articleId) {
		return noticeDao.getArticleDetail(articleId);
	}

	@Override
	public long getNoticeLastUpdateTime() {
		return noticeDao.getNoticeLastUpdateTime();
	}

	@Override
	public int checkNoticeNew(long updateTime) {

		return noticeDao.checkNoticeNew(updateTime);
	}

	@Override
	public List<KnowEarly> geKnowEarlyList(Integer pageNo, Integer pagesize) {
		if(pageNo==null)pageNo=1;
		if(pagesize==null)pagesize=InterfaceConst.PAGESIZE;
		Map map=new HashMap<>();
		map.put("pageNo", (pageNo-1)*pagesize);
		map.put("pagesize", pagesize);
		return noticeDao.geKnowEarlyList(map);
	}

	@Override
	public List<KnowMore> getKnowMoreList(Integer pageNo, Integer pagesize) {
		if(pageNo==null)pageNo=1;
		if(pagesize==null)pagesize=InterfaceConst.PAGESIZE;
		Map map=new HashMap<>();
		map.put("pageNo", (pageNo-1)*pagesize);
		map.put("pagesize", pagesize);
		return noticeDao.getKnowMoreList(map);
	}

	@Override
	public KnowMore getKnowMoreDetail(int id) {
		return noticeDao.getKnowMoreDetail(id);
	}

	@Override
	public KnowEarly getKnowEarlyDetail(int id) {
		return noticeDao.getKnowEarlyDetail(id);
	}

	@Override
	public List<ReportDeatil> getRepoetList(Integer pageNo, Integer pagesize,String type) {
		if(pageNo==null)pageNo=1;
		if(pagesize==null)pagesize=InterfaceConst.PAGESIZE;
		Map map=new HashMap<>();
		map.put("pageNo", (pageNo-1)*pagesize);
		map.put("pagesize", pagesize);
		map.put("type", type);
		return noticeDao.getReportList(map);
	}

	@Override
	public List<Article> getArticleListNew(Integer pageNo,Integer pagesize,String type){
		if(pageNo==null)pageNo=1;
		if(pagesize==null)pagesize=InterfaceConst.PAGESIZE;
		Map map=new HashMap<>();
		map.put("pageNo", (pageNo-1)*pagesize);
		map.put("pagesize", pagesize);
		map.put("type", type);
		return noticeDao.getArticleList(map);
	}
}
