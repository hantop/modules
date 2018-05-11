package com.fenlibao.p2p.controller.noversion.dbfile;

import com.fenlibao.p2p.model.form.dbfile.LatestDBFileForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.dbfile.DBFileVO;
import com.fenlibao.p2p.service.dbfile.DBFileService;
import com.fenlibao.p2p.service.dbfile.IDBDataTable;
import com.fenlibao.p2p.service.dbfile.factory.DBDataTableFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * db文件
 * Created by chenzhixuan on 2015/9/8.
 */
@RestController
@RequestMapping("dbfile")
public class DBFileController {
    private static final Logger logger = LogManager.getLogger(DBFileController.class);
    public static final String DB_VERSION_KEY = "dbVersion";

    @Resource
    private DBFileService dbFileService;

    /**
     * 获取最新的db文件版本号
     *
     * @return
     */
    @RequestMapping(value = "latest/version", method = RequestMethod.GET)
    HttpResponse getLatestDBFileVersion(int type) {
        HttpResponse response = new HttpResponse();
        String latestDBFileVersion = dbFileService.getLatestDBFileVersion(type);
        Map<String, Object> map = new HashMap<>();
        map.put(DB_VERSION_KEY, latestDBFileVersion);
        response.setData(map);
        return response;
    }

    /**
     * 创建sqlite数据库文件
     *
     * @param type
     * @return
     */
    @RequestMapping(value = "latest/creater", method = RequestMethod.GET)
    HttpResponse createDBFile(int type) {
        HttpResponse response = new HttpResponse();
        try {
            // 根据类型创建DB数据表实现类实例
            IDBDataTable dbDataTable = DBDataTableFactory.createDBDataTable(type);
            if (dbDataTable == null) {
                response.setCodeMessage(ResponseCode.OTHER_DBFILE_TYPE_NO_EXISTS);
            } else {
                dbFileService.createDBFile(type, dbDataTable);
            }
        } catch (Exception e) {
            logger.error("save sqliteDB file error: " + e.getMessage(), e);
            response.setCodeMessage(ResponseCode.FAILURE);
        }
        return response;
    }

    /**
     * 获取最新的db文件
     *
     * @param latestDBFileForm
     * @return
     */
    @RequestMapping(value = "latest/file", method = RequestMethod.GET)
    HttpResponse latestDBFile(
            LatestDBFileForm latestDBFileForm) {
        HttpResponse response = new HttpResponse();
        DBFileVO dbFileVO = dbFileService.getLatestDBFile(latestDBFileForm);

        Map<String, Object> map = new HashMap<>();
        String dbVersion = null;
        String url = null;
        if (dbFileVO != null) {
            dbVersion = dbFileVO.getDbVersion();
            url = dbFileVO.getUrl();
        }
        map.put(DB_VERSION_KEY, dbVersion);
        map.put("url", url);
        response.setData(map);
        return response;
    }

}
