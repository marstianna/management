package org.demo.cluster;


import com.dmall.managed.core.IAchievePort;

/**
 * Created by zoupeng on 16/3/3.
 */
public class DemoAchievePort implements IAchievePort {
    public Integer getPort() {
        return 8080;
    }
}
