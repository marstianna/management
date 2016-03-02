package com.dmall.management.client.configuration;

import com.dmall.management.core.IAchievePort;
import com.dmall.management.core.bean.Node;
import com.dmall.management.core.provider.SystemInfoProvider;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zoupeng on 16/3/2.
 */
public class ManagementConfig {
    private Set<String> classNames = new HashSet<String>();
    private String desc;
    private String name;
    private String displayName;
    private String basePath;
    private IAchievePort achievePort;

    public void addClass(String className){
        classNames.add(className);
    }

    public Set<String> getClassNames() {
        return classNames;
    }

    public void setClassNames(Set<String> classNames) {
        this.classNames = classNames;
    }

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

    public Node getNode(){
        Node node = new Node();
        node.setName(this.name);
        node.setBasePath(this.basePath);
        node.setDesc(this.desc);
        node.setDisplayName(this.displayName);
        node.setIp(SystemInfoProvider.getLocalIP());
        node.setPort(SystemInfoProvider.getLocalPort(this.achievePort));
        return node;
    }
}
