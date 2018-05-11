package com.fenlibao.p2p.service.withdraw.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6130;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.p2p.S61.enums.T6130_F16;
import com.dimeng.util.parser.DateTimeParser;
import com.fenlibao.p2p.model.form.trade.BankCardInfo;
import com.fenlibao.p2p.model.global.FeeCode;
import com.fenlibao.p2p.service.base.abstracts.BaseAbstractService;
import com.fenlibao.p2p.service.withdraw.IAipgBusManageService;
import com.fenlibao.p2p.util.DateUtil;

//copy from com.dimeng.p2p.pay.achieve.AipgBusManageServiceImpl
@Service
public class AipgBusManageServiceImpl extends BaseAbstractService implements
		IAipgBusManageService {
	
	/**
	 * {@inheritDoc} 查询银行卡信息
	 */
	@Override
	public BankCardInfo getUserBankCardInfo(Connection connection,
			int bankCardId) throws Exception {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT bankCard.F07 bankCardNo, uinf.F02 realName, bank.F04 bankCode,bank.F02 bankName, bankCard.F12 flag, bankCard.F04 cityCode, bankCard.F05 branchName, bankCard.F13 bindStatus"
						+ " FROM S61.T6114 bankCard, S50.T5020 bank, S61.T6110 uer,S61.T6141 uinf WHERE bankCard.F03 = bank.F01 AND bankCard.F02 = uer.F01 and uer.F01 = uinf.F01 and bankCard.F01 = ?")) {
			pstmt.setInt(1, bankCardId);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					BankCardInfo bankCardInfo = new BankCardInfo();
					bankCardInfo.setBankCardNo(resultSet
							.getString("bankCardNo"));
					bankCardInfo.setBankCode(resultSet.getString("bankCode"));
					bankCardInfo.setRealName(resultSet.getString("realName"));
					bankCardInfo.setBankName(resultSet.getString("bankName"));
					bankCardInfo.setFlag(resultSet.getString("flag"));
					bankCardInfo.setCityCode(resultSet.getString("cityCode"));
					bankCardInfo.setBranchName(resultSet
							.getString("branchName"));
					bankCardInfo.setBindStatus(resultSet
							.getString("bindStatus"));
					return bankCardInfo;
				}
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc} 根据订单id获取提现银行卡id
	 */
	@Override
	public int getWithdrawBankCardByOrderId(Connection connection, int orderId)
			throws Exception {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT req.F03 FROM S65.T6503 ord,S61.T6130 req WHERE ord.F09 = req.F01 and ord.F01 = ?")) {
			pstmt.setInt(1, orderId);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}
		}
		return 0;
	}

	/**
	 * {@inheritDoc} 获取放款中状态的提现订单
	 */
	@Override
	public List<Integer> getWithdrawingList() throws Exception {
		List<Integer> resList = new ArrayList<>();
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT ord.F01 FROM S65.T6503 ord,S61.T6130 req WHERE ord.F09 = req.F01 and req.F09 = ? AND ord.F07 = 900"
							+ " AND req.F08 <= DATE_SUB(now(), INTERVAL 1 HOUR) ORDER BY req.F01 ASC LIMIT 100 ")) { //获取一小时前的，避免正在提交就直接获取了
				pstmt.setString(1, T6130_F09.FKZ.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						resList.add(resultSet.getInt(1));
					}
				}
			}
		}
		return resList;
	}

	@Override
	public int updateT6130Status(Connection connection, int orderId,
			T6130_F09 status) throws Exception {
		if (connection == null) {
			connection = getConnection();
		}
		try (PreparedStatement pstmt = connection
				.prepareStatement("update S61.T6130 set F09 = ? where F01 = (select F09 from S65.T6503 where F01 = ? limit 1)")) {
			pstmt.setString(1, status.name());
			pstmt.setInt(2, orderId);
			return pstmt.executeUpdate();
		}
	}

	@Override
	public int updateT6130Status(Connection connection, int orderId,
			T6130_F09 status, String reason) throws Exception {
		if (connection == null) {
			connection = getConnection();
		}
		try (PreparedStatement pstmt = connection
				.prepareStatement("update S61.T6130 set F09 = ?,F15 = ? where F01 = (select F09 from S65.T6503 where F01 = ? limit 1)")) {
			pstmt.setString(1, status.name());
			pstmt.setString(2, reason);
			pstmt.setInt(3, orderId);
			return pstmt.executeUpdate();
		}
	}

	@Override
	public T6130 lockWithdrawByOrderId(Connection connection,
			int whithdrawOrderId, T6130_F09 status) throws Exception {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT req.* FROM S65.T6503 ord,S61.T6130 req WHERE ord.F09 = req.F01 and ord.F01 = ? and req.F09 = ? FOR UPDATE")) {
			pstmt.setInt(1, whithdrawOrderId);
			pstmt.setString(2, status.name());
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					T6130 t6130 = new T6130();
					t6130.F01 = resultSet.getInt("F01");
					t6130.F02 = resultSet.getInt("F02");
					t6130.F03 = resultSet.getInt("F03");
					t6130.F04 = resultSet.getBigDecimal("F04");
					t6130.F06 = resultSet.getBigDecimal("F06");
					t6130.F07 = resultSet.getBigDecimal("F07");
					t6130.F08 = resultSet.getTimestamp("F08");
					return t6130;
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据用户ID获取手机号码
	 * @param conn
	 * @param userId
	 * @return
	 * @throws SQLException 
	 */
	private String getPhoneByUserId(Connection connection, int userId) throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F04 FROM s61.t6110 WHERE F01 = ? LIMIT 1")) {
			pstmt.setInt(1, userId);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getString("F04");
				}
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc} 代付失败
	 */
	@Override
	public void withdrawFail(int orderId, String reason) throws Throwable {
		Connection connection = null;
		Throwable exception = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false);// 打开事务
			T6130 userWithdrawals = lockWithdrawByOrderId(connection, orderId,
					T6130_F09.FKZ); //这里必须根据状态并且锁定记录来获取记录
			if (userWithdrawals != null) { //只有提现申请订单为放款中的才进行操作
				updateT6130Status(connection, orderId, T6130_F09.TXSB, reason,
						T6130_F16.F);
				T6101 sdzh = selectT6101(connection, userWithdrawals.F02,
						T6101_F03.SDZH);
				if (sdzh == null) {
					throw new LogicalException("用户锁定资金账户不存在");
				}
				T6101 wlzh = selectT6101(connection, userWithdrawals.F02,
						T6101_F03.WLZH);
				if (wlzh == null) {
					throw new LogicalException("用户往来资金账户不存在");
				}
				{
					if (sdzh.F06.compareTo(userWithdrawals.F04
							.add(userWithdrawals.F07)) < 0) {
						throw new LogicalException("用户锁定资金账户余额不足");
					}
					{
						T6102 t6102 = new T6102();
						t6102.F02 = sdzh.F01;
						t6102.F03 = FeeCode.TX_SB;
						t6102.F04 = wlzh.F01;
						t6102.F07 = userWithdrawals.F04;
						sdzh.F06 = sdzh.F06.subtract(userWithdrawals.F04);
						t6102.F08 = sdzh.F06;
						t6102.F09 = "提现失败,本金返还";
						insertT6102(connection, t6102);
					}
					{
						T6102 t6102 = new T6102();
						t6102.F02 = sdzh.F01;
						t6102.F03 = FeeCode.TX_SB_SXF;
						t6102.F04 = wlzh.F01;
						t6102.F07 = userWithdrawals.F07;
						sdzh.F06 = sdzh.F06.subtract(userWithdrawals.F07);
						t6102.F08 = sdzh.F06;
						t6102.F09 = "提现失败,手续费返还";
						insertT6102(connection, t6102);
					}
					// 扣减锁定账户资金
					updateT6101(connection, sdzh.F01, sdzh.F06);
				}
				{
					{
						T6102 t6102 = new T6102();
						t6102.F02 = wlzh.F01;
						t6102.F03 = FeeCode.TX_SB;
						t6102.F04 = sdzh.F01;
						t6102.F06 = userWithdrawals.F04;
						wlzh.F06 = wlzh.F06.add(userWithdrawals.F04);
						t6102.F08 = wlzh.F06;
						t6102.F09 = "提现失败,本金返还";
						insertT6102(connection, t6102);
					}
					{
						T6102 t6102 = new T6102();
						t6102.F02 = wlzh.F01;
						t6102.F03 = FeeCode.TX_SB_SXF;
						t6102.F04 = sdzh.F01;
						t6102.F06 = userWithdrawals.F07;
						wlzh.F06 = wlzh.F06.add(userWithdrawals.F07);
						t6102.F08 = wlzh.F06;
						t6102.F09 = "提现失败,手续费返还";
						insertT6102(connection, t6102);
					}
					// 增加往来账户资金
					updateT6101(connection, wlzh.F01, wlzh.F06);
				}
				String content = String.format(
						"尊敬的用户：您好！您于%s提交的%s元提现申请失败，金额已经退回账户。感谢您对我们的关注与支持！",
						DateTimeParser.format(userWithdrawals.F08),
						userWithdrawals.F04.setScale(2,
								BigDecimal.ROUND_HALF_UP).toString());
				sendLetter(connection, userWithdrawals.F02, "提现失败", content);
				String phoneNum = this.getPhoneByUserId(connection, userWithdrawals.F02);
				int days = DateUtil.getDayBetweenDates(userWithdrawals.F08, new Date());
				if (StringUtils.isNotBlank(phoneNum) && days < 10) { //10天前的不再发送短信
					sendMsg(connection, phoneNum, content);
				}
				connection.commit();
				connection.setAutoCommit(true);
			}
		} catch (Throwable t) {
			connection.rollback();
			exception = t;
			logger.error(t.toString(), t);
		} finally {
			if (connection != null) {
				if (exception != null) {
					this.insertOrderExceptionLog(connection, orderId, exception.getMessage());
				}
				try {
					connection.rollback();
					connection.setAutoCommit(true);
				} catch (Throwable e) {
				}
				connection.close();
			}
		}
		if (exception != null) {
			throw exception;
		}
	}
	
	/**
	 * 记录订单异常日志
	 * @param connection
	 * @param orderId
	 * @param log
	 * @throws Exception
	 */
	private void insertOrderExceptionLog(Connection connection, int orderId, String log) throws Exception {
		try (PreparedStatement pstmt = connection
				.prepareStatement("INSERT INTO S65.T6550 SET F02 = ?, F03 = ? ")) {
			pstmt.setInt(1, orderId);
			pstmt.setString(2, log);
			pstmt.execute();
		}
	}

	private T6101 selectT6101(Connection connection, int F02, T6101_F03 F03)
			throws Exception {
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

	private int insertT6102(Connection connection, T6102 entity)
			throws Exception {
		try (PreparedStatement pstmt = connection
				.prepareStatement(
						"INSERT INTO S61.T6102 SET F02 = ?, F03 = ?, F04 = ?, F05 = CURRENT_TIMESTAMP(), F06 = ?, F07 = ?, F08 = ?, F09 = ?",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, entity.F02);
			pstmt.setInt(2, entity.F03);
			pstmt.setInt(3, entity.F04);
			pstmt.setBigDecimal(4, entity.F06);
			pstmt.setBigDecimal(5, entity.F07);
			pstmt.setBigDecimal(6, entity.F08);
			pstmt.setString(7, entity.F09);
			pstmt.execute();
			try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
				return 0;
			}
		}
	}

	private void updateT6101(Connection connection, int F01, BigDecimal F06)
			throws Exception {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = now()  WHERE F01 = ? ")) {
			pstmt.setBigDecimal(1, F06);
			pstmt.setInt(2, F01);
			pstmt.execute();
		}
	}

	@Override
	public int updateT6130Status(Connection connection, int orderId,
			T6130_F09 status, String reason, T6130_F16 isDZ) throws Exception {
		if (connection == null) {
			connection = getConnection();
		}
		try (PreparedStatement pstmt = connection
				.prepareStatement("update S61.T6130 set F09 = ?,F15 = ?,F16 = ? where F01 = (select F09 from S65.T6503 where F01 = ? limit 1)")) {
			pstmt.setString(1, status.name());
			pstmt.setString(2, reason);
			pstmt.setString(3, isDZ.name());
			pstmt.setInt(4, orderId);
			return pstmt.executeUpdate();
		}
	}
}
