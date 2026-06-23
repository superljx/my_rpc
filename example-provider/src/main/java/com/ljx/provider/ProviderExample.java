package com.ljx.provider;

import com.ljx.RpcApplication;
import com.ljx.common.service.UserService;
import com.ljx.provider.service.UserServiceImpl;
import com.ljx.registry.LocalRegistry;
import com.ljx.server.VertxHttpServer;

public class ProviderExample {
    public static void main(String[] args) {
//        RpcApplication.init();

        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        VertxHttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
