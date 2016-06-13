package com.dmall.managed.core.server;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by zoupeng on 6/13/16.
 */
public interface BlockingExecuteService {

    /**
     * 提交一个任务
     * @param qualifier 任务标识
     * @param task
     * @return
     */
    Future<Object> submit(String qualifier, Callable<Object> task);

    /**
     * 根据标识获取异步处理结果
     * 该方法阻塞获取结果,如果结果一直没有处理完,将无限期阻塞;
     * @param qualifier
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    Object take(String qualifier) throws InterruptedException, ExecutionException;

    /**
     * 根据标识获取异步处理结果
     * 如果获取结果瞬间,任务还没有处理完,将返回null
     * @param qualifier
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    Object poll(String qualifier) throws ExecutionException, InterruptedException;

    /**
     * 根据标识获取异步处理结果
     * 该方法设置一个超时时间,如果超时后依然没有结果
     * 就返回null
     * @param qualifier
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    Object poll(String qualifier,long timeout, TimeUnit unit) throws InterruptedException, ExecutionException ;
}
