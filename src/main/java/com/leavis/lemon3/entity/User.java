package com.leavis.lemon3.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @Author: paynejlli
 * @Description: 用户实体类
 * @Date: 2024/8/13 11:51
 */
@TableName("t_user")
@Data
public class User {

    private Long id;

    private String name;

    private Integer age;

    private String email;

    private Boolean isDelete;

    private Date createTime;

    private Date modifyTime;
}