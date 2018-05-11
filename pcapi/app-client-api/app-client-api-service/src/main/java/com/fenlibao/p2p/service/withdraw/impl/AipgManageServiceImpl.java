package com.fenlibao.p2p.service.withdraw.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.DateTimeParser;
import com.fenlibao.p2p.model.entity.pay.UserWithdrawals;
import com.fenlibao.p2p.model.global.FeeCode;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.service.trade.IOrderService;
import com.fenlibao.p2p.service.withdraw.AbstractAipgService;
import com.fenlibao.p2p.service.withdraw.IAipgManageService;
import com.fenlibao.p2p.service.withdraw.IAipgWithdrawService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Payment;
import com.fenlibao.p2p.util.pay.DigestUtil;
import com.fenlibao.p2p.util.pay.HttpClientHandler;
import com.fenlibao.p2p.util.pay.OrderUtil;

@Service
public class AipgManageServiceImpl extends AbstractAipgService implements
		IAipgManageService {

	@Resource
	private IOrderService orderService;
    @Resource
    private IAipgWithdrawService aipgWithdrawService;

	@Override
	public String loan(Map<String, String> requsetParams) throws Throwable {
		// 后台通知地址
		String notifyURL = getNotifyURL();
		// 异步通知
		requsetParams.put("notify_url", notifyURL);
		// 商户编号(必)
		requsetParams.put("oid_partner", getOidPartner());
		// 签名
		String sign = DigestUtil.createSendSign(requsetParams);
		requsetParams.put("sign", sign);
		// 请求地址
		String actionUrl = getwithdrawApply();
		// 发送请求拿到同步返回结果字符串(Json格式)
		boolean isTestAccount = BooleanParser.parse(Payment
				.get(Payment.IS_ACCOUNT_TEST));
		 if (isTestAccount) { //是否是测试账号
			 actionUrl = Payment.get(Payment.WITHDRAW_SERVER_TEST_URL);
			 logger.debug("actionUrl >>> "+actionUrl);
		 }
		 logger.info("开始向连连提交代付申请。。。");
		 String retString = HttpClientHandler.doPostJson(requsetParams, actionUrl);
		logger.info("代付返回信息：" + retString);

		return retString;
	}

	/**
	 * {@inheritDoc} 放款失败
	 */
	@Override
	public int fksb(String check_reason, int id) throws Throwable {
		if (check_reason.length() >= 100) {
			check_reason = check_reason.substring(0, 99);
		}
		int t6130Id = 0;
		if (id == 0) {
			return 0;
		}
		try (Connection connection = getConnection();) {
			connection.setAutoCommit(false);
			try {
				T6130_F09 t6130_F09 = T6130_F09.TXSB;
				T6110 t6110 = getUserInfo(id, connection);
				if (T6110_F07.HMD == t6110.F07) {
					throw new LogicalException("用户:" + t6110.F02
							+ "已经被拉黑，不能进行提现！");
				}
				// 查询 T6130 提现记录
				t6130Id = getT6130(connection, id, T6130_F09.YFK);
				if (t6130Id == 0) {
					throw new LogicalException("提现申请记录不存在");
				}
				UserWithdrawals userWithdrawals = get(connection, t6130Id);
				if (userWithdrawals == null) {
					throw new LogicalException("提现申请记录不存在");
				}
				try (PreparedStatement ps = connection
						.prepareStatement("UPDATE S61.T6130 SET F09 = ?, F15 = ? WHERE F01 = ?")) {
					ps.setString(1, t6130_F09.name());
					ps.setString(2, check_reason);
					ps.setInt(3, t6130Id);
					ps.execute();
				}
				rollback(connection, userWithdrawals);

				writeLog(connection, "操作日志",
						String.format("提现放款不通过,原因:%s", check_reason), id, null);

				connection.commit();
				connection.setAutoCommit(true);
			} catch (Exception e) {
				connection.rollback();
				connection.setAutoCommit(true);
				throw e;
			}
		}
		return t6130Id;
	}

	/**
	 * 查询用户提现申请ID <功能详细描述>
	 * 
	 * @param id
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws SQLException
	 */
	private int getT6130(Connection connection, int orderId, T6130_F09 status)
			throws ResourceNotFoundException, SQLException {

		try (PreparedStatement ps = connection
				.prepareStatement("SELECT F01 FROM S61.T6130 where F09 = ? AND F01 = (SELECT F09 from  S65.T6503 WHERE F01 = ?) FOR UPDATE")) {
			ps.setString(1, status.name());
			ps.setInt(2, orderId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		return 0;
	}

	/**
	 * 提现失败
	 * 
	 * @param check_reason
	 * @param txglRecord
	 * @throws Throwable
	 */
	private void rollback(Connection connection, UserWithdrawals userWithdrawals)
			throws Throwable {
		if (userWithdrawals == null) {
			throw new ParameterException("提现记录不存在");
		}
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
			if (sdzh.F06
					.compareTo(userWithdrawals.F04.add(userWithdrawals.F07)) < 0) {
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
				t6102.F09 = "提现撤销,本金返还";
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
				t6102.F09 = "提现撤销,手续费返还";
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
				t6102.F09 = "提现撤销,本金返还";
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
				t6102.F09 = "提现撤销,手续费返还";
				insertT6102(connection, t6102);
			}
			// 增加往来账户资金
			updateT6101(connection, wlzh.F01, wlzh.F06);
		}
		BigDecimal je = userWithdrawals.F04.add(userWithdrawals.F07);
		String content = String.format(
				"尊敬的用户：您好！您于%s提交的%s元提现申请失败，金额已经退回账户。感谢您对我们的关注与支持！",
				DateTimeParser.format(userWithdrawals.F08),
				je.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		sendLetter(connection, userWithdrawals.F02, "提现失败", content);
	}

	/**
	 * 根据用户ID，获取用户对象
	 * 
	 * @param connection
	 * @param F01
	 * @return
	 * @throws SQLException
	 */
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
	 * 更新资金账号 <功能详细描述>
	 * 
	 * @param connection
	 * @param F01
	 * @param F06
	 * @throws Throwable
	 */
	private void updateT6101(Connection connection, int F01, BigDecimal F06)
			throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ?  WHERE F01 = ? ")) {
			pstmt.setBigDecimal(1, F06);
			pstmt.setTimestamp(2, getCurrentTimestamp(connection));
			pstmt.setInt(3, F01);
			pstmt.execute();
		}
	}

	/**
	 * 增加资金流水 <功能详细描述>
	 * 
	 * @param connection
	 * @param entity
	 * @return
	 * @throws Throwable
	 */
	private int insertT6102(Connection connection, T6102 entity)
			throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement(
						"INSERT INTO S61.T6102 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, entity.F02);
			pstmt.setInt(2, entity.F03);
			pstmt.setInt(3, entity.F04);
			pstmt.setTimestamp(4, DateUtil.secondAddToTimestamp(getCurrentTimestamp(connection), 1)); //创建时间添加 1 秒为了交易记录的更好展示
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
	 * 锁定资金账户 <功能详细描述>
	 * 
	 * @param connection
	 * @param F02
	 * @param F03
	 * @return
	 * @throws SQLException
	 */
	private T6101 selectT6101(Connection connection, int F02, T6101_F03 F03)
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

	public UserWithdrawals get(Connection connection, int id) throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT T6130.F01 AS F01, T6130.F02 AS F02, T6130.F03 AS F03, T6130.F04 AS F04, T6130.F06 AS F05, T6130.F07 AS F06, T6130.F08 AS F07, T6130.F09 AS F08, T6130.F10 AS F09, T6130.F11 AS F10, T6130.F12 AS F11, T6130.F13 AS F12, T6130.F14 AS F13, T6130.F15 AS F14, T6110.F02 AS F15, T6110.F06 AS F16,T6114.F04 AS F17,T5020.F02 AS F18,T6114.F07 AS F19,T6114.F05 AS F20,T6130.F16 AS F21, T6114.F11 AS F22 FROM S61.T6130 INNER JOIN S61.T6110 ON T6130.F02 = T6110.F01 INNER JOIN S61.T6114 ON T6130.F03=T6114.F01 INNER JOIN S50.T5020 ON T6114.F03=T5020.F01 WHERE T6130.F01=?")) {
			pstmt.setInt(1, id);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					UserWithdrawals record = new UserWithdrawals();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getInt(3);
					record.F04 = resultSet.getBigDecimal(4);
					record.F06 = resultSet.getBigDecimal(5);
					record.F07 = resultSet.getBigDecimal(6);
					record.F08 = resultSet.getTimestamp(7);
					record.F09 = T6130_F09.parse(resultSet.getString(8));
					record.F10 = resultSet.getInt(9);
					record.F11 = resultSet.getTimestamp(10);
					record.F12 = resultSet.getString(11);
					record.F13 = resultSet.getInt(12);
					record.F14 = resultSet.getTimestamp(13);
					record.F15 = resultSet.getString(14);
					record.userName = resultSet.getString(15);
					record.userType = T6110_F06.parse(resultSet.getString(16));
					record.location = getRegion(resultSet.getInt(17));
					record.extractionBank = resultSet.getString(18);
					record.bankId = resultSet.getString(19);
					record.subbranch = resultSet.getString(20);
					record.shName = getName(record.F10);
					record.txName = getName(record.F13);
					record.realName = StringHelper.isEmpty(resultSet
							.getString(22)) ? getUserName(record.F02,
							record.userType.name()) : resultSet.getString(22); // 开户名
					return record;
				}
			}
		}
		return null;
	}

	/**
	 * 查询区域名称
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	private String getRegion(int id) throws SQLException {
		if (id <= 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F06,F07,F08 FROM S50.T5019 WHERE F01=?")) {
				ps.setInt(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						sb.append(rs.getString(1));
						sb.append(",");
						sb.append(rs.getString(2));
						sb.append(",");
						sb.append(rs.getString(3));
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 用户姓名 <功能详细描述>
	 * 
	 * @param id
	 * @param userType
	 * @return
	 * @throws SQLException
	 */
	private String getUserName(int id, String userType) throws SQLException {

		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02 FROM S61.T6141 WHERE F01=?")) {
				ps.setInt(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getString(1);
					}
				}
			}
		}
		return "";
	}

	/**
	 * 审核人信息 <功能详细描述>
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	private String getName(int id) throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F02 FROM S71.T7110 WHERE T7110.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getString(1);
					}
				}
			}
		}
		return "";
	}

	/**
	 * 查询个人信息 <功能详细描述>
	 * 
	 * @param id
	 * @param conn
	 * @return
	 * @throws Throwable
	 */
	private T6110 getUserInfo(int id, Connection conn) throws Throwable {
		T6110 t6110 = new T6110();
		try (PreparedStatement ps = conn
				.prepareStatement("SELECT T6110.F02,T6110.F07 FROM S61.T6110 WHERE T6110.F01=(SELECT T6130.F02 FROM S61.T6130 WHERE T6130.F01 = ?)")) {
			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					t6110.F02 = rs.getString(1);
					t6110.F07 = T6110_F07.parse(rs.getString(2));
				}
			}
		}
		return t6110;
	}

	@Override
	public String queryTrade(String orderId) throws Throwable {
		logger.info("连连代付对账");
		// 构建信息集合
		Map<String, String> requestParam = getRequestParams(orderId);
		// 签名
		String sign = DigestUtil.createSendSign(requestParam);
		// 签名(必)
		requestParam.put("sign", sign);
		// 请求地址
		String actionUrl = getWithdrawChkUrl();

		// 发送请求拿到同步返回结果字符串
		String retString = HttpClientHandler
				.doPostJson(requestParam, actionUrl);

		return retString;
	}

	/**
	 * 代付查询（提现）请求信息集合
	 * 
	 * @param requestEntity
	 *            请求实体类
	 * @return 返回map集合
	 * @throws Exception
	 */
	private Map<String, String> getRequestParams(String orderId)
			throws Exception {
		// 请求参数
		Map<String, String> params = new LinkedHashMap<String, String>();
		// 商户编号(必)
		params.put("oid_partner", getOidPartner());
		// 签名方式
		params.put("sign_type", getSignType());

		int orderID = Integer.valueOf(orderId);
		Timestamp orderTime = orderService.getCreateTimeByOrderId(orderID);
		String no_order = OrderUtil.genLlpWithdrawOrderId(orderID, orderTime);
		// 是否为测试
		boolean isTest = BooleanParser.parse(Payment.get(Payment.IS_PAY_TEST));
		if (isTest) {
			no_order = OrderUtil.genLlpWithdrawOrderId_Test(orderID, orderTime);
		}
		// 订单号
		params.put("no_order", no_order);
		// 商户时间(格式: YYYYMMDDH24MISS)
		// params.put("dt_order", "");
		// 连连支付支付单号
		// params.put("oid_paybill", "");
		// 收付标识
		params.put("type_dc", "1"); // 一定要是1不能空
		// 查询版本号
		// params.put("query_version", "");

		return params;
	}

	@Override
	public void withdrawRollback(int withdrawApplyId, String failureReason) throws Throwable {
		String failureReason_100 = failureReason;
		if (failureReason.length() >= 100) { //t6130.F15 length=100
			failureReason_100 = failureReason.substring(0, 90);
		}
		try (Connection connection = this.getConnection()) {
			try {
				connection.setAutoCommit(false);
				UserWithdrawals userWithdrawals = get(connection, withdrawApplyId);
				if (userWithdrawals == null) {
					throw new LogicalException("提现申请记录不存在");
				}
				try (PreparedStatement ps = connection
						.prepareStatement("UPDATE S61.T6130 SET F09 = ?, F15 = ? WHERE F01 = ?")) {
					ps.setString(1, T6130_F09.TXSB.name());
					ps.setString(2, failureReason_100);
					ps.setInt(3, withdrawApplyId);
					ps.execute();
				}
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
						t6102.F09 = "提现撤销,本金返还";
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
						t6102.F09 = "提现撤销,手续费返还";
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
						t6102.F09 = "提现撤销,本金返还";
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
						t6102.F09 = "提现撤销,手续费返还";
						insertT6102(connection, t6102);
					}
					// 增加往来账户资金
					updateT6101(connection, wlzh.F01, wlzh.F06);
				}
				BigDecimal amount = userWithdrawals.F04.add(userWithdrawals.F07);
				// 暂时
				String content = String.format(
						"尊敬的用户%s：您于%s提交的%s元提现申请失败，金额已经退回账户。感谢您对我们的关注与支持！",
						wlzh.F05, DateTimeParser.format(userWithdrawals.F08), amount
								.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				sendLetter(connection, userWithdrawals.F02, "提现失败", content);
				if (failureReason.length() > 200) { //t7120.F05 length=100
					failureReason = failureReason.substring(0, 180);
				}
				writeLog(connection, "操作日志", String.format("提现失败,原因:%s", failureReason), 
						InterfaceConst.BACK_AUTO_OPERATION_ACCOUNT_ID, null);
				connection.commit();
				connection.setAutoCommit(true);
			} catch (Exception e) {
				connection.rollback();
				connection.setAutoCommit(true);
				String msg = String.format("refund fail, withdrawApplyOrderId[%s]", withdrawApplyId);
				aipgWithdrawService.insertRefundFailLog(withdrawApplyId, msg + e);
				logger.info(msg + e.getMessage());
				logger.error(e.toString(), e);
				throw e;
			}
		}
	}

}
