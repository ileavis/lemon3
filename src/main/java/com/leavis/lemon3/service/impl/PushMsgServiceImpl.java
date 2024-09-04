package com.leavis.lemon3.service.impl;

import com.leavis.lemon3.config.NettyConfig;
import com.leavis.lemon3.exception.BizException;
import com.leavis.lemon3.service.PushMsgService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.util.Objects;
import org.springframework.stereotype.Service;

/**
 * @Author: paynejlli
 * @Description: pushMsgService
 * @Date: 2024/9/3 18:03
 */
@Service
public class PushMsgServiceImpl implements PushMsgService {

    @Override
    public void pushMsgToOne(String userId, String msg) {
        Channel channel = NettyConfig.getChannel(userId);
        if (Objects.isNull(channel)) {
            BizException.throwException("当前用户不在线");
        }
        channel.writeAndFlush(new TextWebSocketFrame(msg));
    }

    @Override
    public void pushMsgToAll(String msg) {
        if(NettyConfig.getChannelMap().isEmpty()) {
            BizException.throwException("没有用户在线，消息发送失败");
        }
        NettyConfig.getChannelGroup().writeAndFlush(new TextWebSocketFrame(msg));
    }
}
