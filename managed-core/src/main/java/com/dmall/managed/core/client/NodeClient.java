package com.dmall.managed.core.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmall.managed.core.NodeServiceBuilder;
import com.dmall.managed.core.annotaion.ManagementService;
import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.bean.Operation;
import com.dmall.managed.core.bean.Service;
import com.dmall.managed.core.helper.HttpSender;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zoupeng on 16/3/11.
 */
public class NodeClient implements ApplicationListener<ContextRefreshedEvent> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(NodeClient.class);

    protected ApplicationContext ac;

    protected ManagementConfig managementConfig;

    private String centralNode;

    private Node node;

    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(ac == null){
            ac = event.getApplicationContext();
            initOnStartUp();
        }
    }

    public void initOnStartUp(){
        managementConfig = ac.getBean(ManagementConfig.class);
    }

    public void connect(String centralNode, String registerPath) {
        Preconditions.checkArgument(!StringUtils.isBlank(centralNode));

        LOGGER.info("启动时自动注册方法调用开始,中央节点注册地址为"+centralNode+registerPath);
        Node node = collectNodeInfo();
        NameValuePair pair = new NameValuePair("nodeInfo", JSON.toJSONString(node));
        try {
            String registerResult = HttpSender.connect(centralNode, registerPath, Lists.newArrayList(pair));
            JSONObject result = JSON.parseObject(registerResult);
            Boolean success = result.getBoolean("success");
            if (!success) {
                throw new RuntimeException("注册失败！"+ result.getString("message"));
            }
        }catch(Exception e){
            LOGGER.error("自动注册到man节点失败,请手动重试",e);
        }

        fillRegisterInfo(centralNode, node);

    }



    public Operation findOperation(String operationQualifier){
        for(Service service : this.node.getServices()){
            for(Operation operation : service.getOperations()){
                if(operation.getQualifier().equals(operationQualifier)){
                    operation.setService(service);
                    service.setNode(this.node);
                    return operation;
                }
            }
        }
        return null;
    }

    private void fillRegisterInfo(String centralNode , Node node){
        this.setCentralNode(centralNode);
        this.node = node;
    }

    private Node collectNodeInfo() {
        Node node = managementConfig.getNode();
        String qualifier = NodeServiceBuilder.buildNodeQualifier(node);
        node.setNodeQualifier(qualifier);

        List<Service> beans = getAllManagedBeans();
        node.setServices(beans);

        return node;
    }

    private List<Service> getAllManagedBeans() {
        List<Service> ret = new ArrayList<Service>();
        ApplicationContext context = this.ac;

        do {
            Map<String, Object> beanMap = context.getBeansWithAnnotation(ManagementService.class);
            if (MapUtils.isNotEmpty(beanMap)) {
                for(Object obj : beanMap.values()){
                    Service service = NodeServiceBuilder.buidNodeService(obj.getClass());
                    ret.add(service);
                }
            }
        } while ((context = context.getParent()) != null);

        return ret;
    }


    public ApplicationContext getAc() {
        return ac;
    }

    public ManagementConfig getManagementConfig() {
        return managementConfig;
    }

    public void setManagementConfig(ManagementConfig managementConfig) {
        this.managementConfig = managementConfig;
    }

    public String getCentralNode() {
        return centralNode;
    }

    public void setCentralNode(String centralNode) {
        this.centralNode = centralNode;
    }

    public Node getNode() {
        return node;
    }

}

