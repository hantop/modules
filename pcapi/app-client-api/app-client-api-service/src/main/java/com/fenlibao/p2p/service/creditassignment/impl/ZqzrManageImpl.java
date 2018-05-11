package com.fenlibao.p2p.service.creditassignment.impl;

import com.alibaba.fastjson.JSONArray;
import com.dimeng.p2p.S62.entities.T6232;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6260_F07;
import com.dimeng.p2p.XyType;
import com.dimeng.util.DateHelper;
import com.fenlibao.p2p.dao.PublicAccessoryDao;
import com.fenlibao.p2p.dao.creditassignment.CreditAssigmentDao;
import com.fenlibao.p2p.model.entity.bid.BidBaseInfo;
import com.fenlibao.p2p.model.entity.creditassignment.AddTransfer;
import com.fenlibao.p2p.model.entity.creditassignment.Zqzrlb;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.creditassignment.ApplyforDetailVO;
import com.fenlibao.p2p.model.vo.creditassignment.ApplyforDetailVO_131;
import com.fenlibao.p2p.service.base.abstracts.BaseAbstractService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.creditassignment.ZqzrManage;
import com.fenlibao.p2p.service.xinwang.credit.XWCreditService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.Pager;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.pay.CalculateEarningsUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import com.mysql.jdbc.Statement;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ZqzrManageImpl extends BaseAbstractService implements ZqzrManage
{
	@Autowired
	private CreditAssigmentDao creditAssigmentDao;
	
	@Resource
	private PublicAccessoryDao publicAccessoryDao;
	
	@Resource
	private BidInfoService bidInfoService;

    @Resource
    private XWCreditService xwCreditService;
    
    @Override
    public void transfer(AddTransfer query) throws Throwable
    {
        if (query == null) {
            throw new BusinessException(ResponseCode.ZQZR_ZQ_INFO_NOT_FOUND.getCode(),ResponseCode.ZQZR_ZQ_INFO_NOT_FOUND.getMessage());
        }
        BigDecimal money = null;
        {
            money = query.getTransferValue();
            if (money.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException(ResponseCode.ZQZR_TRANSFER_VALUE_UNDER_ZERO.getCode(),ResponseCode.ZQZR_TRANSFER_VALUE_UNDER_ZERO.getMessage());
            }
        }

        BigDecimal passedEarning = null;
        {
            passedEarning = query.getPassedEarning();
            if (passedEarning.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException(ResponseCode.ZQZR_PASSEDEARNING_UNDER_ZERO.getCode(),ResponseCode.ZQZR_PASSEDEARNING_UNDER_ZERO.getMessage());
            }
        }

        try (Connection connection = getConnection())
        {
            connection.setAutoCommit(false);

            int id = query.getTransferId();// 债权ID
            T6251 t6251 = selectT6251(connection, id);
            if (t6251 == null) {
                throw new BusinessException(ResponseCode.ZQZR_ZQ_INFO_NOT_FOUND.getCode(),ResponseCode.ZQZR_ZQ_INFO_NOT_FOUND.getMessage());
            }
            if (t6251.F08 == T6251_F08.S) {
                throw new BusinessException(ResponseCode.ZQZR_IS_TRANSFER.getCode(),ResponseCode.ZQZR_IS_TRANSFER.getMessage());
            }
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT 1 FROM S62.T6251 INNER JOIN S62.T6252 ON T6252.F11 = T6251.F01 AND T6252.F09 = 'HKZ' WHERE T6251.F01 = ? LIMIT 1 ");)
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        throw new BusinessException(ResponseCode.ZQZR_IS_REPAYMENT.getCode(),ResponseCode.ZQZR_IS_REPAYMENT.getMessage());
                    }
                }
            }
            BigDecimal bidValue = query.getBidValue();
            //判断转让金额是否在80%~100% 之间 modify by laubrence 2016-4-1 17:34:42
            if (!isNormalRange(bidValue, passedEarning,money)) {
                throw new BusinessException(ResponseCode.ZQZR_ZQVALUE_LESS_TRANSFERVALUE.getCode(),ResponseCode.ZQZR_ZQVALUE_LESS_TRANSFERVALUE.getMessage());
            }
            //判断该债权是否有逾期未还记录
            if (isExpired(connection, id)) {
                throw new BusinessException(ResponseCode.ZQZR_OVER_REPAY.getCode(),ResponseCode.ZQZR_OVER_REPAY.getMessage());
            }
            int zqsqId =0;
            //插入债权转让申请记录
            //生成新网流水
            BidBaseInfo bidBaseInfo = bidInfoService.getBidBaseInfo(t6251.F03);
            String requestNo = null;
            if (bidBaseInfo.getCgMode() == VersionTypeEnum.CG.getIndex()) {
                requestNo = XinWangUtil.createRequestNo();
            }
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO S62.T6260 ( F02, F03, F04, F05, F06, F07, F08 ,F09, creditsale_no) VALUES (?,?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS))
            {
                BigDecimal rate = money.divide(bidValue.add(passedEarning),2,RoundingMode.HALF_UP);//折扣率
                ps.setInt(1, id);
                ps.setBigDecimal(2, money);
                ps.setBigDecimal(3, bidValue);
                ps.setTimestamp(4, getCurrentTimestamp(connection));
                ps.setDate(5, getEndDate(connection, id,InterfaceConst.ZQZR_CY_LAST_DAY));
                ps.setString(6, T6260_F07.ZRZ.name());
                ps.setBigDecimal(7, query.getRateMoney());
                ps.setBigDecimal(8, rate);
                ps.setString(9, requestNo);
                ps.execute();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    zqsqId = rs.getInt(1);
                }
            }
            //更新当前债权状态->转让中
            try (PreparedStatement ps = connection.prepareStatement("UPDATE S62.T6251 SET F08 = ? WHERE F01 = ?");) {
                ps.setString(1, T6251_F08.S.name());
                ps.setInt(2, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO S62.T6261 SET F01 = ?, F02 = ?");) {
                ps.setInt(1, zqsqId);
                ps.setInt(2, selectint(connection));
                ps.execute();
            }
            try {
                if (bidBaseInfo.getCgMode() == VersionTypeEnum.CG.getIndex()) {
                    xwCreditService.doDebentureSale(t6251.F04, id, requestNo);
                }
                connection.commit();
                connection.setAutoCommit(true);
            } catch (Throwable ex) {
            connection.rollback();
            throw new Exception(ex);
             }
        }
     }
    
    private int selectint(Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02 FROM S51.T5125 WHERE T5125.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, XyType.ZQZRXY);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
            return 0;
        }
    }
    
    /**
     * 转让金额是否在正常范围
     * @param bidValue      标的原始债权金额
     * @param passedEarning 过去天数收益
     * @param transferValue 交易金额
     * @return
     */
    private boolean isNormalRange(BigDecimal bidValue, BigDecimal passedEarning,BigDecimal transferValue){
        bidValue=bidValue.setScale(2,BigDecimal.ROUND_HALF_UP);
        passedEarning=passedEarning.setScale(2,BigDecimal.ROUND_HALF_UP);
        transferValue=transferValue.setScale(2,BigDecimal.ROUND_HALF_UP);

        BigDecimal maxRange = bidValue.add(passedEarning).multiply(new BigDecimal(InterfaceConst.MAX_CREDIT_ASSIGNMENT_RATE)).setScale(2,BigDecimal.ROUND_DOWN);
    	BigDecimal minRange = bidValue.add(passedEarning).multiply(new BigDecimal(InterfaceConst.MIN_CREDIT_ASSIGNMENT_RATE)).setScale(2,BigDecimal.ROUND_DOWN);
    	
    	if(transferValue.compareTo(maxRange)>0||transferValue.compareTo(minRange)<0){
    		return false;
    	}
    	return true;
    }
    
    private boolean isExpired(Connection connection, int F11)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT COUNT(*) FROM S62.T6252 WHERE T6252.F08 < ? AND T6252.F09 = ? AND T6252.F11 = ? "))
        {
        	pstmt.setDate(1, getCurrentDate(connection));
            pstmt.setString(2, T6252_F09.WH.name());
            pstmt.setInt(3, F11);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                } else {
                    return false;
                }
            }
        }
    }
    
    private Date getEndDate(Connection connection, int F11,int lastDays)
        throws Throwable
    {
        Date date = getCurrentDate(connection);
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT DISTINCT F08 FROM S62.T6252 WHERE T6252.F08 >= ? AND T6252.F09 = ? AND T6252.F11 = ? ORDER BY F06 ASC"))
        {
            pstmt.setDate(1, date);
            pstmt.setString(2, T6252_F09.WH.name());
            pstmt.setInt(3, F11);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    Date nextDate = resultSet.getDate(1);
                    nextDate.setTime(nextDate.getTime() - DateHelper.DAY_IN_MILLISECONDS * lastDays);
                    if (DateHelper.beforeDate(nextDate, date)) {
                    	throw new BusinessException(ResponseCode.ZQZR_SJKD.getCode(),ResponseCode.ZQZR_SJKD.getMessage());
                    }
                    return nextDate;
                } else {
                	throw new BusinessException(ResponseCode.ZQZR_NOT_WH.getCode(),ResponseCode.ZQZR_NOT_WH.getMessage());
                }
            }
        }
    }
    
    private T6251 selectT6251(Connection connection, int F01)
        throws SQLException
    {
        T6251 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6251 WHERE T6251.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
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
                }
            }
        }
        return record;
    }
    
	@Override
	public Pager<Zqzrlb> applyforList(String status, int curpage,int limit,String isTransfer,String timestamp) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		map.put("isTransfer", isTransfer);
		if(StringUtils.isNotEmpty(timestamp)){
			map.put("timestamp",DateUtil.timestampToDateBySec(Long.valueOf(timestamp)));
		}
		
		int total = this.creditAssigmentDao.getCreditassignmentApplyforCount(map);
		Pager<Zqzrlb> pager = new Pager<Zqzrlb>(total,curpage,limit);
			
		map.put("start", pager.getStart());
		map.put("limit", limit);
		
		List<Zqzrlb> list = creditAssigmentDao.getCreditassignmentApplyforList(map);
		pager.setList(list);
		
		return pager;
	}

	@Override
	public ApplyforDetailVO applyforDetail(String status, int applyforId,String isTransfer) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		map.put("applyforId", applyforId);
		map.put("isTransfer", isTransfer);
		List<Zqzrlb> list = creditAssigmentDao.getCreditassignmentApplyforList(map);
		if(list!=null&&list.size()>0){
			Zqzrlb zqzr = list.get(0);
			
			int surplusDays = DateUtil.getDayBetweenDates(new java.util.Date(),zqzr.F05);//标的距离到期时间的剩余天数
			//int totalDays = DateUtil.getDayBetweenDates(DateUtil.dateAdd(zqzr.F20, 1),zqzr.F05);//原始债权一共计息的天数
			
			ApplyforDetailVO vo = new ApplyforDetailVO();
			vo.setApplyforId(zqzr.F25);
			vo.setKdbPlantId(zqzr.F24);
			vo.setTransferValue(zqzr.F02.doubleValue());
			vo.setZqSum(zqzr.F03.doubleValue());
			vo.setZqTitle(InterfaceConst.CREDIT_NAME_PREFIX+zqzr.F18);
			vo.setZqYield(String.valueOf(zqzr.F14.doubleValue()));
			vo.setZqTime(surplusDays);
			vo.setEndTimestamp(zqzr.F05.getTime());
			vo.setProductDetailUrl(Config.get("shop.detail.url")+zqzr.F24);//产品明细
			vo.setShopInfoUrl(Config.get("shop.info.url")+zqzr.F24);
			vo.setAssignmentAgreement(Config.get("assignment.agreement.url"));
			
			//预期本息
			BigDecimal totalEarning = CalculateEarningsUtil.calEarnings(InterfaceConst.YCFQ, zqzr.F10, zqzr.F14.doubleValue(), zqzr.F15);//总收益（预期）
			//BigDecimal surplusEarning = totalEarning.divide(BigDecimal.valueOf(totalDays),2,BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(surplusDays));//剩下的收益
			vo.setExpectEarning(totalEarning.add(zqzr.F10).doubleValue());
			
			//获取合同
			Map<String, Object> accessoryMap = new HashMap<String, Object>();
			accessoryMap.put("bidId", zqzr.F24);
			accessoryMap.put("accessoryType", Config.get("shop.accessory.type"));
			List<T6232> publicAccessoryList = publicAccessoryDao.getPublicAccessory(accessoryMap);
			String[] contractUrls = new String[publicAccessoryList.size()];
			for(int i=0;i<publicAccessoryList.size();i++){
				T6232 accessory = publicAccessoryList.get(i);
				String fileUrl=FileUtils.getPicURL(accessory.F04,Config.get("contact.url"));
				contractUrls[i] = fileUrl;
			}
			vo.setContractUrl(contractUrls);
			vo.setContractInfoUrl(Config.get("contactInfo.url")+URLEncoder.encode(JSONArray.toJSONString(contractUrls)));
			
			return vo;
		}
		return null;
	}
	
	@Override
	public ApplyforDetailVO_131 applyforDetail_131(String status, int applyforId,String isTransfer) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		map.put("applyforId", applyforId);
		map.put("isTransfer", isTransfer);
		List<Zqzrlb> list = creditAssigmentDao.getCreditassignmentApplyforList(map);
		if(list!=null&&list.size()>0){
			Zqzrlb zqzr = list.get(0);
			
			int surplusDays = DateUtil.getDayBetweenDates(new java.util.Date(),zqzr.F05);//标的距离到期时间的剩余天数
			//int totalDays = DateUtil.getDayBetweenDates(DateUtil.dateAdd(zqzr.F20, 1),zqzr.F05);//原始债权一共计息的天数
			
			ApplyforDetailVO_131 vo = new ApplyforDetailVO_131();
			vo.setApplyforId(zqzr.F25);
			vo.setBidId(zqzr.F24);
			vo.setTransferValue(zqzr.F02.doubleValue());
			vo.setZqSum(zqzr.F03.doubleValue());
			vo.setZqTitle(InterfaceConst.CREDIT_NAME_PREFIX+zqzr.F18);
			vo.setZqYield(String.valueOf(zqzr.F14.doubleValue()));
			vo.setZqTime(surplusDays);
			vo.setEndTimestamp(zqzr.F05.getTime()/1000);
			vo.setAssignmentAgreement(Config.get("assignment.agreement.url"));
			
			//预期本息
			BigDecimal totalEarning = CalculateEarningsUtil.calEarnings(InterfaceConst.YCFQ, zqzr.F10, zqzr.F14.doubleValue(), zqzr.F15);//总收益（预期）
			//BigDecimal surplusEarning = totalEarning.divide(BigDecimal.valueOf(totalDays),2,BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(surplusDays));//剩下的收益
			vo.setExpectEarning(totalEarning.add(zqzr.F10).doubleValue());
			
			//获取合同
			Map<String, Object> accessoryMap = new HashMap<String, Object>();
			accessoryMap.put("bidId", zqzr.F24);
			accessoryMap.put("accessoryType", Config.get("shop.accessory.type"));
			List<T6232> publicAccessoryList = publicAccessoryDao.getPublicAccessory(accessoryMap);
			String[] contractUrls = new String[publicAccessoryList.size()];
			for(int i=0;i<publicAccessoryList.size();i++){
				T6232 accessory = publicAccessoryList.get(i);
				String fileUrl=FileUtils.getPicURL(accessory.F04,Config.get("contact.url"));
				contractUrls[i] = fileUrl;
			}
			vo.setLawFiles(contractUrls);
			vo.setLawFileUrl(Config.get("contactInfo.url")+URLEncoder.encode(JSONArray.toJSONString(contractUrls)));
			vo.setRemark(zqzr.F27);
			vo.setGroupInfoList(bidInfoService.getGroupInfoList(zqzr.F24));
			vo.setBorrowerUrl(Config.get("bid.borrower.url")+zqzr.F24);
			if(zqzr.F26!=null && !"".equals(zqzr.F26)){
				vo.setAssetTypes(zqzr.F26.split(","));
			}else{
				vo.setAssetTypes(new String[0]);
			}
			return vo;
		}
		return null;
	}

	@Override
	public int getCreditassignmentCount(int zqId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("zqId", zqId);
		return this.creditAssigmentDao.getCreditassignmentCount(map);
	}

	@Override
	public int getRecordCount(int applyforId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("applyforId", applyforId);
		return this.creditAssigmentDao.getRecord(map);
	}

}
