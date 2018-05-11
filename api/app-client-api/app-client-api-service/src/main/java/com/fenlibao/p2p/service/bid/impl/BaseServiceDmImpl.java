package com.fenlibao.p2p.service.bid.impl;

import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.p2p.S61.enums.T6114_F10;
import com.dimeng.util.StringHelper;
import com.fenlibao.p2p.service.base.db.DbPoolConnection;
import com.fenlibao.p2p.service.bid.BaseDmService;
import org.springframework.stereotype.Service;

import java.sql.*;

/**
 * Created by LouisWang on 2015/8/17.
 */
@Service
public class BaseServiceDmImpl implements BaseDmService {
    @Override
    public T6114 selectT6114(int F01, boolean isSession) throws Throwable {
        T6114 record = null;
        try (Connection connection = DbPoolConnection.getInstance().getConnection())
        {
            try (PreparedStatement statement =
                         connection.prepareStatement("SELECT T6114.F01,T6114.F05,T6114.F07 FROM S61.T6114 WHERE T6114.F02=? AND F08=? LIMIT 1"))
            {
                statement.setInt(1, F01);
                statement.setString(2, T6114_F08.QY.name());
                try (ResultSet rs = statement.executeQuery())
                {
                    if (rs.next())
                    {
                        record = new T6114();
                        record.F01 = rs.getInt(1);
                        record.F05 = rs.getString(2);
                        record.F07 = StringHelper.decode(rs.getString(3));
                    }
                }
            }
        }
        return record;
    }

    @Override
    public T6119 selectT6119(int id, boolean isSession) throws Throwable {
        T6119 recorde = null;
        try (Connection connection = DbPoolConnection.getInstance().getConnection()){
            try (PreparedStatement pstmt =
                         connection.prepareStatement("SELECT F01,F02,F03 FROM S61.T6119 WHERE F01= ?"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery()){
                    if (resultSet.next())
                    {
                        recorde = new T6119();
                        recorde.F01 = resultSet.getInt(1);
                        recorde.F02 = resultSet.getInt(2);
                        recorde.F03 = resultSet.getString(3);
                    }
                }
            }
        }
        return recorde;
    }

    @Override
    public T6110 selectT6110(int id, boolean isSession) throws Throwable {
        T6110 recorde = null;
        try(Connection connection = DbPoolConnection.getInstance().getConnection()) {
            try (PreparedStatement pstmt =
                         connection.prepareStatement("SELECT userd.F01,userd.F02,userd.F04,userd.F05,userd.F06,account.F02 FROM S61.T6110 AS userd,S61.t6141 AS account WHERE userd.F01 = account.F01 AND  userd.F01= ?"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery()){
                    if (resultSet.next())
                    {
                        recorde = new T6110();
                        recorde.F01 = resultSet.getInt(1);
                        recorde.F02 = resultSet.getString(2);
                        recorde.F04 = resultSet.getString(3);
                        recorde.F05 = resultSet.getString(4);
                        recorde.F06 = T6110_F06.parse(resultSet.getString(5));
                        recorde.F14 = resultSet.getString(6);
                    }
                }

            }

        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return recorde;
    }

    @Override
    public int selectCountT6114(int userId) throws Throwable {
        int result = 0;
        String sql = "SELECT count(1) FROM S61.T6114 WHERE T6114.F10 = ? and  T6114.F02 = ?";
        try (Connection connection = DbPoolConnection.getInstance().getConnection()){
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setString(1, T6114_F10.TG.name());
                pstmt.setInt(2, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        result = resultSet.getInt(1);
                    }
                }
            }
        }
        return result;
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
}
