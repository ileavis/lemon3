package com.leavis.lemon3.client.model;

import lombok.Data;

/**
 * @Author: paynejlli
 * @Description: 地理位置返回信息
 * @Date: 2024/8/29 16:38
 */
@Data
public class IpLocationRsp {

    private String status;
    private String info;
    private String infocode;
    private String province;
    private String city;
    private String adcode;
    private String rectangle;
}
