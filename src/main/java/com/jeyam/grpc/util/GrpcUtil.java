package com.jeyam.grpc.util;

import io.grpc.ServiceDescriptor;

public class GrpcUtil {
    private GrpcUtil(){

    }

    public static ServiceDescriptor getServiceDescriptor(String className) throws Exception{
        Class<?> serviceClass = Class.forName(className);
        return (ServiceDescriptor) serviceClass.getMethod("getServiceDescriptor", null).invoke(null, null);
    }
}
