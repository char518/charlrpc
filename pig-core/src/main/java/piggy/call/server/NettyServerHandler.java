package piggy.call.server;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import piggy.common.Request;
import piggy.common.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class NettyServerHandler extends SimpleChannelInboundHandler {

    private static final Logger LOG = LoggerFactory.getLogger(NettyServerHandler.class);

    private final ConcurrentHashMap<String, Object> serverNameMap;

    public NettyServerHandler(ConcurrentHashMap<String, Object> serverNameMap) {
        this.serverNameMap = serverNameMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOG.info("received msg from remote!!");
        Request request = (Request) msg;
        Response response = new Response();
        response.setRequestId(request.getRequestId());
        try {
            Object o = handleResult(request);
            response.setResp(o);
        } catch (Exception e) {
            response.setException(e);
            LOG.error("NettyServer handler handle result occured exception：",e.getMessage());
        }
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 根据请求参数找到请求的接口，利用反射进行调用
     *
     * @param request
     * @return
     */
    private Object handleResult(Request request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        LOG.info("handleResult msg from remote!!");
        String serviceName = request.getServiceName();
        String version = request.getVersion();
        if (!StringUtils.isEmpty(version)) {
            serviceName.concat(":" + version);
        }

        if (!serverNameMap.contains(serviceName)) {
            throw new RuntimeException("未找到注册服务！！！");
        }

        Object serverBean = serverNameMap.get(serviceName);
        String methodName = request.getMethodName();

        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        Class<?> aClass = serverBean.getClass();
        Method method = aClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serverBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
