package com.dmall.managed.core.server.impl;

import com.dmall.managed.core.server.BlockingExecuteService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zoupeng on 16/3/29.
 */
public class BatchExecuteService implements BlockingExecuteService {
    private final Executor executor;
    private final Cache<String,Future<Object>> completionMap = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .build();

    /** Lock held by take, poll, etc */
    private final ReentrantLock takeLock = new ReentrantLock();

    /** Wait queue for waiting takes */
    private final Condition notEmpty = takeLock.newCondition();


    private class QueueingFuture extends FutureTask<Object> {
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
        return new FutureTask<>(task);
    }

    private RunnableFuture<Object> newTaskFor(Runnable task, Object result) {
        return new FutureTask<>(task, result);
    }

    public BatchExecuteService(Executor executor) {
        if (executor == null)
            throw new NullPointerException();
        this.executor = executor;
    }

    public Future<Object> submit(String qualifier,Callable<Object> task) {
        if (task == null) throw new NullPointerException();
        RunnableFuture<Object> f = newTaskFor(task);
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
        return result == null ? null : result.get();
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
        return result == null ? null : result.get();
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
        return result == null ? null : result.get();
    }

}
