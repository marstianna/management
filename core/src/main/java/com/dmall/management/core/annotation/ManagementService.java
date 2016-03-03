package com.dmall.management.core.annotation;

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

    /**
     * 类的描述
     * @return
     */
    String desc();
}
