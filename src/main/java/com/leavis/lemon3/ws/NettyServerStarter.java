package com.leavis.lemon3.ws;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: paynejlli
 * @Description: netty服务启动器
 * @Date: 2024/9/4 09:43
 */
@Slf4j
@Component
public class NettyServerStarter {

    @Autowired
    private NettyServer nettyServer;

    private ExecutorService executorService;

    @PostConstruct
    public void start() {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(nettyServer);
    }

    @PreDestroy
    public void stop() {
        if (executorService != null) {
            executorService.shutdown();
            log.info("Netty server stopped.");
        }
    }
}
