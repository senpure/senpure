package com.senpure.executor;


import io.netty.util.concurrent.Future;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.concurrent.TimeUnit;


/**
 * SingleThreadTaskLoopTest
 *
 * @author senpure
 * @time 2019-12-24 13:59:46
 */
public class SingleThreadTaskLoopTest {

    TaskLoopGroup taskLoop = new DefaultTaskLoopGroup(4);

    @Test
    public void submitMayNow() throws InterruptedException {
        taskLoop.execute(new Runnable() {
            @Override
            public void run() {

                Future<String> future = taskLoop.next().submitMayNow(() -> {
                    int i = 5 / 0;
                    return "hello";
                });


                System.out.println("---" + future.isSuccess() + "," + future.isDone());


                System.out.println("out over");
                taskLoop.next().submit(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("5466");
                    }
                });
            }

        });


    }


    public void s() {
        taskLoop.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {

                System.out.println("------------"+Thread.currentThread().getName());
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    @AfterClass
    public static void after() throws InterruptedException {
        //Thread.sleep(50000);
    }
}