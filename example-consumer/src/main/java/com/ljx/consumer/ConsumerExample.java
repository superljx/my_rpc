package com.ljx.consumer;

import com.ljx.common.model.User;
import com.ljx.common.service.UserService;
import com.ljx.proxy.ServiceProxyFactory;

public class ConsumerExample {
    public static void main(String[] args) {
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("ljx");
        User serviceUser = userService.getUser(user);
        if (serviceUser != null) {
            System.out.println(serviceUser.getName());
        } else {
            System.out.println("user == null");
        }
        short number = userService.getNumber();
        System.out.println(number);

        serviceUser = userService.getUser(user);
        if (serviceUser != null) {
            System.out.println(serviceUser.getName());
        } else {
            System.out.println("user == null");
        }
        number = userService.getNumber();
        System.out.println(number);

        serviceUser = userService.getUser(user);
        if (serviceUser != null) {
            System.out.println(serviceUser.getName());
        } else {
            System.out.println("user == null");
        }
        number = userService.getNumber();
        System.out.println(number);
    }
}
