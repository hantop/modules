package com.fenlibao.p2p.service.dbfile;

import com.fenlibao.p2p.model.form.dbfile.LatestDBFileForm;
import com.fenlibao.p2p.model.vo.dbfile.DBFileVO;

import java.io.IOException;
import java.sql.SQLException;

/**
 * db文件
 * Created by chenzhixuan on 2015/9/8.
 */
public interface DBFileService {

    /**
     * 获取最新的db文件版本号
     * @param type
     * @return
     */
    String getLatestDBFileVersion(int type);

    /**
     * 获取最新的db文件
     * @param latestDBFileForm
     * @return
     */
    DBFileVO getLatestDBFile(LatestDBFileForm latestDBFileForm);
    /**
     * 创建数据库文件
     * @param type
     * @param dbDataTable
     */
    void createDBFile(int type, IDBDataTable dbDataTable) throws SQLException, IOException, ClassNotFoundException;

}
