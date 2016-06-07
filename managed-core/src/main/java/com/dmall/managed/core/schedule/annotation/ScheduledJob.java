package com.dmall.managed.core.schedule.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zoupeng on 6/7/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ScheduledJob {
    /**
     * Cron.
     *
     * @return the string
     */
    String cron() default "";

    /**
     * Fixed rate.
     *
     * @return the long
     */
    long fixedRate() default -1;

    /**
     * 相同JVM下,是否可以并发运行.
     *
     * @return true, if successful
     */
    boolean concurrent() default false;

    /**
     * 集群环境下,是否可以并发运行.
     *
     * @return true, if successful
     */
    boolean clusterConcurrent() default false;

    /**
     * Should recover.
     *
     * @return true, if successful
     */
    boolean shouldRecover() default true;

    /**
     * Name.
     *
     * @return the string
     */
    String name() default "";

    /**
     * Group.
     *
     * @return the string
     */
    String group() default "";
}
