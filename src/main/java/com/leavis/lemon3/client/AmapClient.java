package com.leavis.lemon3.client;

import com.leavis.lemon3.client.model.IpLocationRsp;
import com.leavis.lemon3.client.model.WeatherForecastResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: paynejlli
 * @Description: 高德 amap
 * @Date: 2024/8/20 11:35
 */
public interface AmapClient {

    /**
     * 获取天气预报
     * <a
     * href="https://restapi.amap.com/v3/weather/weatherInfo?key=67953f7ce7d657144d5f3b12f84310ed&city=110000&extensions=all">...</a>
     *
     * @param key apikey
     * @param city 城市编码，北京为110000
     * @return 天气信息
     */
    @RequestLine("GET /v3/weather/weatherInfo?key={key}&city={city}&extensions=all")
    @Headers({"Content-Type: " + MediaType.APPLICATION_JSON_VALUE})
    WeatherForecastResponse weatherInfo(@Param("key") String key, @Param("city") String city);

    /**
     * 获取IP定位
     * <a href="https://restapi.amap.com/v3/ip?key=67953f7ce7d657144d5f3b12f84310ed&ip=114.247.50.2&output=json">...</a>
     *
     * @param key apikey
     * @param ipv4 ipv4地址
     * @return IP定位信息
     */
    @RequestLine("GET /v3/ip?key={key}&ip={ipv4}&output=json")
    @Headers({"Content-Type: " + MediaType.APPLICATION_JSON_VALUE})
    IpLocationRsp ip2Location(@Param("key") String key, @Param("ipv4") String ipv4);

    /**
     * 上传文件示例
     *
     * @param file 文件
     */
    @RequestLine("POST /v1/DeviceModelScripts")
    @Headers({"Content-Type: " + MediaType.MULTIPART_FORM_DATA_VALUE})
    void uploadScript(@Param("file") MultipartFile file);
}