package com.leavis.lemon3.controller;

import com.leavis.lemon3.service.PushMsgService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: paynejlli
 * @Description: 消息推送控制器
 * @Date: 2024/9/4 09:54
 */
@Tag(name = "消息推送相关接口")
@RestController
@RequestMapping("/netty")
public class PushMsgController {

    @Autowired
    private PushMsgService pushMsgService;

    @GetMapping("/push/{uid}")
    public void pushMsgToOne(@PathVariable String uid, String msg) {
        pushMsgService.pushMsgToOne(uid, msg);
    }

    @GetMapping("/push/all")
    public void pushMsgToAll(String msg) {
        pushMsgService.pushMsgToAll(msg);
    }
}
