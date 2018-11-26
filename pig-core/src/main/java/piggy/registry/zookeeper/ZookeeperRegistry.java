package piggy.registry.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import piggy.common.Constants;
import piggy.common.ServiceInstance;
import piggy.common.ZookeeperUtil;
import piggy.registry.ServiceRegistry;

public class ZookeeperRegistry implements ServiceRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperRegistry.class);

    @Override
    public void registry(ServiceInstance serviceInstance) {
        LOGGER.info("ServiceRegistry registry service:", serviceInstance.toString());
        ZkClient zk = ZookeeperUtil.connect();
        if (!zk.exists(Constants.MAST_NODE)) {
            zk.createPersistent(Constants.MAST_NODE);
        }

        String servicePath = Constants.MAST_NODE + "/" + serviceInstance.getServiceName();
        if (!zk.exists(servicePath)) {
            zk.createPersistent(servicePath);
        }

        String addressPath = servicePath + "/address-";
        String address = serviceInstance.getHost() + ":" + serviceInstance.getPort();
        String ephemeralSequential = zk.createEphemeralSequential(addressPath, address);

        LOGGER.info("ServiceRegistry registry service result:", ephemeralSequential);
    }
}
