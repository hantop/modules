package com.fenlibao.p2p.dao.mq;

import java.util.List;

import com.fenlibao.p2p.model.mp.vo.mall.MallCommodityVO;
import com.fenlibao.p2p.model.mp.vo.mall.MallConsumePatternsVO;

public interface IMallDao {

	/**
	 * 获取商城消费方式
	 * @return
	 */
	List<MallConsumePatternsVO> getConsumePatterns();

	/**
	 * 获取商品
	 * @return
	 */
	List<MallCommodityVO> getCommodity();
	
}
