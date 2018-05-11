package com.fenlibao.p2p.service.pay.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6141_F04;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenlibao.p2p.service.base.abstracts.BaseAbstractService;
import com.fenlibao.p2p.service.pay.IUserPayInfoService;

@Service
public class UserPayInfoServiceImpl extends BaseAbstractService implements IUserPayInfoService {

//	@Override
//	public int getAchieveVersion() {
//		return 0;
//	}

	@Override
	public T6141 selectT6141(int userId) throws Throwable {
		T6141 recorde = null;
		try (Connection conn = getConnection()) {
			try (PreparedStatement pstmt = conn.prepareStatement(
					"SELECT F01,F02,F04,F07 FROM S61.T6141 WHERE F01= ?  ")) {
				pstmt.setInt(1, userId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						recorde = new T6141();
						recorde.F01 = resultSet.getInt(1);
						recorde.F02 = resultSet.getString(2);
						recorde.F04 = T6141_F04.parse(resultSet.getString(3));
						recorde.F07 = StringHelper.decode(resultSet.getString(4));
					}
				}
			}
			return recorde;
		}
	}
	/**
	 * 获取风控参数
	 * @param F01 用户的id
	 * @return
	 * @throws Throwable
	 */
	@Override
	public String getRiskItem(int userId) throws Throwable{
		T6110 t6110=this.selectT6110(userId);
		Map<String,String> riskItem=new HashMap<String, String>();
		riskItem.put("frms_ware_category","2009");
		riskItem.put("user_info_mercht_userno", t6110.F01+"");//用户在商户系统中的标识
		riskItem.put("user_info_mercht_userlogin", t6110.F02);//用户在商户系统中的登陆名（手机号、邮箱等标识）
		riskItem.put("user_info_bind_phone", t6110.F04);//绑定手机hao
		riskItem.put("user_info_dt_register", DateParser.format(t6110.F09,"yyyyMMddHHmmss"));//注册时间
		riskItem.put("user_info_id_type", "0");
		if(t6110.F06==T6110_F06.ZRR){
			//自然人
			T6141 t6141=this.selectT6141(userId);
			riskItem.put("user_info_full_name", t6141.F02);//用户注册姓名
			riskItem.put("user_info_id_no", t6141.F07);//用户注册证件号码
		}else{
			//非自然人
			T6161 t6161=this.selectT6161(userId);
			riskItem.put("user_info_full_name", t6161.F11);//用户注册姓名
			riskItem.put("user_info_id_no", t6161.F13);//用户注册证件号码
		}
		
		riskItem.put("user_info_identify_state", "1");//是否实名认证
		riskItem.put("user_info_identify_type", "4");//实名认证方式
		ObjectMapper objectMapper=new ObjectMapper();
		return objectMapper.writeValueAsString(riskItem);
		//return URLEncoder.encode(json,"utf-8");
	}
	
	public T6110 selectT6110(int userId) throws Throwable {
		T6110 recorde=null;
		try (Connection conn = this.getConnection()) {
			try (
					PreparedStatement pstmt = conn
					.prepareStatement("SELECT F01,F02,F04,F05,F06,F09 FROM S61.T6110 WHERE F01= ?")) {
				pstmt.setInt(1,userId);
				try (ResultSet resultSet=pstmt.executeQuery()) {
					if(resultSet.next()){
						recorde=new T6110();
						recorde.F01=resultSet.getInt(1);
						recorde.F02=resultSet.getString(2);
						recorde.F04=resultSet.getString(3);
						recorde.F05=resultSet.getString(4);
						recorde.F06=T6110_F06.parse(resultSet.getString(5));
						recorde.F09=resultSet.getTimestamp(6);
					}
				}
			}
			return recorde;
		}
	}
	
	
	public T6161 selectT6161(int userId) throws Throwable {
		T6161 recorde = null;
		try (Connection conn = getConnection()) {
			try (PreparedStatement pstmt = conn.prepareStatement(
					"SELECT F01,F02,F03,F04,F07,F11,F13 FROM S61.T6161 WHERE F01= ?  ")) {
				pstmt.setInt(1, userId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						recorde = new T6161();
						recorde.F01 = resultSet.getInt(1);
						recorde.F02 = resultSet.getString(2);
						recorde.F03=resultSet.getString(3);
						recorde.F04 = resultSet.getString(4);
						recorde.F07 = resultSet.getInt(5);
						recorde.F11=resultSet.getString(6);
						recorde.F13=StringHelper.decode(resultSet.getString(7));
					}
				}
			}
			return recorde;
		}
	}
	@Override
	public String getAccountName(int userId) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F02 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, userId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getString(1);
					}
				}
			}
		}
		return null;
	}
	
}
