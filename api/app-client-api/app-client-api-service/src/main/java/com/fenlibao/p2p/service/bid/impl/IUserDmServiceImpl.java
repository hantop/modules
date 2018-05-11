package com.fenlibao.p2p.service.bid.impl;

import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.enums.*;
import com.dimeng.util.StringHelper;
import com.fenlibao.p2p.model.form.BankCardForm;
import com.fenlibao.p2p.model.form.BankCardQuery;
import com.fenlibao.p2p.service.base.db.DbPoolConnection;
import com.fenlibao.p2p.service.bid.IUserDmService;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by LouisWang on 2015/8/17.
 */
@Service
public class IUserDmServiceImpl extends BaseServiceDmImpl implements IUserDmService {
    @Override
    public T6141 selectT6141(int id, boolean isSession) throws Throwable {
        T6141 recorde = null;
        try (Connection connection = DbPoolConnection.getInstance().getConnection()){
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01,F02,F04,F07 FROM S61.T6141 WHERE F01= ?  "))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery()){
                    if (resultSet.next())
                    {
                        recorde = new T6141();
                        recorde.F01 = resultSet.getInt(1);
                        recorde.F02 = resultSet.getString(2);
                        recorde.F04 = T6141_F04.parse(resultSet.getString(3));
                        recorde.F07 = StringHelper.decode(resultSet.getString(4));
                    }
                }
            }
        }

        return recorde;
    }

    /**
     * 根据第三方账号更新银行卡可用
     * @param status 状态
     * @param auth 认证
     * @param threeAccNo  第三方账号
     * @return
     */
    @Override
    public int updateT6114Status(T6114_F08 status, T6114_F10 auth, String threeAccNo) throws SQLException {
        //更新实名认证信息
        try (Connection connection = DbPoolConnection.getInstance().getConnection()){
            try (PreparedStatement stats =
                         connection.prepareStatement("UPDATE S61.T6114 SET F08=?,F10=? WHERE F02=(SELECT F01 FROM S61.T6119 WHERE F03 = ?) limit 1"))
            {
                stats.setString(1, status.name());
                stats.setString(2, auth.name());
                stats.setString(3, threeAccNo);
                stats.execute();
            }
        }

        return 0;
    }

    @Override
    public int updateT6114Status(T6114_F08 status, T6114_F10 auth,int userId) throws SQLException {
        //更新实名认证信息
        try (Connection connection = DbPoolConnection.getInstance().getConnection()){
            try (PreparedStatement stats =
                         connection.prepareStatement("UPDATE S61.T6114 SET F08=?,F10=? WHERE F02=? limit 1"))
            {
                stats.setString(1, status.name());
                stats.setString(2, auth.name());
                stats.setInt(3, userId);
                stats.execute();
            }
        }

        return 0;
    }

    @Override
    public int insertT6114(int userId,BankCardForm bankCardForm, T6114_F08 f08, T6114_F10 status) throws Throwable {
        boolean ss = false;
        if (bankCardForm == null)
        {
            throw new Exception("参数错误");
        }
            String bankNum = bankCardForm.getBanknumber();
            StringBuilder sb = new StringBuilder();
            sb.append(bankNum.substring(0, 3));
            sb.append("*************");
            sb.append(bankNum.substring(bankNum.length() - 4, bankNum.length()));

        try (Connection con = DbPoolConnection.getInstance().getConnection()){
            try (PreparedStatement pstmt = con.prepareStatement("INSERT INTO S61.T6114 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = now(),F10 = ?,F11 = ?,F12 = ?"))
            {

                pstmt.setInt(1,userId);
                pstmt.setString(2, bankCardForm.getBankId());
                pstmt.setString(3,bankCardForm.getCity());
                pstmt.setString(4,bankCardForm.getSubbranch());
                pstmt.setString(5,sb.toString());
                pstmt.setString(6,StringHelper.encode(bankCardForm.getBanknumber()));
                pstmt.setString(7,String.valueOf(f08));
                pstmt.setString(8, String.valueOf(status));
                pstmt.setString(9, bankCardForm.getUserName());
                pstmt.setString(10, bankCardForm.getUserType());


                ss =  pstmt.execute();
            }
        }
            if(ss){
                return 1;
            }else {
                return 0;
            }
    }

    @Override
    public T6114 selectT6114(String bankCardNum) throws Throwable {
        bankCardNum = StringHelper.encode(bankCardNum);
        String sql = "SELECT F01, F02, F03, F04, F05, F06, F08 ,F10 FROM S61.T6114 WHERE T6114.F07 = ?";

        T6114 record = null;
        try (Connection connection = DbPoolConnection.getInstance().getConnection()){
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setString(1, bankCardNum);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6114();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                        record.F08 = T6114_F08.valueOf(resultSet.getString(7));
                        record.F10 = T6114_F10.valueOf(resultSet.getString(8));
                    }
                }
            }
        }

        return record;
    }

    /**
     *
     * 描述:查询用户的托管账号信息
     * 作者:wangshaohua
     * 创建时间：2015年3月13日
     * @param userId
     * @return
     * @throws Throwable
     */
    @Override
    public T6119 selectT6119(int userId) throws Throwable {
        T6119 recorde = null;
        try ( Connection connection = DbPoolConnection.getInstance().getConnection()){
            try (PreparedStatement pstmt =
                         connection.prepareStatement("SELECT T6119.F02,T6119.F03 FROM S61.T6119 WHERE T6119.F01= ?"))
            {
                pstmt.setInt(1, userId);
                try(ResultSet resultSet = pstmt.executeQuery()) {
                    if (resultSet.next())
                    {
                        recorde = new T6119();
                        recorde.F01 = userId;
                        recorde.F02 = resultSet.getInt(1);
                        recorde.F03 = resultSet.getString(2);
                    }
                }
            }
            return recorde;
        }

    }

    @Override
    public int updateT6114BankAdr(BankCardQuery query, int userId) throws SQLException {
        //更新实名认证信息
        try (Connection connection = DbPoolConnection.getInstance().getConnection()){
            try (PreparedStatement stats =connection.prepareStatement("UPDATE S61.T6114 SET F04=?,F05=? WHERE F02=? limit 1"))
            {
                stats.setString(1, query.getCity());
                stats.setString(2, query.getSubbranch());
                stats.setInt(3, userId);
                stats.execute();
            }
        }

        return 0;
    }

    @Override
    public T6110 getUserInfo(int userId) throws Throwable {
        try(Connection connection =DbPoolConnection.getInstance().getConnection()){
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

    @Override
    public boolean isSmrz(int uerId) throws Throwable {
        try(Connection connection = DbPoolConnection.getInstance().getConnection()){
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1")) {
                pstmt.setInt(1, uerId);
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
}
