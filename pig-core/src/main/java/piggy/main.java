package piggy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import piggy.common.Request;
import piggy.common.ServiceInstance;
import piggy.registry.zookeeper.ZookeeperRegistry;
import piggy.serialize.ProtocolSerializeUtil;

import java.util.UUID;

public class main {

    private static final Logger log = LoggerFactory.getLogger("test");

    public static void main(String[] args) {
//        ZookeeperRegistry registry = new ZookeeperRegistry();
//        ServiceInstance instance = new ServiceInstance();
//        instance.setHost("192.168.77.129");
//        instance.setPort("8081");
//        instance.setServiceName("charl01");
//
//        registry.registry(instance);
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        log.info("run to end!!!!");
//        System.out.println("run to end");


//        ZookeeperDiscovery discovery = new ZookeeperDiscovery();
//        ServiceInstance charl01 = discovery.discovery("charl01");
//        System.out.println(charl01);

        Request request = new Request();
        request.setRequestId(UUID.randomUUID().toString());
        request.setMethodName("hello");
        request.setServiceName("com.charl.HelloService");
        request.setVersion("1.0");

        byte[] serialize = ProtocolSerializeUtil.serialize(request);

        Request deserialize = ProtocolSerializeUtil.deserialize(serialize, Request.class);
        System.out.println(deserialize);
    }
}
