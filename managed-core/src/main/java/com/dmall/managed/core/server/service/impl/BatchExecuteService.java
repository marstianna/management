package com.dmall.managed.core.server.service.impl;

import com.google.common.base.Preconditions;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by zoupeng on 16/3/29.
 */
public class BatchExecuteService {
    private ExecutorService executorService;

    public <T> Future<T> submit(Callable<T> callable){
        Preconditions.checkArgument(callable != null);
        return executorService.submit(callable);
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
