package com.charl;

import com.charl.impl.DemoApiImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import piggy.call.server.Server;
import piggy.registry.zookeeper.ZookeeperRegistry;

@Configuration
public class BeanConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanConfig.class);

    private String address = "127.0.0.1:8099";

    @Bean(name = "zookeeperRegistry")
    public ZookeeperRegistry zookeeperRegistry() {
        LOGGER.info("zookeeperRegistry start!!!!");
        return new ZookeeperRegistry();
    }

    @Bean(name = "server")
    public Server server() {
        LOGGER.info("server start!!!!");
        Server server = new Server(address, zookeeperRegistry());
        return server;
    }

    @Bean
    public DemoApiImpl demoApi() {
        return new DemoApiImpl();
    }

}
