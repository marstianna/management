/*
 * 
 */
package org.demo.cluster.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

// TODO: Auto-generated Javadoc

/**
 * A component providing static access of ApplicationContext
 * supposed to be initialize in Web ApplicationContext(dispatcher), not Root ApplicationContext.
 */
public class ApplicationContextUtil implements ApplicationContextAware {

    /** The context. */
    private static ApplicationContext context;

    /* (non-Javadoc)
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext ac) {
        context = ac;
    }

    /**
     * Gets the application context.
     *
     * @return the application context
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * Gets the bean.
     *
     * @param beanName the bean name
     * @return the bean
     */
    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    /**
     * Gets the bean.
     *
     * @param <T> the generic type
     * @param beanName the bean name
     * @param clazz the clazz
     * @return the bean
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        return context.getBean(beanName, clazz);
    }

    /**
     * Gets the bean.
     *
     * @param <T> the generic type
     * @param clazz the clazz
     * @return the bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

}
