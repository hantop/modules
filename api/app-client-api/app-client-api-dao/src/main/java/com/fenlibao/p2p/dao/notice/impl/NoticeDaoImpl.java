package com.fenlibao.p2p.dao.notice.impl;

import com.fenlibao.p2p.dao.notice.NoticeDao;
import com.fenlibao.p2p.model.entity.notice.*;
import com.fenlibao.p2p.model.global.InterfaceConst;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class NoticeDaoImpl implements NoticeDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "NoticeInfoMapper.";
	
	@Override
	public List<NoticeInfo> getNoticeList(String noticeType,Integer pageNo, Integer pagesize) {
		if(pageNo==null)pageNo=1;
		if(pagesize==null)pagesize=InterfaceConst.PAGESIZE;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("noticeType", noticeType);
//		map.put("timestamp", timestamp);
		map.put("pageNo",(pageNo-1)*pagesize);
		map.put("pagesize",pagesize);
		return sqlSession.selectList(MAPPER+"getNoticeList",map);
	}

	public List<NoticeInfo> getNoticeList(String noticeType, Date timestamp, Integer limit) {
		Map<String, Object> map =new HashMap(3);
		if (limit == null) {
			limit = InterfaceConst.PAGESIZE;
		}

		map.put("noticeType", noticeType);
//		map.put("timestamp", timestamp);
		map.put("limit", limit);
		return sqlSession.selectList(MAPPER+"getNoticeList",map);
	}

	@Override
	public NoticeInfoDetail getNoticeDetail(int noticeId) {
		return sqlSession.selectOne(MAPPER+"getNoticeDetail", noticeId);
	}

	@Override
	public List<Article> getArticleList(Map map) {
		return sqlSession.selectList(MAPPER+"getArticleList",map);
	}

	@Override
	public ArticleDetail getArticleDetail(int articleId) {
		return sqlSession.selectOne(MAPPER+"getArticleDetail", articleId);
	}

	@Override
	public long getNoticeLastUpdateTime() {
		Timestamp timestamp = sqlSession.selectOne(MAPPER+"getNoticeLastUpdateTime");


		return timestamp.getTime();
	}

	@Override
	public int checkNoticeNew(long timstamp) {
		Timestamp updateTime = new Timestamp(timstamp);
		return sqlSession.selectOne(MAPPER+"checkNoticeNew",updateTime);
	}


	@Override
	public List<KnowEarly> geKnowEarlyList(Map map) {
		return sqlSession.selectList(MAPPER+"geKnowEarlyList",map);
	}

	@Override
	public List<KnowMore> getKnowMoreList(Map map) {
		return sqlSession.selectList(MAPPER+"getKnowMoreList",map);
	}

	@Override
	public KnowMore getKnowMoreDetail(int id) {
		return sqlSession.selectOne(MAPPER+"getKnowMoreDetail", id);
	}

	@Override
	public KnowEarly getKnowEarlyDetail(int id) {
		return sqlSession.selectOne(MAPPER+"getKnowEarlyDetail", id);
	}

	@Override
	public List<ReportDeatil> getReportList(Map map) {
		return sqlSession.selectList(MAPPER+"getReportList",map);
	}
}

