package piggy.nio;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class NioDemoClient {

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bs = new Bootstrap();
            bs.group(group);
            bs.channel(NioSocketChannel.class);
            bs.handler(new ChannelInitializer() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
//                    pipeline.addLast(new StringEncoder());
//                    pipeline.addLast(new StringDecoder());
                    pipeline.addLast(new ClientChannelHandler());
                }
            });
            ChannelFuture channel = bs.connect(new InetSocketAddress(8088)).sync();
            System.out.println("Client start up...");
            channel.channel().close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }

}
