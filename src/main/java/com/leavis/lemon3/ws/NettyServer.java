package com.leavis.lemon3.ws;

import com.leavis.lemon3.config.NettyConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: paynejlli
 * @Description: netty服务器
 * @Date: 2024/9/4 09:43
 */
@Slf4j
@Component
public class NettyServer implements Runnable {

    @Autowired
    private NettyConfig nettyConfig;

    @Autowired
    private NettyInitializer nettyInitializer;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    @Override
    public void run() {
        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        // bossGroup辅助客户端的tcp连接请求, workGroup负责与客户端之前的读写操作
        bootstrap.group(bossGroup, workGroup);
        // 设置NIO类型的channel
        bootstrap.channel(NioServerSocketChannel.class);
        // 设置监听端口
        bootstrap.localAddress(new InetSocketAddress(nettyConfig.getPort()));
        // 设置管道
        bootstrap.childHandler(nettyInitializer);
        // 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
        ChannelFuture channelFuture;
        try {
            channelFuture = bootstrap.bind().sync();
            log.info("Server started and listen on:{}", channelFuture.channel().localAddress());
            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 重新设置中断状态
            log.error("Server interrupted while starting", e);
        } finally {
            this.destroy();
        }
    }

    public void destroy() {
        this.shutdownEventLoopGroup(bossGroup);
        this.shutdownEventLoopGroup(workGroup);
    }

    private void shutdownEventLoopGroup(EventLoopGroup eventLoopGroup) {
        if (eventLoopGroup != null) {
            try {
                eventLoopGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 重新设置中断状态
                log.info("Interrupted while shutting down event loop group", e);
            }
        }
    }
}