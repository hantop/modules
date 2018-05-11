package com.fenlibao.p2p.service.dbfile.impl;

import com.fenlibao.p2p.dao.dbfile.DBFileDao;
import com.fenlibao.p2p.model.form.dbfile.LatestDBFileForm;
import com.fenlibao.p2p.model.vo.dbfile.DBFileVO;
import com.fenlibao.p2p.service.dbfile.DBFileService;
import com.fenlibao.p2p.service.dbfile.IDBDataTable;
import com.fenlibao.p2p.util.file.HttpUpload;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.DecimalFormat;

@Service
public class DBFileServiceImpl implements DBFileService {
    private static final Logger logger = LogManager.getLogger(DBFileServiceImpl.class);

    @Resource
    private DBFileDao dbFileDao;

    // 文件服务器根路径URL
    private static final String serverPath;
    // 数据库文件存储的路径
    private static final String dbfilePath;
    // sqlite数据库文件临时存放路径
    private static final String tempDbfilePath;

    static {
        serverPath = Config.get("resources.server.path");
        dbfilePath = Config.get("dbfile.path");
        tempDbfilePath = Config.get("temp.dbfile.path");
    }

    @Override
    public String getLatestDBFileVersion(int type) {
        float dbFileMaxVersion = dbFileDao.getDBFileMaxVersion(type);
        return String.valueOf(dbFileMaxVersion);
    }

    @Override
    public DBFileVO getLatestDBFile(LatestDBFileForm latestDBFileForm) {
        DBFileVO dbFileVO = dbFileDao.getLatestDBFile(latestDBFileForm.getType());
        if (dbFileVO != null) {
            StringBuffer url = new StringBuffer(serverPath);
            url.append(dbfilePath);
            url.append(dbFileVO.getFileName());
            dbFileVO.setUrl(url.toString());
        }
        return dbFileVO;
    }

    @Override
    public void createDBFile(int type, IDBDataTable dbDataTable) throws SQLException, IOException, ClassNotFoundException {
        _createDBFile(dbDataTable, type);
    }

    private void _createDBFile(IDBDataTable dbDataTable, int type) throws IOException, SQLException, ClassNotFoundException {
        // 根据类型获取最大版本号
        String maxVersion = getDBFileMaxVersion(type);
        // 根据类型获取获取最后更新时间
        Timestamp dbFileLastChangetime = getDBFileLastChangetime(type);
        // 获取最后更新时间
        Timestamp dataRecordLastChangetime = getDataRecordLastChangetime(type);

        // 如果数据修改记录表的最后更新时间在db文件表最后更新记录之后
        if (dbFileLastChangetime == null
                || dataRecordLastChangetime == null
                || dataRecordLastChangetime.after(dbFileLastChangetime)) {
            String fileName = getFileName(maxVersion);
            // 目录
            File directory = new File(tempDbfilePath);
            // 创建文件夹
            directory.mkdirs();
            // 创建临时文件
            String prefix = "db_";
            String suffix = maxVersion + ".db";
            File tempFile = File.createTempFile(prefix, suffix, directory);
            String tempFilePath = tempFile.getPath();
            // 创建表和插入数据
            buildTableAndData(dbDataTable, maxVersion, tempFilePath);

            InputStream inputStream = new FileInputStream(tempFile);
            HttpUpload upload = new HttpUpload(serverPath, dbfilePath + fileName);
            upload.upladFile(inputStream);
            // 关闭流入流
            inputStream.close();
            // 删除临时文件
            tempFile.delete();

            // 新增数据库文件记录
            if(dataRecordLastChangetime == null) {
                dataRecordLastChangetime = new Timestamp(System.currentTimeMillis());
            }
            addDBFileRecord(fileName, maxVersion, dataRecordLastChangetime, type);
        }
    }

    /**
     * 创建表和插入数据
     *
     *
     * @param dbDataTable
     * @param maxVersion
     * @param tempFilePath
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private void buildTableAndData(IDBDataTable dbDataTable, String maxVersion, String tempFilePath) throws SQLException, ClassNotFoundException, IOException {
        Connection sqliteConn = getSqliteConnection(tempFilePath);
        Statement stmt = sqliteConn.createStatement();
        // 获取创建表语句
        String createTableSql = dbDataTable.getCreateTableSql();
        stmt.executeUpdate(createTableSql);
        // 获取插入语句
        String insertSql = dbDataTable.getInsertSql();
        PreparedStatement pstmt = sqliteConn.prepareStatement(insertSql);
        //把所有数据放入PreparedStatement中
        dbDataTable.addBatch(pstmt);
        // 执行操作
        pstmt.executeBatch();
        // 关闭资源
        pstmt.close();
        stmt.close();
        sqliteConn.close();
    }

    /**
     * 新增数据库文件记录
     *
     * @param fileName
     * @param maxVersion
     * @param dataRecordLastChangetime
     * @param type
     */
    public void addDBFileRecord(String fileName, String maxVersion, Timestamp dataRecordLastChangetime, int type) {
        DBFileVO dbFileVO = new DBFileVO();
        dbFileVO.setFileName(fileName);
        dbFileVO.setDbVersion(maxVersion);
        dbFileVO.setLastChangetime(dataRecordLastChangetime);
        dbFileVO.setType(type);
        dbFileDao.addDBFileRecord(dbFileVO);
    }

    /**
     * 创建sqlite文件和获取数据库连接
     *
     * @param fileName
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private Connection getSqliteConnection(String fileName) throws SQLException, ClassNotFoundException, IOException {
//        String dbFileName = buildDBFilename(maxVersion);
        // 文件如果存在则删除
        /*File file = new File(dbFileName);
        if(file.exists()) {
            file.delete();
        }*/
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:" + fileName, "root", "root");
    }

    private String buildDBFilename(String maxVersion) {
        StringBuffer dbFileName = new StringBuffer(serverPath);
        dbFileName.append(dbfilePath);
        dbFileName.append(getFileName(maxVersion));
        return dbFileName.toString();
    }

    private String getFileName(String maxVersion) {
        StringBuffer fileName = new StringBuffer("db_");
        fileName.append(maxVersion);
        fileName.append(".db");
        return fileName.toString();
    }

    /**
     * 根据类型获取最大版本号
     *
     * @param type
     * @return
     */
    private String getDBFileMaxVersion(int type) {
        Float dbFileMaxVersion = dbFileDao.getDBFileMaxVersion(type);
        if(dbFileMaxVersion == null) {
            dbFileMaxVersion = 0.0f;
        } else {
            dbFileMaxVersion += 0.1f;
        }
        if (dbFileMaxVersion < 1) {
            dbFileMaxVersion += 1;
        }
        DecimalFormat df = new DecimalFormat("#.0");
        return df.format(dbFileMaxVersion);
    }

    /**
     * 根据类型获取获取最后更新时间
     *
     * @param type
     * @return
     */
    private Timestamp getDBFileLastChangetime(int type) {
        return dbFileDao.getDBFileLastChangetime(type);
    }

    /**
     * 获取数据修改记录表的最后更新时间
     *
     * @return
     */
    private Timestamp getDataRecordLastChangetime(int type) {
        return dbFileDao.getDataRecordLastChangetime(type);
    }

}
