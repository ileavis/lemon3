package com.leavis.lemon3.service.impl;

import com.leavis.lemon3.exception.BizException;
import com.leavis.lemon3.service.PushMsgService;
import com.leavis.lemon3.ws.ChannelHolder;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: paynejlli
 * @Description: pushMsgService
 * @Date: 2024/9/3 18:03
 */
@Slf4j
@Service
public class PushMsgServiceImpl implements PushMsgService {

    @Override
    public void pushMsgToOne(String userId, String msg) {
        Channel channel = ChannelHolder.getChannel(userId);
        if (Objects.isNull(channel)) {
            BizException.throwException("当前用户不在线");
        }
        channel.writeAndFlush(new TextWebSocketFrame(msg));
    }

    @Override
    public void pushMsgToAll(String msg) {
        if (ChannelHolder.getChannelMap().isEmpty()) {
            BizException.throwException("没有用户在线，消息发送失败");
        }
        ChannelGroup channelGroup = ChannelHolder.getChannelGroup();
        log.info("已推送用户数：{}", channelGroup.size());
        channelGroup.writeAndFlush(new TextWebSocketFrame(msg));
    }
}
