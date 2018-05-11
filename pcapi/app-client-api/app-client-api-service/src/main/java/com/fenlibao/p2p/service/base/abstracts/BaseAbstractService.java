package com.fenlibao.p2p.service.base.abstracts;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.util.StringHelper;
import com.fenlibao.p2p.service.base.db.DbPoolConnection;

public abstract class BaseAbstractService {
	
	protected static final Logger logger=LogManager.getLogger(BaseAbstractService.class);

	protected Connection getConnection() throws SQLException {
		return DbPoolConnection.getInstance().getConnection();
	}
	
	protected Timestamp getCurrentTimestamp(Connection connection)
	        throws Throwable
	    {
	        try (PreparedStatement pstmt = connection.prepareStatement("SELECT CURRENT_TIMESTAMP()"))
	        {
	            try (ResultSet resultSet = pstmt.executeQuery())
	            {
	                if (resultSet.next())
	                {
	                    return resultSet.getTimestamp(1);
					}
				}
			}
			return null;
		}
	
	protected Date getCurrentDate(Connection connection) throws Throwable {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT CURRENT_DATE()"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getDate(1);
				}
			}
		}
		return null;
	}
	
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

	/**
	 * 发送短信
	 * @param connection
	 * @param mobile
	 * @param content
	 * @throws Throwable
	 */
	protected void sendMsg(Connection connection, String mobile, String content)
	        throws Throwable
	    {
	        try
	        {
	            if (!StringHelper.isEmpty(content) && !StringHelper.isEmpty(mobile))
	            {
	                long msgId = 0;
	                try (PreparedStatement ps =
	                    connection.prepareStatement("INSERT INTO S10._1040(F02,F03,F04,F05) values(?,?,?,?)",
	                        Statement.RETURN_GENERATED_KEYS))
	                {
	                    ps.setInt(1, 0);
	                    ps.setString(2, content);
	                    ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
	                    ps.setString(4, "W");
	                    ps.execute();
	                    try (ResultSet resultSet = ps.getGeneratedKeys())
	                    {
	                        if (resultSet.next())
	                        {
	                            msgId = resultSet.getLong(1);
	                        }
	                    }
	                }
	                if (msgId > 0)
	                {
	                    try (PreparedStatement ps =
	                        connection.prepareStatement("INSERT INTO S10._1041(F01,F02) VALUES(?,?)"))
	                    {
	                        ps.setLong(1, msgId);
	                        ps.setString(2, mobile);
	                        ps.execute();
	                    }
	                }
	                return;
	            }
	        }
	        catch (Exception e)
	        {
	            logger.error(e, e);
	            throw e;
	        }
	    }
	
	/**
	 * 发送站内信
	 * @param connection
	 * @param userId
	 * @param title
	 * @param content
	 * @throws Throwable
	 */
	protected void sendLetter(Connection connection, int userId, String title, String content)
	        throws Throwable
	    {
	        int letterId = insertT6123(connection, userId, title, T6123_F05.WD);
	        insertT6124(connection, letterId, content);
	    }

	    private int insertT6123(Connection connection, int F02, String F03, T6123_F05 F05)
	        throws Throwable
	    {
	        try
	        {
	            try (PreparedStatement pstmt =
	                connection.prepareStatement("INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?",
	                    PreparedStatement.RETURN_GENERATED_KEYS))
	            {
	                pstmt.setInt(1, F02);
	                pstmt.setString(2, F03);
	                pstmt.setTimestamp(3, getCurrentTimestamp(connection));
	                pstmt.setString(4, F05.name());
	                
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
	        catch (Exception e)
	        {
	            logger.error(e, e);
	            throw e;
	        }
	    }
	    
	    protected void insertT6124(Connection connection, int F01, String F02)
	        throws SQLException
	    {
	        try
	        {
	            try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S61.T6124 SET F01 = ?, F02 = ?"))
	            {
	                pstmt.setInt(1, F01);
	                pstmt.setString(2, F02);
	                pstmt.execute();
	            }
	        }
	        catch (Exception e)
	        {
	            logger.error(e, e);
	            throw e;
	        }
	    }
}
