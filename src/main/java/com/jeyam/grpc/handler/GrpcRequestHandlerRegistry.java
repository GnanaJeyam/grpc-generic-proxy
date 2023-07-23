package com.jeyam.grpc.handler;

import com.jeyam.grpc.handler.GrpcServerCallHandler;
import com.jeyam.grpc.util.GrpcUtil;
import io.grpc.*;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;

import static com.jeyam.grpc.api.ApiMappingJsonMapper.getGrpcAndRestApiDetail;

/**
 * This is fallback registry call where we will construct ServerMethodDefinition
 * based on incoming grpc method name.
 * This class utilize the ApiMappingJsonMapper to retrieve the incoming GRPC Service class
 * and GRPC Method response class.
 */
public class GrpcRequestHandlerRegistry extends HandlerRegistry {
    private static final ConcurrentHashMap<String, ServerMethodDefinition> serverMethodDefinitions = new ConcurrentHashMap<>();

    @Nullable
    @Override
    public ServerMethodDefinition<?, ?> lookupMethod(String methodName, @Nullable String authority) {
        if (serverMethodDefinitions.containsKey(methodName)) {
            return serverMethodDefinitions.get(methodName);
        }

        var grpcAndRestApiDetail = getGrpcAndRestApiDetail(methodName);
        try {
            var serviceDescriptor = GrpcUtil.getServiceDescriptor(grpcAndRestApiDetail.getGrpcServiceClass());
            var builder = ServerServiceDefinition.builder(serviceDescriptor);
            for (MethodDescriptor<?, ?> methodDescriptor: serviceDescriptor.getMethods()) {
                builder.addMethod(methodDescriptor, new GrpcServerCallHandler<>());
            }
            var serverServiceDefinition = builder.build();
            // Adding in the cache
            for (ServerMethodDefinition serverMethodDefinition: serverServiceDefinition.getMethods()) {
                serverMethodDefinitions.put(serverMethodDefinition.getMethodDescriptor().getFullMethodName(), serverMethodDefinition);
            }

            return serverMethodDefinitions.get(methodName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
