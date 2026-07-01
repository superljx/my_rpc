package com.ljx.registry;

import com.ljx.model.ServiceMetaInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册中心服务本地缓存
 */
@Slf4j
public class RegistryServiceCache {
    /**
     * 服务缓存
     */
    private final Map<String, List<ServiceMetaInfo>> serviceCache = new ConcurrentHashMap<>();

    /**
     * 写入缓存
     * @param serviceKey 服务键名
     * @param newServiceCache 更新后的缓存列表
     */
    void writeCache(String serviceKey, List<ServiceMetaInfo> newServiceCache) {
        log.info("写入缓存：{}", serviceKey);
        this.serviceCache.put(serviceKey, newServiceCache);
    }

    /**
     * 读取缓存
     * @param serviceKey 服务键名
     * @return 缓存列表
     */
    List<ServiceMetaInfo> readCache(String serviceKey) {
        log.info("读取缓存：{}", serviceKey);
        return this.serviceCache.get(serviceKey);
    }

    /**
     * 清空缓存
     */
    void clearCache(String serviceKey) {
        this.serviceCache.remove(serviceKey);
    }
}
