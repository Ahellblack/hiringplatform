package com.siti.common.exception;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hongtu on 2018/12/1.
 * <p>
 * 第一步：定义符合线程异常处理器规范的“异常处理器”
 * 实现Thread.UncaughtExceptionHandler规范
 */
public class MyUnchecckedExceptionhandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("捕获异常处理方法：" + e);
    }


    public static void main(String[] args) {

        Long time1 = System.currentTimeMillis();
        Thread t1 = new Thread(() -> {
            try {
                System.out.println("yes_15_sys_hello?");
            } catch (Exception e) {
                try {
                    Thread.sleep(15 * 1000L);
                    System.out.println("15_sys_hello?");
                } catch (Exception ex) {
                    System.out.println("sys_hello?");
                }
            }
        });
        t1.start();
        Thread t2 = new Thread(() -> {
            try {
                throw new RuntimeException("hello!");
            } catch (Exception e) {
                try {
                    Thread.sleep(15 * 1000L);
                    System.out.println("15_sys_hello!");
                } catch (Exception ex) {
                    System.out.println("sys_hello!");
                }
            }
        });
        t2.start();
        Thread t3 = new Thread(() -> {
            try {
                System.out.println("yes_15_sys_hello.");
            } catch (Exception e) {
                try {
                    Thread.sleep(15 * 1000L);
                    System.out.println("15_sys_hello.");
                } catch (Exception ex) {
                    System.out.println("sys_hello.");
                }
            }
        });
        t3.start();

        ExecutorService exec = Executors.newCachedThreadPool(new HanlderThreadFactory());
        exec.execute(t1);
        exec.execute(t2);
        exec.execute(t3);

        while (true) {
            Long time2 = System.currentTimeMillis();
            if(time2-time1>10000){
                System.out.println("retry, please!");
                return;
            }
            if (t1.getState().equals(Thread.State.TERMINATED)
                    && t2.getState().equals(Thread.State.TERMINATED)
                    && t3.getState().equals(Thread.State.TERMINATED)) {

                System.out.println("file size: " + 100 + "M");
                return;
            }
        }
    }

}
