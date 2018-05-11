package com.fenlibao.p2p.dao.dbfile.impl;

import com.fenlibao.p2p.dao.dbfile.DBFileDao;
import com.fenlibao.p2p.model.vo.dbfile.DBFileVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Timestamp;

@Repository
public class DBFileDaoImpl implements DBFileDao {
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "DBFileMapper.";

    @Override
    public DBFileVO getLatestDBFile(int type) {
        return sqlSession.selectOne(MAPPER+"getLatestDBFile", type);
    }

    @Override
    public Float getDBFileMaxVersion(int type) {
        return sqlSession.selectOne(MAPPER+"getDBFileMaxVersion", type);
    }

    @Override
    public Timestamp getDBFileLastChangetime(int type) {
        return sqlSession.selectOne(MAPPER+"getDBFileLastChangetime", type);
    }

    @Override
    public Timestamp getDataRecordLastChangetime(int type) {
        return sqlSession.selectOne(MAPPER+"getDataRecordLastChangetime", type);
    }

    @Override
    public int addDBFileRecord(DBFileVO dbFileVO) {
        return sqlSession.insert(MAPPER+"addDBFileRecord", dbFileVO);
    }
}
