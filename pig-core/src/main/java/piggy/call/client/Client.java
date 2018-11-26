package piggy.call.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import piggy.common.Request;
import piggy.common.Response;
import piggy.serialize.CharlMessageDecoder;
import piggy.serialize.CharlMessageEncoder;

public class Client {

    private String host;

    private int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @SuppressWarnings("unchecked")
    public Response send(Request request) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bs = new Bootstrap();
        try {
            bs.group(group);
            bs.channel(NioSocketChannel.class);
            final Response response = new Response();
            bs.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new CharlMessageEncoder(Request.class));
                    pipeline.addLast(new CharlMessageDecoder(Request.class));
                    pipeline.addLast(new NettyClientHandler(response));
                }
            });

            bs.option(ChannelOption.TCP_NODELAY, true);
            ChannelFuture sync = bs.connect(host, port).sync();

            Channel channel = sync.channel();
            channel.writeAndFlush(request).sync();

            return response;
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } finally {
            group.shutdownGracefully();
        }
    }

}
