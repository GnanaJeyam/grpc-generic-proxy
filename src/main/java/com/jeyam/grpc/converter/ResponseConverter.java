package com.jeyam.grpc.converter;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.util.JsonFormat;

public class ResponseConverter {

    public DynamicMessage convert(String grpcMethodResponseClass, String response) throws Exception {
        // Loading the incoming gRPC response type class and constructing the DynamicMessage
        var aClass = Class.forName(grpcMethodResponseClass);
        var descriptor = (Descriptors.Descriptor) aClass
                .getDeclaredMethod("getDescriptor", null)
                .invoke(null);
        var dynamicMessage = DynamicMessage.newBuilder(descriptor);
        JsonFormat.parser().merge(response, dynamicMessage);

        return dynamicMessage.build();
    }
}
