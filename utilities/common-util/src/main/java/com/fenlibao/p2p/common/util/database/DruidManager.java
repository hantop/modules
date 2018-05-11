package com.fenlibao.p2p.common.util.database;

/**
 * Created by Lullaby on 2015/8/5.
 */

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 获取数据库连接
 */
public class DruidManager {

    private static DruidDataSource dataSource;

    static {
        dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(DruidConfig.MYSQL_URL.getValue());
        dataSource.setUsername(DruidConfig.MYSQL_USERNAME.getValue());
        dataSource.setPassword(DruidConfig.MYSQL_PASSWORD.getValue());
        dataSource.setInitialSize(Integer.valueOf(DruidConfig.DRUID_INITIALSIZE.getValue()));
        dataSource.setMinIdle(Integer.valueOf(DruidConfig.DRUID_MINIDLE.getValue()));
        dataSource.setMaxActive(Integer.valueOf(DruidConfig.DRUID_MAXACTIVE.getValue()));
        dataSource.setMaxWait(Long.valueOf(DruidConfig.DRUID_MAXWAIT.getValue()));
        dataSource.setTimeBetweenEvictionRunsMillis(Long.valueOf(DruidConfig.DRUID_TIMEBETWEENEVICTIONRUNSMILLIS.getValue()));
        dataSource.setMinEvictableIdleTimeMillis(Long.valueOf(DruidConfig.DRUID_MINEVICTABLEIDLETIMEMILLIS.getValue()));
        dataSource.setValidationQuery(DruidConfig.DRUID_MAXWAIT.getValue());
        dataSource.setTestWhileIdle(Boolean.valueOf(DruidConfig.DRUID_TESTWHILEIDLE.getValue()));
        dataSource.setTestOnBorrow(Boolean.valueOf(DruidConfig.DRUID_TESTONBORROW.getValue()));
        dataSource.setTestOnReturn(Boolean.valueOf(DruidConfig.DRUID_TESTONRETURN.getValue()));
        dataSource.setPoolPreparedStatements(Boolean.valueOf(DruidConfig.DRUID_POOLPREPAREDSTATEMENTS.getValue()));
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.valueOf(DruidConfig.DRUID_MAXPOOLPREPAREDSTATEMENTPERCONNECTIONSIZE.getValue()));
        dataSource.setRemoveAbandoned(Boolean.valueOf(DruidConfig.DRUID_REMOVEABANDONED.getValue()));
        dataSource.setRemoveAbandonedTimeout(Integer.valueOf(DruidConfig.DRUID_REMOVEABANDONEDTIMEOUT.getValue()));
        dataSource.setLogAbandoned(Boolean.valueOf(DruidConfig.DRUID_LOGABANDONED.getValue()));
        try {
            dataSource.setFilters(DruidConfig.DRUID_FILTERS.getValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    public Connection getConnection_() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void closeResource(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeResource(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeResource(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeResource(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
