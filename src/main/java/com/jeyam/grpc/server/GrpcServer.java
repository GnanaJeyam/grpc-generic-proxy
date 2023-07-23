package com.jeyam.grpc.server;

import com.jeyam.grpc.handler.GrpcRequestHandlerRegistry;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;

import static io.netty.util.NettyRuntime.availableProcessors;
import static java.lang.Runtime.getRuntime;

public class GrpcServer {
    private EventLoopGroup bossEventLoopGroup;
    private EventLoopGroup workerEventLoopGroup;
    private Server server;

    public GrpcServer() {
        this.bossEventLoopGroup = new NioEventLoopGroup();
        this.workerEventLoopGroup = new NioEventLoopGroup(availableProcessors());
        var inetSocketAddress = new InetSocketAddress("127.0.0.1", 8081);

        var nettyServerBuilder = NettyServerBuilder.forAddress(inetSocketAddress)
                .bossEventLoopGroup(bossEventLoopGroup)
                .workerEventLoopGroup(workerEventLoopGroup)
                .channelType(NioServerSocketChannel.class)
                .fallbackHandlerRegistry(new GrpcRequestHandlerRegistry());

        server = nettyServerBuilder.build();
    }

    public void start() throws IOException {
        server.start();
        System.out.println("Server started, listening on port : " + server.getPort());
        getRuntime().addShutdownHook(new Thread(this::stop));
    }

    private void stop() {
        if (!server.isShutdown()) {
            server.shutdown();
        }
        bossEventLoopGroup.shutdownGracefully();
        workerEventLoopGroup.shutdownGracefully();
    }
}
