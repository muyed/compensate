package com.muye.compensate.work;

import com.muye.compensate.constant.CompensateConstant;

import java.util.concurrent.ThreadFactory;

/**
 * created by dahan at 2018/8/2
 */
public class CompensateThreadFactory implements ThreadFactory {

    private final ThreadGroup group;

    public CompensateThreadFactory(){
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(group, r, CompensateConstant.COMPENSATE_THREAD_NAME, 0);

        return thread;
    }
}
