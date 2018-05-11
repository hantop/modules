package com.fenlibao.p2p.service.mp.impl;

import com.dimeng.p2p.S61.enums.T6101_F03;
import com.fenlibao.p2p.dao.mq.MemberPointsDao;
import com.fenlibao.p2p.dao.mq.UserFundsTenderDao;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.FeeCode;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.mp.entity.*;
import com.fenlibao.p2p.service.mp.MemberPointsService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.mp.PointChangeType;
import com.fenlibao.p2p.util.mp.PointExchangeStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @title  积分service
 * @author laubrence
 * @date   2016-2-6 01:21:25
 */
@Service
public class MemberPointsServiceImpl implements MemberPointsService {
	
    @Resource
    private MemberPointsDao memberPointsDao;

    @Resource
    private UserFundsTenderDao userFundsTenderDao;
    
	@Override
	public MyPoint getMyPointsNum(int userId) throws Exception{
		MyPoint myPoint = memberPointsDao.getMyPointsNum(userId);
		if(myPoint == null ){
			throw new BusinessException(ResponseCode.MP_MY_POINTS_ACCOUNT_NOT_FOUND.getCode(),ResponseCode.MP_MY_POINTS_ACCOUNT_NOT_FOUND.getMessage());
		}
		if(myPoint.getPointStatus() == 2){
			throw new BusinessException(ResponseCode.MP_MY_POINTS_ACCOUNT_FREEZE.getCode(),ResponseCode.MP_MY_POINTS_ACCOUNT_FREEZE.getMessage());
		}
		if(myPoint.getPointStatus() == 0){
			throw new BusinessException(ResponseCode.MP_MY_POINTS_ACCOUNT_FORBID.getCode(),ResponseCode.MP_MY_POINTS_ACCOUNT_FORBID.getMessage());
		}
		if(myPoint.getPointStatus() == 1){
			return myPoint;
		}
		return null;
	}

	@Override
	public MyPoint getPoints(int userId) throws Exception {
		MyPoint myPoint = memberPointsDao.getMyPointsNum(userId);
		if(myPoint == null ){
			throw new BusinessException(ResponseCode.MP_MY_POINTS_ACCOUNT_NOT_FOUND.getCode(),ResponseCode.MP_MY_POINTS_ACCOUNT_NOT_FOUND.getMessage());
		}
		return myPoint;
	}

	@Override
	public MyPointInfo getMyPoints(int userId) throws Exception {
		MyPointInfo myPoint = memberPointsDao.getMyPoints(userId);
		if(myPoint == null ){
			throw new BusinessException(ResponseCode.MP_MY_POINTS_ACCOUNT_NOT_FOUND.getCode(),ResponseCode.MP_MY_POINTS_ACCOUNT_NOT_FOUND.getMessage());
		}
		return myPoint;
	}

	@Override
	public List<MyPointExchangeDetail> getExchangeRecords(int userId, Integer page, Integer limit) {
		if(limit==null)limit= InterfaceConst.PAGESIZE;
		if(page!=null && page>0){
			page=(page-1)*limit;
		}else{
			page=0;
		}
		return memberPointsDao.getExchangeRecords(userId, page,limit);
	}


	@Override
	public List<UserPointDetail> getPointRecordsThirty(int userId) {
		Date nowDatetime = new Date();
		return memberPointsDao.getPointRecordsThirty(userId, nowDatetime);
	}

	@Override
	public List<UserPointDetail> getPointRecords(int userId, int changeType , String createTime) {
		Date createDatetime = null;
		if (StringUtils.isNotEmpty(createTime)) {
			createDatetime =  DateUtil.timestampToDateBySec(Long.valueOf(createTime));
        }
		return memberPointsDao.getPointRecords(userId, changeType, createDatetime);
	}

	@Override
	public BigDecimal getPointsExchangeCashAmount(String pTypeCode, int pNum ) throws Exception{
		Date nowDatetime = new Date();
		PointsExchangeCashInfo pointsExchangeCashInfo = memberPointsDao.getPointsExchangeCashConfigInfo(pTypeCode, nowDatetime);
		BigDecimal cashAmount = new BigDecimal(0.00);
		if(pointsExchangeCashInfo == null ){
			throw new BusinessException(ResponseCode.MP_POINTS_EXCHANGE_CASH_CONFIG_NOT_FOUND.getCode(),ResponseCode.MP_POINTS_EXCHANGE_CASH_CONFIG_NOT_FOUND.getMessage());
		}
		cashAmount = pointsExchangeCashInfo.getExchangeAmount().multiply(new BigDecimal(pNum));
		return cashAmount;
	}
	
	/**
	 * @title 积分兑换金额
	 * @param pTypeCode
	 * @return
	 * @throws Exception
	 */
	public PointsExchangeCashInfo getOnePointsExchangeCashAmount(String pTypeCode ) throws Exception{
		Date nowDatetime = new Date();
		return memberPointsDao.getPointsExchangeCashConfigInfo(pTypeCode, nowDatetime);
	}

	@Override
	public int getConsumeExchangePointsNum(String pTypeCode,
			BigDecimal amount) throws Exception {
		Date nowDatetime = new Date();
		int pNum = 0;
		ConsumeExchangePointsInfo pointsExchangeCashInfo = memberPointsDao.getConsumeExchangePointsConfigInfo(pTypeCode, nowDatetime);
		if(pointsExchangeCashInfo != null ){
			int amountCutDown = amount.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			pNum = amountCutDown * pointsExchangeCashInfo.getPointUnitNum();
		}else{
			throw new BusinessException(ResponseCode.MP_POINTS_EXCHANGE_CASH_CONFIG_NOT_FOUND.getCode(),ResponseCode.MP_POINTS_EXCHANGE_CASH_CONFIG_NOT_FOUND.getMessage());
		}
		return pNum;
	}
	
	@Transactional
	@Override
	public Map<String,Object> pointsExchangeCash(int userId, String pTypeCode, int pNum) throws Exception{
		//验证1：用户是否通过实名认证
		UserAuthStatus authStatus = userFundsTenderDao.getUserAuthInfo(userId);
		if(authStatus == null || "BTG".equals(authStatus.getIdentityStatus())){
			throw new BusinessException(ResponseCode.USER_IDENTITY_UNAUTH.getCode(),ResponseCode.USER_IDENTITY_UNAUTH.getMessage());
		}
		//验证2：积分类型是否可兑换
		boolean isCanExchange = this.pointsCanExchangeCash(userId,pTypeCode,pNum);
		if(isCanExchange!=true){
			throw new BusinessException(ResponseCode.MP_POINTS_EXCHANGE_CASH_CONFIG_NOT_VALID.getCode(),ResponseCode.MP_POINTS_EXCHANGE_CASH_CONFIG_NOT_VALID.getMessage());
		}
		int typeId=0;
		BigDecimal cashAmount = new BigDecimal(0.00);
		Date nowDatetime = new Date();
		
		//1.获取积分兑换金额数量
		PointsExchangeCashInfo pointsExchangeCashInfo = this.getOnePointsExchangeCashAmount(pTypeCode);
		if(pointsExchangeCashInfo == null){
			throw new BusinessException(ResponseCode.MP_POINTS_EXCHANGE_CASH_CONFIG_NOT_FOUND.getCode(),ResponseCode.MP_POINTS_EXCHANGE_CASH_CONFIG_NOT_FOUND.getMessage());
		}
		typeId = pointsExchangeCashInfo.getTypeId();
		cashAmount = pointsExchangeCashInfo.getExchangeAmount().multiply(new BigDecimal(pNum));
		//2.添加积分使用记录
		int rId = memberPointsDao.addPointsUseRecord(typeId,userId,pNum,cashAmount,PointExchangeStatus.EX_APPLY,nowDatetime);
		
		//3.积分兑换
		this.doExchangePoints(userId,pNum,cashAmount);
		this.updatePointsUseRecordStatus(rId,PointExchangeStatus.EX_SUCCESS);
		memberPointsDao.addPointsSheetRecord(typeId, userId, pNum, PointChangeType.PCT_OUTGOINGS, nowDatetime);
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("pNum", pNum);
		returnMap.put("cashAmount", cashAmount);
		return returnMap;
		
//		boolean is_success = false;
//		try{
//			//this.doExchangePoints(userId,pNum,cashAmount);
//			is_success = true;
//		}catch(BusinessException e){
//			throw e;
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			if(is_success){
//				this.updatePointsUseRecordStatus(rId,PointExchangeStatus.EX_SUCCESS);
//				memberPointsDao.addPointsSheetRecord(typeId, userId, pNum, PointChangeType.PCT_OUTGOINGS, nowDatetime);
//			}else{
//				this.updatePointsUseRecordStatus(rId,PointExchangeStatus.EX_FAILURE);
//			}
//		}
	}
	
	public void doExchangePoints(int userId, int pNum, BigDecimal cashAmount) throws Exception{
		Date nowDatetime = new Date();
		//减少用户账户积分
		minusUserAccountPoints(userId, pNum);
		
		/**
		 * 减少平台往来账户金额
		 */
		//获取平台用户id
		int ptUserId = userFundsTenderDao.getPTUserAccountId();
		if(ptUserId==0){
			throw new BusinessException(ResponseCode.COMMON_PLATFORM_ACCOUNT_NOT_EXIST.getCode(),ResponseCode.COMMON_PLATFORM_ACCOUNT_NOT_EXIST.getMessage());
		}
		//锁定平台往来账户
		UserFundsAccountInfo userPTFundsAccountInfo = userFundsTenderDao.getUserFundsAccountInfo(ptUserId, T6101_F03.WLZH.name());
		BigDecimal remainAmount = userPTFundsAccountInfo.getFundsAmount().subtract(cashAmount);

		if(remainAmount.compareTo(BigDecimal.ZERO) < 0 ){
			throw new BusinessException(ResponseCode.COMMON_PLATFORM_ACCOUNT_BALANCE_LACK.getCode(),ResponseCode.COMMON_PLATFORM_ACCOUNT_BALANCE_LACK.getMessage());
		}
		//更新平台往来账户余额
		userFundsTenderDao.updateUserFundsAccountAmount(userPTFundsAccountInfo.getFundsAccountId(), remainAmount, nowDatetime);
	
		/**
		 * 增加用户往来账户金额
		 */
		//锁定用户往来账户
		UserFundsAccountInfo userFundsAccountInfo = userFundsTenderDao.getUserFundsAccountInfo(userId, T6101_F03.WLZH.name());
		if(userFundsAccountInfo == null){
			throw new BusinessException(ResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST.getCode(),ResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST.getMessage());
		}
		BigDecimal increaseAmount = userFundsAccountInfo.getFundsAmount().add(cashAmount);
		//更新用户往来账户余额
		userFundsTenderDao.updateUserFundsAccountAmount(userFundsAccountInfo.getFundsAccountId(), increaseAmount, nowDatetime);
		
		//添加转出者资金流水记录
		userFundsTenderDao.addT6102Record(userPTFundsAccountInfo.getFundsAccountId(), FeeCode.POINTS_EXCHANGE_CASH, userFundsAccountInfo.getFundsAccountId(), nowDatetime, new BigDecimal(0), cashAmount, remainAmount, "现金兑换");
		
		//添加转入者资金流水记录
		userFundsTenderDao.addT6102Record(userFundsAccountInfo.getFundsAccountId(), FeeCode.POINTS_EXCHANGE_CASH, userPTFundsAccountInfo.getFundsAccountId(), nowDatetime, cashAmount, new BigDecimal(0), increaseAmount, "现金兑换");
		
	}
	/**
	 * @title 减少用户账户积分
	 * @param userId
	 * @param pNum
	 * @return
	 */
	@Override
	public int minusUserAccountPoints(int userId, int pNum) throws Exception{
		//锁定用户账户积分
		MyPoint myPointAccount = this.getMyPointsNum(userId);
		int userPointNum = myPointAccount.getPointNum();
		if(userPointNum < pNum){
			throw new BusinessException(ResponseCode.MP_MY_POINTS_ACCOUNT_REMAIN_LACK.getCode(),ResponseCode.MP_MY_POINTS_ACCOUNT_REMAIN_LACK.getMessage());
		}
		//账户剩余积分
		int remainPointNum = userPointNum - pNum ;
		return memberPointsDao.minusUserAccountPoints(userId,remainPointNum);
	}

	@Override
	public int updatePointsUseRecordStatus(int id, int exStatus) {
		Date nowDatetime = new Date();
		return memberPointsDao.updatePointsUseRecordStatus(id, exStatus, nowDatetime);
	}

	@Override
	public boolean pointsCanExchangeCash(int userId, String pTypeCode, int pNum) throws Exception{
		//1.积分账户验证
		MyPoint myPointAccount = this.getMyPointsNum(userId);
		int userPointNum = myPointAccount.getPointNum();
		if(userPointNum < pNum){
			throw new BusinessException(ResponseCode.MP_MY_POINTS_ACCOUNT_REMAIN_LACK.getCode(),ResponseCode.MP_MY_POINTS_ACCOUNT_REMAIN_LACK.getMessage());
		}
		//2.积分规则验证
		return pointsExchangeCashConfigValid(userId,pTypeCode,pNum,true);
	}
	
	/** 
	 * @Title: pointsExchangeCashValid 
	 * @Description: 验证积分兑换金额规则
	 * @param userId
	 * @param pTypeCode
	 * @param pNum
	 * @param isLeaf (当前子节点为true，父节点为false)
	 * @return
	 * @throws Exception
	 * @return: boolean
	 */
	public boolean pointsExchangeCashConfigValid(int userId, String pTypeCode, int pNum, boolean isLeaf) throws Exception{
		Date nowDatetime = new Date();
		//获取当前积分类型配置规则信息
		PointsExchangeCashInfo pointsExchangeCashInfo = memberPointsDao.getPointsExchangeCashConfigInfo(pTypeCode, nowDatetime);
		if(pointsExchangeCashInfo == null ){
			if(isLeaf == true){
				throw new BusinessException(ResponseCode.MP_POINTS_EXCHANGE_CASH_CONFIG_NOT_FOUND.getCode(),ResponseCode.MP_POINTS_EXCHANGE_CASH_CONFIG_NOT_FOUND.getMessage());
			}
			return true;
		}
		//该类型积分最大使用次数
		int getTotalUseFrequency = pointsExchangeCashInfo.getTotalUseFrequency(); 
		//该类型积分每次最高使用限额
		int maxUseNumber = pointsExchangeCashInfo.getMaxUseNumber();
		//该类型积分每次最低使用限额
		int minUseNumber = pointsExchangeCashInfo.getMinUseNumber();
		//是否按年
		int isByYear = pointsExchangeCashInfo.getIsByYear(); 
		//年使用频率
	    int yearFrequency = pointsExchangeCashInfo.getYearFrequency();
	    //是否按月
		int isByMonth = pointsExchangeCashInfo.getIsByMonth(); 
		//月使用频率
		int monthFrequency = pointsExchangeCashInfo.getMonthFrequency(); 
		//是否按日
		int isByDay = pointsExchangeCashInfo.getIsByDay();
		//日使用频率
		int dayFrequency = pointsExchangeCashInfo.getDayFrequency(); 
		
		//1.验证积分使用是否超过最高最低限制
		if(pNum > maxUseNumber){
			throw new BusinessException(ResponseCode.MP_POINTS_OVER_MAX_EXCHANGE_NUM.getCode(),ResponseCode.MP_POINTS_OVER_MAX_EXCHANGE_NUM.getMessage().replace("#{pTypeCode}", pTypeCode));
		}
		if(pNum < minUseNumber){
			throw new BusinessException(ResponseCode.MP_POINTS_BELOW_MIN_EXCHANGE_NUM.getCode(),ResponseCode.MP_POINTS_BELOW_MIN_EXCHANGE_NUM.getMessage().replace("#{pTypeCode}", pTypeCode));
		}
		//获取当前积分类型的子积分类型列表
		List<PointsType> pointsTypeList = memberPointsDao.getPointsTypeChildrenList(pTypeCode);
		List<String> pTypeCodeList = new ArrayList<String>();
		//添加当前积分类型编码
		pTypeCodeList.add(pTypeCode);
		if(pointsTypeList != null && pointsTypeList.size() >0) {
			for(PointsType pointsType : pointsTypeList){
				if(pointsType.getTypeCode() != null && !"".equals(pointsType.getTypeCode())){
					pTypeCodeList.add(pointsType.getTypeCode());
				}
			}
		}
		//获取当前类型及子类型所有成功使用记录
		List<PointsUseRecord> pointsUseRecordAllList = memberPointsDao.getUserPointsUseSucRecord(userId,pTypeCodeList,0,0,0,nowDatetime);
		if(pointsUseRecordAllList!=null && pointsUseRecordAllList.size() > 0){
			//2.验证积分类型使用次数是否超过最大限制
			if(pointsUseRecordAllList.size() >= getTotalUseFrequency ){
				throw new BusinessException(ResponseCode.MP_POINTS_OVER_MAX_EXCHANGE_FREQUENCY.getCode(),ResponseCode.MP_POINTS_OVER_MAX_EXCHANGE_FREQUENCY.getMessage().replace("#{pTypeCode}", pTypeCode));
			}
		}
		//3.验证积分类型是否超过年月日的限制
		if(isByYear == 1){
			List<PointsUseRecord> pointsUseRecordYearList = memberPointsDao.getUserPointsUseSucRecord(userId,pTypeCodeList,isByYear,0,0,nowDatetime);
			if(pointsUseRecordYearList != null && pointsUseRecordYearList.size() >= yearFrequency){
				throw new BusinessException(ResponseCode.MP_POINTS_OVER_YEAR_EXCHANGE_FREQUENCY.getCode(),ResponseCode.MP_POINTS_OVER_YEAR_EXCHANGE_FREQUENCY.getMessage().replace("#{pTypeCode}", pTypeCode));
			}
		}
		if(isByMonth == 1){
			List<PointsUseRecord> pointsUseRecordMonthList = memberPointsDao.getUserPointsUseSucRecord(userId,pTypeCodeList,0,isByMonth,0,nowDatetime);
			if(pointsUseRecordMonthList !=null && pointsUseRecordMonthList.size() >= monthFrequency){
				throw new BusinessException(ResponseCode.MP_POINTS_OVER_MONTH_EXCHANGE_FREQUENCY.getCode(),ResponseCode.MP_POINTS_OVER_MONTH_EXCHANGE_FREQUENCY.getMessage().replace("#{pTypeCode}", pTypeCode));
			}
		}
		if(isByDay == 1){
			List<PointsUseRecord> pointsUseRecordDayList = memberPointsDao.getUserPointsUseSucRecord(userId,pTypeCodeList,0,0,isByDay,nowDatetime);
			if(pointsUseRecordDayList !=null && pointsUseRecordDayList.size() >= dayFrequency){
				throw new BusinessException(ResponseCode.MP_POINTS_OVER_DAY_EXCHANGE_FREQUENCY.getCode(),ResponseCode.MP_POINTS_OVER_DAY_EXCHANGE_FREQUENCY.getMessage().replace("#{pTypeCode}", pTypeCode));
			}
		}
		//验证是否满足父节点的限制，递归验证积分规则
		PointsType parentPointsType = memberPointsDao.getPointsExchangeCashParentInfo(pTypeCode);
		if(parentPointsType != null && parentPointsType.getTypeCode() != null && !"".equals(parentPointsType.getTypeCode()) ){
			this.pointsExchangeCashConfigValid(userId,parentPointsType.getTypeCode(),pNum,false);
		}
		return true;
	}

    @Transactional
    @Override
    public void modifyUserAccountPointsNum(String pTypeCode, int userId, int pNum, int pointChangeType) throws Exception {
        this.modifyUserAccountPointsNum(pTypeCode, userId, pNum, pointChangeType,"");
    }

	@Transactional
	@Override
	public void modifyUserAccountPointsNum(String pTypeCode, int userId, int pNum, int pointChangeType, String remark) throws Exception{
		//锁定用户账户积分数量
		MyPoint myPointAccount = this.getMyPointsNum(userId);
		int userPointNum = myPointAccount.getPointNum();

		//账户剩余积分
		int remainPointNum = 0;
        if(pointChangeType == PointChangeType.PCT_OUTGOINGS){
            remainPointNum = userPointNum - pNum ;
        }
        if(pointChangeType == PointChangeType.PCT_INCOME){
            remainPointNum = userPointNum + pNum ;
        }
        if(remainPointNum<0){
            throw new BusinessException(ResponseCode.MP_MY_POINTS_ACCOUNT_REMAIN_LACK.getCode(),ResponseCode.MP_MY_POINTS_ACCOUNT_REMAIN_LACK.getMessage());
        }

        PointsType pointsType = memberPointsDao.getPointsTypeInfo(pTypeCode);
        if (pointsType == null || pointsType.getId() == 0 ){
            throw new BusinessException(ResponseCode.MP_INTEGRAL_NOT_EXIST);
        }
        Date nowDatetime = DateUtil.nowDate();
        memberPointsDao.modifyUserAccountPoints(userId,remainPointNum);
        memberPointsDao.addPointsSheetRecord(pointsType.getId(), userId, pNum, pointChangeType,remark, nowDatetime);
    }
}
