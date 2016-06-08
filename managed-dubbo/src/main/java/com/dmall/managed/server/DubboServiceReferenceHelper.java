package com.dmall.managed.server;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.dmall.managed.core.client.ManagementClientService;

public class DubboServiceReferenceHelper {
    private final ReferenceConfig<ManagementClientService> reference = new ReferenceConfig<>();

    private final ApplicationConfig application = new ApplicationConfig();

    public DubboServiceReferenceHelper(String appName,String owner,String interfaceName) throws ClassNotFoundException {
        application.setName(appName);
        application.setOwner(owner);
        reference.setInterface(interfaceName);
        reference.setInterface(Class.forName(interfaceName));
        reference.setApplication(application);
    }

    public ManagementClientService get(String url){
        reference.setUrl(url);
        return reference.get();
    }

    public ManagementClientService get(String ip, Integer port, String interfaceName){
        String url = urlBuild(ip, port, interfaceName);
        return get(url);
    }

    public String urlBuild(String ip,Integer port,String interfaceName){
        return "dubbo://"+ip+":"+port+"/"+interfaceName;
    }

}