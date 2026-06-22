package com.ljx.server;

import com.ljx.model.RpcRequest;
import com.ljx.model.RpcResponse;
import com.ljx.registry.LocalRegistry;
import com.ljx.serializer.JdkSerializer;
import com.ljx.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * HTTP 请求处理器
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {
    /**
     * 处理请求
     * @param request
     */
    @Override
    public void handle(HttpServerRequest request) {
        // 获取序列化器
        Serializer serializer = new JdkSerializer();

        System.out.println("HttpServerHandler " + request.method() + " " + request.uri());

        // 获取完整请求体后异步处理请求
        request.bodyHandler(body -> {
            // 反序列获取请求对象
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                 rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            RpcResponse rpcResponse = new RpcResponse();
            if (rpcRequest == null) {
                rpcResponse.setMessage("RpcRequest is null");
                doResponse(request, rpcResponse, serializer);
                return ;
            }

            // 通过反射执行对应方法
            try {
                // 从本地注册器中获取对应的实现类
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
                rpcResponse.setData(result);
                rpcResponse.setMessage("ok");
                rpcResponse.setDataType(method.getReturnType());
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }

            doResponse(request, rpcResponse, serializer);
        });
    }

    /**
     * 响应请求
     * @param request
     * @param rpcResponse
     * @param serializer
     */
    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response().putHeader("content-type", "application/json");
        try {
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (Exception e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
