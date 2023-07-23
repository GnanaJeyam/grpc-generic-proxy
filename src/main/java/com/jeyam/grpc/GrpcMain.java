package com.jeyam.grpc;

import com.jeyam.grpc.api.ApiMappingJsonMapper;
import com.jeyam.grpc.server.GrpcServer;

import java.io.IOException;

public class GrpcMain {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello from Grpc Main");
        // Loading the mapping json file
        ApiMappingJsonMapper.loadMappings();

        new GrpcServer().start();
    }
}
