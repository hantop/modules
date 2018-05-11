package com.fenlibao.p2p.sms.config.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/28.
 */
public class Account implements Serializable{

    private Long id;
    private Long createTime;//创建时间
    private String name;
    private String password;
    private String from;
}
