package com.dmall.management.test;

import com.dmall.management.core.annotation.ManagementOperation;
import com.dmall.management.core.annotation.ManagementParam;
import com.dmall.management.core.annotation.ManagementService;

/**
 * Created by zoupeng on 16/3/3.
 */
@ManagementService(desc = "test")
public class TestManagementService {

    @ManagementOperation(displayName = "testMethod1",qualifier = "testMethod1"
    ,params = {
            @ManagementParam(desc = "testParam1",name = "param1",type = "java.lang.String",order = 0)
    })
    public void testMethod1(String param1){
        System.out.println("----------------------------------------");
    }
}
