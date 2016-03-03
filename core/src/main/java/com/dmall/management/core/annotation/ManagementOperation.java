package com.dmall.management.core.annotation;

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
     * 请求的相对路径,绝对路径是Node节点的basePath/path
     * @return
     */
    String path();

    /**
     * 方法的唯一标识
     * @return
     */
    String qualifier();

    /**
     * 方法的参数列表
     * @return
     */
    ManagementParam[] params();
}
