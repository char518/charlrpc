package piggy.call.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;
import piggy.common.Request;
import piggy.common.Response;
import piggy.common.ServiceInstance;
import piggy.registry.ServiceRegistry;
import piggy.serialize.CharlMessageDecoder;
import piggy.serialize.CharlMessageEncoder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements ApplicationContextAware, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private static ConcurrentHashMap<String, Object> serverNameMap = new ConcurrentHashMap<String, Object>();

    private String serverAddress;

    private ServiceRegistry serviceRegistry;

    public Server(String serverAddress, ServiceRegistry serviceRegistry) {
        this.serverAddress = serverAddress;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LOGGER.info("setApplicationContext is called");
        Map<String, Object> annotation = applicationContext.getBeansWithAnnotation(Charl.class);
        if (null != annotation && !annotation.isEmpty()) {
            for (Object serviceBean : annotation.values()) {
                Charl charl = serviceBean.getClass().getAnnotation(Charl.class);
                String name = charl.value().getName();
                String version = charl.version();
                if (StringUtils.isEmpty(version)) {
                    name.concat(":" + version);
                }
                serverNameMap.put(name, serviceBean);

                LOGGER.info("setApplicationContext serverNameMap key is:{}, value is:{}", name, serviceBean);
            }
        }
        LOGGER.info("setApplicationContext serverNameMap is:", serverNameMap);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("afterPropertiesSet is called end");
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap br = new ServerBootstrap();
        br.group(boss, worker);
        br.channel(NioServerSocketChannel.class);
        br.childHandler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                //先对请求进行转码
                pipeline.addLast(new CharlMessageDecoder(Request.class));
                pipeline.addLast(new CharlMessageEncoder(Response.class));
                pipeline.addLast(new NettyServerHandler(serverNameMap));
            }
        });
        br.option(ChannelOption.SO_BACKLOG, 1024);
        br.childOption(ChannelOption.SO_KEEPALIVE, true);
        String[] split = serverAddress.split(":");
        String host = split[0];
        Integer port = Integer.parseInt(split[1]);
        ChannelFuture sync = br.bind(host, port).sync();
        LOGGER.info("afterPropertiesSet bind server host");

        for (String s : serverNameMap.keySet()) {
            ServiceInstance instance = new ServiceInstance();
            instance.setHost(host);
            instance.setPort(port + "");
            instance.setServiceName(s);
            LOGGER.info("registry server:", s);
            serviceRegistry.registry(instance);
        }

        sync.channel().closeFuture().sync();
        LOGGER.info("afterPropertiesSet server end");
    }
}
