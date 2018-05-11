package com.fenlibao.p2p.model.entity.notice;

import java.util.Date;

/**友情链接
 * Created by Administrator on 2016/11/9.
 */

public class FriendLink {
    public Integer fkId;//友情链接id

    public String name;//友情链接名称
    
    public String url;//友情链接地址

    public Date createTime;//创建时间

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getFkId() {
        return fkId;
    }

    public void setFkId(Integer fkId) {
        this.fkId = fkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
