package com.leavis.lemon3.service.impl;

import com.leavis.lemon3.service.WsBusinessService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: paynejlli
 * @Description: 长连接业务逻辑
 * @Date: 2024/9/6 16:03
 */
@Slf4j
@Service
public class WsBusinessServiceImpl implements WsBusinessService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public String handleUserMsg(String userId, String msg) {
        log.info("handleUserMsg: userId={}, msg={}", userId, msg);
        LocalDateTime now = LocalDateTime.now();
        return String.format("%s 服务端收到来自于: %s 的消息, 消息内容为: %s", DATE_TIME_FORMATTER.format(now), userId,
                msg);
    }
}