package com.leavis.lemon3.bo;

import lombok.Data;

/**
 * @Author: paynejlli
 * @Description: 用户信息
 * @Date: 2024/8/13 11:53
 */
@Data
public class UserBO {

    private Long id;
    private String name;
    private Integer age;
    private String email;
}
