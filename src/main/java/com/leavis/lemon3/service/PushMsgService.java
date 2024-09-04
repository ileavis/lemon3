package com.leavis.lemon3.service;

/**
 * @Author: paynejlli
 * @Description: 推送消息service
 * @Date: 2024/9/3 18:02
 */
public interface PushMsgService {

    /**
     * 推送给指定用户
     */
    void pushMsgToOne(String userId, String msg);

    /**
     * 推送给所有用户
     */
    void pushMsgToAll(String msg);
}
