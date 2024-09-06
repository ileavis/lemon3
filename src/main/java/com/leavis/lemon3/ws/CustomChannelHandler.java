package com.leavis.lemon3.ws;

import com.leavis.lemon3.service.WsBusinessService;
import com.leavis.lemon3.utils.UriUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import java.net.URISyntaxException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: paynejlli
 * @Description: 自定义 WebSocket 处理器
 * @Date: 2024/9/3 18:10
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class CustomChannelHandler extends SimpleChannelInboundHandler<Object> {

    private static final String HEART_BEAT = "PING";
    private static final String WEB_SOCKET = "websocket";
    private static final String UPGRADE = "Upgrade";
    private static final String WS_PATH = "/ws";
    private WebSocketServerHandshaker handshaker;

    @Autowired
    private WsBusinessService wsBusinessService;

    /**
     * 当有新的客户端连接时调用
     *
     * @param ctx ChannelHandlerContext 上下文对象
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("[新连接] 有新的客户端链接 channelId：[{}]", ctx.channel().id());
        // 添加到channelGroup 通道组
        ChannelHolder.getChannelGroup().add(ctx.channel());
    }

    /**
     * 处理接收到的消息
     *
     * @param ctx ChannelHandlerContext 上下文对象
     * @param msg 接收到的消息对象
     * @throws Exception 可能抛出的异常
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 首次websocket接入或http接入
        if (msg instanceof FullHttpRequest) {
            this.handleHttpRequest(ctx, ((FullHttpRequest) msg));
        } else if (msg instanceof WebSocketFrame) {
            this.handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    /**
     * 当通道变为非活动状态时调用
     *
     * @param ctx ChannelHandlerContext 上下文对象
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("[连接关闭] channelId {}, userId {}", ctx.channel().id(), this.getUserId(ctx));
        // 删除通道
        ChannelHolder.getChannelGroup().remove(ctx.channel());
        removeUserId(ctx);
    }

    /**
     * 当捕获到异常时调用
     *
     * @param ctx ChannelHandlerContext 上下文对象
     * @param cause 异常对象
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("[异常捕获] channelId {}, userId {}, 异常信息：{}", ctx.channel().id(), this.getUserId(ctx),
                cause.getMessage());
        // 删除通道
        ChannelHolder.getChannelGroup().remove(ctx.channel());
        removeUserId(ctx);
        ctx.close();
    }

    /**
     * 当通道读操作完成时调用
     *
     * @param ctx ChannelHandlerContext 上下文对象
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * 当触发用户事件时调用
     *
     * @param ctx ChannelHandlerContext 上下文对象
     * @param evt 触发的事件对象
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("[释放不活跃通道] channelId {}, userId {}", ctx.channel().id(), this.getUserId(ctx));
                TextWebSocketFrame tws = new TextWebSocketFrame(String.format("[释放不活跃通道] userId :[%s] 超时自动断开连接", this.getUserId(ctx)));
                ctx.channel().writeAndFlush(tws);
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * 处理 HTTP 请求
     *
     * @param ctx ChannelHandlerContext 上下文对象
     * @param req FullHttpRequest HTTP 请求对象
     * @throws URISyntaxException URI 解析异常
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws URISyntaxException {
        // 如果HTTP解码失败，返回HTTP异常
        if (!req.getDecoderResult().isSuccess() || (!WEB_SOCKET.equals(req.headers().get(UPGRADE)))) {
            sendHttpResponse(ctx, req,
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        // 校验报文协议，协议不正确则断开连接
        String uri = req.uri();
        String path = UriUtils.getPath(uri);
        Map<String, String> params = UriUtils.extractParams(uri);
        String token = params.get("token");
        if (StringUtils.isBlank(token) || !WS_PATH.equals(path)) {
            log.error("[处理HTTP请求] channelId {},正确的格式示例为/ws?token=xxx,实际请求uri: {}", ctx.channel().id(),
                    uri);
            ctx.close();
            return;
        }
        if (!checkToken(token)) {
            log.error("[鉴权失败] channelId {}, token: {}", ctx.channel().id(), token);
            ctx.close();
            return;
        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("", null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
        String userId = token; // 这里假设用户ID是token
        ChannelHolder.getChannelMap().put(userId, ctx.channel());
        // 将用户ID作为自定义属性加入到channel中，方便随时channel中获取用户ID
        ctx.channel().attr(AttributeKey.valueOf("userId")).setIfAbsent(userId);
        log.info("[连接建立成功] channelId {}, userId {}", ctx.channel().id(), this.getUserId(ctx));
    }

    /**
     * 校验 token 是否有效
     *
     * @param token 要校验的 token
     * @return token 是否有效
     */
    private boolean checkToken(String token) {
        // 实际应用中应进行token校验
        return true;
    }

    /**
     * 发送 HTTP 响应
     *
     * @param ctx ChannelHandlerContext 上下文对象
     * @param req FullHttpRequest HTTP 请求对象
     * @param res DefaultFullHttpResponse HTTP 响应对象
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.getStatus().code() != HttpResponseStatus.OK.code()) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            // 手工释放，避免内存泄露
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != HttpResponseStatus.OK.code()) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 处理 WebSocket 消息帧
     *
     * @param ctx ChannelHandlerContext 上下文对象
     * @param frame WebSocketFrame 消息帧对象
     */
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 关闭webSocket的指令
        if (frame instanceof CloseWebSocketFrame) {
            log.info("[主动关闭] channelId {}, userId {}", ctx.channel().id(), this.getUserId(ctx));
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        } else if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        } else if (!(frame instanceof TextWebSocketFrame)) {
            log.warn("[不支持的消息类型] channelId {}, userId {}", ctx.channel().id(), this.getUserId(ctx));
            throw new RuntimeException("【" + this.getClass().getName() + "】不支持的二进制消息");
        }
        // 获取文本消息
        String request = ((TextWebSocketFrame) frame).text();
        if (HEART_BEAT.equals(request)) {
            log.debug("[心跳请求] channelId {}, userId {}, {}", ctx.channel().id(), this.getUserId(ctx), request);
            return;
        }
        log.info("[收到消息] channelId{}, userId {} : {}", ctx.channel().id(), this.getUserId(ctx), request);
        String rsp = wsBusinessService.handleUserMsg(this.getUserId(ctx), request);
        TextWebSocketFrame tws = new TextWebSocketFrame(rsp);
        // 回复消息
        ctx.channel().writeAndFlush(tws);
    }

    /**
     * 获取与通道关联的用户 ID
     *
     * @param ctx ChannelHandlerContext 上下文对象
     * @return 用户 id
     */
    private String getUserId(ChannelHandlerContext ctx) {
        AttributeKey<String> key = AttributeKey.valueOf("userId");
        return ctx.channel().attr(key).get();
    }

    /**
     * 移除与通道关联的用户 ID
     *
     * @param ctx ChannelHandlerContext 上下文对象
     */
    private void removeUserId(ChannelHandlerContext ctx) {
        String userId = this.getUserId(ctx);
        if (StringUtils.isNotBlank(userId)) {
            ChannelHolder.getChannelMap().remove(userId);
        }
    }
}