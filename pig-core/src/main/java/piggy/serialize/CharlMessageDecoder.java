package piggy.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 消息解码
 */
public class CharlMessageDecoder extends ByteToMessageDecoder {

    private Class<?> aClass;

    public CharlMessageDecoder(Class<?> aClass) {
        this.aClass = aClass;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //可读位数小于4，说明没有数据
        if (in.readableBytes() < 4) {
            return;
        }
        //把读指针设置到当前缓存中
        in.markReaderIndex();
        //获取当前指针的下标
        int length = in.readInt();
        //可读数据小于长度，说明有问题
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }
        byte[] array = new byte[length];
        in.readBytes(array);

        Object obj = ProtocolSerializeUtil.deserialize(array, aClass);
        out.add(obj);
    }
}
