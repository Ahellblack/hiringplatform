package com.siti.common.exception;

import java.util.concurrent.ThreadFactory;

/*
 * 第二步：定义线程工厂
 * 线程工厂用来将任务附着给线程，并给该线程绑定一个异常处理器
 */
public class HanlderThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        //System.out.println(this + "creating new Thread");
        Thread t = new Thread(r);
        //System.out.println("created " + t);
        t.setUncaughtExceptionHandler(new MyUnchecckedExceptionhandler());//设定线程工厂的异常处理器
        //System.out.println("eh=" + t.getUncaughtExceptionHandler());
        return t;
    }
}
