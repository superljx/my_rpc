package com.ljx.consumer;

import com.ljx.common.model.User;
import com.ljx.common.service.UserService;
import com.ljx.proxy.ServiceProxy;
import com.ljx.proxy.ServiceProxyFactory;

/**
 * 简易服务消费者示例
 */
public class EasyConsumerExample {

    public static void main(String[] args) {
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
//        UserService userService = new UserServiceProxy();
        User user = new User();
        user.setName("yupi");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
