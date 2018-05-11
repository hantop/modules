package com.fenlibao.p2p.service.thirdparty;

import com.fenlibao.p2p.model.entity.bid.ConsumeBid;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * BestSignService
 *
 * kris [fengjunda@fenlibao.com]
 * 2017/7/10 16:38
 */

public abstract class BestSignService implements Runnable {
    protected List<ConsumeBid> list;
    protected final ThreadPoolExecutor executor =
            new ThreadPoolExecutor(10, 50, 30,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    /**
     * 批量操作
     */
    public void batchExe(List<ConsumeBid> list, Runnable runner) {
        this.list = list;
        executor.submit(runner);
    }

    @Override
    public void run() {
        if (list == null || list.size() == 0) {
            return;
        }
        for (ConsumeBid o:list) {
            sign(o);
        }
    }

    public void sign(ConsumeBid o){    }
}
