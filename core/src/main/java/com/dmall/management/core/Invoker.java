package com.dmall.management.core;

import com.dmall.management.core.bean.Operation;

/**
 * Created by zoupeng on 16/3/2.
 */
public interface Invoker {
    Object invoke(Operation operation);
}
