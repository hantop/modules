package com.fenlibao.service.weixin.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/7/24.
 */
public class Button implements Serializable {

    @JsonProperty(value = "open")
    private boolean open = true;

    @JsonProperty(value = "name")
    private String name;    //菜单标题，不超过16个字节，子菜单不超过40个字节

    @JsonProperty(value = "type")
    private String type;  //click等点击类型必须	菜单KEY值，用于消息接口推送，不超过128字节

    @JsonProperty(value = "key")
    private String key;//菜单KEY值，用于消息接口推送，不超过128字节

    @JsonProperty(value = "media_id")
    private String mediaId;// media_id类型和view_limited类型必须	调用新增永久素材接口返回的合法media_id

    @JsonProperty(value = "url")
    private String url;//view类型必须	网页链接，用户点击菜单可打开链接，不超过256字节

    @JsonProperty(value = "sub_button")
    private List<Button> subButton;//二级菜单数组，个数应为1~5个

    private int sort;

    private String content;//回复消息内容

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Button> getSubButton() {
        return subButton;
    }

    public void setSubButton(List<Button> subButton) {
        this.subButton = subButton;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
