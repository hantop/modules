package com.fenlibao.p2p.service.withdraw.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S65.entities.T6503;
import com.dimeng.util.parser.DateTimeParser;
import com.fenlibao.p2p.service.base.impl.OrderExecutorImpl;
import com.fenlibao.p2p.service.trade.IOrderService;
import com.fenlibao.p2p.util.DateUtil;

//copy from com.dimeng.p2p.order.WithdrawExecutor
public class WithdrawExecutor extends OrderExecutorImpl {
	
	@Resource
	private IOrderService orderService;

	@Override
    protected void doConfirm(Connection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        try
        {
            // 查询订单
            T6503 t6503 = selectT6503(connection, orderId);
            if (t6503 == null)
            {
            	logger.info("withdraw callback confirm fail.");
            	logger.error(String.format("[%s]的提现申请订单不存在", orderId));
                throw new LogicalException("订单记录不存在");
            }
            /*
             * 连连代付金额
             */
            BigDecimal money_order = new BigDecimal(params.get("money_order")); //连连代付实际金额 
            if (t6503.F03.compareTo(money_order) != 0){
            	String exceptionMsg = String.format("订单ID为[%s]的代付订单，代付异常，用户提现为[%s]元，连连实际代付为[%s]元，最终结果已连连代付金额为准。", 
            			orderId, t6503.F03, money_order);
            	this.insertOrderExceptionLog(connection, orderId, exceptionMsg);
				logger.info(exceptionMsg);
				t6503.F03 = money_order;
            }
            	
            int pid = 0; // 平台用户id
            try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S71.T7101 LIMIT 1"))
            {
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        pid = resultSet.getInt(1);
                    }
                }
            }
            if (pid <= 0)
            {
                throw new LogicalException("平台账号不存在");
            }
            // 用户锁定账户信息
            T6101 uT6101 = selectT6101(connection, t6503.F02, T6101_F03.SDZH);
            if (uT6101 == null)
            {
                throw new LogicalException("用户账户不存在");
            }
            // 平台往来账户信息
            T6101 cT6101 = selectT6101(connection, pid, T6101_F03.WLZH);
            if (cT6101 == null)
            {
                throw new LogicalException("平台往来账户不存在");
            }
            if (uT6101.F06.compareTo(t6503.F03.add(t6503.F05)) < 0)
            {
            	logger.info("withdraw callback confirm fail.");
            	logger.error(String.format("[%s]用户锁定账号金额不足", uT6101.F02));
                throw new LogicalException("用户账户金额不足");
            }
            // 插资金流水
            {
                uT6101.F06 = uT6101.F06.subtract(t6503.F03);
                T6102 t6102 = new T6102();
                t6102.F02 = uT6101.F01;
                t6102.F03 = FeeCode.TX;
                t6102.F04 = uT6101.F01;
                t6102.F07 = t6503.F03;
                t6102.F08 = uT6101.F06;
                t6102.F09 = "用户提现";
                insertT6102(connection, t6102);
            }
            if (t6503.F05.compareTo(BigDecimal.ZERO) > 0)
            {
                uT6101.F06 = uT6101.F06.subtract(t6503.F05);
                T6102 t6102 = new T6102();
                t6102.F02 = uT6101.F01;
                t6102.F03 = FeeCode.TX_SXF;
                t6102.F04 = uT6101.F01;
                t6102.F07 = t6503.F05;
                t6102.F08 = uT6101.F06;
                t6102.F09 = "用户提现手续费";
                insertT6102(connection, t6102);
            }
            updateT6101(connection, uT6101.F06, uT6101.F01);
            if (t6503.F04.compareTo(BigDecimal.ZERO) > 0)
            {
                cT6101.F06 = cT6101.F06.add(t6503.F04);
                updateT6101(connection, cT6101.F06, cT6101.F01);
                {
                    T6102 t6102 = new T6102();
                    t6102.F02 = cT6101.F01;
                    t6102.F03 = FeeCode.TX_SXF;
                    t6102.F04 = uT6101.F01;
                    t6102.F06 = t6503.F05;
                    t6102.F08 = cT6101.F06;
                    t6102.F09 = "用户提现手续费";
                    insertT6102(connection, t6102);
                }
            }
            if (t6503.F09 > 0)
            {
                // 修改提现申请放款状态
                try (PreparedStatement ps = connection.prepareStatement("UPDATE S61.T6130 SET F16='S' WHERE F01 = ?"))
                {
                    ps.setInt(1, t6503.F09);
                    ps.execute();
                }
            }
            
            T6110 t6110 = selectT6110(connection, t6503.F02);
             Timestamp orderTime = orderService.getCreateTimeByOrderId(orderId);
            //这里暂时这样
            String content = String.format("尊敬的用户：您好！ 您于%s提交的提现申请已成功，其中实际到账%s元，手续费%s元，请您注意查收。 感谢您对我们的关注与支持", 
            		DateTimeParser.format(orderTime), t6503.F03.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
            		t6503.F05.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            int days = DateUtil.getDayBetweenDates(orderTime, new Date());
            if (days < 10) { //10天前的不再发送短信
            	sendMsg(connection, t6110.F04, content);
            }
            sendLetter(connection, t6503.F02, "提现成功", content);
            
            /**
             * HSP 20150826  连连代付更新 T6503 增加提现流水记录
             */
            if (params != null) {
                updateT6503(connection, orderId, params.get("oid_paybill"));
            }
        }
        catch (Exception e)
        {
        	logger.info(String.format("withdraw callback confirm fail.[%s]", orderId));
            logger.error(e.toString(), e);
            throw e;
        }
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
    
    private void updateT6101(Connection connection, BigDecimal F01, int F02)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
            pstmt.execute();
        }
    }
    
    public void updateT6101(Connection connection, BigDecimal F01, String userName, T6101_F03 F03, int userId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101,S61.T6110 SET T6101.F06 = ?, F07 = ? WHERE T6110.F01 = ? AND T6101.F03 = ? AND T6101.F02 = T6110.F01"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, userId);
            pstmt.setString(4, F03.name());
            pstmt.execute();
        }
    }
    
    private T6101 selectT6101(Connection connection, int F02, T6101_F03 F03)
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
    
    protected T6503 selectT6503(Connection connection, int F01)
        throws SQLException
    {
        T6503 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S65.T6503 WHERE T6503.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6503();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getString(6);
                    record.F07 = resultSet.getInt(7);
                    record.F08 = resultSet.getString(8);
                    record.F09 = resultSet.getInt(9);
                }
            }
        }
        return record;
    }
    
    /**
     * 更新T6503增加提现交易流水记录
     * <作者：何石平，连连代付提现>
     * @param connection
     * @param F01 订单号
     * @param F08 流水号
     * @throws Throwable
     */
    public void updateT6503(Connection connection, int F01, String F08)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S65.T6503 SET T6503.F08 = ? WHERE T6503.F01 = ? "))
        {
            pstmt.setString(1, F08);
            pstmt.setInt(2, F01);
            pstmt.execute();
        }
    }
	
}
