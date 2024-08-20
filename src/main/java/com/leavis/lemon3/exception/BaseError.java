package com.leavis.lemon3.exception;

/**
 * @Author: paynejlli
 * @Description: 错误通用接口
 * @Date: 2024/8/20 15:01
 */
public interface BaseError {

    /**
     * 获取错误码
     *
     * @return 错误代码
     */
    String getCode();

    /**
     * 获取错误描述
     *
     * @return 错误描述信息
     */
    String getMsg();
}
