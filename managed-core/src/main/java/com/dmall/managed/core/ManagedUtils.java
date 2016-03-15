package com.dmall.managed.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoupeng on 16/3/9.
 */
public class ManagedUtils {
    public static List<Method> findAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        List<Method> ret = new ArrayList<Method>();
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                ret.add(method);
            }
        }
        return ret;
    }
}
