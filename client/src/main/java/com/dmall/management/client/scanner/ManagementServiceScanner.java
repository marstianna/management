package com.dmall.management.client.scanner;

import com.dmall.management.client.AnnotationScanner;
import com.dmall.management.client.annotation.ManagementService;
import com.dmall.management.core.bean.Operation;
import com.dmall.management.core.bean.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by zoupeng on 16/3/2.
 */
public class ManagementServiceScanner implements AnnotationScanner<Service>{
    private String className;

    private ManagementServiceScanner(){}

    public static ManagementServiceScanner getInstance(String _className){
        ManagementServiceScanner serviceScanner = new ManagementServiceScanner();
        serviceScanner.className = _className;
        return serviceScanner;
    }

    @Override
    public Service scan() {
        Service service = new Service();
        try {
            Class clazz = Class.forName(className);
            Annotation annotationOfService = clazz.getAnnotation(ManagementService.class);
            if(annotationOfService == null){
                return null;
            }

            ManagementService managementService = (ManagementService)annotationOfService;
            service.setDesc(managementService.desc());
            service.setName(managementService.name());

            Method[] methods = clazz.getMethods();
            for(Method method : methods){
                ManagementOperationScanner operationScanner = ManagementOperationScanner.getInstance(method);
                Operation operation = operationScanner.scan();
                service.add(operation);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return service;
    }

}
