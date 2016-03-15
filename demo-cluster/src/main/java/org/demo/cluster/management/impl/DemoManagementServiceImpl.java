package org.demo.cluster.management.impl;

import com.dmall.managed.core.annotaion.ManagementOperation;
import com.dmall.managed.core.annotaion.ManagementParam;
import com.dmall.managed.core.annotaion.ManagementParams;
import com.dmall.managed.core.annotaion.ManagementService;
import com.dmall.managed.core.constant.ManagedConst;
import com.dmall.managed.core.constant.OperationType;
import org.demo.cluster.management.DemoManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zoupeng on 16/3/3.
 */
@Service
@ManagementService(desc="Demo管理服务")
public class DemoManagementServiceImpl extends DemoManagementService{
    @Autowired
    com.dmall.managed.core.Resource resource;

    @Override
    public void initOnStartUp() {
        super.initOnStartUp();

        String nodeCentral = resource.get(ManagedConst.MASTER_HOST_KEY);
        String registerPath = resource.get(ManagedConst.REGISTER_PATH);
        try {
            connect(nodeCentral, registerPath);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @ManagementOperation(displayName = "加法",  qualifier = "demo_add",type = OperationType.SINGLE)
    @ManagementParams (params = {
            @ManagementParam(desc = "被加数",name = "x",type = "java.lang.Integer",order = 0),
            @ManagementParam(desc = "加数",name = "y",type = "java.lang.Integer",order = 1)
    })
    public Integer add(Integer x, Integer y) {
        return x+y;
    }

    @ManagementOperation(displayName = "减法", qualifier = "demo_minus",type = OperationType.SINGLE)
    @ManagementParams ( params = {
                    @ManagementParam(desc = "被减数",name = "x",type = "java.lang.Integer",order = 0),
                    @ManagementParam(desc = "减数",name = "y",type = "java.lang.Integer",order = 1)
            })
    public Integer minus(Integer x, Integer y) {
        return x-y;
    }

    @ManagementOperation(displayName = "除法", qualifier = "demo_devide",type = OperationType.SINGLE)
    @ManagementParams (params = {
                    @ManagementParam(desc = "被除数",name = "x",type = "java.lang.Integer",order = 0),
                    @ManagementParam(desc = "除数",name = "y",type = "java.lang.Integer",order = 1)
            })
    public Integer devide(Integer x, Integer y) {
        return x / y;
    }

    @ManagementOperation(displayName = "乘法", qualifier = "demo_times",type = OperationType.SINGLE)
    @ManagementParams (params = {
                    @ManagementParam(desc = "被乘数",name = "x",type = "java.lang.Integer",order = 0),
                    @ManagementParam(desc = "乘数",name = "y",type = "java.lang.Integer",order = 1)
            })
    public Integer times(Integer x, Integer y) {
        return x * y;
    }
}
