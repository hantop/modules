package com.fenlibao.p2p.service.recharge.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.p2p.model.entity.pay.RechargeOrder;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.form.user.Auth;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.base.abstracts.BaseAbstractService;
import com.fenlibao.p2p.service.recharge.IRechargeMangeService;
import com.fenlibao.p2p.util.loader.Payment;

@Service
public class RechargeManageServiceImpl extends BaseAbstractService implements IRechargeMangeService {
	
	@Resource
	private UserInfoService userInfoService;

	/**
	 * 插入站内信内容
	 * 
	 * @param connection
	 * @param F01
	 * @param F02
	 * @throws SQLException
	 */
	protected void insertT6124(Connection connection, int F01, String F02) throws SQLException {
		try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S61.T6124 SET F01 = ?, F02 = ?")) {
			pstmt.setInt(1, F01);
			pstmt.setString(2, F02);
			pstmt.execute();
		}
	}

	/**
	 * 插入站内信
	 * 
	 * @param connection
	 * @param F02
	 * @param F03
	 * @param F04
	 * @param F05
	 * @return
	 * @throws SQLException
	 */
	protected int insertT6123(Connection connection, int F02, String F03, Timestamp F04, T6123_F05 F05)
			throws SQLException {
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

	/**
	 * 更新充值订单流水单号
	 * 
	 * @param connection
	 * @param F01
	 * @param F02
	 * @throws SQLException
	 */
	protected void updateT6502(Connection connection, String F01, int F02) throws SQLException {
		try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6502 SET F08 = ? WHERE F01 = ?")) {
			pstmt.setString(1, F01);
			pstmt.setInt(2, F02);
			pstmt.execute();
		}
	}

	/**
	 * 更新订单表
	 * 
	 * @param connection
	 * @param F01
	 * @param F02
	 * @param F03
	 * @throws SQLException
	 */
	protected void updateT6501(Connection connection, T6501_F03 F01, Timestamp F02, int F03) throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F06 = ? WHERE F01 = ?")) {
			pstmt.setString(1, F01.name());
			pstmt.setTimestamp(2, F02);
			pstmt.setInt(3, F03);
			pstmt.execute();
		}
	}

	/**
	 * 查询资金账户信息
	 * 
	 * @param connection
	 * @param F02
	 * @param F03
	 * @return
	 * @throws SQLException
	 */
	protected T6101 selectT6101(Connection connection, int F02, T6101_F03 F03) throws SQLException {
		T6101 record = null;
		try (PreparedStatement pstmt = connection.prepareStatement(
				"SELECT F01, F02, F03, F04, F05, F06, F07 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ? LIMIT 1")) {
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

	/**
	 * 插入资金流水
	 * 
	 * @param connection
	 * @param entity
	 * @return
	 * @throws SQLException
	 */
	protected int insertT6102(Connection connection, T6102 entity) throws SQLException {
		try (PreparedStatement pstmt = connection.prepareStatement(
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

	/**
	 * 更新用户往来账户
	 * 
	 * @param connection
	 * @param F01
	 * @param F02
	 * @param F03
	 * @throws SQLException
	 */
	protected void updateT6101(Connection connection, BigDecimal F01, int F02, T6101_F03 F03) throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S61.T6101 SET F06 = F06 + ? WHERE F02 = ? AND F03 = ?")) {
			pstmt.setBigDecimal(1, F01);
			pstmt.setInt(2, F02);
			pstmt.setString(3, F03.name());
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
	protected int selectChargeCount(Connection connection, T6501_F03 F03, int F02) throws SQLException {
		try (PreparedStatement pstmt = connection.prepareStatement(
				"SELECT COUNT(T6501.F01) FROM S65.T6501 INNER JOIN S65.T6502 ON T6501.F01 = T6502.F01 WHERE T6501.F03 = ? AND T6502.F02 = ?")) {
			pstmt.setString(1, F03.name());
			pstmt.setInt(2, F02);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}
		}
		return 0;
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
	protected void expand(T6502 t6502, T6101 uT6101, T6101 cT6101, int userId) throws Throwable {
		if (t6502.F03.compareTo(new BigDecimal(5000)) < 0) { // configureProvider.getProperty(SystemVariable.TG_YXCZJS
			return;
		}
		Timestamp t = new Timestamp(System.currentTimeMillis());
		BigDecimal exjl = null;
		int exid = 0;// 推广人id
		// 充值次数
		int chargeCount = selectChargeCount(T6501_F03.CG, OrderType.CHARGE.orderType(), userId);
		if (chargeCount >= 2) {
			return;
		}
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("UPDATE S63.T6311 SET F04 = ?, F06 = ? WHERE F03 = ?")) {
				ps.setBigDecimal(1, t6502.F03);
				ps.setTimestamp(2, t);
				ps.setInt(3, userId);
				ps.execute();
			}
			// 充值推广奖励
			String code = null;// 邀请码
			try (PreparedStatement ps = connection.prepareStatement("SELECT F03 FROM S61.T6111 WHERE F01=?")) {
				ps.setInt(1, userId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						code = resultSet.getString(1);
					}
				}
			}
			if (StringHelper.isEmpty(code)) {
				return;
			}
			try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S61.T6111 WHERE F02=? LIMIT 1")) {
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
			// 判断获取奖励次数是否超过上限
			int excount = 0;
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT COUNT(F02) FROM S63.T6311 WHERE F02=? AND F05>0")) {
				ps.setInt(1, exid);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						excount = resultSet.getInt(1);
					}
				}
			}
			if (excount > IntegerParser.parse(50)) { // configureProvider.getProperty(SystemVariable.TG_YXTGSX)
				return;
			}
			// 计算奖励金额
			exjl = new BigDecimal(5); // configureProvider.getProperty(SystemVariable.TG_YXTGJL)
			try (PreparedStatement ps = connection.prepareStatement("UPDATE S63.T6311 SET F05=? WHERE F03=?")) {
				ps.setBigDecimal(1, exjl);
				ps.setInt(2, userId);
				ps.execute();
			}
			try (PreparedStatement ps = connection.prepareStatement("UPDATE S63.T6310 SET F05=F05+? WHERE F01=?")) {
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
			//String tem = "推广:有效奖励站内信模板";// configureProvider.getProperty(LetterVariable.TG_YXJL);
			// Envionment envionment = configureProvider.createEnvionment();
			// envionment.set("cz", t6502.F03.setScale(2,
			// BigDecimal.ROUND_HALF_UP)
			// .toString());
			// envionment.set("jl", exjl.setScale(2, BigDecimal.ROUND_HALF_UP)
			// .toString());
			insertT6124(connection, letterId,
					""/* StringHelper.format(tem, envionment) */);
			// 平台账户扣除奖励金额
			updateT6101(connection, exjl.multiply(new BigDecimal(-1)), cT6101.F02, T6101_F03.WLZH);
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
		}
	}
	
	/**
	 * 认证校验
	 * @param userId
	 */
	private void validateAuth(int userId) throws Exception {
		Auth auth = userInfoService.getAuthStatus(userId);
		if (auth == null) {
			throw new BusinessException(ResponseCode.USER_NOT_EXIST.getCode(), ResponseCode.USER_NOT_EXIST.getMessage());
		}
		if (!auth.IDENTITY) {
			throw new BusinessException(ResponseCode.USER_IDENTITY_UNAUTH.getCode(), ResponseCode.USER_IDENTITY_UNAUTH.getMessage());
		}
		if (!auth.PHONE) {
			throw new BusinessException(ResponseCode.USER_PHONE_UNAUTH.getCode(), ResponseCode.USER_PHONE_UNAUTH.getMessage());
		}
	}

	@Override
	public RechargeOrder addOrder(BigDecimal amount, int payCompanyCode, int userId, boolean isBind) throws Throwable {
		this.validateAuth(userId);
		if (amount.compareTo(new BigDecimal(0)) <= 0 || payCompanyCode <= 0) {
			throw new BusinessException(ResponseCode.TRADE_AMOUNT_FORMAT_ERROR);
		}

		String min = Payment.get(Payment.CHARGE_MIN_AMOUNT);// configureProvider.getProperty(PayVariavle.CHARGE_MIN_AMOUNT);
		String max = Payment.get(Payment.CHARGE_MAX_AMOUNT);// configureProvider.getProperty(PayVariavle.CHARGE_MAX_AMOUNT);
		if (isBind) {
			min = Payment.get(Payment.MONEY_ORDER);
		}
 		if (amount.compareTo(new BigDecimal(min)) < 0 || amount.compareTo(new BigDecimal(max)) > 0) {
			throw new BusinessException(ResponseCode.TRADE_RECHARGE_AMOUNT_SCOPE.getCode(), 
					String.format(ResponseCode.TRADE_RECHARGE_AMOUNT_SCOPE.getMessage(), min, max));
		}
		
		String rate = Payment.get(Payment.CHARGE_RATE);// configureProvider.getProperty(PayVariavle.CHARGE_RATE);
		if (StringHelper.isEmpty(rate)) {
			return null;
		}
		BigDecimal ysPondage = amount.multiply(new BigDecimal(rate)); // 应收手续费
		BigDecimal maxPondage = BigDecimalParser.parse(Payment.get(Payment.CHARGE_MAX_POUNDAGE)); // 最大手续费限额
																// //configureProvider.getProperty(PayVariavle.CHARGE_MAX_POUNDAGE)
		BigDecimal ssPoundage = ysPondage.compareTo(maxPondage) >= 0 ? maxPondage : ysPondage; // 实收手续费

		RechargeOrder order = null;
		T6501 t6501 = new T6501();
		t6501.F02 = OrderType.CHARGE.orderType();
		t6501.F03 = T6501_F03.DTJ;
		t6501.F04 = new Timestamp(System.currentTimeMillis());
		t6501.F07 = T6501_F07.YH;
		t6501.F08 = userId;
		int oId = 0;

		try (Connection connection = getConnection()) {
			try {
				connection.setAutoCommit(false);
				oId = insertT6501(connection, t6501);
				if (oId <= 0) {
					throw new LogicalException("数据库异常");
				}
				T6502 t6502 = new T6502();
				t6502.F01 = oId;
				t6502.F02 = userId;
				t6502.F03 = amount;
				t6502.F04 = ysPondage;
				t6502.F05 = ssPoundage;
				t6502.F07 = payCompanyCode;
				insertT6502(connection, t6502);
				order = new RechargeOrder();
				order.setId(oId);
				order.setAmount(amount);
				order.setCreateTime(t6501.F04);
				order.setPayCompanyCode(payCompanyCode);
				order.setUserId(userId);
				connection.commit();
				connection.setAutoCommit(true);
				return order;
			} catch (Exception e) {
				connection.rollback();
				connection.setAutoCommit(true);
				throw e;
			}
		}
	}

	@Override
	public RechargeOrder getChargeOrder(int orderId) throws Throwable {
		if (orderId <= 0) {
			return null;
		}
		RechargeOrder order = null;
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection.prepareStatement(
					"SELECT T6501.F01 AS F01, T6501.F04 AS F02, T6502.F03 AS F03, T6502.F07 AS F04, T6501.F03 AS F05, T6501.F08 AS F06 FROM S65.T6501 INNER JOIN S65.T6502 ON T6501.F01 = T6502.F01 WHERE T6501.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, orderId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						order = new RechargeOrder();
						order.setId(resultSet.getInt(1));
						order.setCreateTime(resultSet.getTimestamp(2));
						order.setAmount(resultSet.getBigDecimal(3));
						order.setPayCompanyCode(resultSet.getInt(4));;
						order.setStatus(EnumParser.parse(T6501_F03.class, resultSet.getString(5)));
						order.setUserId(resultSet.getInt(6));
					}
				}
			}
		}
		return order;
	}

	protected void updateT6501(Connection connection, T6501_F03 F01, int F02) throws SQLException {
		try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6501 SET F03 = ? WHERE F01 = ?")) {
			pstmt.setString(1, F01.name());
			pstmt.setInt(2, F02);
			pstmt.execute();
		}
	}

	protected int insertT6501(Connection connection, T6501 entity) throws SQLException {
		try (PreparedStatement pstmt = connection.prepareStatement(
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

	protected void insertT6502(Connection connection, T6502 entity) throws SQLException {
		try (PreparedStatement pstmt = connection.prepareStatement(
				"INSERT INTO S65.T6502 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?")) {
			pstmt.setInt(1, entity.F01);
			pstmt.setInt(2, entity.F02);
			pstmt.setBigDecimal(3, entity.F03);
			pstmt.setBigDecimal(4, entity.F04);
			pstmt.setBigDecimal(5, entity.F05);
			pstmt.setString(6, entity.F06);
			pstmt.setInt(7, entity.F07);
			pstmt.execute();
		}
	}

	@Override
	public String getBankCard(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F07 FROM S61.T6114 WHERE T6114.F02 = ? AND F08 = 'QY' LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getString(1);
					}
				}
			}
			return null;
		}
	}

	//////////////////////////// 扩展，来自上次迪蒙-联动////////////////////////////////////////////////////////////

	/**
	 * 查询充值次数
	 * 
	 * @param connection
	 * @param F03
	 * @param F02
	 * @return
	 * @throws SQLException
	 */
	protected int selectChargeCount(T6501_F03 F03, int F02, int F07) throws SQLException {
		try (Connection conn = getConnection()) {
			try (PreparedStatement pstmt = conn.prepareStatement(
					"SELECT COUNT(T6501.F01) FROM S65.T6501 INNER JOIN S65.T6502 ON T6501.F01 = T6502.F01 WHERE T6501.F03 = ? AND T6501.F02 = ? AND T6502.F02 = ? ")) {
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
	}

	protected int insertT6123(int F02, String F03, Timestamp F04, T6123_F05 F05) throws SQLException {
		try (Connection conn = getConnection()) {
			try (PreparedStatement pstmt = conn.prepareStatement(
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
	}
}
