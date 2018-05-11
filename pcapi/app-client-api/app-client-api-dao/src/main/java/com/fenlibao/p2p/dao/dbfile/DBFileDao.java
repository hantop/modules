package com.fenlibao.p2p.dao.dbfile;

import com.fenlibao.p2p.model.vo.dbfile.DBFileVO;

import java.sql.Timestamp;

/**
 *
 * Created by chenzhixuan on 2015/9/8.
 */
public interface DBFileDao {

    /**
     * 获取最新DB文件
     *
     * @param type@return
     */
    DBFileVO getLatestDBFile(int type);

    /**
     * 根据类型获取最大版本号
     * @param type
     * @return
     */
    Float getDBFileMaxVersion(int type);
    /**
     * 根据类型获取最后更新时间
     * @param type
     * @return
     */
    Timestamp getDBFileLastChangetime(int type);

    /**
     * 获取数据修改记录表的最后更新时间
     * @param type
     * @return
     */
    Timestamp getDataRecordLastChangetime(int type);

    /**
     * 新增数据库文件记录
     *
     * @param dbFileVO
     * @return
     */
    int addDBFileRecord(DBFileVO dbFileVO);



}
