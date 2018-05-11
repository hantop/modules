package com.fenlibao.p2p.service.withdraw.impl;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.dimeng.p2p.PaymentInstitution;
import org.springframework.stereotype.Service;

import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;
import com.fenlibao.p2p.model.entity.pay.Bank;
import com.fenlibao.p2p.model.entity.pay.UserWithdrawals;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.form.trade.WithdrawManageFrom;
import com.fenlibao.p2p.model.global.FeeCode;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.withdraw.AbstractAipgService;
import com.fenlibao.p2p.service.withdraw.IAipgManageService;
import com.fenlibao.p2p.service.withdraw.IUserWithdrawalsService;
import com.fenlibao.p2p.util.loader.Sender;

@Service
public class UserWithdrawalsServiceImpl extends AbstractAipgService implements IUserWithdrawalsService {

	@Resource
	private IAipgManageService aipgManageService;
	
	@Override
	public void check(boolean passed, String check_reason, int accountId, int... ids)
			throws Throwable {
		if (ids == null || ids.length == 0) {
			return;
		}
		try (Connection connection = getConnection();) {
			connection.setAutoCommit(false);
			try {
				T6130_F09 t6130_F09 = passed ? T6130_F09.DFK : T6130_F09.TXSB;
	            Timestamp currentTimestamp = getCurrentTimestamp(connection);
				for (int id : ids) {
	                T6110 t6110 = getUserInfo(id, connection);
	                if (T6110_F07.HMD == t6110.F07)
	                {
	                    throw new BusinessException(ResponseCode.PAYMENT_BLACKLIST);
	                }
	                UserWithdrawals userWithdrawals = aipgManageService.get(connection, id);
					if (userWithdrawals == null) {
						throw new LogicalException("提现申请记录不存在");
					}
					if (userWithdrawals.F09 != T6130_F09.DSH) {
						throw new LogicalException("提现申请不是待审核状态,不能进行审核操作");
					}
					try (PreparedStatement ps = connection
							.prepareStatement("UPDATE S61.T6130 SET F09 = ?, F10 = ?, F11 = ?, F12 = ? WHERE F01 = ?")) {
						ps.setString(1, t6130_F09.name());
						ps.setInt(2, accountId);
						ps.setTimestamp(3, currentTimestamp);
						ps.setString(4, check_reason);
						ps.setInt(5, id);
						ps.execute();
					}
					if (!passed) {
						rollback(connection, userWithdrawals);
					}
				}
	    		if (passed) {
	    			writeLog(connection, "操作日志", "提现审核通过", accountId, null);
	    		} else {
	    			writeLog(connection, "操作日志", String.format("提现审核不通过,原因:%s", check_reason), accountId, null);
	    		}
	    		connection.commit();
	    		connection.setAutoCommit(true);
	        }
	        catch (Exception e)
	        {
	        	connection.rollback();
	        	connection.setAutoCommit(true);
	            throw e;
			} 
		}
	}

	@Override
	public int[] fk(boolean passed, String check_reason, int accountId, int... ids)
			throws Throwable {
		if (ids == null || ids.length == 0) {
			return new int[0];
		}
		int[] orderIds = new int[ids.length];
		try (Connection connection = getConnection();) {
			connection.setAutoCommit(false);
			int index = 0;
			try {
				T6130_F09 t6130_F09 = passed ? T6130_F09.YFK : T6130_F09.TXSB;
	            Timestamp currentTimestamp = getCurrentTimestamp(connection);
				for (int id : ids) {
	                T6110 t6110 = getUserInfo(id, connection);
	                if (T6110_F07.HMD == t6110.F07)
	                {
	                	throw new BusinessException(ResponseCode.PAYMENT_BLACKLIST);
	                }
					UserWithdrawals userWithdrawals = aipgManageService.get(connection, id);
					if (userWithdrawals == null) {
						throw new LogicalException("提现申请记录不存在");
					}
					if (userWithdrawals.F09 != T6130_F09.DFK) {
						throw new LogicalException("提现申请不是待放款状态,不能进行放款操作");
					}
					try (PreparedStatement ps = connection
							.prepareStatement("UPDATE S61.T6130 SET F09 = ?, F13 = ?, F14 = ?, F15 = ? WHERE F01 = ?")) {
						ps.setString(1, t6130_F09.name());
						ps.setInt(2, accountId);
						ps.setTimestamp(3, currentTimestamp);
						ps.setString(4, check_reason);
						ps.setInt(5, id);
						ps.execute();
					}
					if (passed) {
						orderIds[index++] = addOrder(connection, userWithdrawals, accountId);
						/*try (PreparedStatement ps = connection
								.prepareStatement("UPDATE S61.T6130 SET F16=? WHERE F01=?")) {
							ps.setString(1, T6130_F16.S.name());
							ps.setInt(2, id);
							ps.executeUpdate();
						}*/
					} else {
						rollback(connection, userWithdrawals);
					}
				}
	            if (passed)
	            {
	                writeLog(connection, "操作日志", "提现放款通过", accountId, null);
	            }
	            else
	            {
	                writeLog(connection, "操作日志", String.format("提现放款不通过,原因:%s", check_reason), accountId, null);
	            }
	            connection.commit();
	            connection.setAutoCommit(true);
	        }
	        catch (Exception e)
	        {
	        	connection.rollback();
	        	connection.setAutoCommit(true);
	            throw e;
			}
		}
		return orderIds;
	}

	// 创建订单
	private int addOrder(Connection connection, UserWithdrawals userWithdrawals, int userId)
			throws Throwable {
		int orderId = 0;
		try (PreparedStatement pstmt = connection
				.prepareStatement(
						"INSERT INTO S65.T6501 SET F02 = ?,F03 = ?, F04 = ?, F07 = ?, F09 = ?, F08 = ? ",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, OrderType.WITHDRAW.orderType());
			pstmt.setString(2, T6501_F03.DTJ.name());
			pstmt.setTimestamp(3, getCurrentTimestamp(connection));
			pstmt.setString(4, T6501_F07.HT.name());
			pstmt.setInt(5, userId);
			pstmt.setInt(6, userWithdrawals.F02);
			pstmt.execute();
			try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
				if (resultSet.next()) {
					orderId = resultSet.getInt(1);
				}
			}
		}
		try (PreparedStatement pstmt = connection
				.prepareStatement("INSERT INTO S65.T6503 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?,F09 = ?")) {
			pstmt.setInt(1, orderId);
			pstmt.setInt(2, userWithdrawals.F02);
			pstmt.setBigDecimal(3, userWithdrawals.F04);
			pstmt.setBigDecimal(4, userWithdrawals.F06);
			pstmt.setBigDecimal(5, userWithdrawals.F07);
			pstmt.setString(6, StringHelper.decode(userWithdrawals.bankId));
			pstmt.setInt(7, PaymentInstitution.LIANLIANGATE.getInstitutionCode());
			pstmt.setInt(8, userWithdrawals.F01);
			pstmt.execute();
		}
		return orderId;
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
		String content = Sender.get("sms.withdraw.failure").replace("${name}", wlzh.F05)
				.replace("${datetime}", DateTimeParser.format(userWithdrawals.F08))
				.replace("${amount}", je.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
		sendLetter(connection, userWithdrawals.F02, "提现失败", content);
	}

	private void updateT6101(Connection connection, int F01, BigDecimal F06)
			throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ?  WHERE F01 = ? ")) {
			pstmt.setBigDecimal(1, F06);
			pstmt.setTimestamp(2,getCurrentTimestamp(connection));
			pstmt.setInt(3, F01);
			pstmt.execute();
		}
	}

	private int insertT6102(Connection connection, T6102 entity)
			throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement(
						"INSERT INTO S61.T6102 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, entity.F02);
			pstmt.setInt(2, entity.F03);
			pstmt.setInt(3, entity.F04);
			pstmt.setTimestamp(4,getCurrentTimestamp(connection));
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

	@Override
	public Bank[] getBanks() throws Throwable {
		List<Bank> banks = new ArrayList<>();
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F01,F02 FROM S50.T5020 WHERE F03 = ?")) {
				ps.setString(1, InterfaceConst.STATUS_QY);
				try (ResultSet resultSet = ps.executeQuery()) {
					while (resultSet.next()) {
						Bank bank = new Bank();
						bank.id = resultSet.getInt(1);
						bank.name = resultSet.getString(2);
						banks.add(bank);
					}
					return banks.toArray(new Bank[banks.size()]);
				}
			}
		}
	}

	@Override
	public void importData(InputStream inputStream, String real_name,
			String charset) throws Throwable {

	}

	@Override
	public WithdrawManageFrom getExtractionInfo() throws Throwable {
		WithdrawManageFrom extraction = new WithdrawManageFrom();
		try (Connection conn = getConnection()) {
			try (PreparedStatement pst = conn
					.prepareStatement("SELECT F01, F02, F03 FROM S70.T7023 ORDER BY F03 DESC LIMIT 1")) {
				try (ResultSet rst = pst.executeQuery()) {
					if (rst.next()) {
						extraction.totalAmount = rst.getBigDecimal(1);
						extraction.charge = rst.getBigDecimal(2);
						extraction.updateTime = rst.getTimestamp(3);
					}
				}
			}
		}
		return extraction;
	}

	@Override
	public void export(UserWithdrawals[] txglRecords,
			OutputStream outputStream, String charset) throws Throwable {
		if (outputStream == null) {
			return;
		}
		if (StringHelper.isEmpty(charset)) {
			charset = "GBK";
		}
		if (txglRecords == null) {
			return;
		}

		try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				outputStream, charset))) {
			CVSWriter writer = new CVSWriter(out);
			writer.write("流水号");
			writer.write("用户名");
			writer.write("提现银行");
			writer.write("开户名");
			writer.write("省");
			writer.write("市");
			writer.write("所在支行 ");
			writer.write("银行卡号");
            writer.write("提现金额(元)");
            writer.write("手续费(元)");
			writer.write("是否到账");
			writer.write("审核时间");
			writer.write("操作人");
			writer.newLine();
			for (UserWithdrawals txglRecord : txglRecords) {
				if (txglRecord == null) {
					continue;
				}
				writer.write(txglRecord.F01);
				writer.write(txglRecord.userName);
				writer.write(txglRecord.extractionBank);
				writer.write(txglRecord.realName);
				writer.write(txglRecord.shengName);
				writer.write(txglRecord.shiName);
				writer.write(txglRecord.subbranch);
				writer.write(StringHelper.decode(txglRecord.bankId)+"\t");
				writer.write(txglRecord.F04);
				writer.write(txglRecord.F07);
				writer.write("否");
				writer.write(DateTimeParser.format(txglRecord.F11));
				writer.write(txglRecord.shName);
				writer.newLine();
			}
		}
	}

	@Override
	public void exportYtx(UserWithdrawals[] txglRecords,
			OutputStream outputStream, String charset) throws Throwable {
		if (outputStream == null) {
			return;
		}
		if (StringHelper.isEmpty(charset)) {
			charset = "GBK";
		}
		if (txglRecords == null) {
			return;
		}

		try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				outputStream, charset))) {
			CVSWriter writer = new CVSWriter(out);
			writer.write("流水号");
			writer.write("用户名");
			writer.write("提现银行");
			writer.write("开户名");
            writer.write("省");
            writer.write("市");
			writer.write("所在支行 ");
			writer.write("银行卡号");
            writer.write("提现金额(元)");
            writer.write("手续费(元)");
			writer.write("是否到账");
			writer.write("放款时间");
			writer.write("放款人");
			writer.newLine();
			for (UserWithdrawals txglRecord : txglRecords) {
				if (txglRecord == null) {
					continue;
				}
				writer.write(txglRecord.F01);
				writer.write(txglRecord.userName);
				writer.write(txglRecord.extractionBank);
				writer.write(txglRecord.realName);
                writer.write(txglRecord.shengName);
                writer.write(txglRecord.shiName);
				writer.write(txglRecord.subbranch);
				writer.write(StringHelper.decode(txglRecord.bankId)+"\t");
				writer.write(txglRecord.F04);
				writer.write(txglRecord.F07);
				writer.write(txglRecord.F16.getChineseName());
				writer.write(DateTimeParser.format(txglRecord.F14));
				writer.write(txglRecord.txName);
				writer.newLine();
			}
		}
	}
	
	@Override
	public void exportYtxContent(UserWithdrawals[] txglRecords,
			OutputStream outputStream, String charset) throws Throwable {
		if (outputStream == null) {
			return;
		}
		if (StringHelper.isEmpty(charset)) {
			charset = "GBK";
		}
		if (txglRecords == null) {
			return;
		}
		
		try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				outputStream, charset))) {
			CVSWriter writer = new CVSWriter(out);
			writer.write("流水号");
			writer.write("用户名");
			writer.write("提现银行");
			writer.write("所在支行 ");
			writer.write("银行卡号");
            writer.write("提现金额(元)");
            writer.write("手续费(元)");
			writer.write("是否到账");
			writer.write("放款时间");
			writer.write("放款人");
			writer.newLine();
			for (UserWithdrawals txglRecord : txglRecords) {
				if (txglRecord == null) {
					continue;
				}
				writer.write(txglRecord.F01);
				writer.write(txglRecord.userName);
				writer.write(txglRecord.extractionBank);
				writer.write(txglRecord.subbranch);
				writer.write("\t"+StringHelper.decode(txglRecord.bankId));
				writer.write(txglRecord.F04);
				writer.write(txglRecord.F07);
				writer.write(txglRecord.F16.getChineseName());
				writer.write(DateTimeParser.format(txglRecord.F14));
				writer.write(txglRecord.txName);
				writer.newLine();
			}
		}
	}

	@Override
	public BigDecimal getTxze() throws Throwable {
		BigDecimal txze = new BigDecimal(0);
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT SUM(F04) FROM S61.T6130 WHERE F09=?")) {
				ps.setString(1, T6130_F09.YFK.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						txze = rs.getBigDecimal(1);
					}
				}
			}
		}
		return txze;
	}

	@Override
	public BigDecimal getTxsxf() throws Throwable {
		BigDecimal txsxf = new BigDecimal(0);
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT SUM(F07) FROM S61.T6130 WHERE F09=?")) {
				ps.setString(1, T6130_F09.YFK.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						txsxf = rs.getBigDecimal(1);
					}
				}
			}
		}
		return txsxf;
	}
	
    private T6110 getUserInfo(int id, Connection conn)
        throws Throwable
    {
        T6110 t6110 = new T6110();
        try (PreparedStatement ps =
            conn.prepareStatement("SELECT T6110.F02,T6110.F07 FROM S61.T6110 WHERE T6110.F01=(SELECT T6130.F02 FROM S61.T6130 WHERE T6130.F01 = ?)"))
        {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    t6110.F02 = rs.getString(1);
                    t6110.F07 = T6110_F07.parse(rs.getString(2));
                }
            }
        }
        return t6110;
    }
	
}
