package com.fenlibao.p2p.service.creditassignment.impl;

import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.entities.T6260;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6260_F07;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6507;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.util.DateHelper;
import com.fenlibao.p2p.model.entity.bid.BidBaseInfo;
import com.fenlibao.p2p.model.entity.creditassignment.ZqzrApplyRecord;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.enums.bid.OperationTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.service.base.abstracts.BaseAbstractService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.creditassignment.TenderTransferManageService;
import com.fenlibao.p2p.service.xinwang.credit.XWCreditService;
import com.fenlibao.p2p.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class TenderTransferManageServiceImpl extends BaseAbstractService implements TenderTransferManageService {

	private static final Logger logger= LogManager.getLogger(TenderTransferManageServiceImpl.class);

    @Resource
    BidInfoService bidInfoService;

	@Resource
	XWCreditService xwCreditService;

	@Override
    public int purchase(int transferId, int accountId, OperationTypeEnum operationTypeEnum) throws Throwable{
        if (transferId <= 0){//线上债权转让申请不存在
            throw new BusinessException(ResponseCode.ZQZR_APPLY_DATA_NOT_FOUND.getCode(),ResponseCode.ZQZR_APPLY_DATA_NOT_FOUND.getMessage());
        }
        try (Connection connection = getConnection()){
            try{
            	T6260 t6260 = isValidT6260(connection, transferId, accountId, operationTypeEnum);
                if(t6260==null){
                    throw new BusinessException(ResponseCode.ZQZR_APPLY_DATA_NOT_FOUND.getCode(),ResponseCode.ZQZR_APPLY_DATA_NOT_FOUND.getMessage());
                }
            	// 债权转让费
                BigDecimal fee = t6260.F03.multiply(t6260.F08).setScale(2,BigDecimal.ROUND_DOWN);

                connection.setAutoCommit(false);
                T6501 t6501 = new T6501();
                t6501.F02 = OrderType.BID_EXCHANGE.orderType();
                t6501.F03 = T6501_F03.DTJ;
                t6501.F04 = getCurrentTimestamp(connection);
                t6501.F07 = T6501_F07.YH;
                t6501.F08 = accountId;
                int orderId = insertT6501(connection, t6501);
                T6507 t6507 = new T6507();
                t6507.F01 = orderId;
                t6507.F02 = transferId;
                t6507.F03 = accountId;
                t6507.F04 = t6260.F04;
                t6507.F05 = t6260.F03;
                t6507.F06 = fee;
                insertT6507(connection, t6507);
                
                connection.commit();
                return orderId;
            } catch (Exception e) {
            	connection.rollback();
                throw e;
            } finally{
            	if(connection!=null){  
            		try {  
            			connection.close();   
            		} catch (SQLException e) {  
            			e.printStackTrace();  
            			throw e;
            		}  
            	}
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
	
	/**
	 * @description 判断用户是否可以购买该债权
	 * @param transferId
	 * @param accountId
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean isUserCanPurchase(int transferId, int accountId) throws Exception{
		try (Connection connection = getConnection()){
	        try{
	        	T6260 t6260 = isValidT6260(connection, transferId, accountId, null);
	        	if(t6260 != null){
	        		return true;
	        	}
	        } catch (Exception e) {
	            throw e;
	        } finally{
	        	if(connection!=null){  
	        		try {  
	        			connection.close();   
	        		} catch (SQLException e) {  
	        			e.printStackTrace();  
	        			throw e;
	        		}  
	        	}
	        }
		}catch (Exception e) {
            throw e;
        }
		return false;
	}

    public T6260 isValidT6260(Connection connection, int transferId, int accountId, OperationTypeEnum operationTypeEnum) throws Exception{
        if (transferId <= 0){//线上债权转让申请不存在
            throw new BusinessException(ResponseCode.ZQZR_APPLY_DATA_NOT_FOUND.getCode(),ResponseCode.ZQZR_APPLY_DATA_NOT_FOUND.getMessage());
        }
        // 查询转让申请
        T6260 t6260 = selectT6260(connection, transferId);
        if (t6260 == null){//线上债权转让申请不存在
        	logger.info("线上债权转让申请不存在");
            throw new BusinessException(ResponseCode.ZQZR_APPLY_DATA_NOT_FOUND.getCode(),ResponseCode.ZQZR_APPLY_DATA_NOT_FOUND.getMessage());
        }
        // 查询是否有逾期未还
        int count = selectYqInfo(connection, accountId);
        if (count > 0){//借款逾期未还，不能购买债权！
        	logger.info("借款逾期未还，不能购买债权！");
            throw new BusinessException(ResponseCode.ZQZR_OVER_REPAY.getCode(),ResponseCode.ZQZR_OVER_REPAY.getMessage());
        }
        if (t6260.F07 != T6260_F07.ZRZ) {//线上债权转让申请不是转让中状态,不能转让
        	logger.info("线上债权转让申请不是转让中状态,不能转让");
            throw new BusinessException(ResponseCode.ZQZR_APPLY_NOT_ZRZ.getCode(),ResponseCode.ZQZR_APPLY_NOT_ZRZ.getMessage());
        }
        if (!DateHelper.beforeOrMatchDate(getCurrentDate(connection), t6260.F06)) {//线上债权转让申请已到截至日期,不能转让
        	logger.info("线上债权转让申请已到截至日期,不能转让");
            throw new BusinessException(ResponseCode.ZQZR_APPLY_HAVE_OVER_DATE.getCode(),ResponseCode.ZQZR_APPLY_HAVE_OVER_DATE.getMessage());
        }
        if (DateUtil.getDayBetweenDates(t6260.F05, getCurrentDate(connection)) >5 ){//线上债权转让申请超过5天,不能转让
        	logger.info("线上债权转让申请超过5天,不能转让");
            throw new BusinessException(ResponseCode.ZQZR_APPLY_HAVE_OVER_FIVE_DAY.getCode(),ResponseCode.ZQZR_APPLY_HAVE_OVER_FIVE_DAY.getMessage());
        }

        int zqrId = 0;
        try (PreparedStatement ps = connection.prepareStatement("SELECT F04 FROM S62.T6251 WHERE F01=?")){
            ps.setInt(1, t6260.F02);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    zqrId = rs.getInt(1);
                }
            }
        }

        if (accountId == zqrId){//不能购买自己的债权
        	logger.info("不能购买自己的债权");
        	throw new BusinessException(ResponseCode.ZQZR_CANNOT_BUY_MYSELF.getCode(),ResponseCode.ZQZR_CANNOT_BUY_MYSELF.getMessage());
        }

        // 锁定投资人资金账户
        //BigDecimal fee = t6260.F03.multiply(t6260.F08);
        //BigDecimal total = t6260.F03.add(fee);
        BigDecimal total = t6260.F03;

		if(OperationTypeEnum.PLAN.equals(operationTypeEnum)){
			T6101 investor = selectT6101(connection, accountId, StringUtils.isNotEmpty(t6260.creditsaleNo)?SysFundAccountType.XW_INVESTOR_SDZH:SysFundAccountType.SDZH);
			if (investor == null){//用户往来账户不存在
				logger.info("用户锁定账户不存在");
				throw new BusinessException(ResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST.getCode(),ResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST.getMessage());
			}
			if (investor.F06.compareTo(total) < 0){//账户余额不足
				logger.info("账户余额不足");
				throw new BusinessException(ResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK); //USER_WLZH_ACCOUNT_BALANCE_LACK
			}
		}else{
			T6101 investor = selectT6101(connection, accountId,StringUtils.isNotEmpty(t6260.creditsaleNo)?SysFundAccountType.XW_INVESTOR_WLZH:SysFundAccountType.WLZH);
			if (investor == null){//用户往来账户不存在
				logger.info("用户往来账户不存在");
				throw new BusinessException(ResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST.getCode(),ResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST.getMessage());
			}
			if (investor.F06.compareTo(total) < 0){//账户余额不足
				logger.info("账户余额不足");
				throw new BusinessException(ResponseCode.USER_ACCOUNT_BALANCE_INSUFFICIENT); //USER_WLZH_ACCOUNT_BALANCE_LACK
			}
		}
    	return t6260;
    }
	
	
	@Override
    public String cancel(int zqId, int userId) throws Exception{
		String code = ""; //added by zcai 20160630
        try (Connection connection = getConnection()){
        	try{
        		connection.setAutoCommit(false);  
	            int zcbId = 0;
				int bidId = 0;
				//lock 债权记录和债权转让申请记录
	            try (PreparedStatement ps =
	                connection.prepareStatement("SELECT f.F01,t.F02,t.F03 FROM S62.T6260 f INNER JOIN S62.T6251 t on t.F01 = f.F02 WHERE f.F02= ? and t.F04 = ? and f.F07= ? for update ");){
	            	ps.setInt(1, zqId);
	                ps.setInt(2, userId);
	                ps.setString(3,T6260_F07.ZRZ.name());
	                try (ResultSet rs = ps.executeQuery();){
	                    if (rs.next()){
//	                        if (EnumParser.parse(T6251_F08.class, rs.getString(1)) == T6251_F08.F){
//	                        	throw new BusinessException(ResponseCode.ZQZR_ZQ_HAVE_CANCEL.getCode(),ResponseCode.ZQZR_ZQ_HAVE_CANCEL.getMessage());
//	                        }
	                    	zcbId = rs.getInt(1);
							code = rs.getString(2);
							bidId = rs.getInt(3);
						}
	                }
	            }
	            if(zcbId==0){
	            	throw new BusinessException(ResponseCode.ZQZR_APPLY_DATA_NOT_FOUND.getCode(),ResponseCode.ZQZR_APPLY_DATA_NOT_FOUND.getMessage());
	            }
	           
	            try (PreparedStatement ps = connection.prepareStatement("UPDATE S62.T6260 SET F07 = ? WHERE F01 = ?");){
	                ps.setString(1, T6260_F07.YJS.name());
	                ps.setInt(2, zcbId);
	                ps.executeUpdate();
	            }
	            
	            try (PreparedStatement pss =
	                connection.prepareStatement("UPDATE S62.T6251 SET F08 = ? WHERE F01 =?");){
	                pss.setString(1, T6251_F08.F.name());
	                pss.setInt(2, zqId);
	                pss.executeUpdate();
	            }


	            /*T6110 t6110 = selectT6110(connection, userId);
	            ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
	            Envionment envionment = configureProvider.createEnvionment();
	            envionment.set("userName", t6110.F02);
	            envionment.set("title", zqNumber);
	            String content = configureProvider.format(LetterVariable.ZQ_MANUAL_CANCEL, envionment);
	            sendLetter(connection, userId, "手动下架债权", content);*/
				BidBaseInfo bidBaseInfo = bidInfoService.getBidBaseInfo(bidId);
				if (VersionTypeEnum.CG.getIndex()==bidBaseInfo.getCgMode()) {
					xwCreditService.doCancelDebentureSale(zqId);
				}
				connection.commit();
	            connection.setAutoCommit(true);
	        }
	        catch (Exception e) {
	        	connection.rollback();
	            throw e;
	        } finally{
	        	if(connection!=null){
	        		try {
	        			connection.close();
	        		} catch (SQLException e) {
	        			e.printStackTrace();
	        			throw e;
	        		}
	        	}
	        }
        }
		return code;
    }

	@Override
    public void zqzrAutoCancel() throws Throwable
    {
        logger.info("开始执行【债权转让自动下架】任务");
        try (Connection connection = getConnection()){
            try{
            	List<ZqzrApplyRecord> recordList = getZrzList(connection);
                if(recordList!=null && recordList.size() >=0){
	            	connection.setAutoCommit(false);
	                for (int i = 0; i < recordList.size(); i++){
	                	ZqzrApplyRecord record = recordList.get(i);
	                    if (record != null && record.getZqId()> 0){
	                        T6251 zqRecord = selectT6251(connection, record.getZqId());
	                        if(zqRecord == null){
	                        	logger.info(ResponseCode.ZQZR_ZQ_INFO_NOT_FOUND.getMessage());
	                        	continue;
	                        }
	                        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6260 SET T6260.F07 = ? WHERE T6260.F01 = ?")){
	                            pstmt.setString(1, T6260_F07.YJS.name());
	                            pstmt.setInt(2, record.getZqApplyId());
	                            pstmt.executeUpdate();
	                        }
	                        try (PreparedStatement pstmt =
	                            connection.prepareStatement("UPDATE S62.T6251 SET F08 = ? WHERE F01 = ?")){
	                            pstmt.setString(1, T6251_F08.F.name());
	                            pstmt.setInt(2, record.getZqId());
	                            pstmt.execute();
	                        }
							if (StringUtils.isNotBlank(record.getCreditsaleNo())) {
								xwCreditService.doCancelDebentureSale(zqRecord.F01);
							}
							logger.info("【目前有"+recordList.size()+"】个债权需要下架,已成功下架：【"+(i+1)+"】个债权");
	                    }
	//                    T6110 t6110 = selectT6110(connection, record.F04);
	//                    ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
	//                    Envionment envionment = configureProvider.createEnvionment();
	//                    envionment.set("userName", t6110.F02);
	//                    envionment.set("title", record.F02);
	//                    String content = configureProvider.format(LetterVariable.ZQ_AUTOMATIC_CANCEL, envionment);
	//                    sendLetter(connection, record.F04, "自动下架债权", content);
	                }
	                connection.commit();
                }
            }
            catch (Exception e)
            {
            	connection.rollback();
            	logger.error(e);
                throw e;
            }
            finally{  
            	if(connection!=null){  
            		try {  
            			connection.close();   
            			logger.info("关闭connection...");
            		} catch (SQLException e) {  
            			e.printStackTrace();  
            			throw e;
            		}  
            	}
            }
        }
    }

    /**
     * 获取转让中需要下架的债权
     * @param connection
     * @return
     * @throws Throwable 
     */
    private List<ZqzrApplyRecord> getZrzList(Connection connection) throws Throwable {
        List<ZqzrApplyRecord> recordList = new ArrayList<ZqzrApplyRecord>();
        
        String sqlStr = "SELECT T6251.F01 AS zqId,T6251.F04 AS userId,T6260.F01 AS zqApplyId,T6260.F05 AS zqApplyTime, T6260.creditsale_no AS creditsaleNo FROM S62.T6260 INNER JOIN S62.T6251 ON T6260.F02 = T6251.F01 WHERE T6260.F07 = 'ZRZ' AND T6251.F08 = 'S' AND datediff(?, T6260.F05) > 3";
        try (PreparedStatement pstmt = connection.prepareStatement(sqlStr)) {
            pstmt.setTimestamp(1, getCurrentTimestamp(connection));
            try (ResultSet rs = pstmt.executeQuery()) {
            	ZqzrApplyRecord record = null;
                while(rs.next()) {
                    record = new ZqzrApplyRecord();
                    record.setZqId(rs.getInt(1));
                    record.setUserId(rs.getInt(2));
                    record.setZqApplyId(rs.getInt(3));
					record.setCreditsaleNo(rs.getString(5));
					recordList.add(record);
                }
            }
        }
        return recordList;
    }
	
	protected int insertT6501(Connection connection, T6501 entity)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement(
						"INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, entity.F02);
			pstmt.setString(2, entity.F03.name());
			pstmt.setTimestamp(3, entity.F04);
			pstmt.setTimestamp(4, entity.F05);
			pstmt.setTimestamp(5, entity.F06);
			pstmt.setString(6, entity.F07.name());
			pstmt.setInt(7, entity.F08);
			pstmt.setInt(8, entity.F09);
			pstmt.execute();
			try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
				return 0;
			}
		}
	}

	/**
	 * lock 锁定债权记录
	 * @param connection
	 * @param zqId
	 * @return
	 * @throws Exception 
	 */
	private T6251 selectT6251(Connection connection, int zqId)
	    throws Exception
	{
	    T6251 record = null;
	    try (PreparedStatement pstmt =
	        connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6251 WHERE T6251.F01 = ? for update"))
	    {
	        pstmt.setInt(1, zqId);
	        try (ResultSet resultSet = pstmt.executeQuery())
	        {
	            if (resultSet.next())
	            {
	                record = new T6251();
	                record.F01 = resultSet.getInt(1);
	                record.F02 = resultSet.getString(2);
	                record.F03 = resultSet.getInt(3);
	                record.F04 = resultSet.getInt(4);
	                record.F05 = resultSet.getBigDecimal(5);
	                record.F06 = resultSet.getBigDecimal(6);
	                record.F07 = resultSet.getBigDecimal(7);
	                record.F08 = T6251_F08.parse(resultSet.getString(8));
	                record.F09 = resultSet.getDate(9);
	                record.F10 = resultSet.getDate(10);
	                record.F11 = resultSet.getInt(11);
	                if (T6251_F08.F.equals(record.F08)){
                        throw new BusinessException(ResponseCode.ZQZR_ZQ_HAVE_CANCEL.getCode(),ResponseCode.ZQZR_ZQ_HAVE_CANCEL.getMessage());
                    }
	            }
	        }
	    }
	    return record;
	}

	private T6260 selectT6260(Connection connection, int F01)
			throws SQLException {
		T6260 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, creditsale_no FROM S62.T6260 WHERE F01 = ? ")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6260();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getBigDecimal(3);
					record.F04 = resultSet.getBigDecimal(4);
					record.F05 = resultSet.getTimestamp(5);
					record.F06 = resultSet.getDate(6);
					record.F07 = T6260_F07.parse(resultSet.getString(7));
					record.F08 = resultSet.getBigDecimal(8);
					record.creditsaleNo = resultSet.getString(9);
				}
			}
		}
		return record;
	}

	private T6101 selectT6101(Connection connection, int F02, SysFundAccountType F03)
			throws SQLException {
		T6101 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ? FOR UPDATE")) {
			pstmt.setInt(1, F02);
			pstmt.setString(2, F03.name());
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6101();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = T6101_F03.parse(resultSet.getString(3));
					record.F04 = resultSet.getString(4);
					record.F05 = resultSet.getString(5);
					record.F06 = resultSet.getBigDecimal(6);
					record.F07 = resultSet.getTimestamp(7);
				}
			}
		}
		return record;
	}

	protected void insertT6507(Connection connection, T6507 entity)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("INSERT INTO S65.T6507 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?")) {
			pstmt.setInt(1, entity.F01);
			pstmt.setInt(2, entity.F02);
			pstmt.setInt(3, entity.F03);
			pstmt.setBigDecimal(4, entity.F04);
			pstmt.setBigDecimal(5, entity.F05);
			pstmt.setBigDecimal(6, entity.F06);
			pstmt.execute();
		}
	}
	
	@Override
    protected Date getCurrentDate(Connection connection) throws Exception {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT CURRENT_DATE()")) {
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getDate(1);
				}
			}
		}
		return null;
	}
    
    private int selectYqInfo(Connection connection, int F03)
        throws SQLException
    {
        int count = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT COUNT(*) FROM S62.T6252 WHERE F03 = ? AND F08 < CURDATE() AND F09 = ? "))
        {
            pstmt.setInt(1, F03);
            pstmt.setString(2, T6252_F09.WH.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    count = resultSet.getInt(1);
                }
            }
        }
        return count;
    }
}
