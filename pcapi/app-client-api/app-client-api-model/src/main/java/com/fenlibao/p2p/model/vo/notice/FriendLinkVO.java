package com.fenlibao.p2p.model.vo.notice;

/** 友情链接Vo
 * Created by Administrator on 2016/11/9.
 */
public class FriendLinkVO {

    public Integer friendLinkId;

    public String friendLinkName;

    public String friendLinkUrl;

    public Long createTime;


    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getFriendLinkId() {
        return friendLinkId;
    }

    public void setFriendLinkId(Integer friendLinkId) {
        this.friendLinkId = friendLinkId;
    }

    public String getFriendLinkName() {
        return friendLinkName;
    }

    public void setFriendLinkName(String friendLinkName) {
        this.friendLinkName = friendLinkName;
    }

    public String getFriendLinkUrl() {
        return friendLinkUrl;
    }

    public void setFriendLinkUrl(String friendLinkUrl) {
        this.friendLinkUrl = friendLinkUrl;
    }
}
