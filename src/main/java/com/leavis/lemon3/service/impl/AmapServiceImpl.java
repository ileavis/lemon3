package com.leavis.lemon3.service.impl;

import com.google.gson.Gson;
import com.leavis.lemon3.client.AmapClient;
import com.leavis.lemon3.client.config.AmapConfig;
import com.leavis.lemon3.client.model.WeatherForecastResponse;
import com.leavis.lemon3.service.AmapService;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.httpclient.ApacheHttpClient;
import feign.slf4j.Slf4jLogger;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Author: paynejlli
 * @Description: 高德
 * @Date: 2024/8/20 11:48
 */
@Service
public class AmapServiceImpl implements AmapService {

    @Resource
    private AmapConfig amapConfig;

    @Cacheable(cacheNames = "weatherCache", unless = "#result == null || #result.trim().isEmpty()")
    @Override
    public String getWeatherInfo(String cityCode) {
        WeatherForecastResponse weatherForecastResponse = this.getAmapClient()
                .weatherInfo(amapConfig.getApiKey(), cityCode);
        Gson gson = new Gson();
        return gson.toJson(weatherForecastResponse);
    }

    /**
     * 获取feignClient
     *
     * @return amapClient
     */
    private AmapClient getAmapClient() {
        return Feign.builder()
                .client(new ApacheHttpClient())
                .options(new Request.Options(amapConfig.getTimeout(), amapConfig.getReadTime()))
                .logger(new Slf4jLogger(AmapClient.class))
                .logLevel(Logger.Level.FULL)
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(AmapClient.class, amapConfig.getDomain());
    }

}
