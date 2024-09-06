package com.leavis.lemon3.ws;

import com.leavis.lemon3.config.NettyConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: paynejlli
 * @Description: netty初始化器
 * @Date: 2024/9/4 09:42
 */
@Slf4j
@Component
public class NettyInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private NettyConfig serverConfig;

    @Autowired
    private CustomChannelHandler customChannelHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        log.info("Initializing channel for socket: {}", socketChannel);
        // 设置管道
        ChannelPipeline pipeline = socketChannel.pipeline();
        if (serverConfig.getSsl().getEnabled()) {
            SSLContext sslContext = createSSLContext(serverConfig.getSsl().getType(),
                    serverConfig.getSsl().getKeyStore(),
                    serverConfig.getSsl().getKeyStorePassword());
            SSLEngine engine = sslContext.createSSLEngine();
            pipeline.addLast(new SslHandler(engine));
        }
        // 添加心跳支持
        pipeline.addLast(new IdleStateHandler(serverConfig.getHeartBeatTimeout(), 0, 0, TimeUnit.SECONDS));
        // webSocket协议本身是基于http协议的，所以这边也要使用http编解码器
        pipeline.addLast(new HttpServerCodec());
        // 以块的方式来写的处理器
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new ChunkedWriteHandler());
        // 自定义的handler，处理业务逻辑
        pipeline.addLast(customChannelHandler);
    }

    /**
     * 创建SSL上下文
     *
     * @param type       密钥库类型，例如JKS
     * @param path       密钥库文件路径
     * @param password   密钥库密码
     * @return SSL上下文实例
     * @throws Exception 如果创建SSL上下文失败，抛出异常
     */
    private SSLContext createSSLContext(String type, String path, String password) throws Exception {
        KeyStore ks = KeyStore.getInstance(type);
        try (InputStream ksInputStream = new FileInputStream(path)) {
            ks.load(ksInputStream, password.toCharArray());
            // KeyManagerFactory充当基于密钥内容源的密钥管理器的工厂。
            // getDefaultAlgorithm:获取默认的 KeyManagerFactory 算法名称。
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password.toCharArray());
            // SSLContext的实例表示安全套接字协议的实现，它充当用于安全套接字工厂或 SSLEngine 的工厂。
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);
            SSLContext.setDefault(sslContext);
            return sslContext;
        }
    }
}