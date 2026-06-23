package com.ljx.api;

import cn.hutool.core.io.resource.ResourceUtil;
import com.ljx.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI 加载器（支持键值对映射）
 */
@Slf4j
public class SpiLoader {

    /**
     * 存储已加载的类：接口名 -》（key -》 实现类）
     */
    private static final Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();

    /**
     * 对象实例缓存（避免重复创建） 类路径 -》 对象实例
     */
    private static final Map<String, Object> instanceCache = new ConcurrentHashMap<>();

    /**
     * 系统实现 SPI 目录
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用户自定义实现 SPI 目录
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    private static final String[] SCAN_DIRS = new String[] {RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};

    /**
     * 动态加载的类列表
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    public static void loadAll() {
        log.info("加载所有 SPI");
        for (Class<?> clazz : LOAD_CLASS_LIST) {
            load(clazz);
        }
    }

    /**
     * 获取某个接口的实例
     * @param tClass
     * @param key
     * @return
     * @param <T>
     */
    public static <T> T getInstance(Class<?> tClass, String key) {
        String tClassName = tClass.getName();

        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);
        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SPILoader 未加载类型: %s", tClassName));
        }
        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SPILoader 中 %s 不存在 key=%s 的类型", tClassName, key));
        }

        // 获取到要加载的实现类型
        Class<?> implClass = keyClassMap.get(key);
        String implClassName = implClass.getName();
        if (!instanceCache.containsKey(implClassName)) {
            try {
                instanceCache.put(implClassName, implClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                String errorMsg = String.format("%s 类实例化失败", implClassName);
                throw new RuntimeException(errorMsg, e);
            }
        }

        return (T) instanceCache.get(implClassName);
    }

    /**
     * 加载某个类型的 SPI
     * @param clazz
     * @return
     */
    public static Map<String, Class<?>> load(Class<?> clazz) {
        log.info("加载 SPI 类型: {}", clazz.getName());

        Map<String, Class<?>> keyClassMap = new HashMap<>();
        // 后遍历 custom 路径 -》 用户自定义的实现优先（系统实现类在 Map 中会被覆盖掉）
        for (String dir : SCAN_DIRS) {
            // 读取每个资源文件
            List<URL> resources = ResourceUtil.getResources(dir + clazz.getName());
            for (URL resource : resources) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] split = line.split("=");
                        if (split.length == 2) {
                            keyClassMap.put(split[0], Class.forName(split[1]));
                        }
                    }
                } catch (Exception e) {
                    log.error("spi resource load error", e);
                }
            }
        }

        loaderMap.put(clazz.getName(), keyClassMap);
        return keyClassMap;
    }
}
