package com.leavis.lemon3.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leavis.lemon3.bo.UserBO;

/**
 * @Author: paynejlli
 * @Description: 用户 Service
 * @Date: 2024/8/13 11:52
 */
public interface UserService {

    /**
     * 分页获取用户信息
     *
     * @param page 页码
     * @param pageSize 每页大小
     * @return 分页数据
     */
    Page<UserBO> getUserPage(Integer page, Integer pageSize);
}
