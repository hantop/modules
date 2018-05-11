package com.fenlibao.p2p.service.recharge.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.fenlibao.p2p.model.enums.bid.OperationTypeEnum;
import org.springframework.stereotype.Service;

import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.S65.entities.T6517;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;
import com.fenlibao.p2p.service.base.impl.OrderExecutorImpl;
import com.fenlibao.p2p.service.recharge.IRechargeOrderService;
import com.fenlibao.p2p.util.loader.Payment;

@Service
public class RechargeOrderServiceImpl extends OrderExecutorImpl implements IRechargeOrderService {

	@Override
	public void doConfirm(Connection connection, int orderId,
			Map<String, String> params) throws Throwable {
		try {
			T6502 t6502 = selectT6502(connection, orderId); // 充值订单信息
			if (t6502 == null) {
				throw new ParameterException("订单不存在");
			}

			/*
			 * 校验充值返回金额和用户充值金额是否一致，以连连返回的金额为准
			 */
			BigDecimal returnAmount = new BigDecimal(params.get("amount"));
			//是否为第三方支付测试
			boolean isTest = BooleanParser.parse(Payment.get(Payment.IS_PAY_TEST)); 
			if (!isTest) {
				if (t6502.F03.compareTo(returnAmount) != 0) { 
					//测试环境不需要
					this.updateT6502ByAmount(connection, orderId, returnAmount);
					String exceptionMsg = String.format("ID为【%s】的订单充值异常，用户充值为【%s】元，连连支付充值为【%s】元。最终充值金额以连连支付充值的金额为准！", 
							orderId, t6502.F03, returnAmount);
					this.insertOrderExceptionLog(connection, orderId, exceptionMsg);
					logger.info(exceptionMsg);
					t6502.F03 = returnAmount;
				}
			}
			int accountId = t6502.F02;
			Timestamp t = getCurrentTimestamp(connection);
			int pid = 0; // 平台用户id
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F01 FROM S71.T7101 LIMIT 1")) {
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						pid = resultSet.getInt(1);
					}
				}
			}
			if (pid <= 0) {
				throw new LogicalException("平台账号不存在");
			}
			// 平台往来账户信息
			T6101 cT6101 = selectT6101(connection, pid, T6101_F03.WLZH);
			if (cT6101 == null) {
				throw new LogicalException("平台往来账户不存在");
			}
			BigDecimal a = feeRate(t6502);

			if (t6502.F02 == pid) {
				// 插入平台账户资金流水
				cT6101.F06 = cT6101.F06.add(t6502.F03);
				T6102 cT6102 = new T6102();
				cT6102.F02 = cT6101.F01;
				cT6102.F03 = FeeCode.CZ;
				cT6102.F04 = cT6101.F01;
				cT6102.F05 = t;
				cT6102.F06 = t6502.F03;
				cT6102.F08 = cT6101.F06;
				cT6102.F09 = "平台充值";
				insertT6102(connection, cT6102);
				if (a != null && a.compareTo(BigDecimal.ZERO) > 0) {
					// 充值成本
					BigDecimal cb = fee(t6502, a, params);
					if (cb.compareTo(BigDecimal.ZERO) > 0) {
						cT6101.F06 = cT6101.F06.subtract(cb);
						cT6102.F03 = FeeCode.CZ_CB;
						cT6102.F04 = cT6102.F01;
						cT6102.F05 = t;
						cT6102.F06 = BigDecimal.ZERO;
						cT6102.F07 = cb;
						cT6102.F08 = cT6101.F06;
						cT6102.F09 = "平台充值成本";
						insertT6102(connection, cT6102);
					}
				}
				updateT6101(connection, cT6101.F06, cT6101.F01);
				return;
			}
			// 用户往来账户信息
			T6101 uT6101 = selectT6101(connection, accountId, T6101_F03.WLZH);
			if (uT6101 == null) {
				throw new LogicalException("用户往来账户不存在");
			}
			// 更新有效推广奖励统计
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F01 FROM S63.T6310 WHERE F01=? FOR UPDATE")) {
					ps.setInt(1, accountId);
				ps.execute();
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F01 FROM S63.T6311 FOR UPDATE")) {
				ps.execute();
			}
			// 插入用户资金流水
			uT6101.F06 = uT6101.F06.add(t6502.F03);
			T6102 uT6102 = new T6102();
			uT6102.F02 = uT6101.F01;
			uT6102.F03 = FeeCode.CZ;
			uT6102.F04 = uT6101.F01;
			uT6102.F05 = t;
			uT6102.F06 = t6502.F03;
			uT6102.F08 = uT6101.F06;
			uT6102.F09 = "账户充值";
			insertT6102(connection, uT6102);
			if (t6502.F05.compareTo(BigDecimal.ZERO) > 0) {
				uT6101.F06 = uT6101.F06.subtract(t6502.F05);
				uT6102.F03 = FeeCode.CZ_SXF;
				uT6102.F04 = uT6101.F01;
				uT6102.F05 = t;
				uT6102.F06 = BigDecimal.ZERO;
				uT6102.F07 = t6502.F05;
				uT6102.F08 = uT6101.F06;
				uT6102.F09 = "充值手续费";
				insertT6102(connection, uT6102);
			}
			updateT6101(connection, uT6101.F06, uT6101.F01);
			// 插入平台账户资金流水
			T6102 cT6102 = new T6102();
			cT6102.F02 = cT6101.F01;
			if (t6502.F05.compareTo(BigDecimal.ZERO) > 0) {
				cT6101.F06 = cT6101.F06.add(t6502.F05);
				cT6102.F03 = FeeCode.CZ_SXF;
				cT6102.F04 = uT6101.F01;
				cT6102.F05 = t;
				cT6102.F06 = t6502.F05;
				cT6102.F08 = cT6101.F06;
				cT6102.F09 = "用户充值手续费";
				insertT6102(connection, cT6102);
			}
			// 获取支付公司收取的手续费率
			if (a != null && a.compareTo(BigDecimal.ZERO) > 0) {
				// 充值成本
				BigDecimal cb = fee(t6502, a, params);
				if (cb.compareTo(BigDecimal.ZERO) > 0) {
					cT6101.F06 = cT6101.F06.subtract(cb);
					cT6102.F03 = FeeCode.CZ_CB;
					cT6102.F04 = cT6102.F01;
					cT6102.F05 = t;
					cT6102.F06 = BigDecimal.ZERO;
					cT6102.F07 = cb;
					cT6102.F08 = cT6101.F06;
					cT6102.F09 = "充值成本";
					insertT6102(connection, cT6102);
				}
			}
			// 更新平台往来账户
			updateT6101(connection, cT6101.F06, cT6101.F01);
			// 更新订单状态
			if (params != null
					&& !StringHelper.isEmpty(params.get("paymentOrderId"))) {
				updateT6502(connection, params.get("paymentOrderId"), orderId);
			}
			// 推广处理    //暂时不做推广推处理
			if (BooleanParser.parse(Payment.get(Payment.ACCOUNT_SFTG))) { //configureProvider.getProperty(SystemVariable.ACCOUNT_SFTG)
				expand(connection, t6502, uT6101, cT6101);
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		}
	}

	protected BigDecimal feeRate(T6502 t6502) throws Throwable{
		//充值手续费为0（让连连扣取商户的）
		return BigDecimal.ZERO;//new BigDecimal(0.003);
	}

	private BigDecimal fee(T6502 t6502, BigDecimal bg,
			Map<String, String> params) {
		if (params != null && !StringHelper.isEmpty(params.get("feeAmt"))) {
			return BigDecimalParser.parse(params.get("feeAmt")).setScale(2,
					BigDecimal.ROUND_HALF_UP);
		} else {
			return t6502.F03.multiply(bg).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
	}

	/**
	 * 推广处理
	 * 
	 * @param amount
	 *            充值金额
	 * @param id
	 *            充值订单id
	 * @throws Throwable
	 */
	protected void expand(Connection connection, T6502 t6502, T6101 uT6101,
			T6101 cT6101) throws Throwable {
		Timestamp t = new Timestamp(System.currentTimeMillis());
		BigDecimal exjl = null;
		int accountId = t6502.F02;
		int exid = 0;// 推广人id
		// 充值次数
		int chargeCount = selectChargeCount(connection, T6501_F03.CG,
				OrderType.CHARGE.orderType(), accountId);
		if (chargeCount > 0) {
			return;
		} else {
			if (t6502.F03.compareTo(new BigDecimal(5000)) < 0) { //configureProvider.getProperty(SystemVariable.TG_YXCZJS)
				// 第一次充值，未达奖励基数：记录首次金额及时间
				try (PreparedStatement ps = connection
						.prepareStatement("UPDATE S63.T6311 SET F04 = ?, F06 = ? WHERE F03 = ?")) {
					ps.setBigDecimal(1, t6502.F03);
					ps.setTimestamp(2, t);
					ps.setInt(3, accountId);
					ps.execute();
				}
				return;
			}
		}
		try (PreparedStatement ps = connection
				.prepareStatement("UPDATE S63.T6311 SET F04 = ?, F06 = ? WHERE F03 = ?")) {
			ps.setBigDecimal(1, t6502.F03);
			ps.setTimestamp(2, t);
			ps.setInt(3, accountId);
			ps.execute();
		}
		// 充值推广奖励
		String code = null;// 邀请码
		try (PreparedStatement ps = connection
				.prepareStatement("SELECT F03 FROM S61.T6111 WHERE F01=?")) {
			ps.setInt(1, accountId);
			try (ResultSet resultSet = ps.executeQuery()) {
				if (resultSet.next()) {
					code = resultSet.getString(1);
				}
			}
		}
		if (StringHelper.isEmpty(code)) {
			return;
		}
		try (PreparedStatement ps = connection
				.prepareStatement("SELECT F01 FROM S61.T6111 WHERE F02=? LIMIT 1")) {
			ps.setString(1, code);
			try (ResultSet resultSet = ps.executeQuery()) {
				if (resultSet.next()) {
					exid = resultSet.getInt(1);
				}
			}
		}
		if (exid <= 0) {
			return;
		}
		// 推广人未托管不奖励
		boolean tg = false;//BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
		if(tg){
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F01 FROM S61.T6119 WHERE F01 = ?")) {
				ps.setInt(1, exid);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (!resultSet.next()) {
						return;
					}
				}
			}
		}
		// 判断获取奖励次数是否超过上限(当月)
		int excount = 0;
		try (PreparedStatement ps = connection
				.prepareStatement("SELECT COUNT(F02) FROM S63.T6311 WHERE F06 >= ? AND F06 <= ? AND F02=? AND F05>0")) {
			Calendar monthCal = Calendar.getInstance();
			monthCal.setTime(new Date());
			monthCal.set(Calendar.DATE,
					monthCal.getActualMinimum(Calendar.DATE));
			monthCal.set(Calendar.HOUR_OF_DAY, 0);
			monthCal.set(Calendar.MINUTE, 0);
			monthCal.set(Calendar.SECOND, 0);
			ps.setDate(1, new java.sql.Date(monthCal.getTimeInMillis()));

			monthCal.set(Calendar.DATE,
					monthCal.getActualMaximum(Calendar.DATE));
			monthCal.set(Calendar.HOUR_OF_DAY, 23);
			monthCal.set(Calendar.MINUTE, 59);
			monthCal.set(Calendar.SECOND, 59);
			ps.setDate(2, new java.sql.Date(monthCal.getTimeInMillis()));

			ps.setInt(3, exid);
			try (ResultSet resultSet = ps.executeQuery()) {
				if (resultSet.next()) {
					excount = resultSet.getInt(1);
				}
			}
		}
		if (excount >= 50) { //IntegerParser.parse(configureProvider.getProperty(SystemVariable.TG_YXTGSX))
			return;
		}
		// 计算奖励金额
		exjl = new BigDecimal(5); //configureProvider.getProperty(SystemVariable.TG_YXTGJL)
		try (PreparedStatement ps = connection
				.prepareStatement("UPDATE S63.T6311 SET F05=? WHERE F03=?")) {
			ps.setBigDecimal(1, exjl);
			ps.setInt(2, accountId);
			ps.execute();
		}
		try (PreparedStatement ps = connection
				.prepareStatement("UPDATE S63.T6310 SET F05=F05+? WHERE F01=?")) {
			ps.setBigDecimal(1, exjl);
			ps.setInt(2, exid);
			ps.executeUpdate();
		}
		updateT6101(connection, exjl, exid, T6101_F03.WLZH);
		T6101 eT6101 = selectT6101(connection, exid, T6101_F03.WLZH);
		T6102 eT6102 = new T6102();
		eT6102.F02 = eT6101.F01;
		eT6102.F03 = 9001;
		eT6102.F04 = cT6101.F01;
		eT6102.F05 = t;
		eT6102.F06 = exjl;
		eT6102.F08 = eT6101.F06;
		eT6102.F09 = "有效推广奖励";
		insertT6102(connection, eT6102);
		// 站内信
		int letterId = insertT6123(connection, exid, "有效推广奖励", t, T6123_F05.WD);
//		String tem = configureProvider.getProperty(LetterVariable.TG_YXJL);
//		Envionment envionment = configureProvider.createEnvionment();
//		envionment.set("cz", t6502.F03.setScale(2, BigDecimal.ROUND_HALF_UP)
//				.toString());
//		envionment.set("jl", exjl.setScale(2, BigDecimal.ROUND_HALF_UP)
//				.toString());
		insertT6124(connection, letterId, ""/*StringHelper.format(tem, envionment)*/);
		// 平台账户扣除奖励金额
		updateT6101(connection, exjl.multiply(new BigDecimal(-1)), cT6101.F02,
				T6101_F03.WLZH);
		cT6101 = selectT6101(connection, cT6101.F02, T6101_F03.WLZH);
		T6102 ecT6102 = new T6102();
		ecT6102.F02 = cT6101.F01;
		ecT6102.F03 = 9001;
		ecT6102.F04 = eT6101.F01;
		ecT6102.F05 = t;
		ecT6102.F07 = exjl;
		ecT6102.F08 = cT6101.F06;
		ecT6102.F09 = "有效推广奖励";
		insertT6102(connection, ecT6102);
		if (BooleanParser.parse("false")) { //configureProvider.getProperty(SystemVariable.SFZJTG)
			// 插入转账订单
			T6501 zzt6501 = new T6501();
			zzt6501.F02 = OrderType.TRANSFER.orderType();
			zzt6501.F03 = T6501_F03.DTJ;
			zzt6501.F07 = T6501_F07.XT;
			zzt6501.F08 = cT6101.F02;
			int ordId = insertT6501(connection, zzt6501);
			T6517 t6517 = new T6517();
			t6517.F01 = ordId;
			t6517.F02 = exjl;
			t6517.F03 = cT6101.F02;
			t6517.F04 = exid;
			t6517.F05 = "有效推广奖励";
			insertT6517(connection, t6517);
		}
	}

	@Override
	protected void doSubmit(Connection connection, int orderId,
			Map<String, String> params) throws Throwable {

	}

	protected void insertT6517(Connection connection, T6517 t6517)
			throws Throwable {
		try (PreparedStatement ps = connection
				.prepareStatement("INSERT INTO S65.T6517 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?")) {
			ps.setInt(1, t6517.F01);
			ps.setBigDecimal(2, t6517.F02);
			ps.setInt(3, t6517.F03);
			ps.setInt(4, t6517.F04);
			ps.setString(5, t6517.F05);
			ps.execute();
		}
	}

	protected int insertT6501(Connection connection, T6501 t6501)
			throws Throwable {
		try (PreparedStatement ps = connection
				.prepareStatement(
						"INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = ?, F07 = ?, F08 = ? ",
						Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, t6501.F02);
			ps.setString(2, t6501.F03.name());
			ps.setTimestamp(3,getCurrentTimestamp(connection));
			ps.setString(4, t6501.F07.name());
			ps.setInt(5, t6501.F08);
			ps.execute();
			try (ResultSet resultSet = ps.getGeneratedKeys()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}
		}
		return 0;
	}

	protected T6502 selectT6502(Connection connection, int F01)
			throws SQLException {
		T6502 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S65.T6502 WHERE T6502.F01 = ? FOR UPDATE")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6502();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getBigDecimal(3);
					record.F04 = resultSet.getBigDecimal(4);
					record.F05 = resultSet.getBigDecimal(5);
					record.F06 = resultSet.getString(6);
					record.F07 = resultSet.getInt(7);
					record.F08 = resultSet.getString(8);
				}
			}
		}
		return record;
	}

	protected T6101 selectT6101(Connection connection, int F02, T6101_F03 F03)
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

	protected void updateT6101(Connection connection, BigDecimal F01, int F02)
			throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ? WHERE F01 = ?")) {
			pstmt.setBigDecimal(1, F01);
			pstmt.setTimestamp(2,getCurrentTimestamp(connection));
			pstmt.setInt(3, F02);
			pstmt.execute();
		}
	}

	protected void updateT6101(Connection connection, BigDecimal F01, int F02,
			T6101_F03 F03) throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S61.T6101 SET F06 = F06 + ?, F07 = ? WHERE F02 = ? AND F03 = ?")) {
			pstmt.setBigDecimal(1, F01);
			pstmt.setTimestamp(2,getCurrentTimestamp(connection));
			pstmt.setInt(3, F02);
			pstmt.setString(4, F03.name());
			pstmt.execute();
		}
	}

	protected int insertT6102(Connection connection, T6102 entity)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement(
						"INSERT INTO S61.T6102 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, entity.F02);
			pstmt.setInt(2, entity.F03);
			pstmt.setInt(3, entity.F04);
			pstmt.setTimestamp(4, entity.F05);
			pstmt.setBigDecimal(5, entity.F06);
			pstmt.setBigDecimal(6, entity.F07);
			pstmt.setBigDecimal(7, entity.F08);
			pstmt.setString(8, entity.F09);
			pstmt.execute();
			try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
				return 0;
			}
		}
	}

	private void updateT6502(Connection connection, String F01, int F02)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S65.T6502 SET F08 = ? WHERE F01 = ?")) {
			pstmt.setString(1, F01);
			pstmt.setInt(2, F02);
			pstmt.execute();
		}
	}
	
	/**
	 * <p>当充值成功后连连返回的充值金额和订单的不一样的时候，以返回的为准</p>
	 * <p>且将金额不一致的订单记录下来
	 * @param connection
	 * @param orderId
	 * @param amount
	 * @throws SQLException
	 */
	private void updateT6502ByAmount(Connection connection, int orderId, BigDecimal amount) 
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S65.T6502 SET F03 = ? WHERE F01 = ?")) {
			pstmt.setBigDecimal(1, amount);
			pstmt.setInt(2, orderId);
			pstmt.execute();
		}
	}

	/**
	 * 查询充值次数
	 * 
	 * @param connection
	 * @param F03
	 * @param F02
	 * @return
	 * @throws SQLException
	 */
	protected int selectChargeCount(Connection connection, T6501_F03 F03,
			int F02, int F07) throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT COUNT(T6501.F01) FROM S65.T6501 INNER JOIN S65.T6502 ON T6501.F01 = T6502.F01 WHERE T6501.F03 = ? AND T6501.F02 = ? AND T6502.F02 = ? ")) {
			pstmt.setString(1, F03.name());
			pstmt.setInt(2, F02);
			pstmt.setInt(3, F07);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}
		}
		return 0;
	}

	protected int insertT6123(Connection connection, int F02, String F03,
			Timestamp F04, T6123_F05 F05) throws SQLException {
		try (PreparedStatement pstmt = connection.prepareStatement(
				"INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?",
				PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, F02);
			pstmt.setString(2, F03);
			pstmt.setTimestamp(3, F04);
			pstmt.setString(4, F05.name());
			pstmt.execute();
			try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
				return 0;
			}
		}
	}

	@Override
	protected void doConfirm(Connection connection, int orderId, Map<String, String> params, OperationTypeEnum operationTypeEnum) throws Throwable {
	}
}
