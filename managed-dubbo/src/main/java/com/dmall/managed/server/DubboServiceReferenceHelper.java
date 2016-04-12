package com.dmall.managed.server;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.dmall.managed.core.client.ManagementClientService;

/**
 * Created by zoupeng on 16/4/7.
 */
public class DubboServiceReferenceHelper {
    private final static ReferenceConfig<ManagementClientService> reference = new ReferenceConfig<>();

    //FIXME 这里需要设置interface.class和application
    private final static ApplicationConfig application = new ApplicationConfig();

    static{
        application.setName("ofc_man");
//        gst
//
// reference.setInterface("com.dmall.managed.core.client.ManagementClientService");
        reference.setInterface(ManagementClientService.class);
        reference.setApplication(application);

    }

    public static ManagementClientService get(String url){
        reference.setUrl(url);
        return reference.get();
    }

    public static ManagementClientService get(String ip,Integer port,String interfaceName){
        String url = urlBuild(ip, port, interfaceName);
        return get(url);
    }

    private static String urlBuild(String ip,Integer port,String interfaceName){
        return "dubbo://"+ip+":"+port+"/"+interfaceName;
    }

}
