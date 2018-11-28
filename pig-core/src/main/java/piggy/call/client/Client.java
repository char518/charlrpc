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
            final Response[] response = {null};
            bs.group(group);
            bs.channel(NioSocketChannel.class);
            bs.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new CharlMessageEncoder(Request.class));
                    pipeline.addLast(new CharlMessageDecoder(Response.class));
                    NettyClientHandler nettyClientHandler = new NettyClientHandler();
                    response[0] = nettyClientHandler.getResponse();
                    pipeline.addLast(nettyClientHandler);
                }
            });

            bs.option(ChannelOption.TCP_NODELAY, true);
            ChannelFuture sync = bs.connect(host, port).sync();

            Channel channel = sync.channel();
            channel.writeAndFlush(request).sync();
            channel.closeFuture().sync();

            return response[0];
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } finally {
            group.shutdownGracefully();
        }
    }

}
