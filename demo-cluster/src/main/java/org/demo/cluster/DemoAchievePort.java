package org.demo.cluster;

import com.dmall.management.core.IAchievePort;

/**
 * Created by zoupeng on 16/3/3.
 */
public class DemoAchievePort implements IAchievePort {
    @Override
    public Integer getPort() {
        return 8080;
    }
}
