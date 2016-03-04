package com.dmall.management.core.scanner;

import com.dmall.management.core.AnnotationScanner;
import com.dmall.management.core.annotation.ManagementParam;
import com.dmall.management.core.bean.Parameter;

/**
 * Created by zoupeng on 16/3/2.
 */
class ManagementParamScanner implements AnnotationScanner<Parameter> {
    private ManagementParam param;


    private ManagementParamScanner(){}

    public static ManagementParamScanner getInstance(ManagementParam _param){
        ManagementParamScanner scanner = new ManagementParamScanner();
        scanner.param = _param;
        return scanner;
    }

    public Parameter scan() {
        Parameter tmpParam = new Parameter();
        tmpParam.setDesc(param.desc());
        tmpParam.setName(param.name());
        tmpParam.setType(param.type());
        return tmpParam;
    }

}
