package com.senpure.executor;

import io.netty.util.concurrent.FastThreadLocal;
import org.junit.Test;

/**
 * FastThreadLocalTest
 *
 * @author senpure
 * @time 2019-12-30 10:31:11
 */
public class FastThreadLocalTest {
    TaskLoopGroup taskLoop = new DefaultTaskLoopGroup(4);

    int count = 1;
    FastThreadLocal<Integer> valueLocal = new FastThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() throws Exception {
            return 5;
        }
    };
    FastThreadLocal<String> valueLocal2 = new FastThreadLocal<String>() {
        @Override
        protected String initialValue() throws Exception {
            return "aa";
        }
    };

    ThreadLocal<Long> valueLocal3 = ThreadLocal.withInitial(() -> 11L);


    @Test
    public void t() throws InterruptedException {

        taskLoop.execute(() -> {

            method();
            valueLocal.set(9);
            valueLocal2.set("bb");
            valueLocal3.set(22L);


            Runnable runnable = new TRunable(new Runnable() {
                @Override
                public void run() {
                    method();
                    taskLoop.execute(new Runnable() {
                        @Override
                        public void run() {
                            method();
                        }
                    });
                }
            });
          //  remove();
            taskLoop.execute(runnable);

        });
        Thread.sleep(5000);
    }

    private void method() {

        System.out.println("methodCount:" + count++);
        System.out.println(Thread.currentThread().getName() + "  " + valueLocal.get());
        System.out.println(Thread.currentThread().getName() + "  " + valueLocal2.get());
        System.out.println(Thread.currentThread().getName() + "  " + valueLocal3.get());
    }

    private void remove() {
        System.out.println(Thread.currentThread().getName() + "  remove");
        valueLocal.remove();
        valueLocal2.remove();
        valueLocal3.remove();

    }
}
