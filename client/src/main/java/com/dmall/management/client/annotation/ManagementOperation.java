package com.dmall.management.client.annotation;

import java.lang.annotation.*;

/**
 * Created by zoupeng on 16/3/2.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ManagementOperation {
    String displayName();

    String path();

    String qualifier();

    ManagementParam[] params();
}
