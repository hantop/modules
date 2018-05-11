package com.fenlibao.p2p.service.creditassignment.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dimeng.p2p.S62.entities.T6232;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6260_F07;
import com.dimeng.p2p.XyType;
import com.fenlibao.p2p.dao.PublicAccessoryDao;
import com.fenlibao.p2p.dao.creditassignment.CreditAssigmentDao;
import com.fenlibao.p2p.dao.redpacket.RedpacketDao;
import com.fenlibao.p2p.dao.user.SpecialUserDao;
import com.fenlibao.p2p.model.entity.FeeType;
import com.fenlibao.p2p.model.entity.Finacing;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.bid.BidBaseInfo;
import com.fenlibao.p2p.model.entity.creditassignment.AddTransfer;
import com.fenlibao.p2p.model.entity.creditassignment.Zqzrlb;
import com.fenlibao.p2p.model.entity.finacing.RepaymentInfo;
import com.fenlibao.p2p.model.entity.plan.InvestPlanInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlan;
import com.fenlibao.p2p.model.entity.plan.UserPlanProduct;
import com.fenlibao.p2p.model.entity.plan.UserPlanRepayment;
import com.fenlibao.p2p.model.enums.SpecialUserType;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.enums.bid.RepaymentModeEnum;
import com.fenlibao.p2p.model.enums.plan.PlanStatusEnum;
import com.fenlibao.p2p.model.enums.plan.PlanTypeEnum;
import com.fenlibao.p2p.model.enums.plan.UserPlanRepay_State;
import com.fenlibao.p2p.model.enums.plan.UserPlanStatusEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.FeeCode;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.global.Status;
import com.fenlibao.p2p.model.vo.creditassignment.ApplyforDetailVO;
import com.fenlibao.p2p.model.vo.creditassignment.ApplyforDetailVO_131;
import com.fenlibao.p2p.model.xinwang.entity.trade.BusinessType;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.service.FinacingService;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.base.abstracts.BaseAbstractService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.bid.PlanService;
import com.fenlibao.p2p.service.creditassignment.ZqzrManage;
import com.fenlibao.p2p.service.funds.IFundsService;
import com.fenlibao.p2p.service.privateMessage.PrivateMessageService;
import com.fenlibao.p2p.service.sms.SmsExtracterService;
import com.fenlibao.p2p.service.xinwang.credit.XWCreditService;
import com.fenlibao.p2p.service.xinwang.trade.XWSyncTransactionService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.Pager;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Sender;
import com.fenlibao.p2p.util.pay.CalculateEarningsUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import com.mysql.jdbc.Statement;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.sql.*;
import java.util.ArrayList;
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
    FinacingService finacingService;

    @Resource
    RedpacketDao redpacketDao;

    @Resource
    IFundsService iFundsService;

    @Resource
    SmsExtracterService smsExtracterService;

    @Resource
    PrivateMessageService privateMessageService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private XWCreditService xwCreditService;

    @Resource
    private PlanService planService;

    @Resource
    private SpecialUserDao specialUserDao;

    @Resource
    private XWSyncTransactionService xwSyncTransactionService;


    @Override
    public Timestamp transfer(AddTransfer query) throws Throwable
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
        Timestamp timestamp=null;
        try (Connection connection = getConnection())
        {
            timestamp=getCurrentTimestamp(connection);
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
                connection.prepareStatement("SELECT 1 FROM S62.T6251 INNER JOIN S62.T6252 ON T6252.F11 = T6251.F01 AND T6252.F09 = 'HKZ' WHERE T6251.F01 = ? LIMIT 1 "))
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
                ps.setTimestamp(4, timestamp);
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
            try (PreparedStatement ps = connection.prepareStatement("UPDATE S62.T6251 SET F08 = ? WHERE F01 = ?")) {
                ps.setString(1, T6251_F08.S.name());
                ps.setInt(2, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO S62.T6261 SET F01 = ?, F02 = ?")) {
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

        return timestamp;
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

        return !(transferValue.compareTo(maxRange) > 0 || transferValue.compareTo(minRange) < 0);
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
//                    nextDate.setTime(nextDate.getTime() - DateHelper.DAY_IN_MILLISECONDS * lastDays);
                    //产品要求，不需要控制最后5天不能转让  updated by zcai 20160704
//                    if (DateHelper.beforeDate(nextDate, date)) {
//                    	throw new BusinessException(ResponseCode.ZQZR_SJKD.getCode(),ResponseCode.ZQZR_SJKD.getMessage());
//                    }
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

    @Override
    public List<InvestPlanInfo> getExpirePlans(int limit) {
        return creditAssigmentDao.getExpirePlans(limit);
    }

    @Override
    public List<UserPlan> getExpireUserPlans(int planId,int status) {
        return creditAssigmentDao.getExpireUserPlans(planId, status);
    }

    @Override
    public int updateInvestPlanInfo(int planId, int planStatus) {
        return creditAssigmentDao.updateInvestPlanInfo(planId, planStatus);
    }

    @Transactional
    @Override
    public void doTansferAndRepayPlan(UserPlan userPlan,Boolean isAuto) throws Throwable {
        creditAssigmentDao.lockUserPlanById(userPlan.getUserPlanId(),UserPlanStatusEnum.CYZ.getCode());
        UserPlan plan = creditAssigmentDao.getUserPlanInfo(userPlan.getUserPlanId(),UserPlanStatusEnum.CYZ.getCode());
         if(plan==null){
            logger.error("(用户计划退出)userPlan不存在:userPlan:{}", JSON.toJSONString(userPlan));
            throw new BusinessException("(用户计划退出)userPlan不存在") ;
        }
        //本息
        BigDecimal totalAmout = getTotalAmout(plan,isAuto);
        // 用户投资计划对应的债权或投资记录(存在未还)
        List<UserPlanProduct> userPlanProducts = creditAssigmentDao.getUserPlanProducts(plan.getUserPlanId());
        if(userPlanProducts!=null && userPlanProducts.size()>0){
            //计划本金
            BigDecimal investAmount = plan.getInvestAmount();

            //总投资金额
            BigDecimal totalInvestAmout = (BigDecimal) creditAssigmentDao.getTotalnAmoutParam(plan.getUserPlanId()).get("totalInvestAmout");
            //总已还金额
            BigDecimal totalReturnedAmout = (BigDecimal) creditAssigmentDao.getTotalnAmoutParam(plan.getUserPlanId()).get("totalReturnedAmout");
            //可复投金额
            BigDecimal cycleInvestAmout = investAmount.add(totalReturnedAmout).subtract(totalInvestAmout);
            //总待还金额
            BigDecimal totalBeReturnAmout = (BigDecimal) creditAssigmentDao.getTotalReturnAmout(plan.getUserPlanId()).get("totalReturnAmout");
            if(totalBeReturnAmout.compareTo(BigDecimal.ZERO)>0){
                //比例系数
                BigDecimal proportion = totalAmout.subtract(cycleInvestAmout).divide(totalBeReturnAmout,2,BigDecimal.ROUND_DOWN);
                for(UserPlanProduct userPlanProduct:userPlanProducts){
                    UserPlanRepayment repayment= new UserPlanRepayment();
                    BigDecimal zqValue; //未还本息
                    if("F".equals(userPlanProduct.getYq())){
                        //判断债权是否产生,已生成的转出
                        if(userPlanProduct.getProductId()!=null){
//                            planProductTransfer(plan.getUserId(),userPlanProduct.getProductId());
                            zqValue = userPlanProduct.getZqValue();
                        }else{ //债权未产生
                            zqValue = userPlanProduct.getInvestAmount();
                        }
                    }else{//逾期的时候
                        zqValue = userPlanProduct.getZqValue();
                    }
                    BigDecimal amount = zqValue.multiply(proportion).setScale(2,BigDecimal.ROUND_HALF_UP);
                    if(amount.compareTo(BigDecimal.ZERO)<0){
                        amount=BigDecimal.ZERO;//不能为负数
                    }
                    repayment.setAmount(amount);
                    if(isAuto){
                        repayment.setRepaymentDate(DateUtil.dateAdd(plan.getExpireTime(),InterfaceConst.PLAN_EXPIRE_DAYS));
                    }else{
                        repayment.setRepaymentDate(DateUtil.dateAdd(plan.getApplyQuitTime(),InterfaceConst.PLAN_USER_QUIT_DAYS));
                    }
                    repayment.setUserPlanProductId(userPlanProduct.getUserPlanProductId());
                    repayment.setPayeeId(plan.getUserId());
                    repayment.setUserPlanId(plan.getUserPlanId());
                    creditAssigmentDao.addUserPlanRepayment(repayment);
                }
            }
        }
        //债权状态更新
        int re = creditAssigmentDao.updateUserPlan(plan.getUserPlanId(), UserPlanStatusEnum.SQTC.getCode(),null);
        if(re!=1){
            logger.error("(计划退出)债权状态更新失败:userPlan:{}", JSON.toJSONString(userPlan));
            throw new BusinessException("债权状态更新失败") ;
        }
        // modify by zeronx 2017-09-12 13:40 将发送计划回款消息放置全部回款后发送
    }

    //本息(包含退出费用)
    public BigDecimal getTotalAmout(UserPlan plan,Boolean isAuto){
        //计算计划预期本息
        BigDecimal investAmount = plan.getInvestAmount();
        BigDecimal totalRate = plan.getTotalRate();
        String cycleType = plan.getCycleType();
        int cycle =  plan.getCycle();
        //借款周期参数
        int period="d".equals(cycleType)?365:12;
        //总收益
        BigDecimal totalEarning = investAmount
                .multiply(totalRate)
                .multiply(new BigDecimal(cycle))
                .divide(BigDecimal.valueOf(period),2,BigDecimal.ROUND_HALF_UP);
        if(!isAuto){//用户申请退出
            totalEarning = totalEarning
                    .divide(BigDecimal.valueOf(plan.getTotalDays()),2,BigDecimal.ROUND_DOWN)
                    .multiply(BigDecimal.valueOf(plan.getPassDays()));
        }
        BigDecimal totalAmout = investAmount.add(totalEarning);
        //省心计划,提前退出收费
        if(PlanTypeEnum.SXJH.getCode() == plan.getPlanType() && !isAuto){
            if(plan.getPassDays()<=30)//30天以下（含30天）的省心计划支持提前退出，收取1.0%手续费
            totalAmout = totalAmout.subtract(totalAmout.multiply(InvestPlanInfo.FEED_RATE).setScale(2,BigDecimal.ROUND_HALF_UP));
        }
        return totalAmout;
    }


    @Override
    public void planProductTransfer(final int userId, int ProductId) throws Throwable {
    final String zqId=String.valueOf(ProductId);
    List<Finacing> list = this.finacingService.getFinacing(String.valueOf(userId), null, String.valueOf(ProductId));
    if (null != list && list.size() > 0) {
        Finacing finacing = list.get(0);
        //校验规则
        if (Status.S.name().equals(finacing.getIsTransfer())) {//正在转让中
            return;
        }
        //持有天数利息
        BigDecimal expectedProfit = getExpectedProfit(finacing, zqId, String.valueOf(userId));
        final BigDecimal finalExpectedProfit = expectedProfit;
        double bidMoney = getBidValue(finacing, zqId, String.valueOf(userId));
        //投标金额
        final double bidValue = bidMoney;
        //默认转让价格 = 债权金额 + 持有天数利息
        final  double TransferValue = bidMoney + expectedProfit.doubleValue();
        //3.2用户计划 债权退出费率为0
        BigDecimal assignmentRate = BigDecimal.ZERO;
        final BigDecimal bidAssignmentRate = assignmentRate;

        transfer(new AddTransfer() {
            @Override
            public BigDecimal getTransferValue() {
                return new BigDecimal(TransferValue);
            }
            @Override
            public int getTransferId() {
                return Integer.parseInt(zqId);
            }
            @Override
            public BigDecimal getRateMoney() {
                return bidAssignmentRate;
            }
            @Override
            public BigDecimal getBidValue() {
                return new BigDecimal(bidValue);
            }
            @Override
            public BigDecimal getPassedEarning() {
                return finalExpectedProfit;
            }
        });

        }
    }

    @Override
    public List<UserPlan> getUserQuitPlans(int limit) {
        return creditAssigmentDao.getUserQuitPlans(limit);
    }

    @Override
    public List<UserPlanProduct> getUserPlanProductsNeedPfBuy(int limit, VersionTypeEnum versionTypeEnum) {
        return creditAssigmentDao.getUserPlanProductsNeedPfBuy(limit, versionTypeEnum);
    }

    @Override
    public List<UserPlan> getUserPlansInQuit(Integer limit) {
        return creditAssigmentDao.getUserPlansInQuit(limit);
    }

    @Transactional
    @Override
    public void doSettlementPlan(UserPlan userPlan) throws Exception {
        creditAssigmentDao.lockUserPlanById(userPlan.getUserPlanId(),UserPlanStatusEnum.SQTC.getCode());
        UserPlan plan = creditAssigmentDao.getUserPlanInfo(userPlan.getUserPlanId(),UserPlanStatusEnum.SQTC.getCode());
        if(plan==null){
            logger.error("(计划退出结算)userPlan不存在:userPlan:{}", JSON.toJSONString(userPlan));
            throw new BusinessException("(计划退出结算)userPlan不存在") ;
        }
        VersionTypeEnum cgMode = planService.getPlanVersion(plan.getPlanId());
        //计划本金
        BigDecimal investAmount = plan.getInvestAmount();

        //总待还金额
        BigDecimal totalBeReturnAmout = (BigDecimal) creditAssigmentDao.getTotalReturnAmout(plan.getUserPlanId()).get("totalReturnAmout");

        //总投资金额
        BigDecimal totalInvestAmout = (BigDecimal) creditAssigmentDao.getTotalnAmoutParam(plan.getUserPlanId()).get("totalInvestAmout");
        //总已还金额
        BigDecimal totalReturnedAmout = (BigDecimal) creditAssigmentDao.getTotalnAmoutParam(plan.getUserPlanId()).get("totalReturnedAmout");
        //可复投金额 - 用户计划冻结金额
        BigDecimal cycleInvestAmout = investAmount.add(totalReturnedAmout).subtract(totalInvestAmout);
        //累计已结算金额
        Map<String,Object> settlementAmoutMap = creditAssigmentDao.getUserPlanSettlementAmout(userPlan.getUserPlanId());
        BigDecimal totalSettlementAmout = settlementAmoutMap != null? (BigDecimal)settlementAmoutMap.get("totalAmout"):BigDecimal.ZERO;
        boolean isAuto = plan.getPassDays()==null;

        if(cycleInvestAmout.compareTo(totalSettlementAmout)>0){
            //计划回款
            FeeType feeType = redpacketDao.getFeeType(FeeCode.JHJSHK);
            if(!isAuto){
                feeType = redpacketDao.getFeeType(FeeCode.JHTQHK);
            }
            feeType.setName(feeType.getName()+":"+plan.getPlanName());
            //解冻锁定账户金额
            int t6102_f01 = iFundsService.unlockUserSDZHBalance(userPlan.getUserId(), cycleInvestAmout.subtract(totalSettlementAmout), feeType,cgMode);
            //添加结清记录
            creditAssigmentDao.saveSettlementRecord(userPlan.getUserPlanId(),t6102_f01);
        }
        if(totalBeReturnAmout.compareTo(BigDecimal.ZERO)==0){
            //本息
            BigDecimal totalAmout = BigDecimal.ZERO;
            if (VersionTypeEnum.CG.equals(cgMode)) {
                totalAmout = getTotalAmountCG(plan,isAuto);
            } else { // 普通版
                //本息
                totalAmout = getTotalAmout(plan,isAuto);
                //逾期罚息-最后算一次
                Map<String,Object> yqAmoutMap =creditAssigmentDao.getYqAmout(plan.getUserPlanId());
                BigDecimal yqAmout = yqAmoutMap!=null?(BigDecimal) yqAmoutMap.get("yqAmout"):BigDecimal.ZERO;
                totalAmout = totalAmout.add(yqAmout);
            }

            BigDecimal rateManageRatio = creditAssigmentDao.getRateManageRatioByPlanId(plan.getPlanId());
            if (rateManageRatio == null) {
                rateManageRatio = BigDecimal.ZERO;
            }
            totalAmout = totalAmout.subtract(investAmount.multiply(rateManageRatio));
            if(cycleInvestAmout.compareTo(totalAmout)<0){
                BigDecimal  balance = totalAmout.subtract(cycleInvestAmout);//差额
                //计划回款
                FeeType feeType = redpacketDao.getFeeType(FeeCode.JHJSHK);
                if(!isAuto){
                     feeType = redpacketDao.getFeeType(FeeCode.JHTQHK);
                }
                feeType.setName(feeType.getName()+":"+plan.getPlanName());
                int t6102_f01 ;
                if (VersionTypeEnum.PT.equals(cgMode)) {
                    t6102_f01 = iFundsService.platformTransfer(plan.getUserId(), balance, feeType, InterfaceConst.ACCOUNT_TYPE_WLZH);
                } else {//使用返现红包的方式补足差额给用户
                    BusinessType businessType = new BusinessType();
                    businessType.setName(feeType.getName());
                    businessType.setCode(feeType.getCode());
                    businessType.setStatus(feeType.getStatus());
                    List<BigDecimal> amount = new ArrayList<>(1);
                    amount.add(balance);
                    Map<String, Object> map = xwSyncTransactionService.syncTransactionMarketingForPlanSettle(plan.getUserId(), UserRole.INVESTOR.getCode(), amount, businessType, null);
                    t6102_f01 = (int) map.get("flowId");
                }
                //添加结清记录
                creditAssigmentDao.saveSettlementRecord(userPlan.getUserPlanId(),t6102_f01);
            }
            int re = creditAssigmentDao.updateUserPlan(plan.getUserPlanId(), UserPlanStatusEnum.YTC.getCode(),new java.util.Date());
            if(re!=1){
                logger.error("(计划退出)债权状态更新失败:userPlan:{}", JSON.toJSONString(userPlan));
                throw new BusinessException("债权状态更新失败") ;
            }
            //用户投资计划回款状态更新
            creditAssigmentDao.updateUserPlanRepay(plan.getUserPlanId(),
                    isAuto?UserPlanRepay_State.YH.name():UserPlanRepay_State.TQH.name(),
                    new java.util.Date());
            //计划结清
            creditAssigmentDao.updateInvestPlan(plan.getPlanId(), PlanStatusEnum.YJQ.getCode(), new java.util.Date());
            // add by zeronx 2017-09-12 13：40 添加计划结清发送消息
            if(isAuto){
                try {
                    UserInfo UserInfo= userInfoService.getUserInfo(userPlan.getUserId());
                    List<String> userIds = specialUserDao.getUserIds(SpecialUserType.INVEST_PLAN_NOT_FULL);
                    if (userIds.contains("" + userPlan.getUserId())) {
                        return;
                    }
                    // 发送短
                    String smsPattern = String.format(Sender.get("sms.plan.expire.content")
                            ,plan.getPlanName(),plan.getInvestAmount(),totalAmout.subtract(plan.getInvestAmount()));
                    smsExtracterService.sendMsg(UserInfo.getPhone(),smsPattern,2);

                    String znx = String.format(Sender.get("znx.plan.expire.content")
                            ,plan.getPlanName(),plan.getInvestAmount(),totalAmout.subtract(plan.getInvestAmount()));
                    privateMessageService.sendLetter(String.valueOf(plan.getUserId()), "系统消息", znx, planService.getPlanVersion(userPlan.getPlanId()));
                }catch (Exception e){
                    logger.error("(计划退出)添加短信发送失败:userPlan:{}", JSON.toJSONString(userPlan));
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取存管版投资计划的本金+利息
     * （1）到期已回款利息 = 回款本金 * 计划利率 * 组合天数 / 365
     * （2）到期未回款利息 = 未回本金 * 计划利率 * 持有天数 / 365
     * @param plan
     * @param isAuto
     * @return
     */
    private BigDecimal getTotalAmountCG(UserPlan plan, boolean isAuto) {
        //计算计划预期本息
        BigDecimal investAmount = plan.getInvestAmount();
        BigDecimal totalRate = plan.getTotalRate();
        String cycleType = plan.getCycleType();
        int cycle =  plan.getCycle();
        java.util.Date expireTime = plan.getExpireTime();
//        BigDecimal beforeAmount = (BigDecimal) creditAssigmentDao.getExpireTimeBeforeReturnAmount(plan.getUserPlanId(), expireTime).get("beforeReturnAmount");
        List<Map<String, Object>> afterReturnAmounts = creditAssigmentDao.getExpireTimeAfterReturnAmount(plan.getUserPlanId(), expireTime);

        //借款周期参数
        int period="d".equals(cycleType)?365:12;
        //到期已回本金收益 舍弃（存在一种情况是有些计划的钱没有匹配到标）
//        BigDecimal beforeEarning = beforeAmount
//                .multiply(totalRate)
//                .multiply(new BigDecimal(cycle))
//                .divide(BigDecimal.valueOf(period),2,BigDecimal.ROUND_HALF_UP);
        // 到期没回本金收益
        BigDecimal afterEarning = BigDecimal.ZERO;
        BigDecimal afterAmount = BigDecimal.ZERO; // 某个日期没回本金的总金额
        BigDecimal afterAmountSum = BigDecimal.ZERO; // 某个日期没回本金的总金额
        BigDecimal temp; // 某个日期没回本金的总金额的利息
        int tempCycle; // 没回本金超出的天数
        for (Map<String, Object> afterReturnAmountMap : afterReturnAmounts) {
            afterAmount = (BigDecimal) afterReturnAmountMap.get("sumAmount");
            DateTime begin = new DateTime(new DateTime(expireTime).toString("yyyy-MM-dd"));
            DateTime end = new DateTime(new DateTime(afterReturnAmountMap.get("dateTime")).toString("yyyy-MM-dd"));
            //计算区间天数
            Period p = new Period(begin, end, PeriodType.days());
            tempCycle = p.getDays();
            temp = afterAmount.multiply(totalRate).multiply(new BigDecimal(cycle + tempCycle)).divide(BigDecimal.valueOf(period),2,BigDecimal.ROUND_HALF_UP);
            afterEarning = afterEarning.add(temp);
            afterAmountSum = afterAmountSum.add(afterAmount);
        }
        // 计划到期已回款的金额（投资计划的钱没有匹配到标的钱也在内）
        BigDecimal freeAmount = investAmount.subtract(afterAmountSum);
        //到期计划
        BigDecimal freeEarning = freeAmount
                .multiply(totalRate)
                .multiply(new BigDecimal(cycle))
                .divide(BigDecimal.valueOf(period),2,BigDecimal.ROUND_HALF_UP);

        BigDecimal totalAmount = investAmount.add(freeEarning).add(afterEarning);
        return totalAmount;
    }

    //算利息
    public BigDecimal getExpectedProfit(Finacing finacing,String zqId,String userId){
        BigDecimal expectedProfit =BigDecimal.ZERO;
        //借款周期参数
        int loanPeriod=finacing.getMonth()==0?finacing.getLoanDays():finacing.getMonth();
        int period=finacing.getMonth()==0?365:12;

        //一次还清的利息计算
        if (RepaymentModeEnum.YCFQ.getCode().equals(finacing.getRepaymentMethod())) {
            //已过天数的收益（未到账）
            int passedDays = DateUtil.getDayBetweenDates(finacing.getCreateTime(),DateUtil.nowDate());//已算收益天数
            int totalDays = DateUtil.getDayBetweenDates(finacing.getBeginTimestamp(),finacing.getEndTimestamp());//当前债权一共计息的天数

            if(totalDays>0){
                BigDecimal totalEarning = new BigDecimal(finacing.getOriginalMoney())
                        .multiply(new BigDecimal(finacing.getRate()))
                        .multiply(new BigDecimal(loanPeriod))
                        .divide(BigDecimal.valueOf(period),2,BigDecimal.ROUND_HALF_UP);
                expectedProfit = totalEarning.divide(BigDecimal.valueOf(totalDays),2,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(passedDays));
            }
        }

        //等额本息的收益计算
        if (RepaymentModeEnum.DEBX.getCode().equals(finacing.getRepaymentMethod())) {
            //查出下一期回款计划里面的利息
            int[] tradeTypes = new int[]{ FeeCode.TZ_LX,FeeCode.TZ_JXJL};
            Map nextRepaymentItemProfit = finacingService.getNextRepaymentItemProfit(Integer.parseInt(userId)
                    ,Integer.parseInt(zqId),tradeTypes);

            BigDecimal totalEarning=BigDecimal.ZERO;
            if(nextRepaymentItemProfit!=null)totalEarning= (BigDecimal) nextRepaymentItemProfit.get("profitRepaymentAmount");

            tradeTypes = new int[]{ FeeCode.TZ_LX};
            RepaymentInfo lastRepaymentInfo = finacingService.getLastRepaymentItem(Integer.parseInt(userId)
                    ,Integer.parseInt(zqId),tradeTypes);  //上一期的回款计划
            java.util.Date lastRepaymentDate = lastRepaymentInfo==null?finacing.getCreateTime():lastRepaymentInfo.getExpectedRepaymentDate();

            List<RepaymentInfo> nextRepayment = finacingService.getNextRepaymentItem(Integer.parseInt(userId)
                    ,Integer.parseInt(zqId),tradeTypes);  //下一期的回款计划
            java.util.Date nextRepaymentDate = nextRepayment.get(0).getExpectedRepaymentDate();

            //当月已过天数的收益（未到账）
            int passedDays= DateUtil.daysToLastRepaymentDay(lastRepaymentDate,new java.util.Date());//当月收益天数,当天收益不计
            int totalDays = DateUtil.getDayBetweenDates(lastRepaymentDate,nextRepaymentDate);//当月债权一共计息的天数
            if(totalDays>0){
                expectedProfit = totalEarning.divide(BigDecimal.valueOf(totalDays),2,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(passedDays));
            }
        }

        //每月付息到期还本的收益计算
        if (RepaymentModeEnum.MYFX.getCode().equals(finacing.getRepaymentMethod())) {
            //当月已过天数的收益（未到账）
            int[] tradeTypes = new int[]{ FeeCode.TZ_LX};
            RepaymentInfo  repaymentInfo = finacingService.getLastRepaymentItem(Integer.parseInt(userId)
                    ,Integer.parseInt(zqId),tradeTypes);  // 上一期的回款计划
            java.util.Date effectDate=repaymentInfo==null?finacing.getCreateTime():repaymentInfo.getExpectedRepaymentDate();

            int passedDays= DateUtil.daysToLastRepaymentDay(effectDate,new java.util.Date());//当月收益天数,当天收益不计
            int totalDays = DateUtil.getDayBetweenDates(finacing.getBeginTimestamp(),finacing.getEndTimestamp());//当前债权一共计息的天数
            if(totalDays>0){
                BigDecimal totalEarning = new BigDecimal(finacing.getOriginalMoney())
                        .multiply(new BigDecimal(finacing.getRate()))
                        .multiply(new BigDecimal(loanPeriod))
                        .divide(BigDecimal.valueOf(period),2,BigDecimal.ROUND_HALF_UP);
                expectedProfit = totalEarning.divide(BigDecimal.valueOf(totalDays),2,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(passedDays));
            }
        }
        return expectedProfit.compareTo(BigDecimal.ZERO)<0?BigDecimal.ZERO:expectedProfit;
    }

    public double getBidValue(Finacing finacing,String zqId,String userId){
        //债权金额--等额本息方式要计算本金
        double bidMoney = finacing.getOriginalMoney();
        if (RepaymentModeEnum.DEBX.getCode().equals(finacing.getRepaymentMethod())) {//等额本息的本金计算
            //查出剩余期数未还本金
            int[] tradeTypes = new int[]{ FeeCode.TZ_BJ};
            List<RepaymentInfo>  repaymentInfos = finacingService.getUserRepaymentItem(Integer.parseInt(userId)
                    ,Integer.parseInt(zqId),tradeTypes);
            BigDecimal creditCapitalAmount=BigDecimal.ZERO;
            for(RepaymentInfo item:repaymentInfos){
                if("WH".equals(item.getRepaymentStatus())){
                    creditCapitalAmount = creditCapitalAmount.add(item.getRepaymentAmount());
                }
            }
            creditCapitalAmount=creditCapitalAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
            bidMoney=creditCapitalAmount.doubleValue();
        }
        return bidMoney;
    }

    public static void main(String[] args) {
        BigDecimal investAmount = new BigDecimal(1000.00);
        BigDecimal totalRate = new BigDecimal(0.08);
        String cycleType = "m";
        int cycle =  6;
        //借款周期参数
        int period="d".equals(cycleType)?365:12;
        //总收益
        BigDecimal totalEarning = investAmount
                .multiply(totalRate)
                .multiply(new BigDecimal(cycle))
                .divide(BigDecimal.valueOf(period),2,BigDecimal.ROUND_HALF_UP);
        System.out.println(totalEarning);
        BigDecimal totalAmout = investAmount.add(totalEarning);
        System.out.println(totalAmout);
        BigDecimal b = new BigDecimal(141058.63);
        BigDecimal a = new BigDecimal(141213.17);
        a=a.subtract(b);//154.54
        System.out.println(a);//
        b = new BigDecimal(10069.86);
        a = new BigDecimal(10107.48);
        a=a.subtract(b);//37.62 + 154.54 = 192.16
        System.out.println(a);//1012.17-996.51 = 15
        b = new BigDecimal(996.51);
        a = new BigDecimal(1012.17);
        System.out.println(a);//
        a=a.subtract(b);//15.66
        double c=15.65;
        c=c*9;
    }

    @Override
    public List<Integer> getTargetCreditId(int userId) {
        return creditAssigmentDao.getTargetCreditId(userId);
    }

    @Override
    public List<Integer> getInCreditId(int userId) {
        return creditAssigmentDao.getInCreditId(userId);
    }
}
