package com.leavis.lemon3.service;

/**
 * @Author: paynejlli
 * @Description: 长链接业务处理
 * @Date: 2024/9/6 16:01
 */
public interface WsBusinessService {

    /**
     * 处理用户消息
     *
     * @param userId 用户 id
     * @param msg 消息
     * @return
     */
    String handleUserMsg(String userId, String msg);
}
