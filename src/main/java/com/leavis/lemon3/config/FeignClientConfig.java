package com.leavis.lemon3.config;

import com.leavis.lemon3.client.AmapClient;
import com.leavis.lemon3.client.config.AmapConfig;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.httpclient.ApacheHttpClient;
import feign.slf4j.Slf4jLogger;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: paynejlli
 * @Description: feignClient集中初始化
 * @Date: 2024/8/23 16:55
 */
@Configuration
public class FeignClientConfig {

    @Resource
    private AmapConfig amapConfig;

    @Bean
    public AmapClient amapClient() {
        return Feign.builder()
                .client(new ApacheHttpClient())
                .options(new Request.Options(amapConfig.getTimeout(), amapConfig.getReadTime(), true))
                .logger(new Slf4jLogger(AmapClient.class))
                .logLevel(Logger.Level.FULL)
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(AmapClient.class, amapConfig.getDomain());
    }
}
