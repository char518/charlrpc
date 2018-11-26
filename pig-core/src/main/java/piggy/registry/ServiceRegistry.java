package piggy.registry;

import piggy.common.ServiceInstance;

public interface ServiceRegistry {

    /**
     * 注册服务
     * @param serviceInstance
     * @return
     */
    void registry(ServiceInstance serviceInstance);

}
