package com.fenlibao.p2p.schedule.common;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public enum ThreadPoolHelper {
	INSTANCE;

	private ThreadPoolExecutor taskExecutor;//定时器
	
	ThreadPoolHelper(){
		System.out.println("创建ThreadPool");
		taskExecutor = new ThreadPoolExecutor(30, 80, 30, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
	}
	public ThreadPoolExecutor getInstance(){
		System.out.println("获取ThreadPool实例");
		return taskExecutor;
	}
	
}
