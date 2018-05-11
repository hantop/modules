package com.fenlibao.p2p.schedule.task;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zeronx on 2018/3/2 14:32.
 * @version 1.0
 * 当提交的任务大于 workQueue + maxNumPoolSize 且使用的是有限阻塞队列时，新提交的任务会交给RejectedExecutionHandler来处理
 * 说明：任务拒绝策略内部有提供四种，这里自定义。目的让每一个提交的任务都能执行。
 */
public class MyRejectedExecutionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            // 将新提交的任务以阻塞的形式 重新提交到队列。
            executor.getQueue().put(r);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
