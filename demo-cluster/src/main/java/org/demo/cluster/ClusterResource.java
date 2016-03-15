package org.demo.cluster;

import com.dmall.managed.core.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by zoupeng on 16/3/11.
 */
@Component
public class ClusterResource implements Resource {
    @Override
    public String get(String key) {
        return "http://192.168.8.44";
    }

    @Override
    public Integer update(String key, String value) {
        return null;
    }
}
