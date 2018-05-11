package com.fenlibao.p2p.service.district.impl;

import com.dimeng.p2p.S50.enums.T5019_F11;
import com.dimeng.p2p.S50.enums.T5019_F13;
import com.fenlibao.p2p.dao.district.IDistrictDao;
import com.fenlibao.p2p.service.base.db.DbPoolConnection;
import com.fenlibao.p2p.service.district.DistrictService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 省份城市
 * Created by chenzhixuan on 2015/8/24.
 */
@Service
public class DistrictServiceImpl implements DistrictService {
	
	@Resource
	private IDistrictDao districtDao;

    protected Connection getConnection() throws SQLException {
        return DbPoolConnection.getInstance().getConnection();
    }

    @Override
    public List<Map<String, String>> getAllProivce() throws Throwable{
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map;

        try (Connection connection = getConnection()) {
//            try (PreparedStatement pstmt = connection
//                    .prepareStatement("SELECT F01, F05 FROM s50.T5019 WHERE T5019.F11 = ? AND F13=?")) {
            String sql = "SELECT code, name FROM flb.t_district WHERE level = ? AND status = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, T5019_F11.SHENG.name());
                pstmt.setString(2, T5019_F13.QY.name());
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        map = new HashMap<>();
                        map.put("id", String.valueOf(rs.getInt(1)));
                        map.put("province", rs.getString(2));
                        list.add(map);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<Map<String, String>> getCity(int provinceId) throws Throwable {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map;
//        String sql = "SELECT F01,F05 FROM s50.T5019 WHERE F02=? AND F04=0 AND F11=? AND F13=?";
        String sql = "SELECT code, name FROM flb.t_district WHERE province_id = ? AND level = ? AND status = ?";
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, provinceId / 10000);
                ps.setString(2, T5019_F11.SHI.name());
                ps.setString(3, T5019_F13.QY.name());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        map = new HashMap<>();
                        map.put("id", String.valueOf(rs.getInt(1)));
                        map.put("city", rs.getString(2));
                        list.add(map);
                    }
                }
            }
        }
        return list;
    }

	@Override
	public String getProvinceCodeByCityCode(String cityCode) {
		return this.districtDao.getProvinceCodeByCityCode(cityCode);
	}


//    @Override
//    public List<Map<String, String>> getCounty(int cityId) throws Throwable {
//        List<Map<String, String>> list = new ArrayList<>();
//        Map<String, String> map;
//        String sql = "SELECT F01,F05 FROM s50.T5019 WHERE F02=? AND F03=? AND F04<>0 AND F11=? AND F13=?";
//        try (Connection connection = getConnection()) {
//            try (PreparedStatement ps = connection.prepareStatement(sql)) {
//                ps.setInt(1, cityId / 10000);
//                ps.setInt(2, (cityId % 10000) / 100);
//                ps.setString(3, T5019_F11.XIAN.name());
//                ps.setString(4, T5019_F13.QY.name());
//                try (ResultSet rs = ps.executeQuery()) {
//                    while (rs.next()) {
//                        map = new HashMap<>();
//                        map.put("id", rs.getInt(1) + "");
//                        map.put("county", rs.getString(2));
//                        list.add(map);
//                    }
//                }
//            }
//        }
//        return list;
//    }
//
//    @Override
//    public CountyVO getCounty(String type, int countyId) throws Throwable {
//        CountyVO countyVO = null;
//        String columnName = null;
//        if(type.equals("1")) {
//            columnName = "F14";
//        }
//        String sql = "SELECT F01,F05," + columnName + " FROM s50.T5019 WHERE F01=? AND F04<>0 AND F11=? AND F13=?";
//        try (Connection connection = getConnection()) {
//            try (PreparedStatement ps = connection.prepareStatement(sql)) {
//                ps.setInt(1, countyId);
//                ps.setString(2, T5019_F11.XIAN.name());
//                ps.setString(3, T5019_F13.QY.name());
//                try (ResultSet rs = ps.executeQuery()) {
//                    if (rs.next()) {
//                        countyVO = new CountyVO();
//                        countyVO.setCode(rs.getInt(1));
//                        countyVO.setCountyName(rs.getString(2));
//                        if(type.equals("1")) {
//                            countyVO.setLlCode(rs.getInt(3));
//                        }
//                    }
//                }
//            }
//        }
//        return countyVO;
//    }

}
