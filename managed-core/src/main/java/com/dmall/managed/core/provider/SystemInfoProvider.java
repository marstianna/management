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
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

public class SystemInfoProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemInfoProvider.class);

    private static final String LOCALHOST = "127.0.0.1";

    private static final String ANYHOST = "0.0.0.0";

    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");


    private IAchievePort achievePort;
    /**
     * 从tomcat注册在本地的mbean服务中获取server端口
     *      通过Tomcat静态类ServerFactory可以更容易的获取到端口
     *      但考虑到兼容性,更换容器时，只需要替换JMX的query字符串
     *      增加从数据库中获取端口配置的逻辑的备选逻辑
     * @return
     */
    public static Integer getLocalPort(IAchievePort achievePort){
        if(achievePort != null){
            return achievePort.getPort();
        }

        Integer ret = null;
        try{
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            Set<ObjectName> objs = mbs.queryNames(new ObjectName("*:type=Connector,*"),
                    Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
            for (Iterator<ObjectName> i = objs.iterator(); i.hasNext();) {
                ObjectName obj = i.next();
                ret =  Integer.valueOf(obj.getKeyProperty("port"));
            }

        }catch (Exception e){
            LOGGER.error("error occured while inquire server port from local JMX");
        }

        return ret == null ? 8080 : ret;
    }

    public static String getLocalIP(){
        InetAddress localAddress = getLocalAddress0();
        return localAddress == null ? LOCALHOST : localAddress.getHostAddress();
    }

    public IAchievePort getAchievePort() {
        return achievePort;
    }

    public void setAchievePort(IAchievePort achievePort) {
        this.achievePort = achievePort;
    }

    private static InetAddress getLocalAddress0() {
        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Throwable e) {
            LOGGER.warn("Failed to retriving ip address, " + e.getMessage(), e);
        }
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        if (addresses != null) {
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = addresses.nextElement();
                                    if (isValidAddress(address)) {
                                        return address;
                                    }
                                } catch (Throwable e) {
                                    LOGGER.warn("Failed to retriving ip address, " + e.getMessage(), e);
                                }
                            }
                        }
                    } catch (Throwable e) {
                        LOGGER.warn("Failed to retriving ip address, " + e.getMessage(), e);
                    }
                }
            }
        } catch (Throwable e) {
            LOGGER.warn("Failed to retriving ip address, " + e.getMessage(), e);
        }
        LOGGER.error("Could not get local host ip address, will use 127.0.0.1 instead.");
        return localAddress;
    }

    private static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress())
            return false;
        String name = address.getHostAddress();
        return (name != null
                && ! ANYHOST.equals(name)
                && ! LOCALHOST.equals(name)
                && IP_PATTERN.matcher(name).matches());
    }
}
