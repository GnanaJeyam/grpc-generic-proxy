package com.jeyam.grpc.handler;

import com.jeyam.grpc.helper.RequestHelper;
import com.jeyam.grpc.converter.ResponseConverter;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.Status;
import io.grpc.stub.ServerCallStreamObserver;

import static com.jeyam.grpc.api.ApiMappingJsonMapper.getGrpcAndRestApiDetail;

/**
 * This is gRPC server call handler class that will construct the DynamicMessage
 * based on gRPC method response class. Eventually we will map the json result with the DynamicMessage
 * and send it back to the gRPC client as a response. Right now json object is hardcoded but future
 * you can replace it any response from API. The contract should be schema for the grpc method response
 * class and response from any REST API should be same.
 * @param <Req>
 * @param <Res>
 */
public class GrpcServerCallHandler<Req,Res> implements ServerCallHandler<Req, Res> {

    @Override
    public ServerCall.Listener<Req> startCall(ServerCall<Req, Res> call, Metadata headers) {

        final ServerCallStreamObserverImpl<Req, Res> responseObserver = new ServerCallStreamObserverImpl<>(call);
        var methodDescriptor = call.getMethodDescriptor();
        var grpcServiceDetail = getGrpcAndRestApiDetail(methodDescriptor.getFullMethodName());
        var grpcMethodResponseClass = grpcServiceDetail.getGrpcMethodResponseClass();

        call.request(1);
        call.sendHeaders(headers);

        return new ServerCall.Listener<>() {
            private Req message;
            @Override
            public void onMessage(Req message) {

                super.onMessage(message);
                this.message = message;
            }

            @Override
            public void onHalfClose() {
                try {
                    var requestHelper = new RequestHelper(grpcServiceDetail);
                    var response = new ResponseConverter().convert(grpcMethodResponseClass, requestHelper.getResponse());
                    responseObserver.onNext((Res) response);
                } catch (Exception e) {
                    e.printStackTrace();
                    responseObserver.onError(e);
                }
                responseObserver.onCompleted();
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }

            @Override
            public void onReady() {
                super.onReady();
            }
        };
    }

    private static final class ServerCallStreamObserverImpl<ReqT, RespT> extends ServerCallStreamObserver<RespT> {
        private final ServerCall<ReqT, RespT> call;

        public ServerCallStreamObserverImpl(ServerCall<ReqT, RespT> call) {
            this.call = call;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public void setOnCancelHandler(Runnable onCancelHandler) {

        }

        @Override
        public void setCompression(String compression) {

        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setOnReadyHandler(Runnable onReadyHandler) {

        }

        @Override
        public void disableAutoInboundFlowControl() {

        }

        @Override
        public void request(int count) {
            call.request(count);
        }

        @Override
        public void setMessageCompression(boolean enable) {

        }

        @Override
        public void onNext(RespT value) {
            call.sendMessage(value);
        }

        @Override
        public void onError(Throwable t) {
            System.out.println(t);
            call.close(Status.ABORTED, new Metadata());
        }

        @Override
        public void onCompleted() {
            call.close(Status.OK, new Metadata());
        }
    }
}
