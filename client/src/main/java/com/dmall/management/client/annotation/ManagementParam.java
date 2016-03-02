package com.dmall.management.client.annotation;

import java.lang.annotation.*;

/**
 * Created by zoupeng on 16/3/2.
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ManagementParam {
    String desc();

    String name();

    String type();
}
