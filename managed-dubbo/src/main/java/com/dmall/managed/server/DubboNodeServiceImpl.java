package com.dmall.managed.server;

import com.alibaba.fastjson.JSON;
import com.dmall.managed.commons.ZookeeperClient;
import com.dmall.managed.core.NodeServiceBuilder;
import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.client.ManagementClientService;
import com.dmall.managed.core.server.impl.AbstractNodeServiceImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by zoupeng on 16/4/7.
 */
public class DubboNodeServiceImpl extends AbstractNodeServiceImpl {
    private ZookeeperClient zookeeperClient;
    private DubboServiceReferenceHelper dubboServiceReferenceHelper;

    @PostConstruct
    public void initOnStart(){
        addNodeChangeWatcher();
        refresh();
    }

    @Override
    public void refresh() {
        registerStore.clear();
        List<String> urls = zookeeperClient.getChildren("providers");
        for(String url : urls){
            try {
                String dubboUrl = getDubboUrl(url);
                register(dubboUrl);
            }catch (Exception e){
                LOGGER.warn("链接"+url+"失败,请启动完成过后重试!");
            }
        }
    }

    @Override
    public void register(String ip, Integer port, String path) {
        String url = dubboServiceReferenceHelper.urlBuild(ip, port, path);
        register(url);
    }

    private void register(String url){
        ManagementClientService managementClientService = dubboServiceReferenceHelper.get(url);
        Node node = managementClientService.registerNodeInfo();
        NodeServiceBuilder.buildNode(node);
        register(node);
    }

    private void addNodeChangeWatcher() {
        try {
            zookeeperClient.addNodeWatcher("providers", new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                    String[] path = pathChildrenCacheEvent.getData().getPath().split("/");
                    String dubboUrl = getDubboUrl(path[path.length - 1]);
                    switch (pathChildrenCacheEvent.getType()){
                        case CHILD_ADDED:
                            register(dubboUrl);
                            break;
                        case CHILD_REMOVED:
                            Pair<String,Integer> ipAndPort = getIpAndPort(dubboUrl);
                            deregister(ipAndPort.getKey(),ipAndPort.getValue());
                            break;
                        case CHILD_UPDATED:
                            register(dubboUrl);
                            break;
                        default:
                            break;
                    }
                }
            });
        }catch (Exception e){
            LOGGER.warn("添加zk节点变更watcher失败,请检查ZK链接",e);
        }
    }

    private String getDubboUrl(String originUrl){
        try {
            originUrl = URLDecoder.decode(originUrl,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("url解析失败",e);
        }
        return originUrl.split("\\?")[0];
    }

    private Pair<String,Integer> getIpAndPort(String dubboUrl){
        String host = dubboUrl.split("/")[2];
        String[] addr = host.split(":");
        return Pair.of(addr[0],Integer.parseInt(addr[1]));
    }

    public void setZookeeperClient(ZookeeperClient zookeeperClient) {
        this.zookeeperClient = zookeeperClient;
    }

    public void setDubboServiceReferenceHelper(DubboServiceReferenceHelper dubboServiceReferenceHelper) {
        this.dubboServiceReferenceHelper = dubboServiceReferenceHelper;
    }

    public static void main(String[] args) {
        String s = "dubbo://169.254.210.254:20820/com.dmall.oss.dubbo.OssManagementClientService";
        String[] arr = s.split("/");
        System.out.println(JSON.toJSONString(arr));
    }

}
