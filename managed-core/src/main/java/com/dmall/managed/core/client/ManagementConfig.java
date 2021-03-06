package com.dmall.managed.core.client;


import com.dmall.managed.core.IAchievePort;
import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.provider.SystemInfoProvider;

/**
 * Management的Node节点的配置信息
 *
 * Created by zoupeng on 16/3/2.
 */
public class ManagementConfig {
    private String desc;
    private String name;
    private String displayName;
    //如果为dubbo协议,那么basePath就是调用接口
    private String basePath;
    //获取当前提供服务的端口
    private IAchievePort achievePort;
    //add by pengz
    //组名,如果组名相同,将会在页面归档为同一类
    private String groupName;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public IAchievePort getAchievePort() {
        return achievePort;
    }

    public void setAchievePort(IAchievePort achievePort) {
        this.achievePort = achievePort;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Node getNode(){
        Node node = new Node();
        node.setName(this.name);
        node.setBasePath(this.basePath);
        node.setDesc(this.desc);
        node.setDisplayName(this.displayName);
        node.setIp(SystemInfoProvider.getLocalIP());
        node.setPort(SystemInfoProvider.getLocalPort(this.achievePort));
        node.setGroup(this.getGroupName());
        return node;
    }
}
