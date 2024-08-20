package com.leavis.lemon3.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leavis.lemon3.bo.UserBO;
import com.leavis.lemon3.mapper.UserMapper;
import com.leavis.lemon3.entity.User;
import com.leavis.lemon3.service.UserService;
import jakarta.annotation.Resource;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @Author: paynejlli
 * @Description: 用户 service
 * @Date: 2024/8/13 11:54
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Page<UserBO> getUserPage(Integer page, Integer pageSize) {
        Page<User> userPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        userPage = userMapper.selectPage(userPage, queryWrapper);
        Page<UserBO> userBOPage = new Page<>();
        BeanUtils.copyProperties(userPage, userBOPage);
        if (CollectionUtils.isEmpty(userBOPage.getRecords())) {
            return userBOPage;
        }
        userBOPage.setRecords(userPage.getRecords().stream().map(e -> {
                    UserBO userBO = new UserBO();
                    BeanUtils.copyProperties(e, userBO);
                    return userBO;
                }
        ).collect(Collectors.toList()));
        return userBOPage;
    }
}