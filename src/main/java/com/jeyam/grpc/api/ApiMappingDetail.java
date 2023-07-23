package com.jeyam.grpc.api;

public class ApiMappingDetail {
    private String host;
    private Integer port;
    private String reqUrl;
    private String grpcServiceClass;
    private String grpcMethodResponseClass;
    private String grpcRequestMethodName;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public String getGrpcServiceClass() {
        return grpcServiceClass;
    }

    public void setGrpcServiceClass(String grpcServiceClass) {
        this.grpcServiceClass = grpcServiceClass;
    }

    public String getGrpcMethodResponseClass() {
        return grpcMethodResponseClass;
    }

    public void setGrpcMethodResponseClass(String grpcMethodResponseClass) {
        this.grpcMethodResponseClass = grpcMethodResponseClass;
    }

    public String getGrpcRequestMethodName() {
        return grpcRequestMethodName;
    }

    public void setGrpcRequestMethodName(String grpcRequestMethodName) {
        this.grpcRequestMethodName = grpcRequestMethodName;
    }

}
