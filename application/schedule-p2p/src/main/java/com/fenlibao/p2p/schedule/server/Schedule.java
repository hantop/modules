/*
 * Copyright (c) 2015 by FENLIBAO NETWORK TECHNOLOGY CO.
 *             All rights reserved
 */
package com.fenlibao.p2p.schedule.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import static com.fenlibao.p2p.util.api.load.ApiUtilConfig.loadUtilConfig;
import static com.fenlibao.p2p.util.loader.Config.load;
import static com.fenlibao.p2p.util.loader.Payment.payment;
import static com.fenlibao.p2p.util.loader.Sender.sender;

/** 
 * @author <a href="mailto:toby.xiong2@qq.com">Toby</a>
 * @Data 2015年10月27日
 * @Version 1.0.0
 */

public class Schedule {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		new ClassPathXmlApplicationContext("applicationContext.xml");
		
		load(); //load config
		loadUtilConfig();
		sender();

		payment(); //load payment
		
		System.out.println("schedule-p2p was started");
		
	}
}
