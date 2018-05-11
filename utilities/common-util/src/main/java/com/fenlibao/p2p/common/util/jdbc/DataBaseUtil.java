package com.fenlibao.p2p.common.util.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseUtil {

		/*
		 * 定义链接数据的参数
		 */
		private static final String dbUrl = "jdbc:mysql://192.168.27.236:3306"; //192.168.27.239:3308
		private static final String dbUsername = "test"; //root
		private static final String dbPassword = "test.fenlibao.com"; //flb.123
		private static final String jdbcName = "com.mysql.jdbc.Driver";
		
		public static Connection getConnection() throws Exception {
			Class.forName(jdbcName); //加载MySQL的jdbc驱动
			Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword); //创建与MySQL数据库的连接类的实例
			return conn;
		}
		
		public static void closeConnection(Connection conn) throws SQLException {
			if (conn != null) {
				conn.close();
			}
		}
		
		public static void main(String[] args) {
			try {
				Connection conn = DataBaseUtil.getConnection();
				System.out.println("数据库连接成功！");
				DataBaseUtil.closeConnection(conn);
				System.out.println("关闭数据库连接");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
