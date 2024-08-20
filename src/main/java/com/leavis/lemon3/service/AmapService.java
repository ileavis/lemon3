package com.leavis.lemon3.service;

/**
 * @Author: paynejlli
 * @Description: 高德 service
 * @Date: 2024/8/20 11:47
 */
public interface AmapService {

    /**
     * 获取天气信息
     *
     * @param cityCode 地理位置，北京为110000
     * @return 天气信息，懒得搞那么多 pojo了，直接返回天气预报信息为一个字符串了
     */
    String getWeatherInfo(String cityCode);
}
