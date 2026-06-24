package com.ljx.provider;

import com.ljx.RpcApplication;
import com.ljx.common.service.UserService;
import com.ljx.config.RegistryConfig;
import com.ljx.config.RpcConfig;
import com.ljx.model.ServiceMetaInfo;
import com.ljx.provider.service.UserServiceImpl;
import com.ljx.registry.LocalRegistry;
import com.ljx.registry.Registry;
import com.ljx.registry.RegistryFactory;
import com.ljx.server.VertxHttpServer;

public class ProviderExample {
    public static void main(String[] args) {
//        RpcApplication.init();

        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());

        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        VertxHttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
