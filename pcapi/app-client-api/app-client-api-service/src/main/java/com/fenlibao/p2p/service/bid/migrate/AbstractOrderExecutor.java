package com.fenlibao.p2p.service.bid.migrate;

import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.*;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;
import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.service.base.abstracts.BaseAbstractService;
import com.fenlibao.p2p.service.base.db.DbPoolConnection;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.Map;

public abstract class AbstractOrderExecutor extends BaseAbstractService implements AutoCloseable {

	protected static final int DECIMAL_SCALE = 9;
	protected final Logger logger = Logger.getLogger(getClass());

	/*public AbstractOrderExecutor(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}*/


	protected int getPTID(Connection connection) throws Throwable {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F01 FROM S71.T7101 LIMIT 1")) {
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getInt(1);
					} else {
						throw new LogicalException("平台账号不存在");
					}
				}
			}
	}

	protected Timestamp getCurrentTimestamp(Connection connection)
			throws Throwable {
		try {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT CURRENT_TIMESTAMP()")) {
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getTimestamp(1);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		}
		return null;
	}

	protected Date getCurrentDate(Connection connection) throws Throwable {
		try {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT CURRENT_DATE()")) {
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getDate(1);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		}
		return null;
	}

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

	/*private void log(Connection connection, int F01, Throwable exception)
			throws SQLException {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Charset charset = Charset.forName(resourceProvider.getCharset());
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(out,
					charset));
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
		}
	}*/

	/*public void submit(int orderId, Map<String, String> params)
        throws Throwable
    {
        // 锁订单
        try (Connection connection = getConnection())
        {
            Throwable exception = null;
            try
            {
                connection.setAutoCommit(false);// 打开事务
                T6501 order = lock(connection, orderId, T6501_F03.DTJ);
                if (order != null)
                {
                    // 执行业务操作
                    Savepoint businessSavepoint = connection.setSavepoint();
                    try
                    {
                        doSubmit(connection, orderId, params);
                    }
                    catch (Throwable e)
                    {
                        exception = e;
                        connection.rollback(businessSavepoint);
                        // 异常处理
                        handleError(connection, orderId);
                    }
                    // 修改订单状态
                    updateSubmit(connection, exception == null ? T6501_F03.DQR : T6501_F03.SB, orderId);
                    connection.commit();
                    connection.setAutoCommit(true);
                }
            }
            catch (Throwable t)
            {
                connection.rollback();
                exception = t;
                logger.error(t, t);
            }
            finally
            {
                if (connection != null)
                {
                    if (exception != null)
                    {
                        log(connection, orderId, exception);
                    }
                    try
                    {
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                    catch (Throwable e)
                    {
                    }
                    connection.close();
                }
            }
            if (exception != null)
            {
                throw exception;
            }
        }
    }

	public void confirm(int orderId, Map<String, String> params)
        throws Throwable
    {
        // 锁订单
        try (SQLConnection connection = getConnection())
        {
            Throwable exception = null;
            try
            {
                connection.setAutoCommit(false);// 打开事务
                T6501 order = lock(connection, orderId, T6501_F03.DQR);
                if (order != null)
                {
                    // 执行业务操作
                    Savepoint businessSavepoint = connection.setSavepoint();
                    try
                    {
                        doConfirm(connection, orderId, params);
                    }
                    catch (Throwable e)
                    {
                        exception = e;
                        connection.rollback(businessSavepoint);
                        // 异常处理
                        handleError(connection, orderId);
                    }
                    // 修改订单状态
                    updateConfirm(connection, exception == null ? T6501_F03.CG : T6501_F03.SB, orderId);
                    connection.commit();
                    connection.setAutoCommit(true);
                }
            }
            catch (Throwable t)
            {
                connection.rollback();
                exception = t;
            }
            finally
            {
                if (connection != null)
                {
                    if (exception != null)
                    {
                        try
                        {
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                        catch (Throwable e)
                        {
                        }
                        log(connection, orderId, exception);
                    }
                    
                    connection.close();
                }
            }
            
            if (exception != null)
            {
                logger.error(exception, exception);
                throw exception;
            }
        }
    }*/

	public Throwable submitKernel(Connection connection, int orderId, Map<String, String> params) throws Throwable {
		// 锁订单
		Throwable exception = null;
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
			updateSubmit(connection, exception == null ? T6501_F03.DQR : T6501_F03.SB, orderId);
		}
		return exception;
	}

	public void submit(int orderId, Map<String, String> params) throws Throwable {
		// 锁订单
		Connection connection = null;
		Throwable exception = null;
		try {
			connection = DbPoolConnection.getInstance().getConnection();
			connection.setAutoCommit(false);// 打开事务
			exception = submitKernel(connection, orderId, params);
			connection.commit();
		} catch (Throwable t) {
			connection.rollback();
			exception = t;
			logger.error(t, t);
		} finally {
			if (connection != null) {
				if (exception != null) {
					log(connection, orderId, exception);
				}
				connection.close();
			}
		}
		if (exception != null) {
			throw exception;
		}
	}

	/**
	 * 确认订单
	 * @param connection
	 * @param orderId
	 * @param params
	 * @param checkAmountFlag 是否检查最小投资额
	 * @param isSetLetter 是否发站内信
	 * @return
	 * @throws Throwable
	 */
	public Throwable confirmKernel(Connection connection, int orderId, Map<String, String> params, boolean checkAmountFlag, boolean isSetLetter) throws Throwable {
		// 锁订单
		Throwable exception = null;
		Map<String,Object> fxhbMap = null;
		T6501 order = lock(connection, orderId, T6501_F03.DQR);
		if (order != null) {
			// 执行业务操作
			Savepoint businessSavepoint = connection.setSavepoint();
			try {
				doConfirm(connection, orderId, params, checkAmountFlag, isSetLetter);
			} catch (Throwable e) {
				exception = e;
				connection.rollback(businessSavepoint);
				// 异常处理
				handleError(connection, orderId);
			}
			// 修改订单状态
			updateConfirm(connection, exception == null ? T6501_F03.CG : T6501_F03.SB, orderId);
		}
		return exception;
	}

	public void confirm(int orderId, Map<String, String> params) throws Throwable {
		// 锁订单
		Connection connection = null;
		Throwable exception = null;
		Map<String,Object> fxhbMap = null;
		try{
			connection = DbPoolConnection.getInstance().getConnection();
			connection.setAutoCommit(false);// 打开事务
			exception = confirmKernel(connection, orderId, params, true, true);
			connection.commit();
		}catch (Throwable t){
			connection.rollback();
			exception = t;
		}finally {
			if (connection != null) {
				if (exception != null) {
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

	protected void sendLetter(Connection connection, int userId, String title,
			String content) throws Throwable {
		int letterId = insertT6123(connection, userId, title, T6123_F05.WD);
		insertT6124(connection, letterId, content);
	}

	protected void sendMsg(Connection connection, String mobile, String content)
			throws Throwable {
		try {
			if (!StringHelper.isEmpty(content) && !StringHelper.isEmpty(mobile)) {
				long msgId = 0;
				try (PreparedStatement ps = connection
						.prepareStatement(
								"INSERT INTO S10._1040(F02,F03,F04,F05) values(?,?,?,?)",
								Statement.RETURN_GENERATED_KEYS)) {
					ps.setInt(1, 0);
					ps.setString(2, content);
					ps.setTimestamp(3,
							new Timestamp(System.currentTimeMillis()));
					ps.setString(4, "W");
					ps.execute();
					try (ResultSet resultSet = ps.getGeneratedKeys()) {
						if (resultSet.next()) {
							msgId = resultSet.getLong(1);
						}
					}
				}
				if (msgId > 0) {
					try (PreparedStatement ps = connection
							.prepareStatement("INSERT INTO S10._1041(F01,F02) VALUES(?,?)")) {
						ps.setLong(1, msgId);
						ps.setString(2, mobile);
						ps.execute();
					}
				}
				return;
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		}
	}

	/**
	 * 容联云通讯发送短信方法
	 * 
	 * @param connection
	 * @param mobile
	 *            手机
	 * @param content
	 *            内容
	 * @param tempId
	 *            模板id
	 * @throws Throwable
	 */
	protected void sendMsg(Connection connection, String mobile,
			String content, int tempId) throws Throwable {
		try {
			if (!StringHelper.isEmpty(content) && !StringHelper.isEmpty(mobile)) {
				long msgId = 0;
				try (PreparedStatement ps = connection
						.prepareStatement(
								"INSERT INTO S10._1040(F02,F03,F04,F05) values(?,?,?,?)",
								Statement.RETURN_GENERATED_KEYS)) {
					ps.setInt(1, tempId);
					ps.setString(2, content);
					ps.setTimestamp(3,
							new Timestamp(System.currentTimeMillis()));
					ps.setString(4, "W");
					ps.execute();
					try (ResultSet resultSet = ps.getGeneratedKeys()) {
						if (resultSet.next()) {
							msgId = resultSet.getLong(1);
						}
					}
				}
				if (msgId > 0) {
					try (PreparedStatement ps = connection
							.prepareStatement("INSERT INTO S10._1041(F01,F02) VALUES(?,?)")) {
						ps.setLong(1, msgId);
						ps.setString(2, mobile);
						ps.execute();
					}
				}
				return;
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		}
	}

	protected void sendEmail(Connection connection, String title,
			String content, String address) throws Throwable {
		try {
			if (StringHelper.isEmpty(content) || StringHelper.isEmpty(title)
					|| StringHelper.isEmpty(address)) {
				return;
			}
			long msgId = 0;
			try (PreparedStatement ps = connection
					.prepareStatement(
							"INSERT INTO S10._1046(F02,F03,F04,F05,F07) VALUES(?,?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, title);
				ps.setString(2, content);
				ps.setInt(3, 0);
				ps.setTimestamp(4,getCurrentTimestamp(connection));
				ps.setString(5, "W");
				ps.execute();
				try (ResultSet resultSet = ps.getGeneratedKeys()) {
					if (resultSet.next()) {
						msgId = resultSet.getLong(1);
					}
				}
			}
			if (msgId > 0) {
				try (PreparedStatement ps = connection
						.prepareStatement("INSERT INTO S10._1047(F01,F02) VALUES(?,?)")) {
					ps.setLong(1, msgId);
					ps.setString(2, address);
					ps.execute();
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		}
	}

	private int insertT6123(Connection connection, int F02, String F03,
			T6123_F05 F05) throws Throwable {
		try {
			try (PreparedStatement pstmt = connection
					.prepareStatement(
							"INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?",
							PreparedStatement.RETURN_GENERATED_KEYS)) {
				pstmt.setInt(1, F02);
				pstmt.setString(2, F03);
				pstmt.setTimestamp(3, getCurrentTimestamp(connection));
				pstmt.setString(4, F05.name());

				pstmt.execute();
				try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
					if (resultSet.next()) {
						return resultSet.getInt(1);
					}
					return 0;
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
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

	/*private Connection getConnection() throws SQLException {
		try {
			SQLConnectionProvider connectionProvider = resourceProvider
					.getDataConnectionProvider(SQLConnectionProvider.class,
							P2PConst.DB_MASTER_PROVIDER);
			return connectionProvider.getConnection();
		} catch (ResourceNotFoundException e) {
			logger.error(e, e);
			throw e ;
		}
	}*/

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

	public void log(Connection connection, int F01, Throwable exception)
			throws SQLException {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Charset charset = Charset.forName("UTF-8");
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
					out, charset));
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
			throw e;
		}
	}

//	protected abstract void doConfirm(Connection connection, int orderId, Map<String, String> params) throws Throwable;
	protected abstract void doConfirm(Connection connection, int orderId, Map<String, String> params, boolean checkAmountFlag, boolean isSetLetter) throws Throwable;

	protected void doSubmit(Connection connection, int orderId,
			Map<String, String> params) throws Throwable {

	}

	protected void handleError(Connection connection, int orderId) {

	}

	@Override
	public void close() throws Exception {
	}

	public void lockAmountForPlan(Connection connection, InvestPlan investPlan, int userId, BigDecimal amount, VersionTypeEnum versionTypeEnum) throws Throwable {

	}
}
