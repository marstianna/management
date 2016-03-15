package com.dmall.managed.core.annotaion;

import java.lang.annotation.*;

/**
 * 嵌套于@ManagementOperation里面,用于注解方法的入参
 * Created by zoupeng on 16/3/2.
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ManagementParam {
    /**
     * 方法入参的描述
     * @return
     */
    String desc();

    /**
     * 入参的参数名
     * @return
     */
    String name();

    /**
     * 入参的参数类型
     * @return
     */
    String type();

    /**
     * 参数顺序位置表示
     * @return
     */
    int order();
}
