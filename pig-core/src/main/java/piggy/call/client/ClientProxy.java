package piggy.call.client;

import piggy.common.Request;
import piggy.common.Response;
import piggy.common.ServiceInstance;
import piggy.common.StringUtils;
import piggy.discovery.ServiceDiscovery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * 用于创建远程服务代理
 */
public class ClientProxy {

    private ServiceInstance serviceInstance;

    private ServiceDiscovery serviceDiscovery;

    public ClientProxy(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public ClientProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T createProxy(final Class<?> aClass) {
        return createProxy(aClass, "");
    }

    /**
     * jdk动态代理外部接口
     *
     * @param aClass
     * @param version
     * @param <T>
     * @return
     */
    public <T> T createProxy(Class<?> aClass, final String version) {
        return (T) Proxy.newProxyInstance(aClass.getClassLoader(), new Class<?>[]{aClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Request request = new Request();
                request.setRequestId(UUID.randomUUID().toString());
                request.setMethodName(method.getName());
                String serverName = method.getDeclaringClass().getName();
                request.setServiceName(serverName);
                request.setParameterTypes(method.getParameterTypes());
                request.setParameters(args);
                request.setVersion(version);

                if (null == serviceDiscovery) {
                    throw new RuntimeException("服务发现出现异常！！！");
                }

                if (!StringUtils.isEmpty(version)) {
                    serverName = serverName.concat(":" + version);
                }

                ServiceInstance discovery = serviceDiscovery.discovery(serverName);
                if (null == discovery) {
                    throw new RuntimeException("zk中未找到服务！！！");
                }

                Client client = new Client(discovery.getHost(), Integer.parseInt(discovery.getPort()));
                Response response = client.send(request);
                if (null == response.getException()) {
                    return response;
                } else {
                    throw response.getException();
                }
            }
        });
    }
}
