package com.leavis.lemon3.client.model;

import lombok.Data;

/**
 * @Author: paynejlli
 * @Description:
 * @Date: 2024/8/20 11:45
 */
@Data
class Cast {

    private String date;
    private String week;
    private String dayweather;
    private String nightweather;
    private String daytemp;
    private String nighttemp;
    private String daywind;
    private String nightwind;
    private String daypower;
    private String nightpower;
    private String daytemp_float;
    private String nighttemp_float;
}