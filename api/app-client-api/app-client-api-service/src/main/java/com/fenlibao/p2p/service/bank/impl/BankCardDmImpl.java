package com.fenlibao.p2p.service.bank.impl;

import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.p2p.S61.enums.T6114_F10;
import com.dimeng.util.StringHelper;
import com.fenlibao.p2p.model.entity.BankCard;
import com.fenlibao.p2p.model.form.BankCardQuery;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.vo.BankCardVO;
import com.fenlibao.p2p.service.bank.BankCardDmService;
import com.fenlibao.p2p.service.bank.BankService;
import com.fenlibao.p2p.service.base.db.DbPoolConnection;
import com.fenlibao.p2p.service.bid.impl.BaseServiceDmImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LouisWang on 2015/8/17.
 */
@Service
public class BankCardDmImpl extends BaseServiceDmImpl implements BankCardDmService {
	
	@Resource
	private BankService bankService;
	
    @Override
    public BankCard[] getBankCars(String userId,String status) throws Throwable {
        //   int acount = serviceResource.getSession().getAccountId();
        int acount = 0;
        if(!StringUtils.isEmpty(userId)){
            acount = Integer.parseInt(userId);
        }

        if (StringHelper.isEmpty(status) || acount <= 0) {
            throw new Exception("参数错误");
        }

        List<BankCard> list = new ArrayList<BankCard>();
        try(Connection connection = DbPoolConnection.getInstance().getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement("SELECT T6114.F01 AS F01, T6114.F02 AS F02, T6114.F03 AS F03, T6114.F04 AS F04, T6114.F05 AS F05, T6114.F06 AS F06, T6114.F08 AS F07, T5020.F02 AS F08 FROM S61.T6114 INNER JOIN S50.T5020 ON T6114.F03 = T5020.F01 WHERE T6114.F02 = ? AND T6114.F08 = ?"))
            {
                statement.setInt(1, acount);
                statement.setString(2, status);
                try (ResultSet rs = statement.executeQuery())
                {
                    if (rs.next())
                    {
                        /*BankCard b = new BankCard();
                        b.id = rs.getInt(1);
                        b.acount = rs.getInt(2);
                        b.BankID = rs.getInt(3);
                        b.City = rs.getString(4);
                        b.BankKhhName = rs.getString(5);
                        b.BankNumber = rs.getString(6);
                        b.status = rs.getString(7);
                        b.Bankname = rs.getString(8);
                        list.add(b);*/
                    }
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return list.toArray(new BankCard[list.size()]);
    }

    @Override
    public BankCardVO getBankCar(String cardnumber) throws Throwable {
        if (StringHelper.isEmpty(cardnumber)) {
            throw new ParameterException("参数错误");
        }
        BankCardVO b = null;
        try(Connection connection = DbPoolConnection.getInstance().getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F08 FROM S61.T6114 WHERE T6114.F07 = ?"))
            {
                statement.setString(1, StringHelper.encode(cardnumber));
                try (ResultSet rs = statement.executeQuery())
                {
                    if (rs.next())
                    {
                        b = new BankCardVO();
                        b.id = rs.getInt(1);
                        b.acount = rs.getInt(2);
                        b.bankID = rs.getInt(3);
                        b.city = rs.getString(4);
                        b.bankKhhName = rs.getString(5);
                        b.bankNumber = rs.getString(6);
                        b.status = rs.getString(7);
                    }
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return b;
    }

    @Override
    public int AddBankCar(BankCardQuery query) throws Throwable {
        if (query == null) {
            throw new ParameterException("参数错误");
        }
            String bankNum = query.getBankNumber();
            StringBuilder sb = new StringBuilder();
            sb.append(bankNum.substring(0, 3));
            sb.append("*************");
            sb.append(bankNum.substring(bankNum.length() - 4, bankNum.length()));
            try(Connection connection = DbPoolConnection.getInstance().getConnection()){
                try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S61.T6114 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?,F10 = ?,F11 = ?,F12 = ?",
                        PreparedStatement.RETURN_GENERATED_KEYS )) {
                    pstmt.setInt(1,query.getAcount());
                    pstmt.setInt(2, query.getBankId());
                    pstmt.setString(3, query.getCity());
                    pstmt.setString(4,query.getSubbranch());
                    pstmt.setString(5,sb.toString());
                    pstmt.setString(6,StringHelper.encode(query.getBankNumber()));
                    pstmt.setString(7, T6114_F08.TY.name());
                    pstmt.setTimestamp(8, getCurrentTimestamp(connection));
                    pstmt.setString(9, T6114_F10.TG.name());
                    pstmt.setString(10, query.getUserName());
                    pstmt.setInt(11, query.getType());
                    pstmt.execute();

                    try (ResultSet resultSet = pstmt.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            return resultSet.getInt(1);
                        }
                        return 0;
                    }
                }
            }
    }

    @Override
    public int updateT6114Bank(int bankCode, int userId) throws SQLException {
        int flag = 0;
        //更新银行信息
        try(Connection connection = DbPoolConnection.getInstance().getConnection()){
            try (PreparedStatement stats =
                         connection.prepareStatement("UPDATE S61.T6114 SET F03=?,F08=? WHERE F02=? limit 1"))
            {
                stats.setInt(1, bankCode);
                stats.setString(2, T6114_F08.QY.name());
                stats.setInt(3, userId);
                flag = stats.executeUpdate();
            }
        }
        return flag;
    }

    @Override
    public void update(int id, BankCardQuery query) throws Throwable {
        if (query == null || id <= 0) {
            throw new ParameterException("参数错误");
        }
        try(Connection connection = DbPoolConnection.getInstance().getConnection()){
            try (PreparedStatement stats =
                         connection.prepareStatement("UPDATE S61.T6114 SET F03 = ?, F04 = ?, F05 = ?, F08 = ?, F11 = ?, F12 = ? WHERE F01 = ?"))
            {
                stats.setInt(1, query.getBankId());
                stats.setString(2, query.getCity());
                stats.setString(3, query.getSubbranch());
                stats.setString(4, query.getStatus());
                stats.setString(5, query.getUserName());
                stats.setInt(6, query.getType());
                stats.setInt(7, id);
                stats.executeUpdate();
            }
        }
    }

    @Override
    public void updateTY(int id, BankCardQuery query, int userId) throws Throwable {
        if (query == null || id <= 0) {
            throw new ParameterException("参数错误");
        }
        try(Connection connection = DbPoolConnection.getInstance().getConnection()){
            try (PreparedStatement stats =
                         connection.prepareStatement("UPDATE S61.T6114 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F08 = ?, F11 = ?, F12 = ? WHERE F01 = ?"))
            {
                stats.setInt(1, userId);
                stats.setInt(2, query.getBankId());
                stats.setString(3, query.getCity());
                stats.setString(4, query.getSubbranch());
                stats.setString(5, query.getStatus());
                stats.setString(6, query.getUserName());
                stats.setInt(7, query.getType());
                stats.setInt(8, id);
                stats.executeUpdate();
            }
        }


    }

	@Override
	public int bindBankcard(int userId, String cardNo) throws Throwable {
		BankCardQuery bankCarInfo = new BankCardQuery();
		bankCarInfo.setBankNumber(cardNo);
		bankCarInfo.setType(InterfaceConst.ACCOUNT_NAME_TYPE_PERSON); //1个人 2企业
		bankCarInfo.setAcount(userId);
		BankCardVO vo = bankService.checkBankCardInfo(userId);
		if (vo == null) {
			return this.AddBankCar(bankCarInfo);
		} 
		vo.setBankNumber(cardNo);
		return this.updateBankCardNo(vo);
	}
	
	/**
	 * 更新银行卡号
	 * @param vo
	 * @return
	 * @throws Throwable 
	 */
	private int updateBankCardNo(BankCardVO vo) throws Throwable {
		StringBuilder sb = new StringBuilder();
		String cardNo = vo.getBankNumber();
		sb.append(cardNo.substring(0, 3));
		sb.append("*************");
		sb.append(cardNo.substring(cardNo.length() - 4, cardNo.length()));
		vo.setBankNumber(StringHelper.encode(cardNo));
		vo.setBankNoStar(sb.toString());
		return bankService.updateBankCardInfo(vo);
	}
}
