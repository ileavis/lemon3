package com.leavis.lemon3.client.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @Author: paynejlli
 * @Description:
 * @Date: 2024/8/20 11:42
 */
class Forecast {

    private String city;
    private String adcode;
    private String province;
    private String reporttime;

    @SerializedName("casts")
    private List<Cast> casts;
}