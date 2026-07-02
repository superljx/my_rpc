package com.ljx.proxy;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.ljx.RpcApplication;
import com.ljx.config.RpcConfig;
import com.ljx.constant.RpcConstant;
import com.ljx.model.RpcRequest;
import com.ljx.model.RpcResponse;
import com.ljx.model.ServiceMetaInfo;
import com.ljx.protocol.*;
import com.ljx.registry.Registry;
import com.ljx.registry.RegistryFactory;
import com.ljx.serializer.JdkSerializer;
import com.ljx.serializer.Serializer;
import com.ljx.serializer.SerializerFactory;
import com.ljx.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;


import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 服务动态代理（JDK 动态代理实现）
 */
public class ServiceProxy implements InvocationHandler {
    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        RpcResponse rpcResponse;
        try {
            byte[] bytes = serializer.serialize(rpcRequest);
            // 发送请求
            // 用注册中心解决获取服务提供者地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(rpcRequest.getServiceName());
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);

            List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());

            if (serviceMetaInfos.isEmpty()) {
                throw new RuntimeException("暂无服务地址");
            }
            // todo 服务节点地址后续可能有多个，现在暂时取第一个
            ServiceMetaInfo metaInfo = serviceMetaInfos.get(0);

            // 发送 TCP 请求
            rpcResponse = VertxTcpClient.doRequest(rpcRequest, metaInfo);
        } catch (IOException e) {
            throw new RuntimeException("调用失败");
        }
        return rpcResponse.getData();
    }
}
