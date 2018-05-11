package com.fenlibao.p2p.service.thirdparty;

import java.util.concurrent.*;

/**
 * AbstractForThreadPoolExecutor
 */
public abstract class AbstractForThreadPoolExecutor implements Callable {
    protected Object object;
    protected final ThreadPoolExecutor executor =
            new ThreadPoolExecutor(10, 50, 30,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    /**
     * 一个对象一个线程开启
     */
    public Future objectExe(Object object, Callable callable) {
        this.object = object;
        Future result = executor.submit(callable);
        return result;
    }

    @Override
    public Object call() throws Exception{
        Object result = doTracsation(object);
        return result;
    }

    abstract public Object doTracsation(Object o) throws Exception;
}
