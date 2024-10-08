package com.leavis.lemon3.service.impl;

import com.google.gson.Gson;
import com.leavis.lemon3.client.AmapClient;
import com.leavis.lemon3.client.config.AmapConfig;
import com.leavis.lemon3.client.model.IpLocationRsp;
import com.leavis.lemon3.client.model.WeatherForecastResponse;
import com.leavis.lemon3.exception.BizException;
import com.leavis.lemon3.enums.ErrorCodeEnum;
import com.leavis.lemon3.service.AmapService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Author: paynejlli
 * @Description: 高德
 * @Date: 2024/8/20 11:48
 */
@Slf4j
@Service
public class AmapServiceImpl implements AmapService {

    @Resource
    private AmapConfig amapConfig;
    @Resource
    private AmapClient amapClient;

    private final Gson gson = new Gson();

    @Cacheable(cacheNames = "weatherCache", unless = "#result == null || #result.trim().isEmpty()")
    @Override
    public String getWeatherInfo(String cityCode) {
        try {
            WeatherForecastResponse weatherForecastResponse = amapClient.weatherInfo(amapConfig.getApiKey(), cityCode);
            return gson.toJson(weatherForecastResponse);
        } catch (Exception e) {
            log.error("Failed to get weather info for city code: {}", cityCode, e);
            throw new BizException(ErrorCodeEnum.FAILED, e);
        }
    }

    @Cacheable(cacheNames = "ip2LocationCache", unless = "#result == null || #result.trim().isEmpty()")
    @Override
    public String ip2Location(String ipv4) {
        ipv4 = ipv4.trim();
        try {
            IpLocationRsp ipLocationRsp = amapClient.ip2Location(amapConfig.getApiKey(), ipv4);
            return ipLocationRsp.getProvince() + "-" + ipLocationRsp.getCity();
        } catch (Exception e) {
            log.error("Failed to get ip location for ipv4: {}", ipv4, e);
            throw new BizException(ErrorCodeEnum.FAILED, e);
        }
    }
}