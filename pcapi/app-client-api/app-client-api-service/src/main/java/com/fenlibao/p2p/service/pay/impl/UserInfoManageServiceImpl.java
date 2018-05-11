package com.fenlibao.p2p.service.pay.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.stereotype.Service;

import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S61.enums.T6118_F05;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S64.enums.T6410_F07;
import com.fenlibao.p2p.service.base.abstracts.BaseAbstractService;
import com.fenlibao.p2p.service.pay.IUserInfoManageService;

@Service
public class UserInfoManageServiceImpl extends BaseAbstractService implements IUserInfoManageService {

	@Override
	public String isYuqi(int userId) throws Throwable {
		String sql = "SELECT DATEDIFF(?,F08) FROM S62.T6252 WHERE F09=? AND F03=? AND F08 < SYSDATE()";
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setTimestamp(1,getCurrentTimestamp(connection));
				ps.setString(2, T6252_F09.WH.name());
				ps.setInt(3, userId);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						int days = rs.getInt(1);
						if (days > 0) {
							return "Y";
						}
					}
				}
			}
		}
		return "N";
	}

	@Override
	public T6101 search(int userId) throws Throwable {
		try (Connection connection = getConnection()) {
			 T6101 record = null;
			    try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ? LIMIT 1")) {
			        pstmt.setInt(1, userId);
			        pstmt.setString(2, T6101_F03.WLZH.name());
			        try(ResultSet resultSet = pstmt.executeQuery()) {
			            if(resultSet.next()) {
			                record = new T6101();
			                record.F01 = resultSet.getInt(1);
			                record.F02 = resultSet.getInt(2);
			                record.F03 = T6101_F03.parse(resultSet.getString(3));
			                record.F04 = resultSet.getString(4);
			                record.F05 = resultSet.getString(5);
			                record.F06 = resultSet.getBigDecimal(6);
			            }
			        }
			    }
			    return record;
		}
	}

    /**
     * 查询用户风险备用金资金
     * @return
     * @throws Throwable
     */
    @Override
    public T6101 searchFxbyj(int userId) throws Throwable {
        try (Connection connection = getConnection()) {
            T6101 record = null;
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ? LIMIT 1")) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, T6101_F03.FXBZJZH.name());
                try(ResultSet resultSet = pstmt.executeQuery()) {
                    if(resultSet.next()) {
                        record = new T6101();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = T6101_F03.parse(resultSet.getString(3));
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getBigDecimal(6);
                    }
                }
            }
            return record;
        }
    }


	@Override
	public String getUserName(int userID) throws Throwable {
		try(Connection connection = getConnection()){
			try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1")) {
		        pstmt.setInt(1, userID);
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                return resultSet.getString(1);
		            }
		        }
		    }
		}
		return "";
	}

	/**
	 * 查询持有债权数量
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private int cyzqsl(int userId) throws Throwable {
		String sql = "SELECT COUNT(*) AS F01 FROM S62.T6251 INNER JOIN S62.T6230 ON T6251.F03 = T6230.F01 WHERE T6251.F04 = ? AND T6230.F20 IN (?,?,?)";
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setInt(1, userId);
				ps.setString(2, T6230_F20.TBZ.name());
				ps.setString(3, T6230_F20.DFK.name());
				ps.setString(4, T6230_F20.HKZ.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getInt(1);
					}
				}
			}
		}
		return 0;
	}

	/**
	 * 查询持有优选投资数量
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private int cylcjhsl(int userId) throws Throwable {
		String sql = "SELECT COUNT(*) AS F01 FROM S64.T6411 INNER JOIN S64.T6410 ON T6411.F02 = T6410.F01 WHERE T6411.F03 = ? AND T6410.F07 = ?";
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setInt(1, userId);
				ps.setString(2, T6410_F07.YSX.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getInt(1);
					}
				}
			}
		}
		return 0;
	}
	
	/**
	 * 逾期金额
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
    @SuppressWarnings("unused")
    private BigDecimal getYqMoney(int userId)
        throws Throwable
    {
		if (userId < 0) {
			return null;
		}
		BigDecimal yqMoney = new BigDecimal(0);
		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F03 = ?  AND F09 = ? AND F10 < ?");) {
				ps.setInt(1, userId);
				ps.setString(2, T6252_F09.WH.name());
                ps.setDate(3, getCurrentDate(conn));
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						yqMoney = rs.getBigDecimal(1);
					}
				}
			}
		}
		return yqMoney;
	}

	/**
	 * 借款笔数
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private int getCreditCount(int userId) throws Throwable {
		if (userId < 0) {
			return 0;
		}
		int jkCount = 0;
		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT COUNT(*) FROM S62.T6230 WHERE F02 = ?");) {
				ps.setInt(1, userId);
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						jkCount = rs.getInt(1);
					}
				}
			}
		}
		return jkCount;
	}

	/**
	 * 借款成功笔数
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private int getCreditSuccCount(int userId) throws Throwable {
		if (userId < 0) {
			return 0;
		}
		int succCount = 0;
		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT COUNT(*) FROM S62.T6230 WHERE F02 = ? AND F20 IN (?,?,?)");) {
				ps.setInt(1, userId);
				ps.setString(2, T6230_F20.YDF.name());
				ps.setString(3, T6230_F20.HKZ.name());
				ps.setString(4, T6230_F20.YJQ.name());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						succCount = rs.getInt(1);
					}
				}
			}
		}
		return succCount;
	}

	/**
	 * 还清笔数
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	@SuppressWarnings("unused")
	private int getHqCount(int userId) throws Throwable {
		if (userId < 0) {
			return 0;
		}
		int hqCount = 0;
		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT COUNT(*) FROM S62.T6230 WHERE F02 = ? AND F20 = ?");) {
				ps.setInt(1, userId);
				ps.setString(2, T6230_F20.YJQ.name());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						hqCount = rs.getInt(1);
					}
				}
			}
		}
		return hqCount;
	}

	/**
	 * 未还清笔数
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private int getWhqCount(int userId) throws Throwable {
		if (userId < 0) {
			return 0;
		}
		int whqCount = 0;
		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT COUNT(*) FROM S62.T6230 WHERE F02 = ? AND F20 IN (?,?)");) {
				ps.setInt(1, userId);
				ps.setString(2, T6230_F20.YDF.name());
				ps.setString(3, T6230_F20.HKZ.name());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						whqCount = rs.getInt(1);
					}
				}
			}
		}
		return whqCount;
	}

	/**
	 * 借款总额
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	@SuppressWarnings("unused")
	private BigDecimal getJkTotle(int userId) throws Throwable {
		if (userId < 0) {
			return null;
		}
		BigDecimal jkTotle = new BigDecimal(0);
		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT IFNULL(SUM(F05),0) FROM S62.T6230 WHERE F02 = ? AND F20 IN (?,?,?)");) {
				ps.setInt(1, userId);
				ps.setString(2, T6230_F20.YDF.name());
				ps.setString(3, T6230_F20.HKZ.name());
				ps.setString(4, T6230_F20.YJQ.name());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						jkTotle = rs.getBigDecimal(1);
					}
				}
			}
		}
		return jkTotle;
	}

	/**
	 * 待还本息
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	@SuppressWarnings("unused")
	private BigDecimal getDhbx(int userId) throws Throwable {
		if (userId < 0) {
			return null;
		}
		BigDecimal dhTotle = new BigDecimal(0);
		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT SUM(IFNULL(F07,0)) FROM S62.T6252 WHERE F03 = ? AND F09 = ?");) {
				ps.setInt(1, userId);
				ps.setString(2, T6252_F09.WH.name());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						dhTotle = rs.getBigDecimal(1);
					}
				}
			}
		}
		return dhTotle;
	}

	@Override
	public boolean isSmrz(int userId) throws Throwable {
		try(Connection connection = getConnection()){
			 try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1")) {
			        pstmt.setInt(1, userId);
			        try(ResultSet resultSet = pstmt.executeQuery()) {
			            if(resultSet.next()) {
			               if(T6118_F02.parse(resultSet.getString(1))==T6118_F02.TG){
			            	   return true;
			               }
			            }
			        }
			    }
		}
		return false;
	}

	@Override
	public boolean isTxmm(int userId) throws Throwable {
		try(Connection connection = getConnection()){
			 try (PreparedStatement pstmt = connection.prepareStatement("SELECT F05 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1")) {
			        pstmt.setInt(1, userId);
			        try(ResultSet resultSet = pstmt.executeQuery()) {
			            if(resultSet.next()) {
			            	if(T6118_F05.parse(resultSet.getString(1)) == T6118_F05.YSZ){
			            		return true;
			            	}
			            }
			        }
			    }
		}
		return false;
	}

	@Override
	public T6110 getUserInfo(int userId) throws Throwable {
		try(Connection connection = getConnection()){
		  T6110 record = null;
		    try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1")) {
		        pstmt.setInt(1, userId);
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                record = new T6110();
		                record.F01 = resultSet.getInt(1);
		                record.F02 = resultSet.getString(2);
		                record.F03 = resultSet.getString(3);
		                record.F04 = resultSet.getString(4);
		                record.F05 = resultSet.getString(5);
		                record.F06 = T6110_F06.parse(resultSet.getString(6));
		                record.F07 = T6110_F07.parse(resultSet.getString(7));
		                record.F08 = T6110_F08.parse(resultSet.getString(8));
		                record.F09 = resultSet.getTimestamp(9);
		                record.F10 = T6110_F10.parse(resultSet.getString(10));
		            }
		        }
		    }
		    return record;
	}
	}
	
}
