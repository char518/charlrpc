package piggy.discovery;

import piggy.common.ServiceInstance;

public interface ServiceDiscovery {

    /**
     * 根据服务名查找注册的服务
     * @param serviceName
     * @return
     */
    ServiceInstance discovery(String serviceName);

}
