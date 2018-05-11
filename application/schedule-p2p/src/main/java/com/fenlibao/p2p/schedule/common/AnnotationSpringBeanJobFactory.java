/*
 * Copyright (c) 2015 by FENLIBAO NETWORK TECHNOLOGY CO.
 *             All rights reserved
 */
package com.fenlibao.p2p.schedule.common;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/** 
 * @author <a href="mailto:toby.xiong2@qq.com">Toby</a>
 * @Data 2015年10月27日
 * @Version 1.0.0
 */

public final class AnnotationSpringBeanJobFactory extends SpringBeanJobFactory {
	@Autowired
	private transient AutowireCapableBeanFactory beanFactory;

	@Override
	protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
		final Object job = super.createJobInstance(bundle);
		beanFactory.autowireBean(job);
		return job;
	}
}