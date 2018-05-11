package com.fenlibao.p2p.service.mp.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fenlibao.p2p.dao.mq.IMallDao;
import com.fenlibao.p2p.model.mp.vo.mall.MallCommodityVO;
import com.fenlibao.p2p.model.mp.vo.mall.MallConsumePatternsVO;
import com.fenlibao.p2p.service.mp.IMallService;

@Service
public class MallServiceImpl implements IMallService {

	@Resource
	private IMallDao mallDao;
	
	@Override
	public List<MallConsumePatternsVO> getConsumePatterns() {
		return mallDao.getConsumePatterns();
	}

	@Override
	public List<MallCommodityVO> getCommodity() {
		return mallDao.getCommodity();
	}

}
