package com.leavis.lemon3.service.impl;

import com.google.gson.Gson;
import com.leavis.lemon3.client.AmapClient;
import com.leavis.lemon3.client.config.AmapConfig;
import com.leavis.lemon3.client.model.WeatherForecastResponse;
import com.leavis.lemon3.service.AmapService;
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
    @Resource
    private AmapClient amapClient;

    @Cacheable(cacheNames = "weatherCache", unless = "#result == null || #result.trim().isEmpty()")
    @Override
    public String getWeatherInfo(String cityCode) {
        WeatherForecastResponse weatherForecastResponse = amapClient.weatherInfo(amapConfig.getApiKey(), cityCode);
        Gson gson = new Gson();
        return gson.toJson(weatherForecastResponse);
    }
}
