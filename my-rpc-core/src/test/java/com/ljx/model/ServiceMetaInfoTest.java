package com.ljx.model;

import junit.framework.TestCase;

public class ServiceMetaInfoTest extends TestCase {

    public void testGetServiceKeyFromNodeKey() {
        System.out.println(ServiceMetaInfo.getServiceKeyFromNodeKey("/rpc/myService:1.0/localhost:8080"));
    }
}