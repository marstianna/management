package com.dmall.managed.core.server.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zoupeng on 16/3/29.
 */
public class BatchExecuteService{
    private final Executor executor;
    private final AbstractExecutorService aes;
    private final Cache<String,Future<Object>> completionMap = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .build();;

    /** Lock held by take, poll, etc */
    private final ReentrantLock takeLock = new ReentrantLock();

    /** Wait queue for waiting takes */
    private final Condition notEmpty = takeLock.newCondition();


    private class QueueingFuture extends FutureTask<Void> {
        QueueingFuture(String qualifier,RunnableFuture<Object> task) {
            super(task, null);
            this.task = task;
            this.qualifier = qualifier;
        }
        protected void done() {
            completionMap.put(qualifier,task);
            signalNotEmpty();
        }
        private final Future<Object> task;
        private final String qualifier;
    }

    private void signalNotEmpty() {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
    }

    private RunnableFuture<Object> newTaskFor(Callable<Object> task) {
        return new FutureTask<Object>(task);
    }

    private RunnableFuture<Object> newTaskFor(Runnable task, Object result) {
        return new FutureTask<Object>(task, result);
    }

    public BatchExecuteService(Executor executor) {
        if (executor == null)
            throw new NullPointerException();
        this.executor = executor;
        this.aes = (executor instanceof AbstractExecutorService) ?
                (AbstractExecutorService) executor : null;
    }

    public Future<Object> submit(String qualifier,Callable<Object> task) {
        if (task == null) throw new NullPointerException();
        RunnableFuture<Object> f = newTaskFor(task);
        executor.execute(new QueueingFuture(qualifier,f));
        return f;
    }

    public Future<Object> submit(String qualifier,Runnable task, Object result) {
        if (task == null) throw new NullPointerException();
        RunnableFuture<Object> f = newTaskFor(task, result);
        executor.execute(new QueueingFuture(qualifier,f));
        return f;
    }

    public Object take(String qualifier) throws InterruptedException, ExecutionException {
        Future<Object> result;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        try{
            while(completionMap.getIfPresent(qualifier) == null){
                notEmpty.await();
            }
            result = completionMap.getIfPresent(qualifier);
            if(result != null){
                notEmpty.signal();
            }
        }finally {
            takeLock.unlock();
        }
        return result.get();
    }

    public Object poll(String qualifier) throws ExecutionException, InterruptedException {
        Future<Object> result = completionMap.getIfPresent(qualifier);
        if(result == null){
            return null;
        }
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try{
            if((result = completionMap.getIfPresent(qualifier)) != null){
                notEmpty.signal();
            }
        }finally {
            takeLock.unlock();
        }
        return result.get();
    }

    public Object poll(String qualifier,long timeout, TimeUnit unit) throws InterruptedException, ExecutionException {
        Future<Object> result = null;
        long nanos = unit.toNanos(timeout);
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        try{
            while((result = completionMap.getIfPresent(qualifier)) == null){
                if(nanos <= 0){
                    return null;
                }
                nanos = notEmpty.awaitNanos(nanos);
            }
            result = completionMap.getIfPresent(qualifier);
            if(result != null){
                notEmpty.signal();
            }
        }finally {
            takeLock.unlock();
        }
        return result.get();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        BatchExecuteService batchExecuteService = new BatchExecuteService(executorService);
        for(int i = 0 ;i < 10 ;i++){
            final int finalI = i;
            batchExecuteService.submit(String.valueOf(i), new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    double random = Math.random();
                    try {
                        if (finalI == 5) {
                            throw new RuntimeException("xetetsetest");
                        }
                    }catch (Exception e){
                        System.out.println("shibaibaibiabi");
                    }
                    System.out.println("inner("+finalI+")--------------->"+random);
                    return random;
                }
            });
        }
        Thread.sleep(1000);
        for(int i = 0; i<10;i++){
//            Object result = batchExecuteService.poll(String.valueOf(i),1,TimeUnit.SECONDS);
            Object result = batchExecuteService.poll(String.valueOf(i));
//            Object result = batchExecuteService.take(String.valueOf(i));
            System.out.println("outer("+i+")--------------->"+result);
        }
    }
}
