package piggy.discovery.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import piggy.common.Constants;
import piggy.common.ServiceInstance;
import piggy.common.ZookeeperUtil;
import piggy.discovery.ServiceDiscovery;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ZookeeperDiscovery implements ServiceDiscovery {

    @Override
    public ServiceInstance discovery(String serviceName) {
        ZkClient zk = ZookeeperUtil.connect();
        try {
            String path = Constants.MAST_NODE + "/" + serviceName;
            if (!zk.exists(path)) {
                System.out.println("run error,service not registry!!!");
            }

            List<String> children = zk.getChildren(path);

            if(null == children || children.size() == 0) {
                System.out.println("run error,service address not registry!!!");
            }

            String service = "";
            int size = children.size();

            if (size == 1) {
                service = children.get(0);
            } else {
                int i = ThreadLocalRandom.current().nextInt(size);
                service = children.get(i);
            }

            String serviePath = path + "/" + service;
            String s = (String) zk.readData(serviePath);

            String[] split = s.split(":");
            ServiceInstance instance = new ServiceInstance();
            instance.setHost(split[0]);
            instance.setPort(split[1]);
            return instance;
        } finally {
            zk.close();
        }
    }
}
