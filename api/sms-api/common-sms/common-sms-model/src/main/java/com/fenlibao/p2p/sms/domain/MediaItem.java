package com.fenlibao.p2p.sms.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/28.
 */
public class MediaItem implements Serializable {
    private Long id;
    private Long createTime;//创建时间
    private int mediaType = 0;
    private String meta;
    private byte[] data;


}
