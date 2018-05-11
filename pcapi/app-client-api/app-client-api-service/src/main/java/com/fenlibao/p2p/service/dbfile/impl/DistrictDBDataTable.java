package com.fenlibao.p2p.service.dbfile.impl;

import com.fenlibao.p2p.model.entity.district.District;
import com.fenlibao.p2p.service.dbfile.IDBDataTable;
import com.fenlibao.p2p.service.base.db.DbPoolConnection;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 行政区划
 * Created by chenzhixuan on 2015/9/9.
 */
@Service
public class DistrictDBDataTable implements IDBDataTable {

    protected Connection getConnection() throws SQLException {
        return DbPoolConnection.getInstance().getConnection();
    }

    @Override
    public String getCreateTableSql() {
        return "CREATE TABLE [DISTRICT] ([ID] INTEGER PRIMARY KEY AUTOINCREMENT, [CODE] INTEGER, [PROVINCE_ID] INTEGER, [CITY_ID] INTEGER, [COUNTY_ID] INTEGER, [NAME] VARCHAR(15), [LEVEL] VARCHAR(5), [SPELL] VARCHAR(15));";
    }

    @Override
    public String getInsertSql() {
        return "INSERT INTO DISTRICT(CODE, PROVINCE_ID, CITY_ID, COUNTY_ID, NAME, LEVEL, SPELL) VALUES(?, ?, ?, ?, ?, ?, ?);";
    }

    @Override
    public void addBatch(PreparedStatement pstmt) throws SQLException {
        List<District> districts = getDistricts();
        for(District district : districts) {
            int code = district.getId();
            pstmt.setInt(1, code);
            pstmt.setInt(2, district.getProvinceId());
            pstmt.setInt(3, district.getCityId());
            pstmt.setInt(4, district.getCountyId());
            pstmt.setString(5, district.getName());
            pstmt.setString(6, district.getLevel());
            pstmt.setString(7, district.getSpell());
            pstmt.addBatch();
        }
    }

    private List<District> getDistricts() throws SQLException {
        List<District> districts = new ArrayList<>();
        // 查询所有记录
        String selectAllSql = "SELECT code, province_id, city_id, name, level, spell FROM flb.t_district WHERE status = 'QY' ORDER BY CONVERT(name USING gbk) COLLATE gbk_chinese_ci";
        try (Connection connection = getConnection()) {
            try (PreparedStatement pstmt = connection.prepareStatement(selectAllSql)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    District district;
                    while (rs.next()) {
                        district = new District();
                        district.setId(rs.getInt(1));
                        district.setProvinceId(rs.getInt(2));
                        district.setCityId(rs.getInt(3));
                        district.setName(rs.getString(4));
                        district.setLevel(rs.getString(5));
                        district.setSpell(rs.getString(6));
                        districts.add(district);
                    }
                }
            }
        }
        return districts;
    }
}
