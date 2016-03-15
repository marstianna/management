package org.demo.cluster.management;

import com.dmall.managed.core.client.NodeClient;

/**
 * Created by zoupeng on 16/3/3.
 */
public abstract class DemoManagementService extends NodeClient{
    public abstract Integer add(Integer x , Integer y);

    public abstract Integer minus(Integer x , Integer y);

    public abstract Integer devide(Integer x , Integer y);

    public abstract Integer times(Integer x , Integer y);
}
