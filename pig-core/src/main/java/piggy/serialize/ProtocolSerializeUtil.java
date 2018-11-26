package piggy.serialize;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * google protobuf 进行序列化
 */
public class ProtocolSerializeUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ProtocolSerializeUtil.class);

    private static Map<Class<?>, Schema<?>> cacheMap = new ConcurrentHashMap<Class<?>,Schema<?>>();

    /**
     * 对象序列化(对象 -> 字节数组)
     * @param t
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T t) {
        Class<T> obj = (Class<T>) t.getClass();
        LinkedBuffer allocate = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(obj);
            return ProtobufIOUtil.toByteArray(t, schema, allocate);
        } catch (Exception e) {
            LOG.error("serialize occured exception:", e.getMessage());
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            allocate.clear();
        }
    }

    /**
     * 反序列化(数组 -> 对象)
     * @param bytes
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bytes, Class<T> tClass) {
        try {
            T t = tClass.newInstance();
            Schema<T> schema = getSchema(tClass);
            ProtobufIOUtil.mergeFrom(bytes, t, schema);
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * 获取schema(jvm缓存提升性能)
     * @param obj
     * @param <T>
     * @return
     */
    private static <T> Schema<T> getSchema(Class<T> obj) {
        Schema<T> schema = (Schema<T>) cacheMap.get(obj);
        if (null == schema) {
            schema = RuntimeSchema.createFrom(obj);
            cacheMap.put(obj, schema);
        }
        return schema;
    }

}
