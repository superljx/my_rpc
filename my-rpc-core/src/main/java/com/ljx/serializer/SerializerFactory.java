package com.ljx.serializer;

import com.ljx.api.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化器工厂（获取序列化器对象）
 */
public class SerializerFactory {

    static {
        SpiLoader.load(Serializer.class);
    }

//    /**
//     * 序列化器映射
//     */
//    private static final Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<String, Serializer>()
//    {{
//        put(SerializerKeys.JDK, new JdkSerializer());
//        put(SerializerKeys.JSON, new JsonSerializer());
//        put(SerializerKeys.HESSIAN, new HessianSerializer());
//        put(SerializerKeys.KRYO, new KryoSerializer());
//    }};

    /**
     * 默认序列化器
     */
//    private static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get(SerializerKeys.JDK);
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取序列化器对象
     * @param key
     * @return
     */
    public static Serializer getInstance(String key) {
//        return KEY_SERIALIZER_MAP.getOrDefault(key, DEFAULT_SERIALIZER);
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
