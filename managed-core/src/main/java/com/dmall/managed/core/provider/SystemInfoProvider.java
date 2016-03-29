package com.dmall.managed.core.provider;

import com.dmall.managed.core.IAchievePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

public class SystemInfoProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemInfoProvider.class);

    private IAchievePort achievePort;
    /**
     * 从tomcat注册在本地的mbean服务中获取server端口
     *      通过Tomcat静态类ServerFactory可以更容易的获取到端口
     *      但考虑到兼容性,更换容器时，只需要替换JMX的query字符串
     *      增加从数据库中获取端口配置的逻辑的备选逻辑
     * @return
     */
    public static Integer getLocalPort(IAchievePort backUpWay){
        Integer ret = null;
        try{
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            Set<ObjectName> objs = mbs.queryNames(new ObjectName("*:type=Connector,*"),
                    Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
            for (Iterator<ObjectName> i = objs.iterator(); i.hasNext();) {
                ObjectName obj = i.next();
                ret =  Integer.valueOf(obj.getKeyProperty("port"));
            }

            if((ret == null || ret == 0) && backUpWay != null){
                ret = backUpWay.getPort();
            }
        }catch (Exception e){
            LOGGER.error("error occured while inquire server port from local JMX");
        }

        return ret == null ? 8080 : ret;
    }

    public static String getLocalIP(){
        StringBuilder sb = new StringBuilder();
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()  && !inetAddress.isLinkLocalAddress()
                            && inetAddress.isSiteLocalAddress()) {
                        sb.append(inetAddress.getHostAddress().toString());
                    }
                }
            }
        } catch (SocketException e) {  }
        return sb.toString();
    }

    public IAchievePort getAchievePort() {
        return achievePort;
    }

    public void setAchievePort(IAchievePort achievePort) {
        this.achievePort = achievePort;
    }
}
