package piggy.call.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import piggy.common.Response;

public class NettyClientHandler extends SimpleChannelInboundHandler {

    private Response response;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.response = (Response) msg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    public Response getResponse() {
        return response;
    }

}
