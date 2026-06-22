package com.ljx.provider;

import com.ljx.common.service.UserService;
import com.ljx.provider.service.UserServiceImpl;
import com.ljx.registry.LocalRegistry;
import com.ljx.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        VertxHttpServer server = new VertxHttpServer();
        server.doStart(8080);
    }
}
