package com.fenlibao.p2p.dao.notice.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fenlibao.p2p.model.entity.notice.*;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import com.fenlibao.p2p.dao.notice.NoticeDao;

@Repository
public class NoticeDaoImpl implements NoticeDao {

	@Resource
	private SqlSession sqlSession;

	private static final String MAPPER = "NoticeInfoMapper.";

	@Override
	public List<NoticeInfo> getNoticeList(String noticeType, PageBounds pageBounds) {
		Map map =new HashMap();
		map.put("noticeType", noticeType);
		return sqlSession.selectList(MAPPER+"getNoticeList",map,pageBounds);
	}

	@Override
	public NoticeInfoDetail getNoticeDetail(String noticeType, Integer noticeId) {
		Map<String, Object> map = new HashMap();
		map.put("noticeType", noticeType);
		map.put("noticeId", noticeId);
		return sqlSession.selectOne(MAPPER+"getNoticeDetail", map);
	}

	@Override
	public List<Article> getArticleList(Map map,PageBounds pageBounds) {
		return sqlSession.selectList(MAPPER+"getArticleList",null,pageBounds);
	}

	@Override
	public ArticleDetail getArticleDetail(int articleId) {
		return sqlSession.selectOne(MAPPER+"getArticleDetail", articleId);
	}

	@Override
	public List<FriendLink> getFriendLink() {
		return sqlSession.selectList(MAPPER+"getFriendLink");
	}

	@Override
	public List<Information> getInformationList(String informationType, PageBounds pageBounds) {
		return sqlSession.selectList(MAPPER+"getInformationList",informationType,pageBounds);
	}

	@Override
	public InformationDetail getInformationDetail(Map map) {
		return sqlSession.selectOne(MAPPER+"getInformationDetail", map);
	}

	@Override
	public List<KnowEarly> geKnowEarlyList(PageBounds pageBounds) {
		return sqlSession.selectList(MAPPER+"geKnowEarlyList","1",pageBounds);
	}

	@Override
	public List<KnowMore> getKnowMoreList(PageBounds pageBounds) {
		return sqlSession.selectList(MAPPER+"getKnowMoreList","1",pageBounds);
	}

	@Override
	public KnowMore getKnowMoreDetail(int id) {
		return sqlSession.selectOne(MAPPER+"getKnowMoreDetail", id);
	}

	@Override
	public KnowEarly getKnowEarlyDetail(int id) {
		return sqlSession.selectOne(MAPPER+"getKnowEarlyDetail", id);
	}
}

