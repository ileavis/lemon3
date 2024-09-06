package com.leavis.lemon3.ws;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: paynejlli
 * @Description: netty配置
 * @Date: 2024/9/3 18:04
 */
public class ChannelHolder {

    // 定义全局单例ChannelGroup，用于管理所有的Channel
    private static volatile ChannelGroup channelGroup = null;

    // 存放用户ID与Channel的对应关系，便于根据用户ID快速找到对应的Channel
    private static volatile ConcurrentHashMap<String, Channel> channelMap = null;

    // 用于保护channelGroup的初始化操作
    private static final ReentrantLock lock1 = new ReentrantLock();
    // 用于保护channelMap的初始化操作
    private static final ReentrantLock lock2 = new ReentrantLock();

    /**
     * 获取全局ChannelGroup实例，使用双重检查锁定确保线程安全
     *
     * @return ChannelGroup实例
     */
    public static ChannelGroup getChannelGroup() {
        if (null == channelGroup) {
            lock1.lock();
            try {
                if (null == channelGroup) {
                    channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
                }
            } finally {
                lock1.unlock();
            }
        }
        return channelGroup;
    }

    /**
     * 获取存放请求ID与Channel对应关系的ConcurrentHashMap实例，使用双重检查锁定确保线程安全
     *
     * @return ConcurrentHashMap实例
     */
    public static ConcurrentHashMap<String, Channel> getChannelMap() {
        if (null == channelMap) {
            lock2.lock();
            try {
                if (null == channelMap) {
                    channelMap = new ConcurrentHashMap<>();
                }
            } finally {
                lock2.unlock();
            }
        }
        return channelMap;
    }

    /**
     * 根据用户ID获取对应的Channel
     *
     * @param userId 用户ID
     * @return 对应的Channel，如果不存在则返回null
     */
    public static Channel getChannel(String userId) {
        if (null == channelMap) {
            return getChannelMap().get(userId);
        }
        return channelMap.get(userId);
    }
}
