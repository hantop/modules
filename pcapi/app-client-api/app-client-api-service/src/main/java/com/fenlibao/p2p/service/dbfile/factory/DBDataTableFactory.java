package com.fenlibao.p2p.service.dbfile.factory;

import com.fenlibao.p2p.service.dbfile.IDBDataTable;
import com.fenlibao.p2p.service.dbfile.impl.DistrictDBDataTable;

/**
 * DB数据表工厂
 * Created by chenzhixuan on 2015/9/11.
 */
public class DBDataTableFactory {

    /**
     * 根据类型创建DB数据表实现类实例
     * @param type
     * @return
     */
    public static IDBDataTable createDBDataTable(int type) {
        IDBDataTable idbDataTable = null;
        switch (type) {
            // 行政区划
            case 1:
                idbDataTable = new DistrictDBDataTable();
                break;
        }
        return idbDataTable;
    }

}
