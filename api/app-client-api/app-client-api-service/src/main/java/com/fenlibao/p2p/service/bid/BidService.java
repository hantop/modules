package com.fenlibao.p2p.service.bid;

import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.vo.bidinfo.AutoTenderVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by LouisWang on 2015/8/14.
 */
public interface BidService {

    ShopTreasureInfo findShopTreasureInfo(int bid)throws Throwable;

	void doBid(int bidId, BigDecimal amount, int accountId, String experFlg,
			String fxhbIds, boolean sendSmsAndLetterFlag) throws Throwable;

	void doBid(int bidId, BigDecimal amount, int accountId, String experFlg,
			   String fxhbIds) throws Throwable;



	/**
	 * 获取投标中剩余可投金额小于100的标
	 * @return
	 */
	List<AutoTenderVO> getTBZ(VersionTypeEnum versionTypeEnum);

	/**
	 * 投标使用加息卷
	 * @throws Throwable
	 */
	void doBid(int bidId, BigDecimal amount, int accountId, String experFlg, String fxhbIds, String jxqId) throws Throwable;

	/**
	 * 投标使用加息卷
     * @throws Throwable
     */
	void doBid(int bidId, BigDecimal amount, int accountId, String experFlg, String fxhbIds, String jxqId, boolean sendSmsAndLetterFlag) throws Throwable;

	/**
	 * 获取投标中的信用贷
	 * @param type 1=获取12小时前的
	 * @return
	 */
	List<AutoTenderVO> getCreditLoanTBZ(Integer type, VersionTypeEnum versionTypeEnum);
}

