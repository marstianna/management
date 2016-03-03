package com.dmall.management.core.scanner;

import com.dmall.management.client.AnnotationScanner;
import com.dmall.management.core.annotation.ManagementOperation;
import com.dmall.management.core.annotation.ManagementParam;
import com.dmall.management.core.bean.Operation;
import com.dmall.management.core.bean.Parameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by zoupeng on 16/3/2.
 */
class ManagementOperationScanner implements AnnotationScanner<Operation>{
    private Method method;

    private ManagementOperationScanner(){}

    public static ManagementOperationScanner getInstance(Method _method){
        ManagementOperationScanner operationScanner = new ManagementOperationScanner();
        operationScanner.method = _method;
        return operationScanner;
    }

    @Override
    public Operation scan() {
        Operation operation = new Operation();
        Annotation annotationOfOperation = method.getAnnotation(ManagementOperation.class);
        if(annotationOfOperation == null){
            return null;
        }
        ManagementOperation managementOperation = (ManagementOperation)annotationOfOperation;
        operation.setDisplayName(managementOperation.displayName());
        operation.setPath(managementOperation.path());
        operation.setQualifier(managementOperation.qualifier());
        operation.setName(method.getName());
        for(ManagementParam param : managementOperation.params()){
            ManagementParamScanner scanner = ManagementParamScanner.getInstance(param);
            Parameter parameter = scanner.scan();
            if(parameter != null) {
                operation.add(parameter);
            }
        }
        return operation;
    }
}
