package piggy.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 消息编码
 */
public class CharlMessageEncoder extends MessageToByteEncoder {

    private Class<?> aClass;

    public CharlMessageEncoder(Class<?> aClass) {
        this.aClass = aClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (aClass.isInstance(msg)) {
            byte[] serialize = ProtocolSerializeUtil.serialize(msg);
            out.writeInt(serialize.length);
            out.writeBytes(serialize);
        }
    }
}
