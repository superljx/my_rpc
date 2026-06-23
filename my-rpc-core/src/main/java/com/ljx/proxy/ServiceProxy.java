package com.ljx.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.ljx.RpcApplication;
import com.ljx.model.RpcRequest;
import com.ljx.model.RpcResponse;
import com.ljx.serializer.JdkSerializer;
import com.ljx.serializer.Serializer;
import com.ljx.serializer.SerializerFactory;


import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

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

        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            byte[] bytes = serializer.serialize(rpcRequest);
            // 发送请求
            // todo 硬编码了请求地址，需要用过注册中心解决
            try (HttpResponse post = HttpRequest.post("http://localhost:8081").body(bytes).execute()) {
                byte[] result = post.bodyBytes();
                RpcResponse deserialized = serializer.deserialize(result, RpcResponse.class);
                return deserialized.getData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
