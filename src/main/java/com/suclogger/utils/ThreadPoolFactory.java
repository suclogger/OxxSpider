package com.suclogger.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by suclogger on 2017/4/6.
 */
public class ThreadPoolFactory {
    private static volatile ExecutorService thredPool = null;
    private static final int CORE_SIZE = 1;
    private static final int MAX_SIZE = 100;
    private static final int KEEP_ALIVE_TIME = 100;

    public static ExecutorService getThreadPool() {
        if (null != thredPool) {
            return thredPool;
        }
        synchronized (ThreadPoolFactory.class) {
            if (null != thredPool) {
                return thredPool;
            }
            thredPool = new ThreadPoolExecutor(CORE_SIZE, MAX_SIZE, KEEP_ALIVE_TIME, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
            return thredPool;
        }
    }
}
