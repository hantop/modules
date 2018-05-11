package com.fenlibao.model.pms.da.statistics.invest;

import java.io.Serializable;

/**
 * Created by Louis Wang on 2015/11/10.
 */

public class ClientTypeInfo implements Serializable {

    private Integer id; //自增类型id

    private String clientName;  //类型名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
