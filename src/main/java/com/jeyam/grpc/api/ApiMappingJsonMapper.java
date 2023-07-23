package com.jeyam.grpc.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class ApiMappingJsonMapper {

    private static final Map<String, ApiMappingDetail> grpcStubCache = new ConcurrentHashMap<>();

    public static void loadMappings() throws IOException {
        var stream = ApiMappingJsonMapper.class.getResourceAsStream("/apimapping.json");
        var objectMapper = new ObjectMapper();
        var apiMapping = objectMapper.readValue(stream, ApiMapping.class);
        apiMapping.getApiMappingDetails().forEach(grpcAndRestApiDetail ->
                grpcStubCache.put(grpcAndRestApiDetail.getGrpcRequestMethodName(), grpcAndRestApiDetail));
    }

    public static ApiMappingDetail getGrpcAndRestApiDetail(String grpcRequestMethodName) {
        return Optional.ofNullable(grpcStubCache.get(grpcRequestMethodName))
            .orElseThrow(() -> new IllegalArgumentException("Invalid grpc method name not found!"));
    }
}
