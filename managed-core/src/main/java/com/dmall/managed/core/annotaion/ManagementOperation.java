package com.dmall.managed.core.annotaion;

import com.dmall.managed.core.constant.OperationType;

import java.lang.annotation.*;

/**
 * cluster提供集群管理的方法,这个注解需要在注解
 * @ManagementService标注的类下才会被扫描到
 *
 * Created by zoupeng on 16/3/2.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ManagementOperation {
    /**
     * 界面展示时使用的名字
     * @return
     */
    String displayName();

    /**
     * 方法的唯一标识
     * @return
     */
    String qualifier();

    /**
     * 方法传播类型,默认为广播
     * @return
     */
    OperationType type() default OperationType.BROADCAST;
}
