package com.dmall.managed.server;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.dmall.managed.core.client.ManagementClientService;

/**
 * Created by zoupeng on 16/4/7.
 */
public class DubboServiceReferenceHelper {
    private final static ReferenceConfig<ManagementClientService> reference = new ReferenceConfig<>();

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
