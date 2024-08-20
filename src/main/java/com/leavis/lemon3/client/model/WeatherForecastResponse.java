package com.leavis.lemon3.client.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/**
 * @Author: paynejlli
 * @Description: 天气预报信息
 * @Date: 2024/8/20 11:38
 */
@Data
public class WeatherForecastResponse {

    private String status;

    private String count;

    private String info;

    private String infocode;

    @SerializedName("forecasts")
    private List<Forecast> forecasts;
}
