/**   
 * Copyright © 2015 fenlibao . All rights reserved.
 * 
 * @Title: TransferInServiceImpl.java 
 * @Prject: app-client-api-service
 * @Package: com.fenlibao.p2p.service.creditassignment.impl 
 * @Description: TODO
 * @author: laubrence   
 * @date: 2015-10-23 下午5:26:10 
 * @version: V1.0.0   
 */
package com.fenlibao.p2p.service.creditassignment.impl;

import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6260_F07;
import com.dimeng.util.parser.EnumParser;
import com.fenlibao.p2p.dao.creditassignment.TransferInDao;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.creditassignment.*;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.Status;
import com.fenlibao.p2p.service.FinacingService;
import com.fenlibao.p2p.service.base.db.DbPoolConnection;
import com.fenlibao.p2p.service.creditassignment.TenderExchangeOrderService;
import com.fenlibao.p2p.service.creditassignment.TenderTransferManageService;
import com.fenlibao.p2p.service.creditassignment.TransferInService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.StringHelper;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * @ClassName: TransferInServiceImpl 
 * @author: laubrence
 * @date: 2015-10-23 下午5:26:10  
 */
@Service
public class TransferInServiceImpl implements TransferInService {
	
	@Resource
	TenderTransferManageService tenderTransferManageService;
	
	@Resource
	TenderExchangeOrderService tenderExchangeOrderService;

    @Resource
    private FinacingService finacingService;

	@Resource
	TransferInDao transferInDao;
	
	/**
	 * @Title: purchase
	 * @param applyforId
	 * @param userId
	 * @return 
	 * @see com.fenlibao.p2p.service.creditassignment.TransferInService#purchase(int, int) 
	 */
	@Override
	public void purchase(int applyforId, int userId) throws Throwable{
    	//生成债权转让订单，并返回订单id
    	int orderId = tenderTransferManageService.purchase(applyforId, userId);
    	//提交债权转让订单
    	tenderExchangeOrderService.submit(orderId, null);
    	//确认债权转让订单
    	tenderExchangeOrderService.confirm(orderId, null);
	}
	/**
	 * @Title: transferInList
	 * @param userId
	 * @param timestamp
	 * @return 
	 * @see com.fenlibao.p2p.service.creditassignment.TransferInService
	 */
	@Override
	public List<TransferInList> getTransferInList(int userId, String timestamp) {
		Date pageDate = null;
    	if (StringUtils.isNotEmpty(timestamp)) {
    		pageDate =  DateUtil.timestampToDateBySec(Long.valueOf(timestamp));
        }
		return transferInDao.getTransferInList(userId, pageDate);
	}
	/**
	 * @Title: transferInDetail
	 * @param userId
	 * @param zqId
	 * @return 
	 * @see com.fenlibao.p2p.service.creditassignment.TransferInService
	 */
	@Override
	public TransferInDetail getTransferInDetail(int userId, int zqId) {
		return transferInDao.getTransferInDetail(userId, zqId);
	}

	@Override
	public int getTransferOutListTotalPages(){
		String transferStatus = Status.ZRZ.name();
		String isTransfer = Status.S.name();
		int totalNums = transferInDao.getTransferOutListTotalPages(transferStatus, isTransfer);
		int limit = InterfaceConst.PAGING_NUMBER;
		int totalpage = new BigDecimal(totalNums).divide(new BigDecimal(limit),0,BigDecimal.ROUND_UP).intValue();
        return totalpage;
	}

    @Override
    public TransferOutInfo getTransferOutDetail(int applyforId) {
        String transferStatus = Status.ZRZ.name();
        String isTransfer = Status.S.name();
        return transferInDao.getTransferOutDetail(applyforId, transferStatus, isTransfer);
    }

    @Override
    public List<TransferInInfo> getTransferInInfoList(int userId, String timestamp) {
        Date pageDate = null;
        if (StringUtils.isNotEmpty(timestamp)) {
            pageDate =  DateUtil.timestampToDateBySec(Long.valueOf(timestamp));
        }
        return transferInDao.getTransferInInfoList(userId, pageDate);
    }

    @Override
    public TransferInInfo getTransferInInfoDetail(int userId, int creditId) {
        return transferInDao.getTransferInInfoDetail(userId, creditId);
    }

    @Override
    public String getUserAssignmentAgreementUrl(int userId){
        String serviceArgeementUrl = Config.get("assignment.agreement.url");
        UserInfo userInfo = finacingService.getUserInfo(Integer.valueOf(userId));
        if (userInfo == null) {
            return serviceArgeementUrl;
        }
        //债权转让服务协议
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("userName", "");
        if (StringUtils.isNotEmpty(userInfo.getFullName())) {
            paramMap.put("userName", userInfo.getFullName().substring(0, 1) + "**");
        }
        paramMap.put("phone", userInfo.getUsername());
        if (StringUtils.isNotEmpty(userInfo.getUsername()) && userInfo.getUsername().length()>7) {
            paramMap.put("phone", StringHelper.replace(3, 7, userInfo.getUsername(), "****"));
        }
        paramMap.put("IDNumber", userInfo.getIdCard());

        if (StringUtils.isNotEmpty(serviceArgeementUrl)) {
            CommonTool.convertParamToUrl(serviceArgeementUrl, paramMap);
        }
        return serviceArgeementUrl;
    }


    public void cancel(int zcbId, int userId)throws Throwable{
        try (Connection connection = DbPoolConnection.getInstance().getConnection())
        {
            String zqNumber = "";
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F08, F02 FROM S62.T6251 WHERE F01 = (SELECT F02 FROM S62.T6260 WHERE F01=?) AND F04 = ?");)
            {
                ps.setInt(1, zcbId);
                ps.setInt(2, userId);
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        if (EnumParser.parse(T6251_F08.class, rs.getString(1)) == T6251_F08.F)
                        {
                            throw new LogicalException("该债权已下架");
                        }
                        zqNumber = rs.getString(2);
                    }

                }
            }
            
            connection.setAutoCommit(false);
           
            try (PreparedStatement ps = connection.prepareStatement("UPDATE S62.T6260 SET F07 = ? WHERE F01 = ?");)
            {
                ps.setString(1, T6260_F07.YJS.toString());
                ps.setInt(2, zcbId);
                ps.executeUpdate();
            }
            
            try (PreparedStatement pss =
                connection.prepareStatement("UPDATE S62.T6251 SET F08 = ? WHERE F01 = (SELECT F02 FROM S62.T6260 WHERE F01=?) AND F04 = ?");)
            {
                pss.setString(1, T6251_F08.F.name());
                pss.setInt(2, zcbId);
                pss.setInt(3, userId);
                pss.executeUpdate();
            }
            
            /*T6110 t6110 = selectT6110(connection, userId);
            ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
            Envionment envionment = configureProvider.createEnvionment();
            envionment.set("userName", t6110.F02);
            envionment.set("title", zqNumber);
            String content = configureProvider.format(LetterVariable.ZQ_MANUAL_CANCEL, envionment);
            sendLetter(connection, userId, "手动下架债权", content);*/
            connection.commit();
            connection.setAutoCommit(true);
            
        }
    }
	@Override
	public List<TransferOutInfo> getTransferOutList(Map map, PageBounds pageBounds,int cgNum) {
		map.put("transferStatus", Status.ZRZ.name());
		map.put("isTransfer", Status.S.name());
        map.put("cgNum", cgNum);
        return transferInDao.getTransferOutList(map,pageBounds);
	
	}

	@Override
	public List<TransferInInfo> getAllowTransferInInfoList(Map map,
			PageBounds pageBounds) {
		return transferInDao.getAllowTransferInInfoList(map,pageBounds);
	}
	@Override
	public List<TransferInInfo> getInTransferInInfoList(Map map,
			PageBounds pageBounds) {
		return transferInDao.getInTransferInInfoList(map,pageBounds);
	}
	@Override
	public List<TransferInInfo> getSuccessTransferInInfoList(Map map,
			PageBounds pageBounds) {
		return transferInDao.getSuccessTransferInInfoList(map,pageBounds);
	}
	
	@Override
	public List<TransferInInfo> getBuyedTransferList(String userId,
                                                     PageBounds pageBounds,int cgNum) {
		return transferInDao.getBuyedTransferList(userId, pageBounds,cgNum);
	}

    @Override
    public List<UserCoupons> getUserCoupons(Map map) {
        return transferInDao.getuserCouponsList(map);
    }

    @Override
    public Map getExpectedProfit(int zqid) {
        return transferInDao.getExpectedProfit(zqid);
    }

    @Override
    public Integer getBidRecordId(int creditId) {
        return transferInDao.getBidRecordId(creditId);
    }

    @Override
    public boolean isValidPlanZq(Integer appforId) {
        int count = transferInDao.getProductCount(appforId);

        return count>0?true:false;
    }

    @Override
    public String getSuccessTransferDetail(int userId, int creditId) {
        return transferInDao.getSuccessTransferDetail(userId,creditId);
    }
}
