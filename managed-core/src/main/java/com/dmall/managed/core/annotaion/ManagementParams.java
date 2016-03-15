package com.dmall.managed.core.annotaion;

import java.lang.annotation.*;

/**
 * Created by zoupeng on 16/3/11.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ManagementParams {
    ManagementParam[] params() default {};
}
