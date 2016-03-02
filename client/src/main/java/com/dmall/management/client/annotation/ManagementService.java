package com.dmall.management.client.annotation;

import java.lang.annotation.*;

/**
 *
 *
 * Created by zoupeng on 16/3/2.
 */

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ManagementService {
    String name();

    String desc();
}
