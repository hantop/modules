package com.fenlibao.p2p.service.base.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Timestamp;
import java.util.Map;

import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.util.parser.EnumParser;
import com.fenlibao.p2p.service.base.IOrderExecutor;
import com.fenlibao.p2p.service.base.abstracts.BaseAbstractService;
import com.fenlibao.p2p.service.xinwang.credit.XWCreditService;

import javax.annotation.Resource;

//copy from com.dimeng.p2p.order.AbstractOrderExecutor
public abstract class OrderExecutorImpl extends BaseAbstractService implements IOrderExecutor {

	protected static final int DECIMAL_SCALE = 9;

	@Resource
	XWCreditService xwCreditService;
	
	private T6501 lock(Connection connection, int orderId, T6501_F03 t6501_F03)
			throws SQLException {
		T6501 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S65.T6501 WHERE T6501.F01 = ? AND T6501.F03 = ?  FOR UPDATE")) {
			pstmt.setInt(1, orderId);
			pstmt.setString(2, t6501_F03.name());
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6501();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = T6501_F03.parse(resultSet.getString(3));
					record.F04 = resultSet.getTimestamp(4);
					record.F05 = resultSet.getTimestamp(5);
					record.F06 = resultSet.getTimestamp(6);
					record.F07 = EnumParser.parse(T6501_F07.class,
							resultSet.getString(7));
				}
			}
		}
		return record;
	}

	private void updateSubmit(Connection connection, T6501_F03 t6501_F03,
			int F02) throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F05 = ? WHERE F01 = ?")) {
			pstmt.setString(1, t6501_F03.name());
			pstmt.setTimestamp(2,getCurrentTimestamp(connection));
			pstmt.setInt(3, F02);
			pstmt.execute();
		}
	}

	private void updateConfirm(Connection connection, T6501_F03 t6501_F03,
			int F02) throws Throwable {
			try (PreparedStatement pstmt = connection
					.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F06 = ? WHERE F01 = ?")) {
				pstmt.setString(1, t6501_F03.name());
				pstmt.setTimestamp(2,getCurrentTimestamp(connection));
				pstmt.setInt(3, F02);
				pstmt.execute();
			}
	}

	private void log(Connection connection, int F01, Throwable exception)
			throws SQLException {
		ByteArrayOutputStream out = null;
		OutputStreamWriter osw = null;
		PrintWriter printWriter = null;
		try {
			out = new ByteArrayOutputStream();
			Charset charset = Charset.forName("UTF-8"); //resourceProvider.getCharset()
			osw = new OutputStreamWriter(out,charset);
			printWriter = new PrintWriter(osw);
			exception.printStackTrace(printWriter);
			printWriter.flush();
			try (PreparedStatement pstmt = connection
					.prepareStatement("INSERT INTO S65.T6550 SET F02 = ?, F03 = ?")) {
				pstmt.setInt(1, F01);
				pstmt.setString(2, new String(out.toByteArray(), charset));
				pstmt.execute();
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw  e;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (osw != null) {
				try {
					osw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (printWriter != null) {
				printWriter.close();
			}
		}
	}

	public void submit(int orderId, Map<String, String> params)
			throws Throwable {
		// 锁订单
		Connection connection = null;
		Throwable exception = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false);// 打开事务
			T6501 order = lock(connection, orderId, T6501_F03.DTJ);
			if (order != null) {
				// 执行业务操作
				Savepoint businessSavepoint = connection.setSavepoint();
				try {
					doSubmit(connection, orderId, params);
				} catch (Throwable e) {
					exception = e;
					connection.rollback(businessSavepoint);
					// 异常处理
					handleError(connection, orderId);
				}
				// 修改订单状态
				updateSubmit(connection, exception == null ? T6501_F03.DQR
						: T6501_F03.SB, orderId);
				connection.commit();
				connection.setAutoCommit(true);
			}
		} catch (Throwable t) {
			connection.rollback();
			exception = t;
			logger.error(t, t);
		}
		finally {
			if (connection != null) {
				if (exception != null) {
					try {
						connection.rollback();
						connection.setAutoCommit(true);
					} catch (Throwable e) {
					}
					log(connection, orderId, exception);
				}
				connection.close();
			}
		}
		if (exception != null) {
			logger.error(exception, exception);
			throw exception;
		}
	}

	public void confirm(int orderId, Map<String, String> params)
			throws Throwable {
		// 锁订单
		Connection connection = null;
		Throwable exception = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false);// 打开事务
			T6501 order = lock(connection, orderId, T6501_F03.DQR);
			if (order != null) {
				// 执行业务操作
				Savepoint businessSavepoint = connection.setSavepoint();
				try {
					doConfirm(connection, orderId, params);
				} catch (Throwable e) {
					//新网取消购买
					xwCreditService.doCancelCreditAssignment(orderId);
					exception = e;
					connection.rollback(businessSavepoint);
					// 异常处理
					handleError(connection, orderId);
				}
				// 修改订单状态
				updateConfirm(connection, exception == null ? T6501_F03.CG
						: T6501_F03.SB, orderId);
				connection.commit();
				connection.setAutoCommit(true);
			}
		} catch (Throwable t) {
			connection.rollback();
			exception = t;
		} finally {
			if (connection != null) {
				if (exception != null) {
					try {
						connection.rollback();
						connection.setAutoCommit(true);
					} catch (Throwable e) {
					}
					log(connection, orderId, exception);
				}

				connection.close();
			}
		}

		if (exception != null) {
			logger.error(exception, exception);
			throw exception;
		}
	}
	
	@Override
	public void confirmForPay(int orderId, Map<String, String> params)
			throws Throwable {
		// 锁订单
		Connection connection = null;
		Throwable exception = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false);// 打开事务
			T6501 order = lock(connection, orderId, T6501_F03.DQR);
			if (order != null) {
				// 执行业务操作
				Savepoint businessSavepoint = connection.setSavepoint();
				try {
					doConfirm(connection, orderId, params);
				} catch (Throwable e) {
					exception = e;
					connection.rollback(businessSavepoint);
					// 异常处理
					handleError(connection, orderId);
				}
				if (exception instanceof SQLException) {
					logger.info("pay callback confirm fail.");
					logger.info(String.format("支付回调确认失败，原因：%s", exception.getStackTrace().toString()));
				} else {
					// 修改订单状态
					updateConfirm(connection, exception == null ? T6501_F03.CG
							: T6501_F03.SB, orderId);
				}
				if (exception != null) {
					logger.error(String.format("[orderId=%s]的订单确认时状态更新为失败，原因：", orderId), exception);
				}
				connection.commit();
				connection.setAutoCommit(true);
			}
		} catch (Throwable t) {
			connection.rollback();
			exception = t;
		} finally {
			if (connection != null) {
				if (exception != null) {
					try {
						connection.rollback();
						connection.setAutoCommit(true);
					} catch (Throwable e) {
					}
					log(connection, orderId, exception);
				}
				connection.close();
			}
		}

		if (exception != null) {
			logger.error(exception.toString(), exception);
			throw exception;
		}
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

	protected void insertT6124(Connection connection, int F01, String F02)
			throws SQLException {
		try {
			try (PreparedStatement pstmt = connection
					.prepareStatement("INSERT INTO S61.T6124 SET F01 = ?, F02 = ?")) {
				pstmt.setInt(1, F01);
				pstmt.setString(2, F02);
				pstmt.execute();
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		}
	}

	protected T6110 selectT6110(Connection connection, int F01)
			throws SQLException {
		T6110 record = null;
		try {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, F01);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
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
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		}
		return record;
	}

	/**
	 * 查询订单信息
	 * 
	 * @param connection
	 * @param F01
	 * @return
	 * @throws SQLException
	 */
	protected T6501 selectT6501(Connection connection, int F01)
			throws Throwable {
		T6501 record = null;
		try {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S65.T6501 WHERE T6501.F01 = ? ")) {
				pstmt.setInt(1, F01);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T6501();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = T6501_F03.parse(resultSet.getString(3));
						record.F04 = resultSet.getTimestamp(4);
						record.F05 = resultSet.getTimestamp(5);
						record.F06 = resultSet.getTimestamp(6);
						record.F07 = T6501_F07.parse(resultSet.getString(7));
						record.F08 = resultSet.getInt(8);
						record.F09 = resultSet.getInt(9);
						record.F10 = resultSet.getString(10);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		}
		return record;
	}

	protected abstract void doConfirm(Connection connection, int orderId,
											Map<String, String> params) throws Throwable;

	protected void doSubmit(Connection connection, int orderId,
			Map<String, String> params) throws Throwable {

	}

	protected void handleError(Connection connection, int orderId) {

	}
	
	/**
	 * 记录订单异常日志
	 * @param connection
	 * @param orderId
	 * @param log
	 * @throws Exception
	 */
	protected void insertOrderExceptionLog(Connection connection, int orderId, String log) throws Exception {
		try (PreparedStatement pstmt = connection
				.prepareStatement("INSERT INTO S65.T6550 SET F02 = ?, F03 = ? ")) {
			pstmt.setInt(1, orderId);
			pstmt.setString(2, log);
			pstmt.execute();
		}
	}
	
	/**
	 * 记录订单异常日志
	 * @param orderId
	 * @param log
	 * @throws Exception
	 */
	protected void insertOrderExceptionLog(int orderId, String log) throws Exception {
		try (Connection connection = this.getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("INSERT INTO S65.T6550 SET F02 = ?, F03 = ? ")) {
				pstmt.setInt(1, orderId);
				pstmt.setString(2, log);
				pstmt.execute();
			}
		}
	}
}
