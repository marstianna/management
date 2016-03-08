package org.demo.cluster.management.impl;

import com.dmall.management.core.annotation.ManagementOperation;
import com.dmall.management.core.annotation.ManagementParam;
import com.dmall.management.core.annotation.ManagementService;
import org.demo.cluster.management.DemoManagementService;
import org.springframework.stereotype.Service;

/**
 * Created by zoupeng on 16/3/3.
 */
@Service
@ManagementService(desc="Demo管理服务")
public class DemoManagementServiceImpl implements DemoManagementService{

    @ManagementOperation(displayName = "加法",  qualifier = "demo_add",
    params = {
            @ManagementParam(desc = "被加数",name = "x",type = "java.lang.Integer",order = 0),
            @ManagementParam(desc = "加数",name = "y",type = "java.lang.Integer",order = 1)
    })
    public Integer add(Integer x, Integer y) {
        return x+y;
    }

    @ManagementOperation(displayName = "减法", qualifier = "demo_minus",
            params = {
                    @ManagementParam(desc = "被减数",name = "x",type = "java.lang.Integer",order = 0),
                    @ManagementParam(desc = "减数",name = "y",type = "java.lang.Integer",order = 1)
            })
    public Integer minus(Integer x, Integer y) {
        return x-y;
    }

    @ManagementOperation(displayName = "除法", qualifier = "demo_devide",
            params = {
                    @ManagementParam(desc = "被除数",name = "x",type = "java.lang.Integer",order = 0),
                    @ManagementParam(desc = "除数",name = "y",type = "java.lang.Integer",order = 1)
            })
    public Integer devide(Integer x, Integer y) {
        return x / y;
    }

    @ManagementOperation(displayName = "乘法", qualifier = "demo_times",
            params = {
                    @ManagementParam(desc = "被乘数",name = "x",type = "java.lang.Integer",order = 0),
                    @ManagementParam(desc = "乘数",name = "y",type = "java.lang.Integer",order = 1)
            })
    public Integer times(Integer x, Integer y) {
        return x * y;
    }
}
