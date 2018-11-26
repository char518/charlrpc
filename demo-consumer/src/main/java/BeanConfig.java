import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import piggy.call.client.ClientProxy;
import piggy.discovery.zookeeper.ZookeeperDiscovery;

@Configuration
public class BeanConfig {

    @Bean
    public ZookeeperDiscovery zookeeperDiscovery() {
       return new ZookeeperDiscovery();
    }

    @Bean
    public ClientProxy clientProxy() {
        return new ClientProxy(zookeeperDiscovery());
    }




}
