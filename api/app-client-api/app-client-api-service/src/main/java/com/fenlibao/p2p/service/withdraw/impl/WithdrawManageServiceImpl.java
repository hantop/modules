package com.fenlibao.p2p.service.withdraw.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S61.enums.T6118_F03;
import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.p2p.S61.enums.T6130_F16;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateTimeParser;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.base.abstracts.BaseAbstractService;
import com.fenlibao.p2p.service.recharge.IRechargeService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.service.withdraw.IWithdrawManageService;
import com.fenlibao.p2p.service.withdraw.IWithdrawService;
import com.fenlibao.p2p.util.loader.Payment;
import com.fenlibao.p2p.util.sms.SmsUtil;

@Service
public class WithdrawManageServiceImpl extends BaseAbstractService implements IWithdrawManageService {
	
	@Resource
	private IRechargeService rechargeService;
	@Resource
	private ITradeService tradeService;
    @Resource
    private IWithdrawService withdrawService;
    
    @Override
    public int withdraw(BigDecimal funds, String withdrawPsd, int cardId, T6101_F03 f03,boolean txkcfs, int userId)
        throws Throwable
    {
        BigDecimal min = new BigDecimal(Payment.get(Payment.WITHDRAW_MIN_FUNDS)); //configureProvider.getProperty(SystemVariable.WITHDRAW_MIN_FUNDS)
        BigDecimal max = new BigDecimal(Payment.get(Payment.WITHDRAW_MAX_FUNDS)); //configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_FUNDS)
        BigDecimal zero = new BigDecimal(0);
        BigDecimal poundage = withdrawService.getPoundage(userId);
        if (funds.compareTo(min) < 0 || funds.compareTo(zero) <= 0)
        {
            throw new BusinessException(ResponseCode.PAYMENT_WITHDRAW_LIMIT_LOW);
        }
        if (funds.compareTo(max) > 0) {
            throw new BusinessException(ResponseCode.PAYMENT_WITHDRAW_LIMIT.getCode(),
                    String.format(ResponseCode.PAYMENT_WITHDRAW_LIMIT.getMessage(), min, max));
        }
        tradeService.validateAuth(userId);
        tradeService.isValidUserPwd(userId, withdrawPsd);
        try (Connection connection = getConnection();) {
          if (isYuqi(userId))
          {
              throw new BusinessException(ResponseCode.PAYMENT_LOAN_OVERDUE);
          }
          connection.setAutoCommit(false);
          
          try
          {
              String cardNumber = null;
              String bindStatus = null;
              try (PreparedStatement ps = connection.prepareStatement("SELECT F06, F13 FROM S61.T6114 WHERE F01=? AND F02=? ")) 
              {
                  ps.setInt(1, cardId);
                  ps.setInt(2, userId);
                  try (ResultSet resultSet = ps.executeQuery())
                  {
                      if (resultSet.next())
                      {
                          cardNumber = resultSet.getString(1);
                          bindStatus = resultSet.getString(2);
                      }
                  }
              }
              if (StringHelper.isEmpty(cardNumber))
              {
                  throw new BusinessException(ResponseCode.PAYMENT_BANK_CARD_NOT_EXIST);
              }
              if (StringUtils.isBlank(bindStatus) || InterfaceConst.BANK_CARD_STATUS_WRZ.equals(bindStatus)) {
            	  throw new BusinessException(ResponseCode.PAYMENT_UNBOUND_BANK_CARD);
              }
              
              // 提现手续费扣除方式
              //boolean txkcfs = Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.TXSXF_KCFS));
              BigDecimal amount = BigDecimal.ZERO;// 提现应付金额
              //连连代付，都需要审核
              BigDecimal money = BigDecimal.ZERO;//new BigDecimal(Payment.get(Payment.WITHDRAW_LIMIT_FUNDS)); //configureProvider.format(SystemVariable.WITHDRAW_LIMIT_FUNDS)
              if (txkcfs)
              {
                  amount = funds;
                  funds = funds.subtract(poundage);
                  if (amount.doubleValue() < poundage.doubleValue())
                  {
                      throw new BusinessException(ResponseCode.PAYMENT_AMOUNT_LESSTHAN_POUNDAGE);
                  }
              }
              else
              {
                  amount = funds.add(poundage);
                  money = money.add(poundage);
              }
              
              // 往来账户
              T6101 wlzh = lock(connection, userId, T6101_F03.WLZH);
              if (amount.compareTo(wlzh.F06) > 0)
              {
                  throw new BusinessException(ResponseCode.PAYMENT_INSUFFICIENT_BALANCE);
              }
              BigDecimal offlineAmount = rechargeService.getOfflineRechargeAmount(userId, 
            		  Integer.valueOf(Payment.get(Payment.WITHDRAW_OFFLINE_INTERVAL))); //xx小时内线下充值的金额
              BigDecimal limitAmount = withdrawService.getLimitAmount(userId);
              if (limitAmount != null && amount.compareTo(wlzh.F06.subtract(limitAmount)) > 0) {
                  BigDecimal amount0 = BigDecimal.ZERO;
                  BigDecimal amount1 = wlzh.F06.subtract(limitAmount);
                  BigDecimal amount2 = wlzh.F06.subtract(offlineAmount);
                  if (amount1.compareTo(amount2) > 0 && amount2.compareTo(BigDecimal.ZERO) > 0) {
                      amount0 = amount2;
                  } else if (amount1.compareTo(BigDecimal.ZERO) > 0) {
                      amount0 = amount1;
                  }
                  throw new BusinessException(ResponseCode.PAYMENT_WITHDRAW_EXCESS.getCode(),
                          String.format(ResponseCode.PAYMENT_WITHDRAW_EXCESS.getMessage(), amount0));
              }
              if (amount.compareTo(wlzh.F06.subtract(offlineAmount)) > 0) {
            	  throw new BusinessException(ResponseCode.PAYMENT_OFFLINE_CANNOT_WITHDRAW.getCode(),
            			  String.format(ResponseCode.PAYMENT_OFFLINE_CANNOT_WITHDRAW.getMessage(),
            			  Payment.get(Payment.WITHDRAW_OFFLINE_INTERVAL)));
              }
              // 锁定账户
              int id = 0;
              T6101 sdzh = lock(connection, userId, T6101_F03.SDZH);
              if(amount.doubleValue() > money.doubleValue() || money.doubleValue() <= 0){
                  id = checkWithdraw(connection, userId, funds, amount, cardId, poundage, wlzh, sdzh, zero);
              }else{
                  id = uncheckWithdraw(connection, userId, funds, amount, cardId, poundage, wlzh, sdzh, zero);
              }
              connection.commit();
               connection.setAutoCommit(true);
               return id ;
          }
          catch (Exception e)
          {
          	connection.rollback();
          	connection.setAutoCommit(true);
              throw e;
          }
        }
    }
    /**
     * 提现不需要审核
     * <功能详细描述>
     * @param connection
     * @param accountId
     * @param funds
     * @param amount
     * @param cardId
     * @param poundage
     * @param wlzh
     * @param sdzh
     * @param zero
     * @return
     * @throws Throwable
     */
    private int uncheckWithdraw(Connection connection,int accountId,BigDecimal funds,BigDecimal amount, int cardId, BigDecimal poundage,T6101 wlzh,T6101 sdzh,BigDecimal zero)
        throws Throwable
    {
        int id = 0;
        String insertT6130Sql = "INSERT INTO S61.T6130(F02,F03,F04,F06,F07,F08,F09,F14,F16) VALUES(?,?,?,?,?,?,?,?,?)";
 
        try (PreparedStatement ps =
            connection.prepareStatement(insertT6130Sql,
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            ps.setInt(1, accountId);
            ps.setInt(2, cardId);
            ps.setBigDecimal(3, funds);
            ps.setBigDecimal(4, poundage);
            ps.setBigDecimal(5, poundage);
            ps.setTimestamp(6, getCurrentTimestamp(connection));
            ps.setString(7, T6130_F09.YFK.toString());
            ps.setTimestamp(8, getCurrentTimestamp(connection));
            ps.setString(9, T6130_F16.S.name());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys())
            {
                if (rs.next())
                {
                    id = rs.getInt(1);
                }
            }
        }
        
        try (PreparedStatement ps =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 - ?, F07 = ?  WHERE F01 = ?"))
        {
            ps.setBigDecimal(1, amount);
            ps.setTimestamp(2, getCurrentTimestamp(connection));
            ps.setInt(3, wlzh.F01);
            ps.executeUpdate();
        }
        {
            // 往来账户流水
            T6102 t6102 = new T6102();
            t6102.F02 = wlzh.F01;
            t6102.F03 = FeeCode.TX;
            t6102.F04 = sdzh.F01;
            t6102.F07 = funds;
            wlzh.F06 = wlzh.F06.subtract(funds);
            t6102.F08 = wlzh.F06;
            t6102.F09 = "提现金额";
            insertT6102(connection, t6102);
        }
        if (poundage.compareTo(zero) > 0)
        {
            // 往来账户流水
            T6102 t6102 = new T6102();
            t6102.F02 = wlzh.F01;
            t6102.F03 = FeeCode.TX_SXF;
            t6102.F04 = sdzh.F01;
            t6102.F07 = poundage;
            wlzh.F06 = wlzh.F06.subtract(poundage);
            t6102.F08 = wlzh.F06;
            t6102.F09 = "提现手续费";
            insertT6102(connection, t6102);
        }
        
       
        //订单ID
        int orderId = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?,F03 = ?, F04 = ?,F05 = ?,F06 = ?, F07 = ?, F09 = ?, F08 = ? ",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, OrderType.WITHDRAW.orderType());
            pstmt.setString(2, T6501_F03.CG.name());
            pstmt.setTimestamp(3, getCurrentTimestamp(connection));
            pstmt.setTimestamp(4, getCurrentTimestamp(connection));
            pstmt.setTimestamp(5, getCurrentTimestamp(connection));
            pstmt.setString(6, T6501_F07.HT.name());
            pstmt.setInt(7, 0);
            pstmt.setInt(8, accountId);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    orderId = resultSet.getInt(1);
                }
            }
        }
        String carNum = "";
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT F07 FROM S61.T6114 WHERE F01=? AND F02=?"))
        {
            ps.setInt(1, cardId);
            ps.setInt(2, accountId);
            try (ResultSet resultSet = ps.executeQuery())
            {
                if (resultSet.next())
                {
                    carNum = resultSet.getString(1);
                }
            }
        }
        if (StringHelper.isEmpty(carNum))
        {
            throw new LogicalException("银行卡不存在");
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6503 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?,F09 = ?"))
        {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, accountId);
            pstmt.setBigDecimal(3, funds);
            pstmt.setBigDecimal(4, poundage);
            pstmt.setBigDecimal(5, poundage);
            pstmt.setString(6, StringHelper.decode(carNum));
            pstmt.setInt(7, id);
            pstmt.execute();
        }
        
        T6110 t6110 = selectT6110(connection, accountId);
//        Envionment envionment = configureProvider.createEnvionment();
//        envionment.set("name", t6110.F02);
//        envionment.set("datetime", DateTimeParser.format(new Date()));
//        envionment.set("amount", funds.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
//        envionment.set("poundage", poundage.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
//        String content = configureProvider.format(LetterVariable.TX_CG,
//                envionment);
        //这里暂时这样
        String content = "尊敬的用户"+t6110.F02+"： 您于"
                +DateTimeParser.format(new Date())+"提交的提现申请已到账，其中实际到账"
        		+funds.setScale(2, BigDecimal.ROUND_HALF_UP).toString()+"元，手续费"
                +poundage.setScale(2, BigDecimal.ROUND_HALF_UP).toString()+"元，请您注意查收，感谢您的支持";
        sendLetter(connection, accountId, "提现成功", content);
        /*
         * 使用API的短信发送
         */
        SmsUtil.sendSms(t6110.F04, content);
        sendMsg(connection, t6110.F04, content);
        return id;
       
    }
    /**
     * 提现需要审核
     * <功能详细描述>
     * @param connection
     * @param accountId
     * @param funds
     * @param amount
     * @param cardId
     * @param poundage
     * @param wlzh
     * @param sdzh
     * @param zero
     * @param configureProvider
     * @return
     * @throws Throwable
     */
    private int checkWithdraw(Connection connection,int accountId,BigDecimal funds,BigDecimal amount, int cardId, BigDecimal poundage,T6101 wlzh,T6101 sdzh,BigDecimal zero)
        throws Throwable
    {
        int id = 0;
        String insertT6130Sql = "INSERT INTO S61.T6130(F02,F03,F04,F06,F07,F08,F09,F16) VALUES(?,?,?,?,?,?,?,?)";
       
        try (PreparedStatement ps =
            connection.prepareStatement(insertT6130Sql,
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            ps.setInt(1, accountId);
            ps.setInt(2, cardId);
            ps.setBigDecimal(3, funds);
            ps.setBigDecimal(4, poundage);
            ps.setBigDecimal(5, poundage);
            ps.setTimestamp(6, getCurrentTimestamp(connection));
            ps.setString(7, T6130_F09.DSH.toString());  //如果跳过审核，这里的状态改成放款中？
            ps.setString(8, T6130_F16.F.name());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys())
            {
                if (rs.next())
                {
                    id = rs.getInt(1);
                }
            }
        }
        
        try (PreparedStatement ps =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 - ?, F07 = ?  WHERE F01 = ?"))
        {
            ps.setBigDecimal(1, amount);
            ps.setTimestamp(2, getCurrentTimestamp(connection));
            ps.setInt(3, wlzh.F01);
            ps.executeUpdate();
        }
        {
            // 往来账户流水
            T6102 t6102 = new T6102();
            t6102.F02 = wlzh.F01;
            t6102.F03 = FeeCode.TX;
            t6102.F04 = sdzh.F01;
            t6102.F07 = funds;
            wlzh.F06 = wlzh.F06.subtract(funds);
            t6102.F08 = wlzh.F06;
            t6102.F09 = "提现金额";
            insertT6102(connection, t6102);
        }
        if (poundage.compareTo(zero) > 0)
        {
            // 往来账户流水
            T6102 t6102 = new T6102();
            t6102.F02 = wlzh.F01;
            t6102.F03 = FeeCode.TX_SXF;
            t6102.F04 = sdzh.F01;
            t6102.F07 = poundage;
            wlzh.F06 = wlzh.F06.subtract(poundage);
            t6102.F08 = wlzh.F06;
             t6102.F09 = "提现手续费";
            insertT6102(connection, t6102);
        }
        try (PreparedStatement ps =
             connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 + ?, F07 = ? WHERE F01 = ? "))
        {
            ps.setBigDecimal(1, amount);
            ps.setTimestamp(2, getCurrentTimestamp(connection));
            ps.setInt(3, sdzh.F01);
            ps.executeUpdate();
         }
        {
            // 锁定账户流水
            T6102 t6102 = new T6102();
            t6102.F02 = sdzh.F01;
            t6102.F03 = FeeCode.TX;
            t6102.F04 = wlzh.F01;
            t6102.F06 = funds;
            sdzh.F06 = sdzh.F06.add(funds);
            t6102.F08 = sdzh.F06;
            t6102.F09 = "提现金额";
             insertT6102(connection, t6102);
        }
         if (poundage.compareTo(zero) > 0)
        {
            // 锁定账户流水
             T6102 t6102 = new T6102();
             t6102.F02 = sdzh.F01;
            t6102.F03 = FeeCode.TX_SXF;
            t6102.F04 = wlzh.F01;
            t6102.F06 = poundage;
            sdzh.F06 = sdzh.F06.add(poundage);
            t6102.F08 = sdzh.F06;
            t6102.F09 = "提现手续费";
            insertT6102(connection, t6102);
        }

          T6110 t6110 = selectT6110(connection, accountId);

        if (new BigDecimal(Payment.get(Payment.WITHDRAW_LIMIT_FUNDS)).compareTo(amount) < 0) { //超过10万需要后台审核
        	String content = String.format("尊敬的用户：您好！您于%s提交的%s元提现申请我们正在处理，如您填写的账户信息正确无误，您的资金将会于2个工作日内到达您的银行账户。感谢您对我们的关注与支持！", 
        			DateTimeParser.format(new Date()),funds.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        	sendMsg(connection, t6110.F04, content);
        }
         return id;
    }
    protected T6110 selectT6110(Connection connection, int F01)
        throws SQLException
    {
        T6110 record = null;
        try
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, F01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6110();
                        record.F02 = resultSet.getString(1);
                        record.F03 = resultSet.getString(2);
                        record.F04 = resultSet.getString(3);
                        record.F05 = resultSet.getString(4);
                        record.F06 = T6110_F06.parse(resultSet.getString(5));
                        record.F07 = T6110_F07.parse(resultSet.getString(6));
                        record.F08 = T6110_F08.parse(resultSet.getString(7));
                        record.F09 = resultSet.getTimestamp(8);
                        record.F10 = T6110_F10.parse(resultSet.getString(9));
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
        return record;
    }
    
    
    /**
      * 检查交易密码是否正确
      * @param withdrawPsd
      * @return
      * @throws Throwable
      */
    @Override
    public boolean checkWithdrawPassword(String withdrawPsd, int userId)
        throws Throwable
    {
       // ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        
        try (Connection connection = getConnection();) {
        	//先不记录
//        int count = psdInputCount(connection);
//        int maxCount = IntegerParser.parse(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_INPUT));
//        if (count >= maxCount)
//        {
//            throw new LogicalException("您今日交易密码输入错误已到最大次数，请改日再试!");
//        }
        	tradeService.validateAuth(userId);
        	boolean aa = false;// 标记交易密码是否正确
        	try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S61.T6118 WHERE F01=? AND F08=?"))
        	{
        		ps.setInt(1, userId);
        		ps.setString(2, UnixCrypt.crypt(withdrawPsd, DigestUtils.sha256Hex(withdrawPsd)));
        		try (ResultSet resultSet = ps.executeQuery())
        		{
        			if (resultSet.next())
        			{
        				aa = true;
        			}
        		}
        	}
        	if (!aa)
        	{
//            addInputCount(connection);
//            String errorMsg = null;
//            if (count + 1 >= maxCount)
//            {
//                errorMsg = "您今日交易密码输入错误已到最大次数，请改日再试!";
//            }
//            else
//            {
//                StringBuilder builder = new StringBuilder("交易密码错误,您最多还可以输入");
//                builder.append(maxCount - (count + 1));
//                builder.append("次");
//                errorMsg = builder.toString();
//            }
        		throw new LogicalException("交易密码错误"); //errorMsg
        	}
        	return aa;
        }
        
    }
    
    private T6101 lock(Connection connection, int F02, T6101_F03 F03)
        throws SQLException
    {
        T6101 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, F03.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
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
    
    private int insertT6102(Connection connection, T6102 entity)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6102 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setInt(3, entity.F04);
            pstmt.setTimestamp(4, getCurrentTimestamp(connection));
            pstmt.setBigDecimal(5, entity.F06);
            pstmt.setBigDecimal(6, entity.F07);
            pstmt.setBigDecimal(7, entity.F08);
            pstmt.setString(8, entity.F09);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }
    
    @Override
    public void addBankCard(String bank, String bankAddr, String branchBank, String cardNumber)
        throws Throwable
    {
        if (!checkBankCard(cardNumber))
        {
            throw new ParameterException("输入银行卡错误");
        }
        
    }
    
    @Override
    public boolean getVerifyStatusItem(int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        if (resultSet.getString(1).equals(T6118_F02.TG.name())
                            && resultSet.getString(2).equals(T6118_F03.TG.name()))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * 校验银行卡卡号
     * 
     * @param cardId
     * @return
     */
    protected boolean checkBankCard(String cardId)
    {
        if (cardId.trim().length() < 16)
        {
            return false;
        }
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N')
        {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }
    
    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * 
     * @param nonCheckCodeCardId
     * @return
     */
    protected char getBankCardCheckCode(String nonCheckCodeCardId)
    {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
            || !nonCheckCodeCardId.matches("\\d+"))
        {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++)
        {
            int k = chs[i] - '0';
            if (j % 2 == 0)
            {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }
    
    @Override
    public int withdrawHdw(BigDecimal funds, String withdrawPsd, int cardId, T6101_F03 f03, int userId)
        throws Throwable
    {
        BigDecimal zero = new BigDecimal(0);
        if (funds.compareTo(zero) <= 0)
        {
            throw new LogicalException("可用余额不足");
        }
        Connection connection = getConnection();
       // int count = psdInputCount(connection);
//        int maxCount = IntegerParser.parse(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_INPUT));
//        if (count >= maxCount)
//        {
//            throw new LogicalException("您今日交易密码输入错误已到最大次数，请改日再试!");
//        }
        tradeService.validateAuth(userId);
        connection.setAutoCommit(false);
        int id = 0;
        try
        {
            boolean aa = false;// 标记交易密码是否正确
            try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S61.T6118 WHERE F01=? AND F08=?"))
            {
                ps.setInt(1, userId);
                ps.setString(2, UnixCrypt.crypt(withdrawPsd, DigestUtils.sha256Hex(withdrawPsd)));
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        aa = true;
                    }
                }
            }
            if (!aa)
            {
//                addInputCount(connection);
//                serviceResource.commit(connection);
//                String errorMsg = null;
//                if (count + 1 >= maxCount)
//                {
//                    errorMsg = "您今日交易密码输入错误已到最大次数，请改日再试!";
//                }
//                else
//                {
//                    StringBuilder builder = new StringBuilder("交易密码错误,您最多还可以输入");
//                    builder.append(maxCount - (count + 1));
//                    builder.append("次");
//                    errorMsg = builder.toString();
//                }
                throw new LogicalException("交易密码错误"); //errorMsg
            }
            
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01 FROM S62.T6252 WHERE DATEDIFF(?,F08)>0 AND F09=? AND F03=?"))
            {
                ps.setTimestamp(1, getCurrentTimestamp(connection));
                ps.setString(2, T6252_F09.WH.name());
                ps.setInt(3, userId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        throw new LogicalException("您还有逾期借款未还，请先还借款！");
                    }
                }
            }
            String cardNumber = null;
            try (PreparedStatement ps = connection.prepareStatement("SELECT F06 FROM S61.T6114 WHERE F01=? AND F02=?"))
            {
                ps.setInt(1, cardId);
                ps.setInt(2, userId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        cardNumber = resultSet.getString(1);
                    }
                }
            }
            if (StringHelper.isEmpty(cardNumber))
            {
                throw new LogicalException("银行卡不存在");
            }
            BigDecimal proportion =
                new BigDecimal(Payment.get(Payment.WITHDRAW_POUNDAGE_PROPORTION)); //configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_PROPORTION)
            BigDecimal poundage = funds.multiply(proportion);
            BigDecimal amount = funds.add(poundage);// 提现应付金额
            // 往来账户
            T6101 wlzh = lock(connection, userId, T6101_F03.WLZH);
            if (amount.compareTo(wlzh.F06) > 0)
            {
                throw new LogicalException("账户余额不足");
            }
            // 锁定账户
            T6101 sdzh = lock(connection, userId, T6101_F03.SDZH);
            try (PreparedStatement ps =
                connection.prepareStatement("INSERT INTO S61.T6130(F02,F03,F04,F06,F07,F08,F09) VALUES(?,?,?,?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS))
            {
                ps.setInt(1, userId);
                ps.setInt(2, cardId);
                ps.setBigDecimal(3, funds);
                ps.setBigDecimal(4, poundage);
                ps.setBigDecimal(5, poundage);
                ps.setTimestamp(6, getCurrentTimestamp(connection));
                ps.setString(7, T6130_F09.DSH.toString());
                ps.execute();
                try (ResultSet rs = ps.getGeneratedKeys())
                {
                    if (rs.next())
                    {
                        id = rs.getInt(1);
                    }
                }
            }
            try (PreparedStatement ps =
                connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 - ?, F07 = ?  WHERE F01 = ?"))
            {
                ps.setBigDecimal(1, amount);
                ps.setTimestamp(2, getCurrentTimestamp(connection));
                ps.setInt(3, wlzh.F01);
                ps.executeUpdate();
            }
            {
                // 往来账户流水
                T6102 t6102 = new T6102();
                t6102.F02 = wlzh.F01;
                t6102.F03 = FeeCode.TX;
                t6102.F04 = sdzh.F01;
                t6102.F07 = funds;
                wlzh.F06 = wlzh.F06.subtract(funds);
                t6102.F08 = wlzh.F06;
                t6102.F09 = "提现金额";
                insertT6102(connection, t6102);
            }
            if (poundage.compareTo(zero) > 0)
            {
                // 往来账户流水
                T6102 t6102 = new T6102();
                t6102.F02 = wlzh.F01;
                t6102.F03 = FeeCode.TX_SXF;
                t6102.F04 = sdzh.F01;
                t6102.F07 = poundage;
                wlzh.F06 = wlzh.F06.subtract(poundage);
                t6102.F08 = wlzh.F06;
                t6102.F09 = "提现手续费";
                insertT6102(connection, t6102);
            }
            
            try (PreparedStatement ps =
                connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 + ?, F07 = ?  WHERE F01 = ? "))
            {
                ps.setBigDecimal(1, amount);
                ps.setTimestamp(2, getCurrentTimestamp(connection));
                ps.setInt(3, sdzh.F01);
                ps.executeUpdate();
            }
            {
                // 锁定账户流水
                T6102 t6102 = new T6102();
                t6102.F02 = sdzh.F01;
                t6102.F03 = FeeCode.TX;
                t6102.F04 = wlzh.F01;
                t6102.F06 = funds;
                sdzh.F06 = sdzh.F06.add(funds);
                t6102.F08 = sdzh.F06;
                t6102.F09 = "提现金额";
                insertT6102(connection, t6102);
            }
            if (poundage.compareTo(zero) > 0)
            {
                // 锁定账户流水
                T6102 t6102 = new T6102();
                t6102.F02 = sdzh.F01;
                t6102.F03 = FeeCode.TX_SXF;
                t6102.F04 = wlzh.F01;
                t6102.F06 = poundage;
                sdzh.F06 = sdzh.F06.add(poundage);
                t6102.F08 = sdzh.F06;
                t6102.F09 = "提现手续费";
                insertT6102(connection, t6102);
            }
            
            connection.commit();
            connection.setAutoCommit(true);
            return id;
        }
        catch (Exception e)
        {
        	connection.rollback();
        	connection.setAutoCommit(true);
            throw e;
        } finally {
        	if (connection != null)
        		connection.close();
        }
    }
    
    private boolean isYuqi(int accountId)
        throws Throwable
    {
        String sql = "SELECT DATEDIFF(?,F08) FROM S62.T6252 WHERE F09=? AND F03=? AND F08 < SYSDATE()";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setTimestamp(1, getCurrentTimestamp(connection));
                ps.setString(2, T6252_F09.WH.name());
                ps.setInt(3, accountId);
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        int days = rs.getInt(1);
                        if (days > 0)
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
	
}
