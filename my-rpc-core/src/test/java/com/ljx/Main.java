package com.ljx;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        List<String> servers = new ArrayList<>();
        servers.add("A");
        servers.add("B");
        servers.add("C");

        Map<String, AtomicInteger> connections = new ConcurrentHashMap<>();

        connections.put("A", new AtomicInteger(2));
        connections.put("B", new AtomicInteger(1));
        connections.put("C", new AtomicInteger(0));

        if (servers.isEmpty()) {
            throw new IllegalStateException("没有可用的服务器");
        }

        String selected = null;
        int minCount = Integer.MAX_VALUE;

        // 普通循环遍历，找出最小连接数（避免使用 Stream 和 Lambda）
        for (Map.Entry<String, AtomicInteger> entry : connections.entrySet()) {
            int current = entry.getValue().get();   // 读取当前连接数
            if (current < minCount) {
                minCount = current;
                selected = entry.getKey();
            }
        }

        // 选中后立即增加连接数（原子自增）
        if (selected != null) {
            connections.get(selected).incrementAndGet();
        }

        System.out.println(selected);
    }

}
