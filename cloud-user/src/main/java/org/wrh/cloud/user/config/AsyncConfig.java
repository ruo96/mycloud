package org.wrh.cloud.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 异步线程池，本项目中用来监听处理事件的时候使用
 */
@Configuration
public class AsyncConfig{

    @Bean("asyncThreadPool")
    public Executor getAsyncExecutor(){
        System.out.println("asyncThreadPool init");
        Executor executor = new ThreadPoolExecutor(
                10,20,60L, TimeUnit.SECONDS
                ,new ArrayBlockingQueue<>(100),new MyThreadFactory());
        return executor;
    }

    class MyThreadFactory implements ThreadFactory {
        final AtomicInteger threadNumber = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r){
            Thread t = new Thread(r);
            t.setName("async-thread-"+threadNumber.getAndIncrement());
            t.setDaemon(true);
            return t;
        }
    }
}